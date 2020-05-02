package ua.org.algoritm.terminal.Objects;

public class TypesPhoto {
    private String typePhotoID;
    private String typePhoto;

    private String listObject = "Photo";

    private PhotoActInspection mPhotoActInspection = new PhotoActInspection();

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

    public PhotoActInspection getPhotoActInspection() {
        return mPhotoActInspection;
    }

    public void setPhotoActInspection(PhotoActInspection photoActInspection) {
        mPhotoActInspection = photoActInspection;
    }
}
