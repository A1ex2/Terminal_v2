package ua.org.algoritm.terminal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ua.org.algoritm.terminal.Objects.Inspection;
import ua.org.algoritm.terminal.R;

public class RecyclerAdapterInspection extends RecyclerView.Adapter<RecyclerAdapterInspection.InspectionViewHolder> {

    private int mResourse;
    private ArrayList<Inspection> mInspection = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;

    public RecyclerAdapterInspection(Context context, int resourse, ArrayList<Inspection> inspection) {
        mResourse = resourse;
        mInspection.addAll(inspection);
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public interface ActionListener {
        void onClick(Inspection inspection);
    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }

    public void setFilter(String newText, ArrayList<Inspection> inspectionList) {
        String query = newText.toLowerCase();

        final ArrayList<Inspection> filterModeList = new ArrayList<>();

        for (Inspection model : inspectionList) {

            final String text = model.toString().toLowerCase();
            if (text.contains(query)) {
                filterModeList.add(model);
            }
        }

        mInspection.clear();
        mInspection.addAll(filterModeList);
        notifyDataSetChanged();
    }

    @Override
    public InspectionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(mResourse, viewGroup, false);
        return new InspectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InspectionViewHolder groupViewHolder, int i) {
        final Inspection inspection = mInspection.get(i);
        groupViewHolder.set(inspection);

        if (mListener != null) {
            groupViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(inspection);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mInspection.size();
    }

    public class InspectionViewHolder extends RecyclerView.ViewHolder {
        private TextView itemInspection;
        private CheckBox checkBox_select;

        public InspectionViewHolder(View itemView) {
            super(itemView);

            itemInspection = itemView.findViewById(R.id.itemInspection);
            checkBox_select = itemView.findViewById(R.id.checkBox_select);
        }

        public void set(Inspection inspection) {
            itemInspection.setText(inspection.getName());

            checkBox_select.setChecked(inspection.isPerformed());
            checkBox_select.setTag(inspection);
            checkBox_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Inspection mInspection = (Inspection) view.getTag();
                    mListener.onClick(mInspection);
                }
            });
        }
    }
}
