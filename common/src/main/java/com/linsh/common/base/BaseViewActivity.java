package com.linsh.common.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.linsh.base.LshActivity;
import com.linsh.common.mvp.Contract;
import com.linsh.common.mvp.TransThreadMvpDelegate;

import java.io.Serializable;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/12/06
 *    desc   :
 * </pre>
 */
public abstract class BaseViewActivity<P extends Contract.Presenter> extends BaseActivity implements Contract.View<P> {

    private TransThreadMvpDelegate<P, Contract.View> mvpDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Serializable extra = LshActivity.intent(getIntent()).getSerializableExtra();
        P presenter = (extra instanceof Contract.Presenter) ? (P) extra : initPresenter();
        mvpDelegate = new TransThreadMvpDelegate<>(presenter, this);
        mvpDelegate.attachView();
    }

    protected abstract P initPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mvpDelegate.detachView();
    }

    @Override
    public P getPresenter() {
        return mvpDelegate.getPresenter();
    }

    public static <P extends Contract.Presenter> void intent(
            Class<? extends BaseViewActivity<P>> classOfActivity, Class<? extends P> classOfPresenter) {
        LshActivity.intent(classOfActivity)
                .putExtra(classOfPresenter);
    }
}
