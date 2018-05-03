package com.junburg.moon.rockbottom.myinfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.firebase.FirebaseMethods;
import com.junburg.moon.rockbottom.login.LoginActivity;
import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by Junburg on 2018. 4. 19..
 */

public class AccountSettingActivity extends AppCompatActivity {

    private static final String TAG = "AccountSettingActivity";

    // Widgets
    private TextView accountSettingEmailTxt, accountSettingLogoutTxt, accountSettingDeleteAccountTxt;


    // Firebases
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseMethods firebaseMethods;

    // Variables
    private String userEmail;
    private AuthCredential credential;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        context = AccountSettingActivity.this;
        firebaseMethods = new FirebaseMethods(context);
        userEmail = firebaseUser.getEmail();

        accountSettingEmailTxt = (TextView) findViewById(R.id.account_setting_email_txt);
        accountSettingEmailTxt.setText(userEmail);

        accountSettingLogoutTxt = (TextView) findViewById(R.id.account_setting_logout_txt);
        accountSettingLogoutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountSettingActivity.this
                        , R.style.AlertDialogStyle);
                builder.setTitle("로그아웃")
                        .setMessage("정말 로그아웃 하시겠어요?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                AuthUI.getInstance().signOut(getApplicationContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(AccountSettingActivity.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        accountSettingDeleteAccountTxt = (TextView) findViewById(R.id.account_setting_delete_account_txt);
        accountSettingDeleteAccountTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountSettingActivity.this
                        , R.style.AlertDialogStyle);
                builder.setTitle("회원탈퇴")
                        .setMessage("정말 탈퇴하시겠어요? 계정 관련 정보는 모두 소멸됩니다")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                final String uid = firebaseUser.getUid();

                                AuthUI.getInstance()
                                        .delete(AccountSettingActivity.this)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                        deleteUserInfo(uid);
                                                } else {
                                                    reauthenticateForDeletingUser();
                                                }
                                            }
                                        });

                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                } else {
                    Intent intent = new Intent(AccountSettingActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        };
    }

    private void reauthenticateForDeletingUser() {
        if (firebaseAuth.getCurrentUser() != null) {
            firebaseUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {

                        String token = task.getResult().getToken();

                        if (firebaseUser.getProviders() != null && firebaseUser.getProviders().get(0).equals("google.com")) {

                            credential = GoogleAuthProvider.getCredential(token, null);

                            firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    final String uid = firebaseUser.getUid();

                                    firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d(TAG, "onComplete: " + task.isSuccessful());
                                            if (task.isSuccessful()) {
                                                deleteUserInfo(uid);

                                            } else {
                                                Toast.makeText(AccountSettingActivity.this, "탈퇴실패", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });

                        } else if (firebaseUser.getProviders() != null && firebaseUser.isEmailVerified()) {
                            Intent intent = new Intent(AccountSettingActivity.this, DeleteEmailAccountActivity.class);
                            startActivity(intent);
                        }


                    }

                }

            });
        }


    }

    private void deleteUserInfo(String uid) {
        databaseReference.child("user_study_condition").child(uid).removeValue();
        databaseReference.child("users").child(uid).removeValue();
        firebaseMethods.deleteSelfieImgOnlyStorage(uid);
        Snackbar.make(getWindow().getDecorView().getRootView()
                , "탈퇴 처리되었습니다. 이용해주셔서 감사합니다 :)", Snackbar.LENGTH_LONG).show();
        Intent intent = new Intent(AccountSettingActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
