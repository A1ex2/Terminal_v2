package ua.org.algoritm.terminal.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.CarData;
import ua.org.algoritm.terminal.R;

public class RecyclerAdapterCarData extends RecyclerView.Adapter<RecyclerAdapterCarData.CarDataViewHolder> {
    private int mResourse;
    private ArrayList<CarData> mCarData = new ArrayList<>();
    private LayoutInflater mInflater;

    public RecyclerAdapterCarData(Context context, int resourse, ArrayList<CarData> carData) {
        mResourse = resourse;
//        mCarData = carData;
        mCarData.addAll(carData);
        mInflater = LayoutInflater.from(context);
    }

    public interface ActionListener {
        void onClick(CarData carData);
    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }

    public void setCarData(ArrayList<CarData> carData) {
        mCarData.clear();
        mCarData.addAll(carData);
        notifyDataSetChanged();
    }

    public void setFilter(String newText, ArrayList<CarData> carDataList) {
        String query = newText.toLowerCase();

        final ArrayList<CarData> filterModeList = new ArrayList<>();

        for (CarData model : carDataList) {

            final String text = model.toString().toLowerCase();
            if (text.contains(query)) {
                filterModeList.add(model);
            }
        }

        mCarData.clear();
        mCarData.addAll(filterModeList);
        notifyDataSetChanged();
    }

    @Override
    public CarDataViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(mResourse, viewGroup, false);
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
        private ConstraintLayout mConstraintLayout;
//        private ConstraintLayout mConstraintLayout;

        public CarDataViewHolder(View itemView) {
            super(itemView);

            itemCar = itemView.findViewById(R.id.itemCar);
            itemBarCode = itemView.findViewById(R.id.itemBarCode);
            itemProductionDate = itemView.findViewById(R.id.itemProductionDate);
            itemSector = itemView.findViewById(R.id.itemSector);
            itemRow = itemView.findViewById(R.id.itemRow);
            mConstraintLayout = itemView.findViewById(R.id.constraintLayout);
//            mConstraintLayout = itemView.findViewById(R.id.constraintLayout);
        }

        public void set(CarData carData) {
            itemCar.setText(carData.getCar());
            itemBarCode.setText(carData.getBarCode());
            itemProductionDate.setText(carData.getProductionDateString());
            itemSector.setText(carData.getSector());
            itemRow.setText(carData.getRow());

            if (carData.saveCB) {
                //mConstraintLayout.setBackgroundColor(Color.parseColor("#BDECB8"));
            }
        }
    }
}

