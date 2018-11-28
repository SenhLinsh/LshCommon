package com.linsh.lshapp.common.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.linsh.utilseverywhere.KeyboardUtils;

public abstract class BaseActivity extends ObservableActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
