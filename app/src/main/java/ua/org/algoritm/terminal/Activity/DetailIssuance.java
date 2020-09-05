package ua.org.algoritm.terminal.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.lang.ref.WeakReference;

import ua.org.algoritm.terminal.Adapters.RecyclerAdapterCarDataIssuanceNew;
import ua.org.algoritm.terminal.ConnectTo1c.UIManager;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.CarDataIssuance;
import ua.org.algoritm.terminal.Objects.Issuance;
import ua.org.algoritm.terminal.R;

public class DetailIssuance extends AppCompatActivity {
    private Issuance issuance;
    private TextView description;
    private TextView driver;
    private TextView autoNumber;
    private RecyclerView recyclerView;
    private RecyclerAdapterCarDataIssuanceNew adapter;

    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_CODE_PUT_CB = 2;

    public static final int ACTION_SET_ISSUANCE = 16;
    public static final int ACTION_UPDATE_ISSUANCE = 17;
    public static final int ACTION_ConnectionError = 0;

    public static UIManager uiManager;
    public static SoapFault responseFault;

    public static SoapObject soapParam_Response;
    public static Handler soapHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_issuance);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uiManager = new UIManager(this);
        soapHandler = new incomingHandler(this);

        String id = getIntent().getStringExtra("Issuance");
        issuance = SharedData.getIssuance(id);

        description = findViewById(R.id.description);
        description.setText(issuance.getDescription());

        driver = findViewById(R.id.driver);
        driver.setText(issuance.getDriver());

        autoNumber = findViewById(R.id.autoNumber);
        autoNumber.setText(issuance.getAutoNumber());

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapterCarDataIssuanceNew(this, R.layout.item_issuance_detail, issuance.getCarData());
        recyclerView.setAdapter(adapter);
        adapter.setActionListener(new RecyclerAdapterCarDataIssuanceNew.ActionListener() {
            @Override
            public void onClick(CarDataIssuance carData) {
                viewCarData(carData);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_issuance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.searchBarCode:
//                scanBarCode();
//                break;
//
//            case R.id.saveCB:
//                setCB();
//                break;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLists();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PUT_CB) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String carID = data.getStringExtra("CarID");
                    if (!carID.equals("")) {
                        SharedData.deleteCarDataIssuance(carID, issuance);
                        updateListsCarData();
                    }

                    updateLists();
                }
            }
        }
    }

//    private CarData getCarDataByBarCode(String barCode) {
//        ArrayList<CarData> carDataArrayList = issuance.getCarData();
//        CarData carData = new CarData();
//        for (int i = 0; i < carDataArrayList.size(); i++) {
//            CarData mCarData = carDataArrayList.get(i);
//            if (barCode.equals(mCarData.getBarCode())) {
//                carData = mCarData;
//                break;
//            }
//        }
//
//        return carData;
//    }

    private void viewCarData(CarDataIssuance carData) {
        Intent intent = new Intent(DetailIssuance.this, CarActivityIssuance.class);
        intent.putExtra("CarDataIssuance", carData);
        startActivityForResult(intent, REQUEST_CODE_PUT_CB);
    }

    private void updateLists() {
        if (adapter == null) {
            adapter = new RecyclerAdapterCarDataIssuanceNew(this, R.layout.item_activity_detail, issuance.getCarData());
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void updateListsCarData() {
        if (adapter == null) {
            adapter = new RecyclerAdapterCarDataIssuanceNew(this, R.layout.item_activity_detail, issuance.getCarData());
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setCarData(issuance.getCarData());
        }
    }

    class incomingHandler extends Handler {
        private final WeakReference<DetailIssuance> mTarget;

        public incomingHandler(DetailIssuance context) {
            mTarget = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            DetailIssuance target = mTarget.get();

            switch (msg.what) {
                case ACTION_ConnectionError:
                    uiManager.showToast(getString(R.string.errorConnection) + getSoapErrorMessage());
                    break;
                case ACTION_SET_ISSUANCE: {
                    target.checkSetIssuance();
                }
                break;

                case ACTION_UPDATE_ISSUANCE: {
                    target.updateListsCarData();
                }
                break;
            }
        }
    }

    private void checkSetIssuance() {

        Boolean isSaveSuccess = Boolean.parseBoolean(soapParam_Response.getPropertyAsString("Result"));
        String carID = soapParam_Response.getPrimitivePropertyAsString("CarID");

        if (isSaveSuccess) {
//            if (!carID.equals("")){
//                SharedData.deleteCarDataIssuance(carID, issuance);
//                updateListsCarData();
//            }
//
//            uiManager.showToast(getString(R.string.success));

//            setResult(MainActivity.REQUEST_CODE_UPDATE_Issuance);
//            finish();

//            SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(ACTION_UPDATE_Issuance);
//            dispatcher.start();

        } else {

            //uiManager.showToast(soapParam_Response.getPropertyAsString("Description"));
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(soapParam_Response.getPropertyAsString("Description"))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.butt_OK), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
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

    private void setCB(CarDataIssuance carData) {
//        Issuance mIssuance = new Issuance(issuance);
//        mIssuance.getCarData().add(carData);
//
//        SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
//        String login = preferences.getString("Login", "");
//        String password = preferences.getString("Password", "");
//
//        SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(ACTION_SET_Issuance, login, password, getApplicationContext());
//        String stringIssuance = SOAP_Objects.getIssuance(mIssuance);
//        dispatcher.string_Inquiry = stringIssuance;
//
//        dispatcher.start();
    }
}
