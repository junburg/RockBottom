package com.junburg.moon.rockbottom.quiz;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.util.ItemClickListener;

/**
 * Created by Junburg on 2018. 5. 24..
 */

public class QuizRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    protected TextView quizExampleTxt;
    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public QuizRecyclerViewHolder(final View itemView) {
        super(itemView);
        quizExampleTxt = (TextView)itemView.findViewById(R.id.quiz_example_txt);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition());
    }
}
