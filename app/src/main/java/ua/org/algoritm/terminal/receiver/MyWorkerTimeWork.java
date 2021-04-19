package ua.org.algoritm.terminal.receiver;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import ua.org.algoritm.terminal.DataBase.SharedData;

public class MyWorkerTimeWork extends Worker {
    private static final String TAG = "MyWorkerTimeWork";

    public MyWorkerTimeWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "start");

        if (SharedData.thisDriver) {

            String login = SharedData.LOGIN;
            String password = SharedData.PASSWORD;

            try {
                // отправка выполненных
                SendPerformedActs.sendPerformed(getApplicationContext());
            } catch (Exception e) {

            }

            try {
                // загрузка новых актов
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
            } catch (Exception e) {

            }

//            MyWorker.oneOffRequest(getApplicationContext(), 3);
//            MyWorker.oneOffRequest(getApplicationContext(), 6);
//            MyWorker.oneOffRequest(getApplicationContext(), 9);
//            MyWorker.oneOffRequest(getApplicationContext(), 12);
//            MyWorker.oneOffRequest(getApplicationContext(), 15);
        }

        //indicate whether work was successful
        return Result.success();
    }

    public static void periodicWorkRequest() {
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(MyWorkerTimeWork.class, 15, TimeUnit.MINUTES)
                .setConstraints(setConstraints())
                .build();

        WorkManager.getInstance().enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
    }

    public static Constraints setConstraints() {
        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        return constraints;
    }

    public void createNofication(String text) {
        NotificationHelperNewAct notificationHelper = new NotificationHelperNewAct(getApplicationContext());
        notificationHelper.createNotification("Есть новые акты", text);
    }


    public static void startPeriodicTask(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(MyWorkerTimeWork.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance().enqueue(periodicWorkRequest);
        WorkManager.getInstance()
                .getWorkInfoByIdLiveData(periodicWorkRequest.getId());

        QueryPreferences.setIdWorkRequest(context, String.valueOf(periodicWorkRequest.getId()));
    }

    public static void cancelPeriodicTask(Context context) {
        String idWorkRequest = QueryPreferences.getIdWorkRequest(context);
        WorkManager.getInstance().cancelWorkById(UUID.fromString(idWorkRequest));
        QueryPreferences.setIdWorkRequest(context, "");
    }
}