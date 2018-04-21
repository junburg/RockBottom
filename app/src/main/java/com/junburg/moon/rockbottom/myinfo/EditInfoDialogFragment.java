package com.junburg.moon.rockbottom.myinfo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.junburg.moon.rockbottom.R;

/**
 * Created by Junburg on 2018. 4. 17..
 */

public class EditInfoDialogFragment extends DialogFragment{

    public interface EditInfoDialogFragmentListener {
        void onEditInfoClick (DialogFragment dialogf, String targetTxt, String editText, int position);
    }

    // Variables
    private EditInfoDialogFragmentListener editInfoDialogFragmentListener;
    private String targetString, editString;
    private int position;

    // Widgets
    private TextView dialogEditInfoTargetTxt;
    private Button dialogEditInfoConfirmBtn, dialogEditInfoCancelBtn;
    private EditText dialogEditInfoEditTxt;

    // Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public EditInfoDialogFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_info, container);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        Bundle bundle = getArguments();
        position = bundle.getInt("position");
        dialogEditInfoTargetTxt = (TextView)view.findViewById(R.id.dialog_edit_info_target_txt);
        dialogEditInfoEditTxt = (EditText)view.findViewById(R.id.dialog_edit_info_edit_txt);

        dialogEditInfoTargetTxt.setText(bundle.getString("targetString"));
        dialogEditInfoEditTxt.setText(bundle.getString("editString"));

        dialogEditInfoConfirmBtn = (Button)view.findViewById(R.id.dialog_edit_info_confirm_btn);
        dialogEditInfoCancelBtn = (Button)view.findViewById(R.id.dialof_edit_info_cancel_btn);

        dialogEditInfoConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmEditInfo();
            }
        });

        dialogEditInfoCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelEditInfo();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            editInfoDialogFragmentListener = (EditInfoDialogFragmentListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement EditInfoDialogFragmentListener");
        }
    }

    public void confirmEditInfo() {
        String targetKey = "";
        targetString = dialogEditInfoTargetTxt.getText().toString();
        editString = dialogEditInfoEditTxt.getText().toString();
        editInfoDialogFragmentListener.onEditInfoClick(EditInfoDialogFragment.this, targetString, editString, position);
        switch (targetString) {
            case "닉네임": targetKey = "nickName";
                break;
            case "메세지": targetKey = "message";
                break;
            case "소속": targetKey = "teamName";
                break;
            case "Github": targetKey = "github";
                break;
        }
        databaseReference.child("users").child(firebaseUser.getUid()).child(targetKey).setValue(editString);
        dismiss();
    }

    public void cancelEditInfo() {
        this.dismiss();
    }
}
