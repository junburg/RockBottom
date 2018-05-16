package com.junburg.moon.rockbottom.login;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.junburg.moon.rockbottom.R;
import com.junburg.moon.rockbottom.util.DataExistCallback;

/**
 * Created by Junburg on 2018. 5. 3..
 */

public class PasswordFindDialogFragment extends DialogFragment {

    // Widgets
    private TextView dialogPasswordFindProgressTxt, dialogPasswordFindConfirmTxt, dialogPasswordFindCancelTxt;
    private EditText dialogPasswordFindEditTxt;

    // View
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_password_find, container);

        initSetup(view);

        // 이메일 정보를 확인하고 비밀번호 변경 메일을 전송함
        dialogPasswordFindConfirmTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if (dialogPasswordFindEditTxt.getText().equals("")) {
                    dialogPasswordFindProgressTxt.setText("올바른 형식의 이메일을 입력해주세요 :)");
                    dialogPasswordFindProgressTxt.setVisibility(View.VISIBLE);
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("확인중입니다");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    checkExistEmail(dialogPasswordFindEditTxt.getText().toString(), new DataExistCallback() {
                        @Override
                        public void onDataExistCheck(boolean check) {
                            if (check) {
                                firebaseAuth.sendPasswordResetEmail(dialogPasswordFindEditTxt.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    dialogPasswordFindProgressTxt.setText("해당 계정으로 변경 메일을 보냈습니다. 확인해주세요 :)");
                                                    dialogPasswordFindProgressTxt.setVisibility(View.VISIBLE);
                                                    progressDialog.dismiss();
                                                }
                                            }
                                        });
                            } else {
                                dialogPasswordFindProgressTxt.setText("등록되지 않은 이메일 입니다 :(");
                                dialogPasswordFindProgressTxt.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            }
        });

        dialogPasswordFindCancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    /**
     * Initialize fragment
     * @param view
     */
    private void initSetup(View view) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        dialogPasswordFindEditTxt = (EditText) view.findViewById(R.id.dialog_password_find_edit_txt);
        dialogPasswordFindConfirmTxt = (TextView) view.findViewById(R.id.dialog_password_find_confirm_btn);
        dialogPasswordFindCancelTxt = (TextView) view.findViewById(R.id.dialog_password_find_cancel_btn);
        dialogPasswordFindProgressTxt = (TextView) view.findViewById(R.id.dialog_password_find_progress_txt);

    }

    /**
     * 비밀번호 변경 메일을 전송하기 전에 존재하는 이메일인지 여부 체크
     * @param email
     * @param dataExistCallback
     */
    public void checkExistEmail(String email, final DataExistCallback dataExistCallback) {
        Query query = databaseReference.child("users").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean existCheck = dataSnapshot.exists();
                dataExistCallback.onDataExistCheck(existCheck);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
