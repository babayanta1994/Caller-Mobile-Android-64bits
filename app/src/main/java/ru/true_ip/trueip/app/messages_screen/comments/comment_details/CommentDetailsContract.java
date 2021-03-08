package ru.true_ip.trueip.app.messages_screen.comments.comment_details;

import ru.true_ip.trueip.base.BaseContract;

/**
 *
 * Created by Andrey Filimonov on 11.01.2018.
 */

public interface CommentDetailsContract extends BaseContract {
    void scrollDialog(int position);

    int getLastVisibleMessagePosition();
}
