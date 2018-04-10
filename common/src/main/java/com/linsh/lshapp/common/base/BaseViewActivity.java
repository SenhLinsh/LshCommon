package com.linsh.lshapp.common.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.linsh.base.BaseContract;
import com.linsh.utilseverywhere.ToastUtils;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Senh Linsh on 17/4/24.
 */

public abstract class BaseViewActivity<T extends BasePresenterImpl> extends BaseActivity implements BaseContract.View {

    protected T mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化Presenter
        mPresenter = initPresenter();
        mPresenter.attachView(this);
    }

    protected abstract T initPresenter();

    @NotNull
    @Override
    public T getPresenter() {
        return mPresenter;
    }

    @NotNull
    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void showToast(@NotNull String content) {
        ToastUtils.show(content);
    }

    @Override
    public void showToastLong(@NotNull String content) {
        ToastUtils.showLong(content);
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @NotNull
    @Override
    public Activity getActivity() {
        return super.getActivity();
    }
}
