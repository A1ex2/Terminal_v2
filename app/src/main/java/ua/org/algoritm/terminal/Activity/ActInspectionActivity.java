package ua.org.algoritm.terminal.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;

import ua.org.algoritm.terminal.Adapters.RecyclerAdapterEquipment;
import ua.org.algoritm.terminal.Adapters.RecyclerAdapterInspection;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.ActInspection;
import ua.org.algoritm.terminal.Objects.Equipment;
import ua.org.algoritm.terminal.Objects.Inspection;
import ua.org.algoritm.terminal.R;

public class ActInspectionActivity extends AppCompatActivity {
    private ActInspection mActInspection;
    private TextView itemForm;
    private TextView itemDatePlan;
    private TextView itemState;
    private TextView itemCar;
    private TextView itemBarCode;
    private TextView itemProductionDate;
    private TextView itemSector;
    private TextView itemRow;
    private TabHost tabHost;

    private String mCurrentPhotoPath;
    private int tabHostSelect = 0;

    private RecyclerView listInspection;
    private RecyclerAdapterInspection mAdapterInspection;

    private RecyclerView listEquipment;
    private RecyclerAdapterEquipment mAdapterEquipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_inspection);

        Intent intent = getIntent();
        mActInspection = SharedData.getActInspection(intent.getStringExtra("actInspection"));

        itemForm = findViewById(R.id.itemForm);
        itemDatePlan = findViewById(R.id.itemDatePlan);
        itemState = findViewById(R.id.itemState);
        itemCar = findViewById(R.id.itemCar);
        itemBarCode = findViewById(R.id.itemBarCode);
        itemProductionDate = findViewById(R.id.itemProductionDate);
        itemSector = findViewById(R.id.itemSector);
        itemRow = findViewById(R.id.itemRow);

        listInspection = findViewById(R.id.list_inspection);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listInspection.setLayoutManager(layoutManager);

        listEquipment = findViewById(R.id.list_equipment);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        listEquipment.setLayoutManager(layoutManager2);

        itemForm.setText(mActInspection.getForm());
        itemDatePlan.setText(mActInspection.getInspectionDatePlanString());
        itemState.setText(mActInspection.getState());
        itemCar.setText(mActInspection.getCar());
        itemBarCode.setText(mActInspection.getBarCode());
        itemProductionDate.setText(mActInspection.getProductionDate());
        itemSector.setText(mActInspection.getSector());
        itemRow.setText(mActInspection.getRow());

//        ActionBar mbar = getSupportActionBar();
//        mbar.setTitle(mActInspection.getDescription());
//        mbar.setDisplayHomeAsUpEnabled(true);

        setTitle(mActInspection.getDescription());
        setTabHost();

        updateListInspections();
        updateListEquipments();

    }

    private void updateListInspections() {
        if (mAdapterInspection == null) {
            mAdapterInspection = new RecyclerAdapterInspection(this, R.layout.item_inspection, mActInspection.getInspections());
            listInspection.setAdapter(mAdapterInspection);
            mAdapterInspection.setActionListener(new RecyclerAdapterInspection.ActionListener() {
                @Override
                public void onClick(Inspection inspection) {
                    inspection.setPerformed(!inspection.isPerformed());
                    mAdapterInspection.notifyDataSetChanged();
                }
            });
        } else {
            mAdapterInspection.notifyDataSetChanged();
        }
    }

    private void updateListEquipments() {
        if (mAdapterEquipment == null) {
            mAdapterEquipment = new RecyclerAdapterEquipment(this, R.layout.item_equipment, mActInspection.getEquipments());
            listEquipment.setAdapter(mAdapterEquipment);
//            mAdapterEquipment.setActionListener(new RecyclerAdapterEquipment.ActionListener() {
//                @Override
//                public void onClick(Equipment equipment) {
//                    inspection.setPerformed(!inspection.isPerformed());
//                    mAdapterEquipment.notifyDataSetChanged();
//                }
//            });
        } else {
            mAdapterEquipment.notifyDataSetChanged();
        }
    }

    private void setTabHost() {
        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setContent(R.id.linearLayoutInfo);
        tabSpec.setIndicator(getString(R.string.text_info));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.linearLayoutInspection);
        tabSpec.setIndicator(getString(R.string.text_inspection));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setContent(R.id.linearLayoutPhoto);
        tabSpec.setIndicator(getString(R.string.text_photo));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setContent(R.id.linearLayoutEquipment);
        tabSpec.setIndicator(getString(R.string.text_complete));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag5");
        tabSpec.setContent(R.id.linearLayoutDamage);
        tabSpec.setIndicator(getString(R.string.text_damage));
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mCurrentPhotoPath", mCurrentPhotoPath);

        tabHostSelect = tabHost.getCurrentTab();
        outState.putInt("tabHostSelect", tabHostSelect);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentPhotoPath = savedInstanceState.getString("mCurrentPhotoPath");

        tabHostSelect = savedInstanceState.getInt("tabHostSelect", 0);
        tabHost.setCurrentTab(tabHostSelect);
    }

}
