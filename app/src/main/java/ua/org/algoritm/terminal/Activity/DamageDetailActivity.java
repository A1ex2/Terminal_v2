package ua.org.algoritm.terminal.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

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
    private TabHost tabHost;
    private String mCurrentPhotoPath;
    private int tabHostSelect = 0;

    private ActInspection actInspection;
    private ArrayList<Scheme> mSchemes = new ArrayList<>();
    private ArrayList<Detail> details = new ArrayList<>();

    private WebView webViewGeneralForm;
    private WebView webViewDamageSalon;
    private WebView webViewDamageGaskets;
    private WebView webViewDamageOther;

    private String folderSVG;

    private AutoCompleteTextView itemPart;
    private ImageView imagePart;

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
        setContentView(R.layout.activity_damage_detail);

        Intent intent = getIntent();
        actInspection = SharedData.getActInspection(intent.getStringExtra("actInspectionID"));
        mSchemes = SharedData.getSchemes(actInspection);

        itemPart = findViewById(R.id.itemPart);

        imagePart = findViewById(R.id.imagePart);
        imagePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemPart.showDropDown();
            }
        });

        itemPart = findViewById(R.id.itemPart);

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

        folderSVG = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath() + "/svg";

        setTabHost();

        webViewGeneralForm = findViewById(R.id.webViewGeneralForm);
        webViewDamageSalon = findViewById(R.id.webViewDamageSalon);
        webViewDamageGaskets = findViewById(R.id.webViewDamageGaskets);
        webViewDamageOther = findViewById(R.id.webViewDamageOther);

        try {
            copyFolder("svg");

            for (int i = 0; i < mSchemes.size(); i++) {
                addDetail(mSchemes.get(i).getDetails());

                try {
                    String fileSVG = folderSVG + "/" + mSchemes.get(i).getName() + ".svg";
                    FileWriter writer = new FileWriter(fileSVG);
                    writer.write(mSchemes.get(i).getSVG());
                    writer.close();

                    if (mSchemes.get(i).getViewSchemesID().equals("ОбщийВид")) {
                        webViewGeneralForm.getSettings().setJavaScriptEnabled(true);
                        webViewGeneralForm.getSettings().setDefaultTextEncodingName("utf-8");
                        webViewGeneralForm.getSettings().setBuiltInZoomControls(true);
                        webViewGeneralForm.addJavascriptInterface(new JavaScriptInterfaceGeneralForm(this), "android");
                        webViewGeneralForm.loadUrl("file://" + fileSVG);

                    } else if (mSchemes.get(i).getViewSchemesID().equals("Салон")) {
                        webViewDamageSalon.getSettings().setJavaScriptEnabled(true);
                        webViewDamageSalon.getSettings().setDefaultTextEncodingName("utf-8");
                        webViewDamageSalon.getSettings().setBuiltInZoomControls(true);
                        webViewDamageSalon.addJavascriptInterface(new JavaScriptInterfaceDamageSalon(this), "android");
                        webViewDamageSalon.loadUrl("file://" + fileSVG);

                    } else if (mSchemes.get(i).getViewSchemesID().equals("Уплотнители")) {
                        webViewDamageGaskets.getSettings().setJavaScriptEnabled(true);
                        webViewDamageGaskets.getSettings().setDefaultTextEncodingName("utf-8");
                        webViewDamageGaskets.getSettings().setBuiltInZoomControls(true);
                        webViewDamageGaskets.addJavascriptInterface(new JavaScriptInterfaceDamageGaskets(this), "android");
                        webViewDamageGaskets.loadUrl("file://" + fileSVG);

                    } else if (mSchemes.get(i).getViewSchemesID().equals("Другое")) {
                        webViewDamageOther.getSettings().setJavaScriptEnabled(true);
                        webViewDamageOther.getSettings().setDefaultTextEncodingName("utf-8");
                        webViewDamageOther.getSettings().setBuiltInZoomControls(true);
                        webViewDamageOther.addJavascriptInterface(new JavaScriptInterfaceDamageOther(this), "android");
                        webViewDamageOther.loadUrl("file://" + fileSVG);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private void copyFolder(String name) throws IOException {
        AssetManager assetManager = getAssets();
        String[] files = null;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            try {
                files = assetManager.list(name);
            } catch (IOException e) {
                Log.i("===", "Failed to get asset file list.\n" + e);
            }
            for (String filename : files) {
                InputStream in = null;
                OutputStream out = null;
                File folder = new File(folderSVG);
                boolean success = true;
                if (!folder.exists()) {
                    success = folder.mkdir();
                }
                if (success) {
                    try {
                        in = assetManager.open(name + "/" + filename);
                        out = new FileOutputStream(folderSVG + "/" + filename);
                        copyFile(in, out);
                    } catch (IOException e) {
                        Log.i("===", "Failed to copy asset file: " + filename + " " + e);
                    } finally {
                        in.close();
                        out.close();
                    }
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
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
        tabSpec.setContent(R.id.linearLayoutGeneralForm);
        tabSpec.setIndicator(getString(R.string.text_general_form));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setContent(R.id.linearLayoutSalon);
        tabSpec.setIndicator(getString(R.string.text_salon));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setContent(R.id.linearLayoutGaskets);
        tabSpec.setIndicator(getString(R.string.text_gaskets));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag5");
        tabSpec.setContent(R.id.linearLayoutOther);
        tabSpec.setIndicator(getString(R.string.text_other));
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);
    }

    public void setDetail(String viewSchemesID, String ID) {
        for (int i = 0; i < mSchemes.size(); i++) {
            if (mSchemes.get(i).getViewSchemesID().equals(viewSchemesID)) {

                for (int j = 0; j < mSchemes.get(i).getDetails().size(); j++) {
                    Detail detail = mSchemes.get(i).getDetails().get(j);
                    if (detail.getID().equals(ID)) {


                        Toast.makeText(getApplicationContext(), "" + detail.getTempID() + " - " + detail.getDetailName(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        }
    }

    public class JavaScriptInterfaceGeneralForm {
        Context mContext;

        JavaScriptInterfaceGeneralForm(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void clickAndroid(String[] toast) {
            setDetail("ОбщийВид", toast[0]);
//
//            if (toast[0].equals("p311")) {
//                Toast.makeText(mContext, "руль", Toast.LENGTH_SHORT).show();
//
//            } else {
//                Toast.makeText(mContext, toast[0], Toast.LENGTH_SHORT).show();
//            }
        }

        @JavascriptInterface
        public void mouseOutAndroid() {
            //Toast.makeText(mContext, "mouseOutAndroid", Toast.LENGTH_SHORT).show();
        }
    }

    public class JavaScriptInterfaceDamageSalon {
        Context mContext;

        JavaScriptInterfaceDamageSalon(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void clickAndroid(String[] toast) {
            setDetail("Салон", toast[0]);
//            if (toast[0].equals("p311")) {
//                Toast.makeText(mContext, "руль", Toast.LENGTH_SHORT).show();
//
//            } else {
//                Toast.makeText(mContext, toast[0], Toast.LENGTH_SHORT).show();
//            }
        }

        @JavascriptInterface
        public void mouseOutAndroid() {
            //Toast.makeText(mContext, "mouseOutAndroid", Toast.LENGTH_SHORT).show();
        }
    }

    public class JavaScriptInterfaceDamageGaskets {
        Context mContext;

        JavaScriptInterfaceDamageGaskets(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void clickAndroid(String[] toast) {
            setDetail("Уплотнители", toast[0]);

//            if (toast[0].equals("p311")) {
//                Toast.makeText(mContext, "руль", Toast.LENGTH_SHORT).show();
//
//            } else {
//                Toast.makeText(mContext, toast[0], Toast.LENGTH_SHORT).show();
//            }
        }

        @JavascriptInterface
        public void mouseOutAndroid() {
            //Toast.makeText(mContext, "mouseOutAndroid", Toast.LENGTH_SHORT).show();
        }
    }

    public class JavaScriptInterfaceDamageOther {
        Context mContext;

        JavaScriptInterfaceDamageOther(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void clickAndroid(String[] toast) {
            setDetail("Другое", toast[0]);
//            if (toast[0].equals("p311")) {
//                Toast.makeText(mContext, "руль", Toast.LENGTH_SHORT).show();
//
//            } else {
//                Toast.makeText(mContext, toast[0], Toast.LENGTH_SHORT).show();
//            }
        }

        @JavascriptInterface
        public void mouseOutAndroid() {
            //Toast.makeText(mContext, "mouseOutAndroid", Toast.LENGTH_SHORT).show();
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


}
