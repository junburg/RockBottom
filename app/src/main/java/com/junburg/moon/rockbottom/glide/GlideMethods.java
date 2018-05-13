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

    /**
     * EditInfoActivity의 프로필 이미지를 Set
     * @param selfieUri
     * @param imgView
     * @param progressDialog
     */
    public void setProfileImage(String selfieUri, ImageView imgView, final ProgressDialog progressDialog) {
        // 사용자 이미지 정보가 없으면 기본 이미지로 대체
        if (selfieUri.equals("")) {
            imgView.setImageResource(R.drawable.intro_background);
            progressDialog.dismiss();
        } else {
            // ImgView의 ScaleType을 CentorCrop으로 고정(변경되는 이슈가 있음)
            imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
                            if(progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            return false;

                        }
                    })
                    .into(imgView);
        }
    }


    /**
     * 둥근 사용자 프로필 이미지 Set (RankingFragment에서 사용)
     * @param selfieUri
     * @param imgView
     */
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


    /**
     * 둥근 사용자 프로필 이미지 Set (MyInfoFragment에서 사용)
     * @param selfieUri
     * @param imgView
     * @param progressDialog
     */
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
