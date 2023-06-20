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

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        logoutBtn = view.findViewById(R.id.logoutBtn);

        mAuth = FirebaseAuth.getInstance();


        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Intent welcomeIntent = new Intent(getActivity(), WelcomeActivity.class);
            startActivity(welcomeIntent);
            getActivity().finish();
        });


        // TOP CHARTS

        int[] textViews = {R.id.tc_textview1, R.id.tc_textview2, R.id.tc_textview3, R.id.tc_textview4, R.id.tc_textview5};

        int[] imageViews = {R.id.tc_imageview1, R.id.tc_imageview2, R.id.tc_imageview3, R.id.tc_imageview4, R.id.tc_imageview5};

        List<Book> tc_List = new ArrayList<Book>();
        List<BookDetails> tc_Detailed_List = new ArrayList<BookDetails>();

        String url = "https://api.nytimes.com/svc/books/v3/lists/current/hardcover-fiction.json?api-key=y6og1zAOyVjSobnuULUpQwC4ivOlVQ0u";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = response.getJSONObject("results");
                    JSONArray array = obj.getJSONArray("books");

                    for(int i = 0; i < array.length(); i++){
                        JSONObject o = array.getJSONObject(i);
                        String title = o.getString("title");
                        String link = o.getString("book_image");

                        JSONArray a = o.getJSONArray("isbns");
                        JSONObject isbns = a.getJSONObject(0);
                        String isbn = isbns.getString("isbn13");

                        String authors = o.getString("author");
                        String[] authorArray = {authors};

                        double rank = o.getInt("rank");


                        if (i<5){
                            TextView tv = (TextView) view.findViewById(textViews[i]);
                            ImageView iv = (ImageView) view.findViewById(imageViews[i]);

                            tv.setText(title);
                            Picasso.get().load(link).fit().into(iv);
                        }

                        Book book = new Book( title, link, isbn );
                        BookDetails bookDetails = new BookDetails(title, authorArray, link, null, rank, null, 0, null, null, null);
                        tc_List.add(book);
                        tc_Detailed_List.add(bookDetails);

                    }

//                    int bruh = (int) Math.round(tc_Detailed_List.get(14).getRating());




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(jsonObjectRequest);


        // TOP CHARTS

        return view;


    }









}