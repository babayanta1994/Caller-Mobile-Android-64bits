package ru.true_ip.trueip.app.main_screen.photo_details_fragment.photo_page_fragment;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseContract;
import ru.true_ip.trueip.base.BaseFragment;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.FragmentPhotoPageBinding;

/**
 * Created by ektitarev on 17.01.2018.
 */

public class PhotoPageFragment extends BaseFragment<BaseContract, PhotoPagePresenter, FragmentPhotoPageBinding> implements BaseContract {

    public static PhotoPageFragment getInstance(Bundle bundle) {
        PhotoPageFragment fragment = new PhotoPageFragment();

        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.setContext(this.getActivity());
        presenter.setExtras(getArguments());
    }

    @Override
    public BaseRouter getRouter() {
        return router;
    }

    @Override
    public FragmentPhotoPageBinding initBinding(LayoutInflater layoutInflater) {
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_photo_page, null, false);
    }

    @Override
    public BaseContract getContract() {
        return this;
    }

    @Override
    public PhotoPagePresenter createPresenter() {
        return new PhotoPagePresenter();
    }

    @Override
    public BaseRouter createRouter() {
        return null;
    }

    @Override
    public String getTitle(Context context) {
        return "";
    }

    @Override
    public void showPreloader() {

    }

    @Override
    public void hidePreloader() {
    }

    @BindingAdapter("android:src")
    public static void setBitmap(ImageView iv, String path) {
        Glide.with(iv.getContext())
                .load(path)
                .apply(new RequestOptions()
                        .fitCenter())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(iv);
        //iv.setImageBitmap(BitmapFactory.decodeFile(path));
    }
}
