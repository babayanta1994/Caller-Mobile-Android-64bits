package ru.true_ip.trueip.app.device_screen.adapters;

import android.support.annotation.LayoutRes;

import java.util.List;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.adapters.BindingRecyclerAdapter;
import ru.true_ip.trueip.databinding.ItemDeviceListBinding;
import ru.true_ip.trueip.db.entity.DevicesDb;
import ru.true_ip.trueip.utils.Constants;

public class DeviceListAdapter extends BindingRecyclerAdapter<DevicesDb> {

    public DeviceListAdapter(@LayoutRes Integer holderLayout, List<DevicesDb> items) {
        super(holderLayout, items);
    }

    @Override
    public void onBindViewHolder(final BindingRecyclerAdapter.BindingHolder holder, int position) {
        final DevicesDb deviceDb = items.get(holder.getAdapterPosition());
        ItemDeviceListBinding bind = (ItemDeviceListBinding) holder.getBinding();
        bind.deviceName.setText(deviceDb.getName());
        if (position == getItemCount() - 1) {
            bind.itemContainer.setBackgroundResource(R.drawable.background_rounded_bottom_white);
        } else {
            bind.itemContainer.setBackgroundResource(R.drawable.background_bottom_border);
        }
        bind.deviceIcon.setImageResource(deviceDb.getDevice_type() == Constants.TYPE_CAMERA ? R.drawable.ic_camera_small : R.drawable.ic_panel_small);
        /*bind.deviceIcon.setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(holder.getAdapterPosition(), deviceDb);
            }
        });
        bind.deviceName.setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(holder.getAdapterPosition(), deviceDb);
            }
        });*/
        bind.deviceLayout.setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(holder.getAdapterPosition(), deviceDb);
            }
        });
    }
}