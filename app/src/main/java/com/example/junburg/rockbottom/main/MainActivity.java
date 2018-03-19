package com.example.junburg.rockbottom.main;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.junburg.rockbottom.myinfo.MyInfoFragment;
import com.example.junburg.rockbottom.R;
import com.example.junburg.rockbottom.ranking.RankingFragment;
import com.example.junburg.rockbottom.study.StudyFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mainBottomNavigationView;
    private Fragment mainFragment;

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
