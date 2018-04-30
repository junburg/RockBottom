package com.junburg.moon.rockbottom.myinfo;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import com.junburg.moon.rockbottom.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

/**
 * Created by Junburg on 2018. 2. 7..
 */

public class MyInfoFragment extends Fragment {

    // Widgets
    private CircleImageView myInfoSelfieImg;
    private TextView myInfoPointsNumTxt;
    private TextView myInfoRankingNumTxt;
    private TextView myInfoNickNameTxt;
    private TextView myInfoMessageTxt;
    private TextView myInfoTeamNameTxt;
    private TextView myInfoGithubBtn;
    private TextView myInfoEditProfileBtn;
    private TextView myInfoStudyConditionBtn;
    private ProgressDialog progressDialog;

    // Variables
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
    private FirebaseUser firebaseUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_info, null);
        context = getActivity();
        glideMethods = new GlideMethods(context);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseMethods = new FirebaseMethods(context);

        myInfoSelfieImg = (CircleImageView) view.findViewById(R.id.my_info_selfie_img);
        myInfoPointsNumTxt = (TextView) view.findViewById(R.id.my_info_points_number_txt);
        myInfoRankingNumTxt = (TextView) view.findViewById(R.id.my_info_ranking_number_txt);
        myInfoNickNameTxt = (TextView) view.findViewById(R.id.my_info_nick_name_txt);
        myInfoMessageTxt = (TextView) view.findViewById(R.id.my_info_message_txt);
        myInfoTeamNameTxt = (TextView) view.findViewById(R.id.my_info_team_name_txt);

        setupFirebaseAuth();

        myInfoGithubBtn = (TextView) view.findViewById(R.id.my_info_github_btn);
        myInfoGithubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMyGithub();
            }
        });

        myInfoEditProfileBtn = (TextView) view.findViewById(R.id.my_info_edit_profile_btn);
        myInfoEditProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toEditInfo();
            }
        });

        myInfoStudyConditionBtn = (TextView) view.findViewById(R.id.my_info_study_condition_btn);
        myInfoStudyConditionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StudyConditionActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void toEditInfo() {

        Intent intent = new Intent(getActivity(), EditInfoActivity.class);
        intent.putExtra("selfieUri", selfie);
        intent.putExtra("nickName", nickName);
        intent.putExtra("message", message);
        intent.putExtra("teamName", teamName);
        intent.putExtra("github", github);
        startActivity(intent);
    }

    private void toMyGithub() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("http://github.com/" + github);
        intent.setData(uri);
        startActivity(intent);
    }

    private void putIntentData(User user) {
        nickName = user.getNickName();
        selfie = user.getSelfieUri();
        message = user.getMessage();
        teamName = user.getTeamName();
        github = user.getGithub();
    }

    private void setupProfileInfo(User user, ProgressDialog progressDialog) {
        Log.d(TAG, "setupProfileInfo: " + user.getSelfieUri());
        putIntentData(user);
        glideMethods.setCircleProfileImage(user.getSelfieUri(), myInfoSelfieImg);
        myInfoNickNameTxt.setText(user.getNickName());
        myInfoMessageTxt.setText(user.getMessage());
        myInfoTeamNameTxt.setText(user.getTeamName());
        myInfoPointsNumTxt.setText(Integer.toString(user.getPoints()));
        myInfoRankingNumTxt.setText(Integer.toString(user.getRanking()));
        progressDialog.dismiss();
    }

    private void setupFirebaseAuth() {
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        };

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("정보를 가져오고 있습니다");
        progressDialog.setCancelable(false);
        progressDialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setupProfileInfo(firebaseMethods.setProfileInfo(dataSnapshot), progressDialog);

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

