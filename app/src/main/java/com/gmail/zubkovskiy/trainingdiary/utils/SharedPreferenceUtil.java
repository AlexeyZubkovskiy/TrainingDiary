package com.gmail.zubkovskiy.trainingdiary.utils;

/**
 * This class is utility to save some data in SharedPreferences
 */

import android.content.Context;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.gmail.zubkovskiy.trainingdiary.global_context.TrainingDiary;

public class SharedPreferenceUtil {

    public static final String SHARED_PREFERENCES_NAME = "TrainingDiary";
    public static final String USER_TOKEN = "user-token";
    public static final String USER_LOGIN = "user-login";
    public static final String USER_DEFAULT_LOGIN = "default";
    public static final String LOGIN = "Login";
    public static final String YES = "yes";
    public static final String NO = "no";

    private static SharedPreferences sp = TrainingDiary.getInstance()
            .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);


    //this method return true if user is login.
    public static boolean isLogin() {
        if (!TextUtils.isEmpty(getToken()))//check this
            return true;
        return false;
    }

    //this method return user-token if user login
    public static String getToken() {
        return sp.getString(USER_TOKEN, null);
    }

    //this method save user-token when user login
    public static void saveToken(String token) {
        sp.edit().putString(USER_TOKEN, token).apply();
    }

    //this method clear user-token when logout
    public static void removeToken() {
        sp.edit().remove(USER_TOKEN).apply();
    }

    //this method return Login if user login else it returns default-string
    public static String getLogin() {
        if (sp.getString(USER_LOGIN, null) != null)
            return sp.getString(USER_LOGIN, null);
        else
            return USER_DEFAULT_LOGIN;
    }

    //this method save Login when user login
    public static void saveLogin(String login) {
        sp.edit().putString(USER_LOGIN, login).apply();
    }

    //this method clear Login when logout
    public static void removeLogin() {
        sp.edit().remove(USER_LOGIN).apply();
    }


}
