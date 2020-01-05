package com.linsh.common.base;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.linsh.base.activity.Contract;
import com.linsh.base.activity.mvp.BaseMvpActivity;
import com.linsh.dialog.DialogComponents;
import com.linsh.dialog.DialogHelper;
import com.linsh.dialog.loading.LoadingDialogHelper;
import com.linsh.utilseverywhere.ToastUtils;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/12/06
 *    desc   :
 * </pre>
 */
public abstract class BaseViewActivity<P extends Contract.Presenter> extends BaseMvpActivity<P> implements BaseContract.View<P> {

    private DialogHelper loadingHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected Activity getActivity() {
        return this;
    }

    @Override
    public void showToast(String text) {
        ToastUtils.show(text);
    }

    @Override
    public void showLoading() {
        if (loadingHelper == null) {
            loadingHelper = DialogComponents.create(this, LoadingDialogHelper.class);
        }
        loadingHelper.show();
    }

    @Override
    public void dismissLoading() {
        if (loadingHelper != null)
            loadingHelper.dismiss();
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
