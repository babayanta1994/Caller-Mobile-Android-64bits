package ru.true_ip.trueip.app.quizzes_screen.quiz_screen.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.App;
import ru.true_ip.trueip.base.adapters.BindingRecyclerAdapter;
import ru.true_ip.trueip.databinding.ItemAnswerBinding;
import ru.true_ip.trueip.models.responses.QuizzModel;
import ru.true_ip.trueip.models.responses.QuizzesResultsModel;

/**
 * Created by ektitarev on 11.01.2018.
 */

public class AnswersResultsAdapter extends BindingRecyclerAdapter<QuizzesResultsModel.QuizResult.QuizAnswer> {

    private int votesCount;
    private QuizzModel quizzModel;

    public AnswersResultsAdapter(List<QuizzesResultsModel.QuizResult.QuizAnswer> items, int votesCount, QuizzModel quizzModel) {
        super(R.layout.item_answer, items);
        this.votesCount = votesCount;
        this.quizzModel = quizzModel;
    }

    @Override
    public void onBindViewHolder(BindingRecyclerAdapter.BindingHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        QuizzesResultsModel.QuizResult.QuizAnswer current = items.get(position);

        ItemAnswerBinding bind = (ItemAnswerBinding) holder.getBinding();

        TextView optionText = bind.getRoot().findViewById(R.id.option_text);
        TextView votesPercent = bind.getRoot().findViewById(R.id.votes_percent);
        ProgressBar votesProgress = bind.getRoot().findViewById(R.id.votes_progress);
        ImageView selectedAnswerPic = bind.getRoot().findViewById(R.id.selected_answer_pic);

        optionText.setText(current.getText());
        optionText.setTextColor(optionText.getResources().getColor(android.R.color.white));

        float percent = (float)current.getResults_count() / votesCount * 100;

        votesPercent.setText(String.format(App.getContext().getResources().getString(R.string.percent_template), (int)percent));
        votesProgress.setProgress((int)percent);

        if (isSelfAnswer(current.getId())) {
            selectedAnswerPic.setVisibility(View.VISIBLE);
        } else {
            selectedAnswerPic.setVisibility(View.GONE);
        }
    }

    private boolean isSelfAnswer(int id) {
        if (id == -1) {
            return quizzModel.getSelected_free() != null;
        }
        for (QuizzModel.AnswerModel item : quizzModel.getSelected_answer()) {
            if(item.getId() == id) {
                return true;
            }
        }
        return false;
    }
}
