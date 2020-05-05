package ua.org.algoritm.terminal.Objects;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class TypeDamagePhoto {
    @Expose
    private String ID;
    private String name;

    @Expose
    private String listObject = "Photo";

    @Expose
    private ArrayList<PhotoActInspection> mPhotoActInspections = new ArrayList<>();

    public TypeDamagePhoto() {
    }

    public TypeDamagePhoto(TypeDamagePhoto typeDamagePhoto) {
        this.ID = typeDamagePhoto.getID();
        this.name = typeDamagePhoto.getName();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
