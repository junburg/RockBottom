package com.junburg.moon.rockbottom.myinfo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
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
    private FragmentManager fm;

    public EditInfoRecyclerAdapter(ArrayList<EditInfoRecyclerData> editInfoDataList, Context context, FragmentManager fm) {
        this.editInfoDataList = editInfoDataList;
        this.context = context;
        this.fm = fm;

    }

    @Override
    public EditInfoRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_info_recycler_list_item, null);
        EditInfoRecyclerViewHolder holder = new EditInfoRecyclerViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final EditInfoRecyclerViewHolder holder, final int position) {
        EditInfoRecyclerData editInfoData = editInfoDataList.get(position);

        Map<String, String> editInfoDataMap = editInfoData.getEditDataMap();
        Iterator<String> keySetIterator = editInfoDataMap.keySet().iterator();

        // 기본 프로필 정보를 리사이클러 뷰에 Set
        while (keySetIterator.hasNext()) {
            String key = keySetIterator.next();
            holder.editInfoDataListTxt.setText(key);
            String value = editInfoDataMap.get(key);
            if (value == null) {
                continue;
            }
            holder.editInfoDataContentTxt.setText(value);
        }

        // 수정할 정보의 종류에 따라 다이얼로그 프래그먼트에 보낼 정보가 달라짐
        holder.editInfoEditImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    case 0:
                        showDialog(holder.editInfoDataListTxt.getText().toString()
                                , holder.editInfoDataContentTxt.getText().toString()
                                , position);
                        break;
                    case 1:
                        showDialog(holder.editInfoDataListTxt.getText().toString()
                                , holder.editInfoDataContentTxt.getText().toString()
                                , position);
                        break;
                    case 2:
                        showDialog(holder.editInfoDataListTxt.getText().toString()
                                , holder.editInfoDataContentTxt.getText().toString()
                                , position);
                        break;
                    case 3:
                        showDialog(holder.editInfoDataListTxt.getText().toString()
                                , holder.editInfoDataContentTxt.getText().toString()
                                , position);
                        break;

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return (editInfoDataList != null) ? editInfoDataList.size() : 0;
    }

    /**
     * 수정할 정보와 함께 다이얼로그 프래그먼트 생성
     * @param targetString
     * @param editString
     * @param position
     */
    private void showDialog(String targetString, String editString, int position) {
        EditInfoDialogFragment dialogFragment = new EditInfoDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("targetString", targetString);
        bundle.putString("editString", editString);
        bundle.putInt("position", position);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(fm, "EditInfoDialogFragment");
    }

    /**
     * 다이얼로그 프래그먼트로 부터 정보를 수정
     * @param position
     * @param editInfoRecyclerData
     */
    protected void editItemFromDialog(int position, EditInfoRecyclerData editInfoRecyclerData) {
        editInfoDataList.set(position, editInfoRecyclerData);
        notifyDataSetChanged();
    }


}
