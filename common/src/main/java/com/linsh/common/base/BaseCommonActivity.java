package com.linsh.common.base;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.linsh.base.LshThread;
import com.linsh.base.activity.base.BaseActivity;
import com.linsh.base.mvp.BaseMvpActivity;
import com.linsh.base.mvp.Contract;
import com.linsh.common.activity.ActivityResultSubscriber;
import com.linsh.common.tools.CommonDialogs;
import com.linsh.dialog.DialogComponents;
import com.linsh.dialog.IDialog;
import com.linsh.dialog.loading.IKeepScreenOnLoadingDialog;
import com.linsh.dialog.loading.ILoadingDialog;
import com.linsh.dialog.text.IListDialog;
import com.linsh.dialog.text.ITextDialog;
import com.linsh.utilseverywhere.HandlerUtils;
import com.linsh.utilseverywhere.ToastUtils;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/02/28
 *    desc   :
 * </pre>
 */
public class BaseCommonActivity<P extends Contract.Presenter> extends BaseMvpActivity<P> implements CommonContract.View {

    private static final String TAG = "BaseCommonActivity";
    private IDialog dialogHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscribe(ActivityResultSubscriber.class);
        addMvpCallAdapter(new MvpAnnotationEnhancer());
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public BaseActivity getBaseActivity() {
        return this;
    }

    public BaseCommonActivity<P> getCommonActivity() {
        return this;
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
        dialogHelper = DialogComponents.create(this, ITextDialog.class)
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
    }

    @Override
    public void dismissTextDialog() {
        HandlerUtils.postRunnable(() -> {
            dialogHelper.dismiss();
        });
    }

    @Override
    public void showListDialog(String title, List<? extends CharSequence> items,
                               IDialog.OnItemClickListener onItemClickListener, IDialog.OnItemClickListener onItemLongClickListener) {
        dialogHelper = DialogComponents.create(this, IListDialog.class)
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
                .show();
    }

    @Override
    public void dismissListDialog() {
        HandlerUtils.postRunnable(() -> {
            dialogHelper.dismiss();
        });
    }

    @Override
    public void showInputDialog(String title, String hint, String text, IDialog.OnClickListener listener) {
        HandlerUtils.postRunnable(() -> {
            CommonDialogs.showInput(this, title, hint, text, dialog -> LshThread.presenter(() -> listener.onClick(dialog)));
        });
    }

    @Override
    public void showLoadingDialog() {
        DialogComponents.create(this, ILoadingDialog.class)
                .show();
    }

    @Override
    public void showLoadingDialog(boolean keepScreenOn) {
        DialogComponents.create(this, IKeepScreenOnLoadingDialog.class)
                .keepScreenOn(true)
                .show();
    }

    @Override
    public void updateLoadingDialog(String progress) {
        IKeepScreenOnLoadingDialog dialog = DialogComponents.find(this, IKeepScreenOnLoadingDialog.class);
        if (dialog != null) {
            dialog.setProgress(progress)
                    .show();
        }
    }

    @Override
    public void dismissLoadingDialog() {
        DialogComponents.dismissAll(this);
    }

    @Override
    public void showToast(String content) {
        ToastUtils.showLong(content);
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
