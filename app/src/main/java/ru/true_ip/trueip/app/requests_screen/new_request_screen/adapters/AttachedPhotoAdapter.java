package ru.true_ip.trueip.app.requests_screen.new_request_screen.adapters;

import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.adapters.BindingRecyclerAdapter;
import ru.true_ip.trueip.databinding.ItemAttachedPhotoBinding;
import ru.true_ip.trueip.models.PhotoModel;
import ru.true_ip.trueip.utils.CustomGlideModule;
import ru.true_ip.trueip.utils.GlideApp;

/**
 * Created by ektitarev on 19.01.2018.
 *
 */

public class AttachedPhotoAdapter extends BindingRecyclerAdapter<PhotoModel> {

    public interface OnRemoveClickListener {
        void onRemove(int position, PhotoModel item);
    }

    private OnRemoveClickListener mOnRemoveClickListener = null;

    public AttachedPhotoAdapter(List<PhotoModel> images) {
        super(R.layout.item_attached_photo, images);
    }

    public void setOnRemoveClickListener(OnRemoveClickListener listener) {
        mOnRemoveClickListener = listener;
    }

    @Override
    public void onBindViewHolder(BindingRecyclerAdapter.BindingHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        PhotoModel current = items.get(position);

        ItemAttachedPhotoBinding bind = (ItemAttachedPhotoBinding)holder.getBinding();

        ImageView attachedImage = bind.getRoot().findViewById(R.id.attached_image);
        ImageButton removeButton = bind.getRoot().findViewById(R.id.remove_button);

        GlideApp.with(attachedImage.getContext())
                .load(current.path)
                .into(attachedImage);

        removeButton.setOnClickListener(view -> {
            if (mOnRemoveClickListener != null) {
                mOnRemoveClickListener.onRemove(holder.getAdapterPosition(), getAdapterItems().get(holder.getAdapterPosition()));
            }
        });
        removeButton.setVisibility(current.removable ? View.VISIBLE : View.GONE);
    }

    public void addItem(PhotoModel item) {
        items.add(item);

        notifyItemInserted(items.size() - 1);
    }

    public void removeItem(int pos) {
        if (pos < items.size()) {
            items.remove(pos);

            notifyItemRemoved(pos);
        }
    }
}
