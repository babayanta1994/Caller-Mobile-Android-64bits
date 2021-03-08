package ru.true_ip.trueip.app.main_screen.photo_details_activity;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.main_screen.photo_details_activity.adapters.PhotoPageAdapter;
import ru.true_ip.trueip.base.BaseContract;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.models.PhotoModel;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;

/**
 * Created by ektitarev on 18.01.2018.
 */

public class PhotoDetailsPresenter extends BasePresenter<BaseContract> {
    public ObservableField<String> photoTimestamp = new ObservableField<>("");

    public int currentPosition;

    public ObservableField<PhotoPageAdapter> viewPagerAdapter = new ObservableField<>();

    private Context context;
    private ArrayList<PhotoModel> photos;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setExtras(Bundle extras) {
        photos = extras.getParcelableArrayList(Constants.PHOTO_ITEMS);
        currentPosition = extras.getInt(Constants.PHOTO_CURRENT_ITEM);
    }

    public void createViewPager() {
        PhotoPageAdapter adapter = new PhotoPageAdapter(((FragmentActivity)context).getSupportFragmentManager(), photos);

        viewPagerAdapter.set(adapter);
        adapter.notifyDataSetChanged();
    }

    public void onClick(View view) {
        Context context = view.getContext();

        switch (view.getId()) {
            case R.id.btn_delete:
                DialogHelper.createOptionsDialog(
                        context,
                        R.string.text_delete_dialog_title,
                        R.string.text_delete_snapshot,
                        new DialogHelper.DialogOption(context.getString(R.string.text_yes)) {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String photoPath = photos.get(currentPosition).path;

                                File file = new File(photoPath);
                                if (file.exists()) {
                                    if(file.delete()) {
                                        BaseContract contract = getContract();
                                        if (contract != null) {
                                            contract.getRouter().moveBackward();
                                        }
                                    } else {
                                        Toast.makeText(context, R.string.text_error_cant_delete_file, Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    DialogHelper.createErrorDialog(context,
                                            context.getString(R.string.text_error_dialog_title),
                                            context.getString(R.string.text_error_file_not_found));
                                }
                            }
                        },

                        new DialogHelper.DialogOption(context.getString(R.string.text_cancel)) {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );
                break;
        }
    }
}
