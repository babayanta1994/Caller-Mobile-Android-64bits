package ru.true_ip.trueip.repository;


import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.operators.completable.CompletableFromAction;
import io.reactivex.internal.operators.completable.CompletableFromCallable;
import io.reactivex.schedulers.Schedulers;
import ru.true_ip.trueip.db.AppDatabase;
import ru.true_ip.trueip.db.entity.DevicesDb;
import ru.true_ip.trueip.db.entity.LocksDb;
import ru.true_ip.trueip.db.entity.MessageDb;
import ru.true_ip.trueip.db.entity.ObjectDb;
import ru.true_ip.trueip.db.entity.ServicesDb;
import ru.true_ip.trueip.db.entity.SettingsDb;
import ru.true_ip.trueip.db.entity.UserDb;
import ru.true_ip.trueip.service.service.Logger;

/**
 * Created by Eugen on 26.09.2017.
 *
 */

public class RepositoryController {
    private final static String TAG = "RepositoryController";
    private AppDatabase appDatabase;
    private Cache cache;

    public RepositoryController(AppDatabase appDatabase, Cache cache) {
        this.appDatabase = appDatabase;
        this.cache = cache;
    }

    public void setToken(String token) {
        cache.saveString(Cache.TOKEN_KEY, token);
    }

    public String getToken() {
        return cache.getString(Cache.TOKEN_KEY);
    }

    public Single<List<ObjectDb>> getAllObjects() {
        return appDatabase.objectDao().getAllObjects();
    }

    public Single<List<ObjectDb>> getLocalObjects() {
        return appDatabase.objectDao().getLocalObjects();
    }

    public Single<List<ObjectDb>> getHLMObjects() {
        return appDatabase.objectDao().getHLMObjects();
    }

    public Single<ObjectDb> getObject(int objectId) {
        return appDatabase.objectDao().getObject(objectId);
    }

    public Single<ObjectDb> getObjectByUserId(Integer userId) {
        return appDatabase.objectDao().getObjectByUserId(userId);
    }

    public Completable deleteObject(ObjectDb objectDb) {
        return new CompletableFromAction(() ->
                appDatabase.objectDao().delete(objectDb))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable addObject(ObjectDb... objectDbs) {
        return new CompletableFromAction(() ->
                appDatabase.objectDao().insertAll(objectDbs))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable addUser(UserDb userDb) {
        return new CompletableFromAction(() ->
                appDatabase.userDao().insert(userDb))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteUser(UserDb userDb) {
        return new CompletableFromAction(() ->
                appDatabase.userDao().delete(userDb))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<UserDb> getUserById(int id) {
        return appDatabase.userDao().getUserById(id);
    }

    public Completable updateUser(UserDb userDb) {
        return new CompletableFromAction(() ->
                appDatabase.userDao().updateUser(userDb))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<UserDb> getFirstUser() {
        return appDatabase.userDao().getFirstUser();
    }

    public Completable addSettings(SettingsDb settingsDB) {
        return new CompletableFromAction(() ->
                appDatabase.settingsDao().insert(settingsDB))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteSettings(SettingsDb settingsDB) {
        return new CompletableFromAction(() ->
                appDatabase.settingsDao().delete(settingsDB))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<SettingsDb> getSettingBySettingsId(int id) {
        return appDatabase.settingsDao().getSettingBySettingsId(id);
    }

    public Single<SettingsDb> getSettingByUserId(int id) {
        return appDatabase.settingsDao().getSettingByUserId(id);
    }

    public Single<SettingsDb> getFirstSettings() {
        return appDatabase.settingsDao().getFirstSettings();
    }

    public Completable addLock(LocksDb locksDb) {
        return new CompletableFromAction(() ->
                appDatabase.locksDao().insert(locksDb))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteLock(LocksDb locksDb) {
        return new CompletableFromAction(() ->
                appDatabase.locksDao().delete(locksDb))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<LocksDb> getLockByLockId(int id) {
        return appDatabase.locksDao().getLockByLockId(id);
    }

    public Single<LocksDb> getLockByDeviceId(int id) {
        return appDatabase.locksDao().getLockByDeviceId(id);
    }

    public Single<LocksDb> getFirstLock() {
        return appDatabase.locksDao().getFirstLock();
    }

    public Single<Integer> getOjbectsCount() {
        return Single.just(appDatabase.objectDao().countObjects());
    }

    public Single<Integer> getHMLObjectsCount() {
        return Single.just(appDatabase.objectDao().countHLMObjects());
    }

    public Completable addService(ServicesDb servicesDb) {
        return new CompletableFromAction(() ->
                appDatabase.servicesDao().insert(servicesDb))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteService(ServicesDb servicesDb) {
        return new CompletableFromAction(() ->
                appDatabase.servicesDao().delete(servicesDb))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ServicesDb> getServiceByServiceId(int id) {
        return appDatabase.servicesDao().getServiceByServiceId(id);
    }

    public Single<ServicesDb> getServiceByObjectId(int id) {
        return appDatabase.servicesDao().getServiceByObjectId(id);
    }

    public Single<ServicesDb> getFirstService() {
        return appDatabase.servicesDao().getFirstService();
    }

    public Completable addDevice(DevicesDb devicesDb) {
        return new CompletableFromAction(() ->
                appDatabase.devicesDao().insert(devicesDb))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable addDevices(List<DevicesDb> devicesDbs) {
        return new CompletableFromAction(() ->
                appDatabase.devicesDao().insert(devicesDbs))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteDevice(DevicesDb devicesDb) {
        return new CompletableFromAction(() ->
                appDatabase.devicesDao().delete(devicesDb))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Single<DevicesDb> getDeviceByDeviceId(int id) {
        return appDatabase.devicesDao().getDeviceByDeviceId(id);
    }

    public Single<DevicesDb> getDevicesOfObjectByTypeAndServerId(int objectId, int serverId, int deviceType) {
        return appDatabase.devicesDao().getDevicesOfObjectByTypeAndServerId(objectId, serverId, deviceType);
    }

    public Single<List<DevicesDb>> getDevicesOfObjectByType(int objectId, int deviceType) {
        return appDatabase.devicesDao().getDevicesOfObjectByType(objectId, deviceType);
    }

    public Single<List<DevicesDb>> getAllDevices() {
        return appDatabase.devicesDao().getAllDevices();
    }

    public Single<List<DevicesDb>> getFavoriteDevices() {
        return appDatabase.devicesDao().getFavoriteDevices();
    }

    public Single<List<DevicesDb>> getAllDevices(int deviceType, int objectId) {
        return appDatabase.devicesDao().getAllDevicesByType(deviceType, objectId);
    }

    public Single<List<DevicesDb>> getAllDevices(int objectId) {
        return appDatabase.devicesDao().getAllDevicesByObjectId(objectId);
    }

    public int removeDevicesByObjectId(int object_id) {
        return appDatabase.devicesDao().removeDevicesByObjectId(object_id);
    }

    public Completable removeDevices(List<DevicesDb> devicesDbs) {
        return new CompletableFromAction(() ->
                appDatabase.devicesDao().delete(devicesDbs))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public boolean isObjectAlreadyExists(String flat_number, String activation_code) {
        return appDatabase.objectDao().countSameObject(flat_number, activation_code) > 0;
    }

    public Observable<Boolean> doesDeviceExist(int deviceId) {
        return Observable.fromCallable(() -> appDatabase.devicesDao().countDevices(deviceId) > 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateObject(ObjectDb objectDb) {
        return new CompletableFromAction(() -> {
            Logger.error(TAG, "IsObjectActive = " + objectDb.IsObjectActive());
            appDatabase.objectDao().update(objectDb);})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void setObjectActive(int objectId, Integer value) {
        Logger.error(TAG,"Set Object Active to " + value);
        appDatabase.objectDao().setObjectActive(objectId, value);
    }

    public Single<List<MessageDb>> getAllMessages() {
        return appDatabase.messagesDao().getAllMessages();
    }

    public Single<List<MessageDb>> getAllMessagesByCommentId(int commentId) {
        return appDatabase.messagesDao().getAllMessagesByCommentId(commentId);
    }

    public Single<List<MessageDb>> getMessagesRange(int rowsCount, int offset) {
        return appDatabase.messagesDao().getMessagesRange(rowsCount, offset);
    }

    public Single<List<MessageDb>> getMessageById(int id) {
        return appDatabase.messagesDao().getMessageById(id);
    }

    public Completable insertMessages(List<MessageDb> messages) {
        return new CompletableFromCallable(() ->
                appDatabase.messagesDao().insert(messages))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insertMessage(MessageDb messages) {
        return new CompletableFromAction(() ->
                appDatabase.messagesDao().insert(messages))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteAllMessages() {
        return new CompletableFromAction(() ->
                appDatabase.messagesDao().deleteAllMessages())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteMessagesByUserId(int userId) {
        return new CompletableFromAction(() ->
                appDatabase.messagesDao().deleteMessagesByUserId(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateAnswersCount(int dialogId, int numberOfMessages) {
        return new CompletableFromAction(() ->
                appDatabase.messagesDao().updateAnswersCount(dialogId, numberOfMessages))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateDevice(DevicesDb devicesDb) {
        return new CompletableFromAction(() ->
                appDatabase.devicesDao().update(devicesDb))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
