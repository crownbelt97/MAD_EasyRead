package sg.edu.np.mad.mad_easyread;


import android.content.Intent;
import android.os.Bundle;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button signInBtn;
    private TextView linkSignUp;

    FirebaseAuth mAuth;
    private EditText emailField;
    private EditText passwordField;
    private TextView forgotPass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.loginEmailField);
        passwordField = findViewById(R.id.passwordField);
        forgotPass = findViewById(R.id.forgotPassword);

        signInBtn = findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(v -> {
            String email, password;
            email = String.valueOf(emailField.getText());
            password = String.valueOf(passwordField.getText());

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(LoginActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Login Success.", Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(homeIntent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });


        forgotPass.setOnClickListener(v -> {
            String email = String.valueOf(emailField.getText());

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(LoginActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Sending password reset email...");
            progressDialog.show();

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Password reset email sent successfully
                            Toast.makeText(LoginActivity.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Failed to send password reset email
                            Toast.makeText(LoginActivity.this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        linkSignUp = findViewById(R.id.linkToSignUp);
        linkSignUp.setOnClickListener(view -> {
            Intent linkSignUpActivity = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(linkSignUpActivity);
        });
    }
}