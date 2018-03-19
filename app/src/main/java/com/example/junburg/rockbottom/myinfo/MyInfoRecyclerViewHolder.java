package com.example.junburg.rockbottom.myinfo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.junburg.rockbottom.R;

/**
 * Created by Junburg on 2018. 3. 4..
 */

public class MyInfoRecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView myInfoSubjectNameTxt;
    public TextView myInfoDoneNumberTxt;
    public ProgressBar myInfoDoneProgressBar;

    public MyInfoRecyclerViewHolder(View itemView) {
        super(itemView);
        myInfoSubjectNameTxt = (TextView)itemView.findViewById(R.id.my_info_subject_name_txt);
        myInfoDoneNumberTxt = (TextView)itemView.findViewById(R.id.my_info_done_number_txt);
        myInfoDoneProgressBar = (ProgressBar)itemView.findViewById(R.id.my_info_done_progress_bar);

    }
}