package com.linsh.lshapp.common.base;

import android.os.Bundle;

import com.linsh.base.activity.impl.DelegateActivity;
import com.linsh.lshapp.common.mvp.Contract;
import com.linsh.lshapp.common.mvp.MvpActivityDelegate;

import androidx.annotation.Nullable;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/12/06
 *    desc   :
 * </pre>
 */
public abstract class BaseActivity<P extends Contract.Presenter> extends DelegateActivity implements Contract.View<P> {

    private MvpActivityDelegate<P, Contract.View> mvpDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpDelegate = new MvpActivityDelegate(initPresenter(), this);
        mvpDelegate.onCreate(savedInstanceState);
    }

    protected abstract P initPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mvpDelegate.onDestroy();
    }

    @Override
    public P getPresenter() {
        return mvpDelegate.getPresenter();
    }
}
