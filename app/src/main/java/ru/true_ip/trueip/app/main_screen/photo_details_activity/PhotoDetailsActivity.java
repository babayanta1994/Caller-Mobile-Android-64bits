package ru.true_ip.trueip.app.main_screen.photo_details_activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.main_screen.photo_details_activity.adapters.PhotoPageAdapter;
import ru.true_ip.trueip.app.main_screen.photo_details_fragment.photo_page_fragment.PhotoPageFragment;
import ru.true_ip.trueip.base.BaseActivity;
import ru.true_ip.trueip.base.BaseContract;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.ActivityPhotoDetailsBinding;
import ru.true_ip.trueip.utils.Constants;

/**
 * Created by ektitarev on 18.01.2018.
 */

public class PhotoDetailsActivity extends BaseActivity<BaseContract, PhotoDetailsPresenter, ActivityPhotoDetailsBinding> implements BaseContract {

    public static void start(Context context, Bundle bundle, boolean startForResult) {
        Intent intent = new Intent(context, PhotoDetailsActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (startForResult && context instanceof Activity) {
            ((Activity)context).startActivityForResult(intent, 1);
        } else {
            context.startActivity(intent);
        }
    }

    public static void start(Fragment fragment, Bundle bundle, boolean startForResult) {
        Intent intent = new Intent(fragment.getActivity(), PhotoDetailsActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (startForResult) {
            fragment.startActivityForResult(intent, 1);
        } else {
            fragment.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(binding != null) {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

            initPager();

            presenter.setContext(this);
            presenter.setExtras(getIntent().getExtras());
            presenter.createViewPager();
        }
    }

    private void initPager() {
        ViewPager pager = binding.getRoot().findViewById(R.id.view_pager);

        pager.clearOnPageChangeListeners();
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {}

            @Override
            public void onPageSelected(int i) {
                PhotoPageAdapter adapter = (PhotoPageAdapter)pager.getAdapter();

                if (adapter != null && i >= 0 && i < adapter.getCount()) {
                    PhotoPageFragment fragment = (PhotoPageFragment) adapter.getItem(i);

                    Bundle bundle = fragment.getArguments();

                    presenter.photoTimestamp.set(bundle.getString(Constants.PHOTO_TIMESTAMP));
                    presenter.currentPosition = i;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        pager.addOnAdapterChangeListener(new ViewPager.OnAdapterChangeListener() {
            @Override
            public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter pagerAdapter, @Nullable PagerAdapter pagerAdapter1) {
                pager.setCurrentItem(presenter.currentPosition);
                if (presenter.currentPosition == 0) {
                    if (pagerAdapter1 != null) {

                        PhotoPageAdapter photoPageAdapter = (PhotoPageAdapter) pagerAdapter1;
                        if (photoPageAdapter.getCount() > 0) {

                            PhotoPageFragment fragment = (PhotoPageFragment) photoPageAdapter.getItem(0);

                            Bundle bundle = fragment.getArguments();

                            presenter.photoTimestamp.set(bundle.getString(Constants.PHOTO_TIMESTAMP));
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }    @Override

    public void showPreloader() {

    }

    @Override
    public void hidePreloader() {

    }

    @Override
    public BaseRouter getRouter() {
        return router;
    }

    @Override
    public ActivityPhotoDetailsBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_photo_details, null, false);
    }

    @Override
    public BaseContract getContract() {
        return this;
    }

    @Override
    public PhotoDetailsPresenter createPresenter() {
        return new PhotoDetailsPresenter();
    }

    @Override
    public BaseRouter createRouter() {
        return new PhotoDetailsRouter(this);
    }
}
