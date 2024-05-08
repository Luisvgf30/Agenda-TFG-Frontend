package com.example.miagenda;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.miagenda.api.Usuario;
import com.google.gson.Gson;

public class SessionManager {
    private static final String PREF_NAME = "SessionPrefs";
    private static final String KEY_USER = "user";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveUser(Usuario user) {
        String userJson = gson.toJson(user);
        sharedPreferences.edit().putString(KEY_USER, userJson).apply();
    }

    public Usuario getUser() {
        String userJson = sharedPreferences.getString(KEY_USER, null);
        if (userJson != null) {
            return gson.fromJson(userJson, Usuario.class);
        }
        return null;
    }

    public void clearSession() {
        sharedPreferences.edit().remove(KEY_USER).apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.contains(KEY_USER);
    }
}
