package com.linsh.lshapp.common.helper.act

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.linsh.base.helper.ActivityHelper
import com.linsh.base.helper.interf.ToolbarHelperInterface
import com.linsh.lshapp.common.R

/**
 * <pre>
 * author : Senh Linsh
 * github : https://github.com/SenhLinsh
 * date   : 2018/03/29
 * desc   : 封装系统 Toolbar 到 ToolbarHelper 中
 * </pre>
 */
class SystemActionBarHelper(private val activity: AppCompatActivity) : ActivityHelper(), ToolbarHelperInterface {

    private val hasDefaultActionBar: Boolean = activity.supportActionBar != null
    private var toolbar: Toolbar? = null

    init {
        if (activity.supportActionBar == null) {
            toolbar = Toolbar(activity)
            activity.setSupportActionBar(toolbar)
        }
    }

    override fun setTitle(title: CharSequence) {
        activity.supportActionBar?.title = title
    }

    override fun getTitle(): String {
        return activity.supportActionBar?.title.toString()
    }

    override fun setLogo(resId: Int) {
        activity.supportActionBar?.setDisplayShowHomeEnabled(resId != 0)
        activity.supportActionBar?.setLogo(resId)
        activity.supportActionBar?.setDisplayUseLogoEnabled(resId != 0)
    }

    override fun setLogo(drawable: Drawable?) {
        activity.supportActionBar?.setDisplayShowHomeEnabled(drawable != null)
        activity.supportActionBar?.setLogo(drawable)
        activity.supportActionBar?.setDisplayUseLogoEnabled(drawable != null)
    }

    override fun setNavigationIcon(resId: Int) {
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(resId != 0)
        activity.supportActionBar?.setHomeAsUpIndicator(resId)
    }

    override fun setNavigationIcon(drawable: Drawable?) {
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(drawable != null)
        activity.supportActionBar?.setHomeAsUpIndicator(drawable)
    }

    override fun setBackgroundColor(color: Int) {
        activity.supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
    }

    override fun setContentView(activity: Activity, layoutRes: Int) {
        if (hasDefaultActionBar) {
            activity.setContentView(layoutRes)
            return
        }
        activity.setContentView(R.layout.activity_toolbar)
        val flRoot: FrameLayout = activity.findViewById(R.id.flRoot)
        val actionBarSize = getActionBarSize(activity)
        // 添加 Toolbar
        toolbar?.let {
            flRoot.addView(it,  ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, actionBarSize))
        }
        // 添加主布局
        LayoutInflater.from(activity).inflate(layoutRes, flRoot)
        val contentView = flRoot.getChildAt(1)
        if (contentView != null) {
            val layoutParams = contentView.layoutParams as FrameLayout.LayoutParams
            layoutParams.topMargin = actionBarSize
        }
    }

    override fun setContentView(activity: Activity, view: View) {
        if (hasDefaultActionBar) {
            activity.setContentView(view)
            return
        }
        activity.setContentView(R.layout.activity_toolbar)
        val flRoot: FrameLayout = activity.findViewById(R.id.flRoot)
        val actionBarSize = getActionBarSize(activity)
        // 添加 Toolbar
        toolbar?.let {
            flRoot.addView(it,  ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, actionBarSize))
        }
        // 添加主布局
        val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        layoutParams.topMargin = actionBarSize
        flRoot.addView(view, layoutParams)
    }

    private fun getActionBarSize(activity: Activity): Int {
        val typedArray = activity.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        val actionBarSize = typedArray.getDimension(0, 0F)
        typedArray.recycle()
        return actionBarSize.toInt()
    }
}