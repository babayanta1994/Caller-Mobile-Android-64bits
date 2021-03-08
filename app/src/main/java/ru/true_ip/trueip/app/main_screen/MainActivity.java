package ru.true_ip.trueip.app.main_screen;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.google.firebase.iid.FirebaseInstanceId;

import ru.true_ip.trueip.R;
import ru.true_ip.trueip.app.main_screen.favorites_fragment.FavoritesFragment;
import ru.true_ip.trueip.app.main_screen.hlm_fragment.HLMFragment;
import ru.true_ip.trueip.app.main_screen.objects_fragment.ObjectsFragment;
import ru.true_ip.trueip.base.BaseActivity;
import ru.true_ip.trueip.base.BaseRouter;
import ru.true_ip.trueip.databinding.ActivityMainBinding;
import ru.true_ip.trueip.receivers.AlarmReceiver;
import ru.true_ip.trueip.service.service.SipService;
import ru.true_ip.trueip.service.service.SipServiceCommands;
import ru.true_ip.trueip.utils.Constants;

public class MainActivity extends BaseActivity<MainContract, MainPresenter, ActivityMainBinding> implements MainContract {

    public static final String TAG = MainActivity.class.getSimpleName();

    private ActionBarDrawerToggle drawerToggle;

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, MainActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
                //.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String token = instanceIdResult.getToken();
            presenter.setContext(this);
            presenter.savePushToken(token);
            presenter.testApiCalls();
        });
        if (binding != null) {
            initLeftDrawer(binding.toolbar, binding.drawerLayout, binding.leftDrawer);
        }
        handleBundle(getIntent().getExtras());
        Intent i = new Intent(getApplicationContext(), SipService.class);
        startService(i);

        //Setting alarm
        AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                60*1000,
                pendingIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SipServiceCommands.stopService(getApplicationContext());
    }

    private void handleBundle(Bundle bundle) {
        if (bundle == null) return;
        Object val = bundle.get(Constants.BUNDLE_DESTINATION);
        if (val instanceof BaseRouter.Destination) {
            switch ((BaseRouter.Destination) val) {
                case OBJECTS_SCREEN:
                    router.moveTo(BaseRouter.Destination.OBJECTS_SCREEN, bundle);
                    break;
                case FAVORITES_SCREEN:
                    router.moveTo(BaseRouter.Destination.FAVORITES_SCREEN);
                    break;
                case HLM_SCREEN:
                    router.moveTo(BaseRouter.Destination.HLM_SCREEN);
                    break;
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleBundle(intent.getExtras());
    }

    @Override
    public ActivityMainBinding initBinding() {
        return DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_main, null, false);
    }

    @Override
    public MainContract getContract() {
        return this;
    }

    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public BaseRouter createRouter() {
        return new MainRouter(this);
    }

    @Override
    public void setTitle(String title) {
        if (binding != null)
            binding.title.setText(title);
    }

    private void initLeftDrawer(Toolbar toolbar, DrawerLayout drawerLayout, LinearLayout leftDrawer) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    public void showHamburgerIcon() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.setToolbarNavigationClickListener(null);
    }

    public void showBackIcon() {
        drawerToggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle.setToolbarNavigationClickListener(view -> onBackPressed());
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

    @Override
    public void closeDrawer() {
        if (binding != null) {
            binding.drawerLayout.closeDrawers();
        }
    }

    @Override
    public void notifyObjectChanged() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.container);
        if (fragment instanceof HLMFragment) {
            HLMFragment hlmFragment = (HLMFragment)fragment;
            hlmFragment.loadObjects();
        } else if (fragment instanceof ObjectsFragment) {
            ObjectsFragment objectsFragment = (ObjectsFragment)fragment;
            objectsFragment.reloadHLMObjects();
        }
    }

    @Override
    public void notifyDevicesUpdated() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.container);
        if (fragment instanceof FavoritesFragment) {
            FavoritesFragment favoritesFragment = (FavoritesFragment) fragment;
            favoritesFragment.updateDevices();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
