package com.example.junburg.rockbottom.ranking;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.junburg.rockbottom.R;

import java.util.ArrayList;

/**
 * Created by Junburg on 2018. 3. 4..
 */

public class RankingRecyclerAdapter extends RecyclerView.Adapter<RankingRecyclerViewHolder> {

    private ArrayList<RankingRecyclerData> dataList;

    public RankingRecyclerAdapter(ArrayList<RankingRecyclerData> dataList) {
        this.dataList = dataList;
    }

    @Override
    public RankingRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_recycler_list_item, null);
        RankingRecyclerViewHolder holder = new RankingRecyclerViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RankingRecyclerViewHolder holder, int position) {
        RankingRecyclerData data = dataList.get(position);
        holder.rankingNickNameTxt.setText(data.getRankingNickName());
        holder.rankingPointTxt.setText(data.getRankingPoint());
        holder.rankingMessageTxt.setText(data.getRankingMessage());
        holder.rankingNumberTxt.setText(data.getRankingNumber());
        holder.rankingTeamTxt.setText(data.getRankingTeam());
        holder.rankingGithubTxt.setText(data.getRankingGithub());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }
}
