package com.example.junburg.rockbottom.study;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.junburg.rockbottom.R;

import java.util.ArrayList;
import java.util.zip.InflaterInputStream;

/**
 * Created by Junburg on 2018. 3. 12..
 */

public class ChapterRecyclerAdapter extends RecyclerView.Adapter<ChapterRecyclerViewHolder> {

    private ArrayList<ChapterRecyclerData> dataList;
    private ChapterRecyclerViewHolder.OnItemClickListener listener;

    public void setOnItemClickListener(ChapterRecyclerViewHolder.OnItemClickListener listener) {
        this.listener = listener;
    }

    public ChapterRecyclerAdapter() {
    }

    public void setData(ArrayList<ChapterRecyclerData> dataList) {
        this.dataList = dataList;
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
        ChapterRecyclerData data = dataList.get(position);
        holder.chapterNameTxt.setText(data.getChapterName());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }
}
