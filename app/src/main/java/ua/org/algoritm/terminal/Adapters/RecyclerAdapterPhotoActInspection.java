package ua.org.algoritm.terminal.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ua.org.algoritm.terminal.Objects.PhotoActInspection;
import ua.org.algoritm.terminal.R;

public class RecyclerAdapterPhotoActInspection extends RecyclerView.Adapter<RecyclerAdapterPhotoActInspection.PhotoActInspectionViewHolder> {

    private int mResourse;
    private ArrayList<PhotoActInspection> mPhotoActInspection = new ArrayList<>();
    private LayoutInflater mInflater;

    public RecyclerAdapterPhotoActInspection(Context context, int resourse, ArrayList<PhotoActInspection> photos) {
        mResourse = resourse;
        mPhotoActInspection.addAll(photos);
        mInflater = LayoutInflater.from(context);
    }

    public void setPhotoActInspection(ArrayList<PhotoActInspection> photo) {
        mPhotoActInspection.clear();
        mPhotoActInspection.addAll(photo);
        notifyDataSetChanged();
    }

    public interface ActionListener {
        void onClick(PhotoActInspection photo);
    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }

    @Override
    public PhotoActInspectionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(mResourse, viewGroup, false);
        return new PhotoActInspectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoActInspectionViewHolder groupViewHolder, int i) {
        final PhotoActInspection photo = mPhotoActInspection.get(i);
        groupViewHolder.set(photo);

        if (mListener != null) {
            groupViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(photo);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mPhotoActInspection.size();
    }

    public class PhotoActInspectionViewHolder extends RecyclerView.ViewHolder {
        private TextView description;
        private ImageView viewPhoto;

        public PhotoActInspectionViewHolder(View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.description);
            viewPhoto = itemView.findViewById(R.id.viewPhoto);
        }

        public void set(PhotoActInspection photo) {
            description.setText(photo.getName());
            //viewPhoto.setImageURI(photo.getUri());
            setPic(photo.getCurrentPhotoPath());
        }

        private void setPic(String mCurrentPhotoPath) {
            // Get the dimensions of the View
            int targetW = 70;
            int targetH = 70;

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            viewPhoto.setImageBitmap(bitmap);
        }
    }
}
