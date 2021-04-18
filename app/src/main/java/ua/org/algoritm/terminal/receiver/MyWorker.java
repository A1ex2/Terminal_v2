package ua.org.algoritm.terminal.receiver;

import android.content.Context;

import android.util.Log;

import java.util.Date;
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
    private static final String TAG = "MyWorker";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "start");
        try {
            String login = SharedData.LOGIN;
            String password = SharedData.PASSWORD;

            try {
                // отправка выполненных
                SendPerformedActs.sendPerformed(getApplicationContext());
            } catch (Exception e){

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
            } catch (Exception e){

            }
        } catch (Exception e){
            Log.d("myLogsTerminal", "" + e.toString());
        }
//        QueryPreferences.setIdWorkRequest(getApplicationContext(), "");
//        oneOffRequest(getApplicationContext());

        //indicate whether work was successful
        return Result.success();
    }

    public void createNofication(String text) {
        NotificationHelperNewAct notificationHelper = new NotificationHelperNewAct(getApplicationContext());
        notificationHelper.createNotification("Есть новые акты", text);
    }

    //create work request
    public static void oneOffRequest(Context context, long timeMinutes) {
//        if (!QueryPreferences.getIdWorkRequest(context).equals("")) {
//            long dateWorkRequest = QueryPreferences.getDateWorkRequest(context);
//            if (new Date().getTime() - dateWorkRequest < 60000) {
//                return;
//            }
//        }
//
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInitialDelay(timeMinutes, TimeUnit.MINUTES)
                .setConstraints(setConstraints())
                .build();

        WorkManager.getInstance().enqueue(oneTimeWorkRequest);

//        Date date = new Date();
//        QueryPreferences.setIdWorkRequest(context, String.valueOf(oneTimeWorkRequest.getId()));
//        QueryPreferences.setDateWorkRequest(context, date.getTime());
    }

    public static Constraints setConstraints() {

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        return constraints;

    }
}