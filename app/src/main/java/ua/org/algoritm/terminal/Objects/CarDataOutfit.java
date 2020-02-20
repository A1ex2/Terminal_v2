package ua.org.algoritm.terminal.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CarDataOutfit {
    @SerializedName("carID")
    private String carID;

    @SerializedName("car")
    private String car;

    @SerializedName("barCode")
    private String barCode;

    @SerializedName("sectorID")
    private String sectorID;

    @SerializedName("sector")
    private String sector;

    @SerializedName("row")
    private String row;

    @SerializedName("Operations")
    private ArrayList<OperationOutfits> Operations = new ArrayList<>();

    @SerializedName("Photo")
    private ArrayList<Photo> Photo = new ArrayList<>();

    public CarDataOutfit() {
    }

    public ArrayList<OperationOutfits> getOperations() {
        return Operations;
    }

    public void setOperations(ArrayList<OperationOutfits> operations) {
        Operations = operations;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getSectorID() {
        return sectorID;
    }

    public void setSectorID(String sectorID) {
        this.sectorID = sectorID;
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

    public ArrayList<Photo> getPhoto() {
        return Photo;
    }

    public void setPhoto(ArrayList<Photo> photo) {
        Photo = photo;
    }

    @Override
    public String toString() {
        return "" + car + " "
                + barCode + " "
                + sector + " "
                + row;
    }
}
