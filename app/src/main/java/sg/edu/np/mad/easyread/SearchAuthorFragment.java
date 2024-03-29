package sg.edu.np.mad.easyread;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchAuthorFragment extends Fragment implements SelectListener{

    private ArrayList<News> bookArrayList;
    private String[] bookHeading;
    private int[] imageResourceID;
    private SearchView searchView;
    private RecyclerView recyclerView;

    private ArrayList<BookDetails> tc_Detailed_List;

    private ArrayList<Book> book_List = new ArrayList<>();


    public SearchAuthorFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_author, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView = view.findViewById(R.id.search);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBooks(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        view.findViewById(R.id.imageView17).setOnClickListener(v -> {
            ((MainActivity)getActivity()).replaceFragment(new HomeFragment());
        });

        view.findViewById(R.id.swap_Button).setOnClickListener(v -> {
            ((MainActivity)getActivity()).replaceFragment(new SearchFragment());
        });

    }

    private void searchBooks(String query){
        tc_Detailed_List = new ArrayList<BookDetails>();

        bookArrayList = new ArrayList<>();

        View view = getView();

        String url = "https://www.googleapis.com/books/v1/volumes?q=inauthor:" + query;
        Log.d("url", url);

        //The adapter is responsible for binding the data to the RecyclerView and creating the necessary views for each item
        MyAdapter myAdapter = new MyAdapter(getContext(), bookArrayList , this);

        //JsonObjectRequest is a part of the Volley library and is used to send a network request with a JSON payload and receive a JSON response from a server
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

            //Callback that is invoked when a network request is successful and receives a response from the server
            @Override
            public void onResponse(JSONObject response) {
                //Try and catch blocks part of volley library as part of exception handling
                try {
                    //"array" stores the top15 books from the NYTimes top books list
                    JSONArray array = response.getJSONArray("items");

                    //Loop to iterate through the books array to get the respective data
                    for (int i = 0; i < array.length(); i++) {
                        try {
                            JSONObject o = array.getJSONObject(i);

                            // Getting the title string and book_image link (that will be displayed through the picasso library)
                            JSONObject obj = o.getJSONObject("volumeInfo");
                            String title = obj.getString("title");
                            JSONObject links = obj.getJSONObject("imageLinks");
                            String img = links.getString("thumbnail");
                            img = img.replace("http://","https://");

                            //Accessing and getting the isbn13 of the book
                            String selfLink = o.getString("selfLink");

                            //Getting the authors from the book but storing it in an array to match
                            // the class asNYTimes api stores the authors in a single string.
                            JSONArray authorArray = obj.getJSONArray("authors");
                            String[] stringArray = null;
                            if (authorArray != null) {
                                int length = authorArray.length();
                                stringArray = new String[length];
                                for (int x = 0; x < length; x++) {
                                    stringArray[x] = authorArray.optString(x);
                                }
                            }

                            //Getting the rank of the book but changing the type to double to match the class

                            //Creating BookDetails objects to put into ArrayList tc_Detailed_List that will be used later on to create 'News' objects for the recyclerview
                            BookDetails bookDetails = new BookDetails(title, stringArray, img, null, 0, null, 0, null, null, null);
                            Book book = new Book(title,img,selfLink);
                            book_List.add(book);
                            tc_Detailed_List.add(bookDetails);
                        } catch (Exception e2)
                        {
                            System.out.println(e2);
                        }

                    }

                    //For loop to iterate through the tc_Detailed_List of BookDetails objects to create 'News' objects
                    //which will be added to the bookArrayList which will be used to display into the recyclerview
                    if (tc_Detailed_List.size() <= 10) {
                        for (int i = 0; i < tc_Detailed_List.size(); i++) {

                            String authorDisplay = "By " + tc_Detailed_List.get(i).getAuthor(0);
                            News news = new News(tc_Detailed_List.get(i).getTitle(), tc_Detailed_List.get(i).getBook_Image(), authorDisplay, null);
                            bookArrayList.add(news);
                        }
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
    public void onViewCreated(@NonNull View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }

    @Override
    public void onItemClicked(int pos) {

        Log.d("position" , "true");
        int rank = 0;
        String details_link = book_List.get(pos).getDetails_Link();
        String image_link = book_List.get(pos).getBook_Image();
        String title = book_List.get(pos).getTitle();
        System.out.println(details_link);
        Log.d("details_link",details_link);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (!details_link.contains("https")){
            for ( int i = 0; i < 15; i ++){
                String book_data = sharedPref.getString("Book" + i, "empty");
                String[] input_data = book_data.split("!-!-!");
                Log.d("inputdata[2]",input_data[2]);
                if (input_data[2].toLowerCase().contains(details_link.toLowerCase())){
                    rank = i + 1;
                    Log.d("rnk_change","true");

                }
            }
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("details_link", details_link);
        editor.putString("image_link", image_link);
        editor.putInt("rank", rank);
        editor.apply();
        ((MainActivity)getActivity()).replaceFragment(new DetailsFragment());
    }
}