package com.junburg.moon.rockbottom;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

/**
 * Created by Junburg on 2018. 4. 24..
 */

public class AppForFont extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/jejugothic.otf"))
                .addBold(Typekit.createFromAsset(this, "fonts/jejugothic.otf"));
    }
}
