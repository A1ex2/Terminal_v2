package ua.org.algoritm.terminal.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.MainActivity;
import ua.org.algoritm.terminal.R;
import ua.org.algoritm.terminal.receiver.MyWorker;
import ua.org.algoritm.terminal.receiver.MyWorkerTimeWork;
import ua.org.algoritm.terminal.receiver.QueryPreferences;

public class ApiSettings extends AppCompatActivity {
    private TextView api;
    private Button ok;
    private Button cancel;
    private SharedPreferences preferences;

    private Switch backgroundActs;
    private String idWorkRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_settings);

        api = findViewById(R.id.editServer);
        ok = findViewById(R.id.buttonOk);
        cancel = findViewById(R.id.buttonCancel);

        preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        api.setText(preferences.getString("Api", ""));

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedData.API = api.getText().toString();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Api", api.getText().toString());
                editor.apply();

                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        backgroundActs = findViewById(R.id.backgroundActs);
        idWorkRequest = QueryPreferences.getIdWorkRequest(getApplicationContext());
        backgroundActs.setChecked(!idWorkRequest.equals(""));
        backgroundActs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backgroundActs.isChecked()) {
                    MyWorkerTimeWork.startPeriodicTask(getApplicationContext());
                } else {
                    MyWorkerTimeWork.cancelPeriodicTask(getApplicationContext());
                }
            }
        });

        if (SharedData.thisDriver){
            backgroundActs.setVisibility(View.VISIBLE);
        } else {
            backgroundActs.setVisibility(View.INVISIBLE);
        }
    }
}
