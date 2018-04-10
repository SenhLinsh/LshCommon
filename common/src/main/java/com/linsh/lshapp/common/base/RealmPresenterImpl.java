package com.linsh.lshapp.common.base;

import com.linsh.base.BaseContract;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * Created by Senh Linsh on 17/4/24.
 */

public abstract class RealmPresenterImpl<T extends BaseContract.View<BaseContract.Presenter<T>>> extends BasePresenterImpl<T> implements RealmChangeListener<Realm> {

    private Realm mRealm;
    private boolean mUnsubscribed;
    private boolean onChangedWhenUnsubscribe;

    @Override
    public void attachView(T view) {
        mRealm = Realm.getDefaultInstance();
        mRealm.addChangeListener(this);
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        mRealm.close();
    }

    public void invalidateView() {
    }

    @Override
    public void subscribe() {
        super.subscribe();
        mUnsubscribed = false;
        if (onChangedWhenUnsubscribe) {
            invalidateView();
        }
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        mUnsubscribed = true;
        onChangedWhenUnsubscribe = false;
    }

    @Override
    public void onChange(Realm element) {
        if (!mUnsubscribed) {
            invalidateView();
        } else {
            onChangedWhenUnsubscribe = true;
        }
    }

    protected Realm getRealm() {
        return mRealm;
    }
}
