package ua.org.algoritm.terminal.Objects;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderOutfit {
    @SerializedName("ID")
    private String ID;

    @SerializedName("Description")
    private String Description;

    @SerializedName("ResponsibleID")
    private String ResponsibleID;

    @SerializedName("Responsible")
    private String Responsible;

    @SerializedName("StateID")
    private String StateID;

    @SerializedName("State")
    private String State;

    @SerializedName("CarDataOutfit")
    private ArrayList<CarDataOutfit> CarDataOutfit = new ArrayList<>();

    public OrderOutfit() {

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getResponsibleID() {
        return ResponsibleID;
    }

    public void setResponsibleID(String responsibleID) {
        ResponsibleID = responsibleID;
    }

    public String getResponsible() {
        return Responsible;
    }

    public void setResponsible(String responsible) {
        Responsible = responsible;
    }

    public String getStateID() {
        return StateID;
    }

    public void setStateID(String stateID) {
        StateID = stateID;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public ArrayList<CarDataOutfit> getCarDataOutfit() {
        return CarDataOutfit;
    }

    public void setCarDataOutfit(ArrayList<CarDataOutfit> carDataOutfit) {
        CarDataOutfit = carDataOutfit;
    }
}
