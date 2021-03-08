package ru.true_ip.trueip.base;

import io.reactivex.disposables.Disposable;

/**
 * Created by user on 13-Sep-17.
 */

public class Callback<T> {

    public void onStart(Disposable d) {}

    public void onSuccess(T object) {}

    public void onError(String error) {}

    public void onComplete() {}
}
