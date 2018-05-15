package com.junburg.moon.rockbottom.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.firebase.FirebaseMethods;
import com.junburg.moon.rockbottom.util.DataExistCallback;
import com.junburg.moon.rockbottom.util.ValidationCheck;
import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by Junburg on 2018. 4. 15..
 */

public class EmailLoginActivity extends AppCompatActivity {
    private static final String TAG = "EmailLoginActivity";

    // Widgets
    private TextInputEditText emailLoginEmailTxt;
    private TextView emailLoginPasswordTxt;
    private TextView emailLoginSignUpBtn, emailLoginPasswordFindBtn;
    private Button emailLoginSignInBtn;

    // Variables
    private String email, password;
    private ValidationCheck validationCheck;
    private Context context;
    private boolean checkOk;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        initSetup();


        emailLoginSignUpBtn.setText(Html.fromHtml("<u>" + getString(R.string.email_login_sign_up_txt) + "</u>"));
        emailLoginSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmailLoginActivity.this, EmailSignUpActivity.class);
                startActivity(intent);
            }
        });

        // 비밀번호 변경 Dialog 띄우기
        emailLoginPasswordFindBtn.setText(Html.fromHtml("<u>" + getString(R.string.email_login_password_find_txt) + "</u>"));
        emailLoginPasswordFindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordFindDialogFragment passwordFindDialogFragment = new PasswordFindDialogFragment();
                passwordFindDialogFragment.show(getFragmentManager(), "PasswordFindDialogFragment");
            }
        });

        // 이메일 로그인
        emailLoginSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(EmailLoginActivity.this);
                progressDialog.setMessage("로그인중입니다");
                progressDialog.setCancelable(false);
                progressDialog.show();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(emailLoginSignInBtn.getWindowToken(), 0);

                // DataExistCallback 값에 따라 첫 로그인 유무 check
                checkFirstLogin(new DataExistCallback() {
                    @Override
                    public void onDataExistCheck(boolean check) {
                        if (check) {
                            email = emailLoginEmailTxt.getText().toString();
                            password = emailLoginPasswordTxt.getText().toString();
                            firebaseMethods.loginEmail(email, password, progressDialog);
                        } else {
                            email = emailLoginEmailTxt.getText().toString();
                            password = emailLoginPasswordTxt.getText().toString();
                            firebaseMethods.firstEmailLogin(email, password, progressDialog);
                        }
                    }
                });

            }
        });
    }

    /**
     * Initialize
     */
    private void initSetup() {

        // Context
        context = EmailLoginActivity.this;

        // View
        emailLoginEmailTxt = (TextInputEditText) findViewById(R.id.email_login_email_txt);
        emailLoginPasswordTxt = (TextInputEditText) findViewById(R.id.email_login_password_txt);
        emailLoginSignUpBtn = (TextView) findViewById(R.id.email_login_sign_up_btn);
        emailLoginPasswordFindBtn = (TextView) findViewById(R.id.email_login_password_find_btn);
        emailLoginSignInBtn = (Button) findViewById(R.id.email_login_sign_in_btn);

        // Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseMethods = new FirebaseMethods(context);

    }

    /**
     * 이메일 계정으로 가입한 사용자가 처음 로그인했는지 Check
     * @param dataExistCallback
     */
    private void checkFirstLogin(final DataExistCallback dataExistCallback) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("가입중입니다");
        progressDialog.setCancelable(false);
        progressDialog.show();
        databaseReference.child("users").orderByChild("email")
                .equalTo(emailLoginEmailTxt.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isFirst = dataSnapshot.exists();
                dataExistCallback.onDataExistCheck(isFirst);
                progressDialog.dismiss();
                Log.d(TAG, "isFirst: " + isFirst);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(getWindow().getDecorView().getRootView(), "다시 가입해주세요", Snackbar.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
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
