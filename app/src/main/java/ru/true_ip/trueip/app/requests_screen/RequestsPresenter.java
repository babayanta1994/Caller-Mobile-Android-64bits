package ru.true_ip.trueip.app.requests_screen;

import android.content.Context;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.requests_screen.adapters.ViewPagerAdapter;
import ru.true_ip.trueip.app.requests_screen.requests_fragment.RequestsFragment;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.models.responses.ClaimModel;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;

/**
 * Created by ektitarev on 26.12.2017.
 */

public class RequestsPresenter extends BasePresenter<RequestsContract> {

    public ObservableField<ViewPagerAdapter> viewPagerAdapter = new ObservableField<>();
    public List<ClaimModel> itemsToShow = new ArrayList<>();

    private Context context;

    private int objectId;

    public void setContext(Context context) { this.context = context; }

    public void onHomeClick(View v) {
        RequestsContract contract = getContract();
        if (contract != null) {
            contract.getRouter().moveBackward();
        }
    }

    public void createViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(((FragmentActivity)context).getSupportFragmentManager());

        String activeRequestsTitle = context.getResources().getString(R.string.requests_active);
        Bundle activeRequestsArguments = new Bundle();

        activeRequestsArguments.putBoolean(Constants.BUNDLE_SHOW_FLOATING_BUTTON, true);
        activeRequestsArguments.putInt(Constants.REQUESTS_STATUS, Constants.ACTIVE_REQUESTS);
        activeRequestsArguments.putInt(Constants.OBJECT_ID, objectId);

        RequestsFragment activeRequestsFragment = RequestsFragment.getInstance(activeRequestsArguments, activeRequestsTitle);

        String completedRequestsTitle = context.getResources().getString(R.string.requests_completed);
        Bundle completedRequestsArguments = new Bundle();

        completedRequestsArguments.putBoolean(Constants.BUNDLE_SHOW_FLOATING_BUTTON, false);
        completedRequestsArguments.putInt(Constants.REQUESTS_STATUS, Constants.COMPLETED_REQUESTS);
        completedRequestsArguments.putInt(Constants.OBJECT_ID, objectId);

        RequestsFragment completedRequestsFragment = RequestsFragment.getInstance(completedRequestsArguments, activeRequestsTitle);

        adapter.addPageFragment(activeRequestsFragment, activeRequestsTitle);
        adapter.addPageFragment(completedRequestsFragment, completedRequestsTitle);

        viewPagerAdapter.set(adapter);
    }

    public void update() {
        viewPagerAdapter.notifyChange();
    }

    public void showInfoMessage() {
        DialogHelper.createInfoDialog(context, R.string.text_new_request, R.string.request_sent_info);
    }

    public void setExtras(Bundle extras) {
        objectId = extras.getInt(Constants.OBJECT_ID, 0);
    }
}
