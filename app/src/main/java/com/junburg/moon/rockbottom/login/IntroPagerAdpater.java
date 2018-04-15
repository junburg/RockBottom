package com.junburg.moon.rockbottom.login;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.junburg.moon.rockbottom.R;

/**
 * Created by Junburg on 2018. 3. 5..
 */

public class IntroPagerAdpater extends FragmentPagerAdapter{

    private int image[] = {R.drawable.rock_bottom_logo, R.drawable.intro_fragment_img_1, R.drawable.intro_fragment_img_2, R.drawable.intro_fragment_img_3};
    private int subject[] = {R.string.intro_subject_first, R.string.intro_subject_second, R.string.intro_subject_third, R.string.intro_subject_fourth};
    private int content[] = {R.string.intro_content_first, R.string.intro_content_second, R.string.intro_content_third, R.string.intro_content_fourth};

    public IntroPagerAdpater(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new IntroFragment().newInstance(image[position], subject[position], content[position]);
    }

    @Override
    public int getCount() {
        return image.length;
    }
}
