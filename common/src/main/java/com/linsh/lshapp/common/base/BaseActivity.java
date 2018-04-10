package com.linsh.lshapp.common.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.linsh.base.helper.interf.DialogHelperInterface;
import com.linsh.base.helper.BaseHelperActivity;
import com.linsh.lshapp.common.helper.act.StatusHelper;
import com.linsh.lshapp.common.helper.act.SystemDialogHelper;
import com.linsh.utilseverywhere.KeyboardUtils;

public abstract class BaseActivity extends BaseHelperActivity {

    private StatusHelper mStatusHelper;
    private DialogHelperInterface mDialogHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStatusHelper = new StatusHelper();
        if (mDialogHelper == null)
            mDialogHelper = new SystemDialogHelper(this);
        addHelper(mStatusHelper, mDialogHelper);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭键盘
        KeyboardUtils.clearFocusAndHideKeyboard(this);
    }

    protected Activity getActivity() {
        return this;
    }

    public StatusHelper getStatusHelper() {
        return mStatusHelper;
    }

    public void setDialogHelper(DialogHelperInterface dialogHelper) {
        if (mDialogHelper != null) removeHelper(mDialogHelper);
        addHelper(dialogHelper);
        mDialogHelper = dialogHelper;
    }

    public DialogHelperInterface getDialogHelper() {
        return mDialogHelper;
    }
}
