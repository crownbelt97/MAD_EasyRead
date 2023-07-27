package sg.edu.np.mad.easyread;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TopchartFragment extends Fragment implements SelectListener{

    private ArrayList<News> bookArrayList;
    private String[] bookHeading;
    private int[] imageResourceID;
    private RecyclerView recyclerView;

    private ArrayList<BookDetails> tc_Detailed_List;

    List<Book> bookList = new ArrayList<Book>();


    public TopchartFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_topchart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tc_Detailed_List = new ArrayList<BookDetails>();

        bookArrayList = new ArrayList<>();



        String url = "https://api.nytimes.com/svc/books/v3/lists/current/hardcover-fiction.json?api-key=y6og1zAOyVjSobnuULUpQwC4ivOlVQ0u";

        //The adapter is responsible for binding the data to the RecyclerView and creating the necessary views for each item
        MyAdapter myAdapter = new MyAdapter(getContext(), bookArrayList, this);

        //JsonObjectRequest is a part of the Volley library and is used to send a network request with a JSON payload and receive a JSON response from a server
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

            //Callback that is invoked when a network request is successful and receives a response from the server
            @Override
            public void onResponse(JSONObject response) {
                //Try and catch blocks part of volley library as part of exception handling
                try {
                    //"array" stores the top15 books from the NYTimes top books list
                    JSONObject obj = response.getJSONObject("results");
                    JSONArray array = obj.getJSONArray("books");

                    //Loop to iterate through the books array to get the respective data
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);

                        // Getting the title string and book_image link (that will be displayed through the picasso library)
                        String title = o.getString("title");
                        String link = o.getString("book_image");

                        //Accessing and getting the isbn13 of the book
                        JSONArray a = o.getJSONArray("isbns");
                        JSONObject isbns = a.getJSONObject(0);
                        String isbn = isbns.getString("isbn13");

                        //Getting the authors from the book but storing it in an array to match
                        // the class asNYTimes api stores the authors in a single string.
                        String authors = o.getString("author");
                        String[] authorArray = {authors};

                        //Getting the rank of the book but changing the type to double to match the class
                        double rank = o.getInt("rank");

                        //Creating BookDetails objects to put into ArrayList tc_Detailed_List that will be used later on to create 'News' objects for the recyclerview
                        BookDetails bookDetails = new BookDetails(title, authorArray, link, null, rank, null, 0, null, null, null);
                        tc_Detailed_List.add(bookDetails);
                        Book book = new Book( title, link, isbn );
                        bookList.add(book);

                    }

                    //For loop to iterate through the tc_Detailed_List of BookDetails objects to create 'News' objects
                    //which will be added to the bookArrayList which will be used to display into the recyclerview
                    for (int i = 0; i < tc_Detailed_List.size(); i++) {
                        int rank = (int) Math.round(tc_Detailed_List.get(i).getRating());
                        String rankDisplay = "Rank #" + Integer.toString(rank);
                        String authorDisplay = "By " + tc_Detailed_List.get(i).getAuthor(0);
                        News news = new News(tc_Detailed_List.get(i).getTitle(), tc_Detailed_List.get(i).getBook_Image(), authorDisplay, rankDisplay);
                        bookArrayList.add(news);
                    }

                    //Assigns the RecyclerView widget to the recyclerView variable
                    recyclerView = view.findViewById(R.id.recyclerviewTopChart);
                    //Creates a new instance of LinearLayoutManager and assigns it as the layout manager for the recyclerView
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    //Set a fixed size for the RecyclerView
                    recyclerView.setHasFixedSize(true);
                    //Sets the created MyAdapter as the adapter for the recyclerView
                    recyclerView.setAdapter(myAdapter);
                    //Notifies the adapter that the underlying data has changed, triggering a refresh of the RecyclerView to reflect any updates made to the data
                    myAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Error listener as part of the volley library necessities
        }, new Response.ErrorListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        //Adds the request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(jsonObjectRequest);


    }

    @Override
    public void onItemClicked(int pos) {
        Log.d("position" , "true");
        int rank = pos + 1;
        String details_link = bookList.get(pos).getDetails_Link();
        String image_link = bookList.get(pos).getBook_Image();
        System.out.println(details_link);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("details_link", details_link);
        editor.putString("image_link", image_link);
        editor.putInt("rank", rank);
        editor.apply();
        ((MainActivity)getActivity()).replaceFragment(new DetailsFragment());
    }


//    private void dataInitialize() {
//
//
//
//        bookArrayList = new ArrayList<>();
//
//        bookHeading = new String[]{
//                getString(R.string.head_1),
//                getString(R.string.head_2),
//                getString(R.string.head_3),
//                getString(R.string.head_4),
//                getString(R.string.head_5),
//                getString(R.string.head_6),
//                getString(R.string.head_7),
//                getString(R.string.head_8),
//                getString(R.string.head_9),
//                getString(R.string.head_10),
//        };
//
//        imageResourceID = new int[] {
//                R.drawable.baseline_android_24,
//                R.drawable.baseline_android_24,
//                R.drawable.baseline_android_24,
//                R.drawable.baseline_android_24,
//                R.drawable.baseline_android_24,
//                R.drawable.baseline_android_24,
//                R.drawable.baseline_android_24,
//                R.drawable.baseline_android_24,
//                R.drawable.baseline_android_24,
//                R.drawable.baseline_android_24,
//        };
//
//        for(int i = 0; i< bookHeading.length; i++) {
//            News news = new News(bookHeading[i], imageResourceID[i], "author", "rank");
//            bookArrayList.add(news);
//        }
//    }
}