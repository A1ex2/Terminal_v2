package ua.org.algoritm.terminal.ui.issuance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import ua.org.algoritm.terminal.Activity.CarActivityIssuance;
import ua.org.algoritm.terminal.Activity.DetailIssuance;
import ua.org.algoritm.terminal.Adapters.RecyclerAdapterCarDataIssuance;
import ua.org.algoritm.terminal.Adapters.RecyclerIssuanceAdapter;
import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Dispatcher;
import ua.org.algoritm.terminal.ConnectTo1c.UIManager;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.CarDataIssuance;
import ua.org.algoritm.terminal.Objects.Issuance;
import ua.org.algoritm.terminal.R;

public class IssuanceFragment extends Fragment {
    public static final int REQUEST_CODE_UPDATE = 20;

    public static final int ACTION_LIST = 22;
    public static final int ACTION_ConnectionError = 0;
    public static final int ACTION_Connection = 1111;
    public static final int ACTION_Connection_Lost = 2222;

    public static UIManager uiManager;
    public static SoapObject soapParam_Response;
    public static SoapFault responseFault;
    public static Handler soapHandler;

    private RecyclerView recyclerView;
    private RecyclerIssuanceAdapter adapter;
    private ProgressDialog mDialog;
    private ArrayList<Issuance> mCarData = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_issuance, container, false);

        setHasOptionsMenu(true);

        recyclerView = root.findViewById(R.id.list);
        updateLists();

        uiManager = new UIManager(getActivity());
        soapHandler = new IssuanceFragment.incomingHandler(this);

        getUpdateList();

        return root;
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

                adapter.setFilter(newText);

                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UPDATE) {
            getUpdateList();
        }
    }

    private void getUpdateList() {

        mDialog = new ProgressDialog(getContext());
        mDialog.setMessage(getString(R.string.wait_update));
        mDialog.setCancelable(false);
        mDialog.show();

        SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(ACTION_LIST, SharedData.LOGIN, SharedData.PASSWORD, getContext());
        dispatcher.start();

    }

    private void updateLists() {
        if (adapter == null) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

            adapter = new RecyclerIssuanceAdapter(getContext(), R.layout.item_issuance_new);
            recyclerView.setAdapter(adapter);
            adapter.setActionListener(new RecyclerIssuanceAdapter.ActionListener() {
                @Override
                public void onClick(Issuance issuance) {
                    Intent intent = new Intent(getContext(), DetailIssuance.class);
                    intent.putExtra("Issuance", issuance.getID());
                    startActivityForResult(intent, REQUEST_CODE_UPDATE);
                }
            });

        } else {

            adapter.setIssuances();
            recyclerView.setAdapter(adapter);

        }
    }

    class incomingHandler extends Handler {
        private final WeakReference<IssuanceFragment> mTarget;

        public incomingHandler(IssuanceFragment context) {
            mTarget = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            IssuanceFragment target = mTarget.get();

            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

            switch (msg.what) {

                case ACTION_ConnectionError:
                    uiManager.showToast(getString(R.string.errorConnection) + getSoapErrorMessage());
                    break;
                case ACTION_LIST:
                    target.checkListResult();
                    break;

                case ACTION_Connection:
                    //uiManager.showToast(getString(R.string.connection_for_internet));
                    target.getUpdateList();
                    break;
                case ACTION_Connection_Lost:
                    //uiManager.showToast(getString(R.string.lost_for_internet));
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

    public void checkListResult() {
        try {
            updateLists();

        } catch (Exception e) {
            uiManager.showToast(getString(R.string.errorConnection));
            e.printStackTrace();
        }
    }

}