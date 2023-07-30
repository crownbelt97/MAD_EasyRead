package sg.edu.np.mad.easyread;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    DatabaseReference DBreference;
    FirebaseAuth mAuth;
    TextView usernameTextView;
    EditText emailEditText;
    TextView passReset;

    Button updateBtn;
    Button logoutBtn;

    FirebaseUser currentUser;
    Button editProfileBtn;

    FirebaseDatabase database;
    DatabaseReference reference;

    TextView followingCount;

    TextView followersCount;

    TextView addfriendBtn;

    Switch notificationBtn;

    ImageView profileImage;

    public ProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameTextView = view.findViewById(R.id.usernameTextView);
        editProfileBtn = view.findViewById(R.id.EditProfileBtn);
        logoutBtn = view.findViewById(R.id.logOutBtn);
        addfriendBtn = view.findViewById(R.id.addfriendBtn);
        followingCount = view.findViewById(R.id.followingCount);
        followersCount = view.findViewById(R.id.ProfileFollowertextView);
        notificationBtn = view.findViewById(R.id.notificationBtn);
        profileImage = view.findViewById(R.id.profileImage);

        mAuth = FirebaseAuth.getInstance();

        reference = FirebaseDatabase.getInstance().getReference("users");
        currentUser = mAuth.getCurrentUser();
        String targetId = currentUser.getUid();


        if (currentUser != null) {
            String targetEmail = currentUser.getEmail();

            reference.orderByChild("email").equalTo(targetEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String usernameDB = String.valueOf(userSnapshot.child("username").getValue(String.class));
                            String followingCountDB = String.valueOf(userSnapshot.child("followingCount").getValue(long.class));
                            String followersCountDB = String.valueOf(userSnapshot.child("followersCount").getValue(long.class));
                            String imageUrl = String.valueOf(userSnapshot.child("profileImageURL").getValue(String.class));
                            usernameTextView.setText(usernameDB);
                            if (followingCountDB == "null"){
                                followingCount.setText("0");
                            }
                            else {
                                followingCount.setText(followingCountDB);
                            }
                            if (followersCountDB == "null"){
                                followersCount.setText("0");
                            }
                            else {
                                followersCount.setText(followersCountDB);
                            }

                            if (imageUrl != null && !imageUrl.isEmpty()) {

                                Glide.with(requireContext())
                                        .load(imageUrl)
                                        .placeholder(R.drawable.profile_icon)
                                        .into(profileImage);
                            }

                            break;

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        SharedPreferences sharedPref = getActivity().getSharedPreferences("sg.edu.np.mad.easyread", Context.MODE_PRIVATE);
        boolean isNotificationEnabled = sharedPref.getBoolean("notification_setting", true);

        reference.child(currentUser.getUid()).child("notification_setting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean notificationEnabled = dataSnapshot.getValue(Boolean.class);
                    notificationBtn.setChecked(notificationEnabled);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if necessary
            }
        });
        editProfileBtn.setOnClickListener(v -> {
            Intent editIntent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(editIntent);
        });

        addfriendBtn.setOnClickListener(v -> {
            Intent addFriendIntent = new Intent(getActivity(), AddFriendsActivity.class);
            startActivity(addFriendIntent);
        });


        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();

            Intent logoutIntent = new Intent(getActivity(), WelcomeActivity.class);
            startActivity(logoutIntent);
            getActivity().finish();

        });



        notificationBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // Get the current user ID
                String targetId = currentUser.getUid();
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("notification_setting", isChecked);
                editor.apply();
                // Update the "notification" field in the database based on the switch state
                reference.child(targetId).child("notification_setting").setValue(isChecked)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // If the data update is successful, you can display a Toast message
                                if (isChecked) {
                                    Toast.makeText(getContext(), "Notifications turned ON", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Notifications turned OFF", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // If there is an error in updating the data, you can show an error message
                                Toast.makeText(getContext(), "Failed to update notification status", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return view;


    }

}