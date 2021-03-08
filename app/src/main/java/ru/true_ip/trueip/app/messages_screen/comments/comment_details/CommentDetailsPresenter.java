package ru.true_ip.trueip.app.messages_screen.comments.comment_details;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Response;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.messages_screen.comments.comment_details.adapters.DialogMessagesAdapter;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.Callback;
import ru.true_ip.trueip.db.entity.MessageDb;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.models.requests.MessageModel;
import ru.true_ip.trueip.models.responses.AnswerModel;
import ru.true_ip.trueip.models.responses.CommentModel;
import ru.true_ip.trueip.models.responses.ErrorApiResponse;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;
import ru.true_ip.trueip.utils.Utils;

/**
 *
 * Created by Andrey Filimonov on 11.01.2018.
 */

public class CommentDetailsPresenter extends BasePresenter<CommentDetailsContract> {

    private static final String TAG = CommentDetailsPresenter.class.getSimpleName();

    private final int MESSAGES_PAGE_SIZE = 100;
    private final int UPDATE_PERIOD = 10000;

    public ObservableField<DialogMessagesAdapter> dialogMessagesAdapter = new ObservableField<>(new DialogMessagesAdapter());
    public ObservableField<String> currentMessage = new ObservableField<>("");
    public ObservableBoolean textFieldVisibility = new ObservableBoolean(true);

    private Context context;
    private int objectId = -1;
    private ObjectDb objectDb;
    private CommentModel comment;
    private int actualNumberOfMessages;

    private boolean inProgress = false;
    private Timer updateTimer;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setExtras(Bundle extras) {
        objectId = extras.getInt(Constants.OBJECT_ID, -1);
        comment = (CommentModel) extras.get(Constants.COMMENT_OBJECT);
    }

    void showMessages() {
        if (comment != null) {
            if (comment.getAnswersCount() != null) { // new server
                repositoryController.getAllMessagesByCommentId(comment.getId()).subscribe(
                        this::handleCachedMessages,
                        throwable -> throwable.printStackTrace());
                setUpdateTimer();
            } else { // old server
                textFieldVisibility.set(false);
                DialogMessagesAdapter adapter = dialogMessagesAdapter.get();
                if (adapter != null) {
                    List<AnswerModel> answerModels = new ArrayList<>();

                    AnswerModel question = new AnswerModel();

                    question.setIsConsierge(0);
                    question.setText(comment.getText());

                    answerModels.add(question);

                    String answerText = comment.getAnswer();

                    if (answerText != null && !answerText.isEmpty()) {
                        AnswerModel answer = new AnswerModel();

                        answer.setIsConsierge(1);
                        answer.setText(answerText);

                        answerModels.add(answer);
                    }

                    adapter.setItems(answerModels);
                }
            }
        }
    }

    private void handleCachedMessages (List<MessageDb> messageDbs) {
        if (!messageDbs.isEmpty()) {
            actualNumberOfMessages = messageDbs.get(0).getAnswersCount();
            List<AnswerModel> answerModels = AnswerModel.messageDbsToAnswerModels(messageDbs);
            DialogMessagesAdapter adapter = dialogMessagesAdapter.get();
            if (adapter != null) {
                adapter.addAllItems(answerModels);
            }
            getNewMessages();
        } else {
            actualNumberOfMessages = comment.getAnswersCount();
            getLastMessages();
        }
    }

    private void getNewMessages() {
        loadMessages(actualNumberOfMessages, MESSAGES_PAGE_SIZE, true);
    }

    private void getLastMessages() {
        /*DialogMessagesAdapter adapter = dialogMessagesAdapter.get();
        int messagesCount = adapter != null ? adapter.getItemCount() : 0;
        int restMessagesCount = actualNumberOfMessages - messagesCount;

        if (restMessagesCount > 0) {
            int messagesOffset = actualNumberOfMessages - (messagesCount + (restMessagesCount > MESSAGES_PAGE_SIZE ? MESSAGES_PAGE_SIZE : restMessagesCount));
            int limit = restMessagesCount > MESSAGES_PAGE_SIZE ? MESSAGES_PAGE_SIZE : restMessagesCount;

            loadMessages(messagesOffset, limit, false);
        }*/
        loadMessages(0, actualNumberOfMessages, false);
    }

    private void loadMessages(int offset, int pageSize, boolean newMessages) {
        repositoryController.getObject(objectId).subscribe(objectDb1 -> {
            objectDb = objectDb1;

            apiController.getCommentAnswers(context, objectDb1, String.valueOf(comment.getId()), offset, pageSize, new Callback<Response<List<AnswerModel>>>() {
                @Override
                public void onSuccess(Response<List<AnswerModel>> response) {
                    super.onSuccess(response);
                    if (response.isSuccessful()) {
                        handleLoadedMessages(response.body(), newMessages);
                    } else {
                        showErrorMessage(response);
                    }
                    inProgress = false;
                }

                @Override
                public void onError(String error) {
                    super.onError(error);
                    DialogHelper.createErrorDialog(context,
                            context.getResources().getString(R.string.text_error_dialog_title),
                            context.getResources().getString(R.string.text_error_default_message));
                    inProgress = false;
                }
            });
        });
    }

    private void handleLoadedMessages(List<AnswerModel> answersList, boolean newMessages) {
        if (answersList != null && !answersList.isEmpty()) {
            DialogMessagesAdapter adapter = dialogMessagesAdapter.get();
            if (newMessages) {
                actualNumberOfMessages += answersList.size();
            }
            if (adapter != null) {
                int previousCount = adapter.getItemCount();

                if (newMessages) {
                    adapter.addAllItems(answersList);
                } else {
                    adapter.insertAllItemsToBegin(answersList);
                }
                //adapter.sortMessages(getComparator());
                saveMessagesToCache(answersList);
                if (getLastVisibleMessagePosition() == previousCount - 1) {
                    scrollDialog(adapter.getItemCount() - 1);
                }
            }
        }
    }

    private Comparator<AnswerModel> getComparator() {
        return (o1, o2) -> {
            int result = 0;

            try {
                String stringDate1 = Utils.dateToTimestamp(o1.getCreatedAt(), "yyyy-MM-dd HH:mm:ss");
                String stringDate2 = Utils.dateToTimestamp(o2.getCreatedAt(), "yyyy-MM-dd HH:mm:ss");
                return stringDate1.compareTo(stringDate2);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return result;
        };
    }

    private void saveMessagesToCache(List<AnswerModel> answerModelList) {
        List<MessageDb> messagesToSave = MessageDb.answerModelsToModelDbs(answerModelList, actualNumberOfMessages);
        repositoryController.insertMessages(messagesToSave).subscribe(
                () -> {
                    updateAnswersCount();
                },
                error -> error.printStackTrace());
    }

    private void updateAnswersCount() {
        repositoryController.updateAnswersCount(comment.getId(), actualNumberOfMessages).subscribe(
                () -> {},
                error -> error.printStackTrace());
    }

    /*private void addCommentAndAnswer(List<AnswerModel> answersList) {
        AnswerModel answerModel = new AnswerModel();

        answerModel.setApartment(objectDb.getFlat_number());
        answerModel.setIsConsierge(0);
        answerModel.setCreatedAt(comment.getCreatedAt());
        answerModel.setUpdatedAt(comment.getUpdatedAt());
        answerModel.setUserId(comment.getUserId());
        answerModel.setIsViewed(comment.getIsViewed());
        answerModel.setText(comment.getText());
        answerModel.setReviewId(comment.getId());

        answersList.add(answerModel);

        answerModel = new AnswerModel();

        answerModel.setApartment(objectDb.getFlat_number());
        answerModel.setIsConsierge(1);
        answerModel.setCreatedAt(comment.getUpdatedAtAnswer());
        answerModel.setUpdatedAt(comment.getUpdatedAtAnswer());
        answerModel.setUserId(comment.getUserId());
        answerModel.setIsViewed(comment.getIsViewed());
        answerModel.setText(comment.getAnswer());
        answerModel.setReviewId(comment.getId());

        answersList.add(answerModel);
    }*/

    public void onHomeClick(View v) {
        CommentDetailsContract contract = getContract();
        if (contract != null) {
            contract.getRouter().moveBackward();
            cancelTimer();
        }
    }


    private void showErrorMessage(Response response) {
        ErrorApiResponse error = new ErrorApiResponse(response.errorBody());
        String msg = Utils.getDetailedErrorMessage(error);

        if (!msg.isEmpty()) {
            DialogHelper.createErrorDialog(context,
                    context.getResources().getString(R.string.text_error_dialog_title),
                    msg);
        } else {
            DialogHelper.createErrorDialog(context,
                    context.getResources().getString(R.string.text_error_dialog_title),
                    context.getResources().getString(R.string.text_error_default_message));
        }
    }

    public void onSendMessage(View v) {
        prepareMessage(currentMessage.get());
    }

    private void prepareMessage(String rawMessage) {
        if (rawMessage != null && !rawMessage.isEmpty()) {
            String message = rawMessage.trim();
            if (!message.isEmpty()) {
                MessageModel messageModel = new MessageModel();

                messageModel.setText(message);
                messageModel.setReviewId(comment.getId());

                sendMessage(messageModel);
            }
        }
    }

    private void sendMessage(MessageModel messageModel) {
        if (objectDb != null) {
            postMessage(messageModel);
        } else {
            repositoryController.getObject(objectId).subscribe(objectDb -> {
                this.objectDb = objectDb;
                postMessage(messageModel);
            });
        }
    }

    private void postMessage(MessageModel messageModel) {
        apiController.postFeedback(context, objectDb, messageModel, new Callback<Response<Void>>() {
            @Override
            public void onSuccess(Response<Void> response) {
                super.onSuccess(response);
                if (response.isSuccessful()) {
                    /*actualNumberOfMessages++;
                    addMessageToDialog(messageModel.getText());*/
                    getNewMessages();
                    currentMessage.set("");
                } else {
                    showErrorMessage(response);
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                DialogHelper.createErrorDialog(context,
                        context.getResources().getString(R.string.text_error_dialog_title),
                        context.getResources().getString(R.string.text_error_default_message));
            }
        });
    }

    private void addMessageToDialog(String text) {
        DialogMessagesAdapter adapter = dialogMessagesAdapter.get();

        AnswerModel answerModel = new AnswerModel();

        answerModel.setText(text);
        answerModel.setIsConsierge(0);
        answerModel.setReviewId(comment.getId());
        answerModel.setIsViewed(0);
        answerModel.setApartment(objectDb.getFlat_number());
        answerModel.setUserId(objectDb.getUser_id());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateString = format.format(new Date());

        answerModel.setCreatedAt(dateString);
        answerModel.setUpdatedAt(dateString);

        if (adapter != null) {
            adapter.addItem(answerModel);
            saveMessageToCache(answerModel);

            scrollDialog(adapter.getItemCount() - 1);
        }
    }

    private void saveMessageToCache(AnswerModel answerModel) {
        MessageDb messageDb = MessageDb.answerModelToModelDb(answerModel, actualNumberOfMessages);
        repositoryController.insertMessage(messageDb).subscribe(
                () -> {
                    updateAnswersCount();
                },
                error -> error.printStackTrace());
    }

    private void scrollDialog (int pos) {
        CommentDetailsContract contract = getContract();
        if (contract != null) {
            contract.scrollDialog(pos);
        }
    }

    private int getLastVisibleMessagePosition() {
        CommentDetailsContract contract = getContract();
        if (contract != null) {
            return contract.getLastVisibleMessagePosition();
        }

        return -1;
    }

    public void onScrolledToTop() {
        /*if (!inProgress) {
            inProgress = true;
            getLastMessages();
        }*/
    }

    private void setUpdateTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                getNewMessages();
            }
        };

        updateTimer = new Timer();
        updateTimer.schedule(task, UPDATE_PERIOD, UPDATE_PERIOD);
    }

    public void cancelTimer() {
        if (updateTimer != null) {
            updateTimer.cancel();
        }
    }
}
