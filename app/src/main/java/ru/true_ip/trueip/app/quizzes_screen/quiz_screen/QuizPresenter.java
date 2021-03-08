package ru.true_ip.trueip.app.quizzes_screen.quiz_screen;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import org.bytedeco.javacpp.annotation.Const;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.quizzes_screen.any_answer.AnyAnswerActivity;
import ru.true_ip.trueip.app.quizzes_screen.quiz_screen.adapters.AnswersAdapter;
import ru.true_ip.trueip.app.quizzes_screen.quiz_screen.adapters.AnswersResultsAdapter;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.Callback;
import ru.true_ip.trueip.base.adapters.BindingRecyclerAdapter;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.models.requests.QuizzesAnswers;
import ru.true_ip.trueip.models.responses.ErrorApiResponse;
import ru.true_ip.trueip.models.responses.QuizzModel;
import ru.true_ip.trueip.models.responses.QuizzesResultsModel;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;
import ru.true_ip.trueip.utils.Utils;

/**
 * Created by ektitarev on 10.01.2018.
 */

public class QuizPresenter extends BasePresenter<QuizContract> {

    private final static String TAG = QuizPresenter.class.getSimpleName();
    private Context context;
    private int objectId = -1;
    private int currentSelectedIndex = -1;

    private ObjectDb objectDb;

    private int userId;
    private int votesCount;
    private QuizzModel quizzModel;
    private QuizzesResultsModel.QuizResult quizResult;

    private QuizzesAnswers answersObject = new QuizzesAnswers();

    private ArrayList<QuizzesAnswers.Answer> answers = new ArrayList<>();

    public ObservableField<LinearLayoutManager> linearLayoutManager = new ObservableField<>();

    public ObservableField<BindingRecyclerAdapter> answersAdapter = new ObservableField<>();

    public ObservableField<String> mainText = new ObservableField<>();
    public ObservableField<String> quizTitle = new ObservableField<>();
    public ObservableField<String> quizType = new ObservableField<>();

    public ObservableBoolean isQuizPassed = new ObservableBoolean(false);
    public ObservableBoolean quizTypeVisible = new ObservableBoolean(false);
    public ObservableBoolean isMultiple = new ObservableBoolean(false);
    public ObservableBoolean hasSelectedOption = new ObservableBoolean(false);

    //
    //support
    //
    public void setContext(Context context) { this.context = context; }
    public void setExtras(Bundle extras) {
        objectId = extras.getInt(Constants.OBJECT_ID, 0);
        quizzModel = extras.getParcelable(Constants.QUIZ_OBJECT);
        int quizStatus = extras.getInt(Constants.QUIZZES_TYPE, Constants.PASSED_QUIZZES);
        isQuizPassed.set(quizStatus == Constants.PASSED_QUIZZES);
    }

    public void setLayoutManager() {
        linearLayoutManager.set(new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
    }

    public void onHomeClick (View v) {
        QuizContract contract = getContract();
        if (contract != null) {
            contract.getRouter().moveBackward();
        }
    }

    private void setQuizData() {
        if (quizzModel != null) {
            mainText.set(quizzModel.getQuestion());
            quizTitle.set(quizzModel.getTitle());
            isMultiple.set(quizzModel.isIs_multiple() == 1);

            if (!isQuizPassed.get()) {
                AnswersAdapter adapter = new AnswersAdapter(new ArrayList<>());
                adapter.addOnItemClickListener(this::handleAnswerClick);

                List<QuizzModel.AnswerModel> options = new ArrayList<>(quizzModel.getAnswers());

                if (quizzModel.isIs_free_answer() == 1) {
                    QuizzModel.AnswerModel freeAnswer = new QuizzModel.AnswerModel();
                    freeAnswer.setText(context.getResources().getString(R.string.text_free_answer_option));
                    freeAnswer.setId(-1);
                    options.add(freeAnswer);
                }

                adapter.setItems(options);

                answersAdapter.set(adapter);
            } else {
                if (quizResult != null) {
                    quizTypeVisible.set(true);
                    quizType.set(String.format(context.getString(R.string.text_open_questionarie), votesCount));

                    AnswersResultsAdapter adapter = new AnswersResultsAdapter(new ArrayList<>(), votesCount, quizzModel);

                    List<QuizzesResultsModel.QuizResult.QuizAnswer> options = new ArrayList<>(quizResult.getAnswers());

                    if (quizResult.getResults_free_count() > 0) {
                        QuizzesResultsModel.QuizResult.QuizAnswer freeAnswer = new QuizzesResultsModel.QuizResult.QuizAnswer();

                        freeAnswer.setText(context.getResources().getString(R.string.text_free_answer_option));
                        freeAnswer.setResults_count(quizResult.getResults_free_count());
                        freeAnswer.setId(-1);
                        options.add(freeAnswer);
                    }
                    adapter.setItems(options);

                    answersAdapter.set(adapter);
                }
            }
            markQuizAsRed();
        }
    }

    void getQuiz() {
        if(objectDb != null) {
            getQuizzesResults();
        } else {
            repositoryController.getObject(objectId).subscribe(objectDb1 -> {
                objectDb = objectDb1;
                userId = objectDb.getUser_id();
                getQuizzesResults();
            });
        }
    }

    private void getQuizzesResults() {
        apiController.getQuizzesResults(context, objectDb, String.valueOf(quizzModel.getId()), new Callback<Response<QuizzesResultsModel.QuizResult>>() {
            @Override
            public void onSuccess(Response<QuizzesResultsModel.QuizResult> response) {
                super.onSuccess(response);
                if(response.isSuccessful()) {
                    quizResult = response.body();
                    if (quizResult != null) {
                        votesCount = quizResult.getResults_free_count();
                        for(QuizzesResultsModel.QuizResult.QuizAnswer item : quizResult.getAnswers()) {
                            votesCount += item.getResults_count();
                        }
                    }
                }
                setQuizData();
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                setQuizData();
            }
        });
    }

    private void markQuizAsRed() {
        if(objectDb != null) {
            setQuizAsRed();
        } else {
            repositoryController.getObject(objectId).subscribe(objectDb1 -> {
                userId = objectDb.getUser_id();
                objectDb = objectDb1;
                setQuizAsRed();
            });
        }
    }

    private void setQuizAsRed() {
        if(quizzModel.isIs_viewed() != 1) {
            apiController.setQuizAsRead(context, objectDb, String.valueOf(quizzModel.getId()), new Callback<Response<QuizzModel>>() {
                @Override
                public void onSuccess(Response<QuizzModel> response) {
                    super.onSuccess(response);
                }

                @Override
                public void onError(String error) {
                    super.onError(error);
                }
            });
        }
    }

    private void handleAnswerClick(int pos, QuizzModel.AnswerModel item) {
        if (item.getId() != -1) {
            AnswersAdapter adapter = (AnswersAdapter) answersAdapter.get();

            if (isMultiple.get()) {
                item.setSelected(!item.isSelected());
                adapter.updateItem(pos, item);
                hasSelectedOption.set(checkSelectedItems(adapter.getAdapterItems()));
            } else {
                if (!item.isSelected()) {
                    if (currentSelectedIndex != -1) {
                        QuizzModel.AnswerModel previouslySelected = adapter.getAdapterItems().get(currentSelectedIndex);
                        previouslySelected.setSelected(false);
                        adapter.updateItem(currentSelectedIndex, previouslySelected);
                    }
                    item.setSelected(true);
                    adapter.updateItem(pos, item);

                    currentSelectedIndex = pos;
                    hasSelectedOption.set(true);
                }
            }
        } else {
            Bundle extras = new Bundle();
            extras.putInt(Constants.OBJECT_ID, objectId);
            extras.putParcelable(Constants.QUIZ_OBJECT, quizzModel);
            extras.putInt(Constants.VOTES_COUNT, votesCount);
            if (userId != 0) {
                extras.putInt(Constants.USER_ID, userId);
                AnyAnswerActivity.startForResult(context, extras);
            } else {
                repositoryController.getObject(objectId).subscribe(objectDb -> {
                    userId = objectDb.getUser_id();
                    extras.putInt(Constants.USER_ID, userId);
                    extras.putString(Constants.BASE_URL, objectDb.getServerUrl());
                    AnyAnswerActivity.startForResult(context, extras);
                });
            }
        }
    }

    private boolean checkSelectedItems (List<QuizzModel.AnswerModel> items) {
        for(QuizzModel.AnswerModel item : items) {
            if (item.isSelected()) {
                return true;
            }
        }
        return false;
    }

    private void postQuizzesAnswers(int pos, QuizzModel.AnswerModel item) {

        answersObject = new QuizzesAnswers();

        answers = new ArrayList<>();

        QuizzesAnswers.Answer answer = new QuizzesAnswers.Answer();

        answer.setId(item.getId());
        answer.setText(item.getText());

        answers.add(answer);

        answersObject.setAnswers(answers);

        postQuizzesAnswers();
    }

    private void postQuizzesAnswers() {
        apiController.postQuizzesAnswers(context, objectDb, String.valueOf(quizzModel.getId()), answersObject, new Callback<Response<Void>>() {
            @Override
            public void onSuccess(Response<Void> response) {
                super.onSuccess(response);
                if(response.isSuccessful()) {
                    QuizContract contract = getContract();
                    if (contract != null) {
                        contract.getRouter().moveBackward();
                    }
                } else {
                    ErrorApiResponse errorResponse = new ErrorApiResponse(response.errorBody());
                    String msg = Utils.getDetailedErrorMessage(errorResponse);

                    DialogHelper.createErrorDialog(context,
                            context.getResources().getString(R.string.text_error_dialog_title),
                            msg);
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

    public void onAnswerButtonClick(View v) {
        List<QuizzModel.AnswerModel> items = ((AnswersAdapter)answersAdapter.get()).getAdapterItems();
        if(isMultiple.get()) {
            for (QuizzModel.AnswerModel item : items) {
                if (item.isSelected()) {
                    QuizzesAnswers.Answer answer = new QuizzesAnswers.Answer();

                    answer.setId(item.getId());
                    answer.setText(item.getText());

                    answers.add(answer);
                }
            }
        } else {
            if (currentSelectedIndex != -1) {
                QuizzesAnswers.Answer answer = new QuizzesAnswers.Answer();

                answer.setId(items.get(currentSelectedIndex).getId());
                answer.setText(items.get(currentSelectedIndex).getText());

                answers.add(answer);
            }
        }

        if (!answers.isEmpty()) {
            answersObject.setAnswers(answers);

            if (objectDb != null) {
                postQuizzesAnswers();
            } else {
                repositoryController.getObject(objectId).subscribe(objectDb1 -> {
                    userId = objectDb.getUser_id();
                    objectDb = objectDb1;
                    postQuizzesAnswers();
                });
            }
        }
    }
}
