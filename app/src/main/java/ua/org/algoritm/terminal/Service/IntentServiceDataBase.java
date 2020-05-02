package ua.org.algoritm.terminal.Service;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import ua.org.algoritm.terminal.DataBase.DataBaseHelper;
import ua.org.algoritm.terminal.Objects.CarData;
import ua.org.algoritm.terminal.Objects.Photo;

public class IntentServiceDataBase extends IntentService {

    private static final String EXTRA_PENDING_INTENT = "ua.org.algoritm.terminal.Service.extra.PENDING_INTENT";
    private static final String ACTION_INSERT_CAR_DATA = "ua.org.algoritm.terminal.Service.action.INSERT_CAR_DATA";

    private static final String ACTION_INSERT_PHOTO = "ua.org.algoritm.terminal.Service.action.INSERT_PHOTO";
    private static final String ACTION_DELETE_PHOTO = "ua.org.algoritm.terminal.Service.action.DELETE_PHOTO";
    private static final String ACTION_GET_PHOTO_OUTFIT = "ua.org.algoritm.terminal.Service.action.GET_PHOTO_OUTFIT";

    private static final String ACTION_INSERT_PHOTO_ACT_INSPECTION = "ua.org.algoritm.terminal.Service.action.INSERT_PHOTO_ACT_INSPECTION";
    private static final String ACTION_DELETE_PHOTO_ACT_INSPECTION = "ua.org.algoritm.terminal.Service.action.DELETE_PHOTO_ACT_INSPECTION";
    private static final String ACTION_GET_PHOTO_ACT_INSPECTION = "ua.org.algoritm.terminal.Service.action.GET_PHOTO_ACT_INSPECTION";

    private static final String EXTRA_INSERT_CAR_DATA = "ua.org.algoritm.terminal.Service.extra.INSERT_CAR_DATA";

    private static final String EXTRA_INSERT_PHOTO = "ua.org.algoritm.terminal.Service.extra.INSERT_PHOTO";
    private static final String EXTRA_DELETE_PHOTO = "ua.org.algoritm.terminal.Service.extra.DELETE_PHOTO";
    private static final String EXTRA_GET_PHOTO_OUTFIT = "ua.org.algoritm.terminal.Service.extra.GET_PHOTO_OUTFIT";

    private static final String EXTRA_INSERT_PHOTO_ACT_INSPECTION = "ua.org.algoritm.terminal.Service.extra.INSERT_PHOTO_ACT_INSPECTION";
    private static final String EXTRA_DELETE_PHOTO_ACT_INSPECTION = "ua.org.algoritm.terminal.Service.extra.DELETE_PHOTO_ACT_INSPECTION";
    private static final String EXTRA_GET_PHOTO_ACT_INSPECTION = "ua.org.algoritm.terminal.Service.extra.GET_PHOTO_ACT_INSPECTION";

    public static final String EXTRA_CAR_DATA = "ua.org.algoritm.terminal.Service.extra.EXTRA_CAR_DATA";

    public static final int REQUEST_CODE_CAR_DATA = 100;

    public static final int REQUEST_CODE_PHOTO = 200;
    public static final int REQUEST_CODE_DELETE_PHOTO = 300;
    public static final int REQUEST_CODE_GET_PHOTO_OUTFIT = 400;

    public static final int REQUEST_CODE_PHOTO_ACT_INSPECTION = 500;
    public static final int REQUEST_CODE_DELETE_PHOTO_ACT_INSPECTION = 600;
    public static final int REQUEST_CODE_GET_PHOTO_ACT_INSPECTION = 700;

    public IntentServiceDataBase() {
        super("IntentServiceDataBase");
    }

    public static void startInsertCarData(AppCompatActivity activity, CarData carData) {

        Intent intent = new Intent(activity, IntentServiceDataBase.class);
        PendingIntent pendingIntent = activity.createPendingResult(REQUEST_CODE_CAR_DATA, intent, 0);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);

        intent.setAction(ACTION_INSERT_CAR_DATA);
        intent.putExtra(EXTRA_INSERT_CAR_DATA, carData);

        activity.startService(intent);
    }

    public static void startInsertPhotoCarDataOutfit(AppCompatActivity activity, String orderID, String carID, String currentPhotoPath) {

        Intent intent = new Intent(activity, IntentServiceDataBase.class);
        PendingIntent pendingIntent = activity.createPendingResult(REQUEST_CODE_PHOTO, intent, 0);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);

        intent.setAction(ACTION_INSERT_PHOTO);
        HashMap<String, String> map = new HashMap<>();
        map.put("orderID", orderID);
        map.put("carID", carID);
        map.put("currentPhotoPath", currentPhotoPath);

        intent.putExtra(EXTRA_INSERT_PHOTO, map);

        activity.startService(intent);
    }

    public static void startInsertPhotoActInspection(AppCompatActivity activity, String actID, String listObject, String objectID, String currentPhotoPath) {

        Intent intent = new Intent(activity, IntentServiceDataBase.class);
        PendingIntent pendingIntent = activity.createPendingResult(REQUEST_CODE_PHOTO_ACT_INSPECTION, intent, 0);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);

        intent.setAction(ACTION_INSERT_PHOTO_ACT_INSPECTION);
        HashMap<String, String> map = new HashMap<>();
        map.put("actID", actID);
        map.put("listObject", listObject);
        map.put("objectID", objectID);
        map.put("currentPhotoPath", currentPhotoPath);

        intent.putExtra(EXTRA_INSERT_PHOTO_ACT_INSPECTION, map);

        activity.startService(intent);
    }

    public static void getPhotoOutfit(AppCompatActivity activity, String orderID) {

        Intent intent = new Intent(activity, IntentServiceDataBase.class);
        PendingIntent pendingIntent = activity.createPendingResult(REQUEST_CODE_GET_PHOTO_OUTFIT, intent, 0);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);

        intent.setAction(ACTION_GET_PHOTO_OUTFIT);
        intent.putExtra(EXTRA_GET_PHOTO_OUTFIT, orderID);

        activity.startService(intent);
    }

    public static void startDeletePhotoCarDataOutfit(AppCompatActivity activity, String currentPhotoPath) {

        Intent intent = new Intent(activity, IntentServiceDataBase.class);
        PendingIntent pendingIntent = activity.createPendingResult(REQUEST_CODE_DELETE_PHOTO, intent, 0);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);
        intent.putExtra(EXTRA_DELETE_PHOTO, currentPhotoPath);

        intent.setAction(ACTION_DELETE_PHOTO);

        activity.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            DataBaseHelper helper = new DataBaseHelper(this);
            PendingIntent pendingIntent = intent.getParcelableExtra(EXTRA_PENDING_INTENT);
            Intent result = new Intent();

            if (ACTION_INSERT_CAR_DATA.equals(action)) {
                CarData carData = intent.getParcelableExtra(EXTRA_INSERT_CAR_DATA);
                helper.insertCarData(carData);
                //result.putExtra(EXTRA_CAR_DATA, true);
            } else if (ACTION_INSERT_PHOTO.equals(action)) {

                HashMap<String, String> map = (HashMap<String, String>) intent.getSerializableExtra(EXTRA_INSERT_PHOTO);

                String orderID = map.get("orderID");
                String carID = map.get("carID");
                String currentPhotoPath = map.get("currentPhotoPath");

                helper.insertPhotoCarDataOutfit(orderID, carID, currentPhotoPath);
                //result.putExtra(EXTRA_CAR_DATA, true);

            } else if (ACTION_INSERT_PHOTO_ACT_INSPECTION.equals(action)) {

                HashMap<String, String> map = (HashMap<String, String>) intent.getSerializableExtra(EXTRA_INSERT_PHOTO_ACT_INSPECTION);

                String actID = map.get("actID");
                String listObject = map.get("listObject");
                String objectID = map.get("objectID");
                String currentPhotoPath = map.get("currentPhotoPath");

                helper.insertPhotoActInspection(actID, listObject, objectID, currentPhotoPath);
                //result.putExtra(EXTRA_CAR_DATA, true);

            } else if (ACTION_DELETE_PHOTO.equals(action)) {

                String currentPhotoPath = intent.getStringExtra(EXTRA_DELETE_PHOTO);

                helper.deletePhoto(currentPhotoPath);
                File mFile = new File(currentPhotoPath);
                if (mFile.delete()) {

                }
            } else if (ACTION_GET_PHOTO_OUTFIT.equals(action)) {
                String orderID = intent.getStringExtra(EXTRA_GET_PHOTO_OUTFIT);
                ArrayList<Photo> mPhotos = helper.getPhotoOutfit(orderID);
                result.putExtra("photos", mPhotos);
            }

//            else if (ACTION_BAZ.equals(action)) {
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionBaz(param1, param2);
//            }

            try {
                pendingIntent.send(this, Activity.RESULT_OK, result);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }
}
