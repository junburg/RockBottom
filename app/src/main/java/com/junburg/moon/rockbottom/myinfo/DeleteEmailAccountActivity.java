package com.junburg.moon.rockbottom.myinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.firebase.FirebaseMethods;
import com.junburg.moon.rockbottom.login.LoginActivity;

/**
 * Created by Junburg on 2018. 5. 3..
 */

public class DeleteEmailAccountActivity extends AppCompatActivity {

    private EditText deleteEmailAccountEditTxt;
    private Button deleteEmailAccountConfirmBtn, deleteEmailAccountCanecelBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseMethods firebaseMethods;

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_email_account);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        context = DeleteEmailAccountActivity.this;
        firebaseMethods = new FirebaseMethods(context);

        deleteEmailAccountEditTxt = (EditText) findViewById(R.id.delete_email_account_edit_txt);
        deleteEmailAccountConfirmBtn = (Button) findViewById(R.id.delete_email_account_confirm_btn);
        deleteEmailAccountConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = deleteEmailAccountEditTxt.getText().toString();
                if (firebaseUser.getProviders() != null && firebaseUser.getProviders().get(0).equals("google.com")) {
                    deleteGoogleUserInfo(password);
                }

                if (firebaseUser.getProviders() != null && firebaseUser.isEmailVerified()) {
                    deleteEmailUserInfo(password);
                }

            }

        });

        deleteEmailAccountCanecelBtn = (Button) findViewById(R.id.delete_email_account_cancel_btn);
        deleteEmailAccountCanecelBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener()

        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    Intent intent = new Intent(DeleteEmailAccountActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }

        ;
    }

    private void deleteEmailUserInfo(String password) {

        AuthCredential credential = GoogleAuthProvider.getCredential(firebaseUser.getEmail(), password);
        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                deleteProcess();
            }
        });
    }

    private void deleteGoogleUserInfo(String password) {

        AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), password);
        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                deleteProcess();
            }
        });
    }

    private void deleteProcess() {
        final String uid = firebaseUser.getUid();
        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    databaseReference.child("user_study_condition").child(uid).removeValue();
                    databaseReference.child("users").child(uid).removeValue();
                    firebaseMethods.deleteSelfieImgOnlyStorage(uid);
                    Snackbar.make(getWindow().getDecorView().getRootView()
                            , "탈퇴 처리되었습니다. 이용해주셔서 감사합니다 :)", Snackbar.LENGTH_LONG).show();
                    Intent intent = new Intent(DeleteEmailAccountActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "정확한 비밀번호를 입력해주세요"
                            , Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
