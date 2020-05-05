package ua.org.algoritm.terminal.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ua.org.algoritm.terminal.Adapters.RecyclerAdapterTypesDamagePhoto;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.ActInspection;
import ua.org.algoritm.terminal.Objects.ClassificationDamage;
import ua.org.algoritm.terminal.Objects.Damage;
import ua.org.algoritm.terminal.Objects.DegreesDamage;
import ua.org.algoritm.terminal.Objects.Detail;
import ua.org.algoritm.terminal.Objects.OriginDamage;
import ua.org.algoritm.terminal.Objects.Scheme;
import ua.org.algoritm.terminal.Objects.TypeDamage;
import ua.org.algoritm.terminal.Objects.TypeDamagePhoto;
import ua.org.algoritm.terminal.R;

public class DamageDetailActivity extends AppCompatActivity {
    public static final int REQUEST_SET_DETAIL = 1;

    public static final int REQUEST_TAKE_PHOTO_TypesPhoto = 2;
    public static final int REQUEST_UPDATE_PHOTO_TypesPhoto = 3;

    private RecyclerView listTypesPhoto;
    private RecyclerAdapterTypesDamagePhoto mAdapterTypesPhoto;

    private String mCurrentPhotoPath;
    private TypeDamagePhoto mTypesPhoto;

    private ActInspection actInspection;
    private Damage mDamage;
    private boolean newDamage = true;

    private ArrayList<Scheme> mSchemes = new ArrayList<>();
    private ArrayList<Detail> details = new ArrayList<>();

    private AutoCompleteTextView itemPart;
    private ImageView imagePart;

    private ImageView searchScheme;

    private AutoCompleteTextView itemTypesDamage;
    private ImageView imageTypesDamage;

    private AutoCompleteTextView itemDegreesDamage;
    private ImageView imageDegreesDamage;

    private EditText detailDamage;

    private AutoCompleteTextView itemClassificationDamage;
    private ImageView imageClassificationDamage;

    private AutoCompleteTextView itemOriginDamage;
    private ImageView imageOriginDamage;

    private AutoCompleteTextView width;
    private AutoCompleteTextView height;
    private AutoCompleteTextView comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_damage_info);

        setTitle(getString(R.string.add_detail_damage));

        Intent intent = getIntent();
        actInspection = SharedData.getActInspection(intent.getStringExtra("actInspectionID"));
        String idDamage = intent.getStringExtra("idDamage");
        mSchemes = SharedData.getSchemes(actInspection);
        mDamage = new Damage();
        if (!idDamage.equals("")) {
            if (idDamage.equals("null")) {
                for (int i = 0; i < actInspection.getDamages().size(); i++) {
                    if (actInspection.getDamages().get(i).getDetail() == null) {
//                        mDamage = actInspection.getDamages().get(i);
                        mDamage.copyDamage(actInspection.getDamages().get(i));
                        newDamage = false;
                        break;
                    }
                }
            } else {
                for (int i = 0; i < actInspection.getDamages().size(); i++) {
                    if (idDamage.equals(actInspection.getDamages().get(i).getDetail().getDetailID())) {
//                        mDamage = actInspection.getDamages().get(i);
                        mDamage.copyDamage(actInspection.getDamages().get(i));
                        newDamage = false;
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < mSchemes.size(); i++) {
            addDetail(mSchemes.get(i).getDetails());
        }

        itemPart = findViewById(R.id.itemPart);
        itemPart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDamage.setDetail((Detail) itemPart.getAdapter().getItem(position));
            }
        });

        imagePart = findViewById(R.id.imagePart);
        imagePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemPart.showDropDown();
            }
        });

        searchScheme = findViewById(R.id.searchScheme);
        searchScheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DamageDetailSchemeActivity.class);
                intent.putExtra("actInspectionID", actInspection.getID());
                startActivityForResult(intent, REQUEST_SET_DETAIL);
            }
        });

        itemTypesDamage = findViewById(R.id.itemTypesDamage);
        itemTypesDamage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDamage.setTypeDamage((TypeDamage) itemTypesDamage.getAdapter().getItem(position));
            }
        });

        imageTypesDamage = findViewById(R.id.imageTypesDamage);
        imageTypesDamage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemTypesDamage.showDropDown();
            }
        });

        itemDegreesDamage = findViewById(R.id.itemDegreesDamage);
        itemDegreesDamage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDamage.setDegreesDamage((DegreesDamage) itemDegreesDamage.getAdapter().getItem(position));
            }
        });

        imageDegreesDamage = findViewById(R.id.imageDegreesDamage);
        imageDegreesDamage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemDegreesDamage.showDropDown();
            }
        });

        detailDamage = findViewById(R.id.detailDamage);

        itemClassificationDamage = findViewById(R.id.itemClassificationDamage);
        itemClassificationDamage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDamage.setClassificationDamage((ClassificationDamage) itemClassificationDamage.getAdapter().getItem(position));
            }
        });

        imageClassificationDamage = findViewById(R.id.imageClassificationDamage);
        imageClassificationDamage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClassificationDamage.showDropDown();
            }
        });

        itemOriginDamage = findViewById(R.id.itemOriginDamage);
        itemOriginDamage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDamage.setOriginDamage((OriginDamage) itemOriginDamage.getAdapter().getItem(position));
            }
        });

        imageOriginDamage = findViewById(R.id.imageOriginDamage);
        imageOriginDamage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemOriginDamage.showDropDown();
            }
        });

        width = findViewById(R.id.width);
        height = findViewById(R.id.height);
        comment = findViewById(R.id.comment);

        setDetailAdapter();

        setTypesDamageAdapter();
        setDegreesDamageAdapter();
        setClassificationDamageAdapter();
        setOriginDamageAdapter();

        listTypesPhoto = findViewById(R.id.list_type_photo);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this);
        listTypesPhoto.setLayoutManager(layoutManager3);

        updateListTypesPhoto();

        if (!newDamage){
            itemPart.setText(mDamage.getDetail() == null ? "" : mDamage.getDetail().toString() );
            itemTypesDamage.setText(mDamage.getTypeDamage() == null ? "" : mDamage.getTypeDamage().toString() );
            itemDegreesDamage.setText(mDamage.getDegreesDamage() == null ? "" : mDamage.getDegreesDamage().toString() );
            detailDamage.setText(mDamage.getDetailDamage());
            itemClassificationDamage.setText(mDamage.getClassificationDamage() == null ? "" : mDamage.getClassificationDamage().toString() );
            itemOriginDamage.setText(mDamage.getOriginDamage() == null ? "" : mDamage.getOriginDamage().toString() );
            width.setText(mDamage.getWidthDamage());
            height.setText(mDamage.getHeightDamage());
            comment.setText(mDamage.getCommentDamage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_damage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.butt_OK:
                if (mDamage.getDetail() == null) {
                    String message = "Укажите деталь!";

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(message)
                            .setCancelable(true)
                            .setPositiveButton(getString(R.string.butt_OK), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                    return true;
                }

                mDamage.setDetailDamage(detailDamage.getText().toString());
                mDamage.setCommentDamage(comment.getText().toString());
                mDamage.setWidthDamage(width.getText().toString());
                mDamage.setHeightDamage(height.getText().toString());

                if (newDamage) {
                    actInspection.getDamages().add(mDamage);
                } else {
                    if (mDamage.getDetail() == null) {
                        for (int i = 0; i < actInspection.getDamages().size(); i++) {
                            if (actInspection.getDamages().get(i).getDetail() == null) {

                                actInspection.getDamages().get(i).copyDamage(mDamage);
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < actInspection.getDamages().size(); i++) {
                            if (mDamage.getDetail().getDetailID().equals(actInspection.getDamages().get(i).getDetail().getDetailID())) {

                                actInspection.getDamages().get(i).copyDamage(mDamage);
                                break;
                            }
                        }
                    }

                }

//                Toast.makeText(getApplicationContext(), R.string.saveCB, Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
                return true;

            case R.id.butt_Cancel:

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateListTypesPhoto() {
        if (mAdapterTypesPhoto == null) {
//            mAdapterTypesPhoto = new RecyclerAdapterTypesDamagePhoto(this, R.layout.item_photo_act, actInspection.getTypeDamagePhoto());
//            listTypesPhoto.setAdapter(mAdapterTypesPhoto);
//            mAdapterTypesPhoto.setActionListener(new RecyclerAdapterTypesDamagePhoto.ActionListener() {
//                @Override
//                public void onClicViewPhotos(TypeDamagePhoto typesPhoto) {
////                    Intent i = new Intent(getApplicationContext(), ListActPhoto.class);
////                    i.putExtra("actInspectionID", actInspection.getID());
////                    i.putExtra("typesPhotoID", typesPhoto.getTypePhotoID());
////                    startActivityForResult(i, REQUEST_UPDATE_PHOTO_TypesPhoto);
//                }
//
//                @Override
//                public void onClicBtnAdd(TypeDamagePhoto typesPhoto) {
//                    mTypesPhoto = typesPhoto;
//                    dispatchTakePictureIntent(REQUEST_TAKE_PHOTO_TypesPhoto);
//                }
//            });
        } else {
            mAdapterTypesPhoto.notifyDataSetChanged();
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

    private void addDetail(ArrayList<Detail> addDetails) {
        for (int i = 0; i < addDetails.size(); i++) {
            Detail detailAdd = addDetails.get(i);
            boolean add = true;

            for (int j = 0; j < details.size(); j++) {
                if (details.get(j).getDetailID().equals(detailAdd.getDetailID())) {
                    add = false;
                    break;
                }
            }

            if (add) {
                details.add(detailAdd);
            }
        }
    }

    private void setDetailAdapter() {
        sortDetails();

        ArrayAdapter<Detail> adapter = new ArrayAdapter<Detail>(this, android.R.layout.simple_dropdown_item_1line, details);
        itemPart.setAdapter(adapter);
    }

    private void setTypesDamageAdapter() {
        ArrayAdapter<TypeDamage> adapter = new ArrayAdapter<TypeDamage>(this, android.R.layout.simple_dropdown_item_1line, SharedData.TypesDamages);
        itemTypesDamage.setAdapter(adapter);
    }

    private void setDegreesDamageAdapter() {
        ArrayAdapter<DegreesDamage> adapter = new ArrayAdapter<DegreesDamage>(this, android.R.layout.simple_dropdown_item_1line, SharedData.DegreesDamages);
        itemDegreesDamage.setAdapter(adapter);
    }

    private void setClassificationDamageAdapter() {
        ArrayAdapter<ClassificationDamage> adapter = new ArrayAdapter<ClassificationDamage>(this, android.R.layout.simple_dropdown_item_1line, SharedData.ClassificationDamages);
        itemClassificationDamage.setAdapter(adapter);
    }

    private void setOriginDamageAdapter() {
        ArrayAdapter<OriginDamage> adapter = new ArrayAdapter<OriginDamage>(this, android.R.layout.simple_dropdown_item_1line, SharedData.OriginDamages);
        itemOriginDamage.setAdapter(adapter);
    }

    private void sortDetails() {
        for (int i = details.size() - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (details.get(j).getTempID() > details.get(j + 1).getTempID()) {
                    Detail tmp = details.get(j);
                    details.set(j, details.get(j + 1));
                    details.set(j + 1, tmp);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SET_DETAIL && resultCode == RESULT_OK) {
            String detailID = data.getStringExtra("detailID");

            for (int i = 0; i < details.size(); i++) {
                Detail mDetail = details.get(i);
                if (mDetail.getDetailID().equals(detailID)) {
                    itemPart.setText(mDetail.toString());
                    mDamage.setDetail(mDetail);
                    break;
                }
            }
        }
    }
}
