package com.junburg.moon.rockbottom.myinfo;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.login.UserData;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

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

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();

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

        for (int i = 0; i < 7; i++) {
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
        myInfoSelfieImg = (ImageView) view.findViewById(R.id.my_info_selfie_img);
        myInfoPointsTxt = (TextView) view.findViewById(R.id.my_info_points_txt);
        myInfoRankingTxt = (TextView) view.findViewById(R.id.my_info_ranking_txt);
        myInfoNickNameTxt = (TextView) view.findViewById(R.id.my_info_nick_name_txt);
        myInfoMessageTxt = (TextView) view.findViewById(R.id.my_info_message_txt);
        myInfoTeamNameTxt = (TextView) view.findViewById(R.id.my_info_team_name_txt);
        myInfoGithubTxt = (TextView) view.findViewById(R.id.my_info_github_txt);

        myInfoEditBtn = (Button) view.findViewById(R.id.my_info_edit_btn);
        myInfoEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                startActivity(intent);
            }
        });

        myInfoRecycler = (RecyclerView) view.findViewById(R.id.my_info_recycler);
        myInfoRecycler.setHasFixedSize(true);
        myInfoRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        myInfoRecycler.setAdapter(new MyInfoRecyclerAdapter(list));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(listener);
    }

    @Override
    public void onStop() {
        databaseReference.removeEventListener(listener);
        super.onStop();
    }

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            getData(dataSnapshot);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void getData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            UserData userData = new UserData();
            userData.setSelfieUri(ds.child(userId).getValue(UserData.class).getSelfieUri());
            userData.setNickName(ds.child(userId).getValue(UserData.class).getNickName());
            userData.setMessage(ds.child(userId).getValue(UserData.class).getMessage());
            userData.setTeamName(ds.child(userId).getValue(UserData.class).getTeamName());
            userData.setGithub(ds.child(userId).getValue(UserData.class).getGithub());
            userData.setPoints(ds.child(userId).getValue(UserData.class).getPoints());
            userData.setRanking(ds.child(userId).getValue(UserData.class).getRanking());

            if (userData.getSelfieUri() != null) {
                Glide.with(this)
                        .load(userData.getSelfieUri())
                        .bitmapTransform(new CropCircleTransformation(getActivity()))
                        .crossFade()
                        .into(myInfoSelfieImg);
            }

            myInfoNickNameTxt.setText(userData.getNickName());
            myInfoMessageTxt.setText(userData.getMessage());
            myInfoTeamNameTxt.setText(userData.getTeamName());
            myInfoGithubTxt.setText(userData.getGithub());
            myInfoRankingTxt.setText(Integer.toString(userData.getRanking()));
            myInfoPointsTxt.setText(Integer.toString(userData.getPoints()));

        }
    }
}

