package com.linsh.common.mvp;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/12/06
 *    desc   :
 * </pre>
 */
public interface Contract {

    interface Presenter<V extends View> {
        void attachView(V view);

        void detachView();
    }

    interface View<P extends Presenter> {
        P getPresenter();
    }
}
