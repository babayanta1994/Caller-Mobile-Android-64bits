package ru.true_ip.trueip.app.main_screen.hlm_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.bytedeco.javacpp.annotation.Const;

import java.util.List;

import retrofit2.Response;
import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.messages_screen.MessagesActivity;
import ru.true_ip.trueip.app.profile_screen.ProfileActivity;
import ru.true_ip.trueip.app.quizzes_screen.QuizzesActivity;
import ru.true_ip.trueip.app.requests_screen.RequestsActivity;
import ru.true_ip.trueip.base.Callback;
import ru.true_ip.trueip.base.adapters.BindingRecyclerAdapter;
import ru.true_ip.trueip.databinding.ItemHlmObjectBinding;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.models.responses.ErrorApiResponse;
import ru.true_ip.trueip.models.responses.UnreadMessagesModel;
import ru.true_ip.trueip.repository.ApiController;
import ru.true_ip.trueip.repository.ApiControllerWithReactivation;
import ru.true_ip.trueip.repository.RepositoryController;
import ru.true_ip.trueip.service.service.SipServiceCommands;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;

/**
 *
 * Created by Andrey Filimonov on 09.01.2018.
 */

public class HLMFragmentObjectAdapter extends BindingRecyclerAdapter<ObjectDb> {
    private final static String TAG = HLMFragmentObjectAdapter.class.getSimpleName();
    private Context context;
    private int selectedPosition = -1;
    RepositoryController repositoryController;
    ApiControllerWithReactivation apiController;

    public HLMFragmentObjectAdapter(@LayoutRes Integer holderLayout,
                                    List<ObjectDb> items,
                                    Context context,
                                    RepositoryController repositoryController,
                                    ApiControllerWithReactivation apiController) {
        super(holderLayout, items);
        this.context = context;
        this.repositoryController = repositoryController;
        this.apiController = apiController;
    }

    @Override
    public void setItems(List<ObjectDb> items) {
        super.setItems(items);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(BindingRecyclerAdapter.BindingHolder holder, int position) {
        //Logger.error(TAG, "Selected position = " + selectedPosition + " position = " + position);
        super.onBindViewHolder(holder, position);

        ObjectDb current = items.get(position);

        ItemHlmObjectBinding bind = (ItemHlmObjectBinding) holder.getBinding();

        TextView hlmObjectName = bind.getRoot().findViewById(R.id.hlm_object_name);
        hlmObjectName.setText(current.getName());

        LinearLayout hlmObjectButton = bind.getRoot().findViewById(R.id.hlm_object_buttons);
        ImageView hlmArrow = bind.getRoot().findViewById(R.id.hlm_object_arrow);

        RelativeLayout hlmObjectsLayout = bind.getRoot().findViewById(R.id.hlm_objects_layout);
        hlmObjectsLayout.setOnClickListener(view -> {
            selectedPosition = position;

            if (hlmObjectButton.getVisibility() == View.GONE){
                hlmObjectButton.setVisibility(View.VISIBLE);
                hlmArrow.setImageDrawable(context.getResources().getDrawable(R.drawable.arrow_up));
            }
            else {
                hlmObjectButton.setVisibility(View.GONE);
                hlmArrow.setImageDrawable(context.getResources().getDrawable(R.drawable.arrow_down));
            }
        });

        TextView hlmObjectMessagesTotalCount = bind.getRoot().findViewById(R.id.hlm_object_messages_total_count);

        if (selectedPosition != position) {
            hlmArrow.setImageDrawable(context.getResources().getDrawable(R.drawable.arrow_down));
            hlmObjectButton.setVisibility(View.GONE);
        }
        else {
            hlmObjectButton.setVisibility(View.VISIBLE);
            hlmArrow.setImageDrawable(context.getResources().getDrawable(R.drawable.arrow_up));
        }

        LinearLayout hlmProfile = bind.getRoot().findViewById(R.id.hlm_profile);
        hlmProfile.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.OBJECT_ID, current.getObject_id());
            ProfileActivity.start(context, bundle);
        });

        RelativeLayout hlmMessages = bind.getRoot().findViewById(R.id.hlm_messages);
        hlmMessages.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.OBJECT_ID, current.getObject_id());
            MessagesActivity.start(context, bundle);
        });

        RelativeLayout hlmQuestionarie = bind.getRoot().findViewById(R.id.hlm_questionaries);
        hlmQuestionarie.setOnClickListener(view -> {
            if (checkIsApiAvailable(current)) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.OBJECT_ID, current.getObject_id());
                QuizzesActivity.start(context, bundle);
            } else {
                showProLicenseRequiredMessage();
            }
        });

        RelativeLayout hlmRequest = bind.getRoot().findViewById(R.id.hlm_requests);
        hlmRequest.setOnClickListener(view -> {
            if (checkIsApiAvailable(current)) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.OBJECT_ID, current.getObject_id());
                RequestsActivity.start(context, bundle);
            } else {
                showProLicenseRequiredMessage();
            }
        });

        TextView messagesCount = bind.getRoot().findViewById(R.id.hlm_object_messages_count);
        TextView questionariesCount = bind.getRoot().findViewById(R.id.hlm_questionaries_count);
        TextView requestsCount = bind.getRoot().findViewById(R.id.hlm_requests_count);

        if (repositoryController == null) {
            //Logger.error(TAG, "repository controller = null");
            return;
        }
        repositoryController.getObject(current.getObject_id()).subscribe(objectDb -> {
            if (checkIsApiAvailable(objectDb)) {
                apiController.getUnreadMessages(context, objectDb, new Callback<Response<UnreadMessagesModel>>() {
                    @Override
                    public void onSuccess(Response<UnreadMessagesModel> response) {
                        super.onSuccess(response);
                        int status = response.code();
                        if (response.isSuccessful()) {
                            UnreadMessagesModel responseBody = response.body();
                            if (responseBody != null) {
                                //Logger.error(TAG, "Setting counters for an element");
                                int totalMessages = responseBody.getAdvert() + responseBody.getNotice() + responseBody.getQuiz();
                                int totalMessagesInMessages = responseBody.getAdvert() + responseBody.getNotice();
                                hlmObjectMessagesTotalCount.setVisibility(View.INVISIBLE);
                                messagesCount.setVisibility(View.INVISIBLE);
                                questionariesCount.setVisibility(View.INVISIBLE);
                                requestsCount.setVisibility(View.INVISIBLE);
                                if (totalMessages == 0) {
                                    return;
                                } else {
                                    hlmObjectMessagesTotalCount.setText(Integer.toString(totalMessages));
                                    hlmObjectMessagesTotalCount.setVisibility(View.VISIBLE);
                                    if (totalMessagesInMessages > 0) {
                                        messagesCount.setVisibility(View.VISIBLE);
                                        messagesCount.setText(Integer.toString(totalMessagesInMessages));
                                    } else {
                                        messagesCount.setVisibility(View.INVISIBLE);
                                    }
                                    if (responseBody.getQuiz() > 0) {
                                        questionariesCount.setVisibility(View.VISIBLE);
                                        questionariesCount.setText(Integer.toString(responseBody.getQuiz()));
                                    } else {
                                        questionariesCount.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }
                        } else {
                            ErrorApiResponse error = new ErrorApiResponse(response.errorBody());
                            DialogHelper.createErrorDialog(context,
                                    context.getResources().getString(R.string.text_error_dialog_title),
                                    error.getError());

                            if (status == 401 || status == 403) {
                                SipServiceCommands.removeAccount(context, objectDb.getIdUri());
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
        });
    }

    private boolean checkIsApiAvailable(ObjectDb objectDb) {
        String license = objectDb.getLicenseType();
        return license != null && license.equals(Constants.LICENSE_TYPE_PRO) || license == null;
    }

    private void showProLicenseRequiredMessage() {
        DialogHelper.createInfoDialog(context,
                context.getString(R.string.text_error_dialog_title),
                context.getString(R.string.text_unavailable_in_lite));
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int position) {
        //Logger.error(TAG, "Set selected to " + position);
        this.selectedPosition  = position;
    }
}
