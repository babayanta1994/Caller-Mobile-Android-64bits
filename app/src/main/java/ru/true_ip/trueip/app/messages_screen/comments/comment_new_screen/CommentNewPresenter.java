package ru.true_ip.trueip.app.messages_screen.comments.comment_new_screen;

import android.content.Context;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;

import retrofit2.Response;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.Callback;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.models.responses.ErrorApiResponse;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;

/**
 * Created by ektitarev on 12.01.2018.
 */

public class CommentNewPresenter extends BasePresenter<CommentNewContract> {

    private static final String LOGCAT = CommentNewPresenter.class.getSimpleName();

    private Context context;
    private int objectId;

    private String userToken;
    private String baseUrl;

    private ObjectDb objectDb;

    private boolean isInProgress;

    public ObservableField<String> commentText = new ObservableField<>("");

    public void setContext(Context context) { this.context = context; }

    public void setExtras(Bundle extras) {
        objectId = extras.getInt(Constants.BUNDLE_INT_KEY);
    }

    public void onSendClick(View v) {
        if (objectId != 0) {
            if (objectDb != null) {
                postFeedback();
            } else {
                repositoryController.getObject(objectId).subscribe(objectDb1 -> {
                    objectDb = objectDb1;
                    postFeedback();
                });
            }
        }
    }

    private void postFeedback() {
        String feedbackText = commentText.get();
        if (feedbackText != null && !feedbackText.isEmpty()) {
            if (!isInProgress) {
                isInProgress = true;
                apiController.postFeedback(context, objectDb, feedbackText, new Callback<Response<Void>>() {
                    @Override
                    public void onSuccess(Response<Void> response) {
                        super.onSuccess(response);
                        if (response.isSuccessful()) {
                            CommentNewContract contract = getContract();
                            if (contract != null) {
                                contract.getRouter().moveBackward();
                            }
                        } else {
                            ErrorApiResponse error = new ErrorApiResponse(response.errorBody());
                            DialogHelper.createErrorDialog(context,
                                    context.getResources().getString(R.string.text_error_dialog_title),
                                    error.getError());
                            isInProgress = false;
                        }
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        DialogHelper.createErrorDialog(context,
                                context.getResources().getString(R.string.text_error_dialog_title),
                                context.getResources().getString(R.string.text_error_default_message));
                        isInProgress = false;
                    }
                });
            }
        }
    }
}
