package ua.org.algoritm.terminal.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ua.org.algoritm.terminal.Adapters.RecyclerAdapterOperation;
import ua.org.algoritm.terminal.Adapters.RecyclerAdapterPhoto;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.CarDataOutfit;
import ua.org.algoritm.terminal.Objects.OperationOutfits;
import ua.org.algoritm.terminal.Objects.Photo;
import ua.org.algoritm.terminal.R;

public class CarActivityOrderOutfit extends AppCompatActivity {
    private CarDataOutfit carDataOutfit;
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

    private ArrayList<Photo> mPhotos = new ArrayList<>();
    private RecyclerView recyclerViewPhoto;
    private RecyclerAdapterPhoto adapterPhoto;

   private Uri photoURI;

    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_order_outfit);

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
        String orderID = intent.getStringExtra("orderID");
        String carID = intent.getStringExtra("carID");

        carDataOutfit = SharedData.getCarOrderOutfit(orderID, carID);

        mOperations.addAll(carDataOutfit.getOperations());

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

                }
            });
        } else {
            adapterOperation.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Photo photo = new Photo();
            photo.setUri(photoURI);
            mPhotos.add(photo);
            carDataOutfit.setPhoto(mPhotos);
            photoURI = null;
            updateListsPhoto();
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
//        mCurrentPhotoPath = image.getAbsolutePath();
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
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
}
