package sg.edu.np.mad.easyread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    FirebaseUser currentUser;

    EditText updateUsername;

    EditText updateEmail;

    EditText joinDate;

    Button updateBtn;

    TextView passReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_profile);

        updateUsername = findViewById(R.id.updateUsernameEditText);
        updateEmail = findViewById(R.id.updateEmailEditText);
        joinDate = findViewById(R.id.joinDate);
        updateBtn = findViewById(R.id.updateBtn);
        passReset = findViewById(R.id.passReset);

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
                            String emailDB = String.valueOf(userSnapshot.child("email").getValue(String.class));
                            String creationDateDB = String.valueOf(userSnapshot.child("creationDate").getValue(String.class));
                            updateUsername.setText(usernameDB);
                            updateEmail.setText(emailDB);
                            joinDate.setText(creationDateDB);

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


        passReset.setOnClickListener(v -> {
            String email = updateEmail.getText().toString();

            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Password reset email sent!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                }
            });
        });




        updateBtn.setOnClickListener(view -> {
            String newUsername = updateUsername.getText().toString().trim();
            if (!TextUtils.isEmpty(newUsername)) {
                DatabaseReference userRef = reference.child(currentUser.getUid());
                userRef.child("username").setValue(newUsername)
                        .addOnSuccessListener(aVoid -> {
                            // Username update successful
                            Toast.makeText(EditProfileActivity.this, "Username updated successfully!", Toast.LENGTH_SHORT).show();

                        })
                        .addOnFailureListener(e -> {
                            // Failed to update the username
                            Toast.makeText(EditProfileActivity.this, "Failed to update username.", Toast.LENGTH_SHORT).show();
                        });
                Intent updateIntent = new Intent(EditProfileActivity.this, MainActivity.class);
                startActivity(updateIntent);

        } else {
                Toast.makeText(EditProfileActivity.this, "Please enter a new username.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}