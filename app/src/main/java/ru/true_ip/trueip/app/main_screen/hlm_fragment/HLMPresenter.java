package ru.true_ip.trueip.app.main_screen.hlm_fragment;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

import ru.true_ip.trueip.base.BasePresenter;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.repository.ApiControllerWithReactivation;
import ru.true_ip.trueip.repository.RepositoryController;



/**
 *
 * Created by Andrey Filimonov on 28.12.2017.
 */

public class HLMPresenter extends BasePresenter<HLMContract>  implements AdapterView.OnItemSelectedListener {
    private final static String TAG = HLMFragment.class.getSimpleName();
    private Context context;
    private List<ObjectDb> objectsdList = new ArrayList<>();
    private ArrayList<String> objectNames = new ArrayList<>();
    private int objectId = -1;
    public HLMFragmentObjectAdapter objectAdapter = null;

    void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void attachToView(HLMContract contract) {
        super.attachToView(contract);
        //new Handler().postDelayed(() -> localCheckServerStatus(), 500);
        localCheckServerStatus();
    }

    private void localCheckServerStatus() {
        super.checkServerStatus(this.context);
    }

    public RepositoryController getRepoController() {
        return repositoryController;
    }

    public ApiControllerWithReactivation getApiController() {
        return apiController;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        //Logger.error(TAG, "Selected object " + objectNames.get(position));
        objectId = objectsdList.get(position).getObject_id();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
