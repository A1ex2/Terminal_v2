package ua.org.algoritm.terminal.Objects;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class TypeDamagePhoto {
    @Expose
    private String ID;
    private String name;

  public TypeDamagePhoto() {
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
