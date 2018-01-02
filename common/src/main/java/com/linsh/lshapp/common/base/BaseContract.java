package com.linsh.lshapp.common.base;

import android.content.Context;
import android.content.DialogInterface;

import com.linsh.dialog.LshColorDialog;

/**
 * Created by Senh Linsh on 17/4/25.
 */

public interface BaseContract {

    interface BaseView {

        Context getContext();

        void showTextDialog(String content);

        void showTextDialog(String title, String content);

        void showTextDialog(String content, LshColorDialog.OnPositiveListener onPositiveListener);

        void showTextDialog(String content, String positiveBtn, LshColorDialog.OnPositiveListener onPositiveListener);

        void showTextDialog(String content, LshColorDialog.OnPositiveListener onPositiveListener, LshColorDialog.OnNegativeListener onNegativeListener);

        void showTextDialog(String content, String positiveBtn, LshColorDialog.OnPositiveListener onPositiveListener,
                            String negativeBtn, LshColorDialog.OnNegativeListener onNegativeListener);

        void dismissTextDialog();

        void showLoadingDialog();

        void showLoadingDialog(String content);

        void showLoadingDialog(String content, boolean cancelable);

        void showLoadingDialog(String content, DialogInterface.OnCancelListener cancelListener);

        void setLoadingDialogText(String content);

        void dismissLoadingDialog();

        void showToast(String content);

        void finishActivity();

        void finishActivity(int resultCode);
    }

    interface BasePresenter<T extends BaseView> {

        void attachView(T view);

        void detachView();

        void subscribe();

        void unsubscribe();
    }
}
