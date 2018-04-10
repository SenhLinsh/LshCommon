package com.linsh.lshapp.common.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.linsh.lshapp.common.R;
import com.linsh.utilseverywhere.ResourceUtils;
import com.linsh.utilseverywhere.SystemUtils;

/**
 * Created by linsh on 17/2/2.
 */

public abstract class BaseToolbarActivity extends BaseViewActivity {

    private Toolbar mToolbar;

    protected abstract String getToolbarTitle();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置沉浸状态栏
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SystemUtils.setTranslucentStatusBar(this, ResourceUtils.getColor(R.color.color_theme_dark_blue_pressed));
        }
//        LshSystemUtils.setTranslucentStatusBar(this, LshResourceUtils.getColor(R.color.color_theme_dark_blue_pressed));
        // 初始化ToolBar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(getToolbarTitle());
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = View.inflate(this, R.layout.activity_base_toolbar, null);
        // 填充子布局, 由子类返回布局id
        getLayoutInflater().inflate(getLayout(), (ViewGroup) view, true);
        setContentView(view);
    }

    private int getLayout() {
        return 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setToolbarTitle(String title) {
        mToolbar.setTitle(title);
    }

    protected void setToolbarColor(int color) {
        mToolbar.setBackgroundColor(color);
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }
}
