package com.linsh.lshapp.common.base;

import android.os.Bundle;

import com.linsh.base.LshActivity;
import com.linsh.base.activity.impl.DelegateActivity;
import com.linsh.lshapp.common.mvp.Contract;
import com.linsh.lshapp.common.mvp.MvpActivityDelegate;

import java.io.Serializable;

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
        Serializable extra = LshActivity.intent(getIntent()).getSerializableExtra();
        P presenter = (extra instanceof Contract.Presenter) ? (P) extra : initPresenter();
        mvpDelegate = new MvpActivityDelegate(presenter, this);
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

    public static <P extends Contract.Presenter> void intent(
            Class<? extends BaseActivity<P>> classOfActivity, Class<? extends P> classOfPresenter) {
        LshActivity.intent(classOfActivity)
                .putExtra(classOfPresenter);
    }
}
