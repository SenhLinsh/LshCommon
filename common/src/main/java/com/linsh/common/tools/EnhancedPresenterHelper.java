package com.linsh.common.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.linsh.base.activity.Contract;
import com.linsh.base.activity.impl.DelegateActivity;
import com.linsh.dialog.DialogComponents;
import com.linsh.dialog.DialogHelper;
import com.linsh.dialog.text.TextDialogHelper;
import com.linsh.lshutils.utils.ToastUtilsEx;
import com.linsh.utilseverywhere.ClassUtils;
import com.linsh.utilseverywhere.HandlerUtils;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/01/05
 *    desc   :
 * </pre>
 */
public class EnhancedPresenterHelper {

    private DelegateActivity activity;
    private DialogHelper dialogHelper;

    public EnhancedPresenterHelper(Contract.View view) {
        if (view instanceof DelegateActivity) {
            activity = (DelegateActivity) view;
        } else {
            try {
                view = (Contract.View) ClassUtils.getField(ClassUtils.getField(ClassUtils.getField(view, "h", true), "this$0", true), "originView", true);
                if (view instanceof DelegateActivity) {
                    activity = (DelegateActivity) view;
                } else {
                    activity = (DelegateActivity) ClassUtils.getField(view, "activity", true);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Activity getActivity() {
        return activity;
    }

    public Context getContext() {
        return activity;
    }

    public Intent getIntent() {
        return activity.getIntent();
    }

    public void showLoadingDialog() {
        showLoadingDialog("加载中...");
    }

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

    public void dismissLoadingDialog() {
        if (dialogHelper != null) {
            HandlerUtils.postRunnable(() -> {
                dialogHelper.dismiss();
            });
        }
    }

    public void showToast(String content) {
        ToastUtilsEx.postShowLong(content);
    }

    public void finishActivity() {
        HandlerUtils.postRunnable(() -> {
            activity.finish();
        });
    }

    public void finishActivity(int resultCode) {
        HandlerUtils.postRunnable(() -> {
            activity.finishActivity(resultCode);
        });
    }
}
