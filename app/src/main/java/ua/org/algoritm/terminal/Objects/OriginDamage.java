package ua.org.algoritm.terminal.Objects;

import com.google.gson.annotations.Expose;

public class OriginDamage {
    @Expose
    private String ID;
    private String name;

    public OriginDamage() {
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

    @Override
    public String toString() {
        return "" + name;
    }
}
