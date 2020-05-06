package ua.org.algoritm.terminal.Activity;

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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ua.org.algoritm.terminal.Adapters.RecyclerAdapterTypesDamagePhoto;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.ActInspection;
import ua.org.algoritm.terminal.Objects.Damage;
import ua.org.algoritm.terminal.Objects.Detail;
import ua.org.algoritm.terminal.Objects.PhotoActInspection;
import ua.org.algoritm.terminal.Objects.TypeDamagePhoto;
import ua.org.algoritm.terminal.R;
import ua.org.algoritm.terminal.Service.IntentServiceDataBase;

public class DamageDefectActivity extends AppCompatActivity {
    public static final int REQUEST_TAKE_PHOTO_TypesPhoto = 2;
    public static final int REQUEST_UPDATE_PHOTO_TypesPhoto = 3;

    private RecyclerView listTypesPhoto;
    private RecyclerAdapterTypesDamagePhoto mAdapterTypesPhoto;

    private String mCurrentPhotoPath;
    private TypeDamagePhoto mTypesPhoto;

    private ActInspection actInspection;
    private Damage mDamage;
    private boolean newDamage = true;

    private AutoCompleteTextView itemDefect;
    private ImageView imageDefect;

    private AutoCompleteTextView comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_damage_defect);

        setTitle(getString(R.string.add_defect_damage));

        Intent intent = getIntent();
        actInspection = SharedData.getActInspection(intent.getStringExtra("actInspectionID"));
        String idDamage = intent.getStringExtra("idDamage");

        mDamage = new Damage();
        mDamage.setTypeDetail("defect");
        if (!idDamage.equals("")) {
            if (idDamage.equals("null")) {
                for (int i = 0; i < actInspection.getDamages().size(); i++) {
                    if (actInspection.getDamages().get(i).getDetail() == null) {
                        mDamage.copyDamage(actInspection.getDamages().get(i));
                        newDamage = false;
                        break;
                    }
                }
            } else {
                for (int i = 0; i < actInspection.getDamages().size(); i++) {
                    if (idDamage.equals(actInspection.getDamages().get(i).getDetail().getDetailID())) {
                        mDamage.copyDamage(actInspection.getDamages().get(i));
                        newDamage = false;
                        break;
                    }
                }
            }
        }

        if (newDamage) {
            actInspection.getDamages().add(mDamage);
        }

        itemDefect = findViewById(R.id.itemPart);
        itemDefect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDamage.setDetail((Detail) itemDefect.getAdapter().getItem(position));
            }
        });

        imageDefect = findViewById(R.id.imageDegreesDamage);
        imageDefect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemDefect.showDropDown();
            }
        });

        comment = findViewById(R.id.comment);

        setDetailAdapter();

        listTypesPhoto = findViewById(R.id.list_type_photo);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listTypesPhoto.setLayoutManager(layoutManager);

        updateListTypesPhoto();

        if (!newDamage) {
            itemDefect.setText(mDamage.getDetail() == null ? "" : mDamage.getDetail().toString());
            comment.setText(mDamage.getCommentDamage());
        }
    }

    private void setDetailAdapter() {
        ArrayAdapter<Detail> adapter = new ArrayAdapter<Detail>(this, android.R.layout.simple_dropdown_item_1line, SharedData.DamageDefect);
        itemDefect.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_damage, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (newDamage) {
            actInspection.getDamages().remove(mDamage);
        }
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

                mDamage.setCommentDamage(comment.getText().toString());

                if (newDamage) {
//                    actInspection.getDamages().add(mDamage);
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

                if (newDamage) {
                    actInspection.getDamages().remove(mDamage);
                }

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateListTypesPhoto() {
        if (mAdapterTypesPhoto == null) {
            mAdapterTypesPhoto = new RecyclerAdapterTypesDamagePhoto(this, R.layout.item_photo_act, mDamage.getTypeDamagePhoto());
            listTypesPhoto.setAdapter(mAdapterTypesPhoto);
            mAdapterTypesPhoto.setActionListener(new RecyclerAdapterTypesDamagePhoto.ActionListener() {
                @Override
                public void onClicViewPhotos(TypeDamagePhoto typesPhoto) {
                    if (mDamage.getDetail() == null) {
                        String message = "Укажите деталь!";

                        AlertDialog.Builder builder = new AlertDialog.Builder(DamageDefectActivity.this);
                        builder.setMessage(message)
                                .setCancelable(true)
                                .setPositiveButton(getString(R.string.butt_OK), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                        return;
                    }

                    Intent i = new Intent(getApplicationContext(), ListActPhotoDamage.class);
                    i.putExtra("actInspectionID", actInspection.getID());
                    i.putExtra("detailID", mDamage.getDetail().getDetailID());
                    i.putExtra("typesPhotoID", typesPhoto.getID());
                    startActivityForResult(i, REQUEST_UPDATE_PHOTO_TypesPhoto);
                }

                @Override
                public void onClicBtnAdd(TypeDamagePhoto typesPhoto) {
                    mTypesPhoto = typesPhoto;
                    dispatchTakePictureIntent(REQUEST_TAKE_PHOTO_TypesPhoto);
                }
            });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO_TypesPhoto && resultCode == RESULT_OK) {

            String fileName = new File(mCurrentPhotoPath).getName();

            PhotoActInspection photoActInspection = new PhotoActInspection();
            photoActInspection.setActID(actInspection.getID());
            photoActInspection.setName(fileName);
            photoActInspection.setObjectID(mDamage.getDetail().getDetailID() + "/" + mTypesPhoto.getID());
            photoActInspection.setListObject(mTypesPhoto.getListObject());
            photoActInspection.setCurrentPhotoPath(mCurrentPhotoPath);

            mTypesPhoto.getPhotoActInspections().add(photoActInspection);

            IntentServiceDataBase.startInsertPhotoActInspection(DamageDefectActivity.this,
                    actInspection.getID(), mTypesPhoto.getListObject(), mDamage.getDetail().getDetailID() + "/" + mTypesPhoto.getID(), mCurrentPhotoPath);

            decodeFile();

            mCurrentPhotoPath = "";
            mTypesPhoto = null;
            updateListTypesPhoto();

        } else if (requestCode == REQUEST_UPDATE_PHOTO_TypesPhoto) {
            updateListTypesPhoto();

        }
    }
}
