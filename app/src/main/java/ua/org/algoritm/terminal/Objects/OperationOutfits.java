package ua.org.algoritm.terminal.Objects;

import com.google.gson.annotations.SerializedName;

public class OperationOutfits {
    @SerializedName("OperationID")
    private String OperationID;

    @SerializedName("Operation")
    private String Operation;

    @SerializedName("Performed")
    private Boolean Performed = false;

    @SerializedName("QuantityPhoto")
    private int QuantityPhoto = 0;

    @SerializedName("Description")
    private String Description = "";

    public OperationOutfits() {
    }

    public OperationOutfits(OperationOutfits mOperationOutfits) {
        this.OperationID = mOperationOutfits.getOperationID();
        this.Operation = mOperationOutfits.getOperation();
        this.Performed = mOperationOutfits.getPerformed();
        this.QuantityPhoto = mOperationOutfits.getQuantityPhoto();
        this.Description = mOperationOutfits.getDescription();
    }

    public String getOperationID() {
        return OperationID;
    }

    public void setOperationID(String operationID) {
        OperationID = operationID;
    }

    public String getOperation() {
        return Operation;
    }

    public void setOperation(String operation) {
        Operation = operation;
    }

    public Boolean getPerformed() {
        return Performed;
    }

    public void setPerformed(Boolean performed) {
        Performed = performed;
    }

    public int getQuantityPhoto() {
        return QuantityPhoto;
    }

    public void setQuantityPhoto(int quantityPhoto) {
        QuantityPhoto = quantityPhoto;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
