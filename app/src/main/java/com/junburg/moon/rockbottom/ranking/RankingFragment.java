package com.junburg.moon.rockbottom.ranking;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.junburg.moon.rockbottom.model.User;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Junburg on 2018. 2. 7..
 */

public class RankingFragment extends Fragment {

    private RecyclerView rankingRecycler;
    private RankingRecyclerAdapter rankingRecyclerAdapter;
    private List<User> userList;
    private Context context;
    private GlideMethods glideMethods;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


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
        getRankingData();
        rankingRecycler = (RecyclerView)view.findViewById(R.id.ranking_recycler);
        rankingRecycler.setHasFixedSize(true);
        rankingRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        rankingRecyclerAdapter = new RankingRecyclerAdapter(userList, context, glideMethods);
        rankingRecycler.setAdapter(rankingRecyclerAdapter);

        return view;
    }


    private void getRankingData() {
        Query query = databaseReference.child("users").orderByChild("points").limitToLast(5);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    userList.add(ds.getValue(User.class));

                }
                rankingRecyclerAdapter.notifyDataSetChanged();
                Log.d(TAG, "onDataChange: " + userList.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

