package com.junburg.moon.rockbottom.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.firebase.FirebaseMethods;
import com.junburg.moon.rockbottom.main.MainActivity;
import com.junburg.moon.rockbottom.util.DataExistCallback;
import com.pixelcan.inkpageindicator.InkPageIndicator;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.Arrays;

/**
 * Created by Junburg on 2018. 2. 1..
 */

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    // Final
    private static final int RC_SIGN_IN = 10;

    // Widgets
    private LinearLayout loginGoogleBtn;
    private LinearLayout loginFacebookBtn;
    private TextView loginEmailBtn;
    private ViewPager loginViewPager;
    private ImageView loginBackgroundImg;
    private InkPageIndicator loginIndicator;
    private ProgressDialog progressDialog;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseMethods firebaseMethods;

    // Variable
    private Context context;

    // Google
    private GoogleApiClient mGoogleApiClient;

    // Facebook
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        context = LoginActivity.this;
        firebaseMethods = new FirebaseMethods(context);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        loginBackgroundImg = (ImageView) findViewById(R.id.login_background_img);
        loginIndicator = (InkPageIndicator) findViewById(R.id.login_indicator);
        loginBackgroundImg.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);
        loginViewPager = (ViewPager) findViewById(R.id.login_view_pager);
        IntroPagerAdpater adapter = new IntroPagerAdpater(getFragmentManager());
        loginViewPager.setAdapter(adapter);
        loginIndicator.setViewPager(loginViewPager);
        loginGoogleBtn = (LinearLayout) findViewById(R.id.login_google_btn);
        loginGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        callbackManager = CallbackManager.Factory.create();

        loginFacebookBtn = (LinearLayout) findViewById(R.id.login_facebook_btn);
        loginFacebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Snackbar.make(getWindow().getDecorView().getRootView(), "페이스북 인증에 실패하였습니다", Snackbar.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(FacebookException error) {
                    }
                });
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    firebaseMethods.checkUserSetting(firebaseAuth.getUid(), new DataExistCallback() {
                        @Override
                        public void onDataExistCheck(boolean check) {
                            if (check) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(LoginActivity.this, InputInfoActivity.class));
                                finish();
                            }
                        }
                    });


                } else {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "환영합니다 :)", 350).show();
                }
            }
        };

        loginEmailBtn = (TextView) findViewById(R.id.login_email_btn);
        loginEmailBtn.setText(Html.fromHtml("<u>" + getString(R.string.login_email_txt) + "</u>"));
        loginEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, EmailLoginActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("로그인 중입니다");
        progressDialog.show();
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
            }
        }
        // Facebook login
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            if (isNew == true) {
                                progressDialog.dismiss();
                                Snackbar.make(getWindow().getDecorView().getRootView(), "로그인에 성공했습니다", Snackbar.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, InputInfoActivity.class));
                            } else {
                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                        } else {
                            Snackbar.make(getWindow().getDecorView().getRootView(), "가입 또는 로그인에 실패하였습니다", Snackbar.LENGTH_SHORT).show();

                        }

                    }
                });

    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            if (isNew == true) {
                                progressDialog.dismiss();
                                Snackbar.make(getWindow().getDecorView().getRootView(), "로그인에 성공했습니다", Snackbar.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, InputInfoActivity.class));
                            } else {
                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                        } else {
                            Snackbar.make(getWindow().getDecorView().getRootView(), "가입 또는 로그인에 실패하였습니다", Snackbar.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
