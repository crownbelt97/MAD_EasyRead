package sg.edu.np.mad.mad_easyread;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import android.widget.Toast;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Button signInBtn;
    private TextView linkSignUp;

    private EditText emailOrUsernameField;
    private EditText passwordField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        emailOrUsernameField = findViewById(R.id.emailOrUsernameField);
        passwordField = findViewById(R.id.passwordField);

        signInBtn = findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(view -> {
            SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();
            String userListJson = mPrefs.getString("UserLists", "");
            List<User> registeredUser = gson.fromJson(userListJson, List.class);

            String enteredEmailOrUsername = emailOrUsernameField.getText().toString();
            String enteredPasswordField = passwordField.getText().toString();

            User foundedUser = registeredUser.stream().filter(customer -> (customer.getEmail().equals(enteredEmailOrUsername) || customer.getUsername().equals(enteredEmailOrUsername))
                    && customer.getPassword().equals(enteredPasswordField)).findFirst().orElse(null);

            if(foundedUser != null) {
                prefsEditor.putBoolean("UserLoggedIn", true).commit();
                Intent homeActivity = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(homeActivity);
            } else {
                // Logic to show error message here (Dialog)
                Toast.makeText(getApplicationContext(), "Invalid Username Email or Password", Toast.LENGTH_SHORT).show();

            }
        });


        linkSignUp = findViewById(R.id.linkToSignUp);
        linkSignUp.setOnClickListener(view -> {
            Intent linkSignUpActivity = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(linkSignUpActivity);
        });
    }
}