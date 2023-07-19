package sg.edu.np.mad.easyread;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sg.edu.np.mad.easyread.R;

public class LoginActivity extends AppCompatActivity {
    private Button signInBtn;
    private TextView linkSignUp;
    FirebaseAuth mAuth;
    private EditText emailField;
    private EditText passwordField;
    private TextView forgotPass;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
                        }

                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        forgotPass.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter your email:");


            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setHint("Email Address");
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String text = input.getText().toString();

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog dialog = builder.create();

            // Prevent the dialog from being dismissed when the positive button is clicked
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String inputEmail = input.getText().toString();
                            // Process the entered text

                            // If necessary conditions are met, dismiss the dialog
                            if (inputEmail.isEmpty()) {
                                Toast.makeText(LoginActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                            } else {
                                ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                                progressDialog.setMessage("Sending password reset email...");
                                progressDialog.show();

                                mAuth.sendPasswordResetEmail(inputEmail)
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
                                dialog.dismiss();
                            }
                        }
                    });
                }
            });

            dialog.show();


        });


        linkSignUp = findViewById(R.id.linkToSignUp);
        linkSignUp.setOnClickListener(view -> {
            Intent linkSignUpActivity = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(linkSignUpActivity);
        });
    }
}