package ua.org.algoritm.terminal.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import ua.org.algoritm.terminal.Adapters.RecyclerAdapterOperation;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.CarDataOutfit;
import ua.org.algoritm.terminal.Objects.OperationOutfits;
import ua.org.algoritm.terminal.R;

public class CarActivityOrderOutfit extends AppCompatActivity {
    private CarDataOutfit carDataOutfit;
    private TextView itemCar;
    private TextView itemBarCode;
    private TextView itemSector;
    private TextView itemRow;
    private TabHost tabHost;

    private RecyclerView recyclerViewOperation;
    private RecyclerAdapterOperation adapterOperation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_order_outfit);

        itemCar = findViewById(R.id.itemCar);
        itemBarCode = findViewById(R.id.itemBarCode);
        itemSector = findViewById(R.id.itemSector);
        itemRow = findViewById(R.id.itemRow);

        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator(getString(R.string.operation));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tab1");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator(getString(R.string.photo));
        tabHost.addTab(tabSpec);
        tabHost.setCurrentTab(0);

        Intent intent = getIntent();
        String orderID = intent.getStringExtra("orderID");
        String carID = intent.getStringExtra("carID");

        carDataOutfit = SharedData.getCarOrderOutfit(orderID, carID);

        itemCar.setText(carDataOutfit.getCar());
        itemBarCode.setText(carDataOutfit.getBarCode());
        itemSector.setText(carDataOutfit.getSector());
        itemRow.setText(carDataOutfit.getRow());

        recyclerViewOperation = findViewById(R.id.recyclerViewOperation);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewOperation.setLayoutManager(layoutManager);

        updateListsOperation();

    }

    private void updateListsOperation() {
        if (adapterOperation == null) {
            adapterOperation = new RecyclerAdapterOperation(this, R.layout.item_car_operations, carDataOutfit.getOperations());
            recyclerViewOperation.setAdapter(adapterOperation);
            adapterOperation.setActionListener(new RecyclerAdapterOperation.ActionListener() {
                @Override
                public void onClick(OperationOutfits operationOutfits) {

                    operationOutfits.setPerformed(!operationOutfits.getPerformed());
                    adapterOperation.notifyDataSetChanged();
                    //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                    //viewCarData(carData);
                }
            });
        } else {
            adapterOperation.notifyDataSetChanged();
        }
    }
}
