package com.junburg.moon.rockbottom.login;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.junburg.moon.rockbottom.R;

/**
 * Created by Junburg on 2018. 3. 5..
 */

public class IntroFragment extends Fragment {

    // Views
    private ImageView introImg;
    private TextView introSubjectTxt;
    private TextView introContentTxt;

    /**
     * 이미지 Url, 제목, 본문 데이터를 Set하고 프래그먼트 생성, 반환
     * @param imageUrl
     * @param subject
     * @param content
     * @return
     */
    public static IntroFragment newInstance(int imageUrl, int subject, int content) {

        Bundle args = new Bundle();
        args.putInt("imageUrl", imageUrl);
        args.putInt("subject", subject);
        args.putInt("content", content);
        IntroFragment fragment = new IntroFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_intro, container, false);
        introImg = (ImageView)view.findViewById(R.id.intro_img);
        introSubjectTxt = (TextView)view.findViewById(R.id.intro_subject_txt);
        introContentTxt = (TextView)view.findViewById(R.id.intro_content_txt);
        introImg.setImageResource(getArguments().getInt("imageUrl"));
        introSubjectTxt.setText(getArguments().getInt("subject"));
        introContentTxt.setText(getArguments().getInt("content"));

        return view;
    }
}
