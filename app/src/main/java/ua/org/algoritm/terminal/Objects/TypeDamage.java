package ua.org.algoritm.terminal.Objects;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class TypeDamage implements Serializable {
    @Expose
    private String ID;
    private String name;

    public TypeDamage() {
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
