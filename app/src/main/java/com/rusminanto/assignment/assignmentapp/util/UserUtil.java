package com.rusminanto.assignment.assignmentapp.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.rusminanto.assignment.assignmentapp.LoginActivity;

/**
 * Created by tyasrus on 18/05/16.
 *
 * class for handle user manager in this app
 */
public class UserUtil {

    public final static String USERNAME = "username";
    public final static String PASSWORD = "password";
    public final static String LOGIN_STATUS = "login_status";

    private Context context;
    private SharedPreferences sharedPreferences;

    public UserUtil(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setUser(String username, String password) {
        sharedPreferences.edit().putString(USERNAME, username).apply();
        sharedPreferences.edit().putString(PASSWORD, password).apply();
        sharedPreferences.edit().putBoolean(LOGIN_STATUS, true).apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(LOGIN_STATUS, false);
    }

    public void logOut() {
        sharedPreferences.edit().putString(USERNAME, "").apply();
        sharedPreferences.edit().putString(PASSWORD, "").apply();
        sharedPreferences.edit().putBoolean(LOGIN_STATUS, false).apply();
        ((AppCompatActivity) context).finish();
        context.startActivity(new Intent(context, LoginActivity.class));
    }
}
