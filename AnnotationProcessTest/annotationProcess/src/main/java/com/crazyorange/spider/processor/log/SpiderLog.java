package com.crazyorange.spider.processor.log;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public class SpiderLog {
    private Messager mLogInfo;

    public SpiderLog(Messager messager) {
        mLogInfo = messager;
    }

    public void d(String msg) {
        printLog(Diagnostic.Kind.NOTE, msg);
    }

    public void w(String msg) {
        printLog(Diagnostic.Kind.WARNING, msg);
    }

    public void e(String msg) {
        printLog(Diagnostic.Kind.ERROR, msg);
    }

    public void printLog(Diagnostic.Kind level, String msg) {
        if (mLogInfo == null) {
            return;
        }
        mLogInfo.printMessage(level, msg);
    }
}
