package com.junburg.moon.rockbottom.study;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.learn.LearnActivity;

/**
 * Created by Junburg on 2018. 3. 13..
 */

public class ChapterDialogFragment extends DialogFragment {
    private LinearLayout dialogLearnLayout;
    private LinearLayout dialogQuizLayout;
    private TextView dialogChapterBackTxt;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_chapter_click, container);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Bundle bundle = getArguments();
        final int position = bundle.getInt("position");
        final String chapterId = bundle.getString("chapterId");
        final String subjectId = bundle.getString("subjectId");

        dialogLearnLayout = (LinearLayout) view.findViewById(R.id.dialog_learn_layout);
        dialogQuizLayout = (LinearLayout) view.findViewById(R.id.dialog_quiz_layout);

        dialogLearnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LearnActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("chapterId", chapterId);
                intent.putExtra("subjectId", subjectId);
                getActivity().startActivity(intent);
                dismiss();
            }
        });

        dialogChapterBackTxt = (TextView) view.findViewById(R.id.dialog_chapter_back_txt);
        dialogChapterBackTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}

