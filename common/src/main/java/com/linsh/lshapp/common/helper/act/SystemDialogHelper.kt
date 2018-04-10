package com.linsh.lshapp.common.helper.act

import android.app.Activity
import android.app.Dialog
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import com.linsh.base.dialog.*
import com.linsh.base.helper.ActivityHelper
import com.linsh.base.helper.interf.DialogHelperInterface
import com.linsh.utilseverywhere.UnitConverseUtils

/**
 * <pre>
 * author : Senh Linsh
 * github : https://github.com/SenhLinsh
 * date   : 2018/03/27
 * desc   : 通过系统 AlertDialog 实现一套弹出窗口
 * </pre>
 */
open class SystemDialogHelper(val activity: Activity) : ActivityHelper(), DialogHelperInterface {

    private var dialog: Dialog? = null

    override fun showTextDialog(title: CharSequence?, content: CharSequence,
                                positiveBtn: CharSequence?, onPositiveListener: DialogInterface.OnClickListener?,
                                negativeBtn: CharSequence?, onNegativeListener: DialogInterface.OnClickListener?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title ?: "提示")
        builder.setMessage(content)
        if (positiveBtn != null || onPositiveListener != null)
            builder.setPositiveButton(positiveBtn ?: "确定", getOnClickListener(onPositiveListener))
        if (negativeBtn != null || onNegativeListener != null)
            builder.setNegativeButton(negativeBtn ?: "取消", getOnClickListener(onNegativeListener))
        dialog = builder.show()
        return dialog!!
    }

    override fun showTextDialog(content: CharSequence,
                                positiveBtn: CharSequence?, onPositiveListener: DialogInterface.OnClickListener?,
                                negativeBtn: CharSequence?, onNegativeListener: DialogInterface.OnClickListener?): Dialog {
        return showTextDialog("提示", content, positiveBtn, onPositiveListener, negativeBtn, onNegativeListener)
    }

    override fun showInputDialog(title: CharSequence?, content: CharSequence?, hint: CharSequence?,
                                 positiveBtn: CharSequence?, onPositiveListener: InputDialogInterface.OnClickListener?,
                                 negativeBtn: CharSequence?, onNegativeListener: InputDialogInterface.OnClickListener?): Dialog {
        val builder = AlertDialog.Builder(activity)
        if (title != null) builder.setTitle(title)
        val editText = EditText(activity)
        builder.setView(editText)
        if (positiveBtn != null || onPositiveListener != null)
            builder.setPositiveButton(positiveBtn ?: "确定", { dialog, which ->
                if (onPositiveListener != null) onPositiveListener.onClick(dialog, editText.text.toString())
                else dialog.dismiss()
            })
        if (negativeBtn != null || onNegativeListener != null)
            builder.setNegativeButton(negativeBtn ?: "取消", { dialog, which ->
                if (onNegativeListener != null) onNegativeListener.onClick(dialog, editText.text.toString())
                else dialog.dismiss()
            })
        dialog = builder.show()
        return dialog!!
    }

    override fun showListDialog(title: CharSequence?, items: Array<String>, listener: ListDialogInterface.OnItemClickListener?,
                                positiveBtn: CharSequence?, onPositiveListener: DialogInterface.OnClickListener?,
                                negativeBtn: CharSequence?, onNegativeListener: DialogInterface.OnClickListener?): Dialog {
        val builder = AlertDialog.Builder(activity)
        if (title != null) builder.setTitle(title)
        builder.setItems(items, if (listener != null) android.content.DialogInterface.OnClickListener { dialog, which ->
            listener.onClick(dialog, items, which)
        } else null)
        if (positiveBtn != null || onPositiveListener != null)
            builder.setPositiveButton(positiveBtn ?: "确定", getOnClickListener(onPositiveListener))
        if (negativeBtn != null || onNegativeListener != null)
            builder.setNegativeButton(negativeBtn ?: "取消", getOnClickListener(onPositiveListener))
        dialog = builder.show()
        return dialog!!
    }

    override fun showListDialog(title: CharSequence?, items: List<String>, listener: ListDialogInterface.OnItemClickListener?,
                                positiveBtn: CharSequence?, onPositiveListener: DialogInterface.OnClickListener?,
                                negativeBtn: CharSequence?, onNegativeListener: DialogInterface.OnClickListener?): Dialog {
        return showListDialog(title, items.toTypedArray(), listener, positiveBtn, onPositiveListener, negativeBtn, onNegativeListener)
    }

    override fun showSingleChoiceDialog(title: CharSequence?, items: Array<String>,
                                        selectedItem: Int, listener: SingleChoiceDialogInterface.OnItemClickListener?,
                                        positiveBtn: CharSequence?, onPositiveListener: SingleChoiceDialogInterface.OnClickListener?,
                                        negativeBtn: CharSequence?, onNegativeListener: SingleChoiceDialogInterface.OnClickListener?): Dialog {
        val builder = AlertDialog.Builder(activity)
        if (title != null) builder.setTitle(title)
        var lastSelectedItem = selectedItem
        builder.setSingleChoiceItems(items, selectedItem, if (listener != null) android.content.DialogInterface.OnClickListener { dialog, which ->
            listener.onClick(dialog, items, which, lastSelectedItem)
            lastSelectedItem = which
        } else null)
        if (positiveBtn != null || onPositiveListener != null)
            builder.setPositiveButton(positiveBtn ?: "确定", { dialog, which ->
                if (onPositiveListener != null) onPositiveListener.onClick(dialog, items, lastSelectedItem)
                else dialog.dismiss()
            })
        if (negativeBtn != null || onNegativeListener != null)
            builder.setNegativeButton(negativeBtn ?: "取消", { dialog, which ->
                if (onNegativeListener != null) onNegativeListener.onClick(dialog, items, lastSelectedItem)
                else dialog.dismiss()
            })
        dialog = builder.show()
        return dialog!!
    }

    override fun showSingleChoiceDialog(title: CharSequence?, items: List<String>,
                                        selectedItem: Int, listener: SingleChoiceDialogInterface.OnItemClickListener?,
                                        positiveBtn: CharSequence?, onPositiveListener: SingleChoiceDialogInterface.OnClickListener?,
                                        negativeBtn: CharSequence?, onNegativeListener: SingleChoiceDialogInterface.OnClickListener?): Dialog {
        return showSingleChoiceDialog(title, items.toTypedArray(), selectedItem, listener, positiveBtn, onPositiveListener, negativeBtn, onNegativeListener)
    }

    override fun showMultiChoiceDialog(title: CharSequence?, items: Array<String>,
                                       checkedItems: BooleanArray?, listener: MultiChoiceDialogInterface.OnItemClickListener?,
                                       positiveBtn: CharSequence?, onPositiveListener: MultiChoiceDialogInterface.OnClickListener?,
                                       negativeBtn: CharSequence?, onNegativeListener: MultiChoiceDialogInterface.OnClickListener?): Dialog {
        val builder = AlertDialog.Builder(activity)
        if (title != null) builder.setTitle(title)
        val curCheckedItems = checkedItems ?: kotlin.BooleanArray(items.size)
        builder.setMultiChoiceItems(items, checkedItems,
                if (listener != null) android.content.DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                    curCheckedItems[which] = isChecked
                    listener.onClick(dialog, items, which, isChecked)
                } else null)
        if (positiveBtn != null || onPositiveListener != null)
            builder.setPositiveButton(positiveBtn ?: "确定", { dialog, which ->
                if (onPositiveListener != null) onPositiveListener.onClick(dialog, items, curCheckedItems)
                else dialog.dismiss()
            })
        if (negativeBtn != null || onNegativeListener != null)
            builder.setNegativeButton(negativeBtn ?: "取消", { dialog, which ->
                if (onNegativeListener != null) onNegativeListener.onClick(dialog, items, curCheckedItems)
                else dialog.dismiss()
            })
        dialog = builder.show()
        return dialog!!
    }

    override fun showCustomDialog(title: CharSequence?, view: View,
                                  positiveBtn: CharSequence?, onPositiveListener: CustomDialogInterface.OnClickListener?,
                                  negativeBtn: CharSequence?, onNegativeListener: CustomDialogInterface.OnClickListener?): Dialog {
        val builder = AlertDialog.Builder(activity)
        if (title != null) builder.setTitle(title)
        builder.setView(view)
        if (positiveBtn != null || onPositiveListener != null)
            builder.setPositiveButton(positiveBtn ?: "确定", { dialog, which ->
                if (onPositiveListener != null) onPositiveListener.onClick(dialog, view)
                else dialog.dismiss()
            })
        if (negativeBtn != null || onNegativeListener != null)
            builder.setNegativeButton(negativeBtn ?: "取消", { dialog, which ->
                if (onNegativeListener != null) onNegativeListener.onClick(dialog, view)
                else dialog.dismiss()
            })
        dialog = builder.show()
        return dialog!!
    }

    override fun showCustomDialog(view: View,
                                  positiveBtn: CharSequence?, onPositiveListener: CustomDialogInterface.OnClickListener?,
                                  negativeBtn: CharSequence?, onNegativeListener: CustomDialogInterface.OnClickListener?): Dialog {
        return showCustomDialog(null, view, positiveBtn, onPositiveListener, negativeBtn, onNegativeListener)
    }

    override fun showLoadingDialog(cancelListener: DialogInterface.OnCancelListener?): Dialog {
        return showLoadingDialog(content = "正在加载...", cancelListener = cancelListener)
    }

    override fun showLoadingDialog(content: CharSequence?, cancelable: Boolean): Dialog {
        val builder = AlertDialog.Builder(activity)
        val progressBar = ProgressBar(activity)
        val dp25 = UnitConverseUtils.dp2px(25)
        progressBar.setPadding(dp25, dp25, dp25, dp25)
        builder.setView(progressBar)
        if (content != null) builder.setMessage(content)
        builder.setCancelable(cancelable)
        dialog = builder.show()
        dialog!!.setCanceledOnTouchOutside(false)
        return dialog!!
    }

    override fun showLoadingDialog(content: CharSequence?, cancelListener: DialogInterface.OnCancelListener?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val progressBar = ProgressBar(activity)
        val dp25 = UnitConverseUtils.dp2px(25)
        progressBar.setPadding(dp25, dp25, dp25, dp25)
        builder.setView(progressBar)
        if (content != null) builder.setMessage(content)
        builder.setCancelable(true)
        if (cancelListener != null) builder.setOnCancelListener { cancelListener.onCancel(it) }
        dialog = builder.show()
        dialog!!.setCanceledOnTouchOutside(false)
        return dialog!!
    }

    override fun dismissDialog() {
        dialog?.let {
            if (it.isShowing) it.dismiss()
        }
    }

    private fun getOnClickListener(onClickListener: DialogInterface.OnClickListener?): android.content.DialogInterface.OnClickListener {
        return android.content.DialogInterface.OnClickListener { dialog, which ->
            if (onClickListener != null) onClickListener.onClick(dialog)
            else dialog.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissDialog()
        dialog = null
    }
}
