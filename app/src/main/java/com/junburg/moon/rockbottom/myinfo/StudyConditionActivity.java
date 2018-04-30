package com.junburg.moon.rockbottom.myinfo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.model.Chapter;
import com.junburg.moon.rockbottom.study.StudyRecyclerAdapter;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junburg on 2018. 4. 20..
 */

public class StudyConditionActivity extends AppCompatActivity {

    private static final String TAG = "StudyConditionActivity";

    private RecyclerView studyConditionRecycler;
    private Toolbar studyConditionToolbar;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private List<String> subjectNameList;
    private List<Integer> countChapterList;
    private List<Integer> trueChapterList;
    private StudyConditionRecyclerAdapter studyConditionRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_condition);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        getUserStudyCondition(firebaseUser.getUid());

        studyConditionToolbar = (Toolbar)findViewById(R.id.study_condition_toolbar);
        setSupportActionBar(studyConditionToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        studyConditionRecycler = (RecyclerView) findViewById(R.id.study_condition_recycler);
        studyConditionRecycler.setHasFixedSize(true);
        studyConditionRecycler.setLayoutManager(new LinearLayoutManager(this));
        studyConditionRecyclerAdapter = new StudyConditionRecyclerAdapter(subjectNameList, countChapterList, trueChapterList);
        studyConditionRecycler.setAdapter(studyConditionRecyclerAdapter);
    }

    private void getUserStudyCondition(final String uid) {
        subjectNameList = new ArrayList<>();
        countChapterList = new ArrayList<>();
        trueChapterList = new ArrayList<>();

        databaseReference.child("subject").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    int count = 0;
                    for (DataSnapshot ds2 : ds.child("chapter").getChildren()) {
                        count++;
                    }
                    countChapterList.add(count);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("user_study_condition").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue().toString();
                    subjectNameList.add(name);
                    trueChapterList.add((int) ds.getChildrenCount());
                    Log.d(TAG, "query1: " + name);
                }

                studyConditionRecyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}