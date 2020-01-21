package ua.org.algoritm.terminal.ConnectTo1c;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ua.org.algoritm.terminal.DataBase.SharedData;

import static ua.org.algoritm.terminal.MainActivity.dialog;


public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (!isInitialStickyBroadcast()) {
                if (SharedData.isOnline(context)) {
                    dialog(true, context);
                } else {
                    dialog(false, context);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

}
