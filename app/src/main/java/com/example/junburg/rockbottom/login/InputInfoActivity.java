package com.example.junburg.rockbottom.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.junburg.rockbottom.main.MainActivity;
import com.example.junburg.rockbottom.R;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Junburg on 2018. 2. 2..
 */

public class InputInfoActivity extends AppCompatActivity {
    private static final int GALLERY_CODE = 10;

    private ImageView inputInfoSelfieImg;
    private TextInputEditText inputInfoNickNameEdit;
    private TextInputEditText inputInfoMessageEdit;
    private TextInputEditText inputInfoTeamNameEdit;
    private TextInputEditText inputInfoGithubEdit;
    private Button inputInfoDoneBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        inputInfoSelfieImg = (ImageView)findViewById(R.id.input_info_selfie_img);
        inputInfoSelfieImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickUpPicture();
            }
        });
        inputInfoNickNameEdit = (TextInputEditText) findViewById(R.id.input_info_nick_name_edit_txt);
        inputInfoMessageEdit = (TextInputEditText)findViewById(R.id.input_info_message_edit_txt);
        inputInfoTeamNameEdit = (TextInputEditText)findViewById(R.id.input_info_team_name_edit_txt);
        inputInfoGithubEdit = (TextInputEditText)findViewById(R.id.input_info_github_edit_txt);
        inputInfoDoneBtn = (Button)findViewById(R.id.input_info_done_btn);
        inputInfoDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InputInfoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Glide.with(this)
                    .load(uri)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .crossFade()
                    .into(inputInfoSelfieImg);
        }
    }

    private void pickUpPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }
}
