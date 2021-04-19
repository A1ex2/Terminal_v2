package ua.org.algoritm.terminal.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class WorkManagerStartReceiver extends BroadcastReceiver {
    WorkManager mWorkManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        String id = QueryPreferences.getIdWorkRequest(context);
        if (!id.equals("")){
            MyWorkerTimeWork.cancelPeriodicTask(context);
            MyWorkerTimeWork.startPeriodicTask(context);
        }
    }
}