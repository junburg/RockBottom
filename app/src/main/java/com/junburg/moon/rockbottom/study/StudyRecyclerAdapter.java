package com.junburg.moon.rockbottom.study;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.model.Chapter;
import com.junburg.moon.rockbottom.model.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junburg on 2018. 3. 11..
 */

public class StudyRecyclerAdapter extends RecyclerView.Adapter<StudyRecyclerViewHolder>{
    private List<Subject> subjectList;
    private Context context;

    public StudyRecyclerAdapter(
            List<Subject> subjectList
            , Context context) {
        this.subjectList = subjectList;
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
        holder.studySubjectNameTxt.setText(subjectList.get(position).getSubjectName());
        holder.studySubjectExplainTxt.setText(subjectList.get(position).getSubjectExplain());
        List<Chapter> chapterList = new ArrayList<>();
        chapterList.add(subjectList.get(position).getChapter());
        holder.chapterRecyclerAdapter.setData(chapterList);
        holder.chapterRecyclerAdapter.setOnItemClickListener(new ChapterRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            ChapterClickDialog dialog = new ChapterClickDialog(context);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (subjectList != null) ? subjectList.size() : 0;
    }
}
