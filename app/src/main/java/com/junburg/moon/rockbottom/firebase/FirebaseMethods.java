package com.junburg.moon.rockbottom.firebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.junburg.moon.rockbottom.login.EmailLoginActivity;
import com.junburg.moon.rockbottom.login.EmailSignUpActivity;
import com.junburg.moon.rockbottom.login.InputInfoActivity;
import com.junburg.moon.rockbottom.main.MainActivity;
import com.junburg.moon.rockbottom.model.Chapter;
import com.junburg.moon.rockbottom.model.Subject;
import com.junburg.moon.rockbottom.model.User;
import com.junburg.moon.rockbottom.util.GetPath;
import com.junburg.moon.rockbottom.util.ValidationCheck;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by Junburg on 2018. 4. 16..
 */

public class FirebaseMethods {

    // Variables
    private Context context;
    private boolean firstEmailLogin;
    private String uid;
    private List<Chapter> chapterList = new ArrayList<>();
    // Firebase
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    public FirebaseMethods(Context context) {
        this.context = context;
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReferenceFromUrl("gs://rockbottom-2bc4e.appspot.com");
        if (auth.getCurrentUser() != null) {
            uid = auth.getCurrentUser().getUid();
        } else {

        }
    }


    public void registerNewEmail(String email, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("가입 진행중 입니다");
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "fuck", Toast.LENGTH_SHORT).show();
                        } else if (task.isSuccessful()) {
                            sendVerificationEmail();
                            progressDialog.dismiss();
                        }
                    }
                });

    }

    private void sendVerificationEmail() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Snackbar
                                        .make(
                                                ((EmailSignUpActivity) context).getWindow()
                                                        .getDecorView().findViewById(android.R.id.content)
                                                , "인증 메일을 보냈습니다! 확인 후 로그인 하세요 :)"
                                                , Snackbar.LENGTH_SHORT)
                                        .show();
                            } else {
                                Snackbar
                                        .make(
                                                ((EmailSignUpActivity) context).getWindow()
                                                        .getDecorView().findViewById(android.R.id.content)
                                                , "해당 이메일로 인증 메일을 발송할 수 없습니다."
                                                , Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
        }
    }

    public void loginEmail(String email, String password) {
        ValidationCheck validationCheck = new ValidationCheck(context);
        if (validationCheck.isEmailString(email)
                && validationCheck.blankStringCheck(email, password)
                && validationCheck.isPasswordPattern(password)) {

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (!task.isSuccessful()) {
                                Snackbar
                                        .make(((EmailLoginActivity) context).getWindow().getDecorView().getRootView()
                                                , "로그인 실패"
                                                , Snackbar.LENGTH_SHORT)
                                        .show();
                            } else {
                                if (user.isEmailVerified()) {
                                    isFirstEmailLogin(user.getUid());
                                    if (firstEmailLogin == true) {
                                        Intent intent = new Intent(context, InputInfoActivity.class);
                                        context.startActivity(intent);
                                    }
                                    Intent intent = new Intent(context, MainActivity.class);
                                    context.startActivity(intent);
                                } else {
                                    Snackbar
                                            .make(((EmailLoginActivity) context).getWindow().getDecorView().getRootView()
                                                    , "이메일 인증 메일을 확인하지 않으셨어요"
                                                    , Snackbar.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        }
                    });

        }

    }

    private void isFirstEmailLogin(String uid) {
        databaseReference = databaseReference.child("users").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange1: " + dataSnapshot.getValue());
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    firstEmailLogin = true;
                } else {
                    firstEmailLogin = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d(TAG, "isFirstEmailLogin: " + firstEmailLogin);
    }

//    private void initEmailLogin(String uid) {
//        User userData = new User();
//        userData.setSelfieUri("");
//        userData.setNickName("닉네임을 수정해주세요");
//        userData.setMessage("메세지를 입력해주세요");
//        userData.setTeamName("소속을 수정해주세요");
//        userData.setGithub("깃헙 주소를 수정해주세요");
//        userData.setPoints(0);
//        userData.setRanking(0);
//        databaseReference.child("users").child(uid).setValue(userData);
//
//    }

    public User setProfileInfo(DataSnapshot dataSnapshot) {
        User user = new User();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (ds.getKey().equals("users")) {
                try {
                    Log.d(TAG, "uid: " + uid);
                    user.setSelfieUri(
                            ds.child(uid)
                                    .getValue(User.class)
                                    .getSelfieUri()
                    );

                    user.setNickName(
                            ds.child(uid)
                                    .getValue(User.class)
                                    .getNickName()
                    );

                    user.setMessage(
                            ds.child(uid)
                                    .getValue(User.class)
                                    .getMessage()
                    );

                    user.setGithub(
                            ds.child(uid)
                                    .getValue(User.class)
                                    .getGithub()
                    );

                    user.setTeamName(
                            ds.child(uid)
                                    .getValue(User.class)
                                    .getTeamName()
                    );

                    user.setPoints(
                            ds.child(uid)
                                    .getValue(User.class)
                                    .getPoints()
                    );

                    user.setRanking(
                            ds.child(uid)
                                    .getValue(User.class)
                                    .getRanking()
                    );
                } catch (NullPointerException e) {
                    Log.d(TAG, "setProfileInfoNullException: " + e.getMessage());
                }
            }

        }
        return user;
    }

    public void setSelfieImg(Uri selfieUri, final String uid) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("사진을 등록중입니다.");
        progressDialog.setCancelable(false);
        progressDialog.show();
        GetPath getPath = new GetPath(context);
        Uri file = Uri.fromFile(new File(getPath.getPathUri(selfieUri)));
        Log.d(TAG, "setSelfieImg: " + file.toString());
        StorageReference riversRef = storageReference.child("users/" + "selfieImages/" + uid + "_selfie");
        if (riversRef != null) {
            riversRef.delete();
        }
        UploadTask uploadTask = riversRef.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests")
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                firebaseDatabase.getReference().child("users").child(uid).child("selfieUri").setValue(downloadUrl.toString());
                progressDialog.dismiss();
            }
        });
    }

    public void deleteSelfieImg(String uid) {
        Log.d(TAG, "deleteSelfieImg: " + uid.toString());
        StorageReference deleteRef = storageReference.child("users/" + "selfieImages/" + uid + "_selfie");
        if (deleteRef != null) {
            deleteRef.delete();
        }
        firebaseDatabase.getReference().child("users").child(uid).child("selfieUri").setValue("");


    }

    public void initUserLearnSettings(final String uid) {
        databaseReference.child("subject").child("chapter").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Chapter chapter = ds.getValue(Chapter.class);
                    String chapterId = chapter.getChapter_id();
                    databaseReference.child("user_learn_settings").child(uid).push();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void initUserConditionSetting(final String uid) {
        Log.d(TAG, "initUserConditionSetting: " + "come");
            final Map<String, Object> chapterMap = new HashMap<>();
            databaseReference.child("subject").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Subject subject = ds.getValue(Subject.class);

                        Map<String, Object> nameMap = new HashMap<>();
                        nameMap.put("name", subject.getName());
                        databaseReference.child("user_study_condition").child(uid).child(subject.getSubject_id()).setValue(nameMap);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

    }

    public void checkUserConditionSetting(final String uid) {
        Log.d(TAG, "checkUserConditionSetting: " + "ok");
        databaseReference.child("user_study_condition").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null) {
                    Log.d(TAG, "null: " + "null");
                    initUserConditionSetting(uid);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




}


