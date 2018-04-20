package com.junburg.moon.rockbottom.study;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.junburg.moon.rockbottom.R;

/**
 * Created by Junburg on 2018. 3. 11..
 */

public class StudyRecyclerViewHolder extends RecyclerView.ViewHolder{

    protected TextView studySubjectNameTxt;
    protected TextView studySubjectExplainTxt;
    protected RecyclerView chapterRecycler;
    protected ChapterRecyclerAdapter chapterRecyclerAdapter;

    public StudyRecyclerViewHolder(View itemView) {
        super(itemView);
        studySubjectNameTxt = (TextView)itemView.findViewById(R.id.study_subject_name_txt);
        studySubjectExplainTxt = (TextView)itemView.findViewById(R.id.study_subject_explain_txt);
        chapterRecycler = (RecyclerView)itemView.findViewById(R.id.chapter_recycler);
        chapterRecycler.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        chapterRecyclerAdapter = new ChapterRecyclerAdapter();
        chapterRecycler.setAdapter(chapterRecyclerAdapter);
    }
}
