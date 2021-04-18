package ua.org.algoritm.terminal.receiver;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

import ua.org.algoritm.terminal.DataBase.SharedData;

public class MyWorkerStart extends Worker {
    private static final String TAG = "MyWorkerStart";

    public MyWorkerStart(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "start");

        MyWorker.oneOffRequest(getApplicationContext(), 1);
        MyWorkerTimeWork.periodicWorkRequest();

        return Result.success();
    }

    //create work request
    public static void oneOffRequest(long timeMinutes) {
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWorkerStart.class)
                .setInitialDelay(timeMinutes, TimeUnit.MINUTES)
                .setConstraints(setConstraints())
                .build();

        WorkManager.getInstance().enqueue(oneTimeWorkRequest);
    }

    public static Constraints setConstraints() {

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        return constraints;

    }
}