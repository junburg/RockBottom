package com.junburg.moon.rockbottom.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.junburg.moon.rockbottom.firebase.FirebaseMethods;
import com.junburg.moon.rockbottom.myinfo.MyInfoFragment;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.ranking.RankingFragment;
import com.junburg.moon.rockbottom.study.StudyFragment;
import com.tsengvn.typekit.TypekitContextWrapper;

public class MainActivity extends AppCompatActivity {

    // Views
    private BottomNavigationView mainBottomNavigationView;
    private Fragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Replace fragment
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

    /**
     * Typekit for font
     * @param newBase
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
