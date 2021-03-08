package ru.true_ip.trueip.app.main_screen.photo_details_fragment.photo_page_fragment;

import android.content.Context;
import android.databinding.ObservableField;
import android.os.Bundle;

import ru.true_ip.trueip.base.BaseContract;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.utils.Constants;

/**
 * Created by ektitarev on 17.01.2018.
 */

public class PhotoPagePresenter extends BasePresenter<BaseContract> {

    public ObservableField<String> photoPath = new ObservableField<>("");

    private Context context;

    public void setContext(Context context) { this.context = context; }

    public void setExtras(Bundle extras) {
        photoPath.set(extras.getString(Constants.PHOTO_PATH, ""));
    }
}
