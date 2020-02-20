package com.linsh.common.base;

import android.app.Activity;
import android.content.Context;

import com.linsh.base.activity.Contract;
import com.linsh.base.activity.impl.DelegateActivity;
import com.linsh.dialog.DialogComponents;
import com.linsh.dialog.DialogHelper;
import com.linsh.dialog.text.TextDialogHelper;
import com.linsh.lshutils.utils.ToastUtilsEx;
import com.linsh.utilseverywhere.ClassUtils;
import com.linsh.utilseverywhere.HandlerUtils;

class EnhancedViewImpl implements EnhancedView {

    private Contract.View view;
    private DelegateActivity activity;
    private DialogHelper dialogHelper;

    public EnhancedViewImpl(Contract.View view) {
        this.view = view;
        if (view instanceof DelegateActivity) {
            activity = (DelegateActivity) view;
        } else {
            try {
                activity = (DelegateActivity) ClassUtils.getField(ClassUtils.getField(ClassUtils.getField(view, "h", true), "this$0", true), "originView", true);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    public void showTextDialog(String content) {
        showTextDialog(null, content, null, null, null, null);
    }

    @Override
    public void showTextDialog(String title, String content) {
        showTextDialog(title, content, null, null, null, null);
    }

    @Override
    public void showTextDialog(String content, DialogHelper.OnClickListener onPositiveListener) {
        showTextDialog(null, content, "确认", onPositiveListener, null, null);
    }

    @Override
    public void showTextDialog(String content, String positiveBtn, DialogHelper.OnClickListener onPositiveListener) {
        showTextDialog(null, content, positiveBtn, onPositiveListener, null, null);
    }

    @Override
    public void showTextDialog(String content, DialogHelper.OnClickListener onPositiveListener, DialogHelper.OnClickListener onNegativeListener) {
        showTextDialog(null, content, "确认", onPositiveListener, "取消", onNegativeListener);
    }

    @Override
    public void showTextDialog(String content, String positiveBtn, DialogHelper.OnClickListener onPositiveListener, String negativeBtn, DialogHelper.OnClickListener onNegativeListener) {
        showTextDialog(null, content, positiveBtn, onPositiveListener, negativeBtn, onNegativeListener);
    }

    private void showTextDialog(String title, String content,
                                String positiveBtn, DialogHelper.OnClickListener onPositiveListener,
                                String negativeBtn, DialogHelper.OnClickListener onNegativeListener) {
        HandlerUtils.postRunnable(() -> {
            dialogHelper = DialogComponents.create(activity, TextDialogHelper.class)
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
            dialogHelper = DialogComponents.create(activity, TextDialogHelper.class)
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

    @Override
    public Contract.Presenter getPresenter() {
        return view.getPresenter();
    }
}