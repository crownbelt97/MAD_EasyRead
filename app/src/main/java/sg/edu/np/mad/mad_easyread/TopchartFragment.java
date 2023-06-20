package sg.edu.np.mad.mad_easyread;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class TopchartFragment extends Fragment {

    private ArrayList<News> bookArrayList;
    private String[] bookHeading;
    private int[] imageResourceID;
    private RecyclerView recyclerView;

    private ArrayList<BookDetails> tc_Detailed_List;


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

        TextView tv = view.findViewById(R.id.textView7);

        tc_Detailed_List = new ArrayList<BookDetails>();

        bookArrayList = new ArrayList<>();

        String url = "https://api.nytimes.com/svc/books/v3/lists/current/hardcover-fiction.json?api-key=y6og1zAOyVjSobnuULUpQwC4ivOlVQ0u";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = response.getJSONObject("results");
                    JSONArray array = obj.getJSONArray("books");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        String title = o.getString("title");
                        String link = o.getString("book_image");

                        JSONArray a = o.getJSONArray("isbns");
                        JSONObject isbns = a.getJSONObject(0);
                        String isbn = isbns.getString("isbn13");

                        String authors = o.getString("author");
                        String[] authorArray = {authors};

                        double rank = o.getInt("rank");

                        BookDetails bookDetails = new BookDetails(title, authorArray, link, null, rank, null, 0, null, null, null);
                        tc_Detailed_List.add(bookDetails);

                    }

                    for (int i = 0; i < tc_Detailed_List.size(); i++) {
                        int rank = (int) Math.round(tc_Detailed_List.get(i).getRating());
                        String rankDisplay = "Rank #" + Integer.toString(rank);
                        String authorDisplay = "By " + tc_Detailed_List.get(i).getAuthor(0);
                        News news = new News(tc_Detailed_List.get(i).getTitle(), tc_Detailed_List.get(i).getBook_Image(), authorDisplay, rankDisplay);
                        bookArrayList.add(news);
                    }

                    recyclerView = view.findViewById(R.id.recyclerviewTopChart);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setHasFixedSize(true);
                    MyAdapter myAdapter = new MyAdapter(getContext(), bookArrayList);
                    recyclerView.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();

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