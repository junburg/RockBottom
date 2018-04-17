package com.junburg.moon.rockbottom.myinfo;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.junburg.moon.rockbottom.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Junburg on 2018. 3. 14..
 */

public class EditInfoRecyclerAdapter extends RecyclerView.Adapter<EditInfoRecyclerViewHolder> {

    private ArrayList<EditInfoRecyclerData> editInfoDataList;
    private Context context;

    public EditInfoRecyclerAdapter(ArrayList<EditInfoRecyclerData> editInfoDataList, Context context) {
        this.editInfoDataList = editInfoDataList;
        this.context = context;

    }

    @Override
    public EditInfoRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_info_recycler_list_item,  null);
        EditInfoRecyclerViewHolder holder = new EditInfoRecyclerViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(EditInfoRecyclerViewHolder holder, final int position) {
        EditInfoRecyclerData editInfoData =  editInfoDataList.get(position);

        Map<String, String> editInfoDataMap = editInfoData.getEditDataMap();
        Iterator<String> keySetIterator = editInfoDataMap.keySet().iterator();

        while (keySetIterator.hasNext()) {
            String key = keySetIterator.next();
            holder.editInfoDataListTxt.setText(key);
            String value = editInfoDataMap.get(key);
            if (value == null) {
                continue;
            }
            holder.editInfoDataContentTxt.setText(value);
        }

        holder.editInfoEditImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

            }
        });


    }

    @Override
    public int getItemCount() {
        return (editInfoDataList != null) ? editInfoDataList.size() : 0;
    }


}
