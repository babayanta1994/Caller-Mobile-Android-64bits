package ru.true_ip.trueip.api;

/**
 * Created by ektitarev on 03.09.2018.
 */

public class Endpoints {
    public static final String GET_LIST_CAMERAS_SHORT = "api/cameras";

    public static final String GET_LIST_PANELS_SHORT = "api/callingpanels";

    public static final String GET_LIST_CAMERAS = "api/cameras";

    public static final String GET_CLAIM_TYPES = "api/claimtypes";

    public static final String POST_CLAIM = "api/claims";

    public static final String GET_OBJECT = "api/getobject";

    public static final String LOGOUT = "api/logout";

    public static final String GET_UNREAD_MESSAGES = "api/messages/count";

    public static final String POST_QUIZZES_ANSWERS = "api/quizzes/%s/answers";

    public static final String POST_FEEDBACK = "api/recall";

    public static final String POST_FEEDBACK_ANSWER = "api/recall/answer";

    public static final String GET_COMMENTS = POST_FEEDBACK;

    public static final String POST_PUSH_TOKEN = "api/pushtoken";

    public static final String GET_SIP_NUMBER = "api/device/sipnumber";

    public static final String GET_LANGUAGE = "api/locale";

    public static final String SET_LANGUAGE = GET_LANGUAGE;

    public static final String GET_ADVERTS = "api/adverts";

    public static final String SET_ADVERT_AS_READ = "api/adverts/%s";

    public static final String GET_ADVERTS_BOARD = "api/advertsboard";

    public static final String GET_NOTIFICATIONS = "api/notifications";

    public static final String SET_NOTIFICATION_AS_READ = "api/notifications/%s";

    public static final String GET_QUIZZES = "api/quizzes";

    public static final String GET_QUIZZES_RESULTS = "api/quizzes/%s";

    public static final String SET_QUIZ_AS_READ = "api/quizzes/%s";

    public static final String GET_CLAIMS_LIST = "api/claims";

    public static final String GET_CLAIM = "api/claims/%s";

    public static final String GET_ACTIVATIONS_COUNT = "api/apartment/activations";

    public static final String UPDATE_CLAIM = "api/claims/%s";

    public static final String UPLOAD_IMAGE_FILES = "api/claims/%s/images";

    public static final String GET_COMMENT_ANSWERS = "api/recall/%s/?start=%d&limit=%d";
}
