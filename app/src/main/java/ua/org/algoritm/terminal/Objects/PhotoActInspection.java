package ua.org.algoritm.terminal.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PhotoActInspection {
    @Expose
    private String actID;

    @Expose
    private String listObject;
    @Expose
    private String objectID;

    @Expose
    private String name;

    @Expose
    private String currentPhotoPath;
    private String currentPhotoPathFTP;

    public PhotoActInspection() {
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

    public String getActID() {
        return actID;
    }

    public void setActID(String actID) {
        this.actID = actID;
    }

    public String getListObject() {
        return listObject;
    }

    public void setListObject(String listObject) {
        this.listObject = listObject;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getCurrentPhotoPathFTP() {
        return currentPhotoPathFTP;
    }

    public void setCurrentPhotoPathFTP(String currentPhotoPathFTP) {
        this.currentPhotoPathFTP = currentPhotoPathFTP;
    }
}
