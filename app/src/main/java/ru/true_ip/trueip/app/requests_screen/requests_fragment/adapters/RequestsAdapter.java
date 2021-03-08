package ru.true_ip.trueip.app.requests_screen.requests_fragment.adapters;

import android.support.annotation.LayoutRes;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.App;
import ru.true_ip.trueip.base.adapters.BindingRecyclerAdapter;
import ru.true_ip.trueip.databinding.ItemRequestBinding;
import ru.true_ip.trueip.models.responses.ClaimModel;
import ru.true_ip.trueip.utils.Constants;

/**
 * Created by ektitarev on 26.12.2017.
 */

public class RequestsAdapter extends BindingRecyclerAdapter<ClaimModel> {

    public RequestsAdapter(@LayoutRes Integer holderLayout, List<ClaimModel> items) {
        super(holderLayout, items);
    }

    @Override
    public void onBindViewHolder(BindingRecyclerAdapter.BindingHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        ItemRequestBinding bind = (ItemRequestBinding) holder.getBinding();

        ClaimModel current = items.get(position);

        TextView counter = bind.getRoot().findViewById(R.id.counter);
        TextView timestamp = bind.getRoot().findViewById(R.id.timestamp);
        TextView type = bind.getRoot().findViewById(R.id.type);
        TextView statusTextView = bind.getRoot().findViewById(R.id.status);
        ImageView statusImage = bind.getRoot().findViewById(R.id.status_image);
        bind.getRoot().setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(holder.getAdapterPosition(), items.get(holder.getAdapterPosition()));
            }
        });

        String needAtFormattedString = "";

        try {
            SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outerFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());

            Date sourceDate = sourceFormat.parse(current.getNeed_at());
            needAtFormattedString = outerFormat.format(sourceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (needAtFormattedString.isEmpty()) {
            needAtFormattedString = App.getContext().getString(R.string.unknown_date);
        }

        counter.setText(current.getId());
        type.setText(current.getType().getText());
        timestamp.setText(needAtFormattedString);

        switch(current.getStatus().toLowerCase()) {
            case Constants.STATUS_NEW:
                statusTextView.setText(R.string.status_new);
                statusImage.setImageResource(R.drawable.drawable_request_status_new);
                break;
            case Constants.STATUS_IN_WORK:
                statusTextView.setText(R.string.status_in_work);
                statusImage.setImageResource(R.drawable.drawable_request_status_in_work);
                break;
            case Constants.STATUS_REJECTED:
                statusTextView.setText(R.string.status_rejected);
                statusImage.setImageResource(R.drawable.drawable_request_status_rejected);
                break;
            case Constants.STATUS_DONE:
                statusTextView.setText(R.string.status_done);
                statusImage.setImageResource(R.drawable.drawable_request_status_done);
                break;
            default:
                statusTextView.setText(current.getStatus());
        }
    }
}
