package sg.edu.np.mad.easyread;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import sg.edu.np.mad.easyread.R;

public class ProfileFragment extends Fragment {
    FirebaseAuth mAuth;
    EditText usernameEditText;
    EditText emailEditText;
    TextView passReset;

    Button updateBtn;
    Button logoutBtn;


    public ProfileFragment(){

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

        usernameEditText = view.findViewById(R.id.usernameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        passReset = view.findViewById(R.id.passReset);

        logoutBtn = view.findViewById(R.id.logout);
        updateBtn = view.findViewById(R.id.updateBtn);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserName = currentUser.getDisplayName();

        usernameEditText.setText(currentUserName);
        emailEditText.setText(currentUser.getEmail());
        emailEditText.setEnabled(false);


        passReset.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();

            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Password reset email sent!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                }
            });
        });


        updateBtn.setOnClickListener(v -> {

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(String.valueOf(usernameEditText.getText()))
                    .build();

            currentUser.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Update profile success!", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                }
            });


        });

        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Intent welcomeIntent = new Intent(getActivity(), WelcomeActivity.class);
            startActivity(welcomeIntent);
            getActivity().finish();
        });

        return view;



    }
}