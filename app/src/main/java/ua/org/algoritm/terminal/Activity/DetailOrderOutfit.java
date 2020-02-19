package ua.org.algoritm.terminal.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import ua.org.algoritm.terminal.Adapters.RecyclerAdapterCarDataOrderOutfit;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.CarDataOutfit;
import ua.org.algoritm.terminal.Objects.OrderOutfit;
import ua.org.algoritm.terminal.R;

public class DetailOrderOutfit extends AppCompatActivity {
    private TextView description;
    private TextView state;
    private TextView responsible;
    private RecyclerView recyclerView;
    private RecyclerAdapterCarDataOrderOutfit adapter;
    private OrderOutfit orderOutfit;

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
            adapter = new RecyclerAdapterCarDataOrderOutfit(this, R.layout.item_issuance_detail, orderOutfit.getCarDataOutfit());
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

        startActivity(intent);
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
        }
    }

}
