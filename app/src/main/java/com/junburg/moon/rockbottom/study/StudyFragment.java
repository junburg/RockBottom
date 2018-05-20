package com.junburg.moon.rockbottom.study;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.firebase.FirebaseMethods;
import com.junburg.moon.rockbottom.login.LoginActivity;
import com.junburg.moon.rockbottom.model.Chapter;
import com.junburg.moon.rockbottom.model.Subject;
import com.junburg.moon.rockbottom.util.MutedVideoView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Junburg on 2018. 2. 7..
 */

public class StudyFragment extends Fragment {

    // Views
    private MutedVideoView studyVideo;
    private RecyclerView studyRecycler;
    private StudyRecyclerAdapter studyRecyclerAdapter;
    private ProgressDialog progressDialog;

    // Firebases
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseMethods firebaseMethods;
    private FirebaseAuth.AuthStateListener authStateListener;

    // Objects
    private List<Subject> subjectList;
    private Context context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study, null);
        subjectList = new ArrayList<>();

        initSetting(view);
        viewSetting();
        getStudyData();

        return view;
    }

    /**
     * Initial setting
     */
    private void initSetting(View view) {
        // Context
        context = getActivity();

        // Firebases
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {

                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        };
        firebaseMethods = new FirebaseMethods(context);
        firebaseMethods.checkUserConditionSetting(firebaseUser.getUid());

        // Views
        studyVideo = (MutedVideoView) view.findViewById(R.id.study_video);
        studyRecycler = (RecyclerView) view.findViewById(R.id.study_recycler_view);


    }

    /**
     * Set view
     */
    private void viewSetting() {
        String videoUriPath = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.study_video;
        Uri uri = Uri.parse(videoUriPath);
        studyVideo.setVideoURI(uri);
        studyVideo.start();
        studyVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                studyVideo.start();
            }
        });
        studyRecycler.setHasFixedSize(true);
        studyRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        studyRecycler.setNestedScrollingEnabled(false);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        studyRecyclerAdapter = new StudyRecyclerAdapter(subjectList, getContext(), fm);
        studyRecycler.setAdapter(studyRecyclerAdapter);

    }


    /**
     * 과목과 과목별 챕터의 정보를 가져와 리사이클러 뷰를 notify
     */
    private void getStudyData() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("정보를 가져오고 있습니다");
        progressDialog.setCancelable(false);
        progressDialog.show();
        databaseReference.child("subject").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                subjectList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Subject subject = ds.getValue(Subject.class);

                    ArrayList<Chapter> chapterList = new ArrayList<>();
                    for(DataSnapshot ds2 : ds.child("chapter").getChildren()) {
                        Chapter chapter = ds2.getValue(Chapter.class);
                        chapterList.add(chapter);
                    }
                    subject.setChapterList(chapterList);
                    subjectList.add(subject);
                    studyRecyclerAdapter.notifyDataSetChanged();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }


    /**
     * AuthStateListener 추가
     */
    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }


    /**
     * AuthStateListener 제거
     */
    @Override
    public void onResume() {
        super.onResume();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
