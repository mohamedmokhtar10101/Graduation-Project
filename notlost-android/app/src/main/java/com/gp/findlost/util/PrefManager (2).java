package com.gp.findlost.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.gp.findlost.data.model.User;

public class PrefManager {

    public static void saveToken(String accessToken) {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(App.getContext()).edit();
        editor.putString(Constants.KEY_TOKEN, accessToken);
        editor.apply();
    }

    public static String getToken() {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(App.getContext());
        return preferences.getString(Constants.KEY_TOKEN, null);
    }

    public static void deleteToken() {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(App.getContext()).edit();
        editor.remove(Constants.KEY_TOKEN);
        editor.apply();
    }

    public static void saveUser(User user) {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(App.getContext()).edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(Constants.USER, json);
        editor.apply();
    }

    public static User getUser() {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(App.getContext());
        Gson gson = new Gson();
        String json = preferences.getString(Constants.USER, null);
        return gson.fromJson(json, User.class);
    }

    public static void deleteUser() {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(App.getContext()).edit();
        editor.remove(Constants.USER);
        editor.apply();
    }

    public static void saveLocale(String language) {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(App.getContext()).edit();
        editor.putString(Constants.KEY_LOCALE, language);
        editor.apply();
    }

    public static String getLocale() {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(App.getContext());
        return preferences.getString(Constants.KEY_LOCALE, null);
    }

    public static void saveShowIntro(boolean state) {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(App.getContext()).edit();
        editor.putBoolean(Constants.SHOW_INTRO, state);
        editor.apply();
    }

    public static boolean getShowIntro() {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(App.getContext());
        return preferences.getBoolean(Constants.SHOW_INTRO, false);
    }

    public static void logOut() {
        deleteToken();
        deleteToken();
    }

    public static boolean isLogin() {
        return getToken() != null;
    }
}