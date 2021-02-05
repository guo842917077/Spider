package com.crazyorange.spider.logger;

import android.util.Log;

/**
 * @author guojinlong01
 * @Date 2021-02-05
 * <p>
 * 日志工具类
 */
public class SpiderLogger {
    private static boolean isOpenDebug = false;
    private static final int LOG_CAPACITY = 100;
    private static final String TAG = "Spider";

    public static void openDebug(boolean isDebug) {
        isOpenDebug = isDebug;
    }

    private static String createLog(String msg) {
        if (isOpenDebug()) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            String className = "";
            String method = "";
            if (elements.length > 5) {
                StackTraceElement element = elements[4];
                className = element.getClassName();
                method = element.getMethodName();
            }
            // todo 避免频繁的创建 log 日志
            StringBuffer logStr = new StringBuffer(LOG_CAPACITY);
            if (!className.equals("")) {
                logStr.append(className).append(":").append(method).append(":").append(msg);
                return logStr.toString();
            } else {
                return msg;
            }
        }
        return null;
    }

    public static void i(String msg) {
        printLog(Log.INFO, msg);
    }

    public static void d(String msg) {
        printLog(Log.DEBUG, msg);
    }

    public static void e(String msg) {
        printLog(Log.ERROR, msg);
    }

    public static void w(String msg) {
        printLog(Log.WARN, msg);
    }

    public static void printLog(int level, String msg) {
        msg = createLog(msg);
        if (msg == null) {
            return;
        }
        switch (level) {
            case Log.DEBUG:
                Log.d(TAG, msg);
                break;
            case Log.INFO:
                Log.i(TAG, msg);
                break;
            case Log.WARN:
                Log.w(TAG, msg);
                break;
            case Log.ERROR:
                Log.e(TAG, msg);
                break;
            default:
                Log.d(TAG, msg);
        }
    }

    public static boolean isOpenDebug() {
        return isOpenDebug;
    }
}
