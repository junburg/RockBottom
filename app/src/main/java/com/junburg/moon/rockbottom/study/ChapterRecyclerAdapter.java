package com.junburg.moon.rockbottom.study;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.model.Chapter;
import com.junburg.moon.rockbottom.myinfo.EditInfoDialogFragment;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Junburg on 2018. 3. 12..
 */

public class ChapterRecyclerAdapter extends RecyclerView.Adapter<ChapterRecyclerViewHolder> {

    private List<Chapter> chapterList;
    private ChapterRecyclerViewHolder.OnItemClickListener listener;

    public void setOnItemClickListener(ChapterRecyclerViewHolder.OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public ChapterRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_recycler_list_item, null);
        ChapterRecyclerViewHolder holder = new ChapterRecyclerViewHolder(v, listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ChapterRecyclerViewHolder holder, int position) {

        holder.chapterNameTxt.setText(chapterList.get(position).getName());
        holder.chapterExplainTxt.setText(chapterList.get(position).getExplain());

    }

    @Override
    public int getItemCount() {
        return (chapterList != null) ? chapterList.size() : 0;
    }

    public void setData(List<Chapter> chapterList) {
        this.chapterList = chapterList;
        Log.d(TAG, "setData: " + this.chapterList.toString());
        notifyDataSetChanged();
    }


}
