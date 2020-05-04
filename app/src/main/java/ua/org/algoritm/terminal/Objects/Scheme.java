package ua.org.algoritm.terminal.Objects;

public class Scheme {
    private String ID;
    private String Name;
    private String TypeMachineID;
    private String TypeMachine;
    private String ViewSchemesID;
    private String ViewSchemes;
    private String SVG;

    public Scheme() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTypeMachineID() {
        return TypeMachineID;
    }

    public void setTypeMachineID(String typeMachineID) {
        TypeMachineID = typeMachineID;
    }

    public String getTypeMachine() {
        return TypeMachine;
    }

    public void setTypeMachine(String typeMachine) {
        TypeMachine = typeMachine;
    }

    public String getViewSchemesID() {
        return ViewSchemesID;
    }

    public void setViewSchemesID(String viewSchemesID) {
        ViewSchemesID = viewSchemesID;
    }

    public String getViewSchemes() {
        return ViewSchemes;
    }

    public void setViewSchemes(String viewSchemes) {
        ViewSchemes = viewSchemes;
    }

    public String getSVG() {
        return SVG;
    }

    public void setSVG(String SVG) {
        this.SVG = SVG;
    }
}
