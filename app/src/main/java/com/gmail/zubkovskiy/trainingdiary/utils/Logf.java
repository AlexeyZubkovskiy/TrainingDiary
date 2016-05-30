package com.gmail.zubkovskiy.trainingdiary.utils;

import android.util.Log;

/**
 * Created by alexey.zubkovskiy@gmail.com on 26.04.2016.
 */
public class Logf {

    Logf() {
    }

    private static boolean flag = true;

    public static synchronized void i(String tag, String message) {
        if (flag)
            Log.i(tag, message);
    }

    public static synchronized void d(String tag, String message) {
        if (flag)
            Log.d(tag, message);
    }

    public static synchronized void e(String tag, String message) {
        if (flag)
            Log.e(tag, message);

    }

    public static synchronized void w(String tag, String message){
        if(flag)
            Log.w(tag,message);
    }


}
