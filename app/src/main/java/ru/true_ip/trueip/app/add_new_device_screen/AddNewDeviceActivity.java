package ru.true_ip.trueip.app.add_new_device_screen;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import pl.aprilapps.easyphotopicker.EasyImage;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BaseActivity;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.ActivityAddNewDeviceBinding;
import ru.true_ip.trueip.utils.DialogHelper;


public class AddNewDeviceActivity extends BaseActivity<AddNewDeviceContract, AddNewDevicePresenter, ActivityAddNewDeviceBinding> implements AddNewDeviceContract {

    public static final int REQUEST_CODE_TAKE_PHOTO = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setContext(this);
        presenter.handelBundle(getIntent().getExtras());
    }

    @Override
    public ActivityAddNewDeviceBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_add_new_device, null, false);
    }

    @Override
    public AddNewDeviceContract getContract() {
        return this;
    }

    @Override
    public AddNewDevicePresenter createPresenter() {
        return new AddNewDevicePresenter();
    }

    @Override
    public BaseRouter createRouter() {
        return new AddNewDeviceRouter(this);
    }

    @Override
    public void showPreloader() {

    }

    @Override
    public void hidePreloader() {

    }


    @Override
    public BaseRouter getRouter() {
        return router;
    }

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, AddNewDeviceActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void pickPhoto() {
        checkPermissionsAndOpenPhotoPicker();
    }

    @Override
    public void setObjectPhoto(File file) {
        Glide.with(this).load(file)
                .apply(new RequestOptions()
                        .centerCrop())
                .into(binding.objectPhoto);
    }

    @Override
    public void setObjectPhoto(Bitmap bitmap) {
        binding.objectPhoto.setImageBitmap(bitmap);
    }

    @Override
    public void showDialog() {
        DialogHelper.createExplanationDialog(this, R.string.text_need_enter_all_settings);
    }

    @Override
    public void setImeOptions(Integer editTextAction) {
        binding.etDftm1.setImeOptions(editTextAction);
    }

    @Override
    public void setDefaultPhoto() {
        binding.objectPhoto.setImageResource(android.R.color.transparent);
    }

    private void checkPermissionsAndOpenPhotoPicker() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, REQUEST_CODE_TAKE_PHOTO);

        } else {
            openPicker();
        }
    }

    private void openPicker() {
        EasyImage.openChooserWithGallery(this, "Pick your image", 0);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_TAKE_PHOTO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openPicker();
                } else {
                    //TODO open alert dialog to explain what to do next
//                    new AlertDialog.Builder(getActivity())
//                            .setTitle(R.string.text_attention)
//                            .setMessage(R.string.text_permissions_explanation)
//                            .setPositiveButton(R.string.text_allow_permissions, (dialog, which) -> startActivity(
//                                    new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                                            Uri.parse(Constants.PREFIX_PACKAGE + BuildConfig.APPLICATION_ID))))
//                            .setNegativeButton(R.string.text_close, ((dialog, which) -> dialog.dismiss()))
//                            .create()
//                            .show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult(this, requestCode, resultCode, data);
    }

    public enum DeviceType {
        PANEL_DEVICE,
        CAMERA_DEVICE
    }
}
