package ua.org.algoritm.terminal.Objects;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;


public class Photo implements Parcelable {
    private String orderID;
    private String carID;

    private String name;
    private String currentPhotoPath;
    private String currentPhotoPathFTP;

    public Photo() {
        this.currentPhotoPath = "";
        this.currentPhotoPathFTP = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    public void setCurrentPhotoPath(String currentPhotoPath) {
        this.currentPhotoPath = currentPhotoPath;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getCurrentPhotoPathFTP() {
        return currentPhotoPathFTP;
    }

    public void setCurrentPhotoPathFTP(String currentPhotoPathFTP) {
        this.currentPhotoPathFTP = currentPhotoPathFTP;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderID);
        dest.writeString(this.carID);
        dest.writeString(this.name);
        dest.writeString(this.currentPhotoPath);
    }

    protected Photo(Parcel in) {
        this.orderID = in.readString();
        this.carID = in.readString();
        this.name = in.readString();
        this.currentPhotoPath = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
