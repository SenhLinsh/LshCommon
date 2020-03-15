package com.linsh.common.base;

import com.linsh.base.LshLog;
import com.linsh.base.mvp.Contract;
import com.linsh.base.mvp.MvpCallAdapter;
import com.linsh.base.mvp.MvpCallExecutor;
import com.linsh.common.mvp.AvoidRepeating;
import com.linsh.common.mvp.CallAfter;
import com.linsh.common.mvp.CallBefore;
import com.linsh.common.mvp.WithLoading;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/03/15
 *    desc   :
 * </pre>
 */
class MvpAnnotationEnhancer extends MvpCallAdapter {

    private static final String TAG = "MvpAnnotationEnhancer";
    private Contract.Presenter delegatedPresenter;
    private CommonContract.View delegatedView;
    private MvpCallExecutor callExecutor;
    private Set<Method> runningMethods = new HashSet<>();

    @Override
    protected void onBind(Contract.Presenter delegatedPresenter, Contract.View delegatedView, MvpCallExecutor callExecutor) {
        this.delegatedPresenter = delegatedPresenter;
        this.delegatedView = (CommonContract.View) delegatedView;
        this.callExecutor = callExecutor;
    }

    @Override
    protected Object handlePresenterMethod(Object proxy, Method method, Object[] args) throws Throwable {
        AvoidRepeating avoidRepeating = method.getAnnotation(AvoidRepeating.class);
        if (avoidRepeating != null) {
            if (runningMethods.contains(method)) {
                LshLog.i(TAG, "cancel presenter method [" + method.getName() + "] by AvoidRepeating Annotation, it is running");
                return null;
            }
        }
        return super.handlePresenterMethod(proxy, method, args);
    }

    @Override
    protected Object invokePresenterMethod(Object proxy, Method method, Object[] args) throws Throwable {
        // before
        WithLoading withLoading = method.getAnnotation(WithLoading.class);
        if (withLoading != null) {
            LshLog.d(TAG, "show loading dialog by WithLoading Annotation");
            delegatedView.showLoadingDialog();
        }
        CallBefore callBefore = method.getAnnotation(CallBefore.class);
        if (callBefore != null) {
            String value = callBefore.value();
            Method callMethod;
            try {
                callMethod = delegatedView.getClass().getInterfaces()[0].getMethod(value);
            } catch (NoSuchMethodException e) {
                LshLog.w(TAG, "can not find view method for CallBefore Annotation, view: " + delegatedView.getClass().getSimpleName() + ", name: " + value);
                return null;
            }
            LshLog.d(TAG, "invoke method " + value + "() by CallBefore Annotation");
            callExecutor.invoke(delegatedView, callMethod, null);
        }
        AvoidRepeating avoidRepeating = method.getAnnotation(AvoidRepeating.class);
        if (avoidRepeating != null) {
            runningMethods.add(method);
        }

        // invoke
        Object result = super.invokePresenterMethod(proxy, method, args);

        // after
        if (withLoading != null) {
            LshLog.d(TAG, "dismiss loading dialog by WithLoading Annotation");
            delegatedView.dismissLoadingDialog();
        }
        CallAfter callAfter = method.getAnnotation(CallAfter.class);
        if (callAfter != null) {
            String value = callAfter.value();
            Method callMethod = null;
            for (Class<?> anInterface : delegatedView.getClass().getInterfaces()) {
                for (Method m : anInterface.getMethods()) {
                    if (m.getName().equals(value)) {
                        callMethod = m;
                        break;
                    }
                }
            }
            if (callMethod == null) {
                LshLog.w(TAG, "can not find view method for CallAfter Annotation, view: " + delegatedView.getClass().getSimpleName() + ", name: " + value);
                return null;
            }
            LshLog.d(TAG, "invoke method " + value + "() by CallAfter Annotation");
            callExecutor.invoke(delegatedView, callMethod, null);
        }
        if (avoidRepeating != null) {
            runningMethods.remove(method);
        }
        return result;
    }

}
