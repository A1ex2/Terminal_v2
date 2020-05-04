package ua.org.algoritm.terminal.Objects;

import com.google.gson.annotations.Expose;

public class Detail {
    @Expose
    private String ID;

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
}
