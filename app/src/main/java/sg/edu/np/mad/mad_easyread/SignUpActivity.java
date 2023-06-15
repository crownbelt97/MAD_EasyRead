package sg.edu.np.mad.mad_easyread;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class SignUpActivity extends AppCompatActivity {

    private Button signUpBtn;

    private TextView linkSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);


        linkSignIn = findViewById(R.id.linkToSignIn);

        EditText emailField = findViewById(R.id.emailField);
        EditText usernameField = findViewById(R.id.usernameField);
        EditText passwordField = findViewById(R.id.passwordField);

        linkSignIn.setOnClickListener(view -> {
            Intent linkSignInActivity = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(linkSignInActivity);
        });

        signUpBtn = findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(view -> {
            SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();

            String enteredEmail = emailField.getText().toString();
            String enteredUsername = usernameField.getText().toString();
            String enteredPassword = passwordField.getText().toString();

            User newUser = new User(enteredUsername, enteredEmail, enteredPassword);

            if(mPrefs.contains("UserLists")) {
                String userListJson = mPrefs.getString("UserLists", "");
                List<User> registeredUser = gson.fromJson(userListJson, List.class);

                registeredUser.add(newUser);
                String json = gson.toJson(registeredUser);
                prefsEditor.putString("UserLists", json).commit();
            } else {
                List<User> registeredUser = new ArrayList();

                registeredUser.add(newUser);
                String json = gson.toJson(registeredUser);
                prefsEditor.putString("UserLists", json).commit();
            }

            // Need to change to LoginActivity
            Intent homeActivity = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(homeActivity);
        });

    }

}