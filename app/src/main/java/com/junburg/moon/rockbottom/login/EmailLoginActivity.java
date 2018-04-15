package com.junburg.moon.rockbottom.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.junburg.moon.rockbottom.R;

/**
 * Created by Junburg on 2018. 4. 15..
 */

public class EmailLoginActivity  extends AppCompatActivity{

    private TextView emailLoginSignUpBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        emailLoginSignUpBtn = (TextView)findViewById(R.id.email_login_sign_up_btn);
        emailLoginSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmailLoginActivity.this, EmailSignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
