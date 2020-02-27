package com.linsh.common.base;

import com.linsh.base.mvp.Contract;
import com.linsh.base.mvp.PresenterImpl;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/01/05
 *    desc   :
 * </pre>
 */
public abstract class BasePresenterImpl<V extends Contract.View> extends PresenterImpl<V> {

    private CommonContract.View enhancedView;

    public CommonContract.View getCommonView() {
        V view = getView();
        if (view instanceof CommonContract.View) {
            return (CommonContract.View) getView();
        }
        if (enhancedView == null)
            enhancedView = new CommonViewImpl(getView());
        return enhancedView;
    }
}
