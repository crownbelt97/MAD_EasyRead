package sg.edu.np.mad.mad_easyread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.content.Intent;


public class loginActivity extends AppCompatActivity {

    private Button signInBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        signInBtn = findViewById(R.id.signInBtn);

        signInBtn.setOnClickListener(view -> {
            Intent homeActivity = new Intent(loginActivity.this, HomeActivity.class);
            startActivity(homeActivity);
        });


    }
}