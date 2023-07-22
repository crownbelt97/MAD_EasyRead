package sg.edu.np.mad.easyread;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SignUpActivity extends AppCompatActivity {

    private Button signUpBtn;
    FirebaseAuth mAuth;
    private TextView linkSignIn;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);


        linkSignIn = findViewById(R.id.linkToSignIn);
        mAuth = FirebaseAuth.getInstance();
        EditText usernameField = findViewById(R.id.usernameField);
        EditText emailField = findViewById(R.id.emailField);
        EditText passwordField = findViewById(R.id.passwordField);
        signUpBtn = findViewById(R.id.signUpBtn);

        linkSignIn.setOnClickListener(view -> {
            Intent linkSignInActivity = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(linkSignInActivity);
        });


        signUpBtn.setOnClickListener(v ->{
            String username, email, password;
            username = String.valueOf(usernameField.getText());
            email = String.valueOf(emailField.getText());
            password = String.valueOf(passwordField.getText());

            if(TextUtils.isEmpty(username)){
                Toast.makeText(SignUpActivity.this, "Enter username", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(email)){
                Toast.makeText(SignUpActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(password)){
                Toast.makeText(SignUpActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignUpActivity.this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser Firebaseuser = mAuth.getCurrentUser();

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String currentDate = sdf.format(new Date());

                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignUpActivity.this, "Sign Up Success, Please login", Toast.LENGTH_SHORT).show();
                            Intent loginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(loginIntent);



                            database = FirebaseDatabase.getInstance();
                            reference = database.getReference("users");
                            User user = new User(username, email, "", Firebaseuser.getUid());
                            user.setCreationDate(currentDate); // Set the creation date
                            reference.child(user.getUserId()).setValue(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Sign Up failed.", Toast.LENGTH_SHORT).show();
                        }
                    });


            });
    }


}