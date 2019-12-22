package ua.org.algoritm.terminal.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.R;

public class ApiSettings extends AppCompatActivity {
    private TextView api;
    private Button ok;
    private Button cancel;
    private SharedPreferences preferences;

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
    }
}
