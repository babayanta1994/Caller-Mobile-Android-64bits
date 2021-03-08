package ru.true_ip.trueip.app.main_screen.objects_fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.main_screen.MainActivity;
import ru.true_ip.trueip.app.main_screen.MainRouter;
import ru.true_ip.trueip.base.BaseFragment;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.FragmentObjectsBinding;
import ru.true_ip.trueip.service.data.BroadcastEventReceiver;
import ru.true_ip.trueip.service.data.SipAccountData;
import ru.true_ip.trueip.service.service.SipServiceCommands;
import ru.true_ip.trueip.utils.Constants;

/**
 *
 * Updated by Andrey Filimonov on 11-Sep-17.
 */

public class ObjectsFragment extends BaseFragment<ObjectsContract, ObjectsPresenter, FragmentObjectsBinding> implements ObjectsContract {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.createObjectRvs();
        binding.objectRv.setItemAnimator(null);
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
    public FragmentObjectsBinding initBinding(LayoutInflater layoutInflater) {
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_objects, null, false);
    }

    @Override
    public ObjectsContract getContract() {
        return this;
    }

    @Override
    public ObjectsPresenter createPresenter() {
        return new ObjectsPresenter();
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
        Bundle extras = getArguments();
        if (extras != null && !extras.getBoolean(Constants.OBJECT_HAS_SIP_NUMBER, true)) {
            presenter.showDialogNoSipNumber(getActivity());
            extras.remove(Constants.OBJECT_HAS_SIP_NUMBER);
            setArguments(extras);
        }
        presenter.onResume();
        presenter.updateAccounts();
        presenter.getAccountsState();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.objects);
    }

    @Override
    public void updateSipAccounts(ArrayList<SipAccountData> accountsList) {
        //SipServiceCommands.updateAccounts(getActivity(), accountsList);
    }

    @Override
    public void registerBroadcastReceiver(BroadcastEventReceiver broadcastEventReceiver) {
        if (broadcastEventReceiver == null) return;
        broadcastEventReceiver.register(getActivity());
    }

    @Override
    public void unregisterBroadcastReceiver(BroadcastEventReceiver broadcastEventReceiver) {
        if (broadcastEventReceiver == null) return;
        broadcastEventReceiver.unregister(getActivity());
    }

    @Override
    public void getAllAccountsState() {
        SipServiceCommands.getAllAccountState(getActivity());
    }

    @Override
    public void reloadHLMObjects() {
        presenter.getHLMObjects();
    }
}
