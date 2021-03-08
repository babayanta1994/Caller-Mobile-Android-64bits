package ru.true_ip.trueip.app.quizzes_screen.quizzes_fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseFragment;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.FragmentQuizzesBinding;

/**
 * Created by ektitarev on 27.12.2017.
 */

public class QuizzesListFragment extends BaseFragment<QuizzesListContract, QuizzesListPresenter, FragmentQuizzesBinding> implements QuizzesListContract {

    private String title;

    public static QuizzesListFragment getInstance(Bundle bundle, String title) {
        QuizzesListFragment fragment = new QuizzesListFragment();
        fragment.title = title;
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public FragmentQuizzesBinding initBinding(LayoutInflater layoutInflater) {
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_quizzes, null, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.setContext(getActivity());
        presenter.setExtras(getArguments());
        presenter.getQuizzes();
    }

    @Override
    public QuizzesListContract getContract() {
        return this;
    }

    @Override
    public QuizzesListPresenter createPresenter() {
        return new QuizzesListPresenter();
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
