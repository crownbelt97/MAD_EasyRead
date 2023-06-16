package sg.edu.np.mad.mad_easyread;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
public class WelcomeActivity extends AppCompatActivity {

    private Button homeBtn;
    private Button welcomeLogin;
    private Button welcomeSignup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome);


        homeBtn = findViewById(R.id.linkHome);
        welcomeLogin = findViewById(R.id.WelcomeLogin);
        welcomeSignup = findViewById(R.id.WelcomeSignUp);

        homeBtn.setOnClickListener(view -> {
            Intent homeActivity = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(homeActivity);
        });

        welcomeLogin.setOnClickListener(view -> {
            Intent loginActivity = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(loginActivity);
        });

        welcomeSignup.setOnClickListener(view -> {
            Intent signupActivity = new Intent(WelcomeActivity.this, SignUpActivity.class);
            startActivity(signupActivity);
        });
    }
}