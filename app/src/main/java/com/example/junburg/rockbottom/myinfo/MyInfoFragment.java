package com.example.junburg.rockbottom.myinfo;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.junburg.rockbottom.R;

import java.util.ArrayList;

/**
 * Created by Junburg on 2018. 2. 7..
 */

public class MyInfoFragment extends Fragment {

    private ImageView myInfoSelfieImg;
    private TextView myInfoPointsTxt;
    private TextView myInfoRankingTxt;
    private TextView myInfoNickNameTxt;
    private TextView myInfoMessageTxt;
    private TextView myInfoTeamNameTxt;
    private TextView myInfoGithubTxt;
    private Button myInfoEditBtn;
    private RecyclerView myInfoRecycler;

    private MyInfoRecyclerData myInfoRecyclerData;
    private ArrayList<MyInfoRecyclerData> list = new ArrayList<>();
    private String[] subjectArray = new String[7];
    private int[] subjectNumArray = new int[7];
    private int[] doneNumArray = new int[7];

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        subjectArray[0] = "자료구조";
        subjectArray[1] = "알고리즘";
        subjectArray[2] = "소프트웨어 공학";
        subjectArray[3] = "네트워크";
        subjectArray[4] = "데이터 베이스";
        subjectArray[5] = "컴퓨터 구조";
        subjectArray[6] = "이산수학";

        subjectNumArray[0] = 20;
        subjectNumArray[1] = 30;
        subjectNumArray[2] = 23;
        subjectNumArray[3] = 14;
        subjectNumArray[4] = 12;
        subjectNumArray[5] = 90;
        subjectNumArray[6] = 45;


        doneNumArray[0] = 1;
        doneNumArray[1] = 3;
        doneNumArray[2] = 0;
        doneNumArray[3] = 12;
        doneNumArray[4] = 9;
        doneNumArray[5] = 23;
        doneNumArray[6] = 33;

        for(int i=0; i<7; i++) {
            myInfoRecyclerData = new MyInfoRecyclerData();
         myInfoRecyclerData.setMyInfoSubjectName(subjectArray[i]);
         myInfoRecyclerData.setMyInfoSubjectNumber(subjectNumArray[i]);
         myInfoRecyclerData.setMyInfoDoneNumber(doneNumArray[i]);
         list.add(myInfoRecyclerData);

        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_info, null);
        myInfoSelfieImg = (ImageView)view.findViewById(R.id.my_info_selfie_img);
        myInfoPointsTxt = (TextView)view.findViewById(R.id.my_info_points_txt);
        myInfoRankingTxt = (TextView)view.findViewById(R.id.my_info_ranking_txt);
        myInfoNickNameTxt = (TextView)view.findViewById(R.id.my_info_nick_name_txt);
        myInfoMessageTxt = (TextView)view.findViewById(R.id.my_info_message_txt);
        myInfoTeamNameTxt = (TextView)view.findViewById(R.id.my_info_team_name_txt);
        myInfoGithubTxt = (TextView)view.findViewById(R.id.my_info_github_txt);

        myInfoEditBtn = (Button)view.findViewById(R.id.my_info_edit_btn);
        myInfoEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                startActivity(intent);
            }
        });

        myInfoRecycler = (RecyclerView)view.findViewById(R.id.my_info_recycler);
        myInfoRecycler.setHasFixedSize(true);
        myInfoRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        myInfoRecycler.setAdapter(new MyInfoRecyclerAdapter(list));

        return view;
    }
}

