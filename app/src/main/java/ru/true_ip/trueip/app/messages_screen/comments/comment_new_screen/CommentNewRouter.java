package ru.true_ip.trueip.app.messages_screen.comments.comment_new_screen;

import android.app.Activity;
import android.os.Bundle;

import ru.true_ip.trueip.base.BaseRouter;

/**
 * Created by ektitarev on 12.01.2018.
 */

public class CommentNewRouter extends BaseRouter {

    Activity activity;

    public CommentNewRouter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void moveBackward() {
        super.moveBackward();
        activity.finish();
    }

    @Override
    public void moveTo(Destination dest, Bundle bundle) {

    }
}
