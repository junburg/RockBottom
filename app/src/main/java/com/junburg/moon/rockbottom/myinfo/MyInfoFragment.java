package com.junburg.moon.rockbottom.myinfo;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.firebase.FirebaseMethods;
import com.junburg.moon.rockbottom.glide.GlideMethods;
import com.junburg.moon.rockbottom.login.LoginActivity;
import com.junburg.moon.rockbottom.model.UserData;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.content.ContentValues.TAG;

/**
 * Created by Junburg on 2018. 2. 7..
 */

public class MyInfoFragment extends Fragment {

    // Widgets
    private CircleImageView myInfoSelfieImg;
    private TextView myInfoPointsTxt;
    private TextView myInfoRankingTxt;
    private TextView myInfoNickNameTxt;
    private TextView myInfoMessageTxt;
    private TextView myInfoTeamNameTxt;
    private TextView myInfoGithubTxt;
    private Button myInfoEditBtn;
    private RecyclerView myInfoRecycler;
    private ProgressDialog progressDialog;

    // Variables
    private MyInfoRecyclerData myInfoRecyclerData;
    private ArrayList<MyInfoRecyclerData> list = new ArrayList<>();
    private String[] subjectArray = new String[7];
    private int[] subjectNumArray = new int[7];
    private int[] doneNumArray = new int[7];
    private String uid;
    private Context context;
    private String selfie, nickName, message, teamName, github;
    private GlideMethods glideMethods;

    // Firebase
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private FirebaseMethods firebaseMethods;

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
        context = getActivity();
        glideMethods = new GlideMethods(context);
        progressDialog = new ProgressDialog(getActivity());
        firebaseMethods = new FirebaseMethods(context);

        progressDialog.setMessage("정보를 가져오고있습니다");
        progressDialog.show();

        myInfoSelfieImg = (CircleImageView) view.findViewById(R.id.my_info_selfie_img);
        myInfoPointsTxt = (TextView) view.findViewById(R.id.my_info_points_txt);
        myInfoRankingTxt = (TextView) view.findViewById(R.id.my_info_ranking_txt);
        myInfoNickNameTxt = (TextView) view.findViewById(R.id.my_info_nick_name_txt);
        myInfoMessageTxt = (TextView) view.findViewById(R.id.my_info_message_txt);
        myInfoTeamNameTxt = (TextView) view.findViewById(R.id.my_info_team_name_txt);

        myInfoGithubTxt = (TextView) view.findViewById(R.id.my_info_github_txt);
        myInfoGithubTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("http://github.com/" + myInfoGithubTxt.getText().toString());
                intent.setData(uri);
                startActivity(intent);
            }
        });

        myInfoEditBtn = (Button) view.findViewById(R.id.my_info_edit_btn);
        myInfoEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                intent.putExtra("selfieUri", selfie);
                intent.putExtra("nickName", nickName);
                intent.putExtra("message", message);
                intent.putExtra("teamName", teamName);
                intent.putExtra("github", github);
                startActivity(intent);
            }
        });

        myInfoRecycler = (RecyclerView) view.findViewById(R.id.my_info_recycler);
        myInfoRecycler.setHasFixedSize(true);
        myInfoRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        myInfoRecycler.setAdapter(new MyInfoRecyclerAdapter(list));

        setupFirebaseAuth();
        return view;
    }

    private void putIntentData(UserData userData) {
        selfie = userData.getSelfieUri();
        nickName = userData.getNickName();
        message = userData.getMessage();
        teamName = userData.getTeamName();
        github = userData.getGithub();
    }

    private void setupProfileInfo(UserData userData) {
        Log.d(TAG, "setupProfileInfo: " + userData.getSelfieUri());
        putIntentData(userData);
        glideMethods.setCircleProfileImage(userData.getSelfieUri(), myInfoSelfieImg);
        myInfoNickNameTxt.setText(userData.getNickName());
        myInfoMessageTxt.setText(userData.getMessage());
        myInfoTeamNameTxt.setText(userData.getTeamName());
        myInfoGithubTxt.setText(Html.fromHtml("<u>" + userData.getGithub() + "</u>"));
        myInfoPointsTxt.setText(Integer.toString(userData.getPoints()));
        myInfoRankingTxt.setText(Integer.toString(userData.getRanking()));
    }

    private void setupFirebaseAuth() {
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("정보를 가져오는 중입니다");
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                } else {
                    Intent intent = new Intent (getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        };

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setupProfileInfo(firebaseMethods.setProfileInfo(dataSnapshot));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);


    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }


}

