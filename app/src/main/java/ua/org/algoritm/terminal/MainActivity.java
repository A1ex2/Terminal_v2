package ua.org.algoritm.terminal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.DialogFragmentNavigator;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.lang.ref.WeakReference;

import ua.org.algoritm.terminal.Activity.ApiSettings;
import ua.org.algoritm.terminal.Activity.CarActivityMoving;
import ua.org.algoritm.terminal.Activity.CarDataList;
import ua.org.algoritm.terminal.Activity.Password;
import ua.org.algoritm.terminal.Activity.SettingsOtherActivity;
import ua.org.algoritm.terminal.ConnectTo1c.NetworkChangeReceiver;
import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Dispatcher;
import ua.org.algoritm.terminal.ConnectTo1c.UIManager;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.ui.acceptance.AcceptanceFragment;
import ua.org.algoritm.terminal.ui.issuance.IssuanceFragment;
import ua.org.algoritm.terminal.ui.order.OrderOutfitFragment;

public class MainActivity extends AppCompatActivity {

    private long backPressedTime;
    private BroadcastReceiver mNetworkReceiver;

    public static final int ACTION_SECTORS_LIST = 13;
    //    public static final int ACTION_RECEPTION_LIST = 12;
    //    public static final int REQUEST_CODE_UPDATE_RECEPTION = 15;
    public static final int ACTION_ConnectionError = 0;

    public static UIManager uiManager;
    public static SoapFault responseFault;

    public static SoapObject soapParam_Response;
    public static Handler soapHandler;

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private static NavController navController;
    private static NavigationView navigationView;
    private static Boolean start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SharedData.thisDriver) {
            setContentView(R.layout.activity_main_driver);
        } else {
            setContentView(R.layout.activity_main);
        }

        start = true;
        mNetworkReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastForNougat();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        if (SharedData.thisDriver) {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_act_inspection)
                    .setDrawerLayout(drawer)
                    .build();
        } else {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_acceptance, R.id.nav_moving, R.id.nav_issue, R.id.nav_order_outfit)
                    .setDrawerLayout(drawer)
                    .build();
        }
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, drawer);

        SharedData.app = this;
        uiManager = new UIManager(this);
        soapHandler = new incomingHandler(this);

//        SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
//        login = preferences.getString("Login", "");
//        password = preferences.getString("Password", "");

        getUpdateSectorsList(getApplicationContext());
        getUpdateReceptionList();

    }

    public static void dialog(boolean value, Context context) {

        if (start) {
            start = false;
            return;
        }

        try {
            int acceptance = R.id.nav_acceptance; //приемка
            int issuance = R.id.nav_issue; // выдача
            int moving = R.id.nav_moving; // перемещение
            int orderOutfit = R.id.nav_order_outfit; // перемещение
            int actInspection = R.id.nav_act_inspection; // акт осмотра

            if (value) {

                getUpdateSectorsList(context);

                if (navController.getCurrentDestination().getId() == acceptance) {
                    AcceptanceFragment.soapHandler.sendEmptyMessage(AcceptanceFragment.ACTION_Connection);

                } else if (navController.getCurrentDestination().getId() == issuance) {
                    IssuanceFragment.soapHandler.sendEmptyMessage(IssuanceFragment.ACTION_Connection);

                } else if (navController.getCurrentDestination().getId() == moving) {
                    CarDataList.soapHandler.sendEmptyMessage(CarDataList.ACTION_Connection);

                } else if (navController.getCurrentDestination().getId() == orderOutfit) {
                    OrderOutfitFragment.soapHandler.sendEmptyMessage(OrderOutfitFragment.ACTION_Connection);

                } else if (navController.getCurrentDestination().getId() == actInspection) {
                    CarDataList.soapHandler.sendEmptyMessage(CarDataList.ACTION_Connection);

                }
            } else {
                if (navController.getCurrentDestination().getId() == acceptance) {
                    AcceptanceFragment.soapHandler.sendEmptyMessage(AcceptanceFragment.ACTION_Connection_Lost);

                } else if (navController.getCurrentDestination().getId() == issuance) {
                    IssuanceFragment.soapHandler.sendEmptyMessage(IssuanceFragment.ACTION_Connection_Lost);

                } else if (navController.getCurrentDestination().getId() == moving) {
                    CarDataList.soapHandler.sendEmptyMessage(CarDataList.ACTION_Connection_Lost);

                } else if (navController.getCurrentDestination().getId() == orderOutfit) {
                    OrderOutfitFragment.soapHandler.sendEmptyMessage(OrderOutfitFragment.ACTION_Connection_Lost);

                } else if (navController.getCurrentDestination().getId() == actInspection) {
                    CarDataList.soapHandler.sendEmptyMessage(CarDataList.ACTION_Connection_Lost);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }

    private void getUpdateReceptionList() {
//        SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(ACTION_RECEPTION_LIST);
//        dispatcher.start();
    }

    private static void getUpdateSectorsList(Context context) {
        if (SharedData.thisDriver & SharedData.isOfflineReception & !SharedData.isOnline(context)) {
        } else {
            SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(ACTION_SECTORS_LIST, context);
            dispatcher.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem action_settings = menu.findItem(R.id.action_settings);
        action_settings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, SettingsOtherActivity.class);
                startActivity(intent);

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();
            return;
        } else {
            Snackbar.make(drawer, getText(R.string.exit), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    class incomingHandler extends Handler {
        private final WeakReference<MainActivity> mTarget;

        public incomingHandler(MainActivity context) {
            mTarget = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity target = mTarget.get();

            switch (msg.what) {

                case ACTION_ConnectionError:
                    uiManager.showToast(getString(R.string.errorConnection) + getSoapErrorMessage());
                    break;
//                case ACTION_RECEPTION_LIST:
//                    target.checkReceptionListResult();
//                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    public void checkReceptionListResult() {
        try {


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
