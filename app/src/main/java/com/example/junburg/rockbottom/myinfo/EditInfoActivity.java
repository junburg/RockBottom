package com.example.junburg.rockbottom.myinfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.junburg.rockbottom.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Junburg on 2018. 2. 26..
 */

public class EditInfoActivity extends AppCompatActivity {
    private static final int GALLERY_CODE = 11;
    private ArrayList<EditInfoRecyclerData> dataList = new ArrayList<>();
    private Map<String, String> editDataMap;
    private EditInfoRecyclerData editInfoRecyclerData;
    private RecyclerView editInfoRecyclerView;
    private Toolbar editInfoToolbar;
    private ImageView editInfoSelfieImg;
    private Button editInfoSelfieBtn;
    private CollapsingToolbarLayout editInfoCollapsingToolbarLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        editInfoCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.edit_info_collapsing_tool_bar_layout);
        editInfoCollapsingToolbarLayout.setTitle("내 정보");
        editInfoToolbar = (Toolbar) findViewById(R.id.edit_info_tool_bar);
        setSupportActionBar(editInfoToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        editInfoSelfieImg = (ImageView) findViewById(R.id.edit_info_selfie_img);
        editInfoSelfieBtn = (Button) findViewById(R.id.edit_info_selfie_btn);
        createDummyData();
        editInfoRecyclerView = (RecyclerView) findViewById(R.id.edit_info_recycler);
        editInfoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        EditInfoRecyclerAdapter adapter = new EditInfoRecyclerAdapter(dataList);
        editInfoRecyclerView.setAdapter(adapter);
        editInfoSelfieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickUpPicture();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Glide.with(this)
                    .load(uri)
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

//
//    private void alertDialogForEdit(String title, String message, final int id) {
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//
//        alert.setTitle(title);
//        alert.setMessage(message);
//
//        final EditText editName = new EditText(this);
//        alert.setView(editName);
//
//        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                String text = editName.getText().toString();
//                if(id == R.id.edit_user_name_txt) {
//                    editUserNameTxt.setText(text);
//                } else if (id == R.id.edit_message_txt) {
//                    editMessageTxt.setText(text);
//                }
//            }
//        });
//
//        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        alert.show();
//    }

    private void createDummyData() {
        for (int i = 0; i < 4; i++) {
            editInfoRecyclerData = new EditInfoRecyclerData();
            editDataMap = new HashMap<>();
            switch (i) {
                case 0:
                    editDataMap.put("닉네임", "준벅");
                    break;
                case 1:
                    editDataMap.put("메세지", "내가 짱이다. 그니까 내 맘대로 한다. 알았냐?");
                    break;
                case 2:
                    editDataMap.put("소속", "백수");
                    break;
                case 3:
                    editDataMap.put("Github", "Github.com/junhyeock");
                    break;

            }

            editInfoRecyclerData.setEditDataMap(editDataMap);
            dataList.add(editInfoRecyclerData);
        }
    }
}
