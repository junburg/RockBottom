package com.junburg.moon.rockbottom.myinfo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.junburg.moon.rockbottom.R;

/**
 * Created by Junburg on 2018. 5. 1..
 */

public class AccountSettingDialogFragment extends DialogFragment {

    private EditText dialogAccountPasswordEditTxt;
    private Button dialogAccountConfirmBtn, dialogAccountCancelBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_account_delete, container);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogAccountPasswordEditTxt = (EditText)view.findViewById(R.id.dialog_account_password_edit_txt);
        dialogAccountConfirmBtn = (Button)view.findViewById(R.id.dialog_account_confirm_btn);
        dialogAccountConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        dialogAccountCancelBtn = (Button)view.findViewById(R.id.dialog_account_cancel_btn);


        return view;

    }
}
