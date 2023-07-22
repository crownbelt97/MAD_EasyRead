package sg.edu.np.mad.easyread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
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
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_friends);

        allUsernames = new ArrayList<>();
        displayedUsernames = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayedUsernames);
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setVisibility(ListView.GONE); // Hide the ListView by default

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

                // We do not update the ListView here.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error if necessary
            }
        });



        // Set item click listener for the ListView
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedUsername = displayedUsernames.get(position);
            // Handle navigation to the user's profile activity here
            navigateToUserProfile(selectedUsername);
        });
    }

    private void filterUsernames(String query) {
        displayedUsernames.clear();

        if (query.isEmpty()) {
            // Clear the search query, so we hide the ListView again
            listView.setVisibility(ListView.GONE);
        } else {
            // Filter the usernames based on the search query
            for (String username : allUsernames) {
                if (username.toLowerCase().contains(query.toLowerCase())) {
                    displayedUsernames.add(username);
                }
            }

            adapter.notifyDataSetChanged();

            // Show the ListView since there are filtered results
            listView.setVisibility(ListView.VISIBLE);
        }
    }

    private void navigateToUserProfile(String userId) {
        // Navigate to the user's profile activity based on the selected username
        // Replace UserProfileActivity.class with the actual name of the user's profile activity.
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }
}
