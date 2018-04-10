package com.linsh.lshapp.common.helper.act

import android.os.Bundle

import com.linsh.base.helper.ActivityHelper

/**
 * <pre>
 * author : Senh Linsh
 * github : https://github.com/SenhLinsh
 * date   : 2018/03/23
 * desc   : Activity 状态
 * </pre>
 */
class StatusHelper : ActivityHelper() {

    var isCreated: Boolean = false
    var isStarted: Boolean = false
    var isResumed: Boolean = false
    var isPaused: Boolean = false
    var isStopped: Boolean = false
    var isDestroyed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCreated = true
        isDestroyed = false
    }

    override fun onStart() {
        super.onStart()
        isStarted = true
        isStopped = false
    }

    override fun onResume() {
        super.onResume()
        isResumed = true
        isPaused = false
    }

    override fun onPause() {
        super.onPause()
        isPaused = true
        isResumed = false
    }

    override fun onStop() {
        super.onStop()
        isStopped = true
        isStarted = false
    }

    override fun onDestroy() {
        super.onDestroy()
        isDestroyed = true
        isCreated = false
    }
}
