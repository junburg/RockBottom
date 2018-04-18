package com.junburg.moon.rockbottom.myinfo;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.firebase.FirebaseMethods;
import com.junburg.moon.rockbottom.glide.GlideMethods;
import com.junburg.moon.rockbottom.login.LoginActivity;
import com.junburg.moon.rockbottom.model.UserData;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Junburg on 2018. 2. 26..
 */

public class EditInfoActivity extends AppCompatActivity implements EditInfoDialogFragment.EditInfoDialogFragmentListener{
    private static final String TAG = "EditInfoActivity";
    // Constant
    private static final int GALLERY_CODE = 11;

    // Variables
    private ArrayList<EditInfoRecyclerData> dataList = new ArrayList<>();
    private Map<String, String> editDataMap;
    private EditInfoRecyclerData editInfoRecyclerData;
    private Uri selfieUri;
    private UserData userData;
    private Intent intent;
    private GlideMethods glideMethods;
    private Context context;

    // Widgets
    private RecyclerView editInfoRecyclerView;
    private Toolbar editInfoToolbar;
    private ImageView editInfoSelfieImg;
    private Button editInfoSelfieEditBtn, editInfoSelfieDeleteBtn;
    private CollapsingToolbarLayout editInfoCollapsingToolbarLayout;
    private EditInfoRecyclerAdapter editInfoRecyclerAdapter;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String uid;
    private FirebaseStorage storage;
    private FirebaseMethods firebaseMethods;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        storage = FirebaseStorage.getInstance();
        context = EditInfoActivity.this;
        firebaseMethods = new FirebaseMethods(context);
        glideMethods = new GlideMethods(context);

        editInfoCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.edit_info_collapsing_tool_bar_layout);
        editInfoCollapsingToolbarLayout.setTitle("프로필 설정");
        editInfoCollapsingToolbarLayout.setExpandedTitleMarginBottom(140);
        editInfoToolbar = (Toolbar) findViewById(R.id.edit_info_tool_bar);
        setSupportActionBar(editInfoToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editInfoSelfieImg = (ImageView) findViewById(R.id.edit_info_selfie_img);
        editInfoSelfieEditBtn = (Button) findViewById(R.id.edit_info_selfie_edit_btn);
        editInfoSelfieDeleteBtn = (Button) findViewById(R.id.edit_info_selfie_delete_btn);
        editInfoRecyclerView = (RecyclerView) findViewById(R.id.edit_info_recycler);
        editInfoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        FragmentManager fm = getSupportFragmentManager();
        editInfoRecyclerAdapter = new EditInfoRecyclerAdapter(dataList, context, fm);
        editInfoRecyclerView.setAdapter(editInfoRecyclerAdapter);
        editInfoSelfieEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickUpPicture();
            }
        });
        editInfoSelfieDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUserSelfie();
            }
        });

        getIntentFromMyInfo();


    }

    private void deleteUserSelfie() {
        editInfoSelfieImg.setImageResource(R.drawable.rock_bottom_logo);
        editInfoSelfieImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
        firebaseMethods.deleteSelfieImg(uid);
    }

    private void getIntentFromMyInfo() {
        intent = getIntent();
        String selfie = intent.getStringExtra("selfieUri");
        String nickName = intent.getStringExtra("nickName");
        String message = intent.getStringExtra("message");
        String github = intent.getStringExtra("github");
        String teamName = intent.getStringExtra("teamName");
        setUserData(selfie, nickName, message, teamName, github);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.edit_menu_logout:
                auth.signOut();
                finish();
                startActivity(new Intent(EditInfoActivity.this, LoginActivity.class));
                break;
            case R.id.edit_menu_signout:

                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            selfieUri = data.getData();
            firebaseMethods.setSelfieImg(selfieUri, uid);
            Glide.with(this)
                    .load(selfieUri)
                    .crossFade()
                    .into(editInfoSelfieImg);

        }
    }

    private void pickUpPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }

    private void setUserData(String selfie, String nickName, String message, String teamName, String github) {
        glideMethods.setProfileImage(selfie, editInfoSelfieImg);
        for (int i = 0; i < 4; i++) {
            editInfoRecyclerData = new EditInfoRecyclerData();
            editDataMap = new HashMap<>();
            switch (i) {
                case 0:
                    editDataMap.put("닉네임", nickName);
                    break;
                case 1:
                    editDataMap.put("메세지", message);
                    break;
                case 2:
                    editDataMap.put("소속", teamName);
                    break;
                case 3:
                    editDataMap.put("Github", github);
                    break;
            }
            editInfoRecyclerData.setEditDataMap(editDataMap);
            dataList.add(editInfoRecyclerData);
        }
    }


    @Override
    public void onEditInfoClick(DialogFragment dialogf, String targetString, String editString, int position) {
        editInfoRecyclerData = new EditInfoRecyclerData();
        editDataMap = new HashMap<>();
        editDataMap.put(targetString, editString);
        editInfoRecyclerData.setEditDataMap(editDataMap);
        editInfoRecyclerAdapter.editItemFromDialog(position, editInfoRecyclerData);
    }
}
