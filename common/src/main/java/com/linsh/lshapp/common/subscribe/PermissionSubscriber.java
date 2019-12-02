package com.linsh.lshapp.common.subscribe;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.SparseArray;

import com.linsh.base.activity.ActivitySubscribe;
import com.linsh.lshutils.utils.IdUtilsEx;
import com.linsh.utilseverywhere.PermissionUtils;

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

    private SparseArray<PermissionListener> listeners = new SparseArray<>();
    private Activity activity;

    @Override
    public void attach(Activity activity) {
        this.activity = activity;
    }

    public void requestPermission(@NonNull String permission, PermissionListener listener) {
        int requestCode = IdUtilsEx.generateId();
        listeners.put(requestCode, listener);
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
    }

    public void checkOrRequestPermission(@NonNull String permission, PermissionListener listener) {
        if (PermissionUtils.checkPermission(permission)) {
            listener.onGranted(permission);
        } else {
            requestPermission(permission, listener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionListener listener = listeners.get(requestCode);
        if (listener != null) {
            if (permissions.length == 1) {
                String permission = permissions[0];
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    listener.onGranted(permission);
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    listener.onDenied(permission, false);
                } else {
                    listener.onDenied(permission, true);
                }
            }
            listeners.remove(requestCode);
        }
    }

    public interface PermissionListener {

        void onGranted(String permission);

        void onDenied(String permission, boolean isNeverAsked);
    }
}
