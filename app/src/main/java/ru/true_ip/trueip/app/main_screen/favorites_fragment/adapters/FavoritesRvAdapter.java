package ru.true_ip.trueip.app.main_screen.favorites_fragment.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.App;
import ru.true_ip.trueip.base.adapters.BindingRecyclerAdapter;
import ru.true_ip.trueip.databinding.ItemDeviceBinding;
import ru.true_ip.trueip.db.entity.DevicesDb;
import ru.true_ip.trueip.utils.Utils;

public class FavoritesRvAdapter extends BindingRecyclerAdapter<DevicesDb> {
    private final static String TAG = FavoritesRvAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<Integer> cellColors = null;

    public FavoritesRvAdapter(@LayoutRes Integer holderLayout, List<DevicesDb> items) {
        super(holderLayout, items);
        this.context = App.getContext();
    }

    public void init() {
        if (cellColors == null) {
            cellColors = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                cellColors.add(R.color.color_indicator_inactive);
            }
            //Logger.error(TAG, "Created colors array with size " + cellColors.size());
        }
    }

    @Override
    public void onBindViewHolder(final BindingRecyclerAdapter.BindingHolder holder, final int position) {
        final DevicesDb devicesDb = items.get(holder.getAdapterPosition());
        ItemDeviceBinding bind = (ItemDeviceBinding) holder.getBinding();
        bind.name.setText(devicesDb.getName());

        if (devicesDb.getImage() != null && !devicesDb.getImage().isEmpty()) {
            bind.iconDefault.setVisibility(View.INVISIBLE);
            bind.iconDevice.setImageBitmap(Utils.convertBase64StringToImage(devicesDb.getImage()));
        } else {
            bind.iconDefault.setImageResource(devicesDb.getDevice_type() == 1 ? R.drawable.panel : R.drawable.camera);
        }

        bind.iconEditMode.setVisibility(View.GONE);

        if (cellColors.size() != 0)
            bind.indicator.setBackgroundColor(App.getContext().getResources().getColor(cellColors.get(position)));

        bind.getRoot().setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(holder.getAdapterPosition(), devicesDb);
            }
        });
    }

    public void setCellColors(ArrayList<Integer> colors) {
        //Logger.error(TAG, "Cellcolors set. Size = " + colors.size());
        cellColors = colors;
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }
}
