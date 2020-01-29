package ua.org.algoritm.terminal;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import ua.org.algoritm.terminal.Activity.Password;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, Password.class);
        startActivity(intent);
        finish();
    }
}