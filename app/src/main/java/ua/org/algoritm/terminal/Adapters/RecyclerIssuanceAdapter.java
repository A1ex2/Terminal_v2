package ua.org.algoritm.terminal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.Issuance;
import ua.org.algoritm.terminal.R;

public class RecyclerIssuanceAdapter extends RecyclerView.Adapter<RecyclerIssuanceAdapter.IssuanceViewHolder> {
    private int mResourse;
    private ArrayList<Issuance> mIssuances = new ArrayList<>();
    private LayoutInflater mInflater;

    public RecyclerIssuanceAdapter(Context context, int resourse) {
        mResourse = resourse;
        mIssuances.addAll(SharedData.ISSUANCE);
        mInflater = LayoutInflater.from(context);
    }

    public void setIssuances() {
        mIssuances.clear();
        mIssuances.addAll(SharedData.ISSUANCE);
        notifyDataSetChanged();
    }

    public interface ActionListener {
        void onClick(Issuance issuance);
    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }

    @Override
    public IssuanceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(mResourse, viewGroup, false);
        return new IssuanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IssuanceViewHolder groupViewHolder, int i) {
        final Issuance issuance = mIssuances.get(i);
        groupViewHolder.set(issuance);

        if (mListener != null) {
            groupViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(issuance);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mIssuances.size();
    }

    public void setFilter(String newText) {
        String query = newText.toLowerCase();

        final ArrayList<Issuance> filterModeList = new ArrayList<>();

        for (Issuance model : SharedData.ISSUANCE) {

            final String textDescription = model.getDescription().toLowerCase();
            final String textAutoNumber = model.getAutoNumber().toLowerCase();
            final String textDriver = model.getDriver().toLowerCase();
            if (textDescription.contains(query)
                    | textAutoNumber.contains(query)
                    | textDriver.contains(query)) {

                filterModeList.add(model);
            }
        }

        mIssuances.clear();
        mIssuances.addAll(filterModeList);
        notifyDataSetChanged();
    }

    public class IssuanceViewHolder extends RecyclerView.ViewHolder {
        private TextView itemDescription;
        private TextView itemAutoNumber;
        private TextView itemDriver;

        public IssuanceViewHolder(View itemView) {
            super(itemView);

            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemAutoNumber = itemView.findViewById(R.id.itemAutoNumber);
            itemDriver = itemView.findViewById(R.id.itemDriver);
        }

        public void set(Issuance issuance) {
            itemDescription.setText(issuance.getDescription());
            itemAutoNumber.setText(issuance.getAutoNumber());
            itemDriver.setText(issuance.getDriver());
        }
    }

}
