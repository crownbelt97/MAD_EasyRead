package sg.edu.np.mad.mad_easyread;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SIgnUpActivity extends AppCompatActivity {

    private TextView linkSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        linkSignIn = findViewById(R.id.linkToSignIn);

        linkSignIn.setOnClickListener(view -> {
            Intent linkSignInActivity = new Intent(SIgnUpActivity.this, loginActivity.class);
            startActivity(linkSignInActivity);
        });



    }

}