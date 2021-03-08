package ru.true_ip.trueip.javaFCM;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.splash_screen.SplashActivity;
import ru.true_ip.trueip.utils.Constants;

/**
 * Created by rmolodkin on 12.01.2018.
 */

public class MessagingService extends FirebaseMessagingService {

    //private static final String TAG = MessagingService.class.getSimpleName();

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options

        Map<String, String> data = remoteMessage.getData();
        if (data != null) {
            String title = data.get("title");
            String message = data.get("message");
            sendNotification(title, message);
        }
    }

    private void sendNotification(String title, String message) {
        PendingIntent contentIntent = createContentIntent();
        Notification notification = createNotification(title, message, contentIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(createNotificationChannel());
                notificationManager.notify(Constants.NOTIFICATION_CHANNEL_ID, 1, notification);
            } else {
                notificationManager.notify(Constants.NOTIFICATION_CHANNEL_ID, 1, notification);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private NotificationChannel createNotificationChannel() {
        return new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
    }

    private Notification createNotification(CharSequence title, CharSequence message, PendingIntent contentIntent) {
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_push_notification)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(contentIntent);

        return notificationBuilder.build();
    }

    private PendingIntent createContentIntent() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.BUNDLE_FROM_NOTIFICATION, true);

        return PendingIntent.getActivity(this, 0 /* Request code */,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

    }
}
