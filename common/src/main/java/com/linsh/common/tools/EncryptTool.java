package com.linsh.common.tools;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/10/19
 *    desc   :
 * </pre>
 */
public class EncryptTool {

    public static String encrypt(String text, String key) {
        byte[] encrypt = encrypt(getBytes(text), getBytes(key));
        return new String(Base64.encode(encrypt, Base64.DEFAULT)).replaceAll("[\r\n]", "");
    }

    public static String decrypt(String text, String key) {
        byte[] decode = Base64.decode(text.getBytes(), Base64.DEFAULT);
        return getString(decrypt(decode, getBytes(key)));
    }

    private static byte[] encrypt(byte[] data, byte[] key) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }

    private static byte[] decrypt(byte[] data, byte[] key) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }

    /**
     * 将转换后的字节数组补齐位数到 16 的整数倍
     */
    private static byte[] getBytes(String text) {
        byte[] bytes = text.getBytes();
        if (bytes.length % 16 > 0) {
            byte[] newBytes = new byte[bytes.length - (bytes.length % 16) + 16];
            System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
            return newBytes;
        }
        return bytes;
    }

    /**
     * 清除补齐的位数
     */
    private static String getString(byte[] bytes) {
        for (int i = bytes.length - 1; i >= 0; i--) {
            if (bytes[i] != 0) {
                byte[] newBytes = new byte[i + 1];
                System.arraycopy(bytes, 0, newBytes, 0, i + 1);
                return new String(newBytes);
            }
        }
        return new String(new byte[0]);
    }
}
