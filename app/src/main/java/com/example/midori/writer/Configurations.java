package com.example.midori.writer;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Alessandra on 15/11/15.
 */
public class Configurations {
    private static final String CONF_FILE = "config";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public Configurations() {
        RootActivity rootActivity = RootActivity.getInstanceRootActivity();
        preferences = rootActivity.getContext().getSharedPreferences(CONF_FILE, Context.MODE_PRIVATE);
    }

    public String setConfigurations(String key, String value) {
        editor = preferences.edit();
        editor.putString(key, value);
        boolean res = editor.commit();
        if (res) {
            System.out.println("Configurazione salvata: " + key + "-" + value);
        }
        return value;
    }

}
