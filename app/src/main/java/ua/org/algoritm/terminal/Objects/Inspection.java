package ua.org.algoritm.terminal.Objects;

public class Inspection {
    private String ID;
    private String name;
    private boolean performed;

    public Inspection() {
    }

    public Inspection(Inspection inspection) {
        this.ID = inspection.getID();
        this.name = inspection.getName();
        this.performed = inspection.isPerformed();
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

    public boolean isPerformed() {
        return performed;
    }

    public void setPerformed(boolean performed) {
        this.performed = performed;
    }
}
