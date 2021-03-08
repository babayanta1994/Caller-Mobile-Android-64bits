package ru.true_ip.trueip.app.main_screen.hlm_fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.main_screen.MainActivity;
import ru.true_ip.trueip.app.main_screen.MainRouter;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.base.BaseFragment;
import ru.true_ip.trueip.databinding.FragmentHlmBinding;
import ru.true_ip.trueip.service.service.Logger;

/**
 *
 * Created by Andrey Filimonov on 28.12.2017.
 */

public class HLMFragment extends BaseFragment<HLMContract, HLMPresenter, FragmentHlmBinding> implements HLMContract {
    private final static String TAG = HLMFragment.class.getSimpleName();
    private int selectedObject = -1;
    private Handler handler = new Handler();
    private View.OnLayoutChangeListener listener;

    private HLMFragmentObjectAdapter objectAdapter;

    @Override
    public void showPreloader() { }

    @Override
    public void hidePreloader() { }

    @Override
    public BaseRouter getRouter() { return router; }

    @Override
    public FragmentHlmBinding initBinding(LayoutInflater layoutInflater) {
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_hlm, null, false);
    }

    @Override
    public HLMContract getContract() {
        return this;
    }

    @Override
    public HLMPresenter createPresenter() {
        HLMPresenter hlmPresenter = new HLMPresenter();
        hlmPresenter.setContext(getContext());
        return hlmPresenter;
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

        binding.infoLink.setMovementMethod(LinkMovementMethod.getInstance());
        binding.hlmFragmentRv.setNestedScrollingEnabled(false);
        listener = (View v, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) -> {
            setLinkLayoutParams();
        };

        binding.hlmFragmentRv.addOnLayoutChangeListener(listener);
    }

    private void setLinkLayoutParams() {
        HLMFragmentObjectAdapter adapter = (HLMFragmentObjectAdapter)binding.hlmFragmentRv.getAdapter();
        handler.post(() -> binding.hlmFragmentRv.removeOnLayoutChangeListener(listener));
        if (adapter != null && adapter.getItemCount() > 0) {
            if (isListScrollable()) {
                handler.post(this::setPositionBelowList);
            } else {
                handler.post(this::setPositionStickToBottom);
            }
        } else {
            handler.post(this::setPositionCenter);
        }
        handler.postDelayed(() -> binding.hlmFragmentRv.addOnLayoutChangeListener(listener), 100);
    }

    private void setPositionStickToBottom() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)binding.infoLink.getLayoutParams();
        layoutParams = clearRules(layoutParams);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        binding.infoLink.setLayoutParams(layoutParams);
    }

    private void setPositionCenter() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)binding.infoLink.getLayoutParams();
        layoutParams = clearRules(layoutParams);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        binding.infoLink.setLayoutParams(layoutParams);
    }

    private void setPositionBelowList() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)binding.infoLink.getLayoutParams();
        layoutParams = clearRules(layoutParams);
        layoutParams.addRule(RelativeLayout.BELOW, binding.hlmFragmentRv.getId());
        binding.infoLink.setLayoutParams(layoutParams);
    }

    private RelativeLayout.LayoutParams clearRules(RelativeLayout.LayoutParams params) {
        params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.removeRule(RelativeLayout.BELOW);
        params.removeRule(RelativeLayout.CENTER_IN_PARENT);

        return params;
    }

    private boolean isListScrollable() {
        NestedScrollView scrollView = binding.scrollView;
        int childHeight = binding.hlmFragmentRv.getHeight();
        int scrollHeight = scrollView.getHeight();
        int paddings = Math.round(getResources().getDimension(R.dimen.dimen_10dp)) + getLinkLabelHeight();

        return scrollHeight < childHeight + paddings;
    }

    private int getLinkLabelHeight() {
        int height = binding.infoLink.getHeight();

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)binding.infoLink.getLayoutParams();
        return height + params.topMargin + params.bottomMargin;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle(getTitle(getContext()));
        loadObjects();
        setLinkLayoutParams();
    }

    @Override
    public void onPause() {
        super.onPause();
        selectedObject = objectAdapter.getSelectedPosition();
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.hlm);
    }

    public void loadObjects() {
        //Logger.error(TAG, "===> Load objects");
        presenter.getRepoController().getHLMObjects().subscribe(objects -> {
            objectAdapter  = new HLMFragmentObjectAdapter(R.layout.item_hlm_object, objects, getActivity(), presenter.getRepoController(), presenter.getApiController());
        }, throwable -> Logger.error(TAG, "loadFavorites: throuwable", throwable));
        objectAdapter.setSelectedPosition(selectedObject);
        binding.hlmFragmentRv.setAdapter(objectAdapter);
    }
}
