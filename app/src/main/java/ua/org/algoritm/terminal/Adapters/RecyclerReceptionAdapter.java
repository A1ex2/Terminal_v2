package ua.org.algoritm.terminal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.Reception;
import ua.org.algoritm.terminal.R;

public class RecyclerReceptionAdapter extends RecyclerView.Adapter<RecyclerReceptionAdapter.ReceptionViewHolder> {
    private int mResourse;
    private ArrayList<Reception> mReceptions = new ArrayList<>();
    private LayoutInflater mInflater;

    public RecyclerReceptionAdapter(Context context, int resourse) {
        mResourse = resourse;
        mReceptions.addAll(SharedData.RECEPTION);
        mInflater = LayoutInflater.from(context);
    }

    public void setReceptions() {
        mReceptions.clear();
        mReceptions.addAll(SharedData.RECEPTION);
        notifyDataSetChanged();
    }

    public interface ActionListener {
        void onClick(Reception reception);
    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }

    @Override
    public ReceptionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(mResourse, null);
        return new ReceptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReceptionViewHolder groupViewHolder, int i) {
        final Reception reception = mReceptions.get(i);
        groupViewHolder.set(reception);

        if (mListener != null) {
            groupViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(reception);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mReceptions.size();
    }

    public void setFilter(String newText) {
        String query = newText.toLowerCase();

        final ArrayList<Reception> filterModeList = new ArrayList<>();

        for (Reception model : SharedData.RECEPTION) {

            final String textDescription = model.getDescription().toLowerCase();
            final String textAutoNumber = model.getAutoNumber().toLowerCase();
            final String textDriver = model.getDriver().toLowerCase();
            if (textDescription.contains(query)
                    | textAutoNumber.contains(query)
                    | textDriver.contains(query)) {

                filterModeList.add(model);
            }
        }

        mReceptions.clear();
        mReceptions.addAll(filterModeList);
        notifyDataSetChanged();
    }

    public class ReceptionViewHolder extends RecyclerView.ViewHolder {
        private TextView itemDescription;
        private TextView itemAutoNumber;
        private TextView itemDriver;

        public ReceptionViewHolder(View itemView) {
            super(itemView);

            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemAutoNumber = itemView.findViewById(R.id.itemAutoNumber);
            itemDriver = itemView.findViewById(R.id.itemDriver);
        }

        public void set(Reception reception) {
            itemDescription.setText(reception.getDescription());
            itemAutoNumber.setText(reception.getAutoNumber());
            itemDriver.setText(reception.getDriver());
        }
    }

}
