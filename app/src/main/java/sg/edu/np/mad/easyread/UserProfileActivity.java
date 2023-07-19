package sg.edu.np.mad.easyread;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {

    TextView profileUsernameTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_profile);

        profileUsernameTextView = findViewById(R.id.profileUsernameTextView);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        // Set the username to the TextView
        profileUsernameTextView.setText(username);



    }
}