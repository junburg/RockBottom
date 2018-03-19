package com.example.junburg.rockbottom.login;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.junburg.rockbottom.R;

/**
 * Created by Junburg on 2018. 3. 5..
 */

public class IntroFragment extends Fragment {

    private ImageView introImg;
    private TextView introSubjectTxt;
    private TextView introContentTxt;

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
