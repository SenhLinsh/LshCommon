package com.linsh.lshapp.common.tools;

/**
 * Created by Senh Linsh on 17/4/27.
 */

public class LshActivityStatusTool {

    private static final int ON_CREATE = 1;
    private static final int ON_START = 2;
    private static final int ON_RESUME = 3;
    private static final int ON_PAUSE = 4;
    private static final int ON_STOP = 5;
    private static final int ON_DESTROY = 6;

    private int mStatus;

    public void onCreate() {
        mStatus = ON_CREATE;
    }

    public void onStart() {
        mStatus = ON_START;
    }

    public void onResume() {
        mStatus = ON_RESUME;
    }

    public void onPause() {
        mStatus = ON_PAUSE;
    }

    public void onStop() {
        mStatus = ON_STOP;
    }

    public void onDestroy() {
        mStatus = ON_DESTROY;
    }

    public boolean isOnCreated() {
        return mStatus >= 1 && mStatus < 6;
    }

    public boolean isOnStarted() {
        return mStatus >= 2 && mStatus < 5;
    }

    public boolean isOnResumed() {
        return mStatus >= 3 && mStatus < 4;
    }

    public boolean isOnPaused() {
        return mStatus >= 4 && mStatus < 6;
    }

    public boolean isOnStopped() {
        return mStatus >= 5 && mStatus < 6;
    }

    public boolean isOnDestroyed() {
        return mStatus >= 6;
    }
}
