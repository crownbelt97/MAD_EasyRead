package sg.edu.np.mad.mad_easyread;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class TopchartFragment extends Fragment {

    private ArrayList<News> newsArrayList;
    private String[] newsHeading;
    private int[] imageResourceID;
    private RecyclerView recyclerView;

    public TopchartFragment(){

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

        dataInitialize();

        recyclerView = view.findViewById(R.id.recyclerviewTopChart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        MyAdapter myAdapter = new MyAdapter(getContext(), newsArrayList);
        recyclerView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }


    private void dataInitialize() {

        newsArrayList = new ArrayList<>();

        newsHeading = new String[]{
                getString(R.string.head_1),
                getString(R.string.head_2),
                getString(R.string.head_3),
                getString(R.string.head_4),
                getString(R.string.head_5),
                getString(R.string.head_6),
                getString(R.string.head_7),
                getString(R.string.head_8),
                getString(R.string.head_9),
                getString(R.string.head_10),
        };

        imageResourceID = new int[] {
                R.drawable.baseline_android_24,
                R.drawable.baseline_android_24,
                R.drawable.baseline_android_24,
                R.drawable.baseline_android_24,
                R.drawable.baseline_android_24,
                R.drawable.baseline_android_24,
                R.drawable.baseline_android_24,
                R.drawable.baseline_android_24,
                R.drawable.baseline_android_24,
                R.drawable.baseline_android_24,
        };

        for(int i = 0; i< newsHeading.length; i++) {
            News news = new News(newsHeading[i], imageResourceID[i]);
            newsArrayList.add(news);
        }
    }
}