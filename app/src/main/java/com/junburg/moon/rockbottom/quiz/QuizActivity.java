package com.junburg.moon.rockbottom.quiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.model.Quiz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Junburg on 2018. 5. 24..
 */

public class QuizActivity extends AppCompatActivity {

    // Views
    private ImageButton quizBackBtn, quizForwardBtn;
    private TextView quizConfirmBtn;
    private Fragment quizFragment;
    private ArrayList<Quiz> quizList = new ArrayList<>();
    private int fragmentCount = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initSetting();
        setQuizData();
        viewSetting();

    }

    private void initSetting() {

        quizBackBtn = (ImageButton) findViewById(R.id.quiz_back_btn);
        quizForwardBtn = (ImageButton) findViewById(R.id.quiz_forward_btn);
        quizConfirmBtn = (TextView) findViewById(R.id.quiz_confirm_btn);

    }

    private void viewSetting() {

        quizFragment = new QuizFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("quizObject", quizList.get(0));
        bundle.putInt("quizProgress", 0);
        bundle.putInt("quizSize", quizList.size());
        quizFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, quizFragment).commit();

        quizBackBtn.setVisibility(View.INVISIBLE);
        quizBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentCount > 0 && fragmentCount < quizList.size()) {
                    fragmentCount--;
                    quizFragment = new QuizFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("quizObject", quizList.get(fragmentCount));
                    bundle.putInt("quizProgress", fragmentCount);
                    bundle.putInt("quizSize", quizList.size());
                    quizFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, quizFragment).commit();
                    if (fragmentCount > 0 && fragmentCount < quizList.size() - 1) {
                        quizForwardBtn.setVisibility(View.VISIBLE);
                    }
                    if (fragmentCount == 0) {
                        quizBackBtn.setVisibility(View.INVISIBLE);
                    }
                }
                Log.d("count", "onClick: " + fragmentCount);


            }
        });

        quizForwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentCount >= 0 && fragmentCount < quizList.size()) {
                    fragmentCount++;
                    quizFragment = new QuizFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("quizObject", quizList.get(fragmentCount));
                    bundle.putInt("quizProgress", fragmentCount);
                    bundle.putInt("quizSize", quizList.size());
                    quizFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, quizFragment).commit();
                    if (fragmentCount > 0 && fragmentCount < quizList.size()) {
                        quizBackBtn.setVisibility(View.VISIBLE);
                    }
                    if (fragmentCount == quizList.size() - 1) {
                        quizForwardBtn.setVisibility(View.INVISIBLE);
                    }
                }
                Log.d("count", "onClick: " + fragmentCount);
            }
        });

    }

    private void setQuizData() {

        quizList.clear();

        Quiz quiz1 = new Quiz();
        quiz1.setQuestion("Question1");
        quiz1.setFirstExample("Example1");
        quiz1.setSecondExample("Example2");
        quiz1.setThirdExample("Example3");
        quiz1.setFourthExample("Example4");

        Quiz quiz2 = new Quiz();
        quiz2.setQuestion("Question2");
        quiz2.setFirstExample("Example1");
        quiz2.setSecondExample("Example2");
        quiz2.setThirdExample("Example3");
        quiz2.setFourthExample("Example4");

        Quiz quiz3 = new Quiz();
        quiz3.setQuestion("Question3");
        quiz3.setFirstExample("Example1");
        quiz3.setSecondExample("Example2");
        quiz3.setThirdExample("Example3");
        quiz3.setFourthExample("Example4");

        Quiz quiz4 = new Quiz();
        quiz4.setQuestion("Question4");
        quiz4.setFirstExample("Example1");
        quiz4.setSecondExample("Example2");
        quiz4.setThirdExample("Example3");
        quiz4.setFourthExample("Example4");

        quizList.add(quiz1);
        quizList.add(quiz2);
        quizList.add(quiz3);
        quizList.add(quiz4);


    }


}
