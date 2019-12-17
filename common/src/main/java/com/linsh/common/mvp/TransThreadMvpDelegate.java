package com.linsh.common.mvp;

import android.os.Handler;
import android.os.HandlerThread;

import com.linsh.base.LshLog;
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
 *    desc   : MVP 线程切换代理
 *
 *             使用代理模式, 将 Presenter 层和 View 层的接口调用线程进行隔离, View 层接口默认分配
 *             主线程进行调用, Presenter 层接口默认分配后台线程(MvpPresenterThread)进行调用, 其中
 *             调用的线程, 可以在接口定义处(Contract)使用注解进行指定.
 *
 *             注: 由于返回值具有时效性, 因为如果在 Contract 中定义了具有返回值的接口, 将不会进行线程
 *             切换, 而是使用原来的线程进行调用. 因此, 在该模式下的 MVP 接口定义时, 应尽量避免使用
 *             具有返回值的接口.
 * </pre>
 */
public class TransThreadMvpDelegate<P extends Contract.Presenter, V extends Contract.View> {

    private static final String TAG = "TransThreadMvpDelegate";
    private P delegatedPresenter;
    private V delegatedView;
    private P originPresenter;
    private V originView;
    private boolean isViewAttached;

    public TransThreadMvpDelegate(P presenter, V view) {
        this.originPresenter = presenter;
        this.originView = view;
        this.delegatedPresenter = delegatePresenter();
        this.delegatedView = delegateView();
    }

    public V getView() {
        return delegatedView;
    }

    public P getPresenter() {
        return delegatedPresenter;
    }

    public void attachView() {
        isViewAttached = true;
        delegatedPresenter.attachView(delegatedView);
    }

    public void detachView() {
        delegatedPresenter.detachView();
        isViewAttached = false;
        originView = null;
    }


    /**
     * 代理 View 层实例, 在 Presenter 调用 View 层接口时, 如果运行线程为非 UI 线程, 则将方法调用转移到 UI 线程进行调用.
     * <p>
     * 注意: 如果方法存在返回值, 将不会自动切换线程, 而是继续在当前的线程进行调用.
     */
    private <T extends Contract.View> T delegateView() {
        ArrayList<Class<?>> list = new ArrayList<>();
        Class<?> viewClass = originView.getClass();
        if (Contract.View.class.isAssignableFrom(viewClass)) {
            Class<?>[] interfaces = viewClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                if (Contract.View.class.isAssignableFrom(anInterface)) {
                    list.add(anInterface);
                }
            }
            viewClass = viewClass.getSuperclass();
        }
        LshLog.d(TAG, "delegatedView: new proxy instance for Interfaces " + list.toString());
        return (T) Proxy.newProxyInstance(viewClass.getClassLoader(), list.toArray(new Class[list.size()]), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (!isViewAttached) {
                    LshLog.i(TAG, "try to invoke view method, but the view is detached, ignore.");
                    return null;
                }
                LshLog.d(TAG, "delegatedView: delegatedView=" + originView.getClass().getSimpleName()
                        + ", method=" + method.getName() + ", thread=" + Thread.currentThread().getName());
                if (method.getReturnType() == void.class && !ThreadUtils.isMainThread()) {
                    LshThread.ui(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                method.invoke(originView, args);
                            } catch (Exception e) {
                                throw new RuntimeException("delegate delegatedView method " + method.toString() + " throw an exception: ", e);
                            }
                        }
                    });
                    return null;
                }
                if (method.getReturnType() == void.class) {
                    LshLog.i(TAG, "delegate delegatedView with a deprecated return type: " + method.getReturnType());
                }
                if (!ThreadUtils.isMainThread()) {
                    LshLog.i(TAG, "delegate delegatedView in a deprecated thread: " + Thread.currentThread().getName());
                }
                return method.invoke(originView, args);
            }
        });
    }


    /**
     * 代理 Presenter 层实例, 在 View 调用 Presenter 层接口时, 如果运行线程为 UI 线程, 则将方法调用转移到后台线程(默认为MvpPresenterThread)进行调用.
     * <p>
     * 注意: 如果方法存在返回值, 将不会自动切换线程, 而是继续在当前的线程进行调用.
     */
    private <T extends Contract.Presenter> T delegatePresenter() {
        ArrayList<Class<?>> list = new ArrayList<>();
        Class<?> viewClass = originPresenter.getClass();
        if (Contract.Presenter.class.isAssignableFrom(viewClass)) {
            Class<?>[] interfaces = viewClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                if (Contract.Presenter.class.isAssignableFrom(anInterface)) {
                    list.add(anInterface);
                }
            }
            viewClass = viewClass.getSuperclass();
        }
        LshLog.d(TAG, "delegatedPresenter: new proxy instance for Interfaces " + list.toString());
        return (T) Proxy.newProxyInstance(viewClass.getClassLoader(), list.toArray(new Class[list.size()]), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (!isViewAttached) {
                    LshLog.i(TAG, "try to invoke presenter method after view is detached, ignore.");
                    return null;
                }
                LshLog.d(TAG, "delegatedPresenter: presenter=" + originPresenter.getClass().getSimpleName()
                        + ", method=" + method.getName());
                if (method.getReturnType() == void.class && ThreadUtils.isMainThread()) {
                    Holder.MVP_PRESENTER_THREAD.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                method.invoke(originPresenter, args);
                            } catch (Exception e) {
                                throw new RuntimeException("delegate presenter method " + method.toString() + " throw an exception: ", e);
                            }
                        }
                    });
                    return null;
                }
                if (method.getReturnType() == void.class) {
                    LshLog.i(TAG, "delegate view with a deprecated return type: " + method.getReturnType());
                }
                if (ThreadUtils.isMainThread()) {
                    LshLog.i(TAG, "delegate view in a deprecated thread: " + Thread.currentThread().getName());
                }
                return method.invoke(originPresenter, args);
            }
        });
    }

    /**
     * 内部类方式声明单例
     */
    static class Holder {
        static final Handler MVP_PRESENTER_THREAD = getHandlerTHread();

        private static Handler getHandlerTHread() {
            HandlerThread handlerThread = new HandlerThread("MvpPresenterThread");
            handlerThread.start();
            return new Handler(handlerThread.getLooper());
        }
    }
}
