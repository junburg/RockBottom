package com.junburg.moon.rockbottom.myinfo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.junburg.moon.rockbottom.R;

/**
 * Created by Junburg on 2018. 3. 4..
 */

public class StudyConditionRecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView studyConditionSubjectNameTxt;
    public TextView studyConditionDoneNumberTxt;
    public ProgressBar studyConditionProgressBar;

    public StudyConditionRecyclerViewHolder(View itemView) {
        super(itemView);
        studyConditionSubjectNameTxt = (TextView)itemView.findViewById(R.id.study_condition_subject_name_txt);
        studyConditionDoneNumberTxt = (TextView)itemView.findViewById(R.id.study_condition_done_number_txt);
        studyConditionProgressBar = (ProgressBar)itemView.findViewById(R.id.study_condition_done_progress_bar);

    }
}