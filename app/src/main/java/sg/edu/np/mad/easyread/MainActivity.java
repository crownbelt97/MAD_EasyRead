package sg.edu.np.mad.easyread;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;

import sg.edu.np.mad.easyread.R;
import sg.edu.np.mad.easyread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private Fragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    if (!(currentFragment instanceof HomeFragment)) {
                        replaceFragment(new HomeFragment());
                    }
                    break;
                case R.id.profile:
                    if (!(currentFragment instanceof ProfileFragment)) {
                        replaceFragment(new ProfileFragment());
                    }
                    break;
                case R.id.favourite:
                    if (!(currentFragment instanceof SettingsFragment)) {
                        replaceFragment(new SettingsFragment());
                    }
                    break;
                case R.id.topchart:
                    if (!(currentFragment instanceof TopchartFragment)) {
                        replaceFragment(new TopchartFragment());
                    }
                    break;
            }
            return true;
        });


    }

    public void setActiveItem(int itemId) {
        binding.bottomNavigationView.setSelectedItemId(itemId);
    }

    public void replaceFragment(Fragment fragment) {
        currentFragment = fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}