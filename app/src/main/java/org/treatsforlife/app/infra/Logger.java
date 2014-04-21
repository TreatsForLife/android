package org.treatsforlife.app.infra;

import android.util.Log;

public class Logger {
    // ===========================================================
    // Constants
    // ===========================================================

    private static final String		c_tag	= "treatsforlife logger";
    public static boolean 			c_debug	= true;


    public static int sStackTraceIndex;
    public static int INFO = android.util.Log.INFO;
    public static int DEBUG = android.util.Log.DEBUG;
    public static int WARN = android.util.Log.WARN;
    public static int VERBOSE = android.util.Log.VERBOSE;
    public static int ASSERT = android.util.Log.ASSERT;
    public static int ERROR = android.util.Log.ERROR;


    // ===========================================================
    // Public methods
    // ===========================================================

    public static void l(String str, int type) {
        if (c_debug) {
            if (null == str) {
                return;
            }

            try {
                StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
                String className = stackTraceElements[sStackTraceIndex].getClassName();
                if (className.lastIndexOf(".") > -1)
                    className = className.substring(className.lastIndexOf(".") + 1);
                str = "[" + className + "] " + str;
            } catch (Exception e) {
                Logger.l(e);
            }
            sStackTraceIndex = 3;


            switch (type){
                case (android.util.Log.INFO):{
                    Log.i(c_tag, str);
                    break;
                }
                case (android.util.Log.DEBUG):{
                    Log.d(c_tag, str);
                    break;
                }
                case (android.util.Log.VERBOSE):{
                    Log.v(c_tag, str);
                    break;
                }
                case (android.util.Log.WARN):{
                    Log.w(c_tag, str);
                    break;
                }
                case (android.util.Log.ASSERT):{
                    Log.i(c_tag, str);
                    break;
                }
                case (android.util.Log.ERROR):{
                    Log.e(c_tag, str);
                    break;
                }

            }

        }
    }

    public static void lf(int type, String format, Object... args){
        l(String.format(format, args),type);
    }

    public static void l(String str) {
        sStackTraceIndex = 4;
        Logger.l(str, INFO);

    }

    public static void l(Exception e) {
        if (e == null){
            Logger.l("Given exception is null, can't log", ERROR); // TODO can print current stack trace?
            return;
        }
        if (c_debug) {
            Logger.l(e.toString(), ERROR);
            StackTraceElement[] elements = e.getStackTrace();
            if (null == elements) {
                return;
            }

            for (int i = 0; i < elements.length; i++) {
                if (null != elements[i]) {
                    sStackTraceIndex = 4;
                    l(elements[i].toString(), ERROR);
                }
            }
        }
    }

    // public static void clearLogCat()
    // {
    // try {
    // Runtime runtime = Runtime.getRuntime();
    // String[] cmd = { "logcat -c" } ;
    // runtime.exec(cmd);
    // Thread.sleep(300);
    // } catch (Exception e) { Logger.l(e); }
    // }
}