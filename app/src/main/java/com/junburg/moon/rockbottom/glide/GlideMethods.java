package com.junburg.moon.rockbottom.glide;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.junburg.moon.rockbottom.R;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.content.ContentValues.TAG;

/**
 * Created by Junburg on 2018. 4. 17..
 */

public class GlideMethods {

    private Context context;
    private Uri selfieUri;
    private ImageView imgView;

    public GlideMethods(Context context) {
        this.context = context;
    }

    public void setProfileImage(String selfieUri, ImageView imgView) {
        Log.d(TAG, "setProfileImage: " + selfieUri.toString());
        if (selfieUri.equals("")) {
            imgView.setImageResource(R.drawable.rock_bottom_logo);
        } else {
            Glide.with(context)
                    .load(selfieUri)
                    .crossFade()
                    .into(imgView);
        }
    }

    public void setCircleProfileImage(String selfieUri, CircleImageView imgView) {
        if (selfieUri.equals("")) {
            imgView.setImageResource(R.drawable.rock_bottom_logo);
        } else {
            Glide.with(context)
                    .load(selfieUri)
                    .crossFade()
                    .into(imgView);

        }
    }

    public void setRankingImage(String selfieUri, ImageView imgView) {
        if (!(selfieUri.equals(""))) {
            Glide.with(context)
                    .load(selfieUri)
                    .crossFade()
                    .into(imgView);
        }
    }
}
