package ua.org.algoritm.terminal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ua.org.algoritm.terminal.Objects.Damage;
import ua.org.algoritm.terminal.R;

public class RecyclerAdapterDamage extends RecyclerView.Adapter<RecyclerAdapterDamage.DamageViewHolder> {

    private int mResourse;
    private ArrayList<Damage> mDamage = new ArrayList<>();
    private LayoutInflater mInflater;

    public RecyclerAdapterDamage(Context context, int resourse, ArrayList<Damage> damages) {
        mResourse = resourse;
        mDamage.addAll(damages);
        mInflater = LayoutInflater.from(context);
    }

    public void setDamage(ArrayList<Damage> damage) {
        mDamage.clear();
        mDamage.addAll(damage);
        notifyDataSetChanged();
    }

    public interface ActionListener {
        void onClick(Damage damage);

    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }

    @Override
    public DamageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(mResourse, viewGroup, false);
        return new DamageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DamageViewHolder groupViewHolder, int i) {
        final Damage damage = mDamage.get(i);
        groupViewHolder.set(damage);

        if (mListener != null) {
            groupViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(damage);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDamage.size();
    }

    public class DamageViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView description1;
        private TextView description2;

        public DamageViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description1 = itemView.findViewById(R.id.description1);
            description2 = itemView.findViewById(R.id.description2);
        }

        public void set(Damage damage) {
            if (damage.getDetail() != null) {
                name.setText(damage.getDetail().toString());
            } else {
                name.setText("<Деталь не указана>");
            }

            if (damage.getOriginDamage() != null) {
                description1.setText(damage.getOriginDamage().getName());
            }

            if (damage.getDetail() != null) {
                description2.setText(damage.getCommentDamage());
            }
        }
    }
}
