package ua.org.algoritm.terminal.Objects;

import java.util.ArrayList;

public class TypesPhoto {
    private String typePhotoID;
    private String typePhoto;

    private String listObject = "Photo";

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
