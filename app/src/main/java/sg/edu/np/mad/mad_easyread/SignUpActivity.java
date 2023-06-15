package sg.edu.np.mad.mad_easyread;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private TextView linkSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);


        linkSignIn = findViewById(R.id.linkToSignIn);

        linkSignIn.setOnClickListener(view -> {
            Intent linkSignInActivity = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(linkSignInActivity);
        });



    }

}