package com.junburg.moon.rockbottom.learn;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.junburg.moon.rockbottom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junburg on 2018. 3. 4..
 */

public class LearnRecyclerAdapter extends RecyclerView.Adapter<LearnRecyclerViewHolder> {

    private List<String> titleList;
    private List<String> bodyList;

    public LearnRecyclerAdapter(List<String> titleList, List<String> bodyList) {
        this.titleList = titleList;
        this.bodyList = bodyList;
    }

    @Override
    public LearnRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.learn_recycler_list_item, null);
        LearnRecyclerViewHolder holder = new LearnRecyclerViewHolder(v);
        return holder;
    }

    /**
     * titleList -> 제목
     * bodyList -> 본문
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(LearnRecyclerViewHolder holder, int position) {
        holder.learnSubjectTxt.setText(titleList.get(position));
        holder.learnContentTxt.setText(bodyList.get(position));

    }

    @Override
    public int getItemCount() {
        return (null != titleList ? titleList.size() : 0);
    }

    /**
     * 제목, 본문 List Update
     * @param titleList
     * @param bodyList
     */
    public void updateData(List<String> titleList, List<String> bodyList) {
        this.titleList = titleList;
        this.bodyList = bodyList;
        notifyDataSetChanged();
    }
}
