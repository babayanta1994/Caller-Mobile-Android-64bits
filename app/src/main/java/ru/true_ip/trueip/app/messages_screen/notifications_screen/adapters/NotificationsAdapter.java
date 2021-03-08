package ru.true_ip.trueip.app.messages_screen.notifications_screen.adapters;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.messages_screen.adapters.AbstractMessagesAdapter;
import ru.true_ip.trueip.base.adapters.BindingRecyclerAdapter;
import ru.true_ip.trueip.databinding.ItemNotificationBinding;
import ru.true_ip.trueip.models.responses.NotificationModel;

/**
 * Created by ektitarev on 29.12.2017.
 */

public class NotificationsAdapter extends AbstractMessagesAdapter<NotificationModel> {

    public NotificationsAdapter(List<NotificationModel> items) { super(items); }

    @Override
    protected String getTheme(int position) {
        return items.get(position).getTheme();
    }

    @Override
    protected String getMessage(int position) {
        return items.get(position).getText();
    }

    @Override
    protected String getActualDate(int position) {
        return items.get(position).getActual_from_date();
    }

    @Override
    protected int isViewed(int position) {
        return items.get(position).isIs_viewed();
    }

}
