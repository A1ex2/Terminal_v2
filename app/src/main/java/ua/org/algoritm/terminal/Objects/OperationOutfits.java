package ua.org.algoritm.terminal.Objects;

import com.google.gson.annotations.SerializedName;

public class OperationOutfits {
    @SerializedName("OperationID")
    private String OperationID;

    @SerializedName("Operation")
    private String Operation;

    @SerializedName("Performed")
    private Boolean Performed = false;

    public OperationOutfits() {
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
}
