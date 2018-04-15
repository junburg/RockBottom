package com.junburg.moon.rockbottom.study;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.junburg.moon.rockbottom.R;

/**
 * Created by Junburg on 2018. 3. 12..
 */

public class ChapterRecyclerViewHolder extends RecyclerView.ViewHolder {

    protected TextView chapterNameTxt;
    protected CardView chapterCard;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public ChapterRecyclerViewHolder(View itemView, final OnItemClickListener listener) {
        super(itemView);
        chapterNameTxt = (TextView)itemView.findViewById(R.id.chapter_subject_name_txt);
        chapterCard = (CardView)itemView.findViewById(R.id.chapter_card);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    int position  = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            }
        });

    }
}

