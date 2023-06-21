package sg.edu.np.mad.mad_easyread;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

        // Layout inflater that instantiates a layout XML file into its corresponding View objects
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        logoutBtn = view.findViewById(R.id.logoutBtn);

        mAuth = FirebaseAuth.getInstance();

        //On click listener to sign a user out of their account and brings them
        // back to the welcome page upon clicking the log out button.
        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Intent welcomeIntent = new Intent(getActivity(), WelcomeActivity.class);
            startActivity(welcomeIntent);
            getActivity().finish();
        });


        // TOP CHARTS SECTION

        //Array of the ids of all the text views of the horizontal scrollview displaying the top charts top 5 books
        int[] textViews = {R.id.tc_textview1, R.id.tc_textview2, R.id.tc_textview3, R.id.tc_textview4, R.id.tc_textview5};

        //Array of the ids of all the image views of the horizontal scrollview displaying the top charts top 5 books
        int[] imageViews = {R.id.tc_imageview1, R.id.tc_imageview2, R.id.tc_imageview3, R.id.tc_imageview4, R.id.tc_imageview5};

        List<Book> tc_List = new ArrayList<Book>();
        List<BookDetails> tc_Detailed_List = new ArrayList<BookDetails>();

        //Url that will be used to retrieve the current top books from the NYTimes book api
        String url = "https://api.nytimes.com/svc/books/v3/lists/current/hardcover-fiction.json?api-key=y6og1zAOyVjSobnuULUpQwC4ivOlVQ0u";

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

                    //'For loop' to iterate through the books in the array
                    for(int i = 0; i < array.length(); i++){
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

                        //Setting the images and titles of the books to the respective textviews and imageviews
                        //if the count is under 5, as there are 5 books in the main page.
                        if (i<5){
                            TextView tv = (TextView) view.findViewById(textViews[i]);
                            ImageView iv = (ImageView) view.findViewById(imageViews[i]);

                            tv.setText(title);
                            Picasso.get().load(link).fit().into(iv);
                        }

                        //Create Book and BookDetails objects for each book and adding it to ArrayLists so it can
                        //be reused on other fragments. (will implement in the later stage)
                        Book book = new Book( title, link, isbn );
                        BookDetails bookDetails = new BookDetails(title, authorArray, link, null, rank, null, 0, null, null, null);
                        tc_List.add(book);
                        tc_Detailed_List.add(bookDetails);

                    }

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


        // TOP CHARTS SECTION

        return view;


    }









}