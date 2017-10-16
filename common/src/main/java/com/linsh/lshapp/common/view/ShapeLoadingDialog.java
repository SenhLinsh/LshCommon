package com.linsh.lshapp.common.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.linsh.lshapp.common.R;


/**
 * Created by zzz40500 on 15/6/15.
 */
public class ShapeLoadingDialog {


    private Context mContext;
    private Dialog mDialog;
    private LoadingView mLoadingView;
    private View mDialogContentView;


    public ShapeLoadingDialog(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        mDialog = new Dialog(mContext, R.style.ShapeLoadingDialog);
        mDialogContentView = LayoutInflater.from(mContext).inflate(R.layout.layout_loading_dialog, null);

        mLoadingView = (LoadingView) mDialogContentView.findViewById(R.id.loadView);
        mDialog.setContentView(mDialogContentView);
    }

    public void setBackground(int color) {
        GradientDrawable gradientDrawable = (GradientDrawable) mDialogContentView.getBackground();
        gradientDrawable.setColor(color);
    }

    public void setLoadingText(CharSequence charSequence) {
        mLoadingView.setLoadingText(charSequence);
    }

    public void show() {
        mLoadingView.startLoading(200);
        mDialog.show();
    }

    public void dismiss() {
        try {
            if (mDialog.isShowing()) {
                mLoadingView.stopLoading();
                mDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }

    public Dialog getDialog() {
        return mDialog;
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
    }

    public void setCancelable(boolean cancel) {
        mDialog.setCancelable(cancel);
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener listener) {
        mDialog.setOnCancelListener(listener);
    }
}
