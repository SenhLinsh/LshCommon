package com.linsh.lshapp.common.base;


import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Senh Linsh on 17/4/24.
 */

public abstract class BasePresenterImpl<T extends BaseContract.BaseView> implements BaseContract.BasePresenter<T> {

    private T mView;
    private CompositeDisposable mCompositeDisposable;
    private CompositeDisposable RxBusSubscriptions;

    @Override
    public void attachView(T view) {
        mView = view;
        mCompositeDisposable = new CompositeDisposable();
        RxBusSubscriptions = new CompositeDisposable();

        attachView();
    }

    @Override
    public void detachView() {
        mCompositeDisposable.dispose();
        RxBusSubscriptions.clear();
        RxBusSubscriptions.dispose();
    }

    protected abstract void attachView();

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    protected T getView() {
        return mView;
    }

    protected CompositeDisposable getSubscription() {
        return mCompositeDisposable;
    }

    protected void addDisposable(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    protected void addDisposable(Disposable... disposables) {
        for (Disposable disposable : disposables) {
            mCompositeDisposable.add(disposable);
        }
    }

    protected void addRxBusSub(Disposable disposable) {
        RxBusSubscriptions.add(disposable);
    }

    protected void addRxBusSub(Disposable... disposables) {
        for (Disposable disposable : disposables) {
            RxBusSubscriptions.add(disposable);
        }
    }
}
