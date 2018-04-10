package com.linsh.lshapp.common.base;


import com.linsh.base.BaseContract;

import org.jetbrains.annotations.NotNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/03/23
 *    desc   : BasePresenter 的实现类, 实现 Presenter 的基础功能
 * </pre>
 */
public abstract class BasePresenterImpl<T extends BaseContract.View<BaseContract.Presenter<T>>> implements BaseContract.Presenter<T> {

    private T mView;
    private CompositeDisposable mCompositeDisposable;
    private CompositeDisposable RxBusSubscriptions;

    void attachView(T view) {
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

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @NotNull
    @Override
    public T getView() {
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
