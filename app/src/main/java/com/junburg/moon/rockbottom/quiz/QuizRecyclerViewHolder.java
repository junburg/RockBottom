package com.junburg.moon.rockbottom.quiz;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.junburg.moon.rockbottom.R;
/**
 * Created by Junburg on 2018. 5. 24..
 */

public class QuizRecyclerViewHolder extends RecyclerView.ViewHolder{

    protected TextView quizExampleTxt;


    public QuizRecyclerViewHolder(final View itemView) {
        super(itemView);
        quizExampleTxt = (TextView)itemView.findViewById(R.id.quiz_example_txt);
    }
}
