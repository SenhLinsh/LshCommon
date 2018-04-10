package com.feiboedu.app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.linsh.base.helper.interf.ToolbarHelperInterface;
import com.linsh.lshapp.common.base.CommonActivity;
import com.linsh.lshapp.common.helper.act.SystemActionBarHelper;
import com.linsh.utilseverywhere.Utils;

public class MainActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.init(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ToolbarHelperInterface initToolbarHelper() {
        SystemActionBarHelper toolbarHelper = new SystemActionBarHelper(this);
        toolbarHelper.setTitle("Linsh");
        toolbarHelper.setLogo(R.drawable.ic_toggle_open);
        toolbarHelper.setBackgroundColor(Color.RED);
        toolbarHelper.setNavigationIcon(R.drawable.ic_done_white);
        return toolbarHelper;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

    }

    public void onclick1(View view) {
    }

    public void onclick2(View view) {
    }

    public void onclick3(View view) {
    }
}
