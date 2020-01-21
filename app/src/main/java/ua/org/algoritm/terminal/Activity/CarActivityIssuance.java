package ua.org.algoritm.terminal.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Dispatcher;
import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Objects;
import ua.org.algoritm.terminal.ConnectTo1c.UIManager;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.CarData;
import ua.org.algoritm.terminal.Objects.CarDataIssuance;
import ua.org.algoritm.terminal.Objects.Sector;
import ua.org.algoritm.terminal.R;
import ua.org.algoritm.terminal.Service.IntentServiceDataBase;

public class CarActivityIssuance extends AppCompatActivity {

    private CarDataIssuance carData;

    public static final int ACTION_SET_ISSUANCE_CAR = 23;
    public static final int ACTION_ConnectionError = 0;

    public static UIManager uiManager;
    public static SoapFault responseFault;

    public static SoapObject soapParam_Response;
    public static Handler soapHandler;

    private ProgressDialog mDialog;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_car);
        setContentView(R.layout.activity_car_issuance);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        uiManager = new UIManager(this);
        soapHandler = new CarActivityIssuance.incomingHandler(this);

        TextView description = findViewById(R.id.description);
        TextView textDriver = findViewById(R.id.textDriver);
        TextView textAutoNumber = findViewById(R.id.textAutoNumber);

        TextView car = findViewById(R.id.car);
        TextView editSector = findViewById(R.id.editSector);
        TextView editRow = findViewById(R.id.editRow);
        ImageView imageOk = findViewById(R.id.imageOk);
        ImageView imageCancel = findViewById(R.id.imageCancel);

        imageOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCB();
            }
        });

        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        carData = intent.getParcelableExtra("CarDataIssuance");

        description.setText(carData.getDescription());
        textDriver.setText(carData.getDriver());
        textAutoNumber.setText(carData.getAutoNumber());

        car.setText(carData.getCar());
        editRow.setText(carData.getRow());

        Sector sector = SharedData.getSector(carData.getSectorID());
        editSector.setText(sector.getName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == IntentServiceDataBase.REQUEST_CODE_CAR_DATA) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setCB() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage(getString(R.string.wait_sending));
        mDialog.setCancelable(false);
        mDialog.show();

        SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        String login = preferences.getString("Login", "");
        String password = preferences.getString("Password", "");

        SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(ACTION_SET_ISSUANCE_CAR, login, password, getApplicationContext());
        String stringCarData = SOAP_Objects.getCarDataIssuance(carData);
        dispatcher.string_Inquiry = stringCarData;

        dispatcher.start();

    }

    class incomingHandler extends Handler {
        private final WeakReference<CarActivityIssuance> mTarget;

        public incomingHandler(CarActivityIssuance context) {
            mTarget = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            CarActivityIssuance target = mTarget.get();

            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

            switch (msg.what) {
                case ACTION_ConnectionError:
                    uiManager.showToast(getString(R.string.errorConnection) + getSoapErrorMessage());
                    break;
                case ACTION_SET_ISSUANCE_CAR: {
                    target.checkSetMovingCar();
                }
                break;
            }
        }
    }

    private void checkSetMovingCar() {
        Boolean isSaveSuccess = Boolean.parseBoolean(soapParam_Response.getPropertyAsString("Result"));

        if (isSaveSuccess) {

            uiManager.showToast(getString(R.string.success));
            setResult(Activity.RESULT_OK);
            finish();

        } else {

            uiManager.showToast(soapParam_Response.getPropertyAsString("Description"));

        }

    }

    private String getSoapErrorMessage() {

        String errorMessage;

        if (responseFault == null)
            errorMessage = getString(R.string.textNoInternet);
        else {
            try {
                errorMessage = responseFault.faultstring;
            } catch (Exception e) {
                e.printStackTrace();
                errorMessage = getString(R.string.unknownError);
            }
        }
        return errorMessage;
    }

}
