package com.example.junburg.rockbottom.study;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.junburg.rockbottom.R;
import com.example.junburg.rockbottom.learn.LearnActivity;

/**
 * Created by Junburg on 2018. 3. 13..
 */

public class ChapterClickDialog extends Dialog  {
    private Context context;
    private LinearLayout dialogLearnLayout;
    private LinearLayout dialogQuizLayout;

    public ChapterClickDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_chapter_click);

        dialogLearnLayout = (LinearLayout) findViewById(R.id.dialog_learn_layout);
        dialogQuizLayout = (LinearLayout) findViewById(R.id.dialog_quiz_layout);

        dialogLearnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LearnActivity.class);
                context.startActivity(intent);
                dismiss();
            }
        });

    }
}
