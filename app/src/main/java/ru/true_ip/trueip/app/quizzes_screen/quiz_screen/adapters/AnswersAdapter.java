package ru.true_ip.trueip.app.quizzes_screen.quiz_screen.adapters;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.adapters.BindingRecyclerAdapter;
import ru.true_ip.trueip.databinding.ItemAnswerBinding;
import ru.true_ip.trueip.models.responses.QuizzModel;

/**
 * Created by ektitarev on 10.01.2018.
 */

public class AnswersAdapter extends BindingRecyclerAdapter<QuizzModel.AnswerModel> {
    public AnswersAdapter(List<QuizzModel.AnswerModel> items) {
        super(R.layout.item_answer, items);
    }

    @Override
    public void onBindViewHolder(BindingRecyclerAdapter.BindingHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        QuizzModel.AnswerModel current = items.get(position);

        ItemAnswerBinding bind = (ItemAnswerBinding)holder.getBinding();

        TextView optionText = bind.getRoot().findViewById(R.id.option_text);
        TextView votesPercent = bind.getRoot().findViewById(R.id.votes_percent);
        ProgressBar votesProgress = bind.getRoot().findViewById(R.id.votes_progress);
        ImageView selectedAnswerPic = bind.getRoot().findViewById(R.id.selected_answer_pic);

        bind.getRoot().setOnClickListener(view -> {
            if(mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(position, current);
            }
        });

        if(current.isSelected()) {
            selectedAnswerPic.setVisibility(View.VISIBLE);
        } else {
            selectedAnswerPic.setVisibility(View.GONE);
        }

        optionText.setText(current.getText());

        votesProgress.setVisibility(View.GONE);
        votesPercent.setVisibility(View.GONE);
    }

    public void updateItem(int position, QuizzModel.AnswerModel item) {
        items.set(position, item);
        notifyItemChanged(position);
    }
}
