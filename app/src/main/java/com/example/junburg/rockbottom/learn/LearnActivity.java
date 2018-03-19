package com.example.junburg.rockbottom.learn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.junburg.rockbottom.R;

import java.util.ArrayList;

/**
 * Created by Junburg on 2018. 3. 4..
 */

public class LearnActivity extends AppCompatActivity {

    private RecyclerView learnRecycler;
    private ArrayList<LearnRecyclerData> dataList = new ArrayList<>();
    private Button learnDoneBtn;
    private Button learnLaterBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        createDummyData();

        learnRecycler = (RecyclerView)findViewById(R.id.learn_recycler);
        learnRecycler.setHasFixedSize(true);
        learnRecycler.setLayoutManager(new LinearLayoutManager(this));
        learnRecycler.setAdapter(new LearnRecyclerAdapter(dataList));
        learnDoneBtn = (Button)findViewById(R.id.learn_done_btn);
        learnLaterBtn = (Button)findViewById(R.id.learn_later_btn);

        learnDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        learnLaterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void createDummyData() {
        for(int i=1; i<10; i++) {
            LearnRecyclerData data = new LearnRecyclerData("subject" + i, "content" + i );
            dataList.add(data);
        }

    }


}
