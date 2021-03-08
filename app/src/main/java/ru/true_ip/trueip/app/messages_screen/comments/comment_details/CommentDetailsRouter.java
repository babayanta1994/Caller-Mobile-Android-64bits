package ru.true_ip.trueip.app.messages_screen.comments.comment_details;

import android.app.Activity;
import android.os.Bundle;

import ru.true_ip.trueip.base.BaseRouter;

/**
 *
 * Created by Andrey Filimonov on 11.01.2018.
 */

public class CommentDetailsRouter extends BaseRouter {

    private Activity activity;

    public CommentDetailsRouter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void moveBackward() {
        activity.finish();
    }

    @Override
    public void moveTo(Destination dest, Bundle bundle) {
        switch (dest) {
            case MESSAGES_SCREEN:
                CommentDetailsActivity.start(activity, bundle);
                break;
        }
    }
}
