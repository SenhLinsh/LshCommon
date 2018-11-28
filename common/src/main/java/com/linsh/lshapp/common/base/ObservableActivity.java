package com.linsh.lshapp.common.base;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.linsh.protocol.Client;
import com.linsh.protocol.activity.ActivitySubscribe;
import com.linsh.protocol.activity.IObservableActivity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/07/19
 *    desc   : 观察者 Activity
 *             用于像 Activity 注册各种其回调的订阅者, 在 Activity 之外获取其回调, 以不直接依赖于
 *             Activity 的情况下完成自己的代码逻辑, 达到解耦的目的.
 *
 *    使用方法: 继承 ObservableActivity 的 Activity, 即可获取被订阅的能力.
 *
 *             需要订阅 Activity 回调的订阅者, 根据需要订阅的回调, 实现 ActivitySubscribe 内部类
 *             内的相关子接口, 即可成为订阅者.
 *
 *             成为订阅者之后, 需要向 ObservableActivity 进行订阅. 订阅方法分为两种, 启动前订阅和
 *             启动后订阅.
 *
 *             启动前订阅即在 startActivity() 时进行订阅, 此时可以在 Intent 中添加订阅者, 方式为
 *             {@link Client#activity().target(android.app.Activity).subscribe(ActivitySubscribe)}.
 *
 *             启动后订阅即在 ObservableActivity 的子类内部任意地方调用 {@link #subscribe(ActivitySubscribe)}
 *             来进行订阅.
 *
 *             注: 如果在 Activity 内部订阅, 需注意只有在订阅之后订阅者才能收到事件回调. 特别注意
 *             如果在 onCreate() 方法中注册了订阅 onCreate() 的订阅者, 那么必须放在 super.onCreate()
 *             之前进行订阅, 否则订阅者无法收到 onCreate() 的回调. (因为此时回调先于注册发生, 导致
 *             无法及时收到)
 * </pre>
 */
public class ObservableActivity extends AppCompatActivity implements IObservableActivity {

    /**
     * 该 Map 包含了所有已经被订阅了的订阅者.
     * Map 中的 key 为指定回调对应的订阅事件, value 为订阅过该事件的所有订阅者
     */
    private HashMap<Class, Set<ActivitySubscribe>> mSubscribers;

    /**
     * 缓存已经使用了的订阅者, 下次可省略查找订阅事件的步骤.
     */
    private static HashMap<Class, Set<Class<? extends ActivitySubscribe>>> sSubscriberClasses = new HashMap<>();

    /**
     * 注册订阅者
     * <p>
     * 传入的类名会被自动实例化, 且默认使用无参构造实例化. 如果实例化失败, 则会订阅失败
     *
     * @param classOfSubscriber 订阅者类名(全名)
     * @return 是否成功订阅
     */
    @Override
    public <T extends ActivitySubscribe> T subscribe(String classOfSubscriber) {
        try {
            Class<?> forName = Class.forName(classOfSubscriber);
            return subscribe((Class<? extends T>) forName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 注册订阅者
     * <p>
     * 传入的类对象会被自动实例化, 且默认使用无参构造实例化. 如果实例化失败, 则会订阅失败
     *
     * @param classOfSubscriber 订阅者类对象
     * @return 是否成功订阅
     */
    @Override
    public <T extends ActivitySubscribe> T subscribe(Class<T> classOfSubscriber) {
        try {
            T callback = classOfSubscriber.newInstance();
            return subscribe(callback);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 注册订阅者
     *
     * @param subscriber 订阅者
     * @return 是否成功订阅
     */
    @Override
    public <T extends ActivitySubscribe> T subscribe(T subscriber) {
        if (mSubscribers == null)
            mSubscribers = new HashMap<>();

        // 已经存在该类的订阅事件缓存
        Set<Class<? extends ActivitySubscribe>> classSet = sSubscriberClasses.get(subscriber.getClass());
        if (classSet != null) {
            for (Class<? extends ActivitySubscribe> callbackClass : classSet) {
                Set<ActivitySubscribe> list = mSubscribers.get(callbackClass);
                if (list == null) {
                    list = new LinkedHashSet<>();
                    mSubscribers.put(callbackClass, list);
                }
                list.add(subscriber);
            }
            return subscriber;
        }
        // 没有该类的订阅事件缓存, 获取并缓存
        boolean subscribed = false;
        Class clazz = subscriber.getClass();
        HashSet<Class<? extends ActivitySubscribe>> cache = new HashSet<>();
        while (clazz != null) {
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> callbackClass : interfaces) {
                if (ActivitySubscribe.class.isAssignableFrom(callbackClass) &&
                        ActivitySubscribe.Register.contains((Class<? extends ActivitySubscribe>) callbackClass)) {
                    Set<ActivitySubscribe> list = mSubscribers.get(callbackClass);
                    if (list == null) {
                        list = new LinkedHashSet<>();
                        mSubscribers.put(callbackClass, list);
                    }
                    list.add(subscriber);
                    cache.add((Class<? extends ActivitySubscribe>) callbackClass);
                    subscribed = true;
                }
            }
            if (subscribed && !ActivitySubscribe.class.isAssignableFrom(clazz))
                break;
            clazz = clazz.getSuperclass();
        }
        if (subscribed) {
            sSubscriberClasses.put(clazz, cache);
            subscriber.attach(this);
            return subscriber;
        }
        return null;
    }

    /**
     * 判断是否已经订阅该事件的类
     *
     * @param classOfSubscriber 订阅者实现类
     * @return 如果订阅者中存在该类的实例, 则返回 true, 否则为 false
     */
    @Override
    public boolean isSubscribed(Class<? extends ActivitySubscribe> classOfSubscriber) {
        if (classOfSubscriber == null) return false;
        Set<Class<? extends ActivitySubscribe>> classSet = sSubscriberClasses.get(classOfSubscriber);
        if (classSet == null || classSet.size() == 0) return false;
        Set<ActivitySubscribe> subscribeSet = mSubscribers.get(classSet.iterator().next());
        if (subscribeSet == null) return false;
        for (ActivitySubscribe subscriber : subscribeSet) {
            if (subscriber.getClass() == classOfSubscriber)
                return true;
        }
        return false;
    }

    /**
     * 判断是否已经订阅该事件的实例
     *
     * @param subscriber 订阅者实例
     * @return 如果订阅者中存在该实例, 则返回 true, 否则为 false
     */
    @Override
    public boolean isSubscribed(ActivitySubscribe subscriber) {
        if (subscriber == null) return false;
        Set<Class<? extends ActivitySubscribe>> classSet = sSubscriberClasses.get(subscriber.getClass());
        if (classSet == null || classSet.size() == 0) return false;
        Set<ActivitySubscribe> subscribeSet = mSubscribers.get(classSet.iterator().next());
        if (subscribeSet == null) return false;
        return subscribeSet.contains(subscriber);
    }

    /**
     * 解注册该类订阅者
     * <p>
     * 注意: 如果该类存在多个实例的订阅者, 会将全部的订阅者进行解注册
     *
     * @param classOfSubscriber 订阅者类对象
     */
    @Override
    public boolean unsubscribe(Class<? extends ActivitySubscribe> classOfSubscriber) {
        boolean res = false;
        if (classOfSubscriber == null) return false;
        Set<Class<? extends ActivitySubscribe>> set = sSubscriberClasses.get(classOfSubscriber);
        if (set == null) return false;
        for (Class<? extends ActivitySubscribe> clazz : set) {
            Set<ActivitySubscribe> subscribers = mSubscribers.get(clazz);
            if (subscribers != null) {
                Iterator<ActivitySubscribe> iterator = subscribers.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getClass() == classOfSubscriber) {
                        iterator.remove();
                        res = true;
                    }
                }
            }
        }
        return res;
    }

    /**
     * 解注册订阅者
     *
     * @param subscriber 订阅者
     */
    @Override
    public boolean unsubscribe(ActivitySubscribe subscriber) {
        boolean res = false;
        if (subscriber == null) return false;
        Set<Class<? extends ActivitySubscribe>> set = sSubscriberClasses.get(subscriber.getClass());
        if (set == null) return false;
        for (Class<? extends ActivitySubscribe> clazz : set) {
            Set<ActivitySubscribe> subscribers = mSubscribers.get(clazz);
            if (subscribers != null) {
                subscribers.remove(subscriber);
                res = true;
            }
        }
        return res;
    }

    //========================================= Lifecycle =========================================//
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 订阅通过 Intention 传递过来的 ActivitySubscribe 订阅者
        List<Class<? extends ActivitySubscribe>> subscribers = Client.activity().target(this).intent().getSubscribers();
        if (subscribers != null) {
            for (Class<? extends ActivitySubscribe> subscriber : subscribers) {
                subscribe(subscriber);
            }
        }
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnCreate.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnCreate) callback).onCreate(savedInstanceState);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnStart.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnStart) callback).onStart();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnResume.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnResume) callback).onResume();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnPause.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnPause) callback).onPause();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnStop.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnStop) callback).onStop();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnDestroy.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnDestroy) callback).onDestroy();
                }
            }
        }
    }

    //========================================= LifecycleEx =========================================//
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnCreateV2.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnCreateV2) callback).onCreate(savedInstanceState, persistentState);
                }
            }
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnPostCreate.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnPostCreate) callback).onPostCreate(savedInstanceState);
                }
            }
        }
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnPostCreateV2.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnPostCreateV2) callback).onPostCreate(savedInstanceState, persistentState);
                }
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnRestart.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnRestart) callback).onRestart();
                }
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnPostResume.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnPostResume) callback).onPostResume();
                }
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnNewIntent.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnNewIntent) callback).onNewIntent(intent);
                }
            }
        }
    }

    //========================================= InstanceState =========================================//
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnSaveInstanceState.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnSaveInstanceState) callback).onSaveInstanceState(outState);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnSaveInstanceStateV2.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnSaveInstanceStateV2) callback).onSaveInstanceState(outState, outPersistentState);
                }
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnRestoreInstanceState.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnRestoreInstanceState) callback).onRestoreInstanceState(savedInstanceState);
                }
            }
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnRestoreInstanceStateV2.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnRestoreInstanceStateV2) callback).onRestoreInstanceState(savedInstanceState, persistentState);
                }
            }
        }
    }

    //========================================= Result =========================================//
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnRequestPermissionsResult.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnRequestPermissionsResult) callback).onRequestPermissionsResult(
                            requestCode, permissions, grantResults);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnActivityResult.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnActivityResult) callback).onActivityResult(
                            requestCode, resultCode, data);
                }
            }
        }
    }

    //========================================= OptionsMenu =========================================//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnCreateOptionsMenu.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    if (((ActivitySubscribe.OnCreateOptionsMenu) callback).onCreateOptionsMenu(menu)) {
                        return true;
                    }
                }
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnPrepareOptionsMenu.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    if (((ActivitySubscribe.OnPrepareOptionsMenu) callback).onPrepareOptionsMenu(menu)) {
                        return true;
                    }
                }
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnOptionsItemSelected.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    if (((ActivitySubscribe.OnOptionsItemSelected) callback).onOptionsItemSelected(item)) {
                        return true;
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnOptionsMenuClosed.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnOptionsMenuClosed) callback).onOptionsMenuClosed(menu);
                }
            }
        }
    }

    //========================================= Event =========================================//

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnKeyDown.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    if (((ActivitySubscribe.OnKeyDown) callback).onKeyDown(keyCode, event)) {
                        return true;
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnKeyLongPress.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    if (((ActivitySubscribe.OnKeyLongPress) callback).onKeyLongPress(keyCode, event)) {
                        return true;
                    }
                }
            }
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnKeyMultiple.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    if (((ActivitySubscribe.OnKeyMultiple) callback).onKeyMultiple(keyCode, repeatCount, event)) {
                        return true;
                    }
                }
            }
        }
        return super.onKeyMultiple(keyCode, repeatCount, event);
    }

    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnKeyShortcut.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    if (((ActivitySubscribe.OnKeyShortcut) callback).onKeyShortcut(keyCode, event)) {
                        return true;
                    }
                }
            }
        }
        return super.onKeyShortcut(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnKeyUp.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    if (((ActivitySubscribe.OnKeyUp) callback).onKeyUp(keyCode, event)) {
                        return true;
                    }
                }
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnBackPressed.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnBackPressed) callback).onBackPressed();
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnTouchEvent.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    if (((ActivitySubscribe.OnTouchEvent) callback).onTouchEvent(event)) {
                        return true;
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    //========================================= Panel =========================================//

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        super.onPanelClosed(featureId, menu);
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnPanelClosed.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnPanelClosed) callback).onPanelClosed(featureId, menu);
                }
            }
        }
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnCreatePanelMenu.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    if (((ActivitySubscribe.OnCreatePanelMenu) callback).onCreatePanelMenu(featureId, menu)) {
                        return true;
                    }
                }
            }
        }
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnPreparePanel.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    if (((ActivitySubscribe.OnPreparePanel) callback).onPreparePanel(featureId, view, menu)) {
                        return true;
                    }
                }
            }
        }
        return super.onPreparePanel(featureId, view, menu);
    }

    @Nullable
    @Override
    public View onCreatePanelView(int featureId) {
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnCreatePanelView.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    View view = ((ActivitySubscribe.OnCreatePanelView) callback).onCreatePanelView(featureId);
                    if (view != null) {
                        return view;
                    }
                }
            }
        }
        return super.onCreatePanelView(featureId);
    }

    //========================================= Window =========================================//
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnDetachedFromWindow.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnDetachedFromWindow) callback).onDetachedFromWindow();
                }
            }
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnAttachedToWindow.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnAttachedToWindow) callback).onAttachedToWindow();
                }
            }
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnUserLeaveHint.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnUserLeaveHint) callback).onUserLeaveHint();
                }
            }
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnUserInteraction.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnUserInteraction) callback).onUserInteraction();
                }
            }
        }
    }

    //========================================= ContextMenu =========================================//
    @Override
    public void onContextMenuClosed(Menu menu) {
        super.onContextMenuClosed(menu);
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnContextMenuClosed.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnContextMenuClosed) callback).onContextMenuClosed(menu);
                }
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnCreateContextMenu.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnCreateContextMenu) callback).onCreateContextMenu(menu, v, menuInfo);
                }
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnContextItemSelected.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    if (((ActivitySubscribe.OnContextItemSelected) callback).onContextItemSelected(item)) {
                        return true;
                    }
                }
            }
        }
        return super.onContextItemSelected(item);
    }

    //========================================= Memory =========================================//
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnLowMemory.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnLowMemory) callback).onLowMemory();
                }
            }
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnTrimMemory.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnTrimMemory) callback).onTrimMemory(level);
                }
            }
        }
    }

    @Override
    protected void onApplyThemeResource(Resources.Theme theme, int resid, boolean first) {
        super.onApplyThemeResource(theme, resid, first);
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnApplyThemeResource.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnApplyThemeResource) callback).onApplyThemeResource(theme, resid, first);
                }
            }
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnMenuOpened.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    if (((ActivitySubscribe.OnMenuOpened) callback).onMenuOpened(featureId, menu)) {
                        return true;
                    }
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnAttachFragment.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnAttachFragment) callback).onAttachFragment(fragment);
                }
            }
        }
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        if (mSubscribers != null) {
            Set<ActivitySubscribe> callbacks = mSubscribers.get(ActivitySubscribe.OnEnterAnimationComplete.class);
            if (callbacks != null) {
                for (ActivitySubscribe callback : callbacks) {
                    ((ActivitySubscribe.OnEnterAnimationComplete) callback).onEnterAnimationComplete();
                }
            }
        }
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

    @Nullable
    @Override
    public CharSequence onCreateDescription() {
        return super.onCreateDescription();
    }

    //========================================= Change =========================================//
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        super.onWindowAttributesChanged(params);
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }

    //========================================= ActionMode =========================================//
    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return super.onWindowStartingActionMode(callback);
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
        return super.onWindowStartingActionMode(callback, type);
    }
}
