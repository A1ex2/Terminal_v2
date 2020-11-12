package ua.org.algoritm.terminal.ui.act;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.lang.ref.WeakReference;

import ua.org.algoritm.terminal.Activity.ActInspectionActivity;
import ua.org.algoritm.terminal.Activity.DetailOrderOutfit;
import ua.org.algoritm.terminal.Adapters.RecyclerActInspectionAdapter;
import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Dispatcher;
import ua.org.algoritm.terminal.ConnectTo1c.UIManager;
import ua.org.algoritm.terminal.Constants;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.ActInspection;
import ua.org.algoritm.terminal.R;
import ua.org.algoritm.terminal.Service.ServicePerformedAct;

public class ActInspectionFragment extends Fragment {
    public static final int REQUEST_CODE_UPDATE = 20;

    public static final int ACTION_LIST = 27;
    public static final int ACTION_ConnectionError = 0;
    public static final int ACTION_Connection = 1111;
    public static final int ACTION_Connection_Lost = 2222;

    public static UIManager uiManager;
    public static SoapObject soapParam_Response;
    public static SoapFault responseFault;
    public static Handler soapHandler;

    private RecyclerView recyclerView;
    private RecyclerActInspectionAdapter adapter;
    private ProgressDialog mDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_act_inspection, container, false);

        setHasOptionsMenu(true);

        recyclerView = root.findViewById(R.id.list);
        updateLists();

        uiManager = new UIManager(getActivity());
        soapHandler = new ActInspectionFragment.incomingHandler(this);

        getUpdateList();

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (SharedData.isOfflineReception) {
            inflater.inflate(R.menu.menu_search_offline, menu);
            final MenuItem update_data_receptions = menu.findItem(R.id.update_data_receptions);
            update_data_receptions.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if (!SharedData.isOnline(getContext())) {
                        uiManager.showToast(getString(R.string.no_internet));
                        return false;
                    }

                    String message = getString(R.string.download_act_2);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(message)
                            .setCancelable(true)
                            .setPositiveButton(getString(R.string.butt_Yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    SharedData.updateReceptionListDB = true;
                                    SharedData.updateActInspectionListDB = true;
                                    getUpdateList();

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
            });

            final MenuItem send_act = menu.findItem(R.id.send_act);
            send_act.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (!SharedData.isOnline(getContext())) {
                        uiManager.showToast(getString(R.string.no_internet));
                        return false;
                    }

                    String message = getString(R.string.send_act) + "?";

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(message)
                            .setCancelable(true)
                            .setPositiveButton(getString(R.string.butt_Yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent startIntent = new Intent(getContext(), ServicePerformedAct.class);
                                    startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION_ALL);
                                    getActivity().startService(startIntent);


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
            });
        } else {
            inflater.inflate(R.menu.menu_search, menu);
        }

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

            adapter = new RecyclerActInspectionAdapter(getContext(), R.layout.item_act_inspection);
            recyclerView.setAdapter(adapter);
            adapter.setActionListener(new RecyclerActInspectionAdapter.ActionListener() {
                @Override
                public void onClick(ActInspection actInspection) {
                    Intent intent = new Intent(getContext(), ActInspectionActivity.class);
                    intent.putExtra("actInspection", actInspection.getID());
                    startActivityForResult(intent, REQUEST_CODE_UPDATE);
                }
            });

        } else {
            adapter.setActInspections();
            recyclerView.setAdapter(adapter);
        }
    }

    class incomingHandler extends Handler {
        private final WeakReference<ActInspectionFragment> mTarget;

        public incomingHandler(ActInspectionFragment context) {
            mTarget = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            ActInspectionFragment target = mTarget.get();

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
                    uiManager.showToast(getString(R.string.connection_for_internet));
                    target.getUpdateList();
                    break;
                case ACTION_Connection_Lost:
                    uiManager.showToast(getString(R.string.lost_for_internet));
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