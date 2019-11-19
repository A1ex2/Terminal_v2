package ua.org.algoritm.terminal.Service;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import ua.org.algoritm.terminal.DataBase.DataBaseHelper;
import ua.org.algoritm.terminal.Objects.CarData;

public class IntentServiceDataBase extends IntentService {

    private static final String EXTRA_PENDING_INTENT = "ua.org.algoritm.terminal.Service.extra.PENDING_INTENT";
    private static final String ACTION_INSERT_CAR_DATA = "ua.org.algoritm.terminal.Service.action.INSERT_CAR_DATA";

    private static final String EXTRA_INSERT_CAR_DATA = "ua.org.algoritm.terminal.Service.extra.INSERT_CAR_DATA";
    public static final String EXTRA_CAR_DATA = "ua.org.algoritm.terminal.Service.extra.EXTRA_CAR_DATA";

    public static final int REQUEST_CODE_CAR_DATA = 100;

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
