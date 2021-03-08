package ru.true_ip.trueip.app.requests_screen.requests_fragment;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseFragment;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.FragmentRequestsBinding;

/**
 * Created by ektitarev on 26.12.2017.
 */

public class RequestsFragment extends BaseFragment<RequestsContract, RequestsPresenter, FragmentRequestsBinding> implements RequestsContract {

    private String title;

    public static RequestsFragment getInstance(Bundle bundle, String title) {
        RequestsFragment fragment = new RequestsFragment();

        fragment.title = title;

        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public FragmentRequestsBinding initBinding(LayoutInflater layoutInflater) {
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_requests, null, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        setItemDecoration();

        presenter.setContext(this.getActivity());
        presenter.setExtras(getArguments());
        presenter.getRequests();
    }

    private void setItemDecoration() {
        if (binding != null) {
            DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
            decoration.setDrawable(getResources().getDrawable(R.drawable.drawable_request_list_divider));
            binding.recyclerView.addItemDecoration(decoration);
        }
    }

    @Override
    public RequestsContract getContract() {
        return this;
    }

    @Override
    public RequestsPresenter createPresenter() {
        return new RequestsPresenter();
    }

    @Override
    public BaseRouter createRouter() {
        return null;
    }

    @Override
    public String getTitle(Context context) {
        return title;
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
}
