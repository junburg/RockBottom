package com.junburg.moon.rockbottom.learn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.login.LoginActivity;
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

    // Widgets
    private RecyclerView learnRecycler;
    private Button learnDoneBtn;
    private Button learnLaterBtn;

    // Adapters
    private LearnRecyclerAdapter learnRecyclerAdapter;

    // Variables
    private Intent intent;
    private String chapterId;
    private String subjectId;
    private int position;
    private List<String> titleList;
    private List<String> bodyList;

    // Util
    private ProcessContent processContent;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        initSetup();
        getLearnData();

        learnDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("user_study_condition")
                        .child(firebaseUser.getUid())
                        .child(subjectId).child(chapterId).setValue(true);
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

    /**
     * Initialize view, firebase, util, intent
     */
    private void initSetup() {
        // View Setup
        learnRecycler = (RecyclerView)findViewById(R.id.learn_recycler);
        learnRecycler.setHasFixedSize(true);
        learnRecycler.setLayoutManager(new LinearLayoutManager(this));
        learnRecyclerAdapter = new LearnRecyclerAdapter(titleList, bodyList);
        learnRecycler.setAdapter(learnRecyclerAdapter);
        learnDoneBtn = (Button)findViewById(R.id.learn_done_btn);
        learnLaterBtn = (Button)findViewById(R.id.learn_later_btn);

        // Firebase Setup
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    Intent intent = new Intent(LearnActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        };

        // Util Setup
        processContent = new ProcessContent();

        // Intent
        intent = getIntent();
        chapterId = intent.getStringExtra("chapterId");
        subjectId = intent.getStringExtra("subjectId");
        position = intent.getIntExtra("position", 0);
    }


    /**
     * Firebase Database로 부터 LearnActivity에서 사용할 데이터 Get
     */
    private void getLearnData() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("learn").child(subjectId).child(chapterId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String content = ds.getValue(String.class);
                    Map<String, String> contentMap = processContent.divideContent(content);
                    searchMap(contentMap);
                    learnRecyclerAdapter.updateData(titleList, bodyList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Map에 저장된 데이터를 List에 구분하여 add
     * @param contentMap
     */
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

    /**
     * Typekit for font
     * @param newBase
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    // AuthStateListener 추가
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    // AuthStateListener 제거
    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
