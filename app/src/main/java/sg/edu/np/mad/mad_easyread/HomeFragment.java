package sg.edu.np.mad.mad_easyread;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {
    private Button logoutBtn;
    FirebaseAuth mAuth;

    public HomeFragment()
    {

    }

    @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
     }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        logoutBtn = view.findViewById(R.id.logoutBtn);

        mAuth = FirebaseAuth.getInstance();


        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Intent welcomeIntent = new Intent(getActivity(), WelcomeActivity.class);
            startActivity(welcomeIntent);
            getActivity().finish();
        });

        return view;


    }









}