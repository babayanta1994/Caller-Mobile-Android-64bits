package ru.true_ip.trueip.app.main_screen.objects_fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.App;
import ru.true_ip.trueip.app.main_screen.objects_fragment.adapters.ObjectAdapter;
import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.base.adapters.BindingRecyclerAdapter;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.service.data.BroadcastEventReceiver;
import ru.true_ip.trueip.service.data.SipAccountData;
import ru.true_ip.trueip.service.service.Logger;
import ru.true_ip.trueip.service.service.SipServiceCommands;
import ru.true_ip.trueip.utils.Constants;
import ru.true_ip.trueip.utils.DialogHelper;
import ru.true_ip.trueip.utils.WifiHelper;

/**
 *
 * Updated by Andrey Filimonov on 11-Sep-17.
 *
 * Shows List of Local and T-nect objects
 */

public class ObjectsPresenter extends BasePresenter<ObjectsContract> {

    private static final String TAG = ObjectsPresenter.class.getSimpleName();
    public ObservableBoolean isEditMode = new ObservableBoolean(false);
    public ObjectAdapter localObjectAdapter;
    public ObjectAdapter hlmObjectAdapter;
    private List<ObjectDb> localObjectsDb = new ArrayList<>();
    private List<ObjectDb> hlmObjectsDb = new ArrayList<>();
    private BroadcastEventReceiver eventReceiver;

    //
    //ObjectPresenter
    //
    ObjectsPresenter() {
        createObjectRvs();
        createEventReceiver();
    }
    //
    //attachToView
    //
    @Override
    public void attachToView(ObjectsContract contract) {
        super.attachToView(contract);
        //print( "Attach To View");
        getLocalObjects();
        getHLMObjects();
        updateSipAccounts();
    }

    public void onResume() {
        getContract().registerBroadcastReceiver(eventReceiver);
    }

    public void onPause() {
        getContract().unregisterBroadcastReceiver(eventReceiver);
    }
    //
    //detachView
    //
    @Override
    public void detachView() {
        WifiHelper.getInstance().addWifiListener(null);
        super.detachView();
    }
    //
    //createEventReceiver
    //
    private void createEventReceiver() {
        eventReceiver = new BroadcastEventReceiver() {
            @Override
            public void onRegistration(String accountSipNumber, int registrationStateCode) {
                localObjectAdapter.updateItemStatus(accountSipNumber, registrationStateCode > 0 );
                hlmObjectAdapter.updateItemStatus(accountSipNumber, registrationStateCode > 0);
                print( "onRegistration: ObjectsPresenter: username: " + accountSipNumber + " : " + registrationStateCode);
            }
        };
        //getContract().getAllAccountsState();
        //getAccountsState();
        WifiHelper.getInstance().addWifiListener(connected -> {
            if (getContract() != null) {
                //getContract().getAllAccountsState();
            }
        });
    }

    public void getAccountsState() {
        SipServiceCommands.getAllAccountState(App.getContext());
    }
    //
    //onEditClick
    //
    public void onEditClick(View v) {
        isEditMode.set(!isEditMode.get());
        localObjectAdapter.setEditMode(isEditMode.get());
        hlmObjectAdapter.setEditMode(isEditMode.get());
    }
    //
    //onClickAdd
    //
    public void onClickAdd(View v) {
        if (localObjectAdapter.getItemCount() + hlmObjectAdapter.getItemCount() >= Constants.MAX_ACCOUNT_SIZE) {
            //print( "MAX ACCOUNT LIMIT HAS BEEN REACHED");
            Toast.makeText(v.getContext(), R.string.text_max_account_limit_has_been_reached, Toast.LENGTH_SHORT).show();
            return;
        }
        getContract().getRouter().moveTo(BaseRouter.Destination.OBJECT_TYPE_PICKER_SCREEN);
    }
    //
    //getLocalObjects
    //
    @SuppressLint("CheckResult")
    private void getLocalObjects() {
        repositoryController.getLocalObjects().subscribe(objectDbs -> {
            if (objectDbs != null) {
                this.localObjectsDb = objectDbs;
                updateLocalAdapter(objectDbs);
            }
        });
    }
    //
    //getHLMObjects
    //
    @SuppressLint("CheckResult")
    void getHLMObjects() {
        repositoryController.getHLMObjects().subscribe(objectDbs -> {
            if (objectDbs != null) {
                this.hlmObjectsDb = objectDbs;
                updateHLMAdapter(objectDbs);
            }
        });
    }
    //
    //updateSipAccounts
    //
    private void updateSipAccounts() {
        ArrayList<SipAccountData> accountsList = new ArrayList<>();
        for (ObjectDb object: localObjectsDb ) {
            if ( object.IsObjectActive()) {
                accountsList.add(new SipAccountData(object.getIp_address(),
                        object.getSip_number(),
                        object.getPassword(),
                        object.getPort()));
            }
        }
        for (ObjectDb object: hlmObjectsDb ) {
            if ( object.IsObjectActive()) {
                accountsList.add(new SipAccountData(object.getIp_address(),
                        object.getSip_number(),
                        object.getPassword(),
                        object.getPort()));
            }
        }
        getContract().updateSipAccounts(accountsList);
    }
    //
    //onClickListener
    //
    private BindingRecyclerAdapter.OnItemClickListener<ObjectDb> objectsAdapter = new BindingRecyclerAdapter.OnItemClickListener<ObjectDb>() {
        @Override
        public void onItemClick(int position, ObjectDb item) {
            Bundle bundle = new Bundle();
            //item.isObjectActive
            bundle.putInt(Constants.BUNDLE_INT_KEY, item.getObject_id());
            bundle.putBoolean(Constants.BUNDLE_IS_EDIT_MODE, isEditMode.get());
            if (isEditMode.get()) {
                getContract().getRouter().moveTo(BaseRouter.Destination.ADD_NEW_OBJECT_SCREEN, bundle);
            } else {
                getContract().getRouter().moveTo(BaseRouter.Destination.OBJECT_SCREEN, bundle);
            }
        }
    };
    //
    //createObjectRvs
    //
    void createObjectRvs() {
        localObjectAdapter = new ObjectAdapter(R.layout.item_object, new ArrayList<>(), repositoryController);
        hlmObjectAdapter = new ObjectAdapter(R.layout.item_object, new ArrayList<>(), repositoryController);
        localObjectAdapter.addOnItemClickListener(objectsAdapter);
        hlmObjectAdapter.addOnItemClickListener(objectsAdapter);
        localObjectAdapter.addOnObjectActiveChangeListener((position) -> {
            //print( "Local Position = " + position);
            ObjectDb objectDb = localObjectAdapter.getAdapterItems().get(position);
            if (objectDb.IsObjectActive()) {
                SipAccountData sipAccountData = new SipAccountData(objectDb.getIp_address(),
                        objectDb.getSip_number(),
                        objectDb.getPassword(),
                        objectDb.getPort());
                SipServiceCommands.changeRegistration(App.getContext(), sipAccountData.getIdUri(), true);
            } else {
                SipServiceCommands.changeRegistration(App.getContext(), localObjectAdapter.getAdapterItems().get(position).getIdUri(), false);
            }
        });
        hlmObjectAdapter.addOnObjectActiveChangeListener((position) -> {
            //print( "HLM Position = " + position);
            ObjectDb objectDb = hlmObjectAdapter.getAdapterItems().get(position);
            if (objectDb.IsObjectActive()) {
                SipAccountData sipAccountData = new SipAccountData(objectDb.getIp_address(),
                        objectDb.getSip_number(),
                        objectDb.getPassword(),
                        objectDb.getPort());
                SipServiceCommands.changeRegistration(App.getContext(), sipAccountData.getIdUri(), true);
            } else {
                SipServiceCommands.changeRegistration(App.getContext(), hlmObjectAdapter.getAdapterItems().get(position).getIdUri(), false);
            }
        });
    }

    //
    //updateLocalAdapter
    //
    private void updateLocalAdapter(List<ObjectDb> objectDbs) {
        localObjectAdapter.setItems(sortAlphabetically(objectDbs));
    }
    //
    //updateHLMAdapter
    //
    private void updateHLMAdapter(List<ObjectDb> objectDbs) {
        hlmObjectAdapter.setItems(sortAlphabetically(objectDbs));
    }
    //
    //sortAlphabetically
    //
    private List<ObjectDb> sortAlphabetically(List<ObjectDb> objectDbs) {
        if (objectDbs.size() > 0)
            Collections.sort(objectDbs, (object1, object2) -> object1.getName().compareToIgnoreCase(object2.getName()));
        return objectDbs;
    }

    public void updateAccounts() {
        getLocalObjects();
        getHLMObjects();
        updateSipAccounts();
    }

    public void showDialogNoSipNumber(Context context) {
        DialogHelper.createInfoDialog(
                context,
                R.string.text_warning_dialog_title,
                R.string.text_object_without_sip);
    }

    private void print(String message) {
        Logger.error(TAG,message);
    }
}
