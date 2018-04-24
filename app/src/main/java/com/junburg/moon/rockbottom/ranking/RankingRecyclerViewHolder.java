package com.junburg.moon.rockbottom.ranking;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.study.ChapterRecyclerViewHolder;

/**
 * Created by Junburg on 2018. 3. 4..
 */

public class RankingRecyclerViewHolder extends RecyclerView.ViewHolder {

    protected TextView rankingNickNameTxt;
    protected TextView rankingPointTxt;
    protected TextView rankingNumberTxt;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public RankingRecyclerViewHolder(View itemView, final OnItemClickListener listener) {
        super(itemView);
        rankingNickNameTxt = (TextView)itemView.findViewById(R.id.ranking_list_nick_name_txt);
        rankingPointTxt = (TextView)itemView.findViewById(R.id.ranking_list_points_txt);
        rankingNumberTxt = (TextView)itemView.findViewById(R.id.ranking_list_number_txt);

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
