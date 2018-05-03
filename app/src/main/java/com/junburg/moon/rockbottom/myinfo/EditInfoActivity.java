package com.junburg.moon.rockbottom.myinfo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.firebase.FirebaseMethods;
import com.junburg.moon.rockbottom.glide.GlideMethods;
import com.junburg.moon.rockbottom.login.LoginActivity;
import com.junburg.moon.rockbottom.model.User;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Junburg on 2018. 2. 26..
 */

public class EditInfoActivity extends AppCompatActivity implements EditInfoDialogFragment.EditInfoDialogFragmentListener {
    private static final String TAG = "EditInfoActivity";
    // Constant
    private static final int GALLERY_CODE = 11;

    // Variables
    private ArrayList<EditInfoRecyclerData> dataList = new ArrayList<>();
    private Map<String, String> editDataMap;
    private EditInfoRecyclerData editInfoRecyclerData;
    private Uri selfieUri;
    private User user;
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
    private TextView editInfoAccountSettingsTxt;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private String uid;
    private FirebaseStorage storage;
    private FirebaseMethods firebaseMethods;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
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
        editInfoAccountSettingsTxt = (TextView) findViewById(R.id.edit_info_account_settings_txt);
        editInfoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        FragmentManager fm = getSupportFragmentManager();
        editInfoRecyclerAdapter = new EditInfoRecyclerAdapter(dataList, context, fm);
        editInfoRecyclerView.setAdapter(editInfoRecyclerAdapter);
        editInfoSelfieEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean galleryPermission = ContextCompat.checkSelfPermission(view.getContext()
                        , Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (galleryPermission) {
                    pickUpPicture();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(EditInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        ActivityCompat.requestPermissions(EditInfoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {
                        Snackbar.make(getWindow().getDecorView().getRootView()
                                , "사진을 등록을 위해선 권한 설정이 필요합니다. 설정에서 권한을 부여해주세요", Snackbar.LENGTH_LONG).show();

                    }
                    ActivityCompat.requestPermissions(EditInfoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                }
            }
        });
        editInfoSelfieDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUserSelfie();
            }
        });

        editInfoAccountSettingsTxt.setText(Html.fromHtml("<u>" + getResources().getString(R.string.edit_info_account_settings_txt) + "</u>"));
        editInfoAccountSettingsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditInfoActivity.this, AccountSettingActivity.class);
                startActivity(intent);
            }
        });
        getIntentFromMyInfo();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    Intent intent = new Intent(EditInfoActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }
        };


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteUserSelfie() {
        editInfoSelfieImg.setImageResource(R.drawable.intro_background);
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
        Log.d(TAG, "getIntentFromMyInfo: " + teamName);
        setUserData(selfie, nickName, message, teamName, github);
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
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("정보를 가져오고 있습니다");
        progressDialog.show();
        if(message.equals("")) {
            message = "메세지를 입력해보세요";
        }
        if(teamName.equals("")) {
            teamName = "소속을 입력해보세요";
        }
        if(github.equals("")) {
            github = "Github 아이디를 입력해보세요";
        }
        glideMethods.setProfileImage(selfie, editInfoSelfieImg, progressDialog);
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
        if(targetString.equals("메세지") && editString.equals("")) {
            editString = "메세지를 등록해보세요";
        }
        if(targetString.equals("소속") && editString.equals("")) {
            editString = "소속을 등록해보세요";
        }
        if(targetString.equals("Github") | editString.equals("")) {
            editString = "Github 아이디를 등록해보세요";
        }
        editDataMap.put(targetString, editString);
        editInfoRecyclerData.setEditDataMap(editDataMap);
        editInfoRecyclerAdapter.editItemFromDialog(position, editInfoRecyclerData);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickUpPicture();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        auth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
