package com.linsh.lshapp.common.tools;

import android.os.Environment;

import com.linsh.lshapp.common.common.Config;
import com.linsh.utilseverywhere.ContextUtils;
import com.linsh.utilseverywhere.FileUtils;
import com.linsh.utilseverywhere.IOUtils;

import java.io.File;

import io.realm.Realm;


/**
 * Created by Senh Linsh on 17/5/2.
 */

public class CommonFileFactory {

    private static String mAppDir;

    /**
     * 获取应用文件夹，文件夹名称在Config中配置
     */
    public static String getAppDir() {
        if (mAppDir == null) {
            File appDir = new File(Environment.getExternalStorageDirectory(),
                    "lshapp/" + Config.get().getAppNameEn());
            makeDir(appDir);
            mAppDir = appDir.getAbsolutePath();
        }
        return mAppDir;
    }

    private static File getCacheDir() {
        File cacheDir = ContextUtils.getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = ContextUtils.get().getCacheDir();
        }
        return cacheDir;
    }

    private static File getFileAir() {
        File filesDir = ContextUtils.get().getExternalFilesDir(null);
        if (filesDir == null) {
            filesDir = ContextUtils.getFilesDir();
        }
        return filesDir;
    }

    private static File getOutputDir() {
        File dir = new File(getAppDir(), "export");
        makeDir(dir);
        return dir;
    }

    public static File getRealmFile() {
        File file = new File(getAppDir(), "realm/" + Config.get().getAppNameEn() + ".realm");
        if (file.exists()) {
            if (!file.delete()) {
                return file;
            }
        } else {
            FileUtils.makeParentDirs(file);
        }
        Realm realm = Realm.getDefaultInstance();
        realm.writeCopyTo(file);
        IOUtils.close(realm);
        return file;
    }

    /**
     * 获取log文件夹
     */
    public static String getLogFile(String fileName) {
        File file = new File(getAppDir(), "log/" + fileName);
        FileUtils.makeParentDirs(file);
        return file.getAbsolutePath();
    }

    public static File getPatchFile(String fileName) {
        File dir = new File(getFileAir(), "patch");
        makeDir(dir);
        return new File(dir, fileName);
    }

    private static void makeDir(File dir) {
        FileUtils.makeDirs(dir);
    }
}
