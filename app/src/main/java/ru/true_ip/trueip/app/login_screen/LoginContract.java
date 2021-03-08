package ru.true_ip.trueip.app.login_screen;

import ru.true_ip.trueip.base.BaseContract;

/**
 * Created by user on 26-Oct-17.
 */

public interface LoginContract extends BaseContract {

    void showFlatNumberError(String message);
    void showCodeError(String message);
    void dismissFlatNumberError();
    void dismissCodeError();
}
