package com.junburg.moon.rockbottom.study;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.model.Chapter;

import java.util.List;

/**
 * Created by Junburg on 2018. 3. 12..
 */

public class ChapterRecyclerAdapter extends RecyclerView.Adapter<ChapterRecyclerViewHolder> {

    private List<Chapter> chapterList;
    private ChapterRecyclerViewHolder.OnItemClickListener listener;

    public void setOnItemClickListener(ChapterRecyclerViewHolder.OnItemClickListener listener) {
        this.listener = listener;
    }

    public ChapterRecyclerAdapter() {

    }

    public void setData(List<Chapter> chapterList) {
        this.chapterList = chapterList;
        notifyDataSetChanged();
    }

    @Override
    public ChapterRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_recycler_list_item, null);
        ChapterRecyclerViewHolder holder = new ChapterRecyclerViewHolder(v, listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ChapterRecyclerViewHolder holder, int position) {

        holder.chapterNameTxt.setText(chapterList.get(position).getChaterName());
        holder.chapterExplainTxt.setText(chapterList.get(position).getChaterExplain());
    }

    @Override
    public int getItemCount() {
        return (chapterList != null) ? chapterList.size() : 0;
    }
}
