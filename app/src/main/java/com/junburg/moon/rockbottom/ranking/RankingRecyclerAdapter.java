package com.junburg.moon.rockbottom.ranking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.glide.GlideMethods;
import com.junburg.moon.rockbottom.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Junburg on 2018. 3. 4..
 */

public class RankingRecyclerAdapter extends RecyclerView.Adapter<RankingRecyclerViewHolder> {

    private List<User> userList;
    private Context context;
    private GlideMethods glideMethods;

    public RankingRecyclerAdapter(List<User> userList, Context context, GlideMethods glideMethods) {
        this.userList = userList;
        this.context = context;
        this.glideMethods = glideMethods;
    }

    @Override
    public RankingRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_recycler_list_item, null);
        RankingRecyclerViewHolder holder = new RankingRecyclerViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RankingRecyclerViewHolder holder, int position) {
        Collections.reverse(userList);
        User userData = userList.get(position);
        holder.rankingNickNameTxt.setText(userData.getNickName());
        holder.rankingPointTxt.setText(Integer.toString(userData.getPoints()));
        holder.rankingMessageTxt.setText(userData.getMessage());
        holder.rankingNumberTxt.setText(Integer.toString(position+1));
        holder.rankingTeamTxt.setText(userData.getTeamName());
        holder.rankingGithubTxt.setText(userData.getGithub());
        glideMethods.setRankingImage(userData.getSelfieUri(), holder.rankingSelfieImg);



    }

    @Override
    public int getItemCount() {
        return (userList != null) ? userList.size() : 0;
    }
}
