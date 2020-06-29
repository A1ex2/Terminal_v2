package ua.org.algoritm.terminal.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import ua.org.algoritm.terminal.Adapters.RecyclerAdapterCarData;
import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Dispatcher;
import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Objects;
import ua.org.algoritm.terminal.ConnectTo1c.UIManager;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.ActInspection;
import ua.org.algoritm.terminal.Objects.CarData;
import ua.org.algoritm.terminal.Objects.Inspection;
import ua.org.algoritm.terminal.Objects.Reception;
import ua.org.algoritm.terminal.Objects.TypeDoc;
import ua.org.algoritm.terminal.R;

public class DetailReception extends AppCompatActivity {
    private Reception reception;
    private TextView description;
    private TextView driver;
    private TextView autoNumber;
    private RecyclerView recyclerView;
    private RecyclerAdapterCarData adapter;

    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_CODE_PUT_CB = 2;

    public static final int ACTION_SET_RECEPTION = 16;
    public static final int ACTION_UPDATE_RECEPTION = 17;
    public static final int ACTION_ConnectionError = 0;

    public static UIManager uiManager;
    public static SoapFault responseFault;

    public static SoapObject soapParam_Response;
    public static Handler soapHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_reception);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uiManager = new UIManager(this);
        soapHandler = new incomingHandler(this);

        String id = getIntent().getStringExtra("Reception");
        reception = SharedData.getReception(id);

        description = findViewById(R.id.description);
        description.setText(reception.getDescription());

        driver = findViewById(R.id.driver);
        driver.setText(reception.getDriver());

        autoNumber = findViewById(R.id.autoNumber);
        autoNumber.setText(reception.getAutoNumber());

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapterCarData(this, R.layout.item_reception_detail, reception.getCarData());
        recyclerView.setAdapter(adapter);
        adapter.setActionListener(new RecyclerAdapterCarData.ActionListener() {
            @Override
            public void onClick(CarData carData) {
                viewCarData(carData);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_reception, menu);
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
                CarData carData = data.getParcelableExtra("CarData");

                for (int i = 0; i < reception.getCarData().size(); i++) {
                    if (carData.getCarID().equals(reception.getCarData().get(i).getCarID())) {
                        reception.getCarData().get(i).setBarCode(carData.getBarCode());
                        reception.getCarData().get(i).setProductionDate(carData.getProductionDate());
                        break;
                    }
                }

                if (SharedData.isActInspection) {

                    boolean performedAct = data.getBooleanExtra("performedAct", false);

                    if (performedAct) {
                        if (SharedData.isOfflineReception) {
                        } else {
                            SharedData.deleteCarData(carData.getCarID(), reception);
                        }
                    }

                    updateListsCarData();

                } else {
                    setCB(carData);
//                    updateLists();
                }
            }
        }
    }

//    private CarData getCarDataByBarCode(String barCode) {
//        ArrayList<CarData> carDataArrayList = reception.getCarData();
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

    private void viewCarData(CarData carData) {
        if (SharedData.isActInspection) {
            {
                ActInspection actInspection = SharedData.getActInspectionReception(reception.getID(), carData.getCarID());

                if (!actInspection.getReceptionID().equals(reception.getID())) {
                    uiManager.showToast("Акт не найден");
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), ActInspectionActivity.class);
                intent.putExtra("actInspection", actInspection.getID());
                intent.putExtra("CarData", carData);
                startActivityForResult(intent, REQUEST_CODE_PUT_CB);
            }
        } else {
            {
                Intent intent = new Intent(DetailReception.this, CarActivity.class);
                intent.putExtra("CarData", carData);
                startActivityForResult(intent, REQUEST_CODE_PUT_CB);
            }
        }
    }

    private void updateLists() {
        if (adapter == null) {
            adapter = new RecyclerAdapterCarData(this, R.layout.item_activity_detail, reception.getCarData());
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void updateListsCarData() {
        if (adapter == null) {
            adapter = new RecyclerAdapterCarData(this, R.layout.item_activity_detail, reception.getCarData());
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setCarData(reception.getCarData());
        }
    }

    class incomingHandler extends Handler {
        private final WeakReference<DetailReception> mTarget;

        public incomingHandler(DetailReception context) {
            mTarget = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            DetailReception target = mTarget.get();

            switch (msg.what) {
                case ACTION_ConnectionError:
                    uiManager.showToast(getString(R.string.errorConnection) + getSoapErrorMessage());
                    break;
                case ACTION_SET_RECEPTION: {
                    target.checkSetReception();
                }
                break;

                case ACTION_UPDATE_RECEPTION: {
                    target.updateListsCarData();
                }
                break;
            }
        }

    }

    private void checkSetReception() {

        Boolean isSaveSuccess = Boolean.parseBoolean(soapParam_Response.getPropertyAsString("Result"));
        String carID = soapParam_Response.getPrimitivePropertyAsString("CarID");

        if (isSaveSuccess) {
            if (!carID.equals("")) {
                SharedData.deleteCarData(carID, reception);
                updateListsCarData();
            }

            uiManager.showToast(getString(R.string.success));

//            setResult(MainActivity.REQUEST_CODE_UPDATE_RECEPTION);
//            finish();

//            SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(ACTION_UPDATE_RECEPTION);
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

    private void setCB(CarData carData) {
        Reception mReception = new Reception(reception);
        mReception.getCarData().add(carData);

        SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        String login = preferences.getString("Login", "");
        String password = preferences.getString("Password", "");

        SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(ACTION_SET_RECEPTION, login, password, getApplicationContext());
        String stringReception = SOAP_Objects.getReception(mReception);
        dispatcher.string_Inquiry = stringReception;

        dispatcher.start();
    }
}
