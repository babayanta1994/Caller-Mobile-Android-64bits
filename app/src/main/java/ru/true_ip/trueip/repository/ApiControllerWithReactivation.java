package ru.true_ip.trueip.repository;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.api.HLMApi;
import ru.true_ip.trueip.app.App;
import ru.true_ip.trueip.base.Callback;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.models.PhotoModel;
import ru.true_ip.trueip.models.requests.MessageModel;
import ru.true_ip.trueip.models.requests.QuizzesAnswers;
import ru.true_ip.trueip.models.responses.ActivationsCountModel;
import ru.true_ip.trueip.models.responses.AdvertModel;
import ru.true_ip.trueip.models.responses.AnswerModel;
import ru.true_ip.trueip.models.responses.CameraModel;
import ru.true_ip.trueip.models.responses.CameraShortModel;
import ru.true_ip.trueip.models.responses.ClaimModel;
import ru.true_ip.trueip.models.responses.ClaimResponse;
import ru.true_ip.trueip.models.responses.CommentModel;
import ru.true_ip.trueip.models.responses.Device;
import ru.true_ip.trueip.models.responses.ErrorApiResponse;
import ru.true_ip.trueip.models.responses.LangModel;
import ru.true_ip.trueip.models.responses.NotificationModel;
import ru.true_ip.trueip.models.responses.PanelShortModel;
import ru.true_ip.trueip.models.responses.QuizzModel;
import ru.true_ip.trueip.models.responses.QuizzesResultsModel;
import ru.true_ip.trueip.models.responses.ServerStatusModel;
import ru.true_ip.trueip.models.responses.TypeModel;
import ru.true_ip.trueip.models.responses.UnreadMessagesModel;
import ru.true_ip.trueip.models.responses.UserModel;
import ru.true_ip.trueip.service.data.BroadcastEventEmitter;
import ru.true_ip.trueip.service.data.SipAccountData;
import ru.true_ip.trueip.service.service.Logger;
import ru.true_ip.trueip.service.service.SipServiceCommands;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;
import ru.true_ip.trueip.utils.ReactivationCodeDialog;
import ru.true_ip.trueip.utils.Utils;

/**
 * Created by ektitarev on 04.09.2018.
 *
 */


@SuppressWarnings(value = "CheckResult")
public class ApiControllerWithReactivation extends ApiController {

    private static final String TAG = ApiControllerWithReactivation.class.getSimpleName();

    private Map<Integer,Queue<RecallAction>> requestsQueue;
    private final Object lockObject = new Object();

    private interface RecallAction {
        void recall(ObjectDb token);
    }

    private static abstract class OnErrorDefaultCallback<T> implements Observer<T> {
        private Callback<T> callback;

        OnErrorDefaultCallback(@NonNull Callback<T> callback) {
            this.callback = callback;
        }

        @Override
        public void onSubscribe(Disposable d) {
            callback.onStart(d);
        }

        @Override
        public void onError(Throwable e) {
            callback.onError(e.getMessage());
        }

        @Override
        public void onComplete() {
            callback.onComplete();
        }
    }

    protected RepositoryController repositoryController;

    public ApiControllerWithReactivation(HLMApi hlmApi, RepositoryController repositoryController) {
        super(hlmApi);

        this.repositoryController = repositoryController;
        requestsQueue = new LinkedHashMap<>();
    }

    public void getCameras(Context context,
                           ObjectDb objectDb,
                              Callback<Response<List<CameraModel>>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    getCameras("Bearer " + userDb.getToken(), objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<List<CameraModel>>>(callback) {
                        @Override
                        public void onNext(Response<List<CameraModel>> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.getCameras(context, objectDb1, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void getCamerasShort(Context context,
                                ObjectDb objectDb,
                                   Callback<Response<ArrayList<CameraShortModel>>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    getCamerasShort("Bearer " + userDb.getToken(), objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<ArrayList<CameraShortModel>>>(callback) {
                        @Override
                        public void onNext(Response<ArrayList<CameraShortModel>> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.getCamerasShort(context, objectDb1, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void getServerStatus(Context context,
                                ObjectDb objectDb,
                                Callback<Response<ServerStatusModel>> callback) {
        repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                getServerStatus("Bearer " + userDb.getToken(), new OnErrorDefaultCallback<Response<ServerStatusModel>>(callback) {
                    @Override
                    public void onNext(Response<ServerStatusModel> response) {
                        if (!response.isSuccessful() && response.code() == 401) {
                            tryToReactivate(context, objectDb, callback, objectDb1 ->
                                    ApiControllerWithReactivation.this.getServerStatus(context, objectDb1, callback));
                        } else {
                            callback.onSuccess(response);
                        }

                    }
                }));
    }

    public void getPanelsShort(Context context,
                               ObjectDb objectDb,
                                  Callback<Response<ArrayList<PanelShortModel>>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    getPanelsShort("Bearer " + userDb.getToken(), objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<ArrayList<PanelShortModel>>>(callback) {
                        @Override
                        public void onNext(Response<ArrayList<PanelShortModel>> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.getPanelsShort(context, objectDb1, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void logout(ObjectDb objectDb,
                          Callback<Response<Void>> callback) {
        repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                logout("Bearer " + userDb.getToken(), new OnErrorDefaultCallback<Response<Void>>(callback) {
                    @Override
                    public void onNext(Response<Void> response) {
                        callback.onSuccess(response);
                    }
                }));
    }

    public void logout(UserModel userModel,
                       Callback<Response<Void>> callback) {
        logout("Bearer " + userModel.getApi_token(), new OnErrorDefaultCallback<Response<Void>>(callback) {
            @Override
            public void onNext(Response<Void> response) {
                callback.onSuccess(response);
            }
        });
    }

    public void getObject(Context context,
                          ObjectDb objectDb,
                          Callback<Response<UserModel>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    getObject("Bearer " + userDb.getToken(), objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<UserModel>>(callback) {
                        @Override
                        public void onNext(Response<UserModel> response) {
                            if(!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.getObject(context, objectDb1, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void getClaimsList(Context context,
                              ObjectDb objectDb,
                                 Callback<Response<ArrayList<ClaimModel>>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    getClaimsList("Bearer " + userDb.getToken(), objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<ArrayList<ClaimModel>>>(callback) {
                        @Override
                        public void onNext(Response<ArrayList<ClaimModel>> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.getClaimsList(context, objectDb1, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }


    public void getQuizzesList(Context context,
                               ObjectDb objectDb,
                                  Callback<Response<List<QuizzModel>>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    getQuizzesList("Bearer " + userDb.getToken(), objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<List<QuizzModel>>>(callback) {
                        @Override
                        public void onNext(Response<List<QuizzModel>> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.getQuizzesList(context, objectDb1, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void getClaimTypes(Context context,
                              ObjectDb objectDb,
                                 Callback<Response<ArrayList<TypeModel>>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    getClaimTypes("Bearer " + userDb.getToken(), objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<ArrayList<TypeModel>>>(callback) {
                        @Override
                        public void onNext(Response<ArrayList<TypeModel>> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.getClaimTypes(context, objectDb1, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void getNotificationsList(Context context,
                                     ObjectDb objectDb,
                                        Callback<Response<List<NotificationModel>>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    getNotificationsList("Bearer " + userDb.getToken(), objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<List<NotificationModel>>>(callback) {
                        @Override
                        public void onNext(Response<List<NotificationModel>> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.getNotificationsList(context, objectDb1, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void getUnreadMessages(Context context,
                                  ObjectDb objectDb,
                                     Callback<Response<UnreadMessagesModel>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    getUnreadMessages("Bearer " + userDb.getToken(), objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<UnreadMessagesModel>>(callback) {
                        @Override
                        public void onNext(Response<UnreadMessagesModel> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.getUnreadMessages(context, objectDb1, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void getAdvertList(Context context,
                              ObjectDb objectDb,
                                 Callback<Response<List<AdvertModel>>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    getAdvertList("Bearer " + userDb.getToken(), objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<List<AdvertModel>>>(callback) {
                        @Override
                        public void onNext(Response<List<AdvertModel>> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.getAdvertList(context, objectDb1, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void getAdvertBoardList(Context context,
                                   ObjectDb objectDb,
                                      Callback<Response<List<AdvertModel>>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    getAdvertBoardList("Bearer " + userDb.getToken(), objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<List<AdvertModel>>>(callback) {
                        @Override
                        public void onNext(Response<List<AdvertModel>> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.getAdvertBoardList(context, objectDb1, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void setAdvertAsRead(Context context, ObjectDb objectDb,
                                   String advertId,
                                   Callback<Response<AdvertModel>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    setAdvertAsRead("Bearer " + userDb.getToken(), advertId, objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<AdvertModel>>(callback) {
                        @Override
                        public void onNext(Response<AdvertModel> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.setAdvertAsRead(context, objectDb1, advertId, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void setNotificationAsRead(Context context,
                                      ObjectDb objectDb,
                                         String notificationId,
                                         Callback<Response<NotificationModel>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    setNotificationAsRead("Bearer " + userDb.getToken(), notificationId, objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<NotificationModel>>(callback) {
                        @Override
                        public void onNext(Response<NotificationModel> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.setNotificationAsRead(context, objectDb1, notificationId, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void postQuizzesAnswers(Context context,
                                   ObjectDb objectDb,
                                      String quizId,
                                      QuizzesAnswers quizzesAnswers,
                                      Callback<Response<Void>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    postQuizzesAnswers("Bearer " + userDb.getToken(), quizId, quizzesAnswers, objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<Void>>(callback) {
                        @Override
                        public void onNext(Response<Void> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.postQuizzesAnswers(context, objectDb1, quizId, quizzesAnswers, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }


    public void setQuizAsRead(Context context,
                              ObjectDb objectDb,
                                 String quizId,
                                 Callback<Response<QuizzModel>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    setQuizAsRead("Bearer " + userDb.getToken(), quizId, objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<QuizzModel>>(callback) {
                        @Override
                        public void onNext(Response<QuizzModel> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.setQuizAsRead(context, objectDb1, quizId, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void getQuizzesResults(Context context,
                                  ObjectDb objectDb,
                                     String quizId,
                                     Callback<Response<QuizzesResultsModel.QuizResult>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    getQuizzesResults("Bearer " + userDb.getToken(), quizId, objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<QuizzesResultsModel.QuizResult>>(callback) {
                        @Override
                        public void onNext(Response<QuizzesResultsModel.QuizResult> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.getQuizzesResults(context, objectDb1, quizId, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }


    public void getComments(Context context,
                            ObjectDb objectDb,
                               Callback<Response<ArrayList<CommentModel>>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    getComments("Bearer " + userDb.getToken(), objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<ArrayList<CommentModel>>>(callback) {
                        @Override
                        public void onNext(Response<ArrayList<CommentModel>> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.getComments(context, objectDb1, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void postFeedback(Context context,
                             ObjectDb objectDb,
                                String feedback,
                                Callback<Response<Void>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    postFeedback("Bearer " + userDb.getToken(), feedback, objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<Void>>(callback) {
                        @Override
                        public void onNext(Response<Void> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.postFeedback(context, objectDb1, feedback, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void postFeedback(Context context,
                             ObjectDb objectDb,
                             MessageModel feedback,
                             Callback<Response<Void>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    postFeedback("Bearer " + userDb.getToken(), feedback, objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<Void>>(callback) {
                        @Override
                        public void onNext(Response<Void> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.postFeedback(context, objectDb1, feedback, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void postPushToken(Context context,
                              ObjectDb objectDb,
                                 String pushToken,
                                 Callback<Response<Device>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    postPushToken("Bearer " + userDb.getToken(), pushToken, objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<Device>>(callback) {
                        @Override
                        public void onNext(Response<Device> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.postPushToken(context, objectDb1, pushToken, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void getSipNumber(Context context,
                             ObjectDb objectDb,
                                Callback<Response<UserModel>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    getSipNumber("Bearer " + userDb.getToken(), objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<UserModel>>(callback) {
                        @Override
                        public void onNext(Response<UserModel> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.getSipNumber(context, objectDb1, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void getLanguage(Context context,
                            ObjectDb objectDb,
                               Callback<Response<LangModel>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    getLanguage("Bearer " + userDb.getToken(), objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<LangModel>>(callback) {
                        @Override
                        public void onNext(Response<LangModel> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.getLanguage(context, objectDb1, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void setLanguage(Context context,
                            ObjectDb objectDb,
                               String locale,
                               Callback<Response<LangModel>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    setLanguage("Bearer " + userDb.getToken(), locale, objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<LangModel>>(callback) {
                        @Override
                        public void onNext(Response<LangModel> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.setLanguage(context, objectDb1, locale, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void getClaim(Context context,
                         ObjectDb objectDb,
                            String claimId,
                            Callback<Response<ClaimResponse>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    getClaim("Bearer " + userDb.getToken(), claimId, objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<ClaimResponse>>(callback) {
                        @Override
                        public void onNext(Response<ClaimResponse> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.getClaim(context, objectDb1, claimId, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }


    public void postClaim(Context context, ObjectDb objectDb,
                             HashMap<String, Object> claimJSON,
                             Callback<Response<ClaimModel>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    postClaim("Bearer " + userDb.getToken(), claimJSON, objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<ClaimModel>>(callback) {
                        @Override
                        public void onNext(Response<ClaimModel> response) {

                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.postClaim(context, objectDb1, claimJSON, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void updateClaim(Context context,
                            ObjectDb objectDb,
                               String claimId,
                               HashMap<String, Object> claimModel,
                               Callback<Response<ClaimModel>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    updateClaim("Bearer " + userDb.getToken(), claimId, claimModel, objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<ClaimModel>>(callback) {
                        @Override
                        public void onNext(Response<ClaimModel> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.updateClaim(context, objectDb1, claimId, claimModel, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void getCommentAnswers(Context context,
                                    ObjectDb objectDb,
                                       String commentId,
                                       int start,
                                       int limit,
                                       Callback<Response<List<AnswerModel>>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    getCommentAnswers("Bearer " + userDb.getToken(), commentId, start, limit, objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<List<AnswerModel>>>(callback) {
                        @Override
                        public void onNext(Response<List<AnswerModel>> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.getCommentAnswers(context, objectDb1, commentId, start, limit, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void getActivationsCount(Context context,
                                    ObjectDb objectDb,
                                    Callback<Response<ActivationsCountModel>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    getActivationsCount("Bearer " + userDb.getToken(), objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<ActivationsCountModel>>(callback) {
                        @Override
                        public void onNext(Response<ActivationsCountModel> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.getActivationsCount(context, objectDb1, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    public void uploadImageFiles(Context context,
                                 ObjectDb objectDb,
                                    String requestId,
                                    List<PhotoModel> photos,
                                    Callback<Response<ClaimModel>> callback) {
        if (mayUseApi(objectDb)) {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb ->
                    uploadImageFiles("Bearer " + userDb.getToken(), requestId, photos, objectDb.getServerUrl(), new OnErrorDefaultCallback<Response<ClaimModel>>(callback) {
                        @Override
                        public void onNext(Response<ClaimModel> response) {
                            if (!response.isSuccessful() && response.code() == 401) {
                                tryToReactivate(context, objectDb, callback, objectDb1 ->
                                        ApiControllerWithReactivation.this.uploadImageFiles(context, objectDb1, requestId, photos, callback));
                            } else {
                                callback.onSuccess(response);
                            }
                        }
                    }));
        }
    }

    private boolean mayUseApi(ObjectDb objectDb) {
        return (objectDb.getIsServerActive() != null && objectDb.getIsServerActive() == 1 && !objectDb.isBlocked()) ||
                (objectDb.getIsServerActive() == null && !objectDb.isBlocked());
    }

    private void updateTokenInDataBaseAndRecallMethod(Context context, Response<UserModel> reactivationResponse, ObjectDb objectDb) {
        if (reactivationResponse != null && reactivationResponse.isSuccessful()) {
            UserModel userModel = reactivationResponse.body();

            if (userModel != null) {
                updateUser(objectDb, userModel);
            }
        } else {
            SipServiceCommands.removeAccount(App.getContext(), objectDb.getIdUri());

            clearCachedMessages(objectDb);

            if (reactivationResponse != null && reactivationResponse.code() == 409) {
                clearPendingRequestsQueue(objectDb.getObject_id());

                showReceivedErrorMessage(context, reactivationResponse);
            } else {
                if (context instanceof AppCompatActivity) {
                    AppCompatActivity activity = (AppCompatActivity) context;

                    ReactivationCodeDialog.OnClickListener listener = createDialogListener(context, objectDb);

                    showReactivationCodeDialog(objectDb, activity, listener);
                } else {
                    clearPendingRequestsQueue(objectDb.getObject_id());
                    setObjectBlocked(context, objectDb);
                }
            }
        }
    }

    private void clearCachedMessages(ObjectDb objectDb) {
        repositoryController.deleteMessagesByUserId(objectDb.getUser_id()).subscribe(
                () -> {},
                t -> t.printStackTrace());
    }

    private ReactivationCodeDialog.OnClickListener createDialogListener(Context context, ObjectDb objectDb) {
        return new ReactivationCodeDialog.OnClickListener() {
            @Override
            public void onSubmitClick(DialogInterface dialogInterface, CharSequence reactivationCode) {
                if (validateActivationCode(context, reactivationCode.toString())) {
                    String lang = getLanguageTag(context);
                    reactivation(lang, objectDb.getFlat_number(), reactivationCode.toString(), new Callback<Response<UserModel>>() {
                        @Override
                        public void onSuccess(Response<UserModel> object) {
                            super.onSuccess(object);
                            if (object.isSuccessful()) {
                                UserModel userModel = object.body();
                                if (userModel != null) {
                                    dialogInterface.dismiss();
                                    objectDb.setActivation_code(reactivationCode.toString());

                                    updateUser(objectDb, userModel);
                                }
                            } else {
                                clearPendingRequestsQueue(objectDb.getObject_id());
                                showReceivedErrorMessage(context, object);
                            }
                        }

                        @Override
                        public void onError(String error) {
                            super.onError(error);
                            clearPendingRequestsQueue(objectDb.getObject_id());
                        }
                    });
                }
            }

            @Override
            public void onCancelClick(DialogInterface dialogInterface) {
                dialogInterface.dismiss();

                clearPendingRequestsQueue(objectDb.getObject_id());
                setObjectBlocked(context, objectDb);
            }
        };
    }

    private void setObjectBlocked(Context context, ObjectDb objectDb) {
        objectDb.setIsBlocked(true);
        SipServiceCommands.removeAccount(context, objectDb.getIdUri());

        repositoryController.updateObject(objectDb).subscribe(() ->
                Logger.error(TAG, "Object successfully added"));
    }

    private synchronized void clearPendingRequestsQueue(int objectId) {
        requestsQueue.remove(objectId);
    }

    private boolean validateActivationCode(Context context, String apartmentCode) {
        boolean result = true;
        String message = "";

        if (apartmentCode != null && !apartmentCode.isEmpty()) {
            if (!apartmentCode.matches(Constants.APARTMENT_ACTIVATION_CODE_REGEXP)) {
                result = false;
                if (apartmentCode.length() < Constants.ACTIVATION_CODE_LENGTH + 3) {
                    message = context.getString(R.string.login_number_not_complete, "");
                } else {
                    message = context.getString(R.string.login_code_text_error, "");
                }
            }
        } else {
            result = false;
            message = context.getString(R.string.login_code_text_error_empty, "");
        }

        if (!result) {
            DialogHelper.createErrorDialog(
                    context,
                    context.getString(R.string.text_error_dialog_title),
                    message);
        }

        return result;
    }


    private void showReactivationCodeDialog(ObjectDb objectDb,
                                            AppCompatActivity activity,
                                            ReactivationCodeDialog.OnClickListener listener) {

        FragmentManager manager = activity.getSupportFragmentManager();
        String tag = ReactivationCodeDialog.class.getSimpleName() + "_" + String.valueOf(objectDb.getObject_id());

        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment != null) {
            if (fragment instanceof ReactivationCodeDialog) {
                ReactivationCodeDialog reactivationCodeDialog = (ReactivationCodeDialog)fragment;

                reactivationCodeDialog.addOnClickListener(listener);
            }

        } else {
            ReactivationCodeDialog dialog = ReactivationCodeDialog.getInstance();

            dialog.addOnClickListener(listener);
            dialog.setTitle(objectDb.getName());
            manager.beginTransaction()
                    .add(dialog, tag)
                    .addToBackStack(null)
                    .commit();
            manager.executePendingTransactions();
        }
    }

    private void tryToReactivate(Context context, ObjectDb objectDb, Callback callback, RecallAction recallAction) {
        String lang = getLanguageTag(context);

        synchronized (lockObject) {
            if (!requestsQueue.containsKey(objectDb.getObject_id())) {
                Queue<RecallAction> queue = new LinkedList<>();
                queue.add(recallAction);

                requestsQueue.put(objectDb.getObject_id(), queue);

                reactivation(lang, objectDb.getFlat_number(), objectDb.getActivation_code(), new Callback<Response<UserModel>>() {
                    @Override
                    public void onSuccess(Response<UserModel> reactivationResponse) {
                        super.onSuccess(reactivationResponse);
                        updateTokenInDataBaseAndRecallMethod(context, reactivationResponse, objectDb);
                    }

                    public void onError(String error) {
                        super.onError(error);
                        clearPendingRequestsQueue(objectDb.getObject_id());

                        callback.onError(error);
                    }
                });
            } else {
                Queue<RecallAction> queue = requestsQueue.get(objectDb.getObject_id());
                queue.add(recallAction);
            }
        }
    }

    private void showReceivedErrorMessage(Context context, Response response) {
        if (context instanceof Activity) {
            ErrorApiResponse errorApiResponse = new ErrorApiResponse(response.errorBody());
            String msg = Utils.getDetailedErrorMessage(errorApiResponse);
            if (!msg.isEmpty()) {
                DialogHelper.createErrorDialog(context,
                        context.getString(R.string.text_error_dialog_title),
                        msg);
            } else {
                DialogHelper.createErrorDialog(context,
                        context.getString(R.string.text_error_dialog_title),
                        context.getString(R.string.text_error_default_message));
            }
        }
    }

    private void updateObject(ObjectDb objectDb, UserModel userModel) {

        String actionForFilter = BroadcastEventEmitter.getAction(BroadcastEventEmitter.BroadcastAction.ACCOUNT_REMOVED);
        int objectToRemoveId = objectDb.getObject_id();

        App.getContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                int objectId = intent.getIntExtra(BroadcastEventEmitter.BroadcastParameters.OBJECT_ID, 0);

                if (action != null && action.equals(actionForFilter) && objectToRemoveId == objectId) {

                    SipAccountData sipAccountData = new SipAccountData(
                            userModel.getSip_server_address(),
                            userModel.getSip_client_number(),
                            userModel.getSip_client_password(),
                            userModel.getSip_server_port());

                    SipServiceCommands.createAccount(App.getContext(), sipAccountData);
                    App.getContext().unregisterReceiver(this);
                }
            }
        }, new IntentFilter(actionForFilter));

        SipServiceCommands.removeAccount(App.getContext(), objectDb.getIdUri(), objectToRemoveId);

        objectDb.setName(userModel.getAddress());
        objectDb.setIp_address(userModel.getSip_server_address());
        objectDb.setPort(userModel.getSip_server_port());
        objectDb.setPassword(userModel.getSip_client_password());
        objectDb.setSip_number(userModel.getSip_client_number());

        objectDb.setLicenseType(userModel.getServer().getLicense());
        objectDb.setServerUrl(userModel.getServer().getUrl());

        String concierge_sip_number = userModel.getConcierge_sip_number();
        if (concierge_sip_number != null && concierge_sip_number.length() > 0) {
            objectDb.setHas_concierge(true);
            objectDb.setConcierge_number(concierge_sip_number);
        } else {
            objectDb.setHas_concierge(false);
            objectDb.setConcierge_number("");
        }

        repositoryController.updateObject(objectDb).subscribe(() -> {
                Logger.error(TAG, "Object successfully added");

                String currentFCMToken = repositoryController.getToken();

                postPushToken("Bearer " + userModel.getApi_token(), currentFCMToken, userModel.getServer().getUrl(), new Observer<Response<Device>>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(Response<Device> deviceResponse) {}

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onComplete() {}
                });
        });
    }

    private String getLanguageTag(Context context) {
        String lang = "en";
        Locale locale = Utils.getCurrentLocale(context);
        if (locale != null) {
            lang = locale.getLanguage();
            //FIXME following 'if' must be removed once language tag is fixed on server
            if (lang.equals("uk")) {
                lang = "ua";
            }
        }
        return lang;
    }

    private void updateUser(ObjectDb objectDb, UserModel userModel) {
        repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb -> {
            userDb.setToken(userModel.getApi_token());

            repositoryController.updateUser(userDb).subscribe(() -> {
                updateObject(objectDb, userModel);

                Queue<RecallAction> queue;
                synchronized (lockObject) {
                    queue = requestsQueue.get(objectDb.getObject_id());
                    requestsQueue.remove(objectDb.getObject_id());
                }

                if (queue != null) {
                    while (!queue.isEmpty()) {
                        RecallAction recallAction = queue.poll();
                        if (recallAction != null) {
                            recallAction.recall(objectDb);
                        }
                    }
                }
            });
        });
    }
}
