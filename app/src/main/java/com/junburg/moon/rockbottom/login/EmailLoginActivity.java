package com.junburg.moon.rockbottom.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.firebase.FirebaseMethods;
import com.junburg.moon.rockbottom.main.MainActivity;
import com.junburg.moon.rockbottom.util.ValidationCheck;

/**
 * Created by Junburg on 2018. 4. 15..
 */

public class EmailLoginActivity extends AppCompatActivity {

    // Widgets
    private TextInputEditText emailLoginEmailTxt, emailLoginPasswordTxt;
    private TextView emailLoginSignUpBtn;
    private Button emailLoginSignInBtn;

    // Variables
    private String email, password;
    private ValidationCheck validationCheck;
    private Context context;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseMethods firebaseMethods;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);
        context = EmailLoginActivity.this;
        firebaseMethods = new FirebaseMethods(context);

        emailLoginEmailTxt = (TextInputEditText)findViewById(R.id.email_login_email_txt);
        emailLoginPasswordTxt = (TextInputEditText)findViewById(R.id.email_login_password_txt);

        emailLoginSignUpBtn = (TextView) findViewById(R.id.email_login_sign_up_btn);
        emailLoginSignUpBtn.setText(Html.fromHtml("<u>" + getString(R.string.email_login_sign_up_txt) + "</u>"));
        emailLoginSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EmailLoginActivity.this, EmailSignUpActivity.class);
                startActivity(intent);
            }
        });

        emailLoginSignInBtn = (Button)findViewById(R.id.email_login_sign_in_btn);
        emailLoginSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(emailLoginSignUpBtn.getWindowToken(), 0);

                email = emailLoginEmailTxt.getText().toString();
                password = emailLoginPasswordTxt.getText().toString();
                firebaseMethods.loginEmail(email, password);
            }
        });
    }
}
