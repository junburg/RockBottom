package com.junburg.moon.rockbottom.myinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.junburg.moon.rockbottom.R;

import java.util.ArrayList;

/**
 * Created by Junburg on 2018. 4. 20..
 */

public class StudyConditionActivity extends AppCompatActivity {


    private StudyConditionRecyclerData studyConditionRecyclerData;
    private ArrayList<StudyConditionRecyclerData> list = new ArrayList<>();
    private String[] subjectArray = new String[7];
    private int[] subjectNumArray = new int[7];
    private int[] doneNumArray = new int[7];
    private RecyclerView studyConditionRecycler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_condition);
        setData();

        studyConditionRecycler = (RecyclerView) findViewById(R.id.study_condition_recycler);
        studyConditionRecycler.setHasFixedSize(true);
        studyConditionRecycler.setLayoutManager(new LinearLayoutManager(this));
        studyConditionRecycler.setAdapter(new StudyConditionRecyclerAdapter(list));
    }

    private void setData() {
        subjectArray[0] = "자료구조";
        subjectArray[1] = "알고리즘";
        subjectArray[2] = "소프트웨어 공학";
        subjectArray[3] = "네트워크";
        subjectArray[4] = "데이터 베이스";
        subjectArray[5] = "컴퓨터 구조";
        subjectArray[6] = "이산수학";

        subjectNumArray[0] = 20;
        subjectNumArray[1] = 30;
        subjectNumArray[2] = 23;
        subjectNumArray[3] = 14;
        subjectNumArray[4] = 12;
        subjectNumArray[5] = 90;
        subjectNumArray[6] = 45;


        doneNumArray[0] = 1;
        doneNumArray[1] = 3;
        doneNumArray[2] = 0;
        doneNumArray[3] = 12;
        doneNumArray[4] = 9;
        doneNumArray[5] = 23;
        doneNumArray[6] = 33;

        for (int i = 0; i < 7; i++) {
            studyConditionRecyclerData = new StudyConditionRecyclerData();
            studyConditionRecyclerData.setStudyConditionSubjectName(subjectArray[i]);
            studyConditionRecyclerData.setStudyConditionSubjectNumber(subjectNumArray[i]);
            studyConditionRecyclerData.setStudyConditionDoneNumber(doneNumArray[i]);
            list.add(studyConditionRecyclerData);

        }
    }
}
