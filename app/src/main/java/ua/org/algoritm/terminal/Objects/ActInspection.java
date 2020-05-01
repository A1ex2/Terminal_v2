package ua.org.algoritm.terminal.Objects;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActInspection {
    private String ID;
    private String description;

    private String stateID;
    private String state;

    private String formID;
    private String form;

    private String storageID;
    private String storage;

    private Date inspectionDatePlan;
    private Date inspectionDateFact;

    private String carID;
    private String car;

    private String productionDate;

    private String barCode;

    private String sectorID;
    private String sector;
    private String row;

    private ArrayList<Equipment> Equipments = new ArrayList<>();
    private ArrayList<Inspection> mInspections = new ArrayList<>();

    public ActInspection() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStateID() {
        return stateID;
    }

    public void setStateID(String stateID) {
        this.stateID = stateID;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getStorageID() {
        return storageID;
    }

    public void setStorageID(String storageID) {
        this.storageID = storageID;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public Date getInspectionDatePlan() {
        return inspectionDatePlan;
    }

    public void setInspectionDatePlan(Date inspectionDatePlan) {
        this.inspectionDatePlan = inspectionDatePlan;
    }

    public void setInspectionDatePlan(String string) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            if (string.equals("")) {
                string = "0001-01-01T00:00:00";
            }

            Date date = format.parse(string);
            this.inspectionDatePlan = date;

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getInspectionDateFact() {
        return inspectionDateFact;
    }

    public void setInspectionDateFact(Date inspectionDateFact) {
        this.inspectionDateFact = inspectionDateFact;
    }

    public void setInspectionDateFact(String string) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            if (string.equals("")) {
                string = "0001-01-01T00:00:00";
            }

            Date date = format.parse(string);
            this.inspectionDateFact = date;

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getSectorID() {
        return sectorID;
    }

    public void setSectorID(String sectorID) {
        this.sectorID = sectorID;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public ArrayList<Equipment> getEquipments() {
        return Equipments;
    }

    public void setEquipments(ArrayList<Equipment> equipments) {
        Equipments = equipments;
    }

    public String getInspectionDatePlanString() {
        String date = "";
        if (inspectionDatePlan.getTime() > 0) {
            String pattern = "dd.MM.yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            date = df.format(inspectionDatePlan);
        }
        return date;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public ArrayList<Inspection> getInspections() {
        return mInspections;
    }

    public void setInspections(ArrayList<Inspection> inspections) {
        mInspections = inspections;
    }
}
