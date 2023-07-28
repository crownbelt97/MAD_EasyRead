package sg.edu.np.mad.easyread;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class SettingsFragment extends Fragment implements SelectListener{

    private ArrayList<News> bookArrayList = new ArrayList<>();

    ArrayList<Book> favouriteList = new ArrayList<>();

    FirebaseDatabase secondaryDatabase = null;

    MyAdapter myAdapter = null;

    private RecyclerView recyclerView;

    ShimmerFrameLayout shimmerFrameLayout;



   public SettingsFragment(){

   }
   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       Log.d("onCreateView","true");
       //The adapter is responsible for binding the data to the RecyclerView and creating the necessary views for each item
       myAdapter = new MyAdapter(getContext(), bookArrayList , this);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view,
                                        Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shimmerFrameLayout=view.findViewById(R.id.recyclerShimmer);
        Log.d("viewcreated","true");

        try {
            Context mContext = requireActivity().getApplicationContext();
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setProjectId("mad-stuff-7f4ce")
                    .setApiKey("AIzaSyBbTx3tyIOCOinpF3Fqq09CZWOg2GGkohE")
                    .setApplicationId("1:446547976657:android:29448a2c63ea08769000f0")
                    .setDatabaseUrl("https://mad-stuff-7f4ce-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .build();
            FirebaseApp.initializeApp(mContext, options, "favourites");
            Log.d("iterations", "true");
        } catch (Exception e)
        {
            System.out.println(e);
        }
        FirebaseApp app = FirebaseApp.getInstance("favourites");
        secondaryDatabase = FirebaseDatabase.getInstance(app);
        DatabaseReference myRef = secondaryDatabase.getReference("results");
        Query myQueryRef = myRef.child("02");

        myQueryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // TODO add if statment to check identity
                Log.d("database access","true");
                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                    Log.d("ISBN_Number",userSnapshot.getValue().toString());
                    String image = userSnapshot.child("book_Image").getValue().toString();
                    String details_link = Objects.requireNonNull(userSnapshot.child("details_Link").getValue()).toString();
                    String title = Objects.requireNonNull(userSnapshot.child("title").getValue()).toString();
                    Log.d("data_bookimage", image);
                    Log.d("data_details_link", details_link);
                    Log.d("data_title", title);
                    //BookDetails bookDetails = new BookDetails(title, null, image, null, 0, null, 0, null, null, null);
                    Book book = new Book(title,image,details_link);
                    Log.d("book",book.getBook_Image());
                    favouriteList.add(book);
                }
                for (int i = 0; i < favouriteList.size(); i++) {

                    //String authorDisplay = "By " + favourites_Detailed_List.get(i).getAuthor(0);
                    News news = new News(favouriteList.get(i).getTitle(), favouriteList.get(i).getBook_Image(),null, null);
                    Log.d("i" , String.valueOf(i));
                    bookArrayList.add(news);
                }

                //Assigns the RecyclerView widget to the recyclerView variable
                recyclerView = view.findViewById(R.id.recyclerviewFavourites);
                //Creates a new instance of LinearLayoutManager and assigns it as the layout manager for the recyclerView
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                //Set a fixed size for the RecyclerView
                recyclerView.setHasFixedSize(true);
                //Sets the created MyAdapter as the adapter for the recyclerView
                recyclerView.setAdapter(myAdapter);
                //Notifies the adapter that the underlying data has changed, triggering a refresh of the RecyclerView to reflect any updates made to the data
                myAdapter.notifyDataSetChanged();

                Log.d("view","true");
                shimmerFrameLayout.stopShimmer();
                RecyclerView rv = view.findViewById(R.id.recyclerviewFavourites);
                rv.setVisibility(View.VISIBLE);
                shimmerFrameLayout.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }); /*{
            @Override
            public void onData(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                    Log.d("ISBN_Number",userSnapshot.getValue().toString());
                    String image = userSnapshot.child("book_Image").getValue().toString();
                    String details_link = Objects.requireNonNull(userSnapshot.child("details_Link").getValue()).toString();
                    String title = Objects.requireNonNull(userSnapshot.child("title").getValue()).toString();
                    Log.d("data_bookimage", image);
                    Log.d("data_details_link", details_link);
                    Log.d("data_title", title);
                    BookDetails bookDetails = new BookDetails(title, null, image, null, 0, null, 0, null, null, null);
                    favourites_Detailed_List.add(bookDetails);
                    for (int i = 0; i < favourites_Detailed_List.size(); i++) {

                        //String authorDisplay = "By " + favourites_Detailed_List.get(i).getAuthor(0);
                        News news = new News(favourites_Detailed_List.get(i).getTitle(), favourites_Detailed_List.get(i).getBook_Image(),null, null);
                        Log.d("i" , String.valueOf(i));
                        bookArrayList.add(news);
                    }

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                    String image = Objects.requireNonNull(userSnapshot.child("book_Image").getValue()).toString();
                    String details_link = Objects.requireNonNull(userSnapshot.child("details_Link").getValue()).toString();
                    String title = Objects.requireNonNull(userSnapshot.child("title").getValue()).toString();
                    Log.d("data_bookimage" , image);
                    Log.d("data_details_link" , details_link);
                    Log.d("data_title" , title);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {}


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


    }

    @Override
    public void onViewCreated(@NonNull View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }

    @Override
    public void onItemClicked(int pos) {

    }
}