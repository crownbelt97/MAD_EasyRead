package sg.edu.np.mad.easyread;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sg.edu.np.mad.easyread.R;

public class ProfileFragment extends Fragment {

    DatabaseReference reference;
    FirebaseAuth mAuth;
    EditText usernameEditText;
    EditText emailEditText;
    TextView passReset;

    Button updateBtn;
    Button logoutBtn;

    TextView addfriendBtn;


    public ProfileFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
//        View rootView = inflater.inflate(R.layout.fragment_addfriends, container, false);
        addfriendBtn = view.findViewById(R.id.addfriendBtn);
        addfriendBtn.setOnClickListener(v -> {
            Intent addFriendIntent = new Intent(getActivity(), AddFriendsActivity.class);
            startActivity(addFriendIntent);
        });
        return view;



    }


}