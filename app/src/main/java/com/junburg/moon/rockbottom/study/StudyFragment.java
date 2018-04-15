package com.junburg.moon.rockbottom.study;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.junburg.moon.rockbottom.R;

import java.util.ArrayList;

/**
 * Created by Junburg on 2018. 2. 7..
 */

public class StudyFragment extends Fragment{

    private VideoView studyVideo;
    private RecyclerView studyRecycler;
    private ArrayList<ChapterRecyclerData> chapterDataList = new ArrayList<>();
    private ArrayList<StudyRecyclerData> studyDataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study, null);
        createStudyData();
        createChpaterData();
        studyVideo = (VideoView)view.findViewById(R.id.study_video);
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
        studyRecycler = (RecyclerView)view.findViewById(R.id.study_recycler_view);
        studyRecycler.setHasFixedSize(true);
        studyRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        studyRecycler.setNestedScrollingEnabled(false);
        studyRecycler.setAdapter(new StudyRecyclerAdapter(studyDataList, chapterDataList, getContext()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        studyVideo.start();
    }

    private void createStudyData() {
        for(int i = 0; i < 5; i++) {
            StudyRecyclerData data = new StudyRecyclerData();
            data.setStudySubjectName("subject" + i);
            data.setStudyUpdateCondition("updating");
            studyDataList.add(data);
        }
    }

    private void createChpaterData() {
        for(int i = 0; i < 6; i++) {
            ChapterRecyclerData data = new ChapterRecyclerData();
            data.setChapterName("chapter" + i);
            chapterDataList.add(data);
        }
    }
}
