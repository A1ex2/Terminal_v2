package ua.org.algoritm.terminal.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ua.org.algoritm.terminal.Adapters.RecyclerAdapterEquipment;
import ua.org.algoritm.terminal.Adapters.RecyclerAdapterInspection;
import ua.org.algoritm.terminal.Adapters.RecyclerAdapterTypesPhoto;
import ua.org.algoritm.terminal.ConnectTo1c.FtpUtil;
import ua.org.algoritm.terminal.ConnectTo1c.SFTPClient;
import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Dispatcher;
import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Objects;
import ua.org.algoritm.terminal.ConnectTo1c.UIManager;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.ActInspection;
import ua.org.algoritm.terminal.Objects.Equipment;
import ua.org.algoritm.terminal.Objects.Inspection;
import ua.org.algoritm.terminal.Objects.PhotoActInspection;
import ua.org.algoritm.terminal.Objects.TypesPhoto;
import ua.org.algoritm.terminal.R;
import ua.org.algoritm.terminal.Service.IntentServiceDataBase;

public class ActInspectionActivity extends AppCompatActivity {
    public static final int REQUEST_TAKE_PHOTO_Equipment = 1;
    public static final int REQUEST_TAKE_PHOTO_TypesPhoto = 2;
    public static final int REQUEST_UPDATE_PHOTO_TypesPhoto = 3;

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
    private TypesPhoto mTypesPhoto;
    private int tabHostSelect = 0;

    private RecyclerView listInspection;
    private RecyclerAdapterInspection mAdapterInspection;

    private RecyclerView listEquipment;
    private RecyclerAdapterEquipment mAdapterEquipment;

    private RecyclerView listTypesPhoto;
    private RecyclerAdapterTypesPhoto mAdapterTypesPhoto;

    private ProgressDialog mDialog;
    private SaveTaskPhotoFTP mTaskPhotoFTP;

    public static final int ACTION_SET_ACT = 28;
    public static final int ACTION_ConnectionError = 0;

    public static UIManager uiManager;
    public static SoapFault responseFault;

    public static SoapObject soapParam_Response;
    public static Handler soapHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_inspection);

        uiManager = new UIManager(this);
        soapHandler = new incomingHandler(this);

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

        listTypesPhoto = findViewById(R.id.list_type_photo);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this);
        listTypesPhoto.setLayoutManager(layoutManager3);

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
        updateListTypesPhoto();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_db, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveCB:
                setCB();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setCB() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage(getString(R.string.wait_sending));
        mDialog.setCancelable(false);
        mDialog.show();

        SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        String login = preferences.getString("Login", "");
        String password = preferences.getString("Password", "");

        String stringObject = SOAP_Objects.getActInspection(mActInspection);

        SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(ACTION_SET_ACT, login, password, getApplicationContext());
        dispatcher.string_Inquiry = stringObject;

        dispatcher.start();
    }

    private void updateListTypesPhoto() {
        if (mAdapterTypesPhoto == null) {
            mAdapterTypesPhoto = new RecyclerAdapterTypesPhoto(this, R.layout.item_photo_act, mActInspection.getTypesPhotos());
            listTypesPhoto.setAdapter(mAdapterTypesPhoto);
            mAdapterTypesPhoto.setActionListener(new RecyclerAdapterTypesPhoto.ActionListener() {
                @Override
                public void onClicViewPhotos(TypesPhoto typesPhoto) {
                    Intent i = new Intent(getApplicationContext(), ListActPhoto.class);
                    i.putExtra("actInspectionID", mActInspection.getID());
                    i.putExtra("typesPhotoID", typesPhoto.getTypePhotoID());
                    startActivityForResult(i, REQUEST_UPDATE_PHOTO_TypesPhoto);
                }

                @Override
                public void onClicBtnAdd(TypesPhoto typesPhoto) {
                    mTypesPhoto = typesPhoto;
                    dispatchTakePictureIntent(REQUEST_TAKE_PHOTO_TypesPhoto);
                }
            });
        } else {
            mAdapterTypesPhoto.notifyDataSetChanged();
        }
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
                    mEquipment = equipment;
                    dispatchTakePictureIntent(REQUEST_TAKE_PHOTO_Equipment);
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

        if (requestCode == REQUEST_TAKE_PHOTO_Equipment && resultCode == RESULT_OK) {

            if (!mEquipment.getPhotoActInspection().getCurrentPhotoPath().equals("")) {
                SharedData.deletePhotoActInspection(mEquipment.getPhotoActInspection().getCurrentPhotoPath());
            }

            String fileName = new File(mCurrentPhotoPath).getName();
            PhotoActInspection photoActInspection = new PhotoActInspection();
            photoActInspection.setActID(mActInspection.getID());
            photoActInspection.setName(fileName);
            photoActInspection.setObjectID(mEquipment.getEquipmentID());
            photoActInspection.setListObject(mEquipment.getListObject());
            photoActInspection.setCurrentPhotoPath(mCurrentPhotoPath);

            mEquipment.setPhotoActInspection(photoActInspection);

            IntentServiceDataBase.startInsertPhotoActInspection(ActInspectionActivity.this,
                    mActInspection.getID(), mEquipment.getListObject(), mEquipment.getEquipmentID(), mCurrentPhotoPath);

            decodeFile();

            mCurrentPhotoPath = "";
            mEquipment = null;
            updateListEquipments();

        } else if (requestCode == REQUEST_TAKE_PHOTO_TypesPhoto && resultCode == RESULT_OK) {

            String fileName = new File(mCurrentPhotoPath).getName();

            PhotoActInspection photoActInspection = new PhotoActInspection();
            photoActInspection.setActID(mActInspection.getID());
            photoActInspection.setName(fileName);
            photoActInspection.setObjectID(mTypesPhoto.getTypePhotoID());
            photoActInspection.setListObject(mTypesPhoto.getListObject());
            photoActInspection.setCurrentPhotoPath(mCurrentPhotoPath);

            mTypesPhoto.getPhotoActInspections().add(photoActInspection);

            IntentServiceDataBase.startInsertPhotoActInspection(ActInspectionActivity.this,
                    mActInspection.getID(), mTypesPhoto.getListObject(), mTypesPhoto.getTypePhotoID(), mCurrentPhotoPath);

            decodeFile();

            mCurrentPhotoPath = "";
            mTypesPhoto = null;
            updateListTypesPhoto();

        } else if ((requestCode == REQUEST_TAKE_PHOTO_Equipment | requestCode == REQUEST_TAKE_PHOTO_TypesPhoto) && resultCode == RESULT_CANCELED) {
            SharedData.deletePhotoActInspection(mCurrentPhotoPath);

        } else if (requestCode == REQUEST_UPDATE_PHOTO_TypesPhoto) {
            updateListTypesPhoto();

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

    private void dispatchTakePictureIntent(int REQUEST) {
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

                startActivityForResult(takePictureIntent, REQUEST);
            }
        }
    }

    private void decodeFile() {
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
    }

    class incomingHandler extends Handler {
        private final WeakReference<ActInspectionActivity> mTarget;

        public incomingHandler(ActInspectionActivity context) {
            mTarget = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            ActInspectionActivity target = mTarget.get();

            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

            switch (msg.what) {
                case ACTION_ConnectionError:
                    uiManager.showToast(getString(R.string.errorConnection) + getSoapErrorMessage());
                    break;
                case ACTION_SET_ACT: {
                    target.checkSetAct();
                }
                break;
            }
        }
    }
    private void checkSetAct() {
        Boolean isSaveSuccess = Boolean.parseBoolean(soapParam_Response.getPropertyAsString("Result"));

        if (isSaveSuccess) {
            final ArrayList<PhotoActInspection> photoAll = new ArrayList<>();

            for (int i = 0; i < mActInspection.getEquipments().size(); i++) {
                if (!mActInspection.getEquipments().get(i).getPhotoActInspection().getCurrentPhotoPath().equals("")){
                    photoAll.add(mActInspection.getEquipments().get(i).getPhotoActInspection());
                }
            }

            for (int i = 0; i < mActInspection.getTypesPhotos().size(); i++) {
                TypesPhoto typesPhoto = mActInspection.getTypesPhotos().get(i);
                for (int j = 0; j < typesPhoto.getPhotoActInspections().size(); j++) {
                    if (!typesPhoto.getPhotoActInspections().get(j).getCurrentPhotoPath().equals("")){
                        photoAll.add(typesPhoto.getPhotoActInspections().get(j));
                    }
                }
            }

            if (photoAll.size() != 0) {
                uiManager.showToast(getString(R.string.success));
                String message = getString(R.string.send_photo);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(message)
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.butt_Yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mTaskPhotoFTP = new SaveTaskPhotoFTP(ActInspectionActivity.this, mActInspection.getID());
                                mTaskPhotoFTP.execute(photoAll);
                            }
                        })
                        .setNegativeButton(getString(R.string.butt_Not), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                                finish();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            } else {
                finish();
            }

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

    public class SaveTaskPhotoFTP extends AsyncTask<ArrayList<PhotoActInspection>, Integer, Boolean> {
        private Context mContext;
        private ProgressDialog mDialog;
        private String actID;
        private boolean error;

        public SaveTaskPhotoFTP(Context context, String actID) {
            this.mContext = context;
            this.actID = actID;
            this.error = false;
        }

        @Override
        protected Boolean doInBackground(ArrayList<PhotoActInspection>... arrayLists) {
            for (ArrayList<PhotoActInspection> mPhotos : arrayLists) {
                for (int i = 0; i < mPhotos.size(); i++) {
                    PhotoActInspection photo = mPhotos.get(i);
                    publishProgress(mPhotos.size(), i + 1);

                    if (sendPhoto(photo)) {
                        //SharedData.deletePhoto(photo.getCurrentPhotoPath());
                    } else {
                        error = true;
                        break;
                    }
                }
            }
            return null;
        }

        private boolean sendPhoto(PhotoActInspection photo) {
            boolean uploadFile = false;
            if (!photo.getCurrentPhotoPathFTP().equals("")){
                uploadFile = true;
                return uploadFile;
            }

            try {

                String host = SharedData.hostFTP;
                int port = SharedData.portFTP;
                String username = SharedData.usernameFTP;
                String password = SharedData.passwordFTP;
                boolean thisSFTP = SharedData.thisSFTP;

                String basePath = "";
                String filePath = "" + actID + "/" + photo.getListObject();
                String filename = photo.getName();

                if (thisSFTP) {
                    SFTPClient sftpClient = new SFTPClient(host, username, password, port);
                    sftpClient.connect();
                    try {
                        sftpClient.upload(photo.getCurrentPhotoPath(), "foto/" + filePath, photo.getName());
                        uploadFile = true;
                    } catch (Exception e) {
                        uploadFile = false;
                    } finally {
                        sftpClient.disconnect();
                    }
                } else {
                    InputStream input = new FileInputStream(new File(photo.getCurrentPhotoPath()));
                    uploadFile = FtpUtil.uploadFile(host, port, username, password, basePath, filePath, filename, input);
                }
            } catch (Exception e) {
                uploadFile = false;
            }
            return uploadFile;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage(mContext.getResources().getString(R.string.wait_ftp));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mDialog.setMessage(String.format(mContext.getResources().getString(R.string.send_ftp), values[1], values[0]));
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

            if (error) {
                Toast.makeText(mContext, R.string.error_ftp, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, R.string.ok_ftp, Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }
        }
    }

}

