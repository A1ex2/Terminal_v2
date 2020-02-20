package ua.org.algoritm.terminal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ua.org.algoritm.terminal.Objects.Photo;
import ua.org.algoritm.terminal.R;

public class RecyclerAdapterPhoto extends RecyclerView.Adapter<RecyclerAdapterPhoto.PhotoViewHolder> {

    private int mResourse;
    private ArrayList<Photo> mPhoto = new ArrayList<>();
    private LayoutInflater mInflater;

    public RecyclerAdapterPhoto(Context context, int resourse, ArrayList<Photo> photos) {
        mResourse = resourse;
        mPhoto= photos;
        mInflater = LayoutInflater.from(context);
    }

    public interface ActionListener {
        void onClick(Photo photo);
    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(mResourse, viewGroup, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder groupViewHolder, int i) {
        final Photo photo = mPhoto.get(i);
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
        return mPhoto.size();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private TextView description;
        private ImageView viewPhoto;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.description);
            viewPhoto = itemView.findViewById(R.id.viewPhoto);
        }

        public void set(Photo photo) {
            description.setText(photo.getName());
            viewPhoto.setImageURI(photo.getUri());
        }
    }
}
