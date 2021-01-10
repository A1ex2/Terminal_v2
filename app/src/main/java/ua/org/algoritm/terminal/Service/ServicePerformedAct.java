package ua.org.algoritm.terminal.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;

import ua.org.algoritm.terminal.Activity.ActInspectionActivity;
import ua.org.algoritm.terminal.ConnectTo1c.FtpUtil;
import ua.org.algoritm.terminal.ConnectTo1c.Message1c;
import ua.org.algoritm.terminal.ConnectTo1c.SFTPClient;
import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Dispatcher;
import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Objects;
import ua.org.algoritm.terminal.Constants;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.MainActivity;
import ua.org.algoritm.terminal.NotificationHelper;
import ua.org.algoritm.terminal.Objects.ActInspection;
import ua.org.algoritm.terminal.Objects.PhotoActInspection;
import ua.org.algoritm.terminal.Objects.TypeDamagePhoto;
import ua.org.algoritm.terminal.Objects.TypesPhoto;
import ua.org.algoritm.terminal.R;

public class ServicePerformedAct extends Service {
    private ArrayList<ActInspection> mActInspections = new ArrayList<>();

    private static final String TAG = "WeatherService";
    //    public static final String EXTRA_COUNT_TO = "count_to";
    public static final String EXTRA_ACT_ID = "act_id";
    public static final String EXTRA_performedAct = "performedAct";
    private final Messenger messenger = new Messenger(new SignalHandler());
    private int countTo;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            ActInspection mActInspection1 = SharedData.getActInspection(intent.getStringExtra(EXTRA_ACT_ID));

            boolean performedAct = intent.getBooleanExtra(EXTRA_performedAct, false);
            mActInspection1.setPerformed(performedAct);

            boolean add = true;
            for (int i = 0; i < mActInspections.size(); i++) {
                if (mActInspection1.getID().equals(mActInspections.get(i).getID())) {
                    add = false;
                }
            }

            if (add) {
                mActInspections.add(mActInspection1);
            }

            for (int i = 0; i < mActInspections.size(); i++) {
                ActInspection mActInspection = mActInspections.get(i);
                if (!mActInspection.sendPhoto) {
                    ArrayList<PhotoActInspection> photoAll = getPhotoAct(mActInspection);
                    countTo = photoAll.size();
                    startCount(mActInspection, photoAll);
                }
            }

        } else if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION_ALL)) {
            for (int i = 0; i < SharedData.ACT_INSPECTION.size(); i++) {
                ActInspection mActInspection1 = SharedData.ACT_INSPECTION.get(i);
                if (mActInspection1.isPerformed()) {
                    if (mActInspection1.sendPerformed) {
                        continue;
                    }

                    mActInspections.add(mActInspection1);
                }
            }

            for (int j = 0; j < mActInspections.size(); j++) {
                ActInspection mActInspection = mActInspections.get(j);
                if (!mActInspection.sendPhoto) {
                    ArrayList<PhotoActInspection> photoAll = getPhotoAct(mActInspection);
                    countTo = photoAll.size();
                    startCount(mActInspection, photoAll);
                }
            }

        } else if (intent.getAction().equals(Constants.ACTION.PREV_CANCEL)) {
//            stopForeground(true);
            stopSelf();
        }
        return START_REDELIVER_INTENT;
    }

    private ArrayList<PhotoActInspection> getPhotoAct(ActInspection mActInspection) {
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

        return photoAll;
    }

    private void startCount(final ActInspection mActInspection,
                            final ArrayList<PhotoActInspection> mPhotoAll) {
        final Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (SharedData.isOfflineReception) {
//                    int id = SharedData.NOTIFY_ID;
                    int id = mActInspection.NOTIFY_ID;

                    String title = "Отправка Акта " + mActInspection.getDescription();
                    sendMessage(title, "отправка изменений", id);

                    Message1c message = new Message1c(SharedData.API, SharedData.LOGIN, SharedData.PASSWORD, getApplicationContext());

                    String stringObject = SOAP_Objects.getActInspection(mActInspection);
                    message.string_Inquiry = stringObject;
                    message.setActInspection();

                    Boolean isMessage = message.isMessage;
                    String textErr = message.text;

                    if (isMessage) {
                        String text = "" + mActInspection.getDescription() + ". " + textErr;
                        sendMessageError(title, text, id + 1);

                        if (!textErr.equals("Все ОК!")) {
                            mActInspections.remove(mActInspection);
                            stopForeground(true);
                            stopSelf();

                            return;
                        }
                    }
                }

                mActInspection.sendPhoto = true;
//                int id = SharedData.NOTIFY_ID;
                int id = mActInspection.NOTIFY_ID;
                String title = "Отправка FTP " + mActInspection.getDescription();
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

                if (mActInspection.isPerformed() & send) {
                    Message1c message = new Message1c(SharedData.API, SharedData.LOGIN, SharedData.PASSWORD, getApplicationContext());

                    String stringObject = SOAP_Objects.getActInspection(mActInspection);
                    message.string_Inquiry = stringObject;
                    message.setStatusActInspection();

                    Boolean isMessage = message.isMessage;
                    String textErr = message.text;

                    if (isMessage) {
                        String text = "" + mActInspection.getDescription() + ". " + textErr;

                        if (textErr.equals("Все ОК!") | textErr.contains("принята")
                                | textErr.contains("выполнен осмотр") | textErr.contains("выдана")) {

                            title = "Отправлено успешно " + mActInspection.getDescription();
                            sendMessageError(title, text, id + 1);

                            mActInspection.sendPerformed = true;
                            SharedData.insertActInspection(mActInspection);

//                            try {
//                                SharedData.getActInspection(mActInspection.getID()).sendPerformed = true;
//                            } catch (Exception e){
//                            }

                        } else{
                            title = "Ошибка отправки " + mActInspection.getDescription();
                            sendMessageError(title, text, id + 1);
                        }
                    }
                }

                //handler.postDelayed(this, 1000);
                mActInspection.sendPhoto = false;
                if (send) {
                    boolean finish = true;
                    for (int i = 0; i < mActInspections.size(); i++) {
                        if (mActInspections.get(i).sendPhoto) {
                            finish = false;
                            break;
                        }
                    }
                    if (finish) {
                        stopForeground(true);
                        stopSelf();
                    }
                } else {
                    String text = "" + mActInspection.getDescription() + ". " + getApplicationContext().getResources().getString(R.string.error_ftp);
                    sendMessageError(title, text, id + 1);
                    stopForeground(true);
                    stopSelf();
                }
            }
        });
        myThread.start();
    }

    private void sendMessage(String title, String text, int id) {
        NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
        Notification notification = notificationHelper.createNotification(title, text);
        startForeground(id, notification);
    }

    private void sendMessageError(String title, String text, int id) {
        NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
        notificationHelper.createNotificationError(title, text, id);

////        stopForeground(true);
//        stopSelf();
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


    private class SignalHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "Got signal");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
