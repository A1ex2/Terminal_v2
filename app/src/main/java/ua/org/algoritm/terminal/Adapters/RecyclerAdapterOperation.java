package ua.org.algoritm.terminal.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.OperationOutfits;
import ua.org.algoritm.terminal.R;

public class RecyclerAdapterOperation extends RecyclerView.Adapter<RecyclerAdapterOperation.OperationViewHolder> {

    private int mResourse;
    private ArrayList<OperationOutfits> mOperation = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;

    public RecyclerAdapterOperation(Context context, int resourse, ArrayList<OperationOutfits> operation) {
        mResourse = resourse;
        mOperation.addAll(operation);
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public interface ActionListener {
        void onClick(OperationOutfits operation);
    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }

    public void setFilter(String newText, ArrayList<OperationOutfits> operationList) {
        String query = newText.toLowerCase();

        final ArrayList<OperationOutfits> filterModeList = new ArrayList<>();

        for (OperationOutfits model : operationList) {

            final String text = model.toString().toLowerCase();
            if (text.contains(query)) {
                filterModeList.add(model);
            }
        }

        mOperation.clear();
        mOperation.addAll(filterModeList);
        notifyDataSetChanged();
    }

    @Override
    public OperationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(mResourse, viewGroup, false);
        return new OperationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OperationViewHolder groupViewHolder, int i) {
        final OperationOutfits operation = mOperation.get(i);
        groupViewHolder.set(operation);

        if (mListener != null) {
            groupViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(operation);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mOperation.size();
    }

    public class OperationViewHolder extends RecyclerView.ViewHolder {
        private TextView itemOperation;
        private TextView itemQuantityPhoto;
        private CheckBox checkBox_select;
        private TextView itemDescription;

        public OperationViewHolder(View itemView) {
            super(itemView);

            itemOperation = itemView.findViewById(R.id.itemOperation);
            itemQuantityPhoto = itemView.findViewById(R.id.itemQuantityPhoto);
            checkBox_select = itemView.findViewById(R.id.checkBox_select);
            itemDescription = itemView.findViewById(R.id.itemDescription);
        }

        public void set(OperationOutfits operation) {
            itemOperation.setText(operation.getOperation());
            itemQuantityPhoto.setText("" + operation.getQuantityPhoto() + " " + mContext.getResources().getString(R.string.photo));

            itemDescription.setText(operation.getDescription());

            checkBox_select.setChecked(operation.getPerformed());
            checkBox_select.setTag(operation);
            checkBox_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OperationOutfits operation = (OperationOutfits) view.getTag();
                    mListener.onClick(operation);
//                    Toast.makeText(view.getContext(), "1111", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
