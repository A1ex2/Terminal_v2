package ua.org.algoritm.terminal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

import ua.org.algoritm.terminal.Activity.ActInspectionActivity;
import ua.org.algoritm.terminal.Activity.Password;
import ua.org.algoritm.terminal.Service.ServicePerformedAct;

public class NotificationHelper {
    private Context mContext;
    private NotificationManager mNotificationManager;
    public NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    public static final int NOTIFY_ID = 101;

    public NotificationHelper(Context context) {
        mContext = context;
    }

    public Notification createNotification(String title, String message) {
        Intent notificationIntent = new Intent(mContext, ActInspectionActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Intent cancelIntent = new Intent(mContext, ServicePerformedAct.class);
        cancelIntent.setAction(Constants.ACTION.PREV_CANCEL);
        PendingIntent pcancelIntent = PendingIntent.getService(mContext, 0,
                cancelIntent, 0);

        /**Creates an explicit intent for an Activity in your app**/
//        Intent resultIntent = new Intent(mContext, Password.class);
//        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
//                0 /* Request code */, resultIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);

//        Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),
//                R.mipmap.ic_launcher);

        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setSmallIcon(R.drawable.ic_file_upload);
        mBuilder.setContentTitle(title)
                .setTicker(title)
                .setContentText(message)
                .setOngoing(true)
//                .setLargeIcon(
//                        Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//                .setContentIntent(resultPendingIntent)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Отмена",
                        pcancelIntent).build();

        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Отправка файлов на FTP", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;

        return mBuilder.build();
    }

    public void createNotificationError(String title, String message, int id) {
        /**Creates an explicit intent for an Activity in your app**/
//        Intent resultIntent = new Intent(mContext, Password.class);
//        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
//                0 /* Request code */, resultIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);

//        Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),
//                R.mipmap.ic_launcher);

        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setSmallIcon(R.drawable.ic_file_upload);
        mBuilder.setContentTitle(title)
                .setTicker(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
//                .setLargeIcon(
//                        Bitmap.createScaledBitmap(icon, 128, 128, false))
//                .setContentIntent(resultPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Отправка файлов на FTP", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;

//        mNotificationManager.notify(NOTIFY_ID, mBuilder.build());
        mNotificationManager.notify(id, mBuilder.build());
    }

    public void cancelNotification(int id) {
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.cancel(id);
        mNotificationManager.cancel(NOTIFY_ID);
    }
}