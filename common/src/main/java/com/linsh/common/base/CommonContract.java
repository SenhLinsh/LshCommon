package com.linsh.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.linsh.base.mvp.Contract;
import com.linsh.dialog.IDialog;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/02/28
 *    desc   :
 * </pre>
 */
public interface CommonContract {

    interface View extends Contract.View {

        Activity getActivity();

        Context getContext();

        Intent getIntent();

        void setActivityTitle(CharSequence title);

        void showTextDialog(String content);

        void showTextDialog(String title, String content);

        void showTextDialog(String content, IDialog.OnClickListener onPositiveListener);

        void showTextDialog(String content, String positiveBtn, IDialog.OnClickListener onPositiveListener);

        void showTextDialog(String content, IDialog.OnClickListener onPositiveListener, IDialog.OnClickListener onNegativeListener);

        void showTextDialog(String content, String positiveBtn, IDialog.OnClickListener onPositiveListener,
                            String negativeBtn, IDialog.OnClickListener onNegativeListener);

        void dismissTextDialog();

        void showListDialog(String title, List<? extends CharSequence> items,
                            IDialog.OnItemClickListener onItemClickListener, IDialog.OnItemClickListener onItemLongClickListener);

        void dismissListDialog();

        void showInputDialog(String title, String hint, String text, IDialog.OnClickListener listener);

        void showLoadingDialog();

        void showLoadingDialog(boolean keepScreenOn);

        void updateLoadingDialog(String progress);

        void dismissLoadingDialog();

        void showToast(String content);

        void finishActivity();

        void finishActivity(int resultCode);
    }
}
