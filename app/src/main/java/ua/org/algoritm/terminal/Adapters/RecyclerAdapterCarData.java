package ua.org.algoritm.terminal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import ua.org.algoritm.terminal.Objects.CarData;
import ua.org.algoritm.terminal.R;

public class RecyclerAdapterCarData extends RecyclerView.Adapter<RecyclerAdapterCarData.CarDataViewHolder> {
    private int mResourse;
    private ArrayList<CarData> mCarData;
    private LayoutInflater mInflater;

    public RecyclerAdapterCarData(Context context, int resourse, ArrayList<CarData> carData) {
        mResourse = resourse;
        mCarData = carData;
        mInflater = LayoutInflater.from(context);
    }

    public interface ActionListener {
        void onClick(CarData carData);
    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }

    @Override
    public CarDataViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(mResourse, null);
        return new CarDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarDataViewHolder groupViewHolder, int i) {
        final CarData carData = mCarData.get(i);
        groupViewHolder.set(carData);

        if (mListener != null) {
            groupViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(carData);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mCarData.size();
    }

    public class CarDataViewHolder extends RecyclerView.ViewHolder {
        private TextView itemCar;
        private TextView itemBarCode;
        private TextView itemProductionDate;
        private TextView itemSector;
        private TextView itemRow;

        public CarDataViewHolder(View itemView) {
            super(itemView);

            itemCar = itemView.findViewById(R.id.itemCar);
            itemBarCode = itemView.findViewById(R.id.itemBarCode);
            itemProductionDate = itemView.findViewById(R.id.itemProductionDate);
            itemSector = itemView.findViewById(R.id.itemSector);
            itemRow = itemView.findViewById(R.id.itemRow);
        }

        public void set(CarData carData) {
            itemCar.setText(carData.getCar());
            itemBarCode.setText(carData.getBarCode());
            itemProductionDate.setText(carData.getProductionDateString());
            itemSector.setText(carData.getSector());
            itemRow.setText(carData.getRow());
        }
    }
}
