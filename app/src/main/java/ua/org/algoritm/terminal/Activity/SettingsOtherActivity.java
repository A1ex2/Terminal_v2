package ua.org.algoritm.terminal.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ua.org.algoritm.terminal.R;
import ua.org.algoritm.terminal.Service.MyManager;

public class SettingsOtherActivity extends AppCompatActivity {
    private Button button_send_DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_other);

        button_send_DB = findViewById(R.id.button_send_DB);

        button_send_DB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(MyManager.class).build();
                WorkManager.getInstance().enqueue(request);

                finish();
            }
        });
    }
}