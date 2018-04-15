package com.junburg.moon.rockbottom.study;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.junburg.moon.rockbottom.R;

import java.util.ArrayList;

/**
 * Created by Junburg on 2018. 3. 11..
 */

public class StudyRecyclerAdapter extends RecyclerView.Adapter<StudyRecyclerViewHolder>{
    private ArrayList<StudyRecyclerData> studyDataList;
    private ArrayList<ChapterRecyclerData> chapterDataList;
    private Context context;

    public StudyRecyclerAdapter(ArrayList<StudyRecyclerData> studyDataList
            , ArrayList<ChapterRecyclerData> chapterDataList, Context context) {
        this.studyDataList = studyDataList;
        this.chapterDataList = chapterDataList;
        this.context = context;
    }

    @Override
    public StudyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.study_recycler_list_item, null);
        StudyRecyclerViewHolder holder = new StudyRecyclerViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(StudyRecyclerViewHolder holder, int position) {
        StudyRecyclerData studyData = studyDataList.get(position);
        holder.studySubjectNameTxt.setText(studyData.getStudySubjectName());
        holder.studyUpdateConditionTxt.setText(studyData.getStudyUpdateCondition());
        holder.chapterRecyclerAdapter.setData(chapterDataList);
        holder.chapterRecyclerAdapter.setOnItemClickListener(new ChapterRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            ChapterClickDialog dialog = new ChapterClickDialog(context);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (studyDataList != null) ? studyDataList.size() : 0;
    }
}
