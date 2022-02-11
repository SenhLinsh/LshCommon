package com.linsh.common.tools;

import android.app.Activity;

import com.linsh.dialog.DialogComponents;
import com.linsh.dialog.IDialog;
import com.linsh.dialog.text.ITextDialog;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2022/01/20
 *    desc   :
 * </pre>
 */
public class CommonDialogs {

    /**
     * 提示弹窗
     *
     * @param activity Activity
     * @param title    标题
     * @param message  内容
     */
    public static void showText(Activity activity, String title, String message) {
        DialogComponents.create(activity, ITextDialog.class)
                .setText(message)
                .setTitle(title)
                .setPositiveButton()
                .show();
    }

    /**
     * 常规警告提示弹窗
     * <p>
     * 点击确认后自动消失
     *
     * @param activity Activity
     * @param title    标题
     * @param message  内容
     * @param listener 确认回调
     */
    public static void showWarning(Activity activity, String title, String message, IDialog.OnClickListener listener) {
        DialogComponents.create(activity, ITextDialog.class)
                .setText(message)
                .setNegativeButton()
                .setPositiveButton(new IDialog.OnClickListener() {
                    @Override
                    public void onClick(IDialog dialog) {
                        dialog.dismiss();
                        if (listener != null) {
                            listener.onClick(dialog);
                        }
                    }
                })
                .setTitle(title)
                .show();
    }
}
