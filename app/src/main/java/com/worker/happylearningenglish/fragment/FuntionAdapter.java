package com.worker.happylearningenglish.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.worker.happylearningenglish.fragment.ListeningFragment;
import com.worker.happylearningenglish.fragment.ReadingFragment;
import com.worker.happylearningenglish.fragment.RecordingFragment;

/**
 * Created by hataketsu on 4/23/16.
 */
public class FuntionAdapter extends FragmentPagerAdapter {
    public static final CharSequence[] TITLES = {"NGHE PODCAST", "ĐỌC TIN TỨC", "GHI ÂM", "TRẮC NGHIỆM"};
    public static final int COUNT = 4;
    private Fragment[] fragments = new Fragment[COUNT];

    public FuntionAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
        fragments[0] = new ListeningFragment();
        fragments[1] = new ReadingFragment();
        fragments[2] = new RecordingFragment();
        fragments[3] = new TestFragment();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
