package com.linsh.lshapp.common.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.linsh.utilseverywhere.ToastUtils;


/**
 * Created by Senh Linsh on 17/4/24.
 */

public abstract class BaseViewActivity<T extends BasePresenterImpl> extends BaseActivity {

    protected T mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化Presenter
        mPresenter = initPresenter();
        mPresenter.attachView(this);
    }

    protected abstract T initPresenter();

    public T getPresenter() {
        return mPresenter;
    }

    public Context getContext() {
        return getApplicationContext();
    }

    public void showToast(String content) {
        ToastUtils.show(content);
    }

    public void showToastLong(String content) {
        ToastUtils.showLong(content);
    }

    public void finishActivity() {
        finish();
    }

    @Override
    public Activity getActivity() {
        return super.getActivity();
    }
}
