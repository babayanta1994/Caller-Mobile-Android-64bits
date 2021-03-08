package ru.true_ip.trueip.app.messages_screen.comments;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.App;
import ru.true_ip.trueip.base.adapters.BindingRecyclerAdapter;
import ru.true_ip.trueip.databinding.ItemCommentBinding;
import ru.true_ip.trueip.models.responses.CommentModel;

/**
 *
 * Created by Andrey Filimonov on 11.01.2018.
 */

public class CommentsAdapter extends BindingRecyclerAdapter<CommentModel> {

    public CommentsAdapter(List<CommentModel> items) {
        super(R.layout.item_comment, items);
    }
    public CommentsAdapter(@LayoutRes Integer holderLayout, List<CommentModel> items) {
        super(holderLayout, items);
    }

    @Override
    public void onBindViewHolder(BindingRecyclerAdapter.BindingHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        CommentModel comment = items.get(position);

        ItemCommentBinding binding = (ItemCommentBinding) holder.getBinding();

        TextView date = binding.getRoot().findViewById(R.id.comment_item_date);
        date.setText(comment.getUpdatedAt());

        TextView text = binding.getRoot().findViewById(R.id.comment_item_text);
        text.setText(comment.getText());

        TextView status = binding.getRoot().findViewById(R.id.comment_item_status);
        TextView statusString = binding.getRoot().findViewById(R.id.comment_item_status_string);

        if ( comment.getIsViewed() == 0 ) {
            status.setBackground(App.getContext().getResources().getDrawable(R.drawable.round_background));
            statusString.setText(App.getContext().getString(R.string.text_not_viewed));
        } else if (comment.getAnswer() == null ) {
            //Viewed but no answer
            status.setBackground(App.getContext().getResources().getDrawable(R.drawable.round_background_green));
            statusString.setText(App.getContext().getString(R.string.text_viewed));
        } else {
            status.setBackground(App.getContext().getResources().getDrawable(R.drawable.round_background_green));
            statusString.setText(App.getContext().getString(R.string.text_viewed_and_has_answer));
        }
    }
}
