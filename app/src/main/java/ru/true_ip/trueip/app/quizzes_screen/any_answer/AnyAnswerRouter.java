package ru.true_ip.trueip.app.quizzes_screen.any_answer;

import android.app.Activity;
import android.os.Bundle;

import ru.true_ip.trueip.base.BaseRouter;

/**
 *
 * Created by Andrey Filimonov on 10.01.2018.
 */

public class AnyAnswerRouter extends BaseRouter {

    private Activity activity;

    public AnyAnswerRouter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void moveBackward() {
        super.moveBackward();
        activity.finish();
    }

    public void moveBackward(int resultCode) {
        activity.setResult(resultCode);
        activity.finish();
    }

    @Override
    public void moveTo(Destination dest, Bundle bundle) {

    }
}
