package sg.edu.np.mad.easyread;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;

import sg.edu.np.mad.easyread.R;


public class WelcomeActivity extends AppCompatActivity {


    private Button homeBtn;
    private Button welcomeLogin;
    private Button welcomeSignup;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Intent homeIntent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(homeIntent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome);


        welcomeLogin = findViewById(R.id.WelcomeLogin);
        welcomeSignup = findViewById(R.id.WelcomeSignUp);



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