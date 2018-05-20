package com.junburg.moon.rockbottom.study;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.model.Chapter;
import com.junburg.moon.rockbottom.model.Subject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Junburg on 2018. 3. 11..
 */

public class StudyRecyclerAdapter extends RecyclerView.Adapter<StudyRecyclerViewHolder>{

    // Objects
    private List<Subject> subjectList;
    private android.support.v4.app.FragmentManager fm;

    public StudyRecyclerAdapter(
            List<Subject> subjectList
            , android.support.v4.app.FragmentManager fm) {
        this.subjectList = subjectList;
        this.fm = fm;
    }

    @Override
    public StudyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.study_recycler_list_item, null);
        StudyRecyclerViewHolder holder = new StudyRecyclerViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(StudyRecyclerViewHolder holder, final int subjectPosition) {
        holder.studySubjectNameTxt.setText(subjectList.get(subjectPosition).getName());
        holder.studySubjectExplainTxt.setText(subjectList.get(subjectPosition).getExplain());
        final List<Chapter> chapterList = subjectList.get(subjectPosition).getChapterList();
        holder.chapterRecyclerAdapter.setData(chapterList);
        holder.chapterRecyclerAdapter.setOnItemClickListener(new ChapterRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(int chapterPosition) {
                showDialog(chapterPosition, chapterList.get(chapterPosition).getChapter_id(), subjectList.get(subjectPosition).getSubject_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (subjectList != null) ? subjectList.size() : 0;
    }

    /**
     * position, chapterId, subjectId 값을 다이얼로그 프래그먼트로 넘겨줌
     * @param position
     * @param chapterId
     * @param subjectId
     */
    private void showDialog(int position, String chapterId, String subjectId) {
        ChapterDialogFragment chapterDialogFragment = new ChapterDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("chapterId", chapterId);
        bundle.putString("subjectId", subjectId);
        chapterDialogFragment.setArguments(bundle);
        chapterDialogFragment.setCancelable(false);
        chapterDialogFragment.show(fm, "ChapterDialogFragment");
    }
}
