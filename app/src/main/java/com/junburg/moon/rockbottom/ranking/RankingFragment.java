package com.junburg.moon.rockbottom.ranking;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.junburg.moon.rockbottom.glide.GlideMethods;
import com.junburg.moon.rockbottom.login.LoginActivity;
import com.junburg.moon.rockbottom.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

/**
 * Created by Junburg on 2018. 2. 7..
 */

public class RankingFragment extends Fragment {

    // Variables
    private RecyclerView rankingRecycler;
    private RankingRecyclerAdapter rankingRecyclerAdapter;
    private List<User> userList;
    private Context context;
    private GlideMethods glideMethods;

    // Firebases
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener authStateListener;

    // Widgets
    private TextView rankingTeamNameTxt, rankingNickNameTxt, rankingMessageTxt, rankingNumberTxt
            , rankingPointsTxt, rankingGitHubTxt;
    private CircleImageView rankingSelfieImg;
    private CollapsingToolbarLayout rankingCollapsingToolbarLayout;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, null);
        userList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        context = getActivity();
        glideMethods = new GlideMethods(context);
        rankingCollapsingToolbarLayout = (CollapsingToolbarLayout)view.findViewById(R.id.ranking_collapsing_tool_bar_layout);
        rankingTeamNameTxt = (TextView) view.findViewById(R.id.ranking_team_name_txt);
        rankingNickNameTxt = (TextView) view.findViewById(R.id.ranking_nick_name_txt);
        rankingMessageTxt = (TextView) view.findViewById(R.id.ranking_message_txt);
        rankingSelfieImg = (CircleImageView) view.findViewById(R.id.ranking_selfie_img);
        rankingGitHubTxt = (TextView) view.findViewById(R.id.ranking_github_txt);

        rankingGitHubTxt.setText(Html.fromHtml("<u>" + getResources().getString(R.string.ranking_github_txt) + "</u>"));
        rankingNumberTxt = (TextView) view.findViewById(R.id.ranking_number_txt);
        rankingPointsTxt = (TextView) view.findViewById(R.id.ranking_points_txt);

        getRankingData();

        rankingRecycler = (RecyclerView) view.findViewById(R.id.ranking_recycler);
        rankingRecycler.setHasFixedSize(true);
        rankingRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        rankingRecyclerAdapter = new RankingRecyclerAdapter(userList, context, glideMethods);
        rankingRecycler.setAdapter(rankingRecyclerAdapter);
        rankingRecyclerAdapter.setOnItemClickListener(new RankingRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                setRankingUser(position);
            }
        });

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

        return view;
    }

    private void getRankingData() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("정보를 가져오고 있습니다");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.d(TAG, "getRankingData: " + "come");
        Query query = databaseReference.child("users").orderByChild("points").limitToLast(10);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    userList.add(ds.getValue(User.class));
                }
                Collections.reverse(userList);
                initFirstUser();
                rankingRecyclerAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

    }

    private void toYourGithub(String github) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("http://github.com/" + github);
        intent.setData(uri);
        startActivity(intent);
    }

    private void initFirstUser() {
        rankingTeamNameTxt.setText(userList.get(0).getTeamName());
        rankingNickNameTxt.setText(userList.get(0).getNickName());
        rankingMessageTxt.setText("\"" + userList.get(0).getMessage() + "\"");
        glideMethods.setCircleProfileImage(userList.get(0).getSelfieUri(), rankingSelfieImg);
        rankingGitHubTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toYourGithub(userList.get(0).getGithub());
            }
        });
        rankingPointsTxt.setText(Integer.toString(userList.get(0).getPoints()) + "pts");
        rankingNumberTxt.setText("1st");
    }

    private void setRankingUser(final int position) {
        rankingTeamNameTxt.setText(userList.get(position).getTeamName());
        rankingNickNameTxt.setText(userList.get(position).getNickName());
        rankingMessageTxt.setText("\"" + userList.get(position).getMessage() + "\"");
        glideMethods.setCircleProfileImage(userList.get(position).getSelfieUri(), rankingSelfieImg);
        rankingGitHubTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toYourGithub(userList.get(position).getGithub());
            }
        });

        rankingPointsTxt.setText(Integer.toString(userList.get(position).getPoints()) + "pts");
        String numAppend = "";
        switch (position) {
            case 0:
                numAppend = "st";
                break;
            case 1:
                numAppend = "nd";
                break;
            case 2:
                numAppend = "rd";
                break;
            default:
                numAppend = "th";
        }
        rankingNumberTxt.setText(position + 1 + numAppend);
        rankingCollapsingToolbarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}

