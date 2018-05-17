package com.junburg.moon.rockbottom.login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.firebase.FirebaseMethods;
import com.junburg.moon.rockbottom.main.MainActivity;
import com.junburg.moon.rockbottom.model.User;
import com.junburg.moon.rockbottom.util.DataExistCallback;
import com.junburg.moon.rockbottom.util.ValidationCheck;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.File;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Junburg on 2018. 2. 2..
 */

public class InputInfoActivity extends AppCompatActivity {

    // Constants
    private static final String TAG = "InputInfoActivity";
    private static final int GALLERY_CODE = 10;

    // Variables
    protected boolean isGlideUsed = false;
    private ValidationCheck validationCheck;
    private Context context;
    private boolean checkOk;
    private InputMethodManager inputMethodManager;

    // Views
    protected ImageView inputInfoSelfieImg;
    private TextInputEditText inputInfoNickNameEdit;
    private TextInputEditText inputInfoMessageEdit;
    private TextInputEditText inputInfoTeamNameEdit;
    private TextInputEditText inputInfoGithubEdit;
    private Button inputInfoDoneBtn;
    private ProgressDialog progressDialog;
    private TextView inputInfoDoubleCheckTxt;

    // Firebases
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private String uid;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener authStateListener;

    // Objects
    private Uri selfieUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_info);
        initSetup();

        // 사용자 권한 체크
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        // Glide 사용여부를 체크(ImgView에 사진이 set 되었는가를 체크)
        // Glide 사용 o -> 사진삭제 다이얼로그
        // Gilde 사용 x -> 권한 체크 후 갤러리로 이동
        inputInfoSelfieImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGlideUsed == true) {
                    InputInfoSelfieClickDialog dialog
                            = new InputInfoSelfieClickDialog(InputInfoActivity.this);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(false);
                    dialog.show();
                } else {
                    boolean galleryPermission = ContextCompat.checkSelfPermission(view.getContext()
                            , Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

                    if (galleryPermission) {
                        pickUpPicture();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(InputInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            ActivityCompat.requestPermissions(InputInfoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                        } else {
                            Snackbar.make(getWindow().getDecorView().getRootView()
                                    , "사진을 등록을 위해선 권한 설정이 필요합니다. 설정에서 권한을 부여해주세요", Snackbar.LENGTH_LONG).show();

                        }
                        ActivityCompat.requestPermissions(InputInfoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    }
                }
            }
        });

        // 사용자 정보 입력 값에 대한 중복검사 완료 유무와 유효성 체크
        inputInfoDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean nickNameEmptyCheck = validationCheck.infoEmptyCheck(inputInfoNickNameEdit);
                Log.d("empty", nickNameEmptyCheck + "");
                boolean lengthCheck = validationCheck.infoLengthCheck(inputInfoNickNameEdit, inputInfoMessageEdit
                        , inputInfoTeamNameEdit, inputInfoGithubEdit);
                Log.d("length", lengthCheck + "");

                if (!checkOk) {
                    Snackbar.make(view, "닉네임 중복검사를 해주세요 :)", Snackbar.LENGTH_SHORT).show();

                } else {
                    if ((nickNameEmptyCheck & lengthCheck) == false) {
                        Snackbar.make(view, "공백과 길이를 확인해주세요. 닉네임은 필수입니다 :)", Snackbar.LENGTH_SHORT).show();
                    } else {
                        userInitialize();
                        Snackbar.make(view, "정보 확인 완료 :)", Snackbar.LENGTH_SHORT).show();

                    }
                }
            }
        });

        // 닉네임 값에 대한 중복검사
        inputInfoDoubleCheckTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                nickNameDoubleCheck(inputInfoNickNameEdit.getText().toString(), new DataExistCallback() {
                    @Override
                    public void onDataExistCheck(boolean check) {
                        inputMethodManager.hideSoftInputFromWindow(inputInfoDoubleCheckTxt.getWindowToken(), 0);
                        if (inputInfoNickNameEdit.getText().length() > 20 || inputInfoNickNameEdit.getText().toString().equals("")) {
                            Snackbar.make(view, "닉네임은 공백과 20자 이상은 허용하지 않아요 :)", Snackbar.LENGTH_SHORT).show();

                        } else {
                            if (check) {
                                Snackbar.make(view, "중복되는 닉네임이 존재합니다 :)", Snackbar.LENGTH_SHORT).show();
                                checkOk = false;
                            } else {
                                Snackbar.make(view, "사용가능한 닉네임 입니다 :)", Snackbar.LENGTH_SHORT).show();
                                checkOk = true;
                            }
                        }
                    }
                });
            }
        });


    }

    /**
     * Initialize activity
     */
    private void initSetup() {

        // Context
        context = InputInfoActivity.this;

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    Intent intent = new Intent(InputInfoActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        };

        // View
        inputInfoSelfieImg = (ImageView) findViewById(R.id.input_info_selfie_img);
        inputInfoNickNameEdit = (TextInputEditText) findViewById(R.id.input_info_nick_name_edit_txt);
        inputInfoMessageEdit = (TextInputEditText) findViewById(R.id.input_info_message_edit_txt);
        inputInfoTeamNameEdit = (TextInputEditText) findViewById(R.id.input_info_team_name_edit_txt);
        inputInfoGithubEdit = (TextInputEditText) findViewById(R.id.input_info_github_edit_txt);
        inputInfoDoneBtn = (Button) findViewById(R.id.input_info_done_btn);
        inputInfoDoubleCheckTxt = (TextView) findViewById(R.id.input_info_double_check_txt);

        // Util
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        validationCheck = new ValidationCheck(context);
        firebaseMethods = new FirebaseMethods(context);



    }

    /**
     * 갤러리 액티비티에서 넘겨준 값을 받아 이미지 Set
     * 이미지 Set -> isGlideUsed = true (Glide사용 여부 -> 이미지 Set여부 판별)
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            selfieUri = data.getData();
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("이미지 로딩중 입니다");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Glide.with(this)
                    .load(selfieUri)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .crossFade()
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            inputInfoSelfieImg.setImageDrawable(resource);
                            progressDialog.dismiss();
                            isGlideUsed = true;
                        }
                    });
        }
    }

    /**
     * 갤러리 액티비티로 이동
     */
    private void pickUpPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }

    /**
     * 이미지 경로 획득
     * @param uri
     * @return
     */
    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(index);
    }

    /**
     * 프로필 이미지 Set 여부에 따라 사용자 정보 initialize
     *
     */
    public void userInitialize() {

        Log.d("isGlideUsed?", isGlideUsed + "");
        final ProgressDialog progressDialog = new ProgressDialog(InputInfoActivity.this);
        progressDialog.setMessage("가입중입니다");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (isGlideUsed == false) {
            selfieUri = null;
            User user = setUserData(selfieUri);
            firebaseDatabase.getReference().child("users").child(uid).setValue(user);
            firebaseMethods.initUserConditionSetting(uid, progressDialog);
            startActivity(new Intent(InputInfoActivity.this, MainActivity.class));
            finish();

        } else {
            StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://rockbottom-2bc4e.appspot.com");
            Uri file = Uri.fromFile(new File(getPath(selfieUri)));
            StorageReference riversRef = storageReference.child("users/" + "selfieImages/" + uid + "_selfie");
            UploadTask uploadTask = riversRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Snackbar.make(getWindow().getDecorView().getRootView(), "다시 한번 시도해주세요!", Snackbar.LENGTH_LONG).show();

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    User user = setUserData(downloadUrl);
                    Log.d(TAG, "onSuccess: " + user.toString());
                    firebaseDatabase.getReference().child("users").child(uid).setValue(user);
                    firebaseMethods.initUserConditionSetting(uid, progressDialog);
                    startActivity(new Intent(InputInfoActivity.this, MainActivity.class));
                    finish();
                }
            });
        }
    }

    /**
     * User 객체 생성 후, 사용자 정보 데이터 Set
     * @param uri
     * @return
     */
    private User setUserData(Uri uri) {
        User user = new User();
        if (selfieUri == null) {
            user.setSelfieUri("");
        } else {
            user.setSelfieUri(uri.toString());
        }
        if(firebaseUser.getEmail() == null) {
            user.setEmail("");
        } else {
            user.setEmail(firebaseUser.getEmail().toString());
        }

        user.setNickName(inputInfoNickNameEdit.getText().toString());
        user.setMessage(inputInfoMessageEdit.getText().toString());
        user.setTeamName(inputInfoTeamNameEdit.getText().toString());
        user.setGithub(inputInfoGithubEdit.getText().toString());
        user.setPoints(0);

        return user;
    }

    /**
     * 권한 체크 여부 확인 ->  갤러리 액티비티로 이동
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickUpPicture();
            }
        }
    }

    /**
     * Typekit for font
     * @param newBase
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    /**
     * 닉네임 중복검사
     * @param nickName
     * @param dataExtistCallback
     */
    private void nickNameDoubleCheck(final String nickName, final DataExistCallback dataExtistCallback) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        final ProgressDialog progressDialog = new ProgressDialog(InputInfoActivity.this);
        progressDialog.setMessage("중복 검사중입니다");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Query query = databaseReference.child("users").orderByChild("nickName").equalTo(nickName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean doubleCheck = dataSnapshot.exists();
                dataExtistCallback.onDataExistCheck(doubleCheck);
                progressDialog.dismiss();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * AuthStateListener 추가
     */
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    /**
     * AuthStateListener 삭제
     */
    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    /**
     * 뒤로가기 버튼을 누르면 로그아웃 후 MainActivity로 이동
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Intent intent = new Intent(InputInfoActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {

                }
            }
        });
    }
}
