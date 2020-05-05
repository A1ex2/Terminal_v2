package ua.org.algoritm.terminal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ua.org.algoritm.terminal.Objects.Equipment;
import ua.org.algoritm.terminal.R;

public class RecyclerAdapterEquipment extends RecyclerView.Adapter<RecyclerAdapterEquipment.EquipmentViewHolder> {

    private int mResourse;
    private ArrayList<Equipment> mEquipment = new ArrayList<>();
    private LayoutInflater mInflater;

    public RecyclerAdapterEquipment(Context context, int resourse, ArrayList<Equipment> equipments) {
        mResourse = resourse;
        mEquipment.addAll(equipments);
        mInflater = LayoutInflater.from(context);
    }

    public void setEquipment(ArrayList<Equipment> equipment) {
        mEquipment.clear();
        mEquipment.addAll(equipment);
        notifyDataSetChanged();
    }

    public interface ActionListener {
        void onClicViewPhoto(Equipment equipment);

        void onClicBtnAdd(Equipment equipment);

        void onClicBtnSub(Equipment equipment);

        void onClicBtnEqually(Equipment equipment);
    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }

    @Override
    public EquipmentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(mResourse, viewGroup, false);
        return new EquipmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EquipmentViewHolder groupViewHolder, int i) {
        final Equipment equipment = mEquipment.get(i);
        groupViewHolder.set(equipment);

        if (mListener != null) {
            groupViewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClicBtnAdd(equipment);
                }
            });

            groupViewHolder.btnSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClicBtnSub(equipment);
                }
            });

            groupViewHolder.btnEqually.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClicBtnEqually(equipment);
                }
            });

            groupViewHolder.viewPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClicViewPhoto(equipment);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mEquipment.size();
    }

    public class EquipmentViewHolder extends RecyclerView.ViewHolder {
        private TextView description;
        private ImageView viewPhoto;
        private TextView quantityPlan;
        private EditText quantityFact;

        private Button btnAdd;
        private Button btnSub;
        private Button btnEqually;

        public EquipmentViewHolder(View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.description);
            viewPhoto = itemView.findViewById(R.id.viewPhoto);
            quantityPlan = itemView.findViewById(R.id.quantityPlan);
            quantityFact = itemView.findViewById(R.id.quantityFact);

            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnSub = itemView.findViewById(R.id.btnSub);
            btnEqually = itemView.findViewById(R.id.btnEqually);
        }

        public void set(Equipment equipment) {
            setPic(equipment.getPhotoActInspection().getCurrentPhotoPath());
            description.setText(equipment.getEquipment());
            quantityPlan.setText(String.valueOf(equipment.getQuantityPlan()));
//            if (equipment.getQuantityFact() != 0) {
                quantityFact.setText(String.valueOf(equipment.getQuantityFact()));
//            }
        }

        private void setPic(String mCurrentEquipmentPath) {
            if (mCurrentEquipmentPath.equals("")) {
                return;
            }
            // Get the dimensions of the View
            int targetW = 70;
            int targetH = 70;

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentEquipmentPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentEquipmentPath, bmOptions);
            viewPhoto.setImageBitmap(bitmap);
        }
    }
}
