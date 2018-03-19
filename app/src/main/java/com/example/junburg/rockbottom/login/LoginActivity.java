package com.example.junburg.rockbottom.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.junburg.rockbottom.R;
import com.pixelcan.inkpageindicator.InkPageIndicator;

/**
 * Created by Junburg on 2018. 2. 1..
 */

public class LoginActivity extends AppCompatActivity {

    private LinearLayout loginGoogleBtn;
    private LinearLayout loginFacebookBtn;
    private ViewPager loginViewPager;
    private ImageView loginBackgroundImg;
    private InkPageIndicator loginIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBackgroundImg = (ImageView)findViewById(R.id.login_background_img);
        loginIndicator = (InkPageIndicator) findViewById(R.id.login_indicator);
        loginBackgroundImg.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);
        loginViewPager = (ViewPager)findViewById(R.id.login_view_pager);
        IntroPagerAdpater adapter = new IntroPagerAdpater(getFragmentManager());
        loginViewPager.setAdapter(adapter);
        loginIndicator.setViewPager(loginViewPager);
        loginGoogleBtn = (LinearLayout) findViewById(R.id.login_google_btn);
        loginGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, InputInfoActivity.class);
                startActivity(intent);
            }
        });
        loginFacebookBtn = (LinearLayout) findViewById(R.id.login_facebook_btn);

    }
}
