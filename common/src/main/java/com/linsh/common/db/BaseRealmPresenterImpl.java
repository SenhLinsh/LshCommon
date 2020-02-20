package com.linsh.common.db;

import com.linsh.base.activity.Contract;
import com.linsh.common.base.BasePresenterImpl;

import java.util.LinkedList;
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
    private List<Object> objectToRemoveListeners;

    @Override
    public void attachView(V view) {
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
                }
            }
        }
    }

    protected Realm getRealm() {
        return mRealm;
    }

    protected <T extends RealmModel> void addRealmChangeListener(RealmResults<T> results, RealmChangeListener<RealmResults<T>> listener) {
        results.addChangeListener(listener);
        if (objectToRemoveListeners == null) objectToRemoveListeners = new LinkedList<>();
        objectToRemoveListeners.add(results);
    }

    protected <T extends RealmModel> void addRealmChangeListener(RealmList<T> realmList, RealmChangeListener<RealmList<T>> listener) {
        realmList.addChangeListener(listener);
        if (objectToRemoveListeners == null) objectToRemoveListeners = new LinkedList<>();
        objectToRemoveListeners.add(realmList);
    }

    protected <T extends RealmObject> void addRealmChangeListener(T realmObject, RealmChangeListener<T> listener) {
        realmObject.addChangeListener(listener);
        if (objectToRemoveListeners == null) objectToRemoveListeners = new LinkedList<>();
        objectToRemoveListeners.add(realmObject);
    }

    protected void removeRealmChangeListeners(RealmObject... realmObjects) {
        for (RealmObject realmObject : realmObjects) {
            removeRealmChangeListener(realmObject);
        }
    }

    protected void removeRealmChangeListener(RealmObject realmObject) {
        if (realmObject != null && realmObject.isValid()) {
            realmObject.removeAllChangeListeners();
        }
    }

    protected void removeRealmChangeListener(RealmList<?> realmList) {
        if (realmList != null && realmList.isValid()) {
            realmList.removeAllChangeListeners();
        }
    }

    protected void removeRealmChangeListener(RealmResults<?> realmResults) {
        if (realmResults != null && realmResults.isValid()) {
            realmResults.removeAllChangeListeners();
        }
    }

    protected <E extends RealmObject> E copyFromRealm(E realmObject) {
        if (realmObject == null || !realmObject.isValid())
            return null;
        return mRealm.copyFromRealm(realmObject);
    }

    protected <E extends RealmModel> List<E> copyFromRealm(RealmResults<E> realmResults) {
        if (realmResults == null || !realmResults.isValid())
            return null;
        return mRealm.copyFromRealm(realmResults);
    }
}
