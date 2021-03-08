package ru.true_ip.trueip.app.requests_screen.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ektitarev on 26.12.2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    protected List<Fragment> fragments;
    protected List<String> fragmentsTitles;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragmentsTitles = new ArrayList<>();
    }

    public void addPageFragment(Fragment pageFragment, String pageTitle) {
        fragments.add(pageFragment);
        fragmentsTitles.add(pageTitle);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentsTitles.get(position);
    }
}
