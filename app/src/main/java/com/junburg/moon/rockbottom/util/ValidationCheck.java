package com.junburg.moon.rockbottom.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.junburg.moon.rockbottom.login.EmailSignUpActivity;
import com.junburg.moon.rockbottom.myinfo.EditInfoActivity;
import com.junburg.moon.rockbottom.myinfo.EditInfoDialogFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by Junburg on 2018. 4. 16..
 */

public class ValidationCheck {

    private Context context;
    private boolean doubleCheck;

    public ValidationCheck(Context context) {
        this.context = context;
    }

    public boolean blankStringCheck(String email, String password, String checkPassword) {
        if (email.equals("") || password.equals("") || checkPassword.equals("")) {
            Snackbar.make(((EmailSignUpActivity) context).getWindow().getDecorView().getRootView(), "입력하신 정보의 공백을 확인해주세욘", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean blankStringCheck(String email, String password) {
        if (email.equals("") || password.equals("")) {
            Snackbar.make(((EmailSignUpActivity) context).getWindow().getDecorView().getRootView(), "입력하신 정보의 공백을 확인해주세욘", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean isEmailString(String email) {
        if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            Snackbar.make(((EmailSignUpActivity) context).getWindow().getDecorView().getRootView(), "입력한 이메일을 확인해주세요", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 영어+숫자+특수문자 조합한 8~16자리 사이 비밀번호 체크
     *
     * @param password
     * @return
     */
    public boolean isPasswordPattern(String password) {
        String PasswordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";
        Pattern pattern = Pattern.compile(PasswordPattern);
        Matcher matcher = pattern.matcher(password);
        if (matcher.matches()) {
            return true;
        } else {
            Snackbar.make(((EmailSignUpActivity) context).getWindow().getDecorView().getRootView(), "입력한 비밀번호 양식을 확인해주세요", Snackbar.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean checkSamePassword(String password, String checkPassword) {
        if (!(password.equals(checkPassword))) {
            Snackbar.make(((EmailSignUpActivity) context).getWindow().getDecorView().getRootView(), "입력한 비밀번호를 확인해주세요", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean infoEmptyCheck(TextInputEditText editText) {
        String text = editText.getText().toString();
        if (text.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public boolean infoLengthCheck(TextInputEditText nickNameEditTxt, TextInputEditText messageEditTxt
            , TextInputEditText teamNameEditTxt, TextInputEditText githubEditTxt) {
        String nickName = nickNameEditTxt.getText().toString();
        String message = messageEditTxt.getText().toString();
        String teamName = teamNameEditTxt.getText().toString();
        String github = githubEditTxt.getText().toString();

        if (nickName.length() > 20 || message.length() > 40 || teamName.length() > 20 || github.length() > 40) {
            return false;
        } else {
            return true;
        }
    }

    public boolean editLengthCheck(String targetString, String editString, TextView progressTxt) {
        if (targetString.equals("닉네임")) {
            if (editString.length() > 20 || editString.equals("")) {
                progressTxt.setText("닉네임은 공백과 20자 이상은 허용하지 않아요 :)");
                progressTxt.setVisibility(View.VISIBLE);
                return false;
            }
            return true;
        }
        if (targetString.equals("메세지") || targetString.equals("Github")) {
            if (editString.length() > 40) {
                progressTxt.setText("40자 이상은 허용하지 않아요 :)");
                progressTxt.setVisibility(View.VISIBLE);
                return false;
            }
            return true;
        }
        if (targetString.equals("소속")) {
            if (editString.length() > 20) {
                progressTxt.setText("20자 이상은 허용하지 않아요 :)");
                progressTxt.setVisibility(View.VISIBLE);
                return false;
            }
            return true;
        }
        return false;
    }


}
