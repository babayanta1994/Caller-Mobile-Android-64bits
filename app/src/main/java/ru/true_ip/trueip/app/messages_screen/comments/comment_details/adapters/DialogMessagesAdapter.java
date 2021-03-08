package ru.true_ip.trueip.app.messages_screen.comments.comment_details.adapters;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.adapters.BindingRecyclerAdapter;
import ru.true_ip.trueip.databinding.ItemDialogMessageBinding;
import ru.true_ip.trueip.models.responses.AnswerModel;

/**
 * Created by ektitarev on 14/01/2019.
 *
 */

public class DialogMessagesAdapter extends BindingRecyclerAdapter<AnswerModel> {
    public DialogMessagesAdapter() {
        super(R.layout.item_dialog_message, new ArrayList<>());
    }

    public DialogMessagesAdapter(List<AnswerModel> items) {
        super(R.layout.item_dialog_message, items);
    }

    @Override
    public void onBindViewHolder(BindingRecyclerAdapter.BindingHolder holder, int position) {

        ItemDialogMessageBinding binding = (ItemDialogMessageBinding) holder.getBinding();

        AnswerModel currentItem = items.get(holder.getAdapterPosition());

        View questionContainer = binding.getRoot().findViewById(R.id.question_container);
        View answerContainer = binding.getRoot().findViewById(R.id.answer_container);
        TextView commentQuestion = binding.getRoot().findViewById(R.id.comment_question);
        TextView commentAnswer = binding.getRoot().findViewById(R.id.comment_answer);

        if (currentItem.getIsConsierge() == 1) {
            questionContainer.setVisibility(View.GONE);
            answerContainer.setVisibility(View.VISIBLE);

            commentAnswer.setText(currentItem.text);
        } else {
            questionContainer.setVisibility(View.VISIBLE);
            answerContainer.setVisibility(View.GONE);

            commentQuestion.setText(currentItem.text);
        }
    }

    public void addItem (AnswerModel item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void insertItem (AnswerModel item, int pos) {
        items.add(pos, item);
        notifyItemInserted(pos);
    }

    public void updateItem (AnswerModel item, int pos) {
        items.set(pos, item);
        notifyItemChanged(pos);
    }

    public void removeItem (int pos) {
        items.remove(pos);
        notifyItemRemoved(pos);
    }

    public void addAllItems(List<AnswerModel> items) {
        if (items != null && !items.isEmpty()) {
            this.items.addAll(items);
            notifyItemRangeInserted(this.items.size() - items.size(), items.size());
        }
    }

    public void insertAllItems(List<AnswerModel> items, int pos) {
        if (items != null && !items.isEmpty()) {
            this.items.addAll(pos, items);
            notifyItemRangeInserted(pos, items.size());
        }
    }

    public void sortMessages(Comparator<AnswerModel> comparator) {
        Collections.sort(items, comparator);
        notifyDataSetChanged();
    }

    public void insertAllItemsToBegin(List<AnswerModel> items) {
        insertAllItems(items, 0);
    }
}
