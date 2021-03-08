package ru.true_ip.trueip.app.add_new_device_screen;

import android.graphics.Bitmap;

import java.io.File;

import ru.true_ip.trueip.base.BaseContract;

/**
 * Created by Eugen on 09.10.2017.
 */

public interface AddNewDeviceContract extends BaseContract {

    void pickPhoto();

    void setObjectPhoto(File file);

    void setObjectPhoto(Bitmap bitmap);

    void showDialog();

    void setImeOptions(Integer editTextAction);

    void setDefaultPhoto();
}
