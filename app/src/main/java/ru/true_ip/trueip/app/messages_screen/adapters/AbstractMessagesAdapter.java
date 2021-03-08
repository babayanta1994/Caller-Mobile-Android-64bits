package ru.true_ip.trueip.app.messages_screen.adapters;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.adapters.BindingRecyclerAdapter;
import ru.true_ip.trueip.databinding.ItemNotificationBinding;
import ru.true_ip.trueip.models.responses.AdvertModel;

/**
 * Created by ektitarev on 09.01.2018.
 */

public abstract class AbstractMessagesAdapter<T> extends BindingRecyclerAdapter<T> {

    public AbstractMessagesAdapter(List<T> items) {
        super(R.layout.item_notification, items);
    }

    @Override
    public void onBindViewHolder(BindingRecyclerAdapter.BindingHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        ItemNotificationBinding bind = (ItemNotificationBinding)holder.getBinding();

        TextView subject = bind.getRoot().findViewById(R.id.subject);
        TextView message = bind.getRoot().findViewById(R.id.message_text);
        TextView timestamp = bind.getRoot().findViewById(R.id.timestamp);
        View isNewMark = bind.getRoot().findViewById(R.id.new_notification_mark);

        bind.getRoot().setOnClickListener(view -> {
            if(mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(holder.getAdapterPosition(), items.get(holder.getAdapterPosition()));
            }
        });

        String subjectText = getTheme(position);
        String messageText = getMessage(position);
        String sourceDateText = getActualDate(position);
        int isViewed = isViewed(position);

        if (subjectText != null && !subjectText.isEmpty()) {
            subject.setText(subjectText);
        }

        if (messageText != null && !messageText.isEmpty()) {
            message.setText(messageText);
        }

        isNewMark.setVisibility(isViewed == 0 ? View.VISIBLE : View.GONE);

        String targetDateText = "";

        try {
            SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat targetFormat = new SimpleDateFormat("MM.dd.yy", Locale.getDefault());

            if (sourceDateText != null && !sourceDateText.isEmpty()) {
                Date sourceDate = sourceFormat.parse(sourceDateText);
                targetDateText = targetFormat.format(sourceDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!targetDateText.isEmpty()) {
            timestamp.setText(targetDateText);
        }
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void insertItem(int position, T item) {
        items.add(position, item);
        notifyItemInserted(position);
    }

    public void updateItem(int position, T item) {
        items.set(position, item);
        notifyItemChanged(position);
    }

    // get subject
    protected abstract String getTheme(int position);

    // get main text
    protected abstract String getMessage(int position);

    // get date
    protected abstract String getActualDate(int position);

    // is current message already viewed
    protected abstract int isViewed(int position);
}
