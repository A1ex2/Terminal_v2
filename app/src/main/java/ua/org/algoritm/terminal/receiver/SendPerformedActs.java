package ua.org.algoritm.terminal.receiver;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import ua.org.algoritm.terminal.ConnectTo1c.FtpUtil;
import ua.org.algoritm.terminal.ConnectTo1c.Message1c;
import ua.org.algoritm.terminal.ConnectTo1c.SFTPClient;
import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Objects;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.ActInspection;
import ua.org.algoritm.terminal.Objects.PhotoActInspection;
import ua.org.algoritm.terminal.Objects.TypeDamagePhoto;
import ua.org.algoritm.terminal.Objects.TypesPhoto;
import ua.org.algoritm.terminal.R;

public class SendPerformedActs {
    public static void sendPerformed(Context context) {
        for (int i = 0; i < SharedData.ACT_INSPECTION.size(); i++) {
            ActInspection mActInspection = SharedData.ACT_INSPECTION.get(i);

            if (mActInspection.isPerformed() & !mActInspection.sendPerformed) {
                if (!mActInspection.sendPhoto) {
                    try {
                        start(mActInspection, context);
                    } catch (Exception e) {
                        Log.d("myLogsTerminal", "" + e.toString());
                    }
                }
            }
        }
    }

    private static void start(ActInspection mActInspection, Context context) {
        ArrayList<PhotoActInspection> mPhotoAll = getPhotoAct(mActInspection);

        if (SharedData.isOfflineReception) {
            int id = mActInspection.NOTIFY_ID;

            Message1c message = new Message1c(SharedData.API, SharedData.LOGIN, SharedData.PASSWORD, context);

            String stringObject = SOAP_Objects.getActInspection(mActInspection);
            message.string_Inquiry = stringObject;
            message.setActInspection();

            Boolean isMessage = message.isMessage;
            String textErr = message.text;

            if (isMessage) {
                String text = "" + mActInspection.getDescription() + ". " + textErr;

                if (!textErr.equals("Все ОК!")) {
                    return;
                }
            }
        }

        mActInspection.sendPhoto = true;

        int id = mActInspection.NOTIFY_ID;

        boolean send = true;
        for (int i = 0; i < mPhotoAll.size(); i++) {
            PhotoActInspection photo = mPhotoAll.get(i);

            String text = String.format(context.getResources().getString(R.string.send_ftp), i + 1, mPhotoAll.size());

            if (sendPhoto(mActInspection, photo)) {

            } else {
                send = false;
                break;
            }
        }

        if (mActInspection.isPerformed() & send) {
            Message1c message = new Message1c(SharedData.API, SharedData.LOGIN, SharedData.PASSWORD, context);

            String stringObject = SOAP_Objects.getActInspection(mActInspection);
            message.string_Inquiry = stringObject;
            message.setStatusActInspection();

            Boolean isMessage = message.isMessage;
            String textErr = message.text;

            if (isMessage) {
                String text = "" + mActInspection.getDescription() + ". " + textErr;

                if (textErr.equals("Все ОК!") | textErr.contains("принята")
                        | textErr.contains("выполнен осмотр") | textErr.contains("выдана")) {

//                    title = "Отправлено успешно " + mActInspection.getDescription();
//                    sendMessageError(title, text, id + 1);

                    mActInspection.sendPerformed = true;
                    SharedData.insertActInspection(mActInspection);

//                            try {
//                                SharedData.getActInspection(mActInspection.getID()).sendPerformed = true;
//                            } catch (Exception e){
//                            }

                } else {
//                    title = "Ошибка отправки " + mActInspection.getDescription();
//                    sendMessageError(title, text, id + 1);
                }
            }
        }


    }

    static private ArrayList<PhotoActInspection> getPhotoAct(ActInspection mActInspection) {
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

    private static boolean sendPhoto(ActInspection mActInspection, PhotoActInspection photo) {
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
