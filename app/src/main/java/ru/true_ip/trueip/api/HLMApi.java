package ru.true_ip.trueip.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;
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

public interface HLMApi {


    @GET
    Observable<Response<ArrayList<CameraShortModel>>> getListCamerasShort(@Header("Authorization") String api_token, @Url String endpointUrl);

    @GET
    Observable<Response<ArrayList<PanelShortModel>>> getListPanelsShort(@Header("Authorization") String api_token, @Url String endpointUrl);

    @GET
    Observable<Response<List<CameraModel>>> getListCameras(@Header("Authorization") String api_token, @Url String endpointUrl);

    @GET
    Observable<Response<ArrayList<TypeModel>>> getClaimTypes(@Header("Authorization") String api_token, @Url String endpointUrl);

    //@FormUrlEncoded
    @POST
    Observable<Response<ClaimModel>> postClaim(@Header("Authorization") String api_token,
                                               @Body HashMap<String, Object> claimJSON,
                                               @Url String endpointUrl);

    @POST("api/logout")
    Observable<Response<Void>> logout(@Header("Authorization") String api_token);

    @GET
    Observable<Response<UnreadMessagesModel>> getUnreadMessages(@Header("Authorization") String api_token, @Url String endpointUrl);

    @POST
    Observable<Response<Void>> postQuizzesAnswers(@Header("Authorization") String api_token,
                                                  @Body QuizzesAnswers quizzesAnswers,
                                                  @Url String endpointUrl);

    @FormUrlEncoded
    @POST
    Observable<Response<Void>> postFeedback(@Header("Authorization") String api_token,
                                            @Field("text") String feedback,
                                            @Url String endpointUrl);

    @POST
    Observable<Response<Void>> postFeedback(@Header("Authorization") String api_token,
                                            @Body MessageModel feedback,
                                            @Url String endpointUrl);

    @GET
    Observable<Response<ArrayList<CommentModel>>> getComments(@Header("Authorization") String api_token, @Url String endpointUrl);


    @FormUrlEncoded
    @POST
    Observable<Response<Device>> postPushToken(@Header("Authorization") String api_token, @Field("push_token") String pushToken, @Field("is_ios")Integer is_ios, @Url String endpointUrl);

    @GET
    Observable<Response<UserModel>> getSipNumber(@Header("Authorization") String api_token, @Url String endpointUrl);

    @GET
    Observable<Response<LangModel>> getLanguage(@Header("Authorization") String api_token, @Url String endpointUrl);

    @FormUrlEncoded
    @PUT
    Observable<Response<LangModel>> setLanguage(@Header("Authorization") String api_token, @Field("locale") String locale, @Url String endpointUrl);

    @GET
    Observable<Response<List<AdvertModel>>> getAdverts(@Header("Authorization") String api_token, @Url String endpointUrl);

    @PUT
    Observable<Response<AdvertModel>> setAdvertAsRead(@Header("Authorization") String api_token, @Url String endpointUrl);

    @GET
    Observable<Response<List<AdvertModel>>> getAdvertsBoard(@Header("Authorization") String api_token, @Url String endpointUrl);

    @GET
    Observable<Response<List<NotificationModel>>> getNotifications(@Header("Authorization") String api_token, @Url String endpointUrl);

    @PUT
    Observable<Response<NotificationModel>> setNotificationAsRead(@Header("Authorization") String api_token,
                                                                  @Url String endpointUrl);

    @GET
    Observable<Response<List<QuizzModel>>> getQuizzes(@Header("Authorization") String api_token,
                                                      @Url String endpointUrl);

    @GET
    Observable<Response<QuizzesResultsModel.QuizResult>> getQuizzesResults(@Header("Authorization") String api_token,
                                                                           @Url String endpointUrl);

    @PUT
    Observable<Response<QuizzModel>> setQuizAsRead(@Header("Authorization") String api_token,
                                                   @Url String endpointUrl);

    @GET
    Observable<Response<ArrayList<ClaimModel>>> getClaimsList(@Header("Authorization") String api_token,
                                                              @Url String endpointUrl);

    @GET
    Observable<Response<ClaimResponse>> getClaim(@Header("Authorization") String api_token,
                                                 @Url String endpointUrl);

    //@FormUrlEncoded
    @PUT
    Observable<Response<ClaimModel>> updateClaim(@Header("Authorization") String api_token,
                                                 @Body HashMap<String, Object> claimJSON,
                                                 @Url String endpointUrl);

    //@Multipart
    @POST
    Observable<Response<ClaimModel>> uploadImageFiles(@Header("Authorization") String api_token,
                                                      @Body RequestBody images,
                                                      @Url String endpointUrl);

    @GET
    Observable<Response<List<AnswerModel>>> getCommentAnswers(@Header("Authorization") String api_token,
                                                              @Url String endpointUrl);
}
