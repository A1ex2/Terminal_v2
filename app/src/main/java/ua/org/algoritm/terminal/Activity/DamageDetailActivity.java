package ua.org.algoritm.terminal.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Date;

import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.ActInspection;
import ua.org.algoritm.terminal.Objects.ClassificationDamage;
import ua.org.algoritm.terminal.Objects.DegreesDamage;
import ua.org.algoritm.terminal.Objects.Detail;
import ua.org.algoritm.terminal.Objects.OriginDamage;
import ua.org.algoritm.terminal.Objects.Scheme;
import ua.org.algoritm.terminal.Objects.TypeDamage;
import ua.org.algoritm.terminal.R;

public class DamageDetailActivity extends AppCompatActivity {
    public static final int REQUEST_SET_DETAIL = 1;

    private String mCurrentPhotoPath;

    private ActInspection actInspection;
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
        mSchemes = SharedData.getSchemes(actInspection);

        for (int i = 0; i < mSchemes.size(); i++) {
            addDetail(mSchemes.get(i).getDetails());
        }

        itemPart = findViewById(R.id.itemPart);

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
        imageTypesDamage = findViewById(R.id.imageTypesDamage);
        imageTypesDamage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemTypesDamage.showDropDown();
            }
        });

        itemDegreesDamage = findViewById(R.id.itemDegreesDamage);
        imageDegreesDamage = findViewById(R.id.imageDegreesDamage);
        imageDegreesDamage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemDegreesDamage.showDropDown();
            }
        });

        detailDamage = findViewById(R.id.detailDamage);

        itemClassificationDamage = findViewById(R.id.itemClassificationDamage);
        imageClassificationDamage = findViewById(R.id.imageClassificationDamage);
        imageClassificationDamage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClassificationDamage.showDropDown();
            }
        });

        itemOriginDamage = findViewById(R.id.itemOriginDamage);
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
                if (mDetail.getDetailID().equals(detailID)){
                    itemPart.setText(mDetail.toString());
                    break;
                }
            }
        }
    }
}
