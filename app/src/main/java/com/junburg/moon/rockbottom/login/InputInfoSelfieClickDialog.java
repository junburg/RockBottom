package com.junburg.moon.rockbottom.login;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.junburg.moon.rockbottom.R;

import org.w3c.dom.Text;

/**
 * Created by Junburg on 2018. 3. 23..
 */

public class InputInfoSelfieClickDialog extends Dialog {
    private TextView inputInfoSelfieClickTxt;
    private Context context;

    public InputInfoSelfieClickDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_info_selfie_click);

        inputInfoSelfieClickTxt = (TextView)findViewById(R.id.dialog_selfie_delete_txt);
        inputInfoSelfieClickTxt.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                ((InputInfoActivity)context).inputInfoSelfieImg.setImageResource(R.drawable.ic_selfie_img);
                ((InputInfoActivity)context).isGlideUsed = false;
                dismiss();
            }
        });
    }
}
