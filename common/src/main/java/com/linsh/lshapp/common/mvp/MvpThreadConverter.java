package com.linsh.lshapp.common.mvp;

import android.util.Log;

import com.linsh.base.LshThread;
import com.linsh.utilseverywhere.ThreadUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/12/06
 *    desc   :
 * </pre>
 */
public class MvpThreadConverter {

    private static final String TAG = "MvpThreadConverter";

    public static <T extends Contract.View> T delegateView(T view) {
        ArrayList<Class<?>> list = new ArrayList<>();
        Class<?> viewClass = view.getClass();
        if (Contract.View.class.isAssignableFrom(viewClass)) {
            Class<?>[] interfaces = viewClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                if (Contract.View.class.isAssignableFrom(anInterface)) {
                    list.add(anInterface);
                }
            }
            viewClass = viewClass.getSuperclass();
        }
        return (T) Proxy.newProxyInstance(viewClass.getClassLoader(), list.toArray(new Class[list.size()]), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Log.d(TAG, "delegateView: view=" + view + ", method=" + method.toGenericString());
                if (method.getReturnType() == void.class && !ThreadUtils.isMainThread()) {
                    LshThread.ui(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                method.invoke(view, args);
                            } catch (Exception e) {
                                throw new RuntimeException("delegate view method " + method.toString() + " throw an exception: ", e);
                            }
                        }
                    });
                    return null;
                }
                if (method.getReturnType() == void.class) {
                    Log.i(TAG, "delegate view with a deprecated return type: " + method.getReturnType());
                }
                if (!ThreadUtils.isMainThread()) {
                    Log.i(TAG, "delegate view in a deprecated thread: " + Thread.currentThread().getName());
                }
                return method.invoke(view, args);
            }
        });
    }

    public static <T extends Contract.Presenter> T delegatePresenter(T presenter) {
        ArrayList<Class<?>> list = new ArrayList<>();
        Class<?> viewClass = presenter.getClass();
        if (Contract.Presenter.class.isAssignableFrom(viewClass)) {
            Class<?>[] interfaces = viewClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                if (Contract.Presenter.class.isAssignableFrom(anInterface)) {
                    list.add(anInterface);
                }
            }
            viewClass = viewClass.getSuperclass();
        }
        return (T) Proxy.newProxyInstance(viewClass.getClassLoader(), list.toArray(new Class[list.size()]), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Log.d(TAG, "delegatePresenter: presenter=" + presenter + ", method=" + method.toGenericString());
                if (method.getReturnType() == void.class && ThreadUtils.isMainThread()) {
                    LshThread.io(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                method.invoke(presenter, args);
                            } catch (Exception e) {
                                throw new RuntimeException("delegate presenter method " + method.toString() + " throw an exception: ", e);
                            }
                        }
                    });
                    return null;
                }
                if (method.getReturnType() == void.class) {
                    Log.i(TAG, "delegate view with a deprecated return type: " + method.getReturnType());
                }
                if (ThreadUtils.isMainThread()) {
                    Log.i(TAG, "delegate view in a deprecated thread: " + Thread.currentThread().getName());
                }
                return method.invoke(presenter, args);
            }
        });
    }
}
