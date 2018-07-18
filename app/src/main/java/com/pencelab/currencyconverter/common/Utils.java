package com.pencelab.currencyconverter.common;

import android.util.Log;

public class Utils {

    public enum LogType{
        DEBUG,
        REGULAR
    }

    private static final String TAG = "CurrencyConverter";
    private static final String DEBUG_TAG = "CurrencyConverter-debug";

    public static void log(String stage, String item){
        Utils.log(LogType.REGULAR, stage, item);
    }

    public static void log(String stage){
        Utils.log(LogType.REGULAR, stage);
    }

    public static void log(Throwable throwable){
        Utils.log(LogType.REGULAR, throwable);
    }

    public static void log(String stage, Throwable throwable){
        Utils.log(LogType.REGULAR, stage, throwable);
    }

    public static void log(LogType logType, String stage, String item){
        Log.d(Utils.getTag(logType), stage + ":" + Thread.currentThread().getName() + ":" + item);
    }

    public static void log(LogType logType, String stage){
        Log.d(Utils.getTag(logType), stage + ":" + Thread.currentThread().getName());
    }

    public static void log(LogType logType, Throwable throwable){
        Log.e(Utils.getTag(logType), "Error", throwable);
    }

    public static void log(LogType logType, String stage, Throwable throwable){
        Log.e(Utils.getTag(logType), stage + ":" + Thread.currentThread().getName() + ": error", throwable);
    }

    private static String getTag(LogType logType){
        String tag = "";
        switch(logType){
            case DEBUG:
                tag = DEBUG_TAG;
                break;
            default:
                tag = TAG;
                break;
        }
        return tag;
    }

}
