package com.linsh.lshapp.common.mvp;

import com.linsh.base.LshLog;
import com.linsh.base.LshThread;
import com.linsh.utilseverywhere.ThreadUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/12/06
 *    desc   : MVP 线程转换器
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
public class MvpThreadConverter {

    private static final String TAG = "MvpThreadConverter";

    /**
     * 内部类方式声明单例
     */
    static class Holder {
        static final ThreadPoolExecutor MVP_PRESENTER_THREAD = new ThreadPoolExecutor(0, 1,
                10, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(), new DefaultThreadFactory());
    }

    /**
     * 代理 View 层实例, 在 Presenter 调用 View 层接口时, 如果运行线程为非 UI 线程, 则将方法调用转移到 UI 线程进行调用.
     * <p>
     * 注意: 如果方法存在返回值, 将不会自动切换线程, 而是继续在当前的线程进行调用.
     *
     * @param view View 层实例
     */
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
                LshLog.d(TAG, "delegateView: view=" + view.getClass().getSimpleName()
                        + ", method=" + method.getName() + ", thread=" + Thread.currentThread().getName());
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
                    LshLog.i(TAG, "delegate view with a deprecated return type: " + method.getReturnType());
                }
                if (!ThreadUtils.isMainThread()) {
                    LshLog.i(TAG, "delegate view in a deprecated thread: " + Thread.currentThread().getName());
                }
                return method.invoke(view, args);
            }
        });
    }

    /**
     * 代理 Presenter 层实例, 在 View 调用 Presenter 层接口时, 如果运行线程为 UI 线程, 则将方法调用转移到后台线程(默认为MvpPresenterThread)进行调用.
     * <p>
     * 注意: 如果方法存在返回值, 将不会自动切换线程, 而是继续在当前的线程进行调用.
     *
     * @param presenter Presenter 层实例
     */
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
                LshLog.d(TAG, "delegatePresenter: presenter=" + presenter.getClass().getSimpleName()
                        + ", method=" + method.getName());
                if (method.getReturnType() == void.class && ThreadUtils.isMainThread()) {
                    Holder.MVP_PRESENTER_THREAD.execute(new Runnable() {
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
                    LshLog.i(TAG, "delegate view with a deprecated return type: " + method.getReturnType());
                }
                if (ThreadUtils.isMainThread()) {
                    LshLog.i(TAG, "delegate view in a deprecated thread: " + Thread.currentThread().getName());
                }
                return method.invoke(presenter, args);
            }
        });
    }

    /**
     * View 层在调用 Presenter 接口时, 默认使用的线程池. 该线程池只有一个线程, 主要是为了避免 Presenter 在使用过程中出现
     * 线程同步问题, 因为需要在使用多线程时, 需要在 presenter 中自己使用多线程, 或在 Contract.Presenter 中定义接口时,
     * 使用多线程相关的注解 (暂未开发)
     */
    private static class DefaultThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "MvpPresenterThread-" + threadNumber.getAndIncrement());
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
