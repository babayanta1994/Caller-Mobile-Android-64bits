package ru.true_ip.trueip.app.profile_screen;

import android.content.Context;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Response;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.Callback;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.models.responses.ActivationsCountModel;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;

/**
 *
 * Created by Andrey Filimonov on 28.12.2017.
 */

public class ProfilePresenter extends BasePresenter<ProfileContract> {
    private Context context;
    private int objectId;
    public ObservableField<String> objectName = new ObservableField<>("");
    public ObservableField<String> userName = new ObservableField<>("");
    public ObservableField<String> activationsCount = new ObservableField<>("");
    public ObservableField<String> sipNumber = new ObservableField<>("");

    private ObjectDb objectDb;

    public void setContext(Context context) { this.context = context; }

    public void setExtras(Bundle extras) {
        objectId = extras.getInt(Constants.OBJECT_ID, -1);
    }

    public void displayData() {
        repositoryController.getObject(objectId).subscribe(objectDb -> {
            repositoryController.getUserById(objectDb.getUser_id()).subscribe(userDb -> {
                Pattern pattern = Pattern.compile("\\d+", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(userDb.getName());
                if (matcher.find()) {
                    String flatNumber = matcher.group();
                    userName.set(flatNumber);
                }
            });
            sipNumber.set(objectDb.getSip_number());
            objectName.set(objectDb.getName());
            getActivationsCountData();
        });
    }

    private void getActivationsCountData() {
        if (objectDb != null) {
            getActivationsCountDataResult(objectDb);
        } else {
            repositoryController.getObject(objectId).subscribe(objectDb1 -> {
                objectDb = objectDb1;
                getActivationsCountDataResult(objectDb);
            });
        }
    }

    private void getActivationsCountDataResult(ObjectDb objectDb) {
        apiController.getActivationsCount(context, objectDb, new Callback<Response<ActivationsCountModel>>() {
            @Override
            public void onSuccess(Response<ActivationsCountModel> response) {
                super.onSuccess(response);

                if (response.isSuccessful()) {
                    ActivationsCountModel model = response.body();
                    if (model != null) {
                        String activationsCountText = context.getString(
                                R.string.activations_count_text,
                                model.getNumber_of_activations(),
                                model.getLimit_of_activations());

                        activationsCount.set(activationsCountText);
                    }
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                DialogHelper.createErrorDialog(context,
                        context.getResources().getString(R.string.text_error_dialog_title),
                        context.getResources().getString(R.string.text_error_default_message));

            }
        });
    }
}
