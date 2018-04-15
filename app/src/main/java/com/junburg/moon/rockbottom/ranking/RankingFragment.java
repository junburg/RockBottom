package com.junburg.moon.rockbottom.ranking;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.junburg.moon.rockbottom.R;

import java.util.ArrayList;

/**
 * Created by Junburg on 2018. 2. 7..
 */

public class RankingFragment extends Fragment {

    private RecyclerView rankingRecycler;
    private ArrayList<RankingRecyclerData> dataList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, null);
        rankingRecycler = (RecyclerView)view.findViewById(R.id.ranking_recycler);
        createDummyData();
        rankingRecycler.setHasFixedSize(true);
        rankingRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        rankingRecycler.setAdapter(new RankingRecyclerAdapter(dataList));

        return view;
    }

    private void createDummyData() {

        for(int i = 0 ; i < 10 ; i++) {
            RankingRecyclerData data = new RankingRecyclerData();
            data.setRankingNickName("user" + i);
            data.setRankingPoint(1000 + i + "");
            data.setRankingMessage("I'm No." + i); ;
            data.setRankingTeam("university" + i);
            data.setRankingNumber("Ranking" + i);
            data.setRankingGithub("Github.com/" + i + "address");
            dataList.add(data);
        }

    }
}
