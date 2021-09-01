package com.iowk.iowkcore.utils;

import java.util.Locale;

public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 拼接入参
     *
     * @param spliterStr 拼接分割符
     * @param objs       变参
     */
    public static String concatParams(String spliterStr, Object... objs) {

        StringBuilder sb = new StringBuilder();
        if (objs != null) {
            for (int i = 0; i < objs.length; i++) {
                sb.append(objs[i]);
                if (i != objs.length - 1) {
                    sb.append(spliterStr);
                }
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * Convert byte[] to hex string
     *
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] bArr, int i) {
        if (i == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder("");
        for (int i2 = 0; i2 < i; i2++) {
            String hexString = Integer.toHexString(bArr[i2] & -1);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString);
        }
        return sb.toString().toUpperCase(Locale.US);
    }
}