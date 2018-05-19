package com.junburg.moon.rockbottom.myinfo;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.firebase.FirebaseMethods;
import com.junburg.moon.rockbottom.glide.GlideMethods;
import com.junburg.moon.rockbottom.login.LoginActivity;
import com.junburg.moon.rockbottom.model.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

/**
 * Created by Junburg on 2018. 2. 7..
 */

public class MyInfoFragment extends Fragment {

    // Views
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
    private String selfie, nickName, message, teamName, github;

    // Objects
    private GlideMethods glideMethods;
    private User user;
    private Context context;

    // Firebases
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ValueEventListener valueEventListener;
    private DatabaseReference databaseReference;
    private FirebaseMethods firebaseMethods;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_info, null);

        initSetting(view);
        viewSetting();
        return view;
    }

    /**
     * Initial setting
     * @param view
     */
    private void initSetting(View view) {

        // Context
        context = getActivity();

        // Firebases
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        };

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("정보를 가져오고 있습니다");
                progressDialog.show();
                setupProfileInfo(firebaseMethods.setProfileInfo(dataSnapshot), progressDialog);
                getUserRanking();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        // Objects
        glideMethods = new GlideMethods(context);
        firebaseMethods = new FirebaseMethods(context);

        // Views
        myInfoSelfieImg = (CircleImageView) view.findViewById(R.id.my_info_selfie_img);
        myInfoPointsNumTxt = (TextView) view.findViewById(R.id.my_info_points_number_txt);
        myInfoRankingNumTxt = (TextView) view.findViewById(R.id.my_info_ranking_number_txt);
        myInfoNickNameTxt = (TextView) view.findViewById(R.id.my_info_nick_name_txt);
        myInfoMessageTxt = (TextView) view.findViewById(R.id.my_info_message_txt);
        myInfoTeamNameTxt = (TextView) view.findViewById(R.id.my_info_team_name_txt);
        myInfoGithubBtn = (TextView) view.findViewById(R.id.my_info_github_btn);
        myInfoEditProfileBtn = (TextView) view.findViewById(R.id.my_info_edit_profile_btn);
        myInfoStudyConditionBtn = (TextView) view.findViewById(R.id.my_info_study_condition_btn);

    }

    private void viewSetting() {
        myInfoGithubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMyGithub();
            }
        });
        myInfoEditProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toEditInfo();
            }
        });
        myInfoStudyConditionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StudyConditionActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * EditInfoActivity로 이동 시에 넘길 데이터 Set
     */
    private void toEditInfo() {

        Intent intent = new Intent(getActivity(), EditInfoActivity.class);
        intent.putExtra("selfieUri", selfie);
        intent.putExtra("nickName", nickName);
        intent.putExtra("message", message);
        intent.putExtra("teamName", teamName);
        intent.putExtra("github", github);
        startActivity(intent);
    }

    /**
     * 사용자 Github으로 이동
     */
    private void toMyGithub() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("http://github.com/" + github);
        intent.setData(uri);
        startActivity(intent);
    }

    /**
     * 사용자 프로필 정보를 변수에 Set
     * @param user
     */
    private void setUserData(User user) {
        nickName = user.getNickName();
        selfie = user.getSelfieUri();
        message = user.getMessage();
        teamName = user.getTeamName();
        github = user.getGithub();
    }

    /**
     * 사용자 프로필 정보를 가져와서 Set
     * @param user
     * @param progressDialog
     */
    private void setupProfileInfo(User user, ProgressDialog progressDialog) {
        this.user = user;
        setUserData(user);
        myInfoNickNameTxt.setText(user.getNickName());
        myInfoMessageTxt.setText(user.getMessage());
        if (user.getMessage().equals("")) {
            myInfoMessageTxt.setText("메세지를 입력해보세요");
        } else {
            myInfoMessageTxt.setText(user.getMessage());
        }
        if (user.getTeamName().equals("")) {
            myInfoTeamNameTxt.setText("소속을 입력해보세요");
        } else {
            myInfoTeamNameTxt.setText(user.getTeamName());
        }
        myInfoPointsNumTxt.setText(Integer.toString(user.getPoints()) + "pts");
        glideMethods.setCircleProfileImageMyInfo(user.getSelfieUri(), myInfoSelfieImg, progressDialog);

    }

    /**
     * 사용자 랭킹 조회를 위한 쿼리
     * 1. Points를 기준으로 정렬
     * 2. 사용자 닉네임과 같은 DataSnapshot이 나올때까지 카운팅
     * 3. 전체 사용자 수에서 카운트 값을 빼고 setText (Firebase가 내림차순을 제공하지 않음, orderByChild -> 오름차순 정렬)
     */
    private void getUserRanking() {
        final Query query = databaseReference.child("users").orderByChild("points");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                int all = (int) dataSnapshot.getChildrenCount();
                Log.d(TAG, "all: " + all);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String nickName = ds.child("nickName").getValue(String.class);
                    if (nickName.equals(user.getNickName())) {
                        int userPlace = i;
                        myInfoRankingNumTxt.setText(Integer.toString(all - userPlace) + "위");
                        break;
                    } else {
                        i++;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    /**
     * AuthStateListener 추가
     * AddValueEventListener 추가
     * ->  EditInfoActivity에서 MyInfoFragment로 돌아왔을때 정보를 다시 갱신해주기 위함
     */
    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
        databaseReference.addValueEventListener(valueEventListener);

    }


    /**
     * AuthStateListener 제거
     * AddValueEventListener 제거
     */
    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
        databaseReference.removeEventListener(valueEventListener);
    }


}

