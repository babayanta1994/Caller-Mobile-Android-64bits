package ru.true_ip.trueip.base;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.true_ip.trueip.BR;


/**
 * Created by user on 11-Sep-17.
 *
 */

public abstract class BaseFragment<C extends BaseContract, P extends BasePresenter, B extends ViewDataBinding> extends Fragment {

    protected P presenter = null;
    protected B binding = null;
    protected BaseRouter router = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.router = createRouter();
        this.binding = initBinding(inflater);
        this.presenter = createPresenter();
        this.binding.setVariable(BR.presenter, this.presenter);
        return binding.getRoot();
    }

    public abstract B initBinding(LayoutInflater layoutInflater);

    public abstract C getContract();

    public abstract P createPresenter();

    public abstract BaseRouter createRouter();

    public abstract String getTitle(Context context);

    @Override
    public void onStart() {
        super.onStart();
        presenter.attachToView(getContract());
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.presenter = null;
    }
}
