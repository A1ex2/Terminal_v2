package ua.org.algoritm.terminal.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ua.org.algoritm.terminal.Adapters.RecyclerAdapterEquipment;
import ua.org.algoritm.terminal.Adapters.RecyclerAdapterInspection;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.ActInspection;
import ua.org.algoritm.terminal.Objects.Equipment;
import ua.org.algoritm.terminal.Objects.Inspection;
import ua.org.algoritm.terminal.Objects.Photo;
import ua.org.algoritm.terminal.R;
import ua.org.algoritm.terminal.Service.IntentServiceDataBase;

public class ActInspectionActivity extends AppCompatActivity {
    public static final int REQUEST_TAKE_PHOTO = 1;

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
    private Equipment mEquipment;
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
            mAdapterEquipment.setActionListener(new RecyclerAdapterEquipment.ActionListener() {
                @Override
                public void onClicViewPhoto(Equipment equipment) {
                    dispatchTakePictureIntent(equipment);
                }

                @Override
                public void onClicBtnAdd(Equipment equipment) {
                    equipment.setQuantityFact(equipment.getQuantityFact() + 1);
                    updateListEquipments();
                }

                @Override
                public void onClicBtnSub(Equipment equipment) {
                    equipment.setQuantityFact(equipment.getQuantityFact() - 1);
                    updateListEquipments();
                }

                @Override
                public void onClicBtnEqually(Equipment equipment) {
                    equipment.setQuantityFact(equipment.getQuantityPlan());
                    updateListEquipments();
                }
            });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            if (!mEquipment.getPhotoActInspection().getCurrentPhotoPath().equals("")){
                SharedData.deletePhoto(mEquipment.getPhotoActInspection().getCurrentPhotoPath());
            }

            String fileName = new File(mCurrentPhotoPath).getName();
            mEquipment.getPhotoActInspection().setName(fileName);
            mEquipment.getPhotoActInspection().setCurrentPhotoPath(mCurrentPhotoPath);

//            IntentServiceDataBase.startInsertPhotoCarDataOutfit(ActInspectionActivity.this, mActInspection.getID(), mActInspection., mCurrentPhotoPath);

            try {
                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                FileOutputStream fos = null;
                try {
                    File file = new File(mCurrentPhotoPath);
                    fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                } finally {
                    if (fos != null) fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            mCurrentPhotoPath = "";
            mEquipment = null;
            updateListEquipments();
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_CANCELED) {
            SharedData.deletePhoto(mCurrentPhotoPath);

        } else if (requestCode == IntentServiceDataBase.REQUEST_CODE_DELETE_PHOTO) {
//
//            adapterPhoto.setPhoto(carDataOutfit.getPhoto());
//            updateListsPhoto();
//
//            if (mDialog != null && mDialog.isShowing()) {
//                mDialog.dismiss();
//            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent(Equipment equipment) {
        mEquipment = equipment;

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI;
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
}
