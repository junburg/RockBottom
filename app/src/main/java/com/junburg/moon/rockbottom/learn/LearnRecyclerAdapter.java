package com.junburg.moon.rockbottom.learn;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.junburg.moon.rockbottom.R;

import java.util.ArrayList;

/**
 * Created by Junburg on 2018. 3. 4..
 */

public class LearnRecyclerAdapter extends RecyclerView.Adapter<LearnRecyclerViewHolder> {

    private ArrayList<LearnRecyclerData> dataList;

    public LearnRecyclerAdapter(ArrayList<LearnRecyclerData> dataList) {
        this.dataList = dataList;
    }

    @Override
    public LearnRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.learn_recycler_list_item, null);
        LearnRecyclerViewHolder holder = new LearnRecyclerViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(LearnRecyclerViewHolder holder, int position) {
        LearnRecyclerData data = dataList.get(position);
        holder.learnSubjectTxt.setText(data.getLearnSubject());
        holder.learnContentTxt.setText(data.getLearnContent());

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }
}
