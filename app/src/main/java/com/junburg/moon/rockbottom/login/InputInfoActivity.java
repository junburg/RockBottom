package com.junburg.moon.rockbottom.login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.main.MainActivity;

import java.io.File;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Junburg on 2018. 2. 2..
 */

public class InputInfoActivity extends AppCompatActivity {
    private static final int GALLERY_CODE = 10;
    private Uri selfieUri;
    protected boolean isGlideUsed = false;

    // View
    protected ImageView inputInfoSelfieImg;
    private TextInputEditText inputInfoNickNameEdit;
    private TextInputEditText inputInfoMessageEdit;
    private TextInputEditText inputInfoTeamNameEdit;
    private TextInputEditText inputInfoGithubEdit;
    private Button inputInfoDoneBtn;
    private ProgressDialog progressDialog;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private String uid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_info);
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        inputInfoSelfieImg = (ImageView) findViewById(R.id.input_info_selfie_img);
        inputInfoSelfieImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGlideUsed == true) {
                    InputInfoSelfieClickDialog dialog
                            = new InputInfoSelfieClickDialog(InputInfoActivity.this);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                } else {
                    pickUpPicture();
                }
            }
        });
        inputInfoNickNameEdit = (TextInputEditText) findViewById(R.id.input_info_nick_name_edit_txt);
        inputInfoMessageEdit = (TextInputEditText) findViewById(R.id.input_info_message_edit_txt);
        inputInfoTeamNameEdit = (TextInputEditText) findViewById(R.id.input_info_team_name_edit_txt);
        inputInfoGithubEdit = (TextInputEditText) findViewById(R.id.input_info_github_edit_txt);
        inputInfoDoneBtn = (Button) findViewById(R.id.input_info_done_btn);
        inputInfoDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean nickNameEmptyCheck = infoEmptyCheck(inputInfoNickNameEdit);
                Log.d("empty", nickNameEmptyCheck + "");
                boolean lengthCheck = infoLengthCheck(inputInfoNickNameEdit, inputInfoMessageEdit
                        , inputInfoTeamNameEdit, inputInfoGithubEdit);
                Log.d("length", lengthCheck + "");

                if ((nickNameEmptyCheck & lengthCheck) == false) {
                    Snackbar.make(view, "공백과 길이를 확인해주세요. 닉네임은 필수입니다 :)", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(view, "정보 확인 완료 :)", Snackbar.LENGTH_SHORT).show();
                    userInitialize();
                    startActivity(new Intent(InputInfoActivity.this, MainActivity.class));
                    finish();

                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            selfieUri = data.getData();
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("이미지 로딩중 입니다");
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

    private void pickUpPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }

    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }

    public void userInitialize() {
//        Log.d("drawable", inputInfoSelfieImg.getDrawable().toString());
//        Log.d("drawable2", ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_selfie_img).toString());
//
//
//        Drawable temp = inputInfoSelfieImg.getDrawable();
//        Drawable temp1 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_selfie_img);
//
//        Bitmap tmpBitmap = ((BitmapDrawable) temp).getBitmap();

        Log.d("isGlideUsed?", isGlideUsed + "");

        if (isGlideUsed == false) {
            selfieUri = null;
            UserData userData = setUserData(selfieUri);
            database.getReference().child("users").child(uid).setValue(userData);

        } else {
            StorageReference storageReference = storage.getReferenceFromUrl("gs://rockbottom-2bc4e.appspot.com");
            Uri file = Uri.fromFile(new File(getPath(selfieUri)));
            StorageReference riversRef = storageReference.child("selfieImages/" + file.getLastPathSegment());
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
                    UserData userData = setUserData(downloadUrl);
                    database.getReference().child("users").child(uid).setValue(userData);

                }
            });
        }
    }

    private boolean infoEmptyCheck(TextInputEditText editText) {
        String text = editText.getText().toString();
        if (text.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    private boolean infoLengthCheck(TextInputEditText nickNameEditTxt, TextInputEditText messageEditTxt
            , TextInputEditText teamNameEditTxt, TextInputEditText githubEditTxt) {
        String nickName = nickNameEditTxt.getText().toString();
        String message = messageEditTxt.getText().toString();
        String teamName = teamNameEditTxt.getText().toString();
        String github = githubEditTxt.getText().toString();

        if (nickName.length() > 20 || message.length() > 40 || teamName.length() > 20 || github.length() > 40) {
            return false;
        } else {
            return true;
        }
    }

    private UserData setUserData(Uri uri) {
        UserData userData = new UserData();
        if (selfieUri == null) {
            userData.setSelfieUri(null);
        } else {
            userData.setSelfieUri(uri.toString());
        }
        userData.setNickName(inputInfoNickNameEdit.getText().toString());
        userData.setMessage(inputInfoMessageEdit.getText().toString());
        userData.setTeamName(inputInfoTeamNameEdit.getText().toString());
        userData.setGithub(inputInfoGithubEdit.getText().toString());
        userData.setPoints(0);
        userData.setRanking(0);

        return userData;
    }
}
