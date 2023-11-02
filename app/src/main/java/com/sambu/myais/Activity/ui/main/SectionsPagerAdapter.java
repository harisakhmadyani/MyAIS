package com.sambu.myais.Activity.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sambu.myais.Activity.ui.istirahat.IstirahatFragment;
import com.sambu.myais.Activity.ui.masuk.MasukFragment;
import com.sambu.myais.Activity.ui.masuk2.Masuk2Fragment;
import com.sambu.myais.Activity.ui.pulang.PulangFragment;
import com.sambu.myais.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {


    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return MasukFragment.newInstance("", "");
        } else if (position == 1) {
            return IstirahatFragment.newInstance("", "");
        } else if (position == 2) {
            return Masuk2Fragment.newInstance("", "");
        } else {
            return PulangFragment.newInstance("", "");
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }





//    public SectionsPagerAdapter(Context context, Integer[] ID, String[] Nama, byte[][] Foto, String[] Kehadiran, Double[] Latitude, Double[] Longitude, String[] Tanggal, Boolean[] Terkirim, String[] PersonelID, Integer[] IDPancang, String[] ShortName, String[] CreatedDate, String[] AppVersion) {
//        super(context, R.layout.listview_history, R.id.Nama, Nama);

    }


