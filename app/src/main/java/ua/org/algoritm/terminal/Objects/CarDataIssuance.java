package ua.org.algoritm.terminal.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CarDataIssuance implements Parcelable {
    private String issuanceID;

    private String Description;
    private String AutoNumber;
    private String Driver;
    private String DriverPhone;

    private String carID;
    private String car;
    private String sectorID;
    private String sector;
    private String row;

    public CarDataIssuance() {
    }


    @Override
    public String toString() {
        return "" + Description + " "
                + AutoNumber + " "
                + Driver + " "
                + DriverPhone + " "
                + carID + " "
                + car + " "
                + sectorID + " "
                + sector + row + " "
                + sector + " " + row;
    }

    public String getIssuanceID() {
        return issuanceID;
    }

    public void setIssuanceID(String issuanceID) {
        this.issuanceID = issuanceID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAutoNumber() {
        return AutoNumber;
    }

    public void setAutoNumber(String autoNumber) {
        AutoNumber = autoNumber;
    }

    public String getDriver() {
        return Driver;
    }

    public void setDriver(String driver) {
        Driver = driver;
    }

    public String getDriverPhone() {
        return DriverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        DriverPhone = driverPhone;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.issuanceID);
        dest.writeString(this.Description);
        dest.writeString(this.AutoNumber);
        dest.writeString(this.Driver);
        dest.writeString(this.DriverPhone);
        dest.writeString(this.carID);
        dest.writeString(this.car);
        dest.writeString(this.sectorID);
        dest.writeString(this.sector);
        dest.writeString(this.row);
    }

    protected CarDataIssuance(Parcel in) {
        this.issuanceID = in.readString();
        this.Description = in.readString();
        this.AutoNumber = in.readString();
        this.Driver = in.readString();
        this.DriverPhone = in.readString();
        this.carID = in.readString();
        this.car = in.readString();
        this.sectorID = in.readString();
        this.sector = in.readString();
        this.row = in.readString();
    }

    public static final Creator<CarDataIssuance> CREATOR = new Creator<CarDataIssuance>() {
        @Override
        public CarDataIssuance createFromParcel(Parcel source) {
            return new CarDataIssuance(source);
        }

        @Override
        public CarDataIssuance[] newArray(int size) {
            return new CarDataIssuance[size];
        }
    };
}
