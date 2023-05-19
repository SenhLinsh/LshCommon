package com.linsh.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.linsh.base.LshThread;
import com.linsh.base.activity.impl.DelegateActivity;
import com.linsh.base.mvp.BaseMvpViewImpl;
import com.linsh.base.mvp.Contract;
import com.linsh.common.tools.CommonDialogs;
import com.linsh.dialog.DialogComponents;
import com.linsh.dialog.IDialog;
import com.linsh.dialog.loading.IKeepScreenOnLoadingDialog;
import com.linsh.dialog.loading.ILoadingDialog;
import com.linsh.dialog.text.IListDialog;
import com.linsh.dialog.text.ITextDialog;
import com.linsh.utilseverywhere.ToastUtils;

import java.util.List;

class CommonViewImpl extends BaseMvpViewImpl<Contract.Presenter> implements CommonContract.View {

    private DelegateActivity activity;

    public CommonViewImpl(Contract.View view) {
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
    public void setActivityTitle(CharSequence title) {
        LshThread.ui(() -> activity.setTitle(title));
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
        LshThread.ui(() -> {
            DialogComponents.create(activity, ITextDialog.class)
                    .setText(content)
                    .setTitle(title)
                    .setPositiveButton(positiveBtn, onPositiveListener == null ? null : dialog -> {
                        dialog.dismiss();
                        LshThread.presenter(() -> onPositiveListener.onClick(dialog));
                    })
                    .setNegativeButton(negativeBtn, onNegativeListener == null ? null : dialog -> {
                        dialog.dismiss();
                        LshThread.presenter(() -> onNegativeListener.onClick(dialog));
                    })
                    .show();
        });
    }

    @Override
    public void dismissTextDialog() {
        LshThread.ui(() -> {
            DialogComponents.dismissAll(getActivity());
        });
    }

    @Override
    public void showListDialog(String title, List<? extends CharSequence> items,
                               IDialog.OnItemClickListener onItemClickListener, IDialog.OnItemClickListener onItemLongClickListener) {
        LshThread.ui(() -> DialogComponents.create(activity, IListDialog.class)
                .setItems(items)
                .setOnItemClickListener(onItemClickListener == null ? null : (IDialog.OnItemClickListener) (dialog, position) -> {
                    dialog.dismiss();
                    LshThread.presenter(() -> onItemClickListener.onItemClick(dialog, position));
                })
                .setOnItemLongClickListener(onItemLongClickListener == null ? null : (IDialog.OnItemClickListener) (dialog, position) -> {
                    dialog.dismiss();
                    LshThread.presenter(() -> onItemLongClickListener.onItemClick(dialog, position));
                })
                .setTitle(title)
                .show());
    }

    @Override
    public void dismissListDialog() {
        LshThread.ui(() -> {
            DialogComponents.dismissAll(getActivity());
        });
    }

    @Override
    public void showInputDialog(String title, String hint, String text, IDialog.OnClickListener listener) {
        LshThread.ui(() -> {
            CommonDialogs.showInput(activity, title, hint, text, dialog -> LshThread.presenter(() -> listener.onClick(dialog)));
        });
    }

    @Override
    public void showLoadingDialog() {
        LshThread.ui(() -> {
            DialogComponents.create(activity, ILoadingDialog.class)
                    .show();
        });
    }

    @Override
    public void showLoadingDialog(boolean keepScreenOn) {
        LshThread.ui(() -> {
            DialogComponents.create(activity, IKeepScreenOnLoadingDialog.class)
                    .keepScreenOn(true)
                    .show();
        });
    }

    @Override
    public void updateLoadingDialog(String progress) {
        LshThread.ui(() -> {
            IKeepScreenOnLoadingDialog dialog = DialogComponents.find(activity, IKeepScreenOnLoadingDialog.class);
            if (dialog != null) {
                dialog.setProgress(progress)
                        .show();
            }
        });
    }

    @Override
    public void dismissLoadingDialog() {
        LshThread.ui(() -> {
            DialogComponents.dismissAll(getActivity());
        });
    }

    @Override
    public void showToast(String content) {
        LshThread.ui(() -> {
            ToastUtils.showLong(content);
        });
    }

    @Override
    public void finishActivity() {
        LshThread.ui(() -> {
            activity.finish();
        });
    }

    @Override
    public void finishActivity(int resultCode) {
        LshThread.ui(() -> {
            activity.finishActivity(resultCode);
        });
    }
}