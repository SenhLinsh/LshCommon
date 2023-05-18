package com.linsh.common.db;

import com.linsh.base.mvp.Contract;
import com.linsh.common.base.BasePresenterImpl;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/12/15
 *    desc   :
 * </pre>
 */
public abstract class BaseRealmPresenterImpl<V extends Contract.View> extends BasePresenterImpl<V> {

    private Realm mRealm;
    private List<Object> objectToRemoveListeners = new ArrayList<>();

    @Override
    public void attachView(Contract.View view) {
        mRealm = Realm.getDefaultInstance();
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (objectToRemoveListeners != null) {
            for (Object object : objectToRemoveListeners) {
                if (object instanceof RealmObject) {
                    removeRealmChangeListener((RealmObject) object);
                } else if (object instanceof RealmResults) {
                    removeRealmChangeListener((RealmResults) object);
                } else if (object instanceof RealmList) {
                    removeRealmChangeListener((RealmList) object);
                } else if (object instanceof RealmChangeListener) {
                    getRealm().removeChangeListener((RealmChangeListener<Realm>) object);
                }
            }
        }
    }

    public Realm getRealm() {
        return mRealm;
    }

    /**
     * 开始事务
     */
    public void beginTransaction() {
        mRealm.beginTransaction();
    }

    /**
     * 提交事务
     */
    public void commitTransaction() {
        mRealm.commitTransaction();
    }

    /**
     * 取消事务
     */
    public void cancelTransaction() {
        if (mRealm.isInTransaction()) {
            mRealm.cancelTransaction();
        }
    }

    /**
     * 添加并管理 RealmChangeListener
     * <p>
     * 由于 RealmChangeListener 添加后没有 remove 掉会造成内存泄露, 使用该方法会在 detachView 时自动进行 remove 操作
     */
    public <T extends RealmModel> void addRealmChangeListener(RealmResults<T> results, RealmChangeListener<RealmResults<T>> listener) {
        addRealmChangeListener(results, listener, false);
    }

    /**
     * 添加并管理 RealmChangeListener
     * <p>
     * 由于 RealmChangeListener 添加后没有 remove 掉会造成内存泄露, 使用该方法会在 detachView 时自动进行 remove 操作
     *
     * @param notifyWhenAdd 添加时是否进行通知. 如果 true, 在添加 RealmChangeListener 时, 会自动进行一次调用
     */
    public <T extends RealmModel> void addRealmChangeListener(RealmResults<T> results, RealmChangeListener<RealmResults<T>> listener, boolean notifyWhenAdd) {
        results.addChangeListener(listener);
        if (notifyWhenAdd && results.isLoaded()) {
            listener.onChange(results);
        }
        objectToRemoveListeners.add(results);
    }

    /**
     * 添加并管理 RealmChangeListener
     * <p>
     * 由于 RealmChangeListener 添加后没有 remove 掉会造成内存泄露, 使用该方法会在 detachView 时自动进行 remove 操作
     */
    public <T extends RealmModel> void addRealmChangeListener(RealmList<T> realmList, RealmChangeListener<RealmList<T>> listener) {
        addRealmChangeListener(realmList, listener, false);
    }

    /**
     * 添加并管理 RealmChangeListener
     * <p>
     * 由于 RealmChangeListener 添加后没有 remove 掉会造成内存泄露, 使用该方法会在 detachView 时自动进行 remove 操作
     *
     * @param notifyWhenAdd 添加时是否进行通知. 如果 true, 在添加 RealmChangeListener 时, 会自动进行一次调用
     */
    public <T extends RealmModel> void addRealmChangeListener(RealmList<T> realmList, RealmChangeListener<RealmList<T>> listener, boolean notifyWhenAdd) {
        realmList.addChangeListener(listener);
        if (notifyWhenAdd && realmList.isLoaded()) {
            listener.onChange(realmList);
        }
        objectToRemoveListeners.add(realmList);
    }

    /**
     * 添加并管理 RealmChangeListener
     * <p>
     * 由于 RealmChangeListener 添加后没有 remove 掉会造成内存泄露, 使用该方法会在 detachView 时自动进行 remove 操作
     */
    public <T extends RealmObject> void addRealmChangeListener(T realmObject, RealmChangeListener<T> listener) {
        addRealmChangeListener(realmObject, listener, false);
    }

    /**
     * 添加并管理 RealmChangeListener
     * <p>
     * 由于 RealmChangeListener 添加后没有 remove 掉会造成内存泄露, 使用该方法会在 detachView 时自动进行 remove 操作
     *
     * @param notifyWhenAdd 添加时是否进行通知. 如果 true, 在添加 RealmChangeListener 时, 会自动进行一次调用
     */
    public <T extends RealmObject> void addRealmChangeListener(T realmObject, RealmChangeListener<T> listener, boolean notifyWhenAdd) {
        realmObject.addChangeListener(listener);
        if (notifyWhenAdd && realmObject.isLoaded()) {
            listener.onChange(realmObject);
        }
        objectToRemoveListeners.add(realmObject);
    }

    /**
     * 添加并管理 RealmChangeListener
     * <p>
     * 由于 RealmChangeListener 添加后没有 remove 掉会造成内存泄露, 使用该方法会在 detachView 时自动进行 remove 操作
     */
    public void addRealmChangeListener(RealmChangeListener<Realm> listener) {
        getRealm().addChangeListener(listener);
        objectToRemoveListeners.add(listener);
    }

    public void removeRealmChangeListeners(RealmObject... realmObjects) {
        for (RealmObject realmObject : realmObjects) {
            removeRealmChangeListener(realmObject);
        }
    }

    public void removeRealmChangeListener(RealmObject realmObject) {
        if (realmObject != null && realmObject.isValid()) {
            realmObject.removeAllChangeListeners();
        }
    }

    public void removeRealmChangeListener(RealmList<?> realmList) {
        if (realmList != null && realmList.isValid()) {
            realmList.removeAllChangeListeners();
        }
    }

    public void removeRealmChangeListener(RealmResults<?> realmResults) {
        if (realmResults != null && realmResults.isValid()) {
            realmResults.removeAllChangeListeners();
        }
    }

    public <E extends RealmObject> E copyFromRealm(E realmObject) {
        if (realmObject == null || !realmObject.isValid())
            return null;
        return mRealm.copyFromRealm(realmObject);
    }

    public <E extends RealmModel> List<E> copyFromRealm(RealmResults<E> realmResults) {
        if (realmResults == null || !realmResults.isValid())
            return null;
        return mRealm.copyFromRealm(realmResults);
    }

    public void checkCancelTransaction() {
        checkCancelTransaction(getRealm());
    }

    public void checkCancelTransaction(Realm... realms) {
        for (Realm realm : realms) {
            if (realm.isInTransaction()) {
                realm.cancelTransaction();
            }
        }
    }
}
