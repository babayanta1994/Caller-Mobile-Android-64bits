package ru.true_ip.trueip.app.main_screen.settings_fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.main_screen.MainActivity;
import ru.true_ip.trueip.app.main_screen.MainRouter;
import ru.true_ip.trueip.base.BaseFragment;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.FragmentSettingsBinding;


/**
 * Created by user on 11-Sep-17.
 */

public class SettingsFragment extends BaseFragment<SettingsContract, SettingsPresenter, FragmentSettingsBinding> implements SettingsContract {


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
    public FragmentSettingsBinding initBinding(LayoutInflater layoutInflater) {
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_settings, null, false);
    }

    @Override
    public SettingsContract getContract() {
        return this;
    }

    @Override
    public SettingsPresenter createPresenter() {
        return new SettingsPresenter();
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
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle(getTitle(getContext()));
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.settings);
    }
}
