package ru.true_ip.trueip.app.quizzes_screen;

import android.content.Context;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.quizzes_screen.quizzes_fragment.QuizzesListFragment;
import ru.true_ip.trueip.app.requests_screen.adapters.ViewPagerAdapter;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.utils.Constants;

/**
 * Created by ektitarev on 27.12.2017.
 */

public class QuizzesPresenter extends BasePresenter<QuizzesContract> {

    public ObservableField<ViewPagerAdapter> viewPagerAdapter = new ObservableField<>();

    private Context context;
    private int objectId;

    public void setContext(Context context) { this.context = context; }

    public void onHomeClick(View v) {
        QuizzesContract contract = getContract();
        if (contract != null) {
            contract.getRouter().moveBackward();
        }
    }

    public void createViewPager() {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(((FragmentActivity)context).getSupportFragmentManager());

        String notPassedQuizzesTitle = context.getResources().getString(R.string.quizzes_not_passed);

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.OBJECT_ID, objectId);
        bundle.putInt(Constants.QUIZZES_TYPE, Constants.NOT_PASSED_QUIZZES);

        Fragment notPassedQuizzesFragment = QuizzesListFragment.getInstance(bundle, notPassedQuizzesTitle);

        String passedQuizzesTitle = context.getResources().getString(R.string.quizzes_passed);

        bundle = new Bundle();
        bundle.putInt(Constants.OBJECT_ID, objectId);
        bundle.putInt(Constants.QUIZZES_TYPE, Constants.PASSED_QUIZZES);

        Fragment passedQuizzesFragment = QuizzesListFragment.getInstance(bundle, passedQuizzesTitle);

        pagerAdapter.addPageFragment(notPassedQuizzesFragment, notPassedQuizzesTitle);
        pagerAdapter.addPageFragment(passedQuizzesFragment, passedQuizzesTitle);

        viewPagerAdapter.set(pagerAdapter);
    }

    public void updateQuizzesList() {
        viewPagerAdapter.notifyChange();
    }

    public void setExtras(Bundle extras) {
        objectId = extras.getInt(Constants.OBJECT_ID, 0);
    }
}
