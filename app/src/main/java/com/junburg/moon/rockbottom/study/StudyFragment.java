package com.junburg.moon.rockbottom.study;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.junburg.moon.rockbottom.model.Chapter;
import com.junburg.moon.rockbottom.model.Subject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Junburg on 2018. 2. 7..
 */

public class StudyFragment extends Fragment {

    private VideoView studyVideo;
    private RecyclerView studyRecycler;
    private StudyRecyclerAdapter studyRecyclerAdapter;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private List<Subject> subjectList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study, null);
        subjectList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        getStudyData();
        studyVideo = (VideoView) view.findViewById(R.id.study_video);
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
        studyRecycler = (RecyclerView) view.findViewById(R.id.study_recycler_view);
        studyRecycler.setHasFixedSize(true);
        studyRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        studyRecycler.setNestedScrollingEnabled(false);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        studyRecyclerAdapter = new StudyRecyclerAdapter(subjectList, getContext(), fm);
        studyRecycler.setAdapter(studyRecyclerAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        studyVideo.start();
    }

    private void getStudyData() {
        databaseReference.child("subject").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                subjectList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Subject subject = ds.getValue(Subject.class);

                    Log.d(TAG, "onDataChange: " + subject.getName()
                            + subject.getExplain()
                            );

                    ArrayList<Chapter> chapterList = new ArrayList<>();
                    for(DataSnapshot ds2 : ds.child("chapter").getChildren()) {
                        Chapter chapter = ds2.getValue(Chapter.class);
                        chapterList.add(chapter);
                    }
                    subject.setChapterList(chapterList);
                    Log.d(TAG, "onDataChange: " +subject.toString());
                    subjectList.add(subject);
                    studyRecyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
