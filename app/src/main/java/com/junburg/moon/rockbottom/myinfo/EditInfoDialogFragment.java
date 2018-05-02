package com.junburg.moon.rockbottom.myinfo;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.util.DataExistCallback;
import com.junburg.moon.rockbottom.util.ValidationCheck;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Junburg on 2018. 4. 17..
 */

public class EditInfoDialogFragment extends DialogFragment {
    private static final String TAG = "EditInfoDialogFragment";

    public interface EditInfoDialogFragmentListener {
        void onEditInfoClick(DialogFragment dialog, String targetTxt, String editText, int position);
    }

    // Variables
    private EditInfoDialogFragmentListener editInfoDialogFragmentListener;
    private String targetString, editString;
    private int position;
    private ValidationCheck validationCheck;

    // Widgets
    private TextView dialogEditInfoTargetTxt, dialogEditInfoProgressTxt;
    private Button dialogEditInfoConfirmBtn, dialogEditInfoCancelBtn, dialogEditInfoDoubleCheckBtn;
    private EditText dialogEditInfoEditTxt;


    // Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private boolean checkOk = false;

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
        validationCheck = new ValidationCheck(getActivity());

        final Bundle bundle = getArguments();
        position = bundle.getInt("position");
        dialogEditInfoTargetTxt = (TextView) view.findViewById(R.id.dialog_edit_info_target_txt);
        dialogEditInfoProgressTxt = (TextView) view.findViewById(R.id.dialog_edit_info_progress_txt);
        dialogEditInfoEditTxt = (EditText) view.findViewById(R.id.dialog_edit_info_edit_txt);

        dialogEditInfoTargetTxt.setText(bundle.getString("targetString"));
        dialogEditInfoEditTxt.setText(bundle.getString("editString"));

        dialogEditInfoConfirmBtn = (Button) view.findViewById(R.id.dialog_edit_info_confirm_btn);
        dialogEditInfoCancelBtn = (Button) view.findViewById(R.id.dialog_edit_info_cancel_btn);
        dialogEditInfoDoubleCheckBtn = (Button) view.findViewById(R.id.dialog_edit_info_double_check_btn);

        if (bundle.getString("targetString").equals("닉네임")) {
            dialogEditInfoDoubleCheckBtn.setVisibility(View.VISIBLE);
        }

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);


        dialogEditInfoConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(dialogEditInfoConfirmBtn.getWindowToken(),0);
                if (checkOk && bundle.getString("targetString").equals("닉네임")) {
                    confirmEditInfo();
                } else if (bundle.getString("targetString").equals("닉네임")) {
                    dialogEditInfoProgressTxt.setVisibility(View.VISIBLE);
                    dialogEditInfoProgressTxt.setText("중복검사를 다시 해주세요");
                } else {
                    confirmEditInfo();
                }
            }
        });

        dialogEditInfoCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelEditInfo();
            }
        });

        dialogEditInfoDoubleCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(dialogEditInfoDoubleCheckBtn.getWindowToken(),0);
                if (dialogEditInfoEditTxt.getText().length() > 20 || dialogEditInfoEditTxt.getText().toString().equals("")) {
                    dialogEditInfoProgressTxt.setText("닉네임은 공백과 20자 이상은 허용하지 않아요 :)");
                    dialogEditInfoEditTxt.setVisibility(View.VISIBLE);
                } else if (bundle.getString("editString").equals(dialogEditInfoEditTxt.getText().toString())) {
                    dialogEditInfoProgressTxt.setText("사용가능한 닉네임입니다 :)");
                    dialogEditInfoEditTxt.setVisibility(View.VISIBLE);
                    checkOk = true;
                } else {
                    nickNameDoubleCheck(dialogEditInfoEditTxt.getText().toString(), new DataExistCallback() {
                        @Override
                        public void onDataExistCheck(boolean check) {
                            if (check) {

                                dialogEditInfoProgressTxt.setVisibility(View.VISIBLE);
                                dialogEditInfoProgressTxt.setText("중복되는 닉네임이 존재합니다");
                                checkOk = false;
                            } else {

                                dialogEditInfoProgressTxt.setVisibility(View.VISIBLE);
                                dialogEditInfoProgressTxt.setText("사용가능한 닉네임 입니다");
                                checkOk = true;
                            }
                        }
                    });
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            editInfoDialogFragmentListener = (EditInfoDialogFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement EditInfoDialogFragmentListener");
        }
    }

    public boolean validationCheck() {
        targetString = dialogEditInfoTargetTxt.getText().toString();
        editString = dialogEditInfoEditTxt.getText().toString();
        return validationCheck.editLengthCheck(targetString, editString, dialogEditInfoProgressTxt);
    }

    public void confirmEditInfo() {
        String targetKey = "";
        targetString = dialogEditInfoTargetTxt.getText().toString();
        editString = dialogEditInfoEditTxt.getText().toString();
        switch (targetString) {
            case "닉네임":
                targetKey = "nickName";
                break;
            case "메세지":
                targetKey = "message";
                break;
            case "소속":
                targetKey = "teamName";
                break;
            case "Github":
                targetKey = "github";
                break;
        }
        if (validationCheck()) {
            databaseReference.child("users").child(firebaseUser.getUid()).child(targetKey).setValue(editString);
            editInfoDialogFragmentListener.onEditInfoClick(EditInfoDialogFragment.this, targetString, editString, position);
            dismiss();
        }

    }

    public void nickNameDoubleCheck(final String nickName, final DataExistCallback dataExistCallback) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("중복 검사중입니다");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Query query = databaseReference.child("users").orderByChild("nickName").equalTo(nickName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean doubleCheck = dataSnapshot.exists();
                dataExistCallback.onDataExistCheck(doubleCheck);
                progressDialog.dismiss();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void cancelEditInfo() {
        this.dismiss();
    }
}
