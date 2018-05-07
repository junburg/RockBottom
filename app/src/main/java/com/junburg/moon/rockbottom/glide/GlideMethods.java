package com.junburg.moon.rockbottom.glide;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.junburg.moon.rockbottom.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Junburg on 2018. 4. 17..
 */

public class GlideMethods {

    private Context context;

    public GlideMethods(Context context) {
        this.context = context;
    }

    public void setProfileImage(String selfieUri, ImageView imgView, final ProgressDialog progressDialog) {
        if (selfieUri.equals("")) {
            imgView.setImageResource(R.drawable.intro_background);
            progressDialog.dismiss();
        } else {
            Glide.with(context)
                    .load(selfieUri)
                    .crossFade()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressDialog.dismiss();
                            return false;

                        }
                    })
                    .into(imgView);
        }
    }

    public void setCircleProfileImage(String selfieUri, CircleImageView imgView) {
        if (selfieUri.equals("")) {
            imgView.setImageResource(R.drawable.intro_background);
        } else {
            Glide.with(context)
                    .load(selfieUri)
                    .crossFade()
                    .into(imgView);
        }
    }

    public void setCircleProfileImageMyInfo(String selfieUri, CircleImageView imgView, final ProgressDialog progressDialog) {
        if (selfieUri.equals("")) {
            imgView.setImageResource(R.drawable.intro_background);
            progressDialog.dismiss();
        } else {
            Glide.with(context)
                    .load(selfieUri)
                    .crossFade()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressDialog.dismiss();
                            return false;
                        }
                    })
                    .into(imgView);
        }
    }
}
