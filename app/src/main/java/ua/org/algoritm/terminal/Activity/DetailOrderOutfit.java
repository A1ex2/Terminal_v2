package ua.org.algoritm.terminal.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import ua.org.algoritm.terminal.Adapters.RecyclerAdapterCarDataOrderOutfit;
import ua.org.algoritm.terminal.ConnectTo1c.FtpUtil;
import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Dispatcher;
import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Objects;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.CarDataOutfit;
import ua.org.algoritm.terminal.Objects.OrderOutfit;
import ua.org.algoritm.terminal.Objects.Photo;
import ua.org.algoritm.terminal.R;
import ua.org.algoritm.terminal.Service.IntentServiceDataBase;

public class DetailOrderOutfit extends AppCompatActivity {
    private TextView description;
    private TextView state;
    private TextView responsible;
    private RecyclerView recyclerView;
    private RecyclerAdapterCarDataOrderOutfit adapter;
    private OrderOutfit orderOutfit;
    private static final int SAVE_FTP = 101;
    private ProgressDialog mDialog;
    private SaveTaskPhotoFTP mTaskPhotoFTP;

    private static final int REQUEST_CODE_SCAN = 0x0000c0de;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order_outfit);

        String id = getIntent().getStringExtra("OrderOutfit");
        orderOutfit = SharedData.getOrderOutfit(id);

        description = findViewById(R.id.description);
        description.setText(orderOutfit.getDescription());

        state = findViewById(R.id.itemState);
        state.setText(orderOutfit.getState());

        responsible = findViewById(R.id.itemResponsible);
        responsible.setText(orderOutfit.getResponsible());

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        updateLists();
    }

    private void updateLists() {
        if (adapter == null) {
            adapter = new RecyclerAdapterCarDataOrderOutfit(this, R.layout.item_car_order_outfit_detail, orderOutfit.getCarDataOutfit());
            recyclerView.setAdapter(adapter);
            adapter.setActionListener(new RecyclerAdapterCarDataOrderOutfit.ActionListener() {
                @Override
                public void onClick(CarDataOutfit carData) {
                    viewCarData(carData);
                }
            });
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void viewCarData(CarDataOutfit carData) {
        Intent intent = new Intent(getApplicationContext(), CarActivityOrderOutfit.class);
        intent.putExtra("orderID", orderOutfit.getID());
        intent.putExtra("carID", carData.getCarID());
        setResult(Activity.RESULT_OK, intent);

        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_scaner, menu);

        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                searchItem.collapseActionView();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.setFilter(newText, orderOutfit.getCarDataOutfit());
                return true;
            }
        });

        menu.add(Menu.NONE, SAVE_FTP, Menu.NONE, R.string.send_ftp_menu)
                .setIcon(R.drawable.ic_send)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        String message = getString(R.string.completed_outfit);

                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailOrderOutfit.this);
                        builder.setMessage(message)
                                .setCancelable(true)
                                .setPositiveButton(getString(R.string.butt_Yes), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        IntentServiceDataBase.getPhotoOutfit(DetailOrderOutfit.this, orderOutfit.getID());
                                    }
                                })
                                .setNegativeButton(getString(R.string.butt_Not), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                        return false;
                    }
                })
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_scaner:
                scanBarCode();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void scanBarCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(ScannerActivity.class);
        intentIntegrator.setDesiredBarcodeFormats(intentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setPrompt(getString(R.string.camera_to_the_barcode));
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN) {
            IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (Result != null) {
                if (Result.getContents() == null) {

                } else {
                    String tBarCode = Result.getContents();
                    tBarCode = SharedData.clearBarcode(tBarCode);

                    ArrayList<CarDataOutfit> carsData = orderOutfit.getCarDataOutfit();
                    for (int i = 0; i < carsData.size(); i++) {
                        CarDataOutfit carData = carsData.get(i);

                        if (carData.getBarCode().equals(tBarCode)) {
                            viewCarData(carData);
                        }
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == 1) {
            updateLists();
        } else if (requestCode == SAVE_FTP && resultCode == RESULT_OK) {
            sendStatusCB();
        } else if (requestCode == IntentServiceDataBase.REQUEST_CODE_GET_PHOTO_OUTFIT && resultCode == RESULT_OK) {
            ArrayList<Photo> mPhotos = data.getParcelableArrayListExtra("photos");
            mTaskPhotoFTP = new SaveTaskPhotoFTP(DetailOrderOutfit.this, orderOutfit.getID());
            mTaskPhotoFTP.execute(mPhotos);
        }
    }

    private void sendStatusCB() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage(getString(R.string.wait_send_status));
        mDialog.setCancelable(false);
        mDialog.show();

        SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        String login = preferences.getString("Login", "");
        String password = preferences.getString("Password", "");

//        String stringCarData = SOAP_Objects.getCarDataOutfit(orderID, carDataOutfit);
//
//        SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(ACTION_SET_CAR_Outfit, login, password, getApplicationContext());
//        dispatcher.string_Inquiry = stringCarData;

//        dispatcher.start();

    }

    public class SaveTaskPhotoFTP extends AsyncTask<ArrayList<Photo>, Integer, Boolean> {
        private Context mContext;
        private ProgressDialog mDialog;
        private String orderID;

        public SaveTaskPhotoFTP(Context context, String orderID) {
            this.mContext = context;
            this.orderID = orderID;
        }

        @Override
        protected Boolean doInBackground(ArrayList<Photo>... arrayLists) {
            for (ArrayList<Photo> mPhotos : arrayLists) {
                for (int i = 0; i < mPhotos.size(); i++) {
                    Photo photo = mPhotos.get(i);
                    publishProgress(mPhotos.size(), i + 1);

                    if (sendPhoto(photo)) {
                        //SharedData.deletePhoto(photo.getCurrentPhotoPath());
                    } else {
                        break;
                    }
                }
            }
            return null;
        }

        private boolean sendPhoto(Photo photo) {
            boolean uploadFile = false;

            try {
                String host = SharedData.hostFTP;
                int port = SharedData.portFTP;
                String username = SharedData.usernameFTP;
                String password = SharedData.passwordFTP;

                String basePath = "";
                String filePath = "" + orderID + "/" + photo.getCarID();
                String filename = photo.getName();
                InputStream input = new FileInputStream(new File(photo.getCurrentPhotoPath()));
                uploadFile = FtpUtil.uploadFile(host, port, username, password, basePath, filePath, filename, input);
            } catch (Exception e) {
                uploadFile = false;
            }
            return uploadFile;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage(mContext.getResources().getString(R.string.wait_ftp));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mDialog.setMessage(String.format(mContext.getResources().getString(R.string.send_ftp), values[1], values[0]));
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            Toast.makeText(mContext, R.string.ok_ftp, Toast.LENGTH_LONG).show();

            setResult(SAVE_FTP);

            //finish();
        }
    }

}
