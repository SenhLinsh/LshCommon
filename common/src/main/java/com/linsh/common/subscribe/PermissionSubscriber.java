package com.linsh.common.subscribe;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.SparseArray;

import com.linsh.base.LshLog;
import com.linsh.base.activity.ActivitySubscribe;
import com.linsh.lshutils.utils.IdUtilsEx;
import com.linsh.utilseverywhere.PermissionUtils;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

/**
 * <pre>
 *    author : Senh Linsh
 *    date   : 2018/02/04
 *    desc   : 辅助申请权限的 Act 订阅者, 内部封装对 OnRequestPermissionsResult 回调的处理
 * </pre>
 */
public class PermissionSubscriber implements ActivitySubscribe.OnRequestPermissionsResult {

    private static final String TAG = "PermissionSubscriber";
    private SparseArray<Object[]> listeners = new SparseArray<>();
    private Activity activity;

    @Override
    public void attach(Activity activity) {
        this.activity = activity;
    }

    public PermissionSubscriber requestPermission(@NonNull String permission, OnGrantedListener onGrantedListener) {
        return requestPermission(permission, onGrantedListener, null);
    }

    public PermissionSubscriber requestPermission(@NonNull String permission, OnGrantedListener onGrantedListener, OnDeniedListener onDeniedListener) {
        int requestCode = IdUtilsEx.generateId();
        if (onGrantedListener != null || onDeniedListener != null) {
            listeners.put(requestCode, new Object[]{onGrantedListener, onDeniedListener});
        }
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
        return this;
    }

    public PermissionSubscriber checkOrRequestPermission(@NonNull String permission, OnGrantedListener onGrantedListener) {
        return checkOrRequestPermission(permission, onGrantedListener, null);
    }

    public PermissionSubscriber checkOrRequestPermission(@NonNull String permission, OnGrantedListener onGrantedListener, OnDeniedListener onDeniedListener) {
        if (PermissionUtils.checkPermission(permission)) {
            onGrantedListener.onGranted(permission);
        } else {
            requestPermission(permission, onGrantedListener, onDeniedListener);
        }
        return this;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LshLog.d(TAG, "onRequestPermissionsResult: requestCode=" + requestCode + ", permissions="
                + Arrays.toString(permissions) + ", grantResults=" + Arrays.toString(grantResults));
        Object[] listener = listeners.get(requestCode);

        if (listener != null) {
            if (permissions.length == 1) {
                String permission = permissions[0];
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (listener[0] != null)
                        ((OnGrantedListener) listener[0]).onGranted(permission);
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    if (listener[1] != null)
                        ((OnDeniedListener) listener[1]).onDenied(permission, false);
                } else {
                    if (listener[1] != null)
                        ((OnDeniedListener) listener[1]).onDenied(permission, true);
                }
            }
            listeners.remove(requestCode);
        }
    }

    public interface OnGrantedListener {

        void onGranted(String permission);
    }

    public interface OnDeniedListener {
        void onDenied(String permission, boolean isNeverAsked);
    }
}
