package ua.org.algoritm.terminal.receiver;

import android.content.Context;
import android.preference.PreferenceManager;

public class QueryPreferences {
    public static String getLogin(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString("Login", "");
    }

    public static void setLogin(Context context, String Login) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("Login", Login)
                .apply();
    }

    public static String getPassword(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString("Password", "");
    }

    public static void setPassword(Context context, String Password) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("Password", Password)
                .apply();
    }

    public static String getApi(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString("Api", "");
    }

    public static void setApi(Context context, String Api) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("Api", Api)
                .apply();
    }

    public static boolean isDriver(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("thisDriver", false);
    }

    public static void setIdWorkRequest(Context context, String id) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("IdWorkRequest", id)
                .apply();
    }
    public static String getIdWorkRequest(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString("IdWorkRequest", "");
    }

    public static void setDateWorkRequest(Context context, long date) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putLong("DateWorkRequest", date)
                .apply();
    }

    public static long getDateWorkRequest(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getLong("DateWorkRequest", 0);
    }
}
