package ua.org.algoritm.terminal.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.ActInspection;
import ua.org.algoritm.terminal.R;

public class ActInspectionActivity extends AppCompatActivity {
    private ActInspection mActInspection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_inspection);

        Intent intent = getIntent();
        mActInspection = SharedData.getActInspection(intent.getStringExtra("actInspection"));

//        ActionBar mbar = getSupportActionBar();
//        mbar.setTitle(mActInspection.getDescription());
//        mbar.setDisplayHomeAsUpEnabled(true);

        setTitle(mActInspection.getDescription());

        TabHost tabHost = findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");

        tabSpec.setContent(R.id.linearLayoutInfo);
        tabSpec.setIndicator("1");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.linearLayout2);
        tabSpec.setIndicator("2");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setContent(R.id.linearLayout3);
        tabSpec.setIndicator("2");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setContent(R.id.linearLayout4);
        tabSpec.setIndicator("4");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag5");
        tabSpec.setContent(R.id.linearLayout5);
        tabSpec.setIndicator("5");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag6");
        tabSpec.setContent(R.id.linearLayout6);
        tabSpec.setIndicator("6");
        tabHost.addTab(tabSpec);


        tabHost.setCurrentTab(0);

    }
}
