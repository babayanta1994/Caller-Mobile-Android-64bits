package ru.true_ip.trueip.base.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class BindingRecyclerAdapter<T> extends RecyclerView.Adapter<BindingRecyclerAdapter.BindingHolder> {

    private Integer holderLayout;
    private Integer variableId;

    protected List<T> items = new ArrayList<>();
    protected OnItemClickListener<T> mOnItemClickListener;

    public BindingRecyclerAdapter() {
        this(null);
    }

    public BindingRecyclerAdapter(List<T> items) {
        this(null, items);
    }

    public BindingRecyclerAdapter(Integer holderLayout, List<T> items) {
        this(holderLayout, items, null);
    }

    public BindingRecyclerAdapter(Integer holderLayout, List<T> items, Integer variableId) {
        this.holderLayout = holderLayout;
        this.items = items;
        this.variableId = variableId;
    }

    public void setItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public List<T> getAdapterItems() {
        return items;
    }

    @Override
    public BindingRecyclerAdapter.BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (holderLayout == null) {
            throw new RuntimeException("Please, attach item layout");
        }
        View v = LayoutInflater.from(parent.getContext())
                .inflate(holderLayout, parent, false);
        return new BindingHolder(v);
    }

    public void deleteItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(final BindingRecyclerAdapter.BindingHolder holder, final int position) {
        final T item = items.get(position);
        holder.getBinding().getRoot().setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(holder.getAdapterPosition(), item);
            }
        });
        if (variableId != null) {
            holder.getBinding().setVariable(variableId, item);
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void addOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public class BindingHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding mBinding;

        public BindingHolder(View v) {
            super(v);
            mBinding = DataBindingUtil.bind(v);
        }

        public BindingHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public ViewDataBinding getBinding() {
            return mBinding;
        }

    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T item);
    }

}