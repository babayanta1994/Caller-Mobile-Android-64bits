package ru.true_ip.trueip.app.object_screen.cameras_fragment.adapters;

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

/**
 * Created by Eugen on 25.09.2017.
 */

public class CamerasAdapter extends BindingRecyclerAdapter<DevicesDb> {
    private final static String TAG = "CamerasAdapter";

    private ArrayList<Integer> cellColors = null;
    private boolean editMode;

    public CamerasAdapter(@LayoutRes Integer holderLayout, List<DevicesDb> items) {
        super(holderLayout, items);
    }

    public void init() {
        if (cellColors == null) {
            cellColors = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                cellColors.add(R.color.color_indicator_inactive);
            }
            //Logger.error(TAG, "Created colors array with size" + cellColors.size());
        }
    }

    @Override
    public void onBindViewHolder(final BindingRecyclerAdapter.BindingHolder holder, final int position) {
        //Logger.error(TAG, "Camera position = " + position);
        final DevicesDb camera = items.get(holder.getAdapterPosition());
        ItemDeviceBinding bind = (ItemDeviceBinding) holder.getBinding();
        bind.name.setText(camera.getName());

        if (isEditMode()) {
            bind.iconDefault.setVisibility(View.INVISIBLE);
            bind.iconDevice.setVisibility(View.INVISIBLE);
            bind.iconEditMode.setVisibility(View.VISIBLE);
        } else {
            bind.iconEditMode.setVisibility(View.INVISIBLE);
            if (camera.getImage() != null && !camera.getImage().isEmpty()) {
                bind.iconDefault.setVisibility(View.INVISIBLE);
                bind.iconDevice.setVisibility(View.VISIBLE);
                bind.iconDevice.setImageBitmap(Utils.convertBase64StringToImage(camera.getImage()));
            } else {
                bind.iconDefault.setImageResource(R.drawable.camera);
                bind.iconDefault.setVisibility(View.VISIBLE);
                bind.iconDevice.setVisibility(View.INVISIBLE);
            }
        }

        bind.getRoot().setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(holder.getAdapterPosition(), camera);
            }
        });
        if ( cellColors.size() != 0) {
            //Logger.error(TAG, "Setting color to cell " + position);
            try {
                bind.indicator.setBackgroundColor(App.getContext().getResources().getColor(cellColors.get(position)));
            } catch (IndexOutOfBoundsException e) {
                cellColors.add((R.color.color_indicator_inactive));
                bind.indicator.setBackgroundColor(App.getContext().getResources().getColor(cellColors.get(position)));
            }
        }
    }

    public void setCellColors(ArrayList<Integer> colors) {
        //Logger.error(TAG, "Cellcolors set. Size = " + colors.size());
        cellColors = colors;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        notifyDataSetChanged();
    }

    public boolean isEditMode() {
        return editMode;
    }
}
