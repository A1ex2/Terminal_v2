package ua.org.algoritm.terminal.receiver;

import android.content.Context;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import ua.org.algoritm.terminal.DataBase.SharedData;

public class MyWorker extends Worker {
    private static final String TAG = "Worker";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.i(TAG, "start");

        String login = SharedData.LOGIN;
        String password = SharedData.PASSWORD;

        Message message = new Message(login, password, getApplicationContext());
        message.getNotifications();

        Boolean isMessage = message.isMessage;
        String text = message.text;

        if (!isMessage) {
            Log.i(TAG, "is empty");
        } else {
            //our work
            createNofication(text);
        }

        QueryPreferences.setIdWorkRequest(getApplicationContext(), "");
        oneOffRequest(getApplicationContext());

        //indicate whether work was successful
        return Result.success();
    }

    public void createNofication(String text){
        NotificationHelperNewAct notificationHelper = new NotificationHelperNewAct(getApplicationContext());
        notificationHelper.createNotification("Есть новые акты", text);
    }

    //create work request
    public static void oneOffRequest(Context context){
        if (!QueryPreferences.getIdWorkRequest(context).equals("")){
            return;
        }

        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInitialDelay(1, TimeUnit.MINUTES)
                .setConstraints(setConstraints())
                .build();

        WorkManager.getInstance().enqueue(oneTimeWorkRequest);

        QueryPreferences.setIdWorkRequest(context, String.valueOf(oneTimeWorkRequest.getId()));
    }

    public static void periodicWorkRequest(){
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(MyWorker.class,1, TimeUnit.MINUTES)
                .setConstraints(setConstraints())
                .build();

        WorkManager.getInstance().enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest);
    }

    public static Constraints setConstraints(){

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        return constraints;

    }
}