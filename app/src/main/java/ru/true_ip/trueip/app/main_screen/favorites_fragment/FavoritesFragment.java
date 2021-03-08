package ru.true_ip.trueip.app.main_screen.favorites_fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.main_screen.MainActivity;
import ru.true_ip.trueip.app.main_screen.MainRouter;
import ru.true_ip.trueip.base.BaseFragment;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.FragmentFavoritesBinding;

/**
 * Created by user on 11-Sep-17.
 */

public class FavoritesFragment extends BaseFragment<FavoritesContract, FavoritesPresenter, FragmentFavoritesBinding> implements FavoritesContract {
    private final static String TAG = FavoritesFragment.class.getSimpleName();
    @Override
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
    public FragmentFavoritesBinding initBinding(LayoutInflater layoutInflater) {
        //Logger.error(TAG, "initBinding");
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_favorites, null, false);
    }

    @Override
    public FavoritesContract getContract() {
        return this;
    }

    @Override
    public FavoritesPresenter createPresenter() {
        FavoritesPresenter favoritesPresenter = new FavoritesPresenter();
        favoritesPresenter.setContext(getContext());
        return favoritesPresenter;
    }

    @Override
    public BaseRouter createRouter() {
        if (getActivity() instanceof MainActivity) {
            return ((MainActivity) getActivity()).getRouter();
        } else {
            return new MainRouter((MainActivity) getActivity());
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.setFavoritesAdapter();
    }

    public void updateDevices() {
        presenter.getDevicesList();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle(getTitle(getContext()));
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.favorites);
    }
}
