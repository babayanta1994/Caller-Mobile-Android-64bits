package ru.true_ip.trueip.app.quizzes_screen.quizzes_fragment;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Response;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.quizzes_screen.quiz_screen.QuizActivity;
import ru.true_ip.trueip.app.quizzes_screen.quizzes_fragment.adapters.QuizzesAdapter;
import ru.true_ip.trueip.base.BaseContract;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.Callback;
import ru.true_ip.trueip.models.responses.ErrorApiResponse;
import ru.true_ip.trueip.models.responses.QuizzModel;
import ru.true_ip.trueip.service.service.SipServiceCommands;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;


/**
 * Created by ektitarev on 27.12.2017.
 */

public class QuizzesListPresenter extends BasePresenter {

    public ObservableBoolean isLoading = new ObservableBoolean(false);
    public ObservableField<QuizzesAdapter> quizzesAdapter = new ObservableField<>(new QuizzesAdapter(R.layout.item_quiz, new ArrayList<>()));

    private Context context;
    private int objectId;
    private int quizzesType;

    @Override
    public void attachToView(BaseContract contract) {
        super.attachToView(contract);
    }

    public void setContext (Context context) {
        this.context = context;
    }

    public void getQuizzes() {
        if (objectId != 0) {
            repositoryController.getObject(objectId).subscribe(objectDb ->
                    apiController.getQuizzesList(context, objectDb, new Callback<Response<List<QuizzModel>>>() {
                        @Override
                        public void onSuccess(Response<List<QuizzModel>> response) {
                            super.onSuccess(response);
                            int status = response.code();
                            if (status == 200) {
                                List<QuizzModel> items = response.body();
                                if (items != null) {
                                    quizzesAdapter.get().setItems(filterItems(items));
                                }
                                quizzesAdapter.get().addOnItemClickListener(((position, item) -> {
                                    Bundle extras = new Bundle();
                                    extras.putInt(Constants.OBJECT_ID, objectId);
                                    extras.putParcelable(Constants.QUIZ_OBJECT, item);
                                    extras.putString(Constants.BASE_URL, objectDb.getServerUrl());
                                    if (item.getSelected_free() != null || item.getSelected_answer() != null && item.getSelected_answer().size() > 0) {
                                        extras.putInt(Constants.QUIZZES_TYPE, Constants.PASSED_QUIZZES);
                                        QuizActivity.start(context, extras);
                                    } else {
                                        extras.putInt(Constants.QUIZZES_TYPE, Constants.NOT_PASSED_QUIZZES);
                                        QuizActivity.startForResult(context, extras);
                                    }
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
                    })
                );
        }

    }

    private List<QuizzModel> filterItems(List<QuizzModel> sourceItems) {
        List<QuizzModel> items;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            switch(quizzesType) {
                case Constants.PASSED_QUIZZES:
                    items = sourceItems.stream()
                            .filter(item -> item.getSelected_free() != null || item.getSelected_answer() != null && item.getSelected_answer().size() > 0)
                            .collect(Collectors.toList());
                    break;
                case Constants.NOT_PASSED_QUIZZES:
                    items = sourceItems.stream()
                            .filter(item -> item.getSelected_free() == null && (item.getSelected_answer() == null || item.getSelected_answer().size() == 0))
                            .collect(Collectors.toList());
                    break;
                default:
                    items = sourceItems;
            }
        } else {
            items = new ArrayList<>();
            switch(quizzesType) {
                case Constants.PASSED_QUIZZES:
                    for (QuizzModel item : sourceItems) {
                        if (item.getSelected_free() != null || item.getSelected_answer() != null && item.getSelected_answer().size() > 0) {
                            items.add(item);
                        }
                    }
                    break;
                case Constants.NOT_PASSED_QUIZZES:
                    for (QuizzModel item : sourceItems) {
                        if (item.getSelected_free() == null && (item.getSelected_answer() == null || item.getSelected_answer().size() == 0)) {
                            items.add(item);
                        }
                    }
                    break;
                default:
                    items = sourceItems;
            }
        }

        return items;
    }

    public void setExtras(Bundle extras) {
        objectId = extras.getInt(Constants.OBJECT_ID, 0);
        quizzesType = extras.getInt(Constants.QUIZZES_TYPE, 0);
    }

    public void onRefreshSwiped() {
        getQuizzes();
    }

}
