package ua.org.algoritm.terminal.ConnectTo1c;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.Photo;
import ua.org.algoritm.terminal.R;

public class SaveTaskPhotoFTP extends AsyncTask<ArrayList<Photo>, Integer, Boolean> {
    private Context mActivity;
    private ProgressDialog mDialog;
    private String orderID;
    private String carID;

    public SaveTaskPhotoFTP(Activity activity, String orderID, String carID) {
        this.mActivity = activity;
        this.orderID = orderID;
        this.carID = carID;
    }

    @Override
    protected Boolean doInBackground(ArrayList<Photo>... arrayLists) {
        for (ArrayList<Photo> mPhotos : arrayLists) {
            for (int i = 0; i < mPhotos.size(); i++) {
                Photo photo = mPhotos.get(i);
                publishProgress(mPhotos.size(), i + 1);

                if (sendPhoto(photo)) {
                } else {
                    break;
                }
            }
        }
        return null;
    }

    private boolean sendPhoto(Photo photo) {
        boolean uploadFile = false;

        try {

            String host = SharedData.hostFTP;
            int port = SharedData.portFTP;
            String username = SharedData.usernameFTP;
            String password = SharedData.passwordFTP;

            String basePath = "";
            String filePath = "" + orderID + "/" + carID;
            String filename = photo.getName();
            InputStream input = new FileInputStream(new File(photo.getCurrentPhotoPath()));
            uploadFile = FtpUtil.uploadFile(host, port, username, password, basePath, filePath, filename, input);
        } catch (Exception e) {
            uploadFile = false;
        }
        return uploadFile;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mDialog = new ProgressDialog(mActivity);
        mDialog.setMessage(mActivity.getResources().getString(R.string.wait_ftp));
        mDialog.setCancelable(false);
        mDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        mDialog.setMessage(String.format(mActivity.getResources().getString(R.string.send_ftp), values[1], values[0]));
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        Toast.makeText(mActivity, R.string.ok_ftp, Toast.LENGTH_LONG).show();
    }
}
