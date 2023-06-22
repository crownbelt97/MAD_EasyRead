package sg.edu.np.mad.mad_easyread;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

        // RECOMMNEDED FOR YOU

        ArrayList<ImageView> re_image_views = new ArrayList<>();

        ArrayList<TextView> re_text_views = new ArrayList<>();

        re_image_views.add(view.findViewById(R.id.re_imageview1));
        re_image_views.add(view.findViewById(R.id.re_imageview2));
        re_image_views.add(view.findViewById(R.id.re_imageview3));
        re_image_views.add(view.findViewById(R.id.re_imageview4));
        re_image_views.add(view.findViewById(R.id.re_imageview5));

        re_text_views.add(view.findViewById(R.id.re_textview1));
        re_text_views.add(view.findViewById(R.id.re_textview2));
        re_text_views.add(view.findViewById(R.id.re_textview3));
        re_text_views.add(view.findViewById(R.id.re_textview4));
        re_text_views.add(view.findViewById(R.id.re_textview5));


        ArrayList<ImageView> lr_image_views = new ArrayList<>();

        ArrayList<TextView> lr_text_views = new ArrayList<>();

        lr_image_views.add(view.findViewById(R.id.lr_imageview1));
        lr_image_views.add(view.findViewById(R.id.lr_imageview2));
        lr_image_views.add(view.findViewById(R.id.lr_imageview3));
        lr_image_views.add(view.findViewById(R.id.lr_imageview4));
        lr_image_views.add(view.findViewById(R.id.lr_imageview5));

        lr_text_views.add(view.findViewById(R.id.lr_textview1));
        lr_text_views.add(view.findViewById(R.id.lr_textview2));
        lr_text_views.add(view.findViewById(R.id.lr_textview3));
        lr_text_views.add(view.findViewById(R.id.lr_textview4));
        lr_text_views.add(view.findViewById(R.id.lr_textview5));


        List<Book> tc_List = new ArrayList<Book>();
        List<BookDetails> tc_Detailed_List = new ArrayList<BookDetails>();
        List<Book> re_list = new ArrayList<>();
        List<Book> lr_list = new ArrayList<>();

        String url = "https://api.nytimes.com/svc/books/v3/lists/current/hardcover-fiction.json?api-key=y6og1zAOyVjSobnuULUpQwC4ivOlVQ0u";
        String url_ggl_re = "https://www.googleapis.com/books/v1/volumes?q=subject:Business&key=AIzaSyC5eD17c8IFcJI2_bxxDx22cXGSUZBRp0s";
        String url_ggl_lr = "https://www.googleapis.com/books/v1/volumes?q=subject:Fiction&orderBy=newest&key=AIzaSyC5eD17c8IFcJI2_bxxDx22cXGSUZBRp0s";

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



        class MyTask extends AsyncTask<String, Void, String> {

            protected String doInBackground(String... urls) {
                String message = "";
                String Response_content = "";
                //String description = "";
                int Response_code = 0;
                String Response_message = "";

                try {
                    URL url = new URL (urls[0]);
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("GET");
                    http.setRequestProperty("Accept", "application/json");
                    BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String input_data = gson.toJson(JsonParser.parseString(response.toString()));
                    //System.out.println(input_data);
                    JsonObject jsonObject = new Gson().fromJson(input_data, JsonObject.class);
                    //System.out.println((jsonObject.get("results").getAsJsonObject().toString()));

                    //Gson g2 = new Gson();
                    //Book b = g2.fromJson(gson.toJson(JsonParser.parseString(response.toString())), Book.class)
                    Map<String, Object> retMap = new Gson().fromJson(
                            gson.toJson(JsonParser.parseString(response.toString())) , new TypeToken<HashMap<String, Object>>() {}.getType()
                    );
                    if (url.toString().contains("googleapis")) {

                        for (Map.Entry<String, Object> pair : retMap.entrySet()) {
                            String key = pair.getKey();
                            if (Objects.equals(key, "items")) {
                                JsonArray items_array = jsonObject.getAsJsonArray("items");
                                Log.d("data",items_array.toString());
                                for ( int y = 0 ; y < items_array.size() ; y ++) {
                                    JsonObject items_object = (JsonObject) items_array.get(y);
                                    Log.d("array_data", items_object.toString());

                                    JsonObject volumeInfo = items_object.getAsJsonObject("volumeInfo");

                                    String selfLink = items_object.get("selfLink").getAsString();


                                    String title = volumeInfo.get("title").getAsString();

                                    JsonArray authors = volumeInfo.getAsJsonArray("authors");

                                    String [] authors_java_array = gson.fromJson(authors, String[].class);



                                    JsonObject imageLinks = volumeInfo.getAsJsonObject("imageLinks");
                                    String book_image = imageLinks.get("thumbnail").getAsString();

                                    String description = "";

                                    try {
                                        description = volumeInfo.get("description").getAsString();

                                    } catch (Exception e)
                                    {
                                        System.out.println(e);
                                    }
                                    Book book = new Book(title, book_image, selfLink);
                                    if (url.toString().contains("orderBy=newest")) {
                                        System.out.println(lr_list);
                                        lr_list.add(book);
                                    }
                                    else{
                                        System.out.println(re_list);
                                        re_list.add(book);
                                    }
                                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("Iwanna", "suicide");
                                    editor.apply();


                                }


                            }
                        }

                        getActivity().runOnUiThread(() -> {

                            // Stuff that updates the UI
                            if (url.toString().contains("orderBy=newest")) {
                                Log.d("lr_run", String.valueOf(lr_list.size()));
                                for ( int x = 0; x < 5 ; x++) {
                                    Book book = lr_list.get(x);
                                    Log.d("lr_list1",lr_list.toString());
                                    String image_url = book.getBook_Image().replace("http:", "https:");
                                    lr_text_views.get(x).setText(book.getTitle());
                                    Picasso.get().load(image_url).into(lr_image_views.get(x));
                                    Log.d("image link",book.getBook_Image());

                                }
                            }
                            else {
                                for (int x = 0; x < 5; x++) {
                                    Book book = re_list.get(x);
                                    Log.d("re_list1", re_list.toString());
                                    String image_url = book.getBook_Image().replace("http:", "https:");
                                    re_text_views.get(x).setText(book.getTitle());
                                    Picasso.get().load(image_url).into(re_image_views.get(x));
                                    Log.d("image link", book.getBook_Image());

                                }
                            }
                        });

                    }


                    http.disconnect();
                } catch (MalformedURLException e) {
                    Log.d("json", "malformedurlexception fail");
                } catch (IOException i) {
                    Log.d("json", "ioexception fail");
                }
                return message;


            }
            protected void onPostExecute(String result) {
                Log.d("json",result);
            }
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(jsonObjectRequest);
        new MyTask().execute(url_ggl_re, null, null);
        new MyTask().execute(url_ggl_lr, null, null);



        View.OnClickListener clickListener = new View.OnClickListener() {
            public void onClick(View v) {
                int id = v.getId();
                for ( int x = 0; x < lr_image_views.size() ; x ++)
                {
                    if (id == lr_image_views.get(x).getId())
                    {
                        String details_link = lr_list.get(x).getDetails_Link();
                        System.out.println(details_link);
                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("details_link", details_link);
                        editor.apply();
                        ((MainActivity)getActivity()).replaceFragment(new DetailsFragment());
                        break;
                    }
                }
                for ( int x = 0; x < imageViews.length ; x ++)
                {
                    if (id == imageViews[x])
                    {
                        String details_link = tc_List.get(x).getDetails_Link();
                        System.out.println(details_link);
                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("details_link", details_link);
                        editor.apply();
                        ((MainActivity)getActivity()).replaceFragment(new DetailsFragment());
                        break;
                    }
                }
                for ( int x = 0; x < re_image_views.size() ; x ++)
                {
                    if (id == re_image_views.get(x).getId())
                    {
                        String details_link = re_list.get(x).getDetails_Link();
                        System.out.println(details_link);
                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("details_link", details_link);
                        editor.apply();
                        ((MainActivity)getActivity()).replaceFragment(new DetailsFragment());
                        break;
                    }
                }
            }
        };
        for (int x = 0; x < lr_image_views.size() ; x ++)
        {
            lr_image_views.get(x).setOnClickListener(clickListener);
        }
        for (int x = 0; x < re_image_views.size() ; x ++)
        {
            re_image_views.get(x).setOnClickListener(clickListener);
        }
        for (int imageView : imageViews) {
            view.findViewById(imageView).setOnClickListener(clickListener);
        }

        // TOP CHARTS

        return view;


    }









}