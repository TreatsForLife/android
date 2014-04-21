package org.treatsforlife.app.entities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.treatsforlife.app.infra.Globals;

public class User {
    public String id;
    public String fullName;

    public User() {}

    public User(Context context) {
        load(context);
    }

    public void load(Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        id = sp.getString(Globals.ID_KEY, "");
        fullName = sp.getString(Globals.FULLNAME_KEY, "");
    }

    public void persist(Context context) {
        Editor e = getSharedPreferences(context).edit();
        e.putString(Globals.ID_KEY, id);
        e.putString(Globals.FULLNAME_KEY, fullName);
        e.commit();
    }

    public static void reset(Context context) {
        getSharedPreferences(context).edit().clear().commit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Globals.USER_PREFS_NAME, Context.MODE_PRIVATE);
    }
}