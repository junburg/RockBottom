package com.junburg.moon.rockbottom.login;

import android.app.ProgressDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.firebase.FirebaseMethods;
import com.junburg.moon.rockbottom.main.MainActivity;
import com.junburg.moon.rockbottom.util.DataExistCallback;
import com.junburg.moon.rockbottom.util.ValidationCheck;
import com.tsengvn.typekit.TypekitContextWrapper;

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
    private boolean checkOk;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);
        context = EmailLoginActivity.this;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseMethods = new FirebaseMethods(context);

        emailLoginEmailTxt = (TextInputEditText) findViewById(R.id.email_login_email_txt);
        emailLoginPasswordTxt = (TextInputEditText) findViewById(R.id.email_login_password_txt);

        emailLoginSignUpBtn = (TextView) findViewById(R.id.email_login_sign_up_btn);
        emailLoginSignUpBtn.setText(Html.fromHtml("<u>" + getString(R.string.email_login_sign_up_txt) + "</u>"));
        emailLoginSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EmailLoginActivity.this, EmailSignUpActivity.class);
                startActivity(intent);
            }
        });

        emailLoginSignInBtn = (Button) findViewById(R.id.email_login_sign_in_btn);
        emailLoginSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(emailLoginSignInBtn.getWindowToken(), 0);

                checkFirstLogin(new DataExistCallback() {
                    @Override
                    public void onDataExistCheck(boolean check) {
                        if(check) {
                            email = emailLoginEmailTxt.getText().toString();
                            password = emailLoginPasswordTxt.getText().toString();
                            firebaseMethods.loginEmail(email, password);
                        } else {
                            email = emailLoginEmailTxt.getText().toString();
                            password = emailLoginPasswordTxt.getText().toString();
                            firebaseMethods.firstEmailLogin(email, password);
                        }
                    }
                });

            }
        });
    }

    private void checkFirstLogin(final DataExistCallback dataExistCallback) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("중복 검사중입니다");
        progressDialog.setCancelable(false);
        progressDialog.show();
        databaseReference.child("users").orderByChild("email")
                .equalTo(emailLoginEmailTxt.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isFirst = dataSnapshot.exists();
                dataExistCallback.onDataExistCheck(isFirst);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
