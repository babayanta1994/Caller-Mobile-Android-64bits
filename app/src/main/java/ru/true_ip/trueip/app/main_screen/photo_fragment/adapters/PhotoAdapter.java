package ru.true_ip.trueip.app.main_screen.photo_fragment.adapters;

import android.support.annotation.LayoutRes;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.adapters.BindingRecyclerAdapter;
import ru.true_ip.trueip.databinding.ItemPhotoBinding;
import ru.true_ip.trueip.models.PhotoModel;

/**
 * Created by Eugen on 25.09.2017.
 */

public class PhotoAdapter extends BindingRecyclerAdapter<PhotoModel> {

    public PhotoAdapter(@LayoutRes Integer holderLayout, List<PhotoModel> items) {
        super(holderLayout, items);
    }

    @Override
    public void onBindViewHolder(final BindingRecyclerAdapter.BindingHolder holder, final int position) {
        final PhotoModel photo = items.get(holder.getAdapterPosition());
        ItemPhotoBinding bind = (ItemPhotoBinding) holder.getBinding();
        Glide.with(bind.getRoot().getContext())
                .load(photo.path)
                .apply(new RequestOptions()
                        .centerCrop())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(bind.photo);
        ((TextView)bind.getRoot().findViewById(R.id.time_stamp)).setText(photo.timestamp);
        bind.getRoot().setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(holder.getAdapterPosition(), photo);
            }
        });
    }
}
