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

import ua.org.algoritm.terminal.Objects.TypeDamagePhoto;
import ua.org.algoritm.terminal.R;

public class RecyclerAdapterTypesDamagePhoto extends RecyclerView.Adapter<RecyclerAdapterTypesDamagePhoto.TypeDamagePhotoViewHolder> {

    private int mResourse;
    private ArrayList<TypeDamagePhoto> mTypeDamagePhoto = new ArrayList<>();
    private LayoutInflater mInflater;

    public RecyclerAdapterTypesDamagePhoto(Context context, int resourse, ArrayList<TypeDamagePhoto> typesPhotos) {
        mResourse = resourse;
        mTypeDamagePhoto.addAll(typesPhotos);
        mInflater = LayoutInflater.from(context);
    }

    public void setTypeDamagePhoto(ArrayList<TypeDamagePhoto> typesPhoto) {
        mTypeDamagePhoto.clear();
        mTypeDamagePhoto.addAll(typesPhoto);
        notifyDataSetChanged();
    }

    public interface ActionListener {
        void onClicViewPhotos(TypeDamagePhoto typesPhoto);
        void onClicBtnAdd(TypeDamagePhoto typesPhoto);
    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }

    @Override
    public TypeDamagePhotoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(mResourse, viewGroup, false);
        return new TypeDamagePhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TypeDamagePhotoViewHolder groupViewHolder, int i) {
        final TypeDamagePhoto typesPhoto = mTypeDamagePhoto.get(i);
        groupViewHolder.set(typesPhoto);

        if (mListener != null) {
            groupViewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClicBtnAdd(typesPhoto);
                }
            });

            groupViewHolder.typesPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClicViewPhotos(typesPhoto);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mTypeDamagePhoto.size();
    }

    public class TypeDamagePhotoViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout typesPhoto;
        private TextView description;
        private TextView quantity;
        private ImageButton btnAdd;

        public TypeDamagePhotoViewHolder(View itemView) {
            super(itemView);
            typesPhoto = itemView.findViewById(R.id.typesPhoto);
            description = itemView.findViewById(R.id.description);
            quantity = itemView.findViewById(R.id.quantity);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }

        public void set(TypeDamagePhoto typesPhoto) {
            description.setText(typesPhoto.getName());
            quantity.setText("(" + String.valueOf(typesPhoto.getPhotoActInspections().size()) + ")");
        }
    }
}
