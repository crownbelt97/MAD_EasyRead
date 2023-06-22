package sg.edu.np.mad.mad_easyread;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

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
import java.util.Stack;


public class DetailsFragment extends Fragment {

    private Button logoutBtn;
    FirebaseAuth mAuth;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        logoutBtn = view.findViewById(R.id.logoutBtn);

        mAuth = FirebaseAuth.getInstance();


        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String details_link = sharedPref.getString("details_link", "empty");

        ArrayList<String> authors_nytimes = new ArrayList<>();

        String author_url = "https://openlibrary.org/api/books?bibkeys=ISBN:";

        final String[] key_nytimes = new String[1];


        class MyTask extends AsyncTask<String, Void, String> {

            protected String doInBackground(String... urls) {
                BookDetails book;
                book = null;
                String message = "";
                String Response_content = "";
                //String description = "";
                int Response_code = 0;
                String Response_message = "";
                int authors_count = 0;
                double ratings = 0;

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
                            JsonObject volumeInfo = jsonObject.getAsJsonObject("volumeInfo");
                            String title = volumeInfo.get("title").getAsString();

                            JsonArray authors = volumeInfo.getAsJsonArray("authors");
                            String [] authors_java_array = gson.fromJson(authors, String[].class);
                            authors_count = authors_java_array.length;


                            String description;
                            try {
                                description = volumeInfo.get("description").getAsString();
                            }
                            catch (Exception e)
                            {
                                description = "N/A";
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

                            book = new BookDetails(title,authors_java_array,book_image,description,ratings,format,Integer.parseInt(length),publisher,release,"na");

                            }
                        }

                    else if (url.toString().contains("ratings")){

                    }

                    else{

                         JsonObject volumeInfo = jsonObject.getAsJsonObject("ISBN:" + details_link);
                        String title = volumeInfo.get("title").getAsString();



                        String release = volumeInfo.get("publish_date").getAsString();

                        JsonArray publishers = volumeInfo.getAsJsonArray("publishers");
                        String publishers_data =  "";
                        for (int x = 0 ; x < publishers.size() ; x ++)
                        {
                            JsonObject name = publishers.get(x).getAsJsonObject();
                            publishers_data += name.get("name").getAsString();
                            if (publishers.size() != 1)
                            {
                                publishers_data += ", ";
                            }
                        }
                        JsonArray authors = volumeInfo.getAsJsonArray("authors");
                        JsonObject [] authorsinfo_java_array = gson.fromJson(authors, JsonObject[].class);
                        String [] authors_java_array = new String[authorsinfo_java_array.length];
                        for (int x = 0; x < authors_java_array.length ; x++)
                        {
                            authors_java_array[x] = authorsinfo_java_array[x].get("name").getAsString();
                        }

                        String length = volumeInfo.get("number_of_pages").getAsString();

                        JsonObject cover = volumeInfo.getAsJsonObject("cover");

                        String book_image = cover.get("large").getAsString();

                        JsonArray works = volumeInfo.getAsJsonArray("works");



                        key_nytimes[0] = volumeInfo.get("key").getAsString();

                        book = new BookDetails(title,authors_java_array,book_image,null,0,"book",Integer.parseInt(length),publishers_data,release,"na");


                    }

                    BookDetails finalBook = book;
                    int finalAuthors_count = authors_count;
                    String finalRatings = String.valueOf(ratings);
                    getActivity().runOnUiThread(() -> {

                        // Stuff that updates the UI
                        String image_url = finalBook.getBook_Image().replace("http:", "https:");
                        Picasso.get().load(image_url).into((ImageView) view.findViewById(R.id.details_Cover));


                        TextView author = view.findViewById(R.id.details_Author);
                        String author_text = "";

                        for (int x = 0; x < finalAuthors_count; x++)
                        {
                            author_text += finalBook.getAuthor(x);
                        }
                        author.setText(author_text);

                        TextView title = view.findViewById(R.id.details_Title);
                        title.setText(finalBook.getTitle());

                        TextView length1 = view.findViewById(R.id.details_Length_Data);
                        String text_input = String.valueOf(finalBook.getLength()) + " Pages";
                        length1.setText(text_input);

                        TextView ratings_view = view.findViewById(R.id.details_Rating_Data);
                        ratings_view.setText(finalRatings);


                        TextView format = view.findViewById(R.id.details_Format_Data);
                        format.setText(finalBook.getFormat());

                        TextView publisher = view.findViewById(R.id.details_Publisher_Data);
                        publisher.setText(finalBook.getPublisher());

                        TextView release = view.findViewById(R.id.details_Released_Data);
                        release.setText(finalBook.getRelease());

                        TextView description = view.findViewById(R.id.details_Description_Data);
                        description.setText(finalBook.getDescription());


                        sharedPref.edit().clear().apply();

                        System.out.println(sharedPref.getString("details_link", "empty"));


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


        if (!details_link.contains("http"))
        {

            String url = author_url + details_link + "&jscmd=data&format=json";
            System.out.println(url);
            new MyTask().execute(url, null, null);
                //new MyTask().execute("https://openlibrary.org/works" + key_nytimes[0] + "/ratings.json", null, null);
        }else
        {
            new MyTask().execute(details_link, null, null);
        }





        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Intent welcomeIntent = new Intent(getActivity(), WelcomeActivity.class);
            startActivity(welcomeIntent);
            getActivity().finish();
        });

        return view;

    }
}