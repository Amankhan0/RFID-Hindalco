package com.example.hellorfid;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "LoginSession";
    private static final String KEY_TOKEN = "token";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Save token
    public void setToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply(); // Save changes
    }

    // Get saved token
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    // Clear session
    public void logout() {
        editor.clear();
        editor.apply();
    }
}
