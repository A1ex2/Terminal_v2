package ua.org.algoritm.terminal.ui.acceptance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.ksoap2.SoapFault;

import java.lang.ref.WeakReference;

import ua.org.algoritm.terminal.Adapters.RecyclerReceptionAdapter;
import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Dispatcher;
import ua.org.algoritm.terminal.ConnectTo1c.UIManager;
import ua.org.algoritm.terminal.DataBase.SharedData;

import ua.org.algoritm.terminal.Objects.Reception;
import ua.org.algoritm.terminal.R;

public class AcceptanceFragment extends Fragment {
    public static final int ACTION_RECEPTION_LIST = 12;
    public static final int REQUEST_CODE_UPDATE_RECEPTION = 15;
    public static final int ACTION_ConnectionError = 0;

    public static UIManager uiManager;
    public static SoapFault responseFault;

    public static Handler soapHandler;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private RecyclerReceptionAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_acceptance, container, false);

        setHasOptionsMenu(true);

        recyclerView = root.findViewById(R.id.listReception);
        recyclerView.setVisibility(RecyclerView.INVISIBLE);
        updateLists();

        progressBar = root.findViewById(R.id.progressBar);

        uiManager = new UIManager(getActivity());
        soapHandler = new incomingHandler(this);

        getUpdateReceptionList();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        adapter.setReceptions();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

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

//                Snackbar.make(getView(), newText, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                adapter.setFilter(newText);

                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        // handle item selection
//        switch (item.getItemId()) {
//            case R.id.menu_item_search:
//
//                //       onCall();   //your logic
//
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
        return super.onOptionsItemSelected(item);
    }

    private void viewReception(Reception reception) {
//        Intent intent = new Intent(DetailReception.this, CarActivity.class);
//        intent.putExtra("CarData", carData);
//        startActivityForResult(intent, REQUEST_CODE);

        Snackbar.make(getView(), reception.getDescription(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getUpdateReceptionList() {
        recyclerView.setVisibility(RecyclerView.INVISIBLE);
        progressBar.setVisibility(ProgressBar.VISIBLE);

        SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(ACTION_RECEPTION_LIST, SharedData.LOGIN, SharedData.PASSWORD);
        dispatcher.start();
    }

    private void updateLists() {
        if (adapter == null) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            adapter = new RecyclerReceptionAdapter(getContext(), R.layout.item_reception_new);
//            adapter = new RecyclerReceptionAdapter(getContext(), R.layout.item_reception, SharedData.RECEPTION);
            recyclerView.setAdapter(adapter);
            adapter.setActionListener(new RecyclerReceptionAdapter.ActionListener() {
                @Override
                public void onClick(Reception reception) {
                    viewReception(reception);
                }
            });

        } else {

            recyclerView.setVisibility(RecyclerView.VISIBLE);
            progressBar.setVisibility(ProgressBar.INVISIBLE);

            adapter.setReceptions();
            recyclerView.setAdapter(adapter);

        }
    }

    class incomingHandler extends Handler {
        private final WeakReference<AcceptanceFragment> mTarget;

        public incomingHandler(AcceptanceFragment context) {
            mTarget = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            AcceptanceFragment target = mTarget.get();

            switch (msg.what) {

                case ACTION_ConnectionError:
                    uiManager.showToast(getString(R.string.errorConnection) + getSoapErrorMessage());
                    break;
                case ACTION_RECEPTION_LIST:
                    target.checkReceptionListResult();
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

    public void checkReceptionListResult() {
        try {
            updateLists();
        } catch (Exception e) {
            uiManager.showToast(getString(R.string.errorConnection));
            e.printStackTrace();
        }
    }
}