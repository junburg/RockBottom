package com.example.junburg.rockbottom.myinfo;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.junburg.rockbottom.R;

import java.util.ArrayList;

/**
 * Created by Junburg on 2018. 2. 13..
 */

public class MyInfoRecyclerAdapter extends RecyclerView.Adapter<MyInfoRecyclerViewHolder>{

    private ArrayList<MyInfoRecyclerData> dataList;

    MyInfoRecyclerAdapter(ArrayList<MyInfoRecyclerData> dataList) {
        this.dataList = dataList;
    }

    @Override
    public MyInfoRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_info_recycler_list_item, parent, false);
        MyInfoRecyclerViewHolder myInfoRecyclerViewHolder = new MyInfoRecyclerViewHolder(v);
        return myInfoRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(MyInfoRecyclerViewHolder holder, int position) {
        MyInfoRecyclerData data = dataList.get(position);

        holder.myInfoSubjectNameTxt.setText(data.getMyInfoSubjectName());
        String number = data.getMyInfoDoneNumber() + "/" + data.getMyInfoSubjectNumber();
        holder.myInfoDoneNumberTxt.setText(number);
        holder.myInfoDoneProgressBar.setMax(data.getMyInfoSubjectNumber());
        holder.myInfoDoneProgressBar.setProgress(data.getMyInfoDoneNumber());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}
