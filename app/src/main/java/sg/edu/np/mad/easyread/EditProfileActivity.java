package sg.edu.np.mad.easyread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

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
                            updateUsername.setText(usernameDB);
                            updateEmail.setText(emailDB);


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






    }
}