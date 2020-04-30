package ua.org.algoritm.terminal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.ActInspection;
import ua.org.algoritm.terminal.R;

public class RecyclerActInspectionAdapter extends RecyclerView.Adapter<RecyclerActInspectionAdapter.ActInspectionViewHolder> {
    private int mResourse;
    private ArrayList<ActInspection> mActInspections = new ArrayList<>();
    private LayoutInflater mInflater;

    public RecyclerActInspectionAdapter(Context context, int resourse) {
        mResourse = resourse;
        mActInspections.addAll(SharedData.ACT_INSPECTION);
        mInflater = LayoutInflater.from(context);
    }

    public void setActInspections() {
        mActInspections.clear();
        mActInspections.addAll(SharedData.ACT_INSPECTION);
        notifyDataSetChanged();
    }

    public interface ActionListener {
        void onClick(ActInspection act);
    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }

    @Override
    public ActInspectionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(mResourse, viewGroup, false);
        return new ActInspectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActInspectionViewHolder groupViewHolder, int i) {
        final ActInspection act = mActInspections.get(i);
        groupViewHolder.set(act);

        if (mListener != null) {
            groupViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(act);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mActInspections.size();
    }

    public void setFilter(String newText) {
        String query = newText.toLowerCase();

        final ArrayList<ActInspection> filterModeList = new ArrayList<>();

        for (ActInspection model : SharedData.ACT_INSPECTION) {

            final String textDescription = model.getDescription().toLowerCase();
            final String textCar = model.getCar().toLowerCase();
            final String textState = model.getState().toLowerCase();
            final String textBarCode = model.getBarCode().toLowerCase();
            final String textForm = model.getForm().toLowerCase();
            if (textDescription.contains(query)
                    | textCar.contains(query)
                    | textState.contains(query)
                    | textBarCode.contains(query)
                    | textForm.contains(query)) {

                filterModeList.add(model);
            }
        }

        mActInspections.clear();
        mActInspections.addAll(filterModeList);
        notifyDataSetChanged();
    }

    public class ActInspectionViewHolder extends RecyclerView.ViewHolder {
        private TextView itemForm;
        private TextView itemDatePlan;
        private TextView itemDescription;
        private TextView itemState;
        private TextView itemCar;
        private TextView itemBarCode;
        private TextView itemProductionDate;
        private TextView itemSector;
        private TextView itemRow;

        public ActInspectionViewHolder(View itemView) {
            super(itemView);

            itemForm = itemView.findViewById(R.id.itemForm);
            itemDatePlan = itemView.findViewById(R.id.itemDatePlan);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemState = itemView.findViewById(R.id.itemState);
            itemCar = itemView.findViewById(R.id.itemCar);
            itemBarCode = itemView.findViewById(R.id.itemBarCode);
            itemProductionDate = itemView.findViewById(R.id.itemProductionDate);
            itemSector = itemView.findViewById(R.id.itemSector);
            itemRow = itemView.findViewById(R.id.itemRow);
        }

        public void set(ActInspection act) {
            itemForm.setText(act.getForm());
            itemDatePlan.setText(act.getInspectionDatePlanString());
            itemDescription.setText(act.getDescription());
            itemState.setText(act.getState());
            itemCar.setText(act.getCar());
            itemBarCode.setText(act.getBarCode());
            itemProductionDate.setText(act.getProductionDate());
            itemSector.setText(act.getSector());
            itemRow.setText(act.getRow());
        }
    }
}
