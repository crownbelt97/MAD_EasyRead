package sg.edu.np.mad.easyread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.mad.easyread.R;

public class AddFriendsActivity extends AppCompatActivity {

    private ArrayList<String> allUsernames;
    private ArrayList<String> displayedUsernames;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_friends);

        ListView listView = findViewById(R.id.listView);
        allUsernames = new ArrayList<>();
        displayedUsernames = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayedUsernames);
        listView.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Not needed, but required to implement
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the usernames based on the search query
                filterUsernames(newText);
                return true;
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allUsernames.clear();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String usernameDB = String.valueOf(userSnapshot.child("username").getValue(String.class));
                    allUsernames.add(usernameDB);
                }

                // Update the ListView with the new data
                filterUsernames(""); // Initial filter to display an empty list
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error if necessary
            }
        });
    }

    private void filterUsernames(String query) {
        displayedUsernames.clear();

        for (String username : allUsernames) {
            if (username.toLowerCase().contains(query.toLowerCase())) {
                displayedUsernames.add(username);
            }
        }

        adapter.notifyDataSetChanged();
    }
}

