package ua.org.algoritm.terminal.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;

import ua.org.algoritm.terminal.Adapters.RecyclerAdapterCarData;
import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Dispatcher;
import ua.org.algoritm.terminal.ConnectTo1c.UIManager;
import ua.org.algoritm.terminal.MainActivity;
import ua.org.algoritm.terminal.Objects.CarData;
import ua.org.algoritm.terminal.R;
import ua.org.algoritm.terminal.ui.acceptance.AcceptanceFragment;

public class CarDataList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerAdapterCarData adapter;
    private ArrayList<CarData> carData = new ArrayList<>();
    private ProgressDialog mDialog;

    public static final int ACTION_CAR_LIST = 20;
    public static final int ACTION_ConnectionError = 0;

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

    private void getList() {
        SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(ACTION_CAR_LIST);
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
            carData.clear();

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


                carData.add(mCarData);
            }

            adapter = new RecyclerAdapterCarData(this, R.layout.item_reception_detail, carData);
            recyclerView.setAdapter(adapter);
            adapter.setActionListener(new RecyclerAdapterCarData.ActionListener() {
                @Override
                public void onClick(CarData carData) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
