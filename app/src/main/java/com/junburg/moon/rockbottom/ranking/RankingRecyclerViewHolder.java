package com.junburg.moon.rockbottom.ranking;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.junburg.moon.rockbottom.R;

/**
 * Created by Junburg on 2018. 3. 4..
 */

public class RankingRecyclerViewHolder extends RecyclerView.ViewHolder {

    protected ImageView rankingSelfieImg;
    protected TextView rankingNickNameTxt;
    protected TextView rankingPointTxt;
    protected TextView rankingMessageTxt;
    protected TextView rankingNumberTxt;
    protected TextView rankingGithubTxt;
    protected TextView rankingTeamTxt;

    public RankingRecyclerViewHolder(View itemView) {
        super(itemView);
        rankingSelfieImg = (ImageView)itemView.findViewById(R.id.ranking_selfie_img);
        rankingNickNameTxt = (TextView)itemView.findViewById(R.id.ranking_nick_name_txt);
        rankingPointTxt = (TextView)itemView.findViewById(R.id.ranking_point_txt);
        rankingMessageTxt = (TextView)itemView.findViewById(R.id.ranking_message_txt);
        rankingNumberTxt = (TextView)itemView.findViewById(R.id.ranking_number_txt);
        rankingGithubTxt = (TextView)itemView.findViewById(R.id.ranking_github_txt);
        rankingTeamTxt = (TextView)itemView.findViewById(R.id.ranking_team_txt);

    }
}
