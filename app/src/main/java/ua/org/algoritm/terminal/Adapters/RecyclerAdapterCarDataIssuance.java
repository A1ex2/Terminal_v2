package ua.org.algoritm.terminal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import ua.org.algoritm.terminal.Objects.CarDataIssuance;
import ua.org.algoritm.terminal.R;

public class RecyclerAdapterCarDataIssuance extends RecyclerView.Adapter<RecyclerAdapterCarDataIssuance.CarDataViewHolder> {
    private int mResourse;
    private ArrayList<CarDataIssuance> mCarData = new ArrayList<>();
    private LayoutInflater mInflater;

    public RecyclerAdapterCarDataIssuance(Context context, int resourse, ArrayList<CarDataIssuance> carData) {
        mResourse = resourse;
        mCarData.addAll(carData);
        mInflater = LayoutInflater.from(context);
    }

    public interface ActionListener {
        void onClick(CarDataIssuance carData);
    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }

    public void setCarDataIssuance(ArrayList<CarDataIssuance> carData) {
        mCarData.clear();
        mCarData.addAll(carData);
        notifyDataSetChanged();
    }

    public void setFilter(String newText, ArrayList<CarDataIssuance> carDataList) {
        String query = newText.toLowerCase();

        final ArrayList<CarDataIssuance> filterModeList = new ArrayList<>();

        for (CarDataIssuance model : carDataList) {

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
        final CarDataIssuance carData = mCarData.get(i);
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
        private TextView itemDescription;
        private TextView itemAutoNumber;
        private TextView itemDriver;
        private TextView itemCar;
        private TextView itemSector;
        private TextView itemRow;

        public CarDataViewHolder(View itemView) {
            super(itemView);

            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemAutoNumber = itemView.findViewById(R.id.itemAutoNumber);
            itemDriver = itemView.findViewById(R.id.itemDriver);
            itemCar = itemView.findViewById(R.id.itemCar);
            itemSector = itemView.findViewById(R.id.itemSector);
            itemRow = itemView.findViewById(R.id.itemRow);

        }

        public void set(CarDataIssuance carData) {

            itemDescription.setText(carData.getDescription());
            itemAutoNumber.setText(carData.getAutoNumber());
            itemDriver.setText(carData.getDriver());
            itemCar.setText(carData.getCar());
            itemSector.setText(carData.getSector());
            itemRow.setText(carData.getRow());

        }
    }
}
