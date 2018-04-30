package com.junburg.moon.rockbottom.myinfo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.junburg.moon.rockbottom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junburg on 2018. 2. 13..
 */

public class StudyConditionRecyclerAdapter extends RecyclerView.Adapter<StudyConditionRecyclerViewHolder> {

    private List<String> subjectNameList;
    private List<Integer> chapterCountList;
    private List<Integer> trueChapterList;


    public StudyConditionRecyclerAdapter(List<String> subjectNameList
            , List<Integer> chapterCountList
            , List<Integer> trueChapterList) {
        this.subjectNameList = subjectNameList;
        this.chapterCountList = chapterCountList;
        this.trueChapterList = trueChapterList;
    }

    @Override
    public StudyConditionRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.study_condition_recycler_list_item, parent, false);
        StudyConditionRecyclerViewHolder studyConditionRecyclerViewHolder = new StudyConditionRecyclerViewHolder(v);
        return studyConditionRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(StudyConditionRecyclerViewHolder holder, int position) {
        holder.studyConditionSubjectNameTxt.setText(subjectNameList.get(position));
        holder.studyConditionDoneNumberTxt.setText(trueChapterList.get(position) - 1 + "/" + chapterCountList.get(position));
        holder.studyConditionProgressBar.setMax(chapterCountList.get(position));
        holder.studyConditionProgressBar.setProgress(trueChapterList.get(position) - 1);
    }

    @Override
    public int getItemCount() {
        return (subjectNameList != null) ? subjectNameList.size() : 0;
    }

}
