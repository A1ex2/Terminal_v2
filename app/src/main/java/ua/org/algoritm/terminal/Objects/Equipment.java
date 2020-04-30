package ua.org.algoritm.terminal.Objects;

public class Equipment {
    private String equipmentID;
    private String equipment;
    private int quantityPlan;
    private int quantityFact;
    private String namePhoto;
    private String currentPhotoPathFTP;

    public Equipment() {
    }

    public String getEquipmentID() {
        return equipmentID;
    }

    public void setEquipmentID(String equipmentID) {
        this.equipmentID = equipmentID;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public int getQuantityPlan() {
        return quantityPlan;
    }

    public void setQuantityPlan(int quantityPlan) {
        this.quantityPlan = quantityPlan;
    }

    public int getQuantityFact() {
        return quantityFact;
    }

    public void setQuantityFact(int quantityFact) {
        this.quantityFact = quantityFact;
    }

    public String getNamePhoto() {
        return namePhoto;
    }

    public void setNamePhoto(String namePhoto) {
        this.namePhoto = namePhoto;
    }

    public String getCurrentPhotoPathFTP() {
        return currentPhotoPathFTP;
    }

    public void setCurrentPhotoPathFTP(String currentPhotoPathFTP) {
        this.currentPhotoPathFTP = currentPhotoPathFTP;
    }
}
