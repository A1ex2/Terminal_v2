package ua.org.algoritm.terminal.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import ua.org.algoritm.terminal.ConnectTo1c.FtpUtil;
import ua.org.algoritm.terminal.ConnectTo1c.SFTPClient;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.R;

public class MyManager extends Worker {
    private static final int NOTIFY_ID = 31415926;
    private Context mContext;

    public MyManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        String desc = "Отправка базы данных начата";
        displayNotification(data.getString(String.valueOf(R.string.app_name)), desc);

        send();

        NotificationManager manager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFY_ID);

        return Result.success();
    }

    private void send() {
        String login = SharedData.LOGIN;

        String host = SharedData.hostFTP;
        int port = SharedData.portFTP;
        String username = SharedData.usernameFTP;
        String password = SharedData.passwordFTP;
        boolean thisSFTP = SharedData.thisSFTP;

        Data data = getInputData();

        // отправить базу данных
        displayNotification(data.getString(String.valueOf(R.string.app_name)), "Отправка базы");
        File fileDB = new File(Environment.getDataDirectory() + "/data/ua.org.algoritm.terminal/databases/MyBD.db");
        if (fileDB.exists()) {
            String filePath = "app_backup/" + login + "/db";
            if (!sendFile(fileDB, filePath, thisSFTP, login, host, username, password, port)){
//                return;
            };
        }

        // отправить все фото
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        int i = 0;
        for (File f : storageDir.listFiles()) {
            boolean uploadFile = false;

            if (f.isFile()) {
                i++;
                displayNotification(data.getString(String.valueOf(R.string.app_name)), "Отправка фото " + i + " из " + storageDir.listFiles().length);

                String filePath = "app_backup/" + login + "/pictures";
                uploadFile = sendFile(f, filePath, thisSFTP, login, host, username, password, port);
            }

            if (!uploadFile) {
                break;
            }
        }
    }

    private boolean sendFile(File f, String filePath, boolean thisSFTP, String login, String host, String username, String password, int port) {
        boolean uploadFile = false;

        try {

            String basePath = "";
            String filename = f.getName();

            if (thisSFTP) {
                SFTPClient sftpClient = new SFTPClient(host, username, password, port);
                sftpClient.connect();
                try {
                    sftpClient.upload(f.getAbsolutePath(), "foto/" + filePath, f.getName());
                    uploadFile = true;
                } catch (Exception e) {
                    uploadFile = false;
                } finally {
                    sftpClient.disconnect();
                }
            } else {
                InputStream input = new FileInputStream(new File(f.getAbsolutePath()));
                uploadFile = FtpUtil.uploadFile(host, port, username, password, basePath, filePath, filename, input);
            }
        } catch (Exception e) {
            uploadFile = false;
        }

        return uploadFile;
    }

    private void displayNotification(String task, String desc) {

        NotificationManager manager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("SendDB", "SendDB", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "SendDB")
                .setContentTitle(task)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentText(desc)
                .setSound(null)
                .setAutoCancel(true)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher);

        builder.setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
                .setGroup("My group")
                .setGroupSummary(false)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        manager.notify(NOTIFY_ID, builder.build());
    }

}
