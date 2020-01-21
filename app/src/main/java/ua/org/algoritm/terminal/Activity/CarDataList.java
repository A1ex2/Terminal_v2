package ua.org.algoritm.terminal.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;

import ua.org.algoritm.terminal.Adapters.RecyclerAdapterCarData;
import ua.org.algoritm.terminal.ConnectTo1c.NetworkChangeReceiver;
import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Dispatcher;
import ua.org.algoritm.terminal.ConnectTo1c.UIManager;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.CarData;
import ua.org.algoritm.terminal.R;

public class CarDataList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerAdapterCarData adapter;
    private ArrayList<CarData> carsData = new ArrayList<>();
    private ProgressDialog mDialog;

    private static final int REQUEST_CODE_SCAN = 0x0000c0de;

    public static final int ACTION_CAR_LIST = 20;
    public static final int ACTION_ConnectionError = 0;
    public static final int ACTION_Connection = 1111;
    public static final int ACTION_Connection_Lost = 2222;

    public static UIManager uiManager;
    public static SoapFault responseFault;

    public static SoapObject soapParam_Response;
    public static Handler soapHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_data_list);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        uiManager = new UIManager(this);
        soapHandler = new CarDataList.incomingHandler(this);

        mDialog = new ProgressDialog(CarDataList.this);
        mDialog.setMessage(getString(R.string.wait_update));
        mDialog.setCancelable(false);
        mDialog.show();

        getList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN) {
            IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (Result != null) {
                if (Result.getContents() == null) {

                } else {
                    String tBarCode = Result.getContents();
                    tBarCode = SharedData.clearBarcode(tBarCode);

                    for (int i = 0; i < carsData.size(); i++) {
                        CarData carData = carsData.get(i);

                        if (carData.getBarCode().equals(tBarCode)) {
                            Intent intent = getIntent();
                            intent.putExtra("CarData", carData);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_scaner, menu);

        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                searchItem.collapseActionView();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.setFilter(newText, carsData);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_scaner:
                scanBarCode();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    public static void dialog(boolean value) {

        try {

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void scanBarCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(ScannerActivity.class);
        intentIntegrator.setDesiredBarcodeFormats(intentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setPrompt(getString(R.string.camera_to_the_barcode));
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();
    }

    private void getList() {
        SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(ACTION_CAR_LIST, getApplicationContext());
        dispatcher.start();
    }

    class incomingHandler extends Handler {
        private final WeakReference<CarDataList> mTarget;

        public incomingHandler(CarDataList context) {
            mTarget = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            CarDataList target = mTarget.get();

            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

            switch (msg.what) {

                case ACTION_ConnectionError:
                    uiManager.showToast(getString(R.string.errorConnection) + getSoapErrorMessage());
                    break;

                case ACTION_CAR_LIST:
                    target.checkListResult();
                    break;
                case ACTION_Connection:
                    uiManager.showToast(getString(R.string.connection_for_internet));
                    target.getList();
                    break;
                case ACTION_Connection_Lost:
                    uiManager.showToast(getString(R.string.lost_for_internet));
                    break;
            }
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

    public void checkListResult() {
        try {
            int count = soapParam_Response.getPropertyCount();
            carsData.clear();

            for (int i = 0; i < count; i++) {
                SoapObject carDetail = (SoapObject) soapParam_Response.getProperty(i);

                CarData mCarData = new CarData();
                mCarData.setCarID(carDetail.getPrimitivePropertyAsString("CarID"));
                mCarData.setCar(carDetail.getPrimitivePropertyAsString("Car"));
                mCarData.setBarCode(carDetail.getPrimitivePropertyAsString("BarCode"));
                mCarData.setSectorID(carDetail.getPrimitivePropertyAsString("SectorID"));
                mCarData.setSector(carDetail.getPrimitivePropertyAsString("Sector"));
                mCarData.setRow(carDetail.getPrimitivePropertyAsString("Row"));
                try {
                    mCarData.setProductionDate(carDetail.getPrimitivePropertyAsString("ProductionDate"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                carsData.add(mCarData);
            }

            adapter = new RecyclerAdapterCarData(this, R.layout.item_reception_detail, carsData);
            recyclerView.setAdapter(adapter);
            adapter.setActionListener(new RecyclerAdapterCarData.ActionListener() {
                @Override
                public void onClick(CarData carData) {
                    Intent intent = getIntent();
                    intent.putExtra("CarData", carData);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
