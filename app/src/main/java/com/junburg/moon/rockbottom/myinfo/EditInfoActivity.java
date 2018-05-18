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

    // Constant
    private static final String TAG = "EditInfoActivity";
    private static final int GALLERY_CODE = 11;

    // Variable
    private String uid;

    // Views
    private RecyclerView editInfoRecyclerView;
    private Toolbar editInfoToolbar;
    private ImageView editInfoSelfieImg;
    private Button editInfoSelfieEditBtn, editInfoSelfieDeleteBtn;
    private CollapsingToolbarLayout editInfoCollapsingToolbarLayout;
    private EditInfoRecyclerAdapter editInfoRecyclerAdapter;
    private TextView editInfoAccountSettingsTxt;

    // Firebases
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseMethods firebaseMethods;

    // Objects
    private Intent intent;
    private GlideMethods glideMethods;
    private Context context;
    private ArrayList<EditInfoRecyclerData> dataList = new ArrayList<>();
    private Map<String, String> editDataMap;
    private EditInfoRecyclerData editInfoRecyclerData;
    private Uri selfieUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        initSetting();
        viewSetting();
        getIntentFromMyInfo();

        editInfoSelfieEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryPermission(view);
            }
        });

        editInfoSelfieDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUserSelfie();
            }
        });

        editInfoAccountSettingsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditInfoActivity.this, AccountSettingActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Initial setting
     */
    private void initSetting() {
        // Context
        context = EditInfoActivity.this;

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseMethods = new FirebaseMethods(context);
        uid = firebaseAuth.getCurrentUser().getUid();
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

        // Util
        glideMethods = new GlideMethods(context);

        // View
        editInfoCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.edit_info_collapsing_tool_bar_layout);
        editInfoToolbar = (Toolbar) findViewById(R.id.edit_info_tool_bar);
        editInfoSelfieImg = (ImageView) findViewById(R.id.edit_info_selfie_img);
        editInfoSelfieEditBtn = (Button) findViewById(R.id.edit_info_selfie_edit_btn);
        editInfoSelfieDeleteBtn = (Button) findViewById(R.id.edit_info_selfie_delete_btn);
        editInfoRecyclerView = (RecyclerView) findViewById(R.id.edit_info_recycler);
        editInfoAccountSettingsTxt = (TextView) findViewById(R.id.edit_info_account_settings_txt);

    }

    /**
     * Set view
     */
    private void viewSetting() {
        editInfoCollapsingToolbarLayout.setTitle("프로필 설정");
        editInfoCollapsingToolbarLayout.setExpandedTitleMarginBottom(140);

        setSupportActionBar(editInfoToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editInfoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        FragmentManager fm = getSupportFragmentManager();
        editInfoRecyclerAdapter = new EditInfoRecyclerAdapter(dataList, context, fm);
        editInfoRecyclerView.setAdapter(editInfoRecyclerAdapter);

        editInfoAccountSettingsTxt.setText(Html.fromHtml("<u>" + getResources().getString(R.string.edit_info_account_settings_txt) + "</u>"));

    }

    /**
     * 갤러리 사용 권한 획득
     * @param view
     */
    private void galleryPermission(View view) {
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

    /**
     * 뒤로가기 아이콘 사용
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 사용자 프로필 이미지 삭제
     */
    private void deleteUserSelfie() {
        editInfoSelfieImg.setImageResource(R.drawable.intro_background);
        editInfoSelfieImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
        firebaseMethods.deleteSelfieImg(uid);
    }

    /**
     * 인텐트 획득 후 사용자 정보 Set
     */
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

    /**
     * 갤러리에서 선택된 프로필 이미지를 Firebase와 ImgView에 Set
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            selfieUri = data.getData();
            firebaseMethods.setSelfieImg(selfieUri, uid);
            glideMethods.setProfileImage(selfieUri.toString(), editInfoSelfieImg, null);

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
     * 사용자 정보 리사이클러 뷰에 Set
     * @param selfie
     * @param nickName
     * @param message
     * @param teamName
     * @param github
     */
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

    /**
     * 사용자 정보 수정 다이얼로그로 부터 받아온 데이터를 통해 리사이클러 뷰 Set
     * @param dialogf
     * @param targetString
     * @param editString
     * @param position
     */
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
        if(targetString.equals("Github") && editString.equals("")) {
            editString = "Github 아이디를 등록해보세요";
        }
        editDataMap.put(targetString, editString);
        editInfoRecyclerData.setEditDataMap(editDataMap);
        editInfoRecyclerAdapter.editItemFromDialog(position, editInfoRecyclerData);
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
     * AuthStateListener 추가
     */
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    /**
     * AuthStateListener 제거
     */
    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    /**
     * Typekit for font
     * @param newBase
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
