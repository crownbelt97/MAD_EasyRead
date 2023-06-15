package sg.edu.np.mad.mad_easyread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;



public class LoginActivity extends AppCompatActivity {

    private Button signInBtn;
    private TextView linkSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        signInBtn = findViewById(R.id.signUpBtn);

        signInBtn.setOnClickListener(view -> {
            Intent homeActivity = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(homeActivity);
        });

        linkSignUp = findViewById(R.id.linkToSignUp);
        linkSignUp.setOnClickListener(view -> {
            Intent linkSignUpActivity = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(linkSignUpActivity);
        });






    }
}