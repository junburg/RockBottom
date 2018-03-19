package com.example.junburg.rockbottom.myinfo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.junburg.rockbottom.R;

/**
 * Created by Junburg on 2018. 3. 14..
 */

public class EditInfoRecyclerViewHolder extends RecyclerView.ViewHolder {

    protected TextView editInfoDataListTxt;
    protected TextView editInfoDataContentTxt;
    protected ImageView editInfoEditImg;

    public EditInfoRecyclerViewHolder(View itemView) {
        super(itemView);
        editInfoDataListTxt = (TextView)itemView.findViewById(R.id.edit_info_list_title_txt);
        editInfoDataContentTxt = (TextView)itemView.findViewById(R.id.edit_info_list_content_txt);
        editInfoEditImg = (ImageView)itemView.findViewById(R.id.edit_info_edit_img);
    }
}
