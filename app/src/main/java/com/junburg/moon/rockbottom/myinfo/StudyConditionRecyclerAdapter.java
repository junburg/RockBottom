package com.junburg.moon.rockbottom.myinfo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.junburg.moon.rockbottom.R;

import java.util.ArrayList;

/**
 * Created by Junburg on 2018. 2. 13..
 */

public class StudyConditionRecyclerAdapter extends RecyclerView.Adapter<StudyConditionRecyclerViewHolder>{

    private ArrayList<StudyConditionRecyclerData> dataList;

    StudyConditionRecyclerAdapter(ArrayList<StudyConditionRecyclerData> dataList) {
        this.dataList = dataList;
    }

    @Override
    public StudyConditionRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.study_condition_recycler_list_item, parent, false);
        StudyConditionRecyclerViewHolder studyConditionRecyclerViewHolder = new StudyConditionRecyclerViewHolder(v);
        return studyConditionRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(StudyConditionRecyclerViewHolder holder, int position) {
        StudyConditionRecyclerData data = dataList.get(position);

        holder.studyConditionSubjectNameTxt.setText(data.getStudyConditionSubjectName());
        String number = data.getStudyConditionDoneNumber() + "/" + data.getStudyConditionSubjectNumber();
        holder.studyConditionDoneNumberTxt.setText(number);
        holder.studyConditionProgressBar.setMax(data.getStudyConditionSubjectNumber());
        holder.studyConditionProgressBar.setProgress(data.getStudyConditionDoneNumber());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}
