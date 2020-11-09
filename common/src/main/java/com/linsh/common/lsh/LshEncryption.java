package com.linsh.common.lsh;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;

import com.linsh.base.LshThread;
import com.linsh.base.common.Callback;
import com.linsh.base.thread.ThreadPolicy;
import com.linsh.common.tools.EncryptTool;
import com.linsh.dialog.DialogComponents;
import com.linsh.dialog.IDialog;
import com.linsh.dialog.text.IInputDialog;
import com.linsh.utilseverywhere.SharedPreferenceUtils;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/10/25
 *    desc   :
 * </pre>
 */
public class LshEncryption {

    private static final String TAG = "LshEncryption";
    private static final String CIPHER_KEY = "omXBQSw4OdhdW1jJG6mnCA==";
    private static final String KEY_PASSWORD = "password";

    public static void fetchPassword(Activity activity, Callback<String> callback) {
        fetchPassword(activity, callback, null);
    }

    public static void fetchPassword(Activity activity, Callback<String> callback, ThreadPolicy callbackThread) {
        String pwd = SharedPreferenceUtils.getString(KEY_PASSWORD);
        LshThread.ui(new Runnable() {
            @Override
            public void run() {
                if (pwd != null) {
                    biometricAuthenticate(activity, pwd, callback, callbackThread);
                    return;
                }
                inputAuthenticate(activity, callback, callbackThread);
            }
        });
    }

    private static void biometricAuthenticate(Context context, String password, Callback<String> callback, ThreadPolicy policy) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            new BiometricPrompt.Builder(context)
                    .setTitle("指纹校验")
                    .setDescription("查看密码需要通过指纹校验")
                    .setNegativeButton("使用密码", context.getMainExecutor(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            inputAuthenticate(context, callback, policy);
                        }
                    })
                    .build()
                    .authenticate(new CancellationSignal(), context.getMainExecutor(), new BiometricPrompt.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                            super.onAuthenticationSucceeded(result);
                            if (policy != null) {
                                LshThread.run(policy, new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onSuccess(password);
                                    }
                                });
                            } else {
                                callback.onSuccess(password);
                            }
                        }

                        @Override
                        public void onAuthenticationError(int errorCode, CharSequence errString) {
                            super.onAuthenticationError(errorCode, errString);
                            if (policy != null) {
                                LshThread.run(policy, new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFailed(new Throwable(errString.toString()));
                                    }
                                });
                            } else {
                                callback.onFailed(new Throwable(errString.toString()));
                            }
                        }
                    });
        } else {
            inputAuthenticate(context, callback, policy);
        }
    }

    private static void inputAuthenticate(Context context, Callback<String> callback, ThreadPolicy policy) {
        DialogComponents.create(context, IInputDialog.class)
                .setNegativeButton()
                .setPositiveButton(new IDialog.OnClickListener() {
                    @Override
                    public void onClick(IDialog dialog) {
                        dialog.dismiss();
                        String pwd = ((IInputDialog) dialog).getText().toString();
                        if (CIPHER_KEY.equals(EncryptTool.encrypt(pwd, pwd))) {
                            // 密码校验正确
                            SharedPreferenceUtils.putString(KEY_PASSWORD, pwd);
                            if (policy != null) {
                                LshThread.run(policy, new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onSuccess(pwd);
                                    }
                                });
                            } else {
                                callback.onSuccess(pwd);
                            }
                        } else {
                            if (policy != null) {
                                LshThread.run(policy, new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFailed(new Throwable("密码不正确"));
                                    }
                                });
                            } else {
                                callback.onFailed(new Throwable("密码不正确"));
                            }
                        }
                    }
                })
                .setTitle("请输入密码")
                .show();
    }
}
