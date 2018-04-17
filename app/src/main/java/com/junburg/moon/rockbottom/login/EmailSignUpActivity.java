package com.junburg.moon.rockbottom.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.firebase.FirebaseMethods;
import com.junburg.moon.rockbottom.util.ValidationCheck;

/**
 * Created by Junburg on 2018. 4. 15..
 */

public class EmailSignUpActivity extends AppCompatActivity{

    // Widgets
    private TextInputEditText emailLoginEmailTxt, emailLoginPasswordTxt, emailLoginCheckPasswordTxt;
    private Button emailLoginSignUpBtn;
    private ProgressDialog progressDialog;
    private ValidationCheck validationCheck;

    // Variables
    private String email, password, checkPassword;
    private Context context;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseMethods firebaseMethods;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_email);

        emailLoginEmailTxt = (TextInputEditText)findViewById(R.id.email_login_email_txt);
        emailLoginPasswordTxt = (TextInputEditText)findViewById(R.id.email_login_password_txt);
        emailLoginCheckPasswordTxt = (TextInputEditText)findViewById(R.id.email_login_check_password_txt);
        emailLoginSignUpBtn = (Button)findViewById(R.id.email_login_sign_up_btn);
        context = EmailSignUpActivity.this;
        firebaseMethods = new FirebaseMethods(context);
        validationCheck = new ValidationCheck(context);

        emailLoginSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(emailLoginSignUpBtn.getWindowToken(), 0);

                email = emailLoginEmailTxt.getText().toString();
                password = emailLoginPasswordTxt.getText().toString();
                checkPassword = emailLoginCheckPasswordTxt.getText().toString();

                if(validationCheck.blankStringCheck(email, password, checkPassword)
                        && validationCheck.isEmailString(email)
                        && validationCheck.checkSamePassword(password, checkPassword)
                        && validationCheck.isPasswordPattern(password)
                        ) {

                    firebaseMethods.registerNewEmail(email, password);
                }


            }
        });

    }



}
