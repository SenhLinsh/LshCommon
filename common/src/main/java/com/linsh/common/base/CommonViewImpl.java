package com.linsh.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.linsh.base.mvp.BaseMvpViewImpl;
import com.linsh.base.mvp.Contract;
import com.linsh.base.activity.impl.DelegateActivity;
import com.linsh.dialog.DialogComponents;
import com.linsh.dialog.IDialog;
import com.linsh.dialog.text.ITextDialog;
import com.linsh.lshutils.utils.ToastUtilsEx;
import com.linsh.utilseverywhere.HandlerUtils;

class CommonViewImpl extends BaseMvpViewImpl<Contract.Presenter> implements CommonContract.View {

    private Contract.View view;
    private DelegateActivity activity;
    private IDialog dialogHelper;

    public CommonViewImpl(Contract.View view) {
        this.view = view;
        Context context = view.getContext();
        if (context instanceof DelegateActivity) {
            activity = (DelegateActivity) context;
        }
        if (activity == null) {
            throw new IllegalArgumentException("can not get activity from view");
        }
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    @Override
    public Context getContext() {
        return activity;
    }

    @Override
    public Intent getIntent() {
        return activity.getIntent();
    }

    @Override
    public void showTextDialog(String content) {
        showTextDialog(null, content, null, null, null, null);
    }

    @Override
    public void showTextDialog(String title, String content) {
        showTextDialog(title, content, null, null, null, null);
    }

    @Override
    public void showTextDialog(String content, IDialog.OnClickListener onPositiveListener) {
        showTextDialog(null, content, "确认", onPositiveListener, null, null);
    }

    @Override
    public void showTextDialog(String content, String positiveBtn, IDialog.OnClickListener onPositiveListener) {
        showTextDialog(null, content, positiveBtn, onPositiveListener, null, null);
    }

    @Override
    public void showTextDialog(String content, IDialog.OnClickListener onPositiveListener, IDialog.OnClickListener onNegativeListener) {
        showTextDialog(null, content, "确认", onPositiveListener, "取消", onNegativeListener);
    }

    @Override
    public void showTextDialog(String content, String positiveBtn, IDialog.OnClickListener onPositiveListener, String negativeBtn, IDialog.OnClickListener onNegativeListener) {
        showTextDialog(null, content, positiveBtn, onPositiveListener, negativeBtn, onNegativeListener);
    }

    private void showTextDialog(String title, String content,
                                String positiveBtn, IDialog.OnClickListener onPositiveListener,
                                String negativeBtn, IDialog.OnClickListener onNegativeListener) {
        HandlerUtils.postRunnable(() -> {
            dialogHelper = DialogComponents.create(activity, ITextDialog.class)
                    .setText(content)
                    .setTitle(title)
                    .setPositiveButton(positiveBtn, onPositiveListener)
                    .setNegativeButton(negativeBtn, onNegativeListener)
                    .show();
        });
    }

    @Override
    public void dismissTextDialog() {
        if (dialogHelper != null) {
            HandlerUtils.postRunnable(() -> {
                dialogHelper.dismiss();
            });
        }
    }

    @Override
    public void showLoadingDialog() {
        showLoadingDialog("加载中...");
    }

    @Override
    public void showLoadingDialog(String content) {
        HandlerUtils.postRunnable(() -> {
            if (dialogHelper != null) {
                dialogHelper.dismiss();
            }
            dialogHelper = DialogComponents.create(activity, ITextDialog.class)
                    .setText(content)
                    .show();
        });
    }

    @Override
    public void dismissLoadingDialog() {
        if (dialogHelper != null) {
            HandlerUtils.postRunnable(() -> {
                dialogHelper.dismiss();
            });
        }
    }

    @Override
    public void showToast(String content) {
        ToastUtilsEx.postShowLong(content);
    }

    @Override
    public void finishActivity() {
        HandlerUtils.postRunnable(() -> {
            activity.finish();
        });
    }

    @Override
    public void finishActivity(int resultCode) {
        HandlerUtils.postRunnable(() -> {
            activity.finishActivity(resultCode);
        });
    }
}