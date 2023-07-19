package sg.edu.np.mad.easyread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        String targetUsername = intent.getStringExtra("username");

        // Set the username to the TextView
        profileUsernameTextView.setText(targetUsername);

        // Check if the current user is already following the target user
        reference.child(currentUserId).child("following").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isFollowing = dataSnapshot.hasChild(targetUsername);
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
                followUser(currentUserId, targetUsername);
            } else {
                unfollowUser(currentUserId, targetUsername);
            }
        });

        // Initialize the DatabaseReference objects
        followersReference = reference.child(targetUsername).child("followers");
        followingReference = reference.child(targetUsername).child("following");

        // Set up the listeners to update the follower and following counts in real-time
        followersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Update the follower count when data changes
                long followerCount = dataSnapshot.getChildrenCount();
                profileFollowersTextView.setText(String.valueOf(followerCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if necessary
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
                // Handle database error if necessary
            }
        });
    }

    // Method to follow a user
    private void followUser(String currentUserId, String targetUsername) {
        reference.child(currentUserId).child("following").child(targetUsername).setValue(true);
        reference.child(targetUsername).child("followers").child(currentUserId).setValue(true);

        // Create a new value "following" for the current user in their data
        reference.child(currentUserId).child("following").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // If the current user's "following" value exists in the database
                if (dataSnapshot.exists()) {
                    long followingCount = dataSnapshot.getChildrenCount();
                    // Increment the following count by 1 and update the value in the database
                    reference.child(currentUserId).child("followingCount").setValue(followingCount + 1);
                } else {
                    // If the "following" value doesn't exist, set it to 1
                    reference.child(currentUserId).child("followingCount").setValue(1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error if necessary
            }
        });

        followBtn.setText("Unfollow");
        Toast.makeText(this, "You are now following " + targetUsername, Toast.LENGTH_SHORT).show();
    }

    // Method to unfollow a user
    private void unfollowUser(String currentUserId, String targetUsername) {
        reference.child(currentUserId).child("following").child(targetUsername).removeValue();
        reference.child(targetUsername).child("followers").child(currentUserId).removeValue();

        // Decrement the following count by 1 and update the value in the database
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

        followBtn.setText("Follow");
        Toast.makeText(this, "You have unfollowed " + targetUsername, Toast.LENGTH_SHORT).show();
    }
}
