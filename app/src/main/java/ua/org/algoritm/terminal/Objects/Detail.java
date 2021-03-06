package ua.org.algoritm.terminal.Objects;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Detail implements Serializable {
    @Expose
    private String ID;
    private int tempID;

    @Expose
    private String detailID;
    private String detailName;

    public Detail() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getTempID() {
        return tempID;
    }

    public void setTempID(int tempID) {
        this.tempID = tempID;
    }

    public String getDetailID() {
        return detailID;
    }

    public void setDetailID(String detailID) {
        this.detailID = detailID;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    @Override
    public String toString() {
        return "" + tempID + " - " + detailName;
    }
}
