package ua.org.algoritm.terminal.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Dispatcher;
import ua.org.algoritm.terminal.ConnectTo1c.UIManager;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.MainActivity;
import ua.org.algoritm.terminal.R;
import ua.org.algoritm.terminal.receiver.MyWorkerTimeWork;
import ua.org.algoritm.terminal.receiver.QueryPreferences;

public class Password extends AppCompatActivity {
    private EditText login;
    private EditText password;
    private Button ok;
    private ImageButton imBiometric;

    private BiometricManager mBiometricManager;

    private ArrayList<String> loginList = new ArrayList<>();
    private SharedPreferences preferences;

    private ProgressDialog mDialog;

    private static final int REQUEST_CODE_EDIT_API = 2;

    public static String mLogin;
    public static String mPassword;

    public static final int ACTION_VERIFY = 10;
    public static final int ACTION_LOGIN_LIST = 11;
    public static final int ACTION_UPDATE = 14;
    public static final int ACTION_UPDATE_NEW_VERSION = 15;

    public static final int ACTION_ConnectionError = 0;
    public static UIManager uiManager;
    public static SoapFault responseFault;

    public static SoapObject soapParam_Response;
    public static SoapObject soapParam_Response_Update;
    public static Handler soapHandler;
    public static String wsParam_PassHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_password);

        preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedData.API = preferences.getString("Api", "");

        if (SharedData.API.equals("")) {
            SharedData.API = "http://terminal.blg-vidi.com:83/blg_log";
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Api", SharedData.API);
            editor.apply();
        }

        SharedData.VERSION = getString(R.string.nav_header_version);
        SharedData.thisDriver = preferences.getBoolean("thisDriver", false);
        SharedData.isActInspection = preferences.getBoolean("isActInspection", false);
        SharedData.isActInspectionForIssuance = preferences.getBoolean("isActInspectionForIssuance", false);
        SharedData.isOfflineReception = preferences.getBoolean("isOfflineReception", false);
        SharedData.absolutePathFTP = preferences.getString("absolutePathFTP", "foto");

        if (SharedData.thisDriver & SharedData.isOfflineReception & !SharedData.isOnline(getApplicationContext())) {
        } else {
            SOAP_Dispatcher dispatcherUpdate = new SOAP_Dispatcher(ACTION_UPDATE, getApplicationContext());
            dispatcherUpdate.start();
        }

        uiManager = new UIManager(this);
        soapHandler = new incomingHandler(this);

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    verify();
                    return true;
                }
                return false;

            }
        });

        ok = findViewById(R.id.butt_OK);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });

        imBiometric = findViewById(R.id.imBiometric);
        imBiometric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (mBiometricManager.canAuthenticate()) {
                    case BiometricManager.BIOMETRIC_SUCCESS:
                        Executor executor = ContextCompat.getMainExecutor(getApplicationContext());

                        BiometricPrompt biometricPrompt = new BiometricPrompt(Password.this, executor, new BiometricPrompt.AuthenticationCallback() {
                            @Override
                            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                                super.onAuthenticationError(errorCode, errString);
                            }

                            @Override
                            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                                super.onAuthenticationSucceeded(result);

                                password.setText(preferences.getString("Password", ""));
                                verify();
                            }

                            @Override
                            public void onAuthenticationFailed() {
                                super.onAuthenticationFailed();
                            }
                        });

                        final BiometricPrompt.PromptInfo mPromptInfo = new BiometricPrompt.PromptInfo.Builder()
                                .setTitle("Подтвердите свою личность")
                                .setDescription("Используйте отпечатки пальцев для проверки личности.")
                                .setNegativeButtonText(getString(R.string.cancel))
                                .build();

                        biometricPrompt.authenticate(mPromptInfo);

                        break;

                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                        Toast.makeText(getApplicationContext(), "биометрический датчик в настоящее время недоступен", Toast.LENGTH_LONG).show();
                        break;

                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                        Toast.makeText(getApplicationContext(), "на вашем устройстве нет сохраненных отпечатков пальцев, пожалуйста, проверьте настройки безопасности", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });


        mBiometricManager = BiometricManager.from(this);
        switch (mBiometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                imBiometric.setVisibility(View.INVISIBLE);
                break;
        }

        if (preferences.getString("Login", "").equals("") & preferences.getString("Password", "").equals("")) {
            imBiometric.setVisibility(View.INVISIBLE);
        }

        login.setText(preferences.getString("Login", ""));

        if (SharedData.thisDriver & SharedData.isOfflineReception & !SharedData.isOnline(getApplicationContext())) {
        } else {
            SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(ACTION_LOGIN_LIST, getApplicationContext());
            dispatcher.start();
        }

        if (hasPermission(Manifest.permission.GET_ACCOUNTS)) {
        } else {
            requestPermissions();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(Password.this, ApiSettings.class);
                startActivityForResult(intent, REQUEST_CODE_EDIT_API);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_API) {
            if (resultCode == Activity.RESULT_OK) {
                SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(ACTION_LOGIN_LIST, getApplicationContext());
                dispatcher.start();
            }
        }
    }

    private boolean validateLogin(String mLogin) {
        if (mLogin.isEmpty()) {
            login.setError(getString(R.string.error_login));
            return false;
        } else {
            login.setError(null);
            return true;
        }
    }

    private boolean validatePassword(String mPassword) {
        if (mPassword.isEmpty()) {
            password.setError(getString(R.string.error_password));
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    private void verify() {
        mLogin = login.getText().toString();
        mPassword = password.getText().toString();

        if (!validateLogin(mLogin) | !validatePassword(mPassword)) {
            return;
        }

        login.setEnabled(false);
        password.setEnabled(false);
        ok.setEnabled(false);

        SharedData.thisDriver = preferences.getBoolean("thisDriver", false);
        SharedData.isOfflineReception = preferences.getBoolean("isOfflineReception", false);

        if (SharedData.thisDriver & SharedData.isOfflineReception & !SharedData.isOnline(getApplicationContext())) {
            login.setEnabled(true);
            password.setEnabled(true);
            ok.setEnabled(true);

            Boolean isLoginSuccess = false;

            if (preferences.getString("Login", "").equals(mLogin)
                    & preferences.getString("Password", "").equals(mPassword)) {
                isLoginSuccess = true;
            }

            if (isLoginSuccess) {
                SharedData.LOGIN = preferences.getString("Login", "");
                SharedData.PASSWORD = preferences.getString("Password", "");

                SharedData.hostFTP = preferences.getString("hostFTP", "");
                SharedData.thisSFTP = preferences.getBoolean("thisSFTP", true);
                SharedData.portFTP = preferences.getInt("portFTP", 21);
                SharedData.usernameFTP = preferences.getString("usernameFTP", "");
                SharedData.passwordFTP = preferences.getString("passwordFTP", "");

                SharedData.thisDriver = preferences.getBoolean("thisDriver", false);
                SharedData.isActInspection = preferences.getBoolean("isActInspection", false);
                SharedData.isActInspectionForIssuance = preferences.getBoolean("isActInspectionForIssuance", false);
                SharedData.isOfflineReception = preferences.getBoolean("isOfflineReception", false);
                SharedData.absolutePathFTP = preferences.getString("absolutePathFTP", "foto");

                if (SharedData.thisDriver) {
                    startPeriodicTask();
                }

                uiManager.showToast(getString(R.string.passwordIncorrect) + SharedData.LOGIN);

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                uiManager.showToast(getString(R.string.passwordNotIncorrect));
            }

        } else {
            SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(ACTION_VERIFY, getApplicationContext());
            dispatcher.start();
        }
    }

    private void startPeriodicTask() {
        if (true){
            return;
        }

        String id = QueryPreferences.getIdWorkRequest(getApplicationContext());
        if (!id.equals("")){
            return;
        }

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(MyWorkerTimeWork.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance().enqueue(periodicWorkRequest);
        WorkManager.getInstance()
                .getWorkInfoByIdLiveData(periodicWorkRequest.getId());

        QueryPreferences.setIdWorkRequest(getApplicationContext(), String.valueOf(periodicWorkRequest.getId()));
    }

    private boolean hasPermission(String permission) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    private void requestPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = {
//                    Manifest.permission.GET_ACCOUNTS,
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            };
            requestPermissions(permissions, 0);
        }
    }

    class incomingHandler extends Handler {
        private final WeakReference<Password> mTarget;

        public incomingHandler(Password context) {
            mTarget = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            login.setEnabled(true);
            password.setEnabled(true);
            ok.setEnabled(true);

            Password target = mTarget.get();

            switch (msg.what) {
                case ACTION_ConnectionError:
                    uiManager.showToast(getString(R.string.errorConnection) + getSoapErrorMessage());
                    break;
                case ACTION_VERIFY: {
                    target.checkLoginResult();
                }
                break;

                case ACTION_LOGIN_LIST: {
                    target.checkLoginListResult();
                }
                break;

                case ACTION_UPDATE: {
                    checkUpdate();
                }
                break;

                case ACTION_UPDATE_NEW_VERSION: {
                    update();
                }
                break;
            }
        }

    }

    private String getSoapErrorMessage() {

        String errorMessage;

        if (responseFault == null)
            errorMessage = getString(R.string.textNoInternet);
        else {
            try {
                errorMessage = responseFault.faultstring;
            } catch (Exception e) {
                e.printStackTrace();
                errorMessage = getString(R.string.unknownError);
            }
        }
        return errorMessage;
    }

    public void checkLoginResult() {

        Boolean isLoginSuccess = Boolean.parseBoolean(soapParam_Response.getPropertyAsString("Result"));

        if (isLoginSuccess) {
            String hostFTP = "";
            int portFTP = 21;
            String usernameFTP = "";
            String passwordFTP = "";
            boolean thisSFTP = false;
            boolean thisDriver = false;
            boolean isActInspection = false;
            boolean isActInspectionForIssuance = false;
            boolean isOfflineReception = false;
            String absolutePathFTP = "foto";

            try {
                hostFTP = soapParam_Response.getPropertyAsString("host");
                thisSFTP = Integer.parseInt(soapParam_Response.getPropertyAsString("thisSFTP")) == 1;
                portFTP = Integer.parseInt(soapParam_Response.getPropertyAsString("port"));
                usernameFTP = soapParam_Response.getPropertyAsString("username");
                passwordFTP = soapParam_Response.getPropertyAsString("password");

                thisDriver = Integer.parseInt(soapParam_Response.getPropertyAsString("thisDriver")) == 1;
                absolutePathFTP = soapParam_Response.getPropertyAsString("AbsolutePathFTP");

                isActInspection = Integer.parseInt(soapParam_Response.getPropertyAsString("isActInspection")) == 1;
                isActInspectionForIssuance = Integer.parseInt(soapParam_Response.getPropertyAsString("isActInspectionForIssuance")) == 1;
                isOfflineReception = Integer.parseInt(soapParam_Response.getPropertyAsString("isOfflineReception")) == 1;
            } catch (Exception e) {
                e.printStackTrace();
            }

            SharedData.LOGIN = mLogin;
            SharedData.PASSWORD = mPassword;

            SharedData.hostFTP = hostFTP;
            SharedData.thisSFTP = thisSFTP;
            SharedData.portFTP = portFTP;
            SharedData.usernameFTP = usernameFTP;
            SharedData.passwordFTP = passwordFTP;

            SharedData.thisDriver = thisDriver;
            SharedData.isActInspection = isActInspection;
            SharedData.isActInspectionForIssuance = isActInspectionForIssuance;
            SharedData.isOfflineReception = isOfflineReception;
            SharedData.absolutePathFTP = absolutePathFTP;

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Login", mLogin);
            editor.putString("Password", mPassword);

            editor.putString("hostFTP", hostFTP);
            editor.putBoolean("thisSFTP", thisSFTP);
            editor.putInt("portFTP", portFTP);
            editor.putString("usernameFTP", usernameFTP);
            editor.putString("passwordFTP", passwordFTP);

            editor.putBoolean("thisDriver", thisDriver);
            editor.putBoolean("isActInspection", isActInspection);
            editor.putBoolean("isActInspectionForIssuance", isActInspectionForIssuance);
            editor.putBoolean("isOfflineReception", isOfflineReception);
            editor.putString("absolutePathFTP", absolutePathFTP);

            editor.apply();

            if (SharedData.thisDriver) {
                startPeriodicTask();
            }

            uiManager.showToast(getString(R.string.passwordIncorrect) + soapParam_Response.getPropertyAsString("Name"));

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        } else {
            uiManager.showToast(getString(R.string.passwordNotIncorrect));
        }

    }

    public void checkLoginListResult() {
        try {

            loginList.clear();

            for (int i = 0; i < SharedData.USERS.size(); i++) {
                loginList.add(SharedData.USERS.get(i).getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice, loginList);
            AutoCompleteTextView acTextView = (AutoCompleteTextView) login;
            acTextView.setThreshold(1);
            acTextView.setAdapter(adapter);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public void checkUpdate() {
        try {

            Boolean isLoginSuccess = Boolean.parseBoolean(soapParam_Response_Update.getPropertyAsString("Result"));

            if (isLoginSuccess) {
                String lastAppVersion = soapParam_Response_Update.getPropertyAsString("Description");
                final String apkUrl = soapParam_Response_Update.getPropertyAsString("URL");

                String message = new Formatter().format(getString(R.string.update_new_version), lastAppVersion).toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(Password.this);
                builder.setMessage(message)
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.butt_Yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                try {

                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(apkUrl));
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

//                            SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(ACTION_UPDATE_NEW_VERSION);
//                            dispatcher.start();
//
//                            dialog.dismiss();
//
//                            mDialog = new ProgressDialog(Password.this);
//                            mDialog.setMessage(getString(R.string.wait_update));
//                            mDialog.setCancelable(false);
//                            mDialog.show();

                            }
                        })
                        .setNegativeButton(getString(R.string.butt_Not), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private void update() {
        try {
//            if (mDialog != null && mDialog.isShowing()) {
//                mDialog.dismiss();
//            }
//
//            String app = soapParam_Response_Update.getPropertyAsString("App");


        } catch (Exception e) {

            e.printStackTrace();

        }
    }

}