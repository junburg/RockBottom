package com.junburg.moon.rockbottom.glide;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.junburg.moon.rockbottom.R;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

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
        if(selfieUri.equals("")) {
            Uri uri = Uri.parse(
                    "android.resource://"
                            + context.getPackageName()
                            + "/"
                            + R.drawable.rock_bottom_logo
            );
            Glide.with(context)
                    .load(uri)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .crossFade()
                    .into(imgView);
        } else {
            Glide.with(context)
                    .load(selfieUri)
                    .crossFade()
                    .into(imgView);
        }
    }

    public void setCircleProfileImage(String selfieUri, CircleImageView imgView) {
        if(selfieUri.equals("")) {
            Uri uri = Uri.parse(
                    "android.resource://"
                            + context.getPackageName()
                            + "/"
                            + R.drawable.rock_bottom_logo
            );
            Glide.with(context)
                    .load(uri)
                    .crossFade()
                    .into(imgView);
        } else {
            Glide.with(context)
                    .load(selfieUri)
                    .crossFade()
                    .into(imgView);

        }
    }
}
