package sg.edu.np.mad.mad_easyread;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.os.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();
    public HomeFragment()
    {

    }

    @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
//          requestWindowFeature(Window.FEATURE_NO_TITLE);
//          this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//          getSupportActionBar().hide();
         //setContentView(R.layout.fragment_home);
          //viewPager2 = viewPager2.findViewById(R.id.viewPagerImageSlider);

                 List<SliderItem> sliderItems = new ArrayList<>();
                 sliderItems.add(new SliderItem(R.drawable.baseline_android_24));
                 sliderItems.add(new SliderItem(R.drawable.baseline_android_24));
                 sliderItems.add(new SliderItem(R.drawable.baseline_android_24));
                 sliderItems.add(new SliderItem(R.drawable.baseline_android_24));


     }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//         viewPager2 = viewPager2.findViewById(R.id.viewPagerImageSlider);
//
//         List<SliderItem> sliderItems = new ArrayList<>();
//         sliderItems.add(new SliderItem(R.drawable.baseline_android_24));
//         sliderItems.add(new SliderItem(R.drawable.baseline_android_24));
//         sliderItems.add(new SliderItem(R.drawable.baseline_android_24));
//         sliderItems.add(new SliderItem(R.drawable.baseline_android_24));
//
//         viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));
//
//         viewPager2.setClipToPadding(false);
//         viewPager2.setClipChildren(false);
//         viewPager2.setOffscreenPageLimit(3);
//
//         viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));
//
//         CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
//         compositePageTransformer.addTransformer(new MarginPageTransformer(40));
//
//         compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
//             @Override
//             public void transformPage(@NonNull View page, float position) {
//                 float r = 1 - Math.abs(position);
//                 page.setScaleY(0.85f + r * 0.15f);
//             }
//         });
//
//
//         viewPager2.setPageTransformer(compositePageTransformer);
//         viewPager2.unregisterOnPageChangeCallback(new);


        return inflater.inflate(R.layout.fragment_home, container, false);

    }









}