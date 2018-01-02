package com.linsh.lshapp.common.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.linsh.dialog.LshColorDialog;


/**
 * Created by Senh Linsh on 17/4/24.
 */

public abstract class BaseViewFragment<T extends BaseViewActivity, P extends BaseContract.BasePresenter> extends BaseFragment<T> implements BaseContract.BaseView {

    protected P mPresenter;

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = initPresenter();
        mPresenter.attachView(this);
    }

    protected abstract P initPresenter();

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.unsubscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void showTextDialog(String content) {
        getMyActivity().showTextDialog(content);
    }

    @Override
    public void showTextDialog(String title, String content) {
        getMyActivity().showTextDialog(title, content);
    }

    @Override
    public void showTextDialog(String content, LshColorDialog.OnPositiveListener onPositiveListener) {
        getMyActivity().showTextDialog(content, onPositiveListener);
    }

    @Override
    public void showTextDialog(String content, String positiveBtn, LshColorDialog.OnPositiveListener onPositiveListener) {
        getMyActivity().showTextDialog(content, positiveBtn, onPositiveListener);
    }

    @Override
    public void showTextDialog(String content, LshColorDialog.OnPositiveListener onPositiveListener, LshColorDialog.OnNegativeListener onNegativeListener) {
        getMyActivity().showTextDialog(content, onPositiveListener, onNegativeListener);
    }

    @Override
    public void showTextDialog(String content, String positiveBtn, LshColorDialog.OnPositiveListener onPositiveListener, String negativeBtn, LshColorDialog.OnNegativeListener onNegativeListener) {
        getMyActivity().showTextDialog(content, positiveBtn, onPositiveListener, negativeBtn, onNegativeListener);
    }

    @Override
    public void dismissTextDialog() {
        getMyActivity().dismissTextDialog();
    }

    @Override
    public void showLoadingDialog() {
        getMyActivity().showLoadingDialog();
    }

    @Override
    public void showLoadingDialog(String content) {
        getMyActivity().showLoadingDialog(content);
    }

    @Override
    public void showLoadingDialog(String content, boolean cancelable) {
        getMyActivity().showLoadingDialog(content, cancelable);
    }

    @Override
    public void showLoadingDialog(String content, DialogInterface.OnCancelListener cancelListener) {
        getMyActivity().showLoadingDialog(content, cancelListener);
    }

    @Override
    public void dismissLoadingDialog() {
        getMyActivity().dismissLoadingDialog();
    }

    @Override
    public void showToast(String content) {
        getMyActivity().showToast(content);
    }

    @Override
    public void finishActivity() {
        getMyActivity().finish();
    }

    @Override
    public void finishActivity(int resultCode) {
        getMyActivity().finishActivity(resultCode);
    }
}
