package com.udinus.uas4506_11743_11758_11773_11774_12098.Adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.udinus.uas4506_11743_11758_11773_11774_12098.R;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.FragmentKategori.FragmentOlahanDaging;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.FragmentKategori.FragmentSayuran;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.FragmentKategori.FragmentSeafood;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.FragmentKategori.FragmentJajanan;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.FragmentKategori.FragmentKue;
import com.udinus.uas4506_11743_11758_11773_11774_12098.View.FragmentKategori.FragmentMinuman;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{
            R.string.tab_text_1,
            R.string.tab_text_2,
            R.string.tab_text_3,
            R.string.tab_text_4,
            R.string.tab_text_5,
            R.string.tab_text_6
    };

    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new FragmentOlahanDaging();
                break;
            case 1:
                fragment = new FragmentSayuran();
                break;
            case 2:
                fragment = new FragmentSeafood();
                break;
            case 3:
                fragment = new FragmentJajanan();
                break;
            case 4:
                fragment = new FragmentKue();
                break;
            case 5:
                fragment = new FragmentMinuman();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 6 total pages.
        return 6;
    }
}