package com.linsh.common.mvp;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/12/06
 *    desc   :
 * </pre>
 */
public abstract class BasePresenterImpl<V extends Contract.View> implements com.linsh.common.mvp.Contract.Presenter<V> {

    private V mView;

    @Override
    public void attachView(V view) {
        mView = MvpThreadConverter.delegateView(view);
        attachView();
    }

    protected abstract void attachView();

    @Override
    public void detachView() {
    }

    public V getView() {
        return mView;
    }
}
