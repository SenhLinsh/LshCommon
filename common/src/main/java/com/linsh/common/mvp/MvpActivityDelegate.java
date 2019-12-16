package com.linsh.common.mvp;

import android.os.Bundle;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/12/06
 *    desc   :
 * </pre>
 */
public class MvpActivityDelegate<P extends Contract.Presenter, V extends Contract.View> {

    private P presenter;
    private V view;

    public MvpActivityDelegate(P presenter, V v) {
        this.view = v;
        this.presenter = MvpThreadConverter.delegatePresenter(presenter);
    }

    public void onCreate(Bundle savedInstanceState) {
        presenter.attachView(view);
    }

    public void onDestroy() {
        presenter.detachView();
    }

    public P getPresenter() {
        return presenter;
    }
}
