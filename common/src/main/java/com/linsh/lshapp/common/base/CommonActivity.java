package com.linsh.lshapp.common.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.linsh.base.helper.interf.ToolbarHelperInterface;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/03/23
 *    desc   :
 * </pre>
 */
public abstract class CommonActivity extends BaseActivity {

    private boolean hasSavedInstanceState;
    private ToolbarHelperInterface mToolbarHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置屏幕方向
        setScreenOrientation();
        // Toolbar
        ToolbarHelperInterface toolbarHelper = initToolbarHelper();
        if (toolbarHelper != null) {
            addHelper(toolbarHelper);
            mToolbarHelper = toolbarHelper;
            toolbarHelper.setContentView(this, getLayout());
        } else {
            setContentView(getLayout());
        }
        // 初始化布局
        initView();
        initView(savedInstanceState);

        if (savedInstanceState != null) hasSavedInstanceState = true;
    }

    @Override
    protected void onResume() {
        initData();
        if (!getStatusHelper().isPaused() && !hasSavedInstanceState) {
            initData(null);
            hasSavedInstanceState = false;
        }
        super.onResume();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        initData(savedInstanceState);
    }

    protected ToolbarHelperInterface initToolbarHelper() {
        return null;
    }

    protected abstract int getLayout();

    protected abstract void initView();

    protected void initView(Bundle savedInstanceState) {
    }

    protected abstract void initData();

    protected void initData(Bundle savedInstanceState) {
    }

    protected void setScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public ToolbarHelperInterface getToolbarHelper() {
        return mToolbarHelper;
    }
}
