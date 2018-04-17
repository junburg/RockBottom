package com.junburg.moon.rockbottom.main;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.junburg.moon.rockbottom.myinfo.MyInfoFragment;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.ranking.RankingFragment;
import com.junburg.moon.rockbottom.study.StudyFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mainBottomNavigationView;
    private Fragment mainFragment;
    // Firebase
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainBottomNavigationView = (BottomNavigationView) findViewById(R.id.main_bottom_navigation_view);
        mainBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_ranking:
                        mainFragment = new RankingFragment();
                        break;
                    case R.id.bottom_study:
                        mainFragment = new StudyFragment();
                        break;
                    case R.id.bottom_my_info:
                        mainFragment = new MyInfoFragment();
                        break;
                }
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_container, mainFragment).commit();
                return true;
            }
        });
        mainBottomNavigationView.setSelectedItemId(R.id.bottom_study);

    }
}
