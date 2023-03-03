package com.linsh.common.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.linsh.lshutils.utils.Dps;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/03/03
 *    desc   :
 * </pre>
 */
public class SimpleDialogs {

    /**
     * 提示弹窗
     *
     * @param context context
     * @param title   标题
     * @param message 内容
     */
    public static void showText(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .show();
    }

    /**
     * 输入弹窗
     *
     * @param context  context
     * @param title    标题
     * @param listener 确认点击事件
     */
    public static void showInput(Context context, String title, OnClickListener2<CharSequence> listener) {
        EditText editText = new EditText(context);
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(editText)
                .setPositiveButton("确定", (dialog, which) -> {
                    if (listener != null) listener.onClick(dialog, editText.getText());
                })
                .show();
        ViewGroup.LayoutParams layoutParams = editText.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin = Dps.toPx(20);
            ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin = Dps.toPx(20);
        }
    }

    /**
     * 列表弹窗
     *
     * @param context  context
     * @param title    标签
     * @param items    列表
     * @param listener 点击事件
     */
    public static void showList(Context context, String title, CharSequence[] items, OnItemClickListener<CharSequence> listener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setItems(items, (dialog, which) -> {
                    if (listener != null) listener.onClick(dialog, items[which], which);
                })
                .show();
    }

    /**
     * 常规警告提示弹窗
     * <p>
     * 点击确认后自动消失
     *
     * @param context  context
     * @param title    标题
     * @param message  内容
     * @param listener 确认回调
     */
    public static void showWarning(Context context, String title, String message, OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", (dialog, which) -> {
                    if (listener != null) {
                        listener.onClick(dialog);
                    }
                })
                .setNegativeButton("取消", null)
                .setTitle(title)
                .show();
    }

    public interface OnClickListener {
        void onClick(DialogInterface dialog);
    }

    public interface OnClickListener2<T> {
        void onClick(DialogInterface dialog, T result);
    }

    public interface OnItemClickListener<T> {
        void onClick(DialogInterface dialog, T item, int position);
    }
}
