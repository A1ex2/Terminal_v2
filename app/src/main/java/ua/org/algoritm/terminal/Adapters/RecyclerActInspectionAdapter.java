package ua.org.algoritm.terminal.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
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
        mActInspections.addAll(getActInspections());
        mInflater = LayoutInflater.from(context);
    }

    public void setActInspections() {
        mActInspections.clear();
        mActInspections.addAll(getActInspections());
        notifyDataSetChanged();
    }

    private ArrayList<ActInspection> getActInspections(){
        ArrayList<ActInspection> mActs = new ArrayList<>();

        String formID = "Сервис";
        String formID2 = "пусто";
        if (SharedData.thisDriver) {
            formID = "ОсмотрНаСкладе";
            formID2 = "ОсмотрНаВыгрузке";
        }

        for (int i = 0; i < SharedData.ACT_INSPECTION.size(); i++) {
            ActInspection act = SharedData.ACT_INSPECTION.get(i);
            if ((act.getFormID().equals(formID) | act.getFormID().equals(formID2)) & !act.sendPerformed){
                mActs.add(act);
            }
        }

        return mActs;
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

        for (ActInspection model : getActInspections()) {

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
        private LinearLayout linearLayoutCard;

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
            linearLayoutCard = itemView.findViewById(R.id.linearLayoutCard);
        }

        public void set(ActInspection act) {
            itemForm.setText(act.getForm());
            itemDatePlan.setText(act.getInspectionDatePlanString());
            itemDescription.setText(act.getDescription());
            itemState.setText(act.getState());
            itemCar.setText(act.getCar());
            itemBarCode.setText(act.getBarCode());
            itemProductionDate.setText(act.getInspectionDatePlanString());
            itemSector.setText(act.getSector());
            itemRow.setText(act.getRow());

            if (SharedData.isOfflineReception) {
                if (act.isPerformed()) {
                    itemState.setText("Выполнен(не отправлен)");

                    itemForm.setTextColor(Color.parseColor("#1d5e16"));
                    itemDatePlan.setTextColor(Color.parseColor("#1d5e16"));
                    itemDescription.setTextColor(Color.parseColor("#1d5e16"));
                    itemState.setTextColor(Color.parseColor("#1d5e16"));
                    itemCar.setTextColor(Color.parseColor("#1d5e16"));
                    itemBarCode.setTextColor(Color.parseColor("#1d5e16"));
                    itemProductionDate.setTextColor(Color.parseColor("#1d5e16"));
                    itemSector.setTextColor(Color.parseColor("#1d5e16"));
                    itemRow.setTextColor(Color.parseColor("#1d5e16"));

                    if (act.sendPerformed) {
                        linearLayoutCard.setBackgroundColor(Color.parseColor("#a7e9a0"));
                        itemState.setText("Выполнен(отправлен)");
                    }
                }
            }

        }
    }
}
