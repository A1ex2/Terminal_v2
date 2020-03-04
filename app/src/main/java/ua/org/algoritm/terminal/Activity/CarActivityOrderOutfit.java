package ua.org.algoritm.terminal.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;

import ua.org.algoritm.terminal.Adapters.RecyclerAdapterOperation;
import ua.org.algoritm.terminal.Adapters.RecyclerAdapterPhoto;
import ua.org.algoritm.terminal.ConnectTo1c.FtpUtil;
import ua.org.algoritm.terminal.ConnectTo1c.SFTPClient;
import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Dispatcher;
import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Objects;
import ua.org.algoritm.terminal.ConnectTo1c.UIManager;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.CarDataOutfit;
import ua.org.algoritm.terminal.Objects.OperationOutfits;
import ua.org.algoritm.terminal.Objects.Photo;
import ua.org.algoritm.terminal.R;
import ua.org.algoritm.terminal.Service.IntentServiceDataBase;

public class CarActivityOrderOutfit extends AppCompatActivity {
    private CarDataOutfit carDataOutfit;
    String orderID;
    String carID;

    private TextView itemCar;
    private TextView itemBarCode;
    private TextView itemSector;
    private TextView itemRow;
    private TabHost tabHost;
    private ImageView imageOk;
    private ImageView imageCancel;
    private FloatingActionButton addPhoto;

    private ArrayList<OperationOutfits> mOperations = new ArrayList<>();
    private RecyclerView recyclerViewOperation;
    private RecyclerAdapterOperation adapterOperation;

    private RecyclerView recyclerViewPhoto;
    private RecyclerAdapterPhoto adapterPhoto;

    private String mCurrentPhotoPath;
    private int tabHostSelect = 0;

    private ProgressDialog mDialog;
    private SaveTaskPhotoFTP mTaskPhotoFTP;

    public static final int REQUEST_TAKE_PHOTO = 1;

    public static final int ACTION_SET_CAR_Outfit = 25;
    public static final int ACTION_ConnectionError = 0;

    public static UIManager uiManager;
    public static SoapFault responseFault;

    public static SoapObject soapParam_Response;
    public static Handler soapHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_order_outfit);

        uiManager = new UIManager(this);
        soapHandler = new incomingHandler(this);

        itemCar = findViewById(R.id.itemCar);
        itemBarCode = findViewById(R.id.itemBarCode);
        itemSector = findViewById(R.id.itemSector);
        itemRow = findViewById(R.id.itemRow);
        imageOk = findViewById(R.id.imageOk);
        imageCancel = findViewById(R.id.imageCancel);

        imageOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carDataOutfit.setOperations(mOperations);
                setCB();
            }
        });

        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addPhoto = findViewById(R.id.addPhoto);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

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
        orderID = intent.getStringExtra("orderID");
        carID = intent.getStringExtra("carID");

        carDataOutfit = SharedData.getCarOrderOutfit(orderID, carID);

        for (int i = 0; i < carDataOutfit.getOperations().size(); i++) {
            mOperations.add(new OperationOutfits(carDataOutfit.getOperations().get(i)));
        }

        itemCar.setText(carDataOutfit.getCar());
        itemBarCode.setText(carDataOutfit.getBarCode());
        itemSector.setText(carDataOutfit.getSector());
        itemRow.setText(carDataOutfit.getRow());

        recyclerViewOperation = findViewById(R.id.recyclerViewOperation);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewOperation.setLayoutManager(layoutManager);

        recyclerViewPhoto = findViewById(R.id.recyclerViewPhoto);
        LinearLayoutManager layoutManagerPhoto = new LinearLayoutManager(this);
        recyclerViewPhoto.setLayoutManager(layoutManagerPhoto);

        updateListsOperation();
        updateListsPhoto();

        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int fromPos = viewHolder.getAdapterPosition();

                mDialog = new ProgressDialog(CarActivityOrderOutfit.this);
                mDialog.setMessage(getString(R.string.wait));
                mDialog.setCancelable(false);
                mDialog.show();

                Photo mPhoto = carDataOutfit.getPhoto().get(fromPos);

                carDataOutfit.getPhoto().remove(mPhoto);
                IntentServiceDataBase.startDeletePhotoCarDataOutfit(CarActivityOrderOutfit.this, mPhoto.getCurrentPhotoPath());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewPhoto);
    }

    private void setCB() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage(getString(R.string.wait_sending));
        mDialog.setCancelable(false);
        mDialog.show();

        SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        String login = preferences.getString("Login", "");
        String password = preferences.getString("Password", "");

        String stringCarData = SOAP_Objects.getCarDataOutfit(orderID, carDataOutfit);

        SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(ACTION_SET_CAR_Outfit, login, password, getApplicationContext());
        dispatcher.string_Inquiry = stringCarData;

        dispatcher.start();
    }

    private void updateListsOperation() {
        if (adapterOperation == null) {
            adapterOperation = new RecyclerAdapterOperation(this, R.layout.item_car_operations, mOperations);
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

    private void updateListsPhoto() {
        if (adapterPhoto == null) {
            adapterPhoto = new RecyclerAdapterPhoto(this, R.layout.item_photo, carDataOutfit.getPhoto());
            recyclerViewPhoto.setAdapter(adapterPhoto);
            adapterPhoto.setActionListener(new RecyclerAdapterPhoto.ActionListener() {
                @Override
                public void onClick(Photo photo) {
                    Intent i = new Intent(getApplicationContext(), ViewPhoto.class);
                    i.putExtra("currentPhotoPath", photo.getCurrentPhotoPath());
                    startActivity(i);
                }
            });
        } else {
            adapterPhoto.notifyDataSetChanged();
        }
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
            Photo photo = new Photo();
            String fileName = new File(mCurrentPhotoPath).getName();
            photo.setName(fileName);
            photo.setCurrentPhotoPath(mCurrentPhotoPath);
            photo.setOrderID(orderID);
            photo.setCarID(carID);
            carDataOutfit.getPhoto().add(photo);

            adapterPhoto.setPhoto(carDataOutfit.getPhoto());

            IntentServiceDataBase.startInsertPhotoCarDataOutfit(CarActivityOrderOutfit.this, orderID, carID, mCurrentPhotoPath);

            mCurrentPhotoPath = "";
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_CANCELED) {
            SharedData.deletePhoto(mCurrentPhotoPath);

        } else if (requestCode == IntentServiceDataBase.REQUEST_CODE_DELETE_PHOTO) {

            adapterPhoto.setPhoto(carDataOutfit.getPhoto());
            updateListsPhoto();

            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
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

    private void dispatchTakePictureIntent() {
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

    class incomingHandler extends Handler {
        private final WeakReference<CarActivityOrderOutfit> mTarget;

        public incomingHandler(CarActivityOrderOutfit context) {
            mTarget = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            CarActivityOrderOutfit target = mTarget.get();

            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

            switch (msg.what) {
                case ACTION_ConnectionError:
                    uiManager.showToast(getString(R.string.errorConnection) + getSoapErrorMessage());
                    break;
                case ACTION_SET_CAR_Outfit: {
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

            if (carDataOutfit.getPhoto().size() != 0) {
                String message = getString(R.string.send_photo);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(message)
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.butt_Yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mTaskPhotoFTP = new SaveTaskPhotoFTP(CarActivityOrderOutfit.this, orderID, carID);
                                mTaskPhotoFTP.execute(carDataOutfit.getPhoto());
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

    public class SaveTaskPhotoFTP extends AsyncTask<ArrayList<Photo>, Integer, Boolean> {
        private Context mContext;
        private ProgressDialog mDialog;
        private String orderID;
        private String carID;
        private boolean error;

        public SaveTaskPhotoFTP(Context context, String orderID, String carID) {
            this.mContext = context;
            this.orderID = orderID;
            this.carID = carID;
            this.error = false;
        }

        @Override
        protected Boolean doInBackground(ArrayList<Photo>... arrayLists) {
            for (ArrayList<Photo> mPhotos : arrayLists) {
                for (int i = 0; i < mPhotos.size(); i++) {
                    Photo photo = mPhotos.get(i);
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

        private boolean sendPhoto(Photo photo) {
            boolean uploadFile = false;

            try {

                String host = SharedData.hostFTP;
                int port = SharedData.portFTP;
                String username = SharedData.usernameFTP;
                String password = SharedData.passwordFTP;
                boolean thisSFTP = SharedData.thisSFTP;

                String basePath = "";
                String filePath = "" + orderID + "/" + carID;
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
