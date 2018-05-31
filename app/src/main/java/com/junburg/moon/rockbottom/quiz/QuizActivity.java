package com.junburg.moon.rockbottom.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.learn.LearnActivity;
import com.junburg.moon.rockbottom.login.LoginActivity;
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

    private static final String TAG = "QuizActivity";

    // Views
    private ImageButton quizBackBtn, quizForwardBtn;
    private TextView quizConfirmBtn;
    private Fragment quizFragment;
    private ArrayList<Quiz> quizList = new ArrayList<>();
    private int fragmentCount = 0;

    // Objects
    private Intent intent;

    // Variables
    private String chapterId;
    private String subjectId;
    private int position;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initSetting();
        setQuizData();

    }

    private void initSetting() {

        // Views
        quizBackBtn = (ImageButton) findViewById(R.id.quiz_back_btn);
        quizForwardBtn = (ImageButton) findViewById(R.id.quiz_forward_btn);
        quizConfirmBtn = (TextView) findViewById(R.id.quiz_confirm_btn);

        // Intent
        intent = getIntent();
        chapterId = intent.getStringExtra("chapterId");
        subjectId = intent.getStringExtra("subjectId");
        position = intent.getIntExtra("position", 0);

        // Firebases
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser == null) {
                    Intent intent = new Intent(QuizActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        };



    }

    private void viewSetting() {

        makeQuizFragment(0, quizList.size(), quizList.get(0));

        quizBackBtn.setVisibility(View.INVISIBLE);
        quizBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentCount > 0 && fragmentCount < quizList.size()) {
                    fragmentCount--;
                    makeQuizFragment(fragmentCount, quizList.size(), quizList.get(fragmentCount));
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
                    makeQuizFragment(fragmentCount, quizList.size(), quizList.get(fragmentCount));
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
        databaseReference.child("quiz").child(subjectId).child(chapterId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                quizList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Quiz quiz = ds.getValue(Quiz.class);
                    Log.d(TAG, "quiz: " + quiz.toString());
                    quizList.add(quiz);
                }

                viewSetting();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    private void makeQuizFragment(int quizProgress, int quizSize, Quiz quiz) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, QuizFragment.newInstance(quizProgress, quizSize, quiz)).commit();
    }


}
