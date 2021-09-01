package com.iowk.iowkcore.common;

import android.util.Log;

public class VLogUtil {
    public static boolean isDebug = true;
    public static String tag = "iowkcore";

    public static void println(String str) {
        if (isDebug) {
            Log.v(tag, str);
        }
    }
}
