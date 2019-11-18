package ua.org.algoritm.terminal.ui.moving;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import ua.org.algoritm.terminal.Activity.CarDataList;
import ua.org.algoritm.terminal.Activity.DetailReception;
import ua.org.algoritm.terminal.Adapters.RecyclerAdapterCarData;
import ua.org.algoritm.terminal.ConnectTo1c.UIManager;
import ua.org.algoritm.terminal.Objects.CarData;
import ua.org.algoritm.terminal.R;

public class MovingFragment extends Fragment {
    private static final int REQUEST_CODE_SELECT_CAR = 50;

    private FloatingActionButton mActionButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_moving, container, false);

        mActionButton = root.findViewById(R.id.floatingActionButton);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionCar();
            }
        });




        return root;
    }

    private void selectionCar() {
        Intent intent = new Intent(getActivity(), CarDataList.class);
        startActivityForResult(intent, REQUEST_CODE_SELECT_CAR);
    }


}