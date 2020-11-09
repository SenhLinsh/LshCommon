package com.linsh.common.entity.callback;

import com.linsh.base.LshThread;
import com.linsh.base.common.Callback;
import com.linsh.base.thread.ThreadPolicy;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/11/09
 *    desc   :
 * </pre>
 */
public class ThreadDelegateCallback<T> implements Callback<T> {

    private final ThreadPolicy policy;
    private final Callback<T> target;

    public ThreadDelegateCallback(ThreadPolicy policy, Callback<T> target) {
        this.policy = policy;
        this.target = target;
    }

    @Override
    public void onSuccess(T t) {
        LshThread.run(policy, new Runnable() {
            @Override
            public void run() {
                target.onSuccess(t);
            }
        });
    }

    @Override
    public void onFailed(Throwable throwable) {
        LshThread.run(policy, new Runnable() {
            @Override
            public void run() {
                target.onFailed(throwable);
            }
        });
    }
}
