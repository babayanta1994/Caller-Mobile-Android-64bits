package ru.true_ip.trueip.repository;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import ru.true_ip.trueip.api.Endpoints;
import ru.true_ip.trueip.api.HLMApi;
import ru.true_ip.trueip.app.main_screen.MainPresenter;
import ru.true_ip.trueip.base.Callback;
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
import ru.true_ip.trueip.models.responses.LangModel;
import ru.true_ip.trueip.models.responses.NotificationModel;
import ru.true_ip.trueip.models.responses.PanelShortModel;
import ru.true_ip.trueip.models.responses.QuizzModel;
import ru.true_ip.trueip.models.responses.QuizzesResultsModel;
import ru.true_ip.trueip.models.responses.ServerStatusModel;
import ru.true_ip.trueip.models.responses.TypeModel;
import ru.true_ip.trueip.models.responses.UnreadMessagesModel;
import ru.true_ip.trueip.models.responses.UserModel;

/**
 *
 * Created by Andrey Filimonov 13-Sep-17.
 */

@SuppressWarnings(value = "CheckResult")
public class ApiController {

    private HLMApi hlmApi;

    private String DEFAULT_BASE_URL = "https://trueip2.smartru.com/";

    public ApiController(HLMApi hlmApi) {
        this.hlmApi = hlmApi;
    }


    private String createFullUrl(String baseUrl, String endpoint) {
        if (baseUrl != null && !baseUrl.isEmpty()) {
            return StringUtils.stripEnd(baseUrl, "/") + "/" + StringUtils.stripStart(endpoint, "/");
        }

        return StringUtils.stripEnd(DEFAULT_BASE_URL, "/") + "/" + StringUtils.stripStart(endpoint, "/");
    }

    public void activation(String locale, String flatNumber, String activationCode, Callback<Response<UserModel>> callback) {
        hlmApi.activation(locale, flatNumber, activationCode).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        t -> callback.onError(t.getMessage())
                );
    }

    public void reactivation(String locale, String flatNumber, String activationCode, Callback<Response<UserModel>> callback) {
        hlmApi.reactivation(locale, flatNumber, activationCode).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        t -> callback.onError(t.getMessage())
                );
    }

    public void getServerStatus(String api_token, Observer<Response<ServerStatusModel>> callback) {
        hlmApi.getServerStatus(api_token)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(callback);
    }

    protected void getObject(String api_token, String baseUrl, Observer<Response<UserModel>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.GET_OBJECT);

        hlmApi.getObject(api_token, fullUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    protected void getCameras(String api_token, String baseUrl, Observer<Response<List<CameraModel>>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.GET_LIST_CAMERAS);

        hlmApi.getListCameras(api_token, fullUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);

    }

    protected void getCamerasShort(String api_token, String baseUrl, Observer<Response<ArrayList<CameraShortModel>>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.GET_LIST_CAMERAS_SHORT);

        hlmApi.getListCamerasShort(api_token, fullUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    protected void getPanelsShort(String api_token, String baseUrl, Observer<Response<ArrayList<PanelShortModel>>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.GET_LIST_PANELS_SHORT);

        hlmApi.getListPanelsShort(api_token, fullUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    protected void logout(String api_token, Observer<Response<Void>> callback) {
        hlmApi.logout(api_token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    protected void getClaimsList(String api_token, String baseUrl, Observer<Response<ArrayList<ClaimModel>>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.GET_CLAIMS_LIST);

        hlmApi.getClaimsList(api_token, fullUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }


    protected void getQuizzesList(String api_token, String baseUrl, Observer<Response<List<QuizzModel>>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.GET_QUIZZES);

        hlmApi.getQuizzes(api_token, fullUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    protected void getClaimTypes(String api_token, String baseUrl, Observer<Response<ArrayList<TypeModel>>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.GET_CLAIM_TYPES);

        hlmApi.getClaimTypes(api_token, fullUrl).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
    }

    protected void getNotificationsList(String api_token, String baseUrl, Observer<Response<List<NotificationModel>>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.GET_NOTIFICATIONS);

        hlmApi.getNotifications(api_token, fullUrl).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
    }

    protected void getUnreadMessages(String api_token, String baseUrl, Observer<Response<UnreadMessagesModel>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.GET_UNREAD_MESSAGES);

        hlmApi.getUnreadMessages(api_token, fullUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    protected void getAdvertList(String api_token, String baseUrl, Observer<Response<List<AdvertModel>>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.GET_ADVERTS);

        hlmApi.getAdverts(api_token, fullUrl).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
    }

    protected void getAdvertBoardList(String api_token, String baseUrl, Observer<Response<List<AdvertModel>>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.GET_ADVERTS_BOARD);

        hlmApi.getAdvertsBoard(api_token, fullUrl).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
    }

    protected void setAdvertAsRead(String api_token, String advertId, String baseUrl, Observer<Response<AdvertModel>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.SET_ADVERT_AS_READ);

        hlmApi.setAdvertAsRead(api_token, String.format(fullUrl, advertId)).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
    }

    protected void setNotificationAsRead(String api_token, String notificationId, String baseUrl, Observer<Response<NotificationModel>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.SET_NOTIFICATION_AS_READ);

        hlmApi.setNotificationAsRead(api_token, String.format(fullUrl, notificationId)).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
    }

    protected void postQuizzesAnswers(String api_token, String quizId, QuizzesAnswers quizzesAnswers, String baseUrl, Observer<Response<Void>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.POST_QUIZZES_ANSWERS);

        hlmApi.postQuizzesAnswers(api_token, quizzesAnswers, String.format(fullUrl, quizId)).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
    }


    protected void setQuizAsRead(String api_token, String quizId, String baseUrl, Observer<Response<QuizzModel>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.SET_QUIZ_AS_READ);

        hlmApi.setQuizAsRead(api_token, String.format(fullUrl, quizId)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    protected void getQuizzesResults(String api_token, String quizId, String baseUrl, Observer<Response<QuizzesResultsModel.QuizResult>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.GET_QUIZZES_RESULTS);

        hlmApi.getQuizzesResults(api_token, String.format(fullUrl, quizId)).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
    }


    protected void getComments(String api_token, String baseUrl, Observer<Response<ArrayList<CommentModel>>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.GET_COMMENTS);

        hlmApi.getComments(api_token, fullUrl).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
    }

    protected void postFeedback(String api_token, String feedback, String baseUrl, Observer<Response<Void>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.POST_FEEDBACK);

        hlmApi.postFeedback(api_token, feedback, fullUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    protected void postFeedback(String api_token, MessageModel feedback, String baseUrl, Observer<Response<Void>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.POST_FEEDBACK_ANSWER);

        hlmApi.postFeedback(api_token, feedback, fullUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    protected void postPushToken(String api_token, String pushToken, String baseUrl, Observer<Response<Device>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.POST_PUSH_TOKEN);

        hlmApi.postPushToken(api_token, pushToken, 0, fullUrl).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
    }

    protected void getSipNumber(String api_token, String baseUrl, Observer<Response<UserModel>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.GET_SIP_NUMBER);

        hlmApi.getSipNumber(api_token, fullUrl).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
    }

    protected void getLanguage(String api_token, String baseUrl, Observer<Response<LangModel>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.GET_LANGUAGE);

        hlmApi.getLanguage(api_token, fullUrl).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
    }

    protected void setLanguage(String api_token, String locale, String baseUrl, Observer<Response<LangModel>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.SET_LANGUAGE);

        hlmApi.setLanguage(api_token, locale, fullUrl).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
    }

    //======================= OLD stuff =============================


    protected void getClaim(String api_token, String claimId, String baseUrl, Observer<Response<ClaimResponse>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.GET_CLAIM);

        hlmApi.getClaim(api_token, String.format(fullUrl, claimId)).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
    }


    protected void postClaim(String api_token, HashMap<String, Object> claimJSON, String baseUrl, Observer<Response<ClaimModel>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.POST_CLAIM);

        hlmApi.postClaim(api_token, claimJSON, fullUrl).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
    }

    protected void updateClaim(String api_token, String claimId, HashMap<String, Object> claimModel, String baseUrl, Observer<Response<ClaimModel>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.UPDATE_CLAIM);

        hlmApi.updateClaim(api_token, claimModel, String.format(fullUrl, claimId)).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
    }

    protected void getActivationsCount(String api_token, String baseUrl, Observer<Response<ActivationsCountModel>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.GET_ACTIVATIONS_COUNT);

        hlmApi.getActivationsCount(api_token, fullUrl).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback);
    }

    protected void getCommentAnswers(String api_token, String commentId, int start, int limit, String baseUrl, Observer<Response<List<AnswerModel>>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.GET_COMMENT_ANSWERS);

        hlmApi.getCommentAnswers(api_token, String.format(fullUrl, commentId, start, limit))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);

    }

    protected void uploadImageFiles(String api_token, String requestId, List<PhotoModel> photos, String baseUrl, Observer<Response<ClaimModel>> callback) {
        String fullUrl = createFullUrl(baseUrl, Endpoints.UPLOAD_IMAGE_FILES);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MediaType.parse("multipart/form-data"));

        int i = 0;
        for (PhotoModel photoModel : photos) {
            File imageFile = new File(photoModel.path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);

            builder.addFormDataPart(String.format(Locale.getDefault(), "image%d", ++i), imageFile.getName(), requestBody);
        }

        hlmApi.uploadImageFiles(api_token, builder.build(), String.format(fullUrl, requestId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }
}