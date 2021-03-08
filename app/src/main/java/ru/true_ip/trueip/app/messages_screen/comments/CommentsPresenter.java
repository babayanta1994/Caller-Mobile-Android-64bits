package ru.true_ip.trueip.app.messages_screen.comments;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.messages_screen.comments.comment_details.CommentDetailsActivity;
import ru.true_ip.trueip.app.messages_screen.comments.comment_new_screen.CommentNewActivity;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.Callback;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.models.responses.CommentModel;
import ru.true_ip.trueip.models.responses.ErrorApiResponse;
import ru.true_ip.trueip.service.service.SipServiceCommands;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;

/**
 * 
 * Created by Andrey Filimonov on 11.01.2018.
 */

public class CommentsPresenter extends BasePresenter<CommentsContract> {
    public ObservableBoolean isLoading = new ObservableBoolean(false);
    private Context context;
    private int objectId = -1;
    public ObservableField<CommentsAdapter> commentsAdapter = new ObservableField<>(new CommentsAdapter(R.layout.item_comment, new ArrayList<>()));

    public void setContext(Context context) {
        this.context = context;
    }

    public void setExtras(Bundle extras) {
        objectId = extras.getInt(Constants.OBJECT_ID, -1);
    }

    public void setComments() {
        repositoryController.getObject(objectId).subscribe(objectDb ->
                apiController.getComments(context, objectDb, new Callback<Response<ArrayList<CommentModel>>>() {
                    @Override
                    public void onSuccess(Response<ArrayList<CommentModel>> response) {
                        super.onSuccess(response);
                        int status = response.code();
                        if (status == 200) {
                            List<CommentModel> items = response.body();
                            if (items != null) {
                                commentsAdapter.get().setItems(items);
                            }
                            commentsAdapter.get().addOnItemClickListener(((position, item) -> {
                                Bundle extras = new Bundle();
                                extras.putInt(Constants.OBJECT_ID, objectId);
                                extras.putParcelable(Constants.COMMENT_OBJECT, item);
                                CommentDetailsActivity.start(context, extras);
                            }));
                        } else {
                            ErrorApiResponse error = new ErrorApiResponse(response.errorBody());
                            DialogHelper.createErrorDialog(context,
                                    context.getResources().getString(R.string.text_error_dialog_title),
                                    error.getError());

                            if (status == 401 || status == 403) {
                                SipServiceCommands.removeAccount(context, objectDb.getIdUri());
                            }
                        }
                        isLoading.notifyChange();
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        isLoading.notifyChange();
                        DialogHelper.createErrorDialog(context,
                                context.getResources().getString(R.string.text_error_dialog_title),
                                context.getResources().getString(R.string.text_error_default_message));
                    }
                }));
    }

    public void onHomeClick(View v) {
        CommentsContract contract = getContract();
        if (contract != null) {
            contract.getRouter().moveBackward();
        }
    }

    public void onRefreshSwiped() {
        setComments();
    }

    public void onClickAdd(View v) {
        repositoryController.getObject(objectId).subscribe(objectDb -> {
            if (mayUseApi(objectDb)) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.BUNDLE_INT_KEY, objectId);
                CommentNewActivity.startForResult(context, bundle);
            }
        });
    }

    private boolean mayUseApi(ObjectDb objectDb) {
        return (objectDb.getIsServerActive() != null && objectDb.getIsServerActive() == 1 && !objectDb.isBlocked()) ||
                (objectDb.getIsServerActive() == null && !objectDb.isBlocked());
    }
}
