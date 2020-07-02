package ua.org.algoritm.terminal.Service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import ua.org.algoritm.terminal.ConnectTo1c.FtpUtil;
import ua.org.algoritm.terminal.ConnectTo1c.SFTPClient;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.NotificationHelper;
import ua.org.algoritm.terminal.Objects.ActInspection;
import ua.org.algoritm.terminal.Objects.CarData;
import ua.org.algoritm.terminal.Objects.PhotoActInspection;
import ua.org.algoritm.terminal.Objects.TypeDamagePhoto;
import ua.org.algoritm.terminal.Objects.TypesPhoto;
import ua.org.algoritm.terminal.R;

public class IntentServicePerformedAct extends IntentService {
    private static final String EXTRA_PENDING_INTENT = "ua.org.algoritm.terminal.Service.extra.PENDING_INTENT";

    private static final String ACTION_INSERT_PHOTO = "ua.org.algoritm.terminal.Service.action.INSERT_PHOTO";
    private static final String EXTRA_INSERT_INSERT_PHOTO = "ua.org.algoritm.terminal.Service.extra.INSERT_INSERT_PHOTO";
    public static final String EXTRA_INSERT_PHOTO = "ua.org.algoritm.terminal.Service.extra.EXTRA_INSERT_PHOTO";
    public static final int REQUEST_CODE_INSERT_PHOTO = 1000;

    public IntentServicePerformedAct() {
        super("IntentServicePerformedAct");
    }

    public static void startInsertPhoto(AppCompatActivity activity, String ACT_ID) {

        Intent intent = new Intent(activity, IntentServicePerformedAct.class);
        PendingIntent pendingIntent = activity.createPendingResult(REQUEST_CODE_INSERT_PHOTO, intent, 0);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);

        intent.setAction(ACTION_INSERT_PHOTO);
        intent.putExtra(EXTRA_INSERT_PHOTO, ACT_ID);

        activity.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INSERT_PHOTO.equals(action)) {
                String ACT_ID = intent.getStringExtra(EXTRA_INSERT_PHOTO);
                start_INSERT_PHOTO(ACT_ID);
            }
        }
    }

    private void start_INSERT_PHOTO(String ACT_ID) {
        ActInspection mActInspection = SharedData.getActInspection(ACT_ID);
        String title = "Отправка FTP " + mActInspection.getDescription();

        ArrayList<PhotoActInspection> photoAll = new ArrayList<>();

        for (int i = 0; i < mActInspection.getEquipments().size(); i++) {
            if (!mActInspection.getEquipments().get(i).getPhotoActInspection().getCurrentPhotoPath().equals("")) {
                photoAll.add(mActInspection.getEquipments().get(i).getPhotoActInspection());
            }
        }

        for (int i = 0; i < mActInspection.getTypesPhotos().size(); i++) {
            TypesPhoto typesPhoto = mActInspection.getTypesPhotos().get(i);
            for (int j = 0; j < typesPhoto.getPhotoActInspections().size(); j++) {
                if (!typesPhoto.getPhotoActInspections().get(j).getCurrentPhotoPath().equals("")) {
                    photoAll.add(typesPhoto.getPhotoActInspections().get(j));
                }
            }
        }

        for (int i = 0; i < mActInspection.getDamages().size(); i++) {
            for (int j = 0; j < mActInspection.getDamages().get(i).getTypeDamagePhoto().size(); j++) {
                TypeDamagePhoto typesPhoto = mActInspection.getDamages().get(i).getTypeDamagePhoto().get(j);

                for (int k = 0; k < typesPhoto.getPhotoActInspections().size(); k++) {
                    if (!typesPhoto.getPhotoActInspections().get(k).getCurrentPhotoPath().equals("")) {
                        photoAll.add(typesPhoto.getPhotoActInspections().get(k));
                    }
                }
            }
        }

        ArrayList<PhotoActInspection> mPhotoAll = new ArrayList<>();
        mPhotoAll.addAll(photoAll);

        SharedData.NOTIFY_ID = SharedData.NOTIFY_ID + 1;
        int id = SharedData.NOTIFY_ID;
        sendMessage(title, getApplicationContext().getResources().getString(R.string.wait_ftp), id);

        boolean send = true;
        for (int i = 0; i < mPhotoAll.size(); i++) {
            PhotoActInspection photo = mPhotoAll.get(i);

            String text = String.format(getApplicationContext().getResources().getString(R.string.send_ftp), i + 1, mPhotoAll.size());
            sendMessage(title, text, id);

            if (sendPhoto(mActInspection, photo)) {
                //SharedData.deletePhoto(photo.getCurrentPhotoPath());
            } else {
                send = false;
                break;
            }
        }
        //handler.postDelayed(this, 1000);
        if (send) {
//                    stopForeground(true);
            stopSelf();
        } else {
            String text = "" + mActInspection.getDescription() + ". " + getApplicationContext().getResources().getString(R.string.error_ftp);
            sendMessageError(title, text, id);
        }
    }


    private void sendMessage(String title, String text, int id) {
        NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
        Notification notification = notificationHelper.createNotification(title, text);
        startForeground(id, notification);
    }

    private void sendMessageError(String title, String text, int id) {
        NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
        notificationHelper.createNotificationError(title, text, id);

//        stopForeground(true);
        stopSelf();
        //startForeground(1, notification);
    }

    private boolean sendPhoto(ActInspection mActInspection, PhotoActInspection photo) {
        boolean uploadFile = false;
        if (!photo.getCurrentPhotoPathFTP().equals("")) {
            uploadFile = true;
            return uploadFile;
        }

        try {

            String host = SharedData.hostFTP;
            int port = SharedData.portFTP;
            String username = SharedData.usernameFTP;
            String password = SharedData.passwordFTP;
            boolean thisSFTP = SharedData.thisSFTP;

            String basePath = "";
            String filePath = "" + mActInspection.getID() + "/" + photo.getListObject();
            if (photo.getListObject().equals("DamagePhoto")) {
                filePath = filePath + "/" + photo.getObjectID();
            }
            String filename = photo.getName();

            if (thisSFTP) {
                SFTPClient sftpClient = new SFTPClient(host, username, password, port);
                sftpClient.connect();
                try {
                    sftpClient.upload(photo.getCurrentPhotoPath(), "foto/" + filePath, photo.getName());
                    uploadFile = true;
                } catch (Exception e) {
                    uploadFile = false;
                } finally {
                    sftpClient.disconnect();
                }
            } else {
                InputStream input = new FileInputStream(new File(photo.getCurrentPhotoPath()));
                uploadFile = FtpUtil.uploadFile(host, port, username, password, basePath, filePath, filename, input);
            }
        } catch (Exception e) {
            uploadFile = false;
        }
        return uploadFile;
    }

}
