package com.junburg.moon.rockbottom.learn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.model.Learn;
import com.junburg.moon.rockbottom.util.ProcessContent;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Junburg on 2018. 3. 4..
 */

public class LearnActivity extends AppCompatActivity {

    private static final String TAG = "LearnActivity";
    private RecyclerView learnRecycler;
    private LearnRecyclerAdapter learnRecyclerAdapter;
    private Button learnDoneBtn;
    private Button learnLaterBtn;
    private TextView learnTxt;

    private Intent intent;
    private String chapterId;
    private int position;
    private String content;
    private ProcessContent processContent;
    private List<String> titleList;
    private List<String> bodyList;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        processContent = new ProcessContent();
        intent = getIntent();
        chapterId = intent.getStringExtra("chapterId");
        position = intent.getIntExtra("position", 0);
        Log.d(TAG, "onCreate: " + chapterId + position);
        learnTxt = (TextView)findViewById(R.id.learn_txt);
        getLearnData();

        learnRecycler = (RecyclerView)findViewById(R.id.learn_recycler);
        learnRecycler.setHasFixedSize(true);
        learnRecycler.setLayoutManager(new LinearLayoutManager(this));
        learnRecyclerAdapter = new LearnRecyclerAdapter(titleList, bodyList);
        learnRecycler.setAdapter(learnRecyclerAdapter);
        learnDoneBtn = (Button)findViewById(R.id.learn_done_btn);
        learnLaterBtn = (Button)findViewById(R.id.learn_later_btn);

        learnDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        learnLaterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

//    private void createDummyData() {
//        for(int i=1; i<10; i++) {
//            LearnRecyclerData data = new LearnRecyclerData("subject" + i, "content" + i );
//            dataList.add(data);
//        }
//
//    }

    private void getLearnData() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("learn").child(chapterId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String content = ds.getValue(String.class);
                    Map<String, String> contentMap = new HashMap<>();
                    contentMap = processContent.divideContent(content);
                    searchMap(contentMap);
                    Log.d(TAG, "onDataChange: " + titleList.toString());
                    Log.d(TAG, "onDataChange: " + bodyList.toString());
                    learnRecyclerAdapter.setData(titleList, bodyList);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void searchMap(Map<String, String> contentMap) {
        titleList = new ArrayList<>();
        bodyList = new ArrayList<>();
        Iterator<String> iterator = contentMap.keySet().iterator();
        while (iterator.hasNext()) {
            String title = (String)iterator.next();
            titleList.add(title);
            bodyList.add(contentMap.get(title));
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
