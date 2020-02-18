package ua.org.algoritm.terminal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.OrderOutfit;
import ua.org.algoritm.terminal.R;

public class RecyclerOrderOutfitAdapter extends RecyclerView.Adapter<RecyclerOrderOutfitAdapter.OrderOutfitViewHolder> {
    private int mResourse;
    private ArrayList<OrderOutfit> mOrderOutfits = new ArrayList<>();
    private LayoutInflater mInflater;

    public RecyclerOrderOutfitAdapter(Context context, int resourse) {
        mResourse = resourse;
        mOrderOutfits.addAll(SharedData.ORDER_OUTFIT);
        mInflater = LayoutInflater.from(context);
    }

    public void setOrderOutfits() {
        mOrderOutfits.clear();
        mOrderOutfits.addAll(SharedData.ORDER_OUTFIT);
        notifyDataSetChanged();
    }

    public interface ActionListener {
        void onClick(OrderOutfit issuance);
    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }

    @Override
    public OrderOutfitViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(mResourse, viewGroup, false);
        return new OrderOutfitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderOutfitViewHolder groupViewHolder, int i) {
        final OrderOutfit issuance = mOrderOutfits.get(i);
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
        return mOrderOutfits.size();
    }

    public void setFilter(String newText) {
        String query = newText.toLowerCase();

        final ArrayList<OrderOutfit> filterModeList = new ArrayList<>();

        for (OrderOutfit model : SharedData.ORDER_OUTFIT) {

            final String textDescription = model.getDescription().toLowerCase();
            final String textResponsible = model.getResponsible().toLowerCase();
            final String textState = model.getState().toLowerCase();
            if (textDescription.contains(query)
                    | textResponsible.contains(query)
                    | textState.contains(query)) {

                filterModeList.add(model);
            }
        }

        mOrderOutfits.clear();
        mOrderOutfits.addAll(filterModeList);
        notifyDataSetChanged();
    }

    public class OrderOutfitViewHolder extends RecyclerView.ViewHolder {
        private TextView itemDescription;
        private TextView itemState;
        private TextView itemResponsible;

        public OrderOutfitViewHolder(View itemView) {
            super(itemView);

            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemState = itemView.findViewById(R.id.itemState);
            itemResponsible = itemView.findViewById(R.id.itemResponsible);
        }

        public void set(OrderOutfit issuance) {
            itemDescription.setText(issuance.getDescription());
            itemState.setText(issuance.getState());
            itemResponsible.setText(issuance.getResponsible());
        }
    }

}
