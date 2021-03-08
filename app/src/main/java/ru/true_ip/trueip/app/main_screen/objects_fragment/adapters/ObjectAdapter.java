package ru.true_ip.trueip.app.main_screen.objects_fragment.adapters;

import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import ru.true_ip.trueip.base.adapters.BindingRecyclerAdapter;
import ru.true_ip.trueip.databinding.ItemObjectBinding;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.repository.RepositoryController;

/**
 * Created by Eugen on 25.09.2017.
 */

public class ObjectAdapter extends BindingRecyclerAdapter<ObjectDb> {
    //private final static String TAG = ObjectAdapter.class.getSimpleName();
    private boolean isEditMode = false;
    private OnObjectActiveChangeListener mOnObjectActiveChangeListener;
    private RepositoryController repositoryController;

    public ObjectAdapter(@LayoutRes Integer holderLayout, List<ObjectDb> items, RepositoryController repo) {
        super(holderLayout, items);
        this.repositoryController = repo;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(final BindingRecyclerAdapter.BindingHolder holder, int position) {
        final ObjectDb objectModel = items.get(holder.getAdapterPosition());
        ItemObjectBinding bind = (ItemObjectBinding) holder.getBinding();

        if (isEditMode) {
            bind.chkIsActive.setVisibility(View.INVISIBLE);
            bind.editIcon.setVisibility(View.VISIBLE);
        } else {
            bind.editIcon.setVisibility(View.INVISIBLE);
            bind.chkIsActive.setVisibility(View.VISIBLE);
        }

        bind.objectEnabled.setChecked(objectModel.IsObjectActive());

        bind.chkIsActive.setChecked(objectModel.isStatusActive());

        bind.objectName.setText(objectModel.getName());
        bind.getRoot().setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(holder.getAdapterPosition(), objectModel);
            }
        });

        bind.objectEnabled.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            bind.objectEnabled.setEnabled(false);
            new Handler().postDelayed(() -> {
                //Logger.error(TAG,"Enabling element");
                bind.objectEnabled.setEnabled(true);
                },1500L);
            ObjectDb objectDb = items.get(position);
            objectDb.setObjectActive(isChecked);
            if ( !isChecked ) {
                bind.chkIsActive.setChecked(false);
                objectDb.setStatusActive(false);
            }
            repositoryController.setObjectActive(objectDb.object_id, objectDb.IsObjectActive() ? 1 : 0);
            if (mOnObjectActiveChangeListener != null) {
                mOnObjectActiveChangeListener.onObjectActiveChange(position);
            }
        }));
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        notifyDataSetChanged();
    }


    public void updateItemStatus(String userSipNumber, boolean isActive) {
        for (int i = 0; i < getItemCount(); i++) {
            String sipNumber = getAdapterItems().get(i).getIdUri();
            if (sipNumber != null && sipNumber.equals(userSipNumber)) {
                ObjectDb item = getAdapterItems().get(i);
                if (item != null && item.isStatusActive != isActive) { // && item.isStatusActive() != isActive) {
                    item.setStatusActive(isActive);
                    notifyItemChanged(i);
                }
                break;
            }
        }
//        notifyDataSetChanged();
    }

//    public void updateItemStatusByIndex(int index, boolean isActive) {
//        getAdapterItems().get(index).setStatusActive(isActive);
//        mRecyclerView.post(() -> notifyItemChanged(index) );
//    }

    public void addOnObjectActiveChangeListener(OnObjectActiveChangeListener listener) {
        this.mOnObjectActiveChangeListener = listener;
    }

    public interface OnObjectActiveChangeListener {
        void onObjectActiveChange(int position);
    }

}
