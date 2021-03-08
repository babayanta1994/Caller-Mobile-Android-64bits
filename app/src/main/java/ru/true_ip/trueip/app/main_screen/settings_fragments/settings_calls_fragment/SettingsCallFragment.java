package ru.true_ip.trueip.app.main_screen.settings_fragments.settings_calls_fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.main_screen.MainActivity;
import ru.true_ip.trueip.app.main_screen.MainRouter;
import ru.true_ip.trueip.base.BaseContract;
import ru.true_ip.trueip.base.BaseFragment;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.FragmentSettingsCallBinding;

/**
 * Created by user on 19-Oct-17.
 */

public class SettingsCallFragment extends BaseFragment<BaseContract, SettingsCallPresenter, FragmentSettingsCallBinding> implements BaseContract {

    @Override
    public void showPreloader() {

    }

    @Override
    public void hidePreloader() {

    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).showBackIcon();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity) getActivity()).showHamburgerIcon();
    }

    @Override
    public BaseRouter getRouter() {
        return router;
    }

    @Override
    public FragmentSettingsCallBinding initBinding(LayoutInflater layoutInflater) {
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_settings_call, null, false);
    }

    @Override
    public BaseContract getContract() {
        return this;
    }

    @Override
    public SettingsCallPresenter createPresenter() {
        return new SettingsCallPresenter();
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
        return context.getString(R.string.calls);
    }
}
