package sg.edu.np.mad.easyread;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class DetailsFragment extends Fragment {



    FirebaseAuth mAuth;
    //DatabaseReference myRef = null;
    FirebaseDatabase secondaryDatabase = null;

    String ISBN_reference = "";


    public DetailsFragment() {
        // Required empty public constructor
    }

    public void writeNewBookmark(String title , String image_link ,String details_link, String userId) {
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
        if (!details_link.contains("https"))
        {
            ISBN_reference = details_link;
        }
        Book user = new Book(title,image_link,details_link);
        DatabaseReference myRef = secondaryDatabase.getReference("results");
        myRef.child(userId).child(ISBN_reference).setValue(user);
    }
    public void deleteBookmark (String userId)
    {
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
        myRef.child(userId).child(ISBN_reference).removeValue();

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //check if followed via ISBN



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_details, container, false);


        mAuth = FirebaseAuth.getInstance();
        //obtain url from previous fragment
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String details_link = sharedPref.getString("details_link", "empty");
        int ranking = sharedPref.getInt("rank",0);

        ArrayList<String> authors_nytimes = new ArrayList<>();

        String author_url = "https://openlibrary.org/api/books?bibkeys=ISBN:";


        final String[] key_nytimes = new String[1];



        class MyTask extends AsyncTask<String, Void, String> {

            protected String doInBackground(String... urls) {
                BookDetails book;
                book = null;
                String message = "";
                String Response_content = "";
                int Response_code = 0;
                String Response_message = "";

                //create variables for fields that may lack json data
                //authors count is also declared since there may be mulitple authors
                int authors_count = 0;
                double ratings = -1;
                String description_input = "N/A";

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
                            JsonObject volumeInfo = jsonObject.getAsJsonObject("volumeInfo");
                            String title = volumeInfo.get("title").getAsString();

                            JsonArray authors = volumeInfo.getAsJsonArray("authors");
                            String [] authors_java_array = gson.fromJson(authors, String[].class);
                            authors_count = authors_java_array.length;


                            try {
                                description_input = volumeInfo.get("description").getAsString();
                            }
                            catch (Exception e)
                            {
                                description_input = "N/A";
                            }

                            String ISBN = "";

                            try {
                                JsonArray industryIdentifiers = volumeInfo.getAsJsonArray("industryIdentifiers");
                                ISBN_reference = industryIdentifiers.get(1).getAsJsonObject().get("identifier").getAsString();
                                Log.d("GoogleAPI",ISBN_reference);
                            }catch (Exception e)
                            {
                                Log.d("Google_URL ISBN" , e.toString());
                            }

                            String release = volumeInfo.get("publishedDate").getAsString();

                            String length = volumeInfo.get("pageCount").getAsString();

                            String publisher = volumeInfo.get("publisher").getAsString();

                            String format = volumeInfo.get("printType").getAsString();

                            JsonObject imageLinks = volumeInfo.getAsJsonObject("imageLinks");

                            String book_image = imageLinks.get("thumbnail").getAsString();

                            try {
                                ratings = volumeInfo.get("averageRating").getAsDouble();
                            } catch (Exception e)
                            {
                                Log.d("error",e.toString());
                            }
                            //add details to bookdetails class
                            //used for details fragment
                            book = new BookDetails(title,authors_java_array,book_image,description_input,ratings,format,Integer.parseInt(length),publisher,release,"na");

                            }
                        }

                    else if (url.toString().contains("jscmd=details")){
                        JsonObject volumeInfo = null;
                        JsonObject detail = null;
                        JsonArray works = null;
                        String description_key = "";

                        try{
                            volumeInfo = jsonObject.getAsJsonObject("ISBN:" + details_link);
                        }catch (Exception e){
                            Log.d("detailsURl volumeinfo", e.toString());
                        }
                        //get works jsonobject to get description of book
                        try{
                            detail = volumeInfo.getAsJsonObject("details");
                        }catch (Exception e){
                            Log.d("detailsURl detail", e.toString());
                        }

                        try {
                            works = detail.getAsJsonArray("works");
                        }catch (Exception e)
                        {
                            Log.d("detailsURl works", e.toString());
                        }

                        //get key for description url

                        try {
                            description_key = works.get(0).getAsJsonObject().get("key").getAsString();
                        }catch (Exception e)
                        {
                            Log.d("detailsURl description_key", e.toString());
                        }




                        //finish forming the description url
                        String description_url ="https://openlibrary.org" + description_key + ".json";
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("description_url", description_url);
                        editor.apply();
                        Log.d("des",sharedPref.getString("description_url", "empty"));

                    }
                    else if (url.toString().contains("works"))
                    {
                        //try catch block to get description
                        // since some json data lacks description field
                        try
                        {
                            JsonObject description = jsonObject.getAsJsonObject("description");
                            description_input = description.get("value").getAsString();
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("description", description_input);
                            editor.apply();
                            Log.d("description",sharedPref.getString("description", "empty"));

                        } catch ( Exception e)
                        {
                            Log.d("worksURL_Description", e.toString());
                        }
                    }

                    else{

                        JsonObject volumeInfo = null;
                        String title = "";
                        String release = "";
                        JsonArray publishers = null;
                        String publishers_data = "";
                        JsonArray authors = null;
                        JsonObject[] authorsinfo_java_array = null;
                        String[] authors_java_array = null;
                        String length = "-1";
                        JsonObject cover = null;
                        String book_image = "";
                        JsonArray works = null;

                            try {
                                volumeInfo = jsonObject.getAsJsonObject("ISBN:" + details_link);

                            }catch (Exception e)
                            {
                                Log.d("data_URL volumeinfo", e.toString());
                            }

                            try {
                                title = volumeInfo.get("title").getAsString();

                            }catch (Exception e)
                            {
                                Log.d("data_URL title", e.toString());
                            }

                            try{
                                release = volumeInfo.get("publish_date").getAsString();
                            }catch (Exception e)
                            {
                                Log.d("data_URL release", e.toString());
                            }

                            try{
                                publishers = volumeInfo.getAsJsonArray("publishers");

                            }catch (Exception e)
                            {
                                Log.d("data_URL publishers", e.toString());
                            }

                            for (int x = 0; x < publishers.size(); x++) {
                                JsonObject name = publishers.get(x).getAsJsonObject();
                                publishers_data += name.get("name").getAsString();
                                if (publishers.size() != 1) {
                                    publishers_data += ", ";
                                }
                            }

                            try{
                                authors = volumeInfo.getAsJsonArray("authors");

                            }catch (Exception e)
                            {
                                Log.d("data_URL authors", e.toString());
                            }

                            try {
                                authorsinfo_java_array = gson.fromJson(authors, JsonObject[].class);

                            }catch (Exception e)
                            {
                                Log.d("data_URL authorsinfo_java_array", e.toString());
                            }

                            try{
                                authors_java_array = new String[authorsinfo_java_array.length];

                            }catch (Exception e)
                            {
                                Log.d("data_URL authors_java_array", e.toString());
                            }


                             for (int x = 0; x < authors_java_array.length; x++) {
                                authors_java_array[x] = authorsinfo_java_array[x].get("name").getAsString();
                            }

                            //try catch since some json data lacks number of pages field
                            try {
                                length = volumeInfo.get("number_of_pages").getAsString();
                            } catch (Exception e) {
                                Log.d("detailsURL_NumPages" , e.toString());
                            }

                            try {
                                cover = volumeInfo.getAsJsonObject("cover");
                            }catch (Exception e)
                            {
                                Log.d("data_URL cover", e.toString());
                            }


                            try{
                                book_image = cover.get("large").getAsString();
                            }catch (Exception e)
                            {
                                Log.d("data_URL book_image", e.toString());
                            }


                            try{
                                works = volumeInfo.getAsJsonArray("works");

                            }catch (Exception e)
                            {
                                Log.d("data_URL works", e.toString());
                            }


                            key_nytimes[0] = volumeInfo.get("key").getAsString();

                            book = new BookDetails(title, authors_java_array, book_image, sharedPref.getString("description", "N/A"), ranking, "book", Integer.parseInt(length), publishers_data, release, "na");

                    }

                    BookDetails finalBook = book;
                    int finalAuthors_count = authors_count;
                    String finalRatings = String.valueOf(ratings);

                    if (book != null && !url.toString().contains("jscmd=details"))
                    {
                        getActivity().runOnUiThread(() -> {

                            // Stuff that updates the UI

                            String image_url = "";
                            ImageView img = null;

                            try{
                                image_url = finalBook.getBook_Image().replace("http:", "https:");
                                Picasso.get().load(image_url).into((ImageView) view.findViewById(R.id.details_Cover));
                                img = view.findViewById(R.id.details_Cover);
                                img.setTag(image_url);

                            }catch (Exception e)
                            {
                                Log.d("picasso_fail" , e.toString());
                                image_url = sharedPref.getString("image_link", "empty").replace("http:", "https:");
                                Picasso.get().load(image_url).into((ImageView) view.findViewById(R.id.details_Cover));
                                img = view.findViewById(R.id.details_Cover);
                                img.setTag(image_url);
                            }

                            TextView author = view.findViewById(R.id.details_Author);
                            String author_text = "";

                            for (int x = 0; x < finalAuthors_count; x++) {
                                author_text += finalBook.getAuthor(x);
                            }
                            author.setText(author_text);

                            TextView title = view.findViewById(R.id.details_Title);
                            title.setText(finalBook.getTitle());
                            title.setTag(finalBook.getTitle());

                            TextView length1 = view.findViewById(R.id.details_Length_Data);
                            //change input if length of book is unavailable
                            if (finalBook.getLength() == -1)
                            {
                                length1.setText("N/A");
                            }
                            else{
                                String text_input = String.valueOf(finalBook.getLength()) + " Pages";
                                Log.d("error",text_input);
                                length1.setText(text_input);
                            }


                            TextView ratings_view = view.findViewById(R.id.details_Rating_Data);
                            TextView ratings_field = view.findViewById((R.id.details_Ratings));
                            //ranking data differs when its from top chart

                            if (!details_link.contains("http")) {
                                String input_rank = String.valueOf(ranking) + "/15";
                                ratings_view.setText(input_rank);
                                //change ui field to make it more understandable
                                ratings_field.setText("Ranking");
                            } else {
                                //change ratings output if lack of rating data
                                if (finalRatings.equals("-1.0")) {
                                    ratings_view.setText("N/A");
                                } else {
                                    ratings_view.setText(finalRatings);
                                }

                            }


                            TextView format = view.findViewById(R.id.details_Format_Data);
                            format.setText(finalBook.getFormat());

                            TextView publisher = view.findViewById(R.id.details_Publisher_Data);
                            publisher.setText(finalBook.getPublisher());

                            TextView release = view.findViewById(R.id.details_Released_Data);
                            release.setText(finalBook.getRelease());

                            TextView description = view.findViewById(R.id.details_Description_Data);

                            //use jsoup to clean descriptions from google api
                            //since descriptions still has html tags
                            String description_data = finalBook.getDescription();
                            description_data = Jsoup.parse(description_data).text();
                            description.setText(description_data);



                            sharedPref.edit().clear().apply();

                            System.out.println(sharedPref.getString("details_link", "empty"));




                        });
                    }

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
                    Query mQueryRef = myRef.child("02").child(ISBN_reference);
                    Log.d("ISBN" , ISBN_reference);
                    mQueryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean exists = snapshot.exists();
                            ImageView img= view.findViewById(R.id.details_bookmark);

                            if (exists) {
                                Log.d("Follow_check", "true");
                                img.setImageResource(R.drawable.checkedbookmark);
                                img.setTag("checkedbookmark");
                            }else {
                                Log.d("Follow_check", "false");
                                img.setImageResource(R.drawable.uncheckedbookmark);
                                img.setTag("uncheckedbookmark");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                    http.disconnect();
                } catch (MalformedURLException e) {
                    Log.d("json", "malformedurlexception fail");
                } catch (IOException i) {
                    Log.d("json", "ioexception fail");
                }
                return message;


            }
        }

        //make url requests
        if (!details_link.contains("http"))
        {
            //create and call GET method for respective urls

            String url_details = author_url + details_link + "&jscmd=details&format=json";
            System.out.println(url_details);
            // wait for this task to finish before calling the other tasks calls
            try {
                new MyTask().execute(url_details, null, null).get();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            //obtain description url from stored shared preference
            String description_url = sharedPref.getString("description_url", "empty");
            Log.d("tagged",description_url);
            new MyTask().execute(description_url, null, null);


            String url_data = author_url + details_link + "&jscmd=data&format=json";
            System.out.println(url_data);
            new MyTask().execute(url_data, null, null);
                //new MyTask().execute("https://openlibrary.org/works" + key_nytimes[0] + "/ratings.json", null, null);
        }else
        {
            new MyTask().execute(details_link, null, null);
        }



        view.findViewById(R.id.details_bookmark).setOnClickListener(v -> {
            ImageView img= view.findViewById(R.id.details_bookmark);
            String imageName = String.valueOf(img.getTag());
            Log.d("imagenamestart" , imageName);
            TextView title = view.findViewById(R.id.details_Title);
            ImageView bookImg = view.findViewById(R.id.details_Cover);
            if (imageName.equals("uncheckedbookmark"))
            {
                img.setImageResource(R.drawable.checkedbookmark);
                img.setTag("checkedbookmark");
                Log.d("details_link1",details_link + title.getTag().toString() + bookImg.getTag().toString());
                writeNewBookmark(title.getTag().toString(), bookImg.getTag().toString(),details_link,"02");

                //IN BOOKMARKS PAGE REMEBER TO PUTSARED PREFERENCES
                // MAINFRAGMENT -> DETAILSFRAGMENT

            }
            else if (imageName.equals("checkedbookmark"))
            {
                img.setImageResource(R.drawable.uncheckedbookmark);
                deleteBookmark("02");
                img.setTag("uncheckedbookmark");

            }


        });


        return view;

    }
}