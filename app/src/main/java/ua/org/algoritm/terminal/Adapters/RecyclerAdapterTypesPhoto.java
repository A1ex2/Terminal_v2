package ua.org.algoritm.terminal.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ua.org.algoritm.terminal.Objects.TypesPhoto;
import ua.org.algoritm.terminal.R;

public class RecyclerAdapterTypesPhoto extends RecyclerView.Adapter<RecyclerAdapterTypesPhoto.TypesPhotoViewHolder> {

    private int mResourse;
    private ArrayList<TypesPhoto> mTypesPhoto = new ArrayList<>();
    private LayoutInflater mInflater;

    public RecyclerAdapterTypesPhoto(Context context, int resourse, ArrayList<TypesPhoto> typesPhotos) {
        mResourse = resourse;
        mTypesPhoto.addAll(typesPhotos);
        mInflater = LayoutInflater.from(context);
    }

    public void setTypesPhoto(ArrayList<TypesPhoto> typesPhoto) {
        mTypesPhoto.clear();
        mTypesPhoto.addAll(typesPhoto);
        notifyDataSetChanged();
    }

    public interface ActionListener {
        void onClicViewPhotos(TypesPhoto typesPhoto);
        void onClicBtnAdd(TypesPhoto typesPhoto);
    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }

    @Override
    public TypesPhotoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(mResourse, viewGroup, false);
        return new TypesPhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TypesPhotoViewHolder groupViewHolder, int i) {
        final TypesPhoto typesPhoto = mTypesPhoto.get(i);
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
        return mTypesPhoto.size();
    }

    public class TypesPhotoViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout typesPhoto;
        private TextView description;
        private TextView quantity;
        private ImageButton btnAdd;

        public TypesPhotoViewHolder(View itemView) {
            super(itemView);
            typesPhoto = itemView.findViewById(R.id.typesPhoto);
            description = itemView.findViewById(R.id.description);
            quantity = itemView.findViewById(R.id.quantity);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }

        public void set(TypesPhoto typesPhoto) {
            description.setText(typesPhoto.getTypePhoto());
            quantity.setText("(" + String.valueOf(typesPhoto.getPhotoActInspections().size()) + ")");
        }
    }
}
