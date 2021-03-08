package ru.true_ip.trueip.app.quizzes_screen.quiz_screen;

import android.app.Activity;
import android.os.Bundle;

import ru.true_ip.trueip.base.BaseRouter;

/**
 * Created by ektitarev on 10.01.2018.
 */

public class QuizRouter extends BaseRouter {

    private Activity activity;

    public QuizRouter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void moveBackward() {
        super.moveBackward();
        activity.finish();
    }

    public void moveBackward(int resultCode) {
        super.moveBackward();
        activity.setResult(resultCode);
        activity.finish();
    }

    @Override
    public void moveTo(Destination dest, Bundle bundle) {

    }
}
