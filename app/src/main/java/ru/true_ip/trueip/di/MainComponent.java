package ru.true_ip.trueip.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.true_ip.trueip.app.login_screen.LoginPresenter;
import ru.true_ip.trueip.base.BaseContract;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.receivers.BaseReceiver;
import ru.true_ip.trueip.di.modules.ApiModule;
import ru.true_ip.trueip.di.modules.RepositoryModule;
import ru.true_ip.trueip.utils.CustomGlideModule;


@Singleton
@Component(modules = {RepositoryModule.class, ApiModule.class})
public interface MainComponent {

    void inject(BasePresenter<BaseContract> cBasePresenter);

    void inject(LoginPresenter loginPresenter);

    void inject(BaseReceiver baseReceiver);

    void inject(CustomGlideModule customGlideModule);
}
