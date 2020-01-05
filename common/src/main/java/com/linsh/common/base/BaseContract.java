package com.linsh.common.base;

import com.linsh.base.activity.Contract;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/01/05
 *    desc   :
 * </pre>
 */
public interface BaseContract {

    interface Presenter<V extends View> extends Contract.Presenter<V> {
    }

    interface View<P extends Presenter> extends Contract.View<P> {
        void showToast(String text);

        void showLoading();

        void dismissLoading();

        void finishActivity();
    }
}
