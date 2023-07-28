package sg.edu.np.mad.easyread;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import sg.edu.np.mad.easyread.R;

public class HomeFragment extends Fragment {


    FirebaseAuth mAuth;
    ImageView imageView7;
    ShimmerFrameLayout latestShimmer;
    ShimmerFrameLayout topShimmer;
    ShimmerFrameLayout recommendedShimmer;
    ShimmerFrameLayout tagShimmer;

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

        latestShimmer = view.findViewById(R.id.shimmer);
        topShimmer = view.findViewById(R.id.shimmer2);
        recommendedShimmer = view.findViewById(R.id.shimmer3);
        tagShimmer = view.findViewById(R.id.shimmerTags);

        HorizontalScrollView hsv1 = view.findViewById(R.id.popularScrollView);
        HorizontalScrollView hsv2 = view.findViewById(R.id.horizontalScrollView2);
        HorizontalScrollView hsv3 = view.findViewById(R.id.horizontalScrollView3);
        HorizontalScrollView hsvTags = view.findViewById(R.id.details_Category);

        latestShimmer.startShimmer();
        topShimmer.startShimmer();
        recommendedShimmer.startShimmer();
        tagShimmer.startShimmer();



        int[] catTextViews = {R.id.category1, R.id.category2, R.id.category3, R.id.category4, R.id.category5, R.id.category6};
        int[] viewAll = {R.id.latestAll, R.id.recommendedAll};
        String[] reco = {"thriller", "history", "cooking", "novel", "music", "literature", "fantasy", "science", "biography"};



        // TOP CHARTS SECTION

        //Array of the ids of all the text views of the horizontal scrollview displaying the top charts top 5 books
        int[] textViews = {R.id.tc_textview1, R.id.tc_textview2, R.id.tc_textview3, R.id.tc_textview4, R.id.tc_textview5};

        //Array of the ids of all the image views of the horizontal scrollview displaying the top charts top 5 books
        int[] imageViews = {R.id.tc_imageview1, R.id.tc_imageview2, R.id.tc_imageview3, R.id.tc_imageview4, R.id.tc_imageview5};

        // RECOMMNEDED FOR YOU

        ArrayList<ImageView> re_image_views = new ArrayList<>();

        ArrayList<TextView> re_text_views = new ArrayList<>();

        // list for image and text views for each sections

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
        //ist<BookDetails> tc_Detailed_List = new ArrayList<BookDetails>();
        List<Book> re_list = new ArrayList<>();
        List<Book> lr_list = new ArrayList<>();



        String url = "https://api.nytimes.com/svc/books/v3/lists/current/hardcover-fiction.json?api-key=y6og1zAOyVjSobnuULUpQwC4ivOlVQ0u";
        String url_ggl_re = "https://www.googleapis.com/books/v1/volumes?q=subject:Business&key=AIzaSyC5eD17c8IFcJI2_bxxDx22cXGSUZBRp0s";
        String url_ggl_lr = "https://www.googleapis.com/books/v1/volumes?q=subject:Fiction&orderBy=newest&key=AIzaSyC5eD17c8IFcJI2_bxxDx22cXGSUZBRp0s";
        //&key=AIzaSyC5eD17c8IFcJI2_bxxDx22cXGSUZBRp0s

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String recoString = sharedPref.getString("reco", "empty");

        String lastUpdateDate = sharedPref.getString("last_update_date", ""); // Retrieve the last update date

        if (recoString.equals("empty") || isFirstDayOfMonth(lastUpdateDate)) {


            // Generate a random index to get a random category from the array
            int randomIndex = (int) (Math.random() * reco.length);
            String randomCategory = reco[randomIndex];

            // Ensure the new recoString is different from the current one (if not empty)
            if (!recoString.equals("empty")) {
                while (randomCategory.equals(recoString)) {
                    randomIndex = (int) (Math.random() * reco.length);
                    randomCategory = reco[randomIndex];
                }
            }

            // Save the new recoString and last update date in shared preferences
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("reco", randomCategory);
            editor.putString("last_update_date", getCurrentDate()); // Store the current date as the last update date
            editor.apply();

            // Use the new recoString for further processing
            recoString = randomCategory;
        }

        url_ggl_re = "https://www.googleapis.com/books/v1/volumes?q=subject:" + recoString + "key=AIzaSyC5eD17c8IFcJI2_bxxDx22cXGSUZBRp0s";


        String book_data_check = sharedPref.getString("Book" + "0", "empty");
        Log.d("book_data",book_data_check);
        if (!book_data_check.equals("empty")) {
            url = "";
            for (int i = 0; i < 5; i++) {
                String book_data = sharedPref.getString("Book" + i, "empty");
                if (!book_data.equals("empty")) {
                    String[] input_data = book_data.split("!-!-!");
                    try {
                        TextView tv = (TextView) view.findViewById(textViews[i]);
                        ImageView iv = (ImageView) view.findViewById(imageViews[i]);
                        tv.setText(input_data[0]);
                        Log.d("input_data[0]", input_data[0]);
                        Log.d("input_data[1]", input_data[1]);
                        Log.d("input_data[2]", input_data[2]);
                        Picasso.get().load(input_data[1]).fit().into(iv);
                        Book book = new Book( input_data[0], input_data[1], input_data[2] );
                        tc_List.add(book);
                        //BookDetails bookDetails = new BookDetails(input_data[0], authorArray, link, null, rank, null, 0, null, null, null);

                    } catch (Exception e) {
                        Log.d("sharedpref_input_fail", e.toString());
                    }
                }
            }
            Log.d("url_Change" , "true");
        }

        String book_data_check_newest = sharedPref.getString("Booknewest" + "0", "empty");
        Log.d("book_data_newest",book_data_check_newest);
        if (!book_data_check_newest.equals("empty")) {
            url_ggl_lr = "";
            for (int i = 0; i < 5; i++) {
                String book_data = sharedPref.getString("Booknewest" + i, "empty");
                if (!book_data.equals("empty")) {
                    String[] input_data = book_data.split("!-!-!");
                    try {
                        Log.d("i", String.valueOf(i));
                        TextView tv = (TextView) view.findViewById(lr_text_views.get(i).getId());
                        ImageView iv = (ImageView) view.findViewById(lr_image_views.get(i).getId());
                        input_data[1] = input_data[1].replace("http:", "https:");
                        tv.setText(input_data[0]);
                        Log.d("input_data[0]_lr", input_data[0]);
                        Log.d("input_data[1]_lr", input_data[1]);
                        Log.d("input_data[2]_lr", input_data[2]);
                        Picasso.get().load(input_data[1]).fit().into(lr_image_views.get(i));
                        //Picasso.get().load(input_data[1]).fit().into(iv);
                        Book book = new Book( input_data[0], input_data[1], input_data[2] );
                        lr_list.add(book);
                        //BookDetails bookDetails = new BookDetails(input_data[0], authorArray, link, null, rank, null, 0, null, null, null);

                    } catch (Exception e) {
                        Log.d("sharedpref_input_fail lr", e.toString());
                    }
                }
            }
            Log.d("url_Change_lr" , "true");
        }

        String book_data_check_recommended = sharedPref.getString("Bookrecommended" + "0", "empty");
        Log.d("book_data_newest",book_data_check_recommended);
        if (!book_data_check_recommended.equals("empty")) {
            url_ggl_re = "";
            for (int i = 0; i < 5; i++) {
                String book_data = sharedPref.getString("Bookrecommended" + i, "empty");
                if (!book_data.equals("empty")) {
                    String[] input_data = book_data.split("!-!-!");
                    try {
                        TextView tv = (TextView) view.findViewById(re_text_views.get(i).getId());
                        ImageView iv = (ImageView) view.findViewById(re_image_views.get(i).getId());
                        tv.setText(input_data[0]);
                        input_data[1] = input_data[1].replace("http:", "https:");
                        Log.d("input_data[0]_re", input_data[0]);
                        Log.d("input_data[1]_re", input_data[1]);
                        Log.d("input_data[2]_re", input_data[2]);
                        Picasso.get().load(input_data[1]).fit().into(iv);
                        Book book = new Book( input_data[0], input_data[1], input_data[2] );
                        re_list.add(book);
                        //BookDetails bookDetails = new BookDetails(input_data[0], authorArray, link, null, rank, null, 0, null, null, null);

                    } catch (Exception e) {
                        Log.d("sharedpref_input_fail re", e.toString());
                        Log.d("input_data[0]_re", input_data[0]);
                        Log.d("input_data[1]_re", input_data[1]);
                        Log.d("input_data[2]_re", input_data[2]);

                    }
                }
            }
            Log.d("url_Change_re" , "true");
        }

        if (!book_data_check_recommended.equals("empty") && !book_data_check_newest.equals("empty") && !book_data_check.equals("empty")){
            hsv1.setVisibility(View.VISIBLE);
            hsv2.setVisibility(View.VISIBLE);
            hsv3.setVisibility(View.VISIBLE);
            hsvTags.setVisibility(View.VISIBLE);

            recommendedShimmer.setVisibility(View.GONE);
            topShimmer.setVisibility(View.GONE);
            latestShimmer.setVisibility(View.GONE);
            tagShimmer.setVisibility(View.GONE);

            recommendedShimmer.stopShimmer();
            topShimmer.stopShimmer();
            latestShimmer.stopShimmer();
            tagShimmer.stopShimmer();
            Log.d("skip api","true");
        }



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
                        String t = o.getString("title");
                        String title = t.substring(0, 1).toUpperCase() + t.substring(1).toLowerCase();
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
                        //BookDetails bookDetails = new BookDetails(title, authorArray, link, null, rank, null, 0, null, null, null);
                        tc_List.add(book);
                        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("Book" + i,title + "!-!-!" + link + "!-!-!" + isbn);
                        Log.d("tag" , "Book" + i);
                        editor.apply();
                        Log.d("SharedPrefEnterBookTCLIST",title + "!-!-!" + link + "!-!-!" + isbn );

                        //tc_Detailed_List.add(bookDetails);

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



        class MyTask extends AsyncTask<String, Void, String> {

            protected String doInBackground(String... urls) {
                String message = "";
                String Response_content = "";
                //String description = "";
                int Response_code = 0;
                String Response_message = "";

                try {

                    // Code to obtain data from Json using Gson
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
                    JsonObject jsonObject = new Gson().fromJson(input_data, JsonObject.class);



                    Map<String, Object> retMap = new Gson().fromJson(
                            gson.toJson(JsonParser.parseString(response.toString())) , new TypeToken<HashMap<String, Object>>() {}.getType()
                    );
                    if (url.toString().contains("googleapis")) {
                        // parse json data to obtain neccessary fields
                        for (Map.Entry<String, Object> pair : retMap.entrySet()) {
                            String key = pair.getKey();
                            if (Objects.equals(key, "items")) {
                                JsonArray items_array = jsonObject.getAsJsonArray("items");
                                Log.d("data",items_array.toString());
                                for ( int y = 0 ; y < items_array.size() ; y ++) {
                                    try {
                                        JsonObject items_object = (JsonObject) items_array.get(y);
                                        Log.d("array_data", items_object.toString());

                                        JsonObject volumeInfo = items_object.getAsJsonObject("volumeInfo");

                                        String selfLink = items_object.get("selfLink").getAsString();


                                        String title = volumeInfo.get("title").getAsString();

                                        JsonArray authors = volumeInfo.getAsJsonArray("authors");

                                        String[] authors_java_array = gson.fromJson(authors, String[].class);


                                        JsonObject imageLinks = volumeInfo.getAsJsonObject("imageLinks");
                                        String book_image = imageLinks.get("thumbnail").getAsString();

                                        String description = "";

                                        try {
                                            description = volumeInfo.get("description").getAsString();

                                        } catch (Exception e) {
                                            System.out.println(e);
                                        }
                                        // use data to add to book class
                                        // book class used for objects to be displayed
                                        Book book = new Book(title, book_image, selfLink);
                                        if (url.toString().contains("orderBy=newest")) {
                                            System.out.println(lr_list);
                                            SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putString("Booknewest" + y,title + "!-!-!" + book_image + "!-!-!" + selfLink);
                                            Log.d("tag" , "Book" + y);
                                            editor.apply();
                                            Log.d("SharedPrefEnterBookLRLIST",title + "!-!-!" + book_image + "!-!-!" + selfLink );
                                            lr_list.add(book);
                                        } else {
                                            System.out.println(re_list);
                                            SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putString("Bookrecommended" + y,title + "!-!-!" + book_image + "!-!-!" + selfLink);
                                            Log.d("tag" , "Book" + y);
                                            editor.apply();
                                            Log.d("SharedPrefEnterBookRELIST",title + "!-!-!" + book_image + "!-!-!" + selfLink );
                                            re_list.add(book);
                                        }

                                    } catch(Exception e)
                                    {
                                        System.out.println(e);
                                    }


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
                                    Picasso.get().load(image_url).fit().into(lr_image_views.get(x));
                                    Log.d("image link",book.getBook_Image());

                                }


                                hsv1.setVisibility(View.VISIBLE);
                                hsv2.setVisibility(View.VISIBLE);
                                hsv3.setVisibility(View.VISIBLE);
                                hsvTags.setVisibility(View.VISIBLE);

                                recommendedShimmer.setVisibility(View.GONE);
                                topShimmer.setVisibility(View.GONE);
                                latestShimmer.setVisibility(View.GONE);
                                tagShimmer.setVisibility(View.GONE);

                                recommendedShimmer.stopShimmer();
                                topShimmer.stopShimmer();
                                latestShimmer.stopShimmer();
                                tagShimmer.stopShimmer();


                            }
                            else {
                                for (int x = 0; x < 5; x++) {
                                    Book book = re_list.get(x);
                                    Log.d("re_list1", re_list.toString());
                                    String image_url = book.getBook_Image().replace("http:", "https:");
                                    re_text_views.get(x).setText(book.getTitle());
                                    Picasso.get().load(image_url).fit().into(re_image_views.get(x));
                                    Log.d("image link", book.getBook_Image());

                                }
                            }

                        });

                    }


                    http.disconnect();
                } catch (MalformedURLException e) {
                    Log.d("json", "malformedurlexception fail");
                } catch (IOException i) {
                    Log.d("json", "ioexception fail " + urls[0]);
                }
                return message;


            }
            protected void onPostExecute(String result) {
                Log.d("json",result);
            }
        }


        //check if there is sharedpref already


        //Adds the request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(jsonObjectRequest);
        if (url.equals("")) {
            Log.d("NYAPI_ran", "false");
        }
        if (!book_data_check_recommended.equals("empty") && !book_data_check_newest.equals("empty") && !book_data_check.equals("empty")){
            Log.d("google_api-ran","true");
            new MyTask().execute(url_ggl_re, null, null);
            new MyTask().execute(url_ggl_lr, null, null);
        }




        view.findViewById(R.id.imageView7).setOnClickListener(v -> {
            ((MainActivity)getActivity()).replaceFragment(new SearchFragment());
        });

        View.OnClickListener viewAllClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                // Get the ID of the clicked ImageView
                int viewId = v.getId();

                // Determine the corresponding category based on the viewId
                String category;
                switch (viewId) {
                    case R.id.latestAll:
                        category = "Latest Releases";
                        break;
                    case R.id.recommendedAll:
                        category = "Recommended";
                        break;
                    default:
                        category = "Category"; // Add a default value or handle other cases as needed
                        break;
                }

                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("category", category);
                editor.apply();

                ((MainActivity) getActivity()).replaceFragment(new CategoryDisplayFragment());
            }
        };

        for (int i = 0; i < viewAll.length; i++) {
            view.findViewById(viewAll[i]).setOnClickListener(viewAllClickListener);
        }

        view.findViewById(R.id.topAll).setOnClickListener(v -> {
            ((MainActivity) requireActivity()).replaceFragment(new TopchartFragment());
            ((MainActivity) requireActivity()).setActiveItem(R.id.topchart);
        });

        //category click listener
        View.OnClickListener catClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                // Get the text of the clicked TextView
                TextView tv = (TextView) v;
                String str = tv.getText().toString();

                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("category", str);
                editor.apply();

                ((MainActivity) getActivity()).replaceFragment(new CategoryDisplayFragment());
            }
        };

        for (int i = 0; i < catTextViews.length; i++) {
            view.findViewById(catTextViews[i]).setOnClickListener(catClickListener);
        }

        View.OnClickListener clickListener = new View.OnClickListener() {
            public void onClick(View v) {
                //click listener for all image views
                // when clicked, show details fragment
                // and send string of book data url to details fragment
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
                        //int rank = (int) tc_Detailed_List.get(x).getRating();
                        int rank = x + 1;
                        String details_link = tc_List.get(x).getDetails_Link();
                        String image_link = tc_List.get(x).getBook_Image();
                        System.out.println(details_link);
                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("details_link", details_link);
                        editor.putString("image_link", image_link);
                        editor.putInt("rank", rank);
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


        return view;


    }

    private boolean isFirstDayOfMonth(String lastUpdateDate) {
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.format("yyyy-MM-dd", calendar).toString();

        return lastUpdateDate.isEmpty() || !lastUpdateDate.equals(currentDate);
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return DateFormat.format("yyyy-MM-dd", calendar).toString();
    }





}