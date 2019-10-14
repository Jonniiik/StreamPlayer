package com.example.AudioStream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.AudioStream.Adapter.OnActivityDataListener;
import com.example.AudioStream.Adapter.OnFragment1DataListener;
import com.example.AudioStream.Fragments.MusicFragment;
import com.example.AudioStream.Fragments.RadioFragment;
import com.example.AudioStream.Fragments.SettingFragment;
import com.example.AudioStream.Fragments.VideoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements OnFragment1DataListener {

    private OnActivityDataListener mListenerActivity;
    final Fragment fragmentMusic = MusicFragment.newInstance();
    final Fragment fragmentRadio = RadioFragment.newInstance();
    final Fragment fragmentVideo = VideoFragment.newInstance();
    final Fragment fragmentSetting = SettingFragment.newInstance();

    Fragment activeFragment = fragmentMusic;
    final FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startApp();
        createFragment();
    }

    private void startApp() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_music);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationView = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_music:
                    fm.beginTransaction().hide(activeFragment).show(fragmentMusic).commit();
                    activeFragment = fragmentMusic;
                    return true;
                case R.id.navigation_radio:
                    fm.beginTransaction().hide(activeFragment).show(fragmentRadio).commit();
                    activeFragment = fragmentRadio;
                    return true;
                case R.id.navigation_video:
                    fm.beginTransaction().hide(activeFragment).show(fragmentVideo).commit();
                    activeFragment = fragmentVideo;
                    return true;
                case R.id.navigation_setting:
                    fm.beginTransaction().hide(activeFragment).show(fragmentSetting).commit();
                    activeFragment = fragmentSetting;
                    return true;
            }
            return false;
        }
    };

    private void createFragment() {
        fm.beginTransaction().add(R.id.frameContainerList, fragmentRadio).hide(fragmentRadio).commit();
        fm.beginTransaction().add(R.id.frameContainerList, fragmentVideo).hide(fragmentVideo).commit();
        fm.beginTransaction().add(R.id.frameContainerList, fragmentSetting).hide(fragmentSetting).commit();
        fm.beginTransaction().add(R.id.frameContainerList, fragmentMusic).commit();
    }

    @Override
    public void onOpenFragment(String imageURL, String artistURL) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frameContainerList);
        if (fragment == null) {
            fragment = new MusicFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.frameContainerList, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        } else {
            if (fragment instanceof OnActivityDataListener) {
                mListenerActivity = (OnActivityDataListener) fragment;
            } else {
                throw new RuntimeException(fragment.toString()
                        + " must implement onActivityDataListener ");
            }
            mListenerActivity.onActivityDataListener(imageURL, artistURL);
        }

    }
}
