package sg.edu.np.mad.easyread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    Button followBtn;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    TextView profileUsernameTextView;
    TextView profileFollowingTextView;
    TextView profileFollowersTextView;

    private DatabaseReference followersReference;
    private DatabaseReference followingReference;
    private DatabaseReference notificationReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_profile);

        followBtn = findViewById(R.id.FollowProfileBtn);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        // Get the user ID of the current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserId = currentUser.getUid();



        profileUsernameTextView = findViewById(R.id.profileUsernameTextView);
        profileFollowingTextView = findViewById(R.id.ProfileFollowingtextView);
        profileFollowersTextView = findViewById(R.id.ProfileFollowertextView);

        Intent intent = getIntent();
        String targetUserId = intent.getStringExtra("userId");
        String targetUsername = intent.getStringExtra("username");
        DatabaseReference userProfileReference = reference.child(targetUserId);

        notificationReference = reference.child(targetUserId).child("notification_setting");
        notificationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean notificationEnabled = dataSnapshot.getValue(Boolean.class);
                    SharedPreferences sharedPref = getSharedPreferences("sg.edu.np.mad.easyread", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("notification_setting", notificationEnabled);
                    editor.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if necessary
            }
        });

        userProfileReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String usernameDB = String.valueOf(dataSnapshot.child("username").getValue(String.class));
                    profileUsernameTextView.setText(usernameDB);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error if necessary
            }
        });



        // Check if the current user is already following the target user
        reference.child(currentUserId).child("following").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isFollowing = dataSnapshot.hasChild(targetUserId);
                if (isFollowing) {
                    followBtn.setText("Unfollow");
                } else {
                    followBtn.setText("Follow");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error if necessary
            }
        });

        // Set an onClickListener for the follow button
        followBtn.setOnClickListener(view -> {
            if (followBtn.getText().equals("Follow")) {
                followUser(currentUserId, targetUserId, targetUsername);
            } else {
                unfollowUser(currentUserId, targetUserId, targetUsername);
            }
        });

        // Initialize the DatabaseReference objects
        followersReference = reference.child(targetUserId).child("followers");
        followingReference = reference.child(targetUserId).child("following");

        // Set up the listeners to update the follower and following counts in real-time
        followersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Update the follower count when data changes
                long followersCount = dataSnapshot.getChildrenCount();
                profileFollowersTextView.setText(String.valueOf(followersCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        followingReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Update the following count when data changes
                long followingCount = dataSnapshot.getChildrenCount();
                profileFollowingTextView.setText(String.valueOf(followingCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // Method to follow a user
    private void followUser(String currentUserId, String targetUserId, String targetUsername) {
        reference.child(currentUserId).child("following").child(targetUserId).setValue(true);
        reference.child(targetUserId).child("followers").child(currentUserId).setValue(true);

        SharedPreferences sharedPref = getSharedPreferences("sg.edu.np.mad.easyread", Context.MODE_PRIVATE);
        boolean notificationEnabled = sharedPref.getBoolean("notification_setting", true);


        // Set following in firebase realtime
        reference.child(currentUserId).child("following").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long followingCount = dataSnapshot.getChildrenCount();
                    reference.child(currentUserId).child("followingCount").setValue(followingCount);
                } else {
                    reference.child(currentUserId).child("followingCount").setValue(1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // Set follower in firebase realtime
        reference.child(targetUserId).child("followers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long followersCount = dataSnapshot.getChildrenCount();
                    reference.child(targetUserId).child("followersCount").setValue(followersCount);
                } else {
                    reference.child(targetUserId).child("followersCount").setValue(1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        followBtn.setText("Unfollow");
        Toast.makeText(this, "You are now following " + targetUsername, Toast.LENGTH_SHORT).show();

        if(notificationEnabled){
            sendFollowNotification(currentUserId);
        }

    }

    // Method to unfollow a user
    private void unfollowUser(String currentUserId, String targetUserId, String targetUsername) {
        reference.child(currentUserId).child("following").child(targetUserId).removeValue();
        reference.child(targetUserId).child("followers").child(currentUserId).removeValue();

        reference.child(currentUserId).child("followingCount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long followingCount = dataSnapshot.getValue(Long.class);
                    if (followingCount > 0) {
                        reference.child(currentUserId).child("followingCount").setValue(followingCount - 1);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error if necessary
            }
        });

        reference.child(targetUserId).child("followers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long followersCount = dataSnapshot.getValue(Long.class);
                    if (followersCount > 0) {
                        reference.child(targetUserId).child("followersCount").setValue(followersCount - 1);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error if necessary
            }
        });

        followBtn.setText("Follow");
        Toast.makeText(this, "You have unfollowed " + targetUsername, Toast.LENGTH_SHORT).show();
    }


    private void sendFollowNotification(String currentUserId) {

        DatabaseReference userProfileReference = reference.child(currentUserId);
        userProfileReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String currentUsernameDB = String.valueOf(dataSnapshot.child("username").getValue(String.class));
                    ShowFollowNotification(currentUsernameDB);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void ShowFollowNotification(String followerUsername) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "follow_channel_id";
            String channelName = "Easyread";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(UserProfileActivity.this, "follow_channel_id")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("You have a new follower!")
                .setContentText(followerUsername + " start following you")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show the notification
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(2, builder.build());


    }


}