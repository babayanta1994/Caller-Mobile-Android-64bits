package ru.true_ip.trueip.app.quizzes_screen.quizzes_fragment.adapters;

import android.support.annotation.LayoutRes;
import android.view.View;
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
import ru.true_ip.trueip.databinding.ItemQuizBinding;
import ru.true_ip.trueip.models.responses.QuizzModel;

/**
 * Created by ektitarev on 27.12.2017.
 */

public class QuizzesAdapter extends BindingRecyclerAdapter<QuizzModel> {

    public QuizzesAdapter(@LayoutRes Integer holderLayout, List<QuizzModel> items) {
        super(holderLayout, items);
    }

    @Override
    public void onBindViewHolder(BindingRecyclerAdapter.BindingHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        QuizzModel current = items.get(position);

        ItemQuizBinding bind = (ItemQuizBinding) holder.getBinding();

        TextView timestamp = bind.getRoot().findViewById(R.id.timestamp);
        TextView description = bind.getRoot().findViewById(R.id.description);
        ImageView quizPic = bind.getRoot().findViewById(R.id.quiz_pic);

        bind.getRoot().setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(holder.getAdapterPosition(), items.get(holder.getAdapterPosition()));
            }
        });

        String date = "";

        try {
            SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat resultFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());

            String actualDate = current.getStart_at();
            if (actualDate != null && !actualDate.isEmpty()) {
                Date sourceDate = sourceFormat.parse(actualDate);
                date = resultFormat.format(sourceDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date.isEmpty()) {
            date = App.getContext().getResources().getString(R.string.unknown_date);
        }

        timestamp.setText(date);
        description.setText(current.getTitle());
        if (current.isIs_viewed() == 0) {
            quizPic.setVisibility(View.VISIBLE);
        } else {
            quizPic.setVisibility(View.GONE);
        }
    }
}
