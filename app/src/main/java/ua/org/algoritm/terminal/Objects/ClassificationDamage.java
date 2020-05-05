package ua.org.algoritm.terminal.Objects;

import com.google.gson.annotations.Expose;

public class ClassificationDamage {
    @Expose
    private String ID;
    private String name;

    public ClassificationDamage() {
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
}
