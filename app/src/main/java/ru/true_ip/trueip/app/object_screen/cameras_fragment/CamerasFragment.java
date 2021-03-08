package ru.true_ip.trueip.app.object_screen.cameras_fragment;

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
import ru.true_ip.trueip.databinding.FragmentCamerasBinding;
import ru.true_ip.trueip.utils.Constants;


/**
 * Created by user on 19-Sep-17.
 */

public class CamerasFragment extends AbstractDevicesFragment<CamerasContract, CamerasPresenter, FragmentCamerasBinding> implements CamerasContract {

    public static CamerasFragment getInstance(Bundle bundle) {
        CamerasFragment camerasFragment = new CamerasFragment();
        camerasFragment.setArguments(bundle);
        return camerasFragment;
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
    public void onDestroyView() {
        super.onDestroyView();
        //presenter.deinit();
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
    public FragmentCamerasBinding initBinding(LayoutInflater layoutInflater) {
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_cameras, null, false);
    }

    @Override
    public CamerasContract getContract() {
        return this;
    }

    @Override
    public CamerasPresenter createPresenter() {
        return new CamerasPresenter();
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
        return context.getString(R.string.cameras);
    }
}
