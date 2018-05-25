package com.junburg.moon.rockbottom.quiz;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.model.Quiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Junburg on 2018. 5. 24..
 */

public class QuizFragment extends Fragment{

    // Views
    private TextView quizQuestionTxt, quizQuestionProgressTxt;
    private RecyclerView quizRecyclerView;
    private ArrayList<String> exampleList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        initSetting(view);
        viewSetting();
        return view;
    }

    private void initSetting(View view) {
        quizQuestionProgressTxt = (TextView)view.findViewById(R.id.quiz_question_progress_txt);
        quizQuestionTxt = (TextView)view.findViewById(R.id.quiz_question_txt);
        quizRecyclerView = (RecyclerView)view.findViewById(R.id.quiz_recycler);
    }

    private void viewSetting() {
        Bundle bundle = getArguments();
        Quiz quiz = (Quiz) bundle.getParcelable("quizObject");
        String question = quiz.getQuestion();
        int quizProgress = bundle.getInt("quizProgress");
        int quizSize  = bundle.getInt("quizSize");
        quizQuestionTxt.setText(question);
        quizRecyclerView.setHasFixedSize(true);
        quizRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        exampleList.add(quiz.getFirstExample());
        exampleList.add(quiz.getSecondExample());
        exampleList.add(quiz.getThirdExample());
        exampleList.add(quiz.getFourthExample());
        QuizRecyclerAdapter quizRecyclerAdapter = new QuizRecyclerAdapter(exampleList);
        quizRecyclerView.setAdapter(quizRecyclerAdapter);
        quizQuestionProgressTxt.setText((quizProgress + 1) + "/" + quizSize);





    }
}
