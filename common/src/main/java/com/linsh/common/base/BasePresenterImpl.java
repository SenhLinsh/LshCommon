package com.linsh.common.base;

import com.linsh.base.activity.Contract;
import com.linsh.base.activity.mvp.PresenterImpl;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/01/05
 *    desc   :
 * </pre>
 */
public abstract class BasePresenterImpl<V extends Contract.View> extends PresenterImpl<V> {

    private EnhancedView enhancedView;

    public EnhancedView getEnhancedView() {
        if (enhancedView == null)
            enhancedView = new EnhancedViewImpl(getView());
        return enhancedView;
    }
}
