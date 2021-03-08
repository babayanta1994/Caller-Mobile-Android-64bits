package ru.true_ip.trueip.utils;

/**
 * Created by Eugen on 02.10.2017.
 */

public class Constants {

    public static final String NOTIFICATION_CHANNEL_ID = "ru.true_ip.trueip.TrueIP";
    public static final String BACKGROUND_SERVICE_CHANNEL_ID = "ru.true_ip.trueip.background.TrueIP";

    public static final String BUNDLE_INT_KEY = "BUNDLE_INT_KEY";
    public static final String BUNDLE_IS_EDIT_MODE = "BUNDLE_IS_EDIT_MODE";
    public static final String BUNDLE_IS_CAMERA_DEVICE = "BUNDLE_IS_CAMERA_DEVICE";
    public static final String DEVICE_ID_KEY = "DEVICE_ID_KEY";
    public static final String BUNDLE_DEVICE_TYPE = "BUNDLE_DEVICE_TYPE";
    public static final String BUNDLE_DESTINATION = "BUNDLE_DESTINATION";
    public static final String BUNDLE_FROM_NOTIFICATION = "BUNDLE_FROM_NOTIFICATION";

    public static final String BUNDLE_SHOW_FLOATING_BUTTON = "BUNDLE_SHOW_FLOATING_BUTTON";

    public static final int IMAGE_COMPRESS_QUALITY = 50;
    public static final String DEFAULT_TAG = "DEFAULT_TAG";
    public static final int MAX_ACCOUNT_SIZE = 4;
    public static final Integer TYPE_PANEL = 1;
    public static final Integer TYPE_CAMERA = 2;
    //Data order username, password , ip address, port
    public static final String RSTP_BASE_FORMAT = "rtsp://%s:%s@%s:%s/cam/realmonitor?channel=1&subtype=0";
    //Concierge
    public static final String CALL_CONCIERGE = "CALL_CONCIERGE";
    public static final String CONCIERGE_SIP = "CONCIERGE_SIP";
    public static final String OBJECT_ID = "OBJECT_ID";
    public static final String USER_ID = "USER_ID";

    public static final String OBJECT_HAS_SIP_NUMBER = "OBJECT_HAS_SIP_NUMBER";

    public static final String LOAD_DEVICES = "LOAD_DEVICES";

    public static final String VOTES_COUNT = "VOTES_COUNT";
    public static final String BASE_URL = "BASE_URL";

    public static final int PICK_IMAGE = 123;

    public static final String REQUESTS_STATUS = "REQUESTS_STATUS";
    public static final int ACTIVE_REQUESTS = 0;
    public static final int COMPLETED_REQUESTS = 1;

    public static final String PHOTO_PATH = "PHOTO_PATH";

    public static final String PHOTO_ITEMS = "PHOTO_ITEMS";
    public static final String PHOTO_CURRENT_ITEM = "PHOTO_CURRENT_ITEM";
    public static final String PHOTO_TIMESTAMP = "PHOTO_TIMESTAMP";

    public static final int APARTMENT_NUMBER_LENGTH = 8;
    public static final int ACTIVATION_CODE_LENGTH = 16;

    public static final String APARTMENT_NUMBER_FORMAT_REGEXP = "^\\d+[a-zA-Z0-9\\u0400-\\u04FF]*$";
    public static final String APARTMENT_ACTIVATION_CODE_REGEXP = "^([a-zA-Z0-9]{4}-){3}[a-zA-Z0-9]{4}$";

    /***
     0 => 'New' => 'Новая' - красный кружок
     1 => 'In work' => 'В работе' - желтый кружок
     2 => 'Rejected' => 'Отклонена' - серый кружок
     3 => 'Done' => 'Выполнено' - зеленый кружок
     */

    public static final String STATUS_NEW = "new";
    public static final String STATUS_IN_WORK = "in work";
    public static final String STATUS_REJECTED = "rejected";
    public static final String STATUS_DONE = "done";

    public static final String REQUEST_EDITABLE = "REQUEST_EDITABLE";
    public static final String REQUEST_OBJECT = "REQUEST_OBJECT";

    public static final String QUIZZES_TYPE = "QUIZZES_TYPE";
    public static final int PASSED_QUIZZES = 0;
    public static final int NOT_PASSED_QUIZZES = 1;

    public static final String QUIZ_OBJECT = "QUIZ_OBJECT";

    public static final String COMMENT_OBJECT = "COMMENT_OBJECT";

    public static final String FINISH_ACTIVITY = "ru.true_ip.trueip.do_finish";

    public static final int MAX_PHOTOS_COUNT = 3;

    public static final String ACTION_NOTIFY_LOCALE_CHANGED = "ru.true_ip.trueip.ACTION_NOTIFY_LOCALE_CHANGED";

    public static final String LICENSE_TYPE_PRO = "pro";
    public static final String LICENSE_TYPE_LITE = "lite";

    public static final int SERVER_STATUS_ACTIVE = 1;
    public static final int SERVER_STATUS_INACTIVE = 0;
}
