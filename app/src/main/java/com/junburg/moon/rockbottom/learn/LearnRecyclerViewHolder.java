package com.junburg.moon.rockbottom.learn;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.junburg.moon.rockbottom.R;

/**
 * Created by Junburg on 2018. 3. 4..
 */

public class LearnRecyclerViewHolder extends RecyclerView.ViewHolder {
    protected TextView learnSubjectTxt;
    protected TextView learnContentTxt;

    public LearnRecyclerViewHolder(View itemView) {
        super(itemView);
        learnSubjectTxt = (TextView)itemView.findViewById(R.id.learn_subject_txt);
        learnContentTxt = (TextView)itemView.findViewById(R.id.learn_content_txt);

    }
}
