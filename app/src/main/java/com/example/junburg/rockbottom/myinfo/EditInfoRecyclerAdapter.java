package com.example.junburg.rockbottom.myinfo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.junburg.rockbottom.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Junburg on 2018. 3. 14..
 */

public class EditInfoRecyclerAdapter extends RecyclerView.Adapter<EditInfoRecyclerViewHolder> {

    private ArrayList<EditInfoRecyclerData> editInfoDataList;

    public EditInfoRecyclerAdapter(ArrayList<EditInfoRecyclerData> editInfoDataList) {
        this.editInfoDataList = editInfoDataList;

    }

    @Override
    public EditInfoRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_info_recycler_list_item,  null);
        EditInfoRecyclerViewHolder holder = new EditInfoRecyclerViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(EditInfoRecyclerViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return (editInfoDataList != null) ? editInfoDataList.size() : 0;
    }
}
