package com.junburg.moon.rockbottom.myinfo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.junburg.moon.rockbottom.R;
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

        getUserStudyCondition();

        studyConditionRecycler = (RecyclerView) findViewById(R.id.study_condition_recycler);
        studyConditionRecycler.setHasFixedSize(true);
        studyConditionRecycler.setLayoutManager(new LinearLayoutManager(this));
        studyConditionRecyclerAdapter = new StudyConditionRecyclerAdapter(subjectNameList, countChapterList);
        studyConditionRecycler.setAdapter(studyConditionRecyclerAdapter);
    }

    private void getUserStudyCondition() {
        subjectNameList = new ArrayList<>();
        countChapterList = new ArrayList<>();
        trueChapterList = new ArrayList<>();

        databaseReference.child("user_study_condition").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    subjectNameList.add(ds.getKey());
                    countChapterList.add((int)ds.getChildrenCount());

                }
            studyConditionRecyclerAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
