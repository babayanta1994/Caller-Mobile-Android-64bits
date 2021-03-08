package ru.true_ip.trueip.app.quizzes_screen.any_answer;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;

import retrofit2.Response;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.Callback;
import ru.true_ip.trueip.models.requests.QuizzesAnswers;
import ru.true_ip.trueip.models.responses.QuizzModel;
import ru.true_ip.trueip.utils.Constants;


/**
 * Created by Andrey Filimonov on 10.01.2018.
 */

public class AnyAnswerPresenter extends BasePresenter<AnyAnswerContract> {
    private final static String TAG = AnyAnswerPresenter.class.getSimpleName();
    public ObservableField<String> questionBody = new ObservableField<>("");
    public ObservableField<String> questionTitle = new ObservableField<>("");
    public ObservableField<String> questionAnswer = new ObservableField<>("");
    public ObservableField<String> questionCounter = new ObservableField<>("");
    public ObservableBoolean isAnswerAccepted = new ObservableBoolean(false);

    private Context context;
    private String quize_id = "";
    private int votesCount = 10;
    private int objectId;

    public void setContext(Context context) {
        this.context = context;
    }

    public void onHomeClick(View v) {
        AnyAnswerContract contract = getContract();
        if (contract != null) {
            contract.getRouter().moveBackward();
        }
    }

    public void setExtras(Bundle extras) {
        //Here we will receive Parcelable Question Object
        QuizzModel model = extras.getParcelable(Constants.QUIZ_OBJECT);
        if (model != null) {
            objectId = extras.getInt(Constants.OBJECT_ID);
            quize_id = String.valueOf(model.getId());
            votesCount = extras.getInt(Constants.VOTES_COUNT);

            questionBody.set(model.getQuestion());
            questionTitle.set(model.getTitle());
            questionCounter.set(String.format(context.getString(R.string.text_open_questionarie), votesCount));
            questionAnswer.set("");
        }
    }

    public void OnAnswerClick(View v) {
        if (isAnswerAccepted.get()) {
            ((AnyAnswerRouter)getContract().getRouter()).moveBackward(2);
        } else {
            if (!questionAnswer.get().isEmpty()) {
                QuizzesAnswers answers = new QuizzesAnswers();
                answers.setFree_answer(questionAnswer.get());

                repositoryController.getObject(objectId)
                        .subscribe(objectDb -> {
                            apiController.postQuizzesAnswers(context, objectDb, quize_id, answers, new Callback<Response<Void>>() {
                                @Override
                                public void onSuccess(Response<Void> response) {
                                    super.onSuccess(response);
                                    if (response.isSuccessful()) {
                                        isAnswerAccepted.set(true);
                                    }
                                }

                                @Override
                                public void onError(String error) {
                                    super.onError(error);
                                }
                            });

                        });
            }
        }
    }
}

