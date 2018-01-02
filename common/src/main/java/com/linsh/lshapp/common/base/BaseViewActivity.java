package com.linsh.lshapp.common.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.linsh.dialog.LshColorDialog;
import com.linsh.lshapp.common.R;
import com.linsh.lshapp.common.view.ShapeLoadingDialog;
import com.linsh.utilseverywhere.ResourceUtils;
import com.linsh.utilseverywhere.StringUtils;
import com.linsh.utilseverywhere.ToastUtils;

import java.util.List;

/**
 * Created by Senh Linsh on 17/4/24.
 */

public abstract class BaseViewActivity<T extends BaseContract.BasePresenter> extends BaseActivity implements BaseContract.BaseView {

    protected T mPresenter;

    protected LshColorDialog mLshColorDialog;
    protected ShapeLoadingDialog mShapeLoadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化Presenter
        mPresenter = initPresenter();
        mPresenter.attachView(this);
    }

    protected abstract T initPresenter();

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.subscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unsubscribe();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void showTextDialog(String content) {
        showTextDialog(null, content);
    }

    @Override
    public void showTextDialog(String title, String content) {
        mLshColorDialog = getTextDialog(title, content, null, null, null, null, false).show();
    }

    @Override
    public void showTextDialog(String content, LshColorDialog.OnPositiveListener onPositiveListener) {
        showTextDialog(content, null, onPositiveListener);
    }

    @Override
    public void showTextDialog(String content, String positiveBtn, LshColorDialog.OnPositiveListener onPositiveListener) {
        mLshColorDialog = getTextDialog(null, content, positiveBtn, onPositiveListener, null, null, false).show();
    }

    @Override
    public void showTextDialog(String content, LshColorDialog.OnPositiveListener onPositiveListener, LshColorDialog.OnNegativeListener onNegativeListener) {
        showTextDialog(content, null, onPositiveListener, null, onNegativeListener);
    }

    @Override
    public void showTextDialog(String content, String positiveBtn, LshColorDialog.OnPositiveListener onPositiveListener, String negativeBtn, LshColorDialog.OnNegativeListener onNegativeListener) {
        mLshColorDialog = getTextDialog(null, content, positiveBtn, onPositiveListener, negativeBtn, onNegativeListener, true).show();
    }

    public LshColorDialog.TextDialogBuilder getTextDialog(String content, String positive, LshColorDialog.OnPositiveListener
            onPositiveListener, String negative, LshColorDialog.OnNegativeListener onNegativeListener) {
        return getTextDialog(null, content, positive, onPositiveListener, negative, onNegativeListener, false);
    }

    public LshColorDialog.TextDialogBuilder getTextDialog(String title, String content, String positive, LshColorDialog.OnPositiveListener
            onPositiveListener, String negative, LshColorDialog.OnNegativeListener onNegativeListener, boolean defaultNegativeBtn) {
        if (mLshColorDialog != null && mLshColorDialog.isShowing()) {
            mLshColorDialog.dismiss();
        }
        LshColorDialog.TextDialogBuilder dialogBuilder =
                new LshColorDialog(getActivity())
                        .buildText()
                        .setBgColor(ResourceUtils.getColor(R.color.color_theme_dark_blue))
                        .setTitle(StringUtils.isEmpty(title) ? "提示" : title)
                        .setContent(content);

        dialogBuilder.setPositiveButton(positive == null ? "确定" : positive, onPositiveListener);

        if (onNegativeListener != null || defaultNegativeBtn)
            dialogBuilder.setNegativeButton(negative == null ? "取消" : negative, onNegativeListener);

        return dialogBuilder;
    }

    public void showListDialog(String title, List<String> data, LshColorDialog.OnItemClickListener listener) {
        if (mLshColorDialog != null && mLshColorDialog.isShowing()) {
            mLshColorDialog.dismiss();
        }
        mLshColorDialog = new LshColorDialog(getActivity())
                .buildList()
                .setTitle(title)
                .setList(data)
                .setOnItemClickListener(listener)
                .show();
    }

    @Override
    public void showLoadingDialog() {
        showLoadingDialog(null, null);
    }

    @Override
    public void showLoadingDialog(String content) {
        showLoadingDialog(content, null);
    }

    @Override
    public void showLoadingDialog(String content, boolean cancelable) {
        showLoadingDialog(content, null);
        if (cancelable) {
            mShapeLoadingDialog.setCancelable(true);
        }
    }

    @Override
    public void showLoadingDialog(String content, DialogInterface.OnCancelListener cancelListener) {
        if (mShapeLoadingDialog == null)
            mShapeLoadingDialog = new ShapeLoadingDialog(this);
        mShapeLoadingDialog.setLoadingText(content == null ? "正在加载中..." : content);
        if (cancelListener != null) {
            mShapeLoadingDialog.setOnCancelListener(cancelListener);
        } else {
            mShapeLoadingDialog.setCancelable(false);
        }
        if (!isOnDestroyed() && !mShapeLoadingDialog.isShowing()) mShapeLoadingDialog.show();
    }

    @Override
    public void setLoadingDialogText(String content) {
        if (mShapeLoadingDialog != null && mShapeLoadingDialog.isShowing()) {
            mShapeLoadingDialog.setLoadingText(content == null ? "正在加载中..." : content);
        }
    }

    @Override
    public void dismissTextDialog() {
        if (!isOnDestroyed() && mLshColorDialog != null && mLshColorDialog.isShowing()) {
            mLshColorDialog.dismiss();
        }
    }

    @Override
    public void dismissLoadingDialog() {
        if (!isOnDestroyed() && mShapeLoadingDialog != null && mShapeLoadingDialog.getDialog().isShowing()) {
            mShapeLoadingDialog.dismiss();
        }
    }

    @Override
    public void showToast(String content) {
        ToastUtils.show(content);
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void finishActivity(int resultCode) {
        setResult(resultCode);
        finish();
    }

    @Override
    protected int getScreenOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        // 关闭弹出窗口
        if (mLshColorDialog != null && mLshColorDialog.isShowing()) {
            mLshColorDialog.dismiss();
        }
        if (mShapeLoadingDialog != null && mShapeLoadingDialog.getDialog().isShowing()) {
            mShapeLoadingDialog.dismiss();
        }
    }
}
