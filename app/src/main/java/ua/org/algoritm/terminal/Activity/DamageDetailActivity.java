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
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
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
import ua.org.algoritm.terminal.Objects.Detail;
import ua.org.algoritm.terminal.Objects.Scheme;
import ua.org.algoritm.terminal.R;

public class DamageDetailActivity extends AppCompatActivity {
    private TabHost tabHost;
    private String mCurrentPhotoPath;
    private int tabHostSelect = 0;

    private ActInspection actInspection;
    private ArrayList<Scheme> mSchemes = new ArrayList<>();

    private WebView webViewGeneralForm;
    private WebView webViewDamageSalon;
    private WebView webViewDamageGaskets;
    private WebView webViewDamageOther;

    private String folderSVG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_damage_detail);

        Intent intent = getIntent();
        actInspection = SharedData.getActInspection(intent.getStringExtra("actInspectionID"));
        mSchemes = SharedData.getSchemes(actInspection);

        folderSVG = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath() + "/svg";

        setTabHost();

        webViewGeneralForm = findViewById(R.id.webViewGeneralForm);
        webViewDamageSalon = findViewById(R.id.webViewDamageSalon);
        webViewDamageGaskets = findViewById(R.id.webViewDamageGaskets);
        webViewDamageOther = findViewById(R.id.webViewDamageOther);

        try {
            copyFolder("svg");

            for (int i = 0; i < mSchemes.size(); i++) {
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

    public void setDeDetail(String viewSchemesID, String ID) {
        for (int i = 0; i < mSchemes.size(); i++) {
            if (mSchemes.get(i).getViewSchemesID().equals(viewSchemesID)) {

                for (int j = 0; j < mSchemes.get(i).getDetails().size(); j++) {
                    Detail detail = mSchemes.get(i).getDetails().get(j);
                    if (detail.getDetailID().equals(ID)){
                        Toast.makeText(getApplicationContext(), detail.getID() + " - " + detail.getDetailName(), Toast.LENGTH_SHORT).show();
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
            setDeDetail("ОбщийВид", toast[0]);
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
            setDeDetail("Салон", toast[0]);
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
            setDeDetail("Уплотнители", toast[0]);

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
            setDeDetail("Другое", toast[0]);
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
