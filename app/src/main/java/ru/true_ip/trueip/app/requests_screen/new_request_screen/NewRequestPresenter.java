package ru.true_ip.trueip.app.requests_screen.new_request_screen;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Response;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.requests_screen.new_request_screen.adapters.AttachedPhotoAdapter;
import ru.true_ip.trueip.base.BaseContract;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.Callback;
import ru.true_ip.trueip.models.PhotoModel;
import ru.true_ip.trueip.models.responses.ClaimModel;
import ru.true_ip.trueip.models.responses.ErrorApiResponse;
import ru.true_ip.trueip.models.responses.TypeModel;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;
import ru.true_ip.trueip.utils.Utils;

/**
 *
 * Created by Andrey Filimonov on 28.12.2017.
 */

public class NewRequestPresenter extends BasePresenter<NewRequestContract> implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private final static String TAG = NewRequestPresenter.class.getSimpleName();

    public static final int REQUEST_CODE_TAKE_PHOTO = 1001;
    private Context context;
    private int objectId = -1;
    private ArrayList<TypeModel> claimTypes = new ArrayList<>();
    private ArrayList<String> claimTypeNames = new ArrayList<>();
    private AppCompatSpinner spinner;
    public ObservableField<String> reason = new ObservableField<>("");
    public ObservableField<String> dateTime = new ObservableField<>("");
    public ObservableField<String> phoneNumber = new ObservableField<>("");
    public ObservableField<String> title = new ObservableField<>("");
    public ObservableField<AttachedPhotoAdapter> attachedPhotoAdapter = new ObservableField<>(new AttachedPhotoAdapter(new ArrayList<>()));
    public ObservableBoolean isEditMode = new ObservableBoolean(true);
    public ObservableBoolean isViewMode = new ObservableBoolean(false);
    public ObservableBoolean canAddPhotos = new ObservableBoolean(true);
    public ClaimModel claimModel;

    private FragmentManager fragmentManager;

    private Toast toast;

    private String time;
    private boolean isDateOk = false;
    private boolean isTimeOk = false;
    private boolean isInProgress = false;
    private GregorianCalendar calendar_at;
    private String baseUrl;

    public void setContext(Context context) {
        this.context = context;
        dateTime.set(context.getString(R.string.text_not_set));
        AttachedPhotoAdapter adapter = attachedPhotoAdapter.get();
        if (adapter != null) {
            adapter.setOnRemoveClickListener(((position, item) -> {
                adapter.removeItem(position);
                canAddPhotos.set(adapter.getItemCount() < Constants.MAX_PHOTOS_COUNT);
                if (canAddPhotos.get() && toast != null && toast.getView().isShown()) {
                    toast.cancel();
                }
            }));
        }
    }

    void setSpinner(AppCompatSpinner spinner) {
        this.spinner = spinner;
    }

    void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setExtras(Bundle extras) {
        title.set(context.getString(R.string.text_new_request));

        objectId = extras.getInt(Constants.OBJECT_ID, 0);
        baseUrl = extras.getString(Constants.BASE_URL);
        if(extras.containsKey(Constants.REQUEST_OBJECT)) {
            isViewMode.set(true);

            title.set(context.getString(R.string.text_view_request));
            isEditMode.set(extras.getBoolean(Constants.REQUEST_EDITABLE, true));
            spinner.setEnabled(extras.getBoolean(Constants.REQUEST_EDITABLE, true));

            claimModel = extras.getParcelable(Constants.REQUEST_OBJECT);

            if (claimModel != null) {
                canAddPhotos.set(claimModel.getImages().size() < Constants.MAX_PHOTOS_COUNT);

                dateTime.set(claimModel.getNeed_at());
                phoneNumber.set(claimModel.getPhone());
                if (claimModel.getClaim_text() != null) {
                    reason.set(claimModel.getClaim_text());
                }

                loadPhotos();
            }

            checkDate();
        }
    }

    private void loadPhotos() {
        if (claimModel != null) {
            List<ClaimModel.PhotoUrl> photos = claimModel.getImages();

            if (photos != null && !photos.isEmpty()) {

                AttachedPhotoAdapter adapter = attachedPhotoAdapter.get();
                if (adapter != null) {
                    for (ClaimModel.PhotoUrl photoUrl : photos) {
                        String photoFullUrl = StringUtils.strip(baseUrl, File.separator) + File.separator + StringUtils.stripStart(photoUrl.getImage_url(), File.separator);

                        PhotoModel photo = new PhotoModel(photoFullUrl, 0);
                        photo.removable = false;

                        adapter.addItem(photo);
                    }
                }
            }
        }
    }

    private void checkDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        try {
            Date date = format.parse(claimModel.getNeed_at());
            Calendar now = Calendar.getInstance();
            Calendar needAt = Calendar.getInstance();

            needAt.setTime(date);

            if (now.before(needAt)) {
                isDateOk = true;
                isTimeOk = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //
    //loadClaimTypes
    //
    void loadClaimTypes() {
        repositoryController.getObject(objectId).subscribe(objectDb ->
                apiController.getClaimTypes(context, objectDb, new Callback<Response<ArrayList<TypeModel>>>() {
                    @Override
                    public void onSuccess(Response<ArrayList<TypeModel>> response) {
                        super.onSuccess(response);
                        if (response.isSuccessful()) {
                            ArrayList<TypeModel> responseBody = response.body();
                            if (responseBody != null) {
                                claimTypes = responseBody;
                                setClaimTypes();
                            }
                        } else {
                            ErrorApiResponse error = new ErrorApiResponse(response.errorBody());
                            DialogHelper.createErrorDialog(context,
                                    context.getResources().getString(R.string.text_error_dialog_title),
                                    error.getError());
                        }
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        DialogHelper.createErrorDialog(context,
                                context.getResources().getString(R.string.text_error_dialog_title),
                                context.getResources().getString(R.string.text_error_default_message));
                    }
                }));
    }

    //
    //setClaimTypes
    //
    private void setClaimTypes() {
        int indexToSelect = 0;
        for (int i = 0; i < claimTypes.size(); ++i) {
            claimTypeNames.add(claimTypes.get(i).getText());
            if (claimModel != null && claimTypes.get(i).getId() == claimModel.getType().getId()) {
                indexToSelect = i;
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.object_name_item, claimTypeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(indexToSelect);
    }

    //
    //onRequestDateClicked
    //
    public void onRequestDateClicked(View v) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(fragmentManager, "Datepickerdialog");
    }

    private void selectTime() {
        //Logger.error(TAG, "Select Timer called");
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, true);
        timePickerDialog.show(fragmentManager, "Timepickerdialog");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
    //
    //onDateSet
    //
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        isDateOk = false;
        Calendar now = Calendar.getInstance();
        Calendar selectedDate = Calendar.getInstance();

        selectedDate.set(Calendar.YEAR, year);
        selectedDate.set(Calendar.MONTH, monthOfYear);
        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if (selectedDate.before(now)) {
            return;
        }

        isDateOk = true;
        calendar_at = new GregorianCalendar();
        calendar_at.set(year, monthOfYear, dayOfMonth);
        selectTime();
    }
    //
    //onTimeSet
    //
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        isTimeOk = false;
        if (!isDateOk)
            return;
        Calendar now = Calendar.getInstance();
        Calendar selectedDate = calendar_at;

        selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        if (selectedDate.before(now)) {
            return;
        }
        selectedDate.set(Calendar.MINUTE, minute);
        selectedDate.set(Calendar.SECOND, second);

        isTimeOk = true;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        time = dateFormat.format(selectedDate.getTime());

        //Logger.error(TAG, time);
        dateTime.set(time);
    }
    //
    //onSendClicked
    //
    public void onSendClicked(View v) {

        if ( !isTimeOk || !isDateOk) {
            Toast.makeText(context, context.getString(R.string.text_set_date_time), Toast.LENGTH_SHORT).show();
            return;
        }

        if ( phoneNumber.get() != null && phoneNumber.get().isEmpty()) {
            Toast.makeText(context, context.getString(R.string.text_set_phone_number), Toast.LENGTH_SHORT).show();
            return;
        }

        if (isInProgress) {
            return;
        }

        // preventing multiple requests to api and possible crashes
        isInProgress = true;
        showProgressBar();

        HashMap<String, Object> body = new HashMap<>();
        body.put("need_at", dateTime.get());
        body.put("type", getSelectedType());
        body.put("phone", phoneNumber.get());
        body.put("claim_text", reason.get());

        Callback<Response<ClaimModel>> callbacks = new Callback<Response<ClaimModel>>() {
            @Override
            public void onSuccess(Response<ClaimModel> response) {
                if (response.isSuccessful()) {
                    claimModel = response.body();
                    uploadImages(claimModel);
                } else {
                    ErrorApiResponse error = new ErrorApiResponse(response.errorBody());
                    String msg = Utils.getDetailedErrorMessage(error);

                    if (!msg.isEmpty()) {
                        DialogHelper.createErrorDialog(context,
                                context.getResources().getString(R.string.text_error_dialog_title),
                                msg);
                    } else {
                        DialogHelper.createErrorDialog(context,
                                context.getResources().getString(R.string.text_error_dialog_title),
                                context.getResources().getString(R.string.text_error_default_message));
                    }

                    hideProgressBar();
                    isInProgress = false;
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                DialogHelper.createErrorDialog(context,
                        context.getResources().getString(R.string.text_error_dialog_title),
                        context.getResources().getString(R.string.text_error_default_message));

                hideProgressBar();
                isInProgress = false;
            }
        };

        repositoryController.getObject(objectId).subscribe(objectDb -> {
            if (isViewMode.get() && claimModel != null) {
                apiController.updateClaim(context, objectDb, claimModel.getId(), body, callbacks);
            } else {
                if (claimModel == null) {
                    apiController.postClaim(context, objectDb, body, callbacks);
                } else {
                    uploadImages(claimModel);
                }
            }
        });
    }

    private void uploadImages(ClaimModel receivedModel) {
        AttachedPhotoAdapter adapter = attachedPhotoAdapter.get();

        if (adapter != null && adapter.getItemCount() > 0 && receivedModel != null) {
            List<PhotoModel> photosToSend = new ArrayList<>();

            for (PhotoModel item : adapter.getAdapterItems()) {
                if (item.removable) {
                    photosToSend.add(item);
                }
            }

            if (!photosToSend.isEmpty()) {
                sendUploadImagesRequest(receivedModel.getId(), photosToSend);
            } else {
                NewRequestContract contract = getContract();
                if (contract != null) {
                    hideProgressBar();
                    ((NewRequestRouter)contract.getRouter()).moveBackward(2);
                }

                isInProgress = false;
            }

        } else {
            NewRequestContract contract = getContract();
            if (contract != null) {
                hideProgressBar();
                ((NewRequestRouter)contract.getRouter()).moveBackward(2);
            }
            isInProgress = false;
        }
    }

    //
    //getSelectedType
    //
    private int getSelectedType() {
        for (TypeModel type: claimTypes) {
            if ( type.getText().equalsIgnoreCase(claimTypeNames.get(spinner.getSelectedItemPosition()))) {
                return type.getId();
            }
        }
        return -1;
    }

    private void sendUploadImagesRequest(String claimId, List<PhotoModel> photosToSend) {
        repositoryController.getObject(objectId).subscribe(objectDb ->
            apiController.uploadImageFiles(context, objectDb, claimId, photosToSend, new Callback<Response<ClaimModel>>() {
                @Override
                public void onSuccess(Response<ClaimModel> uploadResponse) {
                    super.onSuccess(uploadResponse);
                    if (uploadResponse.isSuccessful()) {
                        NewRequestContract contract = getContract();
                        if (contract != null) {
                            hideProgressBar();
                            ((NewRequestRouter) contract.getRouter()).moveBackward(2);
                        }
                    } else {
                        ErrorApiResponse error = new ErrorApiResponse(uploadResponse.errorBody());
                        String msg = Utils.getDetailedErrorMessage(error);

                        if (!msg.isEmpty()) {
                            DialogHelper.createErrorDialog(context,
                                    context.getResources().getString(R.string.text_error_dialog_title),
                                    msg);
                        } else {
                            DialogHelper.createErrorDialog(context,
                                    context.getResources().getString(R.string.text_error_dialog_title),
                                    context.getResources().getString(R.string.text_error_default_message));
                        }
                    }

                    hideProgressBar();
                    isInProgress = false;
                }

                @Override
                public void onError(String error) {
                    super.onError(error);
                    DialogHelper.createErrorDialog(context,
                            context.getResources().getString(R.string.text_error_dialog_title),
                            context.getResources().getString(R.string.text_error_default_message));

                    hideProgressBar();
                    isInProgress = false;
                }
            }));
    }

    public void onClickAttach (View v) {
        if (canAddPhotos.get()) {
            getContract().checkPermissions();
        } else {
            showLimitMessage();
        }
    }

    public void openChooserWithGallery(Activity activity) {
        EasyImage.openChooserWithGallery(activity, "", 0);
    }

    private void showLimitMessage() {
        if (toast != null && toast.getView().isShown()) {
            toast.setText(R.string.images_limit_text);
        } else {
            toast = Toast.makeText(context, R.string.images_limit_text, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void showProgressBar() {
        BaseContract contract = getContract();
        if (contract != null) {
            contract.showPreloader();
        }
    }

    private void hideProgressBar() {
        BaseContract contract = getContract();
        if (contract != null) {
            contract.hidePreloader();
        }
    }

    public void handleImagePickerResult(Activity activity, int requestCode, int resultCode, Intent data) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                PhotoModel item = new PhotoModel(imageFile.getAbsolutePath(), 0);
                item.removable = true;
                AttachedPhotoAdapter adapter = attachedPhotoAdapter.get();
                if (adapter != null) {
                    adapter.addItem(item);
                    canAddPhotos.set(adapter.getItemCount() < Constants.MAX_PHOTOS_COUNT);
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    File imageFile = EasyImage.lastlyTakenButCanceledPhoto(activity);
                    if (imageFile != null) {
                        imageFile.delete();
                    }
                }
            }
        });
    }
}
