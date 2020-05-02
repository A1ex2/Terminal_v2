package ua.org.algoritm.terminal.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import ua.org.algoritm.terminal.Adapters.RecyclerAdapterPhoto;
import ua.org.algoritm.terminal.Adapters.RecyclerAdapterPhotoActInspection;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.Photo;
import ua.org.algoritm.terminal.Objects.PhotoActInspection;
import ua.org.algoritm.terminal.Objects.TypesPhoto;
import ua.org.algoritm.terminal.R;
import ua.org.algoritm.terminal.Service.IntentServiceDataBase;

public class ListActPhoto extends AppCompatActivity {
    private TypesPhoto mTypesPhoto;

    private RecyclerView recyclerViewPhoto;
    private RecyclerAdapterPhotoActInspection adapterPhoto;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_act_photo);

        Intent intent = getIntent();

        String actInspectionID = intent.getStringExtra("actInspectionID");
        String typesPhotoID = intent.getStringExtra("typesPhotoID");

        mTypesPhoto = SharedData.getTypesPhoto(actInspectionID, typesPhotoID);

        setTitle(mTypesPhoto.getTypePhoto());

        recyclerViewPhoto = findViewById(R.id.list_type_photo);
        LinearLayoutManager layoutManagerPhoto = new LinearLayoutManager(this);
        recyclerViewPhoto.setLayoutManager(layoutManagerPhoto);

        updateListsPhoto();

        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int fromPos = viewHolder.getAdapterPosition();

                mDialog = new ProgressDialog(ListActPhoto.this);
                mDialog.setMessage(getString(R.string.wait));
                mDialog.setCancelable(false);
                mDialog.show();

                PhotoActInspection mPhoto = mTypesPhoto.getPhotoActInspections().get(fromPos);

                mTypesPhoto.getPhotoActInspections().remove(mPhoto);
                IntentServiceDataBase.startDeletePhotoActInspection(ListActPhoto.this, mPhoto.getCurrentPhotoPath());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewPhoto);
    }

    private void updateListsPhoto() {
        if (adapterPhoto == null) {
            adapterPhoto = new RecyclerAdapterPhotoActInspection(this, R.layout.item_photo, mTypesPhoto.getPhotoActInspections());
            recyclerViewPhoto.setAdapter(adapterPhoto);
            adapterPhoto.setActionListener(new RecyclerAdapterPhotoActInspection.ActionListener() {
                @Override
                public void onClick(PhotoActInspection photo) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentServiceDataBase.REQUEST_CODE_DELETE_PHOTO_ACT_INSPECTION) {

            adapterPhoto.setPhotoActInspection(mTypesPhoto.getPhotoActInspections());
            updateListsPhoto();

            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }
}
