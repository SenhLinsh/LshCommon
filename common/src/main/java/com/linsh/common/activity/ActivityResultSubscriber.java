package com.linsh.common.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import com.linsh.base.activity.ActivitySubscribe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/06/02
 *    desc   :
 * </pre>
 */
public class ActivityResultSubscriber implements ActivitySubscribe.OnActivityResult {

    private final Map<Integer, Method> cachedMethods = new HashMap<>();
    private Activity activity;

    @Override
    public void attach(Activity activity) {
        this.activity = activity;
        Method[] methods = activity.getClass().getDeclaredMethods();
        for (Method method : methods) {
            ActivityResult annotation = method.getAnnotation(ActivityResult.class);
            if (annotation != null) {
                int requestCode = annotation.value();
                cachedMethods.put(requestCode, method);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        Method method = cachedMethods.get(requestCode);
        if (method == null)
            return;
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        Class[] types = method.getParameterTypes();
        Object[] args = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            Class type = types[i];
            Annotation[] annotations = paramAnnotations[i];
            if (annotations == null || annotations.length == 0)
                throw new IllegalArgumentException("@ActivityResult method must declare annotation for parameter");
            if (annotations.length > 1)
                throw new IllegalArgumentException("@ActivityResult method must declare at most one annotation for each parameter");
            Annotation annotation = annotations[0];
            if (annotation instanceof ActivityResult.ResultCode) {
                if (type != int.class)
                    throw new IllegalArgumentException("@ResultCode must declare the type as int");
                args[i] = resultCode;
                continue;
            }
            if (annotation instanceof ActivityResult.Intent) {
                if (type != Intent.class)
                    throw new IllegalArgumentException("@Intent must declare the type as Intent");
                args[i] = data;
                continue;
            }
            if (annotation instanceof ActivityResult.IntentData) {
                if (type != Uri.class)
                    throw new IllegalArgumentException("@IntentData must declare the type as Uri");
                args[i] = data.getData();
                continue;
            }
            if (annotation instanceof ActivityResult.IntentExtra) {
                String key = ((ActivityResult.IntentExtra) annotation).value();
                if (type == Object.class) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        args[i] = extras.get(key);
                    }
                } else if (type == boolean.class) {
                    args[i] = data.getBooleanExtra(key, false);
                } else if (type == byte.class) {
                    args[i] = data.getByteExtra(key, (byte) 0);
                } else if (type == short.class) {
                    args[i] = data.getShortExtra(key, (short) 0);
                } else if (type == char.class) {
                    args[i] = data.getCharExtra(key, (char) 0);
                } else if (type == int.class) {
                    args[i] = data.getIntExtra(key, 0);
                } else if (type == long.class) {
                    args[i] = data.getLongExtra(key, 0);
                } else if (type == float.class) {
                    args[i] = data.getFloatExtra(key, 0);
                } else if (type == double.class) {
                    args[i] = data.getDoubleExtra(key, 0);
                } else if (type == String.class) {
                    args[i] = data.getStringExtra(key);
                } else if (type == CharSequence.class) {
                    args[i] = data.getCharSequenceExtra(key);
                } else if (type == Parcelable.class) {
                    args[i] = data.getParcelableExtra(key);
                } else if (type == int[].class) {
                    args[i] = data.getIntArrayExtra(key);
                } else if (type == String[].class) {
                    args[i] = data.getStringArrayExtra(key);
                } else {
                    throw new IllegalArgumentException("can not support @IntentExtra type: " + type);
                }
                continue;
            }
            throw new IllegalArgumentException("can not support this annotation: " + annotation);
        }
        try {
            method.setAccessible(true);
            method.invoke(activity, args);
        } catch (Exception e) {
            throw new RuntimeException("invoke @ActivityResult method crashed", e);
        }
    }
}
