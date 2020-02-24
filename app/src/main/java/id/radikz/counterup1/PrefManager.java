package id.radikz.counterup1;

import android.content.Context;
import android.content.SharedPreferences;



public class PrefManager {
    SharedPreferences sharedpreferences;

    public static final String mypreference = "mypref";
    //private Context context;

    public PrefManager(Context context) {
        //sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

    }

    public void deleteKey(final String name) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(name).apply();
    }

    public String catchString(final String name) {
        return sharedpreferences.getString(name, "");
    }

    public Integer catchInt(final String name) {
        return sharedpreferences.getInt(name, 0);
    }

    public Float catchFloat(final String name) {
        return sharedpreferences.getFloat(name, 0);
    }

    public Boolean catchBoolean(final String name) {
        return sharedpreferences.getBoolean(name, false);
    }

    public void saveString(final String name, String value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(name, value);
        editor.apply();
    }

    public void saveBoolean(final String name, boolean value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    public void saveInt(final String name, int value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(name, value);
        editor.apply();
    }

    public void saveFloat(final String name, float value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putFloat(name, value);
        editor.apply();
    }
}
