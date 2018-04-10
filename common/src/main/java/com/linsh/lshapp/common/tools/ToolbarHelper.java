package com.linsh.lshapp.common.tools;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/03/07
 *    desc   :
 * </pre>
 */
public class ToolbarHelper {

    private Toolbar mToolbar;

    public ToolbarHelper(Toolbar toolbar) {
        mToolbar = toolbar;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public void hehe() {
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }

}
