package com.junburg.moon.rockbottom.myinfo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.login.LoginActivity;

/**
 * Created by Junburg on 2018. 4. 19..
 */

public class AccountSettingActivity extends AppCompatActivity {

    // Widgets
    private TextView accountSettingEmailTxt, accountSettingLogoutTxt, accountSettingDeleteAccountTxt;

    // Firebases
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

    // Variables
    private String userEmail;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userEmail = firebaseUser.getEmail();
        progressDialog = new ProgressDialog(this);

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
                                firebaseAuth.signOut();
                                Intent intent = new Intent(AccountSettingActivity.this, LoginActivity.class);
                                startActivity(intent);
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
                                progressDialog.setMessage("탈퇴 처리중입니다");
                                progressDialog.show();
                                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                             progressDialog.dismiss();
                                             Snackbar.make(getWindow().getDecorView().getRootView()
                                                     , "탈퇴 처리되었습니다. 이용해주셔서 감사합니다 :)", Snackbar.LENGTH_LONG).show();
                                             startActivity(new Intent(AccountSettingActivity.this, LoginActivity.class));
                                        } else {
                                            progressDialog.dismiss();
                                            Snackbar.make(getWindow().getDecorView().getRootView()
                                                    , "탈퇴가 정상적으로 처리되지 않았습니다. ahn428@gmail.com으로 문의해주세요! 죄송합니다 :(", Snackbar.LENGTH_SHORT).show();

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
                if (firebaseUser == null) {
                    Intent intent = new Intent(AccountSettingActivity.this, LoginActivity.class);
                }

            }
        };
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
}
