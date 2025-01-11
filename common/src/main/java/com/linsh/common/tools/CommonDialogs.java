package com.linsh.common.tools;

import android.content.Context;

import com.linsh.dialog.DialogComponents;
import com.linsh.dialog.IDialog;
import com.linsh.dialog.text.IInputDialog;
import com.linsh.dialog.text.IListDialog;
import com.linsh.dialog.text.ITextDialog;

import java.util.List;

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
     * @param context context
     * @param title   标题
     * @param message 内容
     */
    public static void showText(Context context, String title, String message) {
        DialogComponents.create(context, ITextDialog.class)
                .setText(message)
                .setTitle(title)
                .setPositiveButton()
                .show();
    }

    /**
     * 提示弹窗
     *
     * @param context context
     * @param title   标题
     * @param message 内容
     */
    public static void showText(Context context, String title, String message, IDialog.OnClickListener confirmListener) {
        DialogComponents.create(context, ITextDialog.class)
                .setText(message)
                .setTitle(title)
                .setPositiveButton(confirmListener)
                .show();
    }

    /**
     * 提示弹窗
     *
     * @param context context
     * @param title   标题
     * @param message 内容
     */
    public static void showText(Context context, String title, String message, IDialog.OnClickListener confirmListener, IDialog.OnClickListener cancelListener) {
        DialogComponents.create(context, ITextDialog.class)
                .setText(message)
                .setTitle(title)
                .setPositiveButton(confirmListener)
                .setNegativeButton(cancelListener)
                .show();
    }

    /**
     * 输入弹窗
     *
     * @param context  context
     * @param title    标题
     * @param listener 确认点击事件
     */
    public static void showInput(Context context, String title, IDialog.OnClickListener listener) {
        showInput(context, title, null, null, listener);
    }

    /**
     * 输入弹窗
     *
     * @param context  context
     * @param title    标题
     * @param hint     提示
     * @param listener 确认点击事件
     */
    public static void showInput(Context context, String title, String text, String hint, IDialog.OnClickListener listener) {
        DialogComponents.create(context, IInputDialog.class)
                .showKeyboard(true)
                .setHint(hint)
                .setText(text)
                .setTitle(title)
                .setNegativeButton()
                .setPositiveButton(listener)
                .show();
    }

    /**
     * 列表弹窗
     *
     * @param context  context
     * @param title    标签
     * @param items    列表
     * @param listener 点击事件
     */
    public static void showList(Context context, String title, List<? extends CharSequence> items, IDialog.OnItemClickListener listener) {
        DialogComponents.create(context, IListDialog.class)
                .setItems(items)
                .setOnItemClickListener(listener)
                .setTitle(title)
                .show();
    }

    /**
     * 列表弹窗
     *
     * @param context  context
     * @param title    标签
     * @param items    列表
     * @param listener 点击事件
     */
    public static void showList(Context context, String title, List<? extends CharSequence> items, IDialog.OnItemClickListenerEx<CharSequence> listener) {
        DialogComponents.create(context, IListDialog.class)
                .setItems(items)
                .setOnItemClickListener(listener == null ? null :
                        (dialog, position) -> listener.onItemClick(dialog, items.get(position), position))
                .setTitle(title)
                .show();
    }

    /**
     * 列表弹窗
     *
     * @param context  context
     * @param title    标签
     * @param items    列表
     * @param listener 点击事件
     */
    public static void showList(Context context, String title, CharSequence[] items, IDialog.OnItemClickListener listener) {
        DialogComponents.create(context, IListDialog.class)
                .setItems(items)
                .setOnItemClickListener(listener)
                .setTitle(title)
                .show();
    }

    /**
     * 列表弹窗
     *
     * @param context  context
     * @param title    标签
     * @param items    列表
     * @param listener 点击事件
     */
    public static void showList(Context context, String title, CharSequence[] items, IDialog.OnItemClickListenerEx<CharSequence> listener) {
        DialogComponents.create(context, IListDialog.class)
                .setItems(items)
                .setOnItemClickListener(listener == null ? null :
                        (dialog, position) -> listener.onItemClick(dialog, items[position], position))
                .setTitle(title)
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
    public static void showWarning(Context context, String title, String message, IDialog.OnClickListener listener) {
        DialogComponents.create(context, ITextDialog.class)
                .setText(message)
                .setNegativeButton()
                .setPositiveButton(dialog -> {
                    dialog.dismiss();
                    if (listener != null) {
                        listener.onClick(dialog);
                    }
                })
                .setTitle(title)
                .show();
    }
}
