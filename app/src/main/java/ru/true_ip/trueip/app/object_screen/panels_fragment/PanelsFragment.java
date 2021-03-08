package ru.true_ip.trueip.app.object_screen.panels_fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.main_screen.MainRouter;
import ru.true_ip.trueip.app.object_screen.ObjectActivity;
import ru.true_ip.trueip.app.object_screen.abstract_fragments.AbstractDevicesFragment;
import ru.true_ip.trueip.base.BaseFragment;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.FragmentPanelsBinding;
import ru.true_ip.trueip.utils.Constants;


/**
 *
 * Created by user on 19-Sep-17.
 */

public class PanelsFragment extends AbstractDevicesFragment<PanelsContract, PanelsPresenter, FragmentPanelsBinding> implements PanelsContract {

    public static PanelsFragment getInstance(Bundle bundle) {
        PanelsFragment panelsFragment = new PanelsFragment();
        panelsFragment.setArguments(bundle);
        return panelsFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.createPhotoRv();
        presenter.setObjectId(getArguments().getInt(Constants.BUNDLE_INT_KEY));
        presenter.setMustLoadDevices(isMustLoadDevices());
        presenter.setContext(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

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
    public FragmentPanelsBinding initBinding(LayoutInflater layoutInflater) {
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_panels, null, false);
    }

    @Override
    public PanelsContract getContract() {
        return this;
    }

    @Override
    public PanelsPresenter createPresenter() {
        return new PanelsPresenter();
    }

    @Override
    public BaseRouter createRouter() {
        if (getActivity() instanceof ObjectActivity) {
            return ((ObjectActivity) getActivity()).getRouter();
        } else {
            return new MainRouter((ObjectActivity) getActivity());
        }
    }

    @Override
    public void updateDevices() {
        if (presenter != null) {
            Bundle extras = getArguments();
            if (extras != null) {
                presenter.setMustLoadDevices(isMustLoadDevices());
            }
            presenter.getDevicesList();
        }
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.panels);
    }
}
