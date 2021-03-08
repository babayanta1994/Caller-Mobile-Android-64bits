package ru.true_ip.trueip.models.responses;

import java.util.List;

/**
 * Created by Eugen on 06.09.2017.
 */

public class NotificationsListModel {

    List<NotificationModel> notifications;

    public List<NotificationModel> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationModel> notifications) {
        this.notifications = notifications;
    }
}
