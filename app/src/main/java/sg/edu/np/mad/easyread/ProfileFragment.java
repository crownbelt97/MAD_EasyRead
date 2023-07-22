package sg.edu.np.mad.easyread;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

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


    TextView addfriendBtn;


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

        mAuth = FirebaseAuth.getInstance();

        reference = FirebaseDatabase.getInstance().getReference("users");
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String targetEmail = currentUser.getEmail();

            reference.orderByChild("email").equalTo(targetEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String usernameDB = String.valueOf(userSnapshot.child("username").getValue(String.class));
                            String followingCountDB = String.valueOf(userSnapshot.child("followingCount").getValue(long.class));
                            usernameTextView.setText(usernameDB);
                            if (followingCountDB == "null"){
                                followingCount.setText("0");
                            }
                            else {
                                followingCount.setText(followingCountDB);
                            }
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error if necessary
                }
            });
        }

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

        return view;


    }


}