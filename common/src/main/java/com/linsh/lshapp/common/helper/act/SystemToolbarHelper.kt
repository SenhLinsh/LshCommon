package com.linsh.lshapp.common.helper.act

import android.app.Activity
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams
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
class SystemToolbarHelper(activity: Activity) : ActivityHelper(), ToolbarHelperInterface {

    private var toolbar: Toolbar = Toolbar(activity)

    override fun setTitle(title: CharSequence) {
        toolbar.title = title
    }

    override fun getTitle(): String {
        return toolbar.title.toString()
    }

    override fun setLogo(resId: Int) {
        toolbar.setLogo(resId)
    }

    override fun setLogo(drawable: Drawable?) {
        toolbar.logo = drawable
    }

    override fun setNavigationIcon(resId: Int) {
        toolbar.setNavigationIcon(resId)
    }

    override fun setNavigationIcon(drawable: Drawable?) {
        toolbar.navigationIcon = drawable
    }

    override fun setBackgroundColor(color: Int) {
        toolbar.setBackgroundColor(color)
    }

    override fun setContentView(activity: Activity, layoutRes: Int) {
        activity.setContentView(R.layout.activity_toolbar)
        val flRoot: FrameLayout = activity.findViewById(R.id.flRoot)
        val actionBarSize = getActionBarSize(activity)
        // 添加 Toolbar
        val lpToolbar = LayoutParams(LayoutParams.MATCH_PARENT, actionBarSize)
        flRoot.addView(toolbar, lpToolbar)
        if (activity is AppCompatActivity && activity.supportActionBar == null) {
            activity.setSupportActionBar(toolbar)
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
        activity.setContentView(R.layout.activity_toolbar)
        val flRoot: FrameLayout = activity.findViewById(R.id.flRoot)
        val actionBarSize = getActionBarSize(activity)
        // 添加 Toolbar
        val lpToolbar = LayoutParams(LayoutParams.MATCH_PARENT, actionBarSize)
        flRoot.addView(toolbar, lpToolbar)
        if (activity is AppCompatActivity && activity.supportActionBar == null) {
            activity.setSupportActionBar(toolbar)
        }
        // 添加主布局
        val layoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
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
