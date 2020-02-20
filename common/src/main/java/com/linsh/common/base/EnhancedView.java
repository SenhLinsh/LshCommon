package com.linsh.common.base;

import android.app.Activity;
import android.content.Context;

import com.linsh.base.activity.Contract;
import com.linsh.dialog.DialogHelper;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/12/15
 *    desc   :
 * </pre>
 */
public interface EnhancedView<P extends Contract.Presenter> extends Contract.View<P> {

    Activity getActivity();

    Context getContext();

    void showTextDialog(String content);

    void showTextDialog(String title, String content);

    void showTextDialog(String content, DialogHelper.OnClickListener onPositiveListener);

    void showTextDialog(String content, String positiveBtn, DialogHelper.OnClickListener onPositiveListener);

    void showTextDialog(String content, DialogHelper.OnClickListener onPositiveListener, DialogHelper.OnClickListener onNegativeListener);

    void showTextDialog(String content, String positiveBtn, DialogHelper.OnClickListener onPositiveListener,
                        String negativeBtn, DialogHelper.OnClickListener onNegativeListener);

    void dismissTextDialog();

    void showLoadingDialog();

    void showLoadingDialog(String content);

    void dismissLoadingDialog();

    void showToast(String content);

    void finishActivity();

    void finishActivity(int resultCode);
}
