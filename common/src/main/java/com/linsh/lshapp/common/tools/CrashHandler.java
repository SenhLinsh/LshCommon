package com.linsh.lshapp.common.tools;

import android.app.Activity;

import com.linsh.lshutils.handler.LshCrashHandler;
import com.linsh.lshutils.utils.Basic.LshLogUtils;

/**
 * Created by Senh Linsh on 17/6/15.
 */

public class CrashHandler extends LshCrashHandler {

    @Override
    protected void onCatchException(Thread thread, Throwable thr) {
        LshLogUtils.printer().e(thr);
    }

    @Override
    protected boolean isHandleByDefaultHandler(Thread thread, Throwable thr) {
        return true;
    }

    @Override
    protected Class<? extends Activity> onRestartAppIfNeeded() {
        return null;
    }
}
