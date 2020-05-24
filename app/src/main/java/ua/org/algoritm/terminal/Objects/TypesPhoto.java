package ua.org.algoritm.terminal.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class TypesPhoto implements Serializable{
    @Expose
    private String typePhotoID;
    private String typePhoto;

    @Expose
    private String listObject = "Photo";

    @Expose
    private ArrayList<PhotoActInspection> mPhotoActInspections = new ArrayList<>();

    public TypesPhoto() {
    }

    public String getTypePhotoID() {
        return typePhotoID;
    }

    public void setTypePhotoID(String typePhotoID) {
        this.typePhotoID = typePhotoID;
    }

    public String getTypePhoto() {
        return typePhoto;
    }

    public void setTypePhoto(String typePhoto) {
        this.typePhoto = typePhoto;
    }

    public String getListObject() {
        return listObject;
    }

    public void setListObject(String listObject) {
        this.listObject = listObject;
    }

    public ArrayList<PhotoActInspection> getPhotoActInspections() {
        return mPhotoActInspections;
    }

    public void setPhotoActInspections(ArrayList<PhotoActInspection> photoActInspections) {
        mPhotoActInspections = photoActInspections;
    }
}
