package ru.true_ip.trueip.app.main_screen.photo_details_activity.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import ru.true_ip.trueip.app.main_screen.photo_details_fragment.photo_page_fragment.PhotoPageFragment;
import ru.true_ip.trueip.models.PhotoModel;
import ru.true_ip.trueip.utils.Constants;

/**
 * Created by ektitarev on 18.01.2018.
 */

public class PhotoPageAdapter extends FragmentPagerAdapter {

    List<PhotoModel> items;

    public PhotoPageAdapter(FragmentManager fm, List<PhotoModel> items) {
        super(fm);
        this.items = items;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();

        bundle.putString(Constants.PHOTO_PATH, items.get(position).path);
        bundle.putString(Constants.PHOTO_TIMESTAMP, items.get(position).timestamp);

        return PhotoPageFragment.getInstance(bundle);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
