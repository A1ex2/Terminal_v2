package ua.org.algoritm.terminal.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class Issuance implements Parcelable {

    private String ID;
    private String Description;
    private String AutoNumber;
    private String Driver;
    private String DriverPhone;
    private String InvoiceNumber;
    private ArrayList<CarDataIssuance> CarData = new ArrayList<>();

    public Issuance() {
    }

    public Issuance(Issuance reception) {
        ID = reception.getID();
        Description = reception.getDescription();
        AutoNumber = reception.getAutoNumber();
        Driver = reception.getDriver();
        DriverPhone = reception.getDriverPhone();
        InvoiceNumber = reception.getInvoiceNumber();
        CarData = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Reception{" +
                "Description='" + Description + '\'' +
                '}';
    }

    public ArrayList<CarDataIssuance> getCarData() {
        return CarData;
    }

    public void setCarData(ArrayList<CarDataIssuance> carData) {
        CarData = carData;
    }

    public String getID() {
        return ID;
    }

    public String getDescription() {
        return Description;
    }

    public String getAutoNumber() {
        return AutoNumber;
    }

    public String getDriver() {
        return Driver;
    }

    public String getDriverPhone() {
        return DriverPhone;
    }

    public String getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setAutoNumber(String autoNumber) {
        AutoNumber = autoNumber;
    }

    public void setDriver(String driver) {
        Driver = driver;
    }

    public void setDriverPhone(String driverPhone) {
        DriverPhone = driverPhone;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        InvoiceNumber = invoiceNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ID);
        dest.writeString(this.Description);
        dest.writeString(this.AutoNumber);
        dest.writeString(this.Driver);
        dest.writeString(this.DriverPhone);
        dest.writeString(this.InvoiceNumber);
        dest.writeList(this.CarData);
    }

    protected Issuance(Parcel in) {
        this.ID = in.readString();
        this.Description = in.readString();
        this.AutoNumber = in.readString();
        this.Driver = in.readString();
        this.DriverPhone = in.readString();
        this.InvoiceNumber = in.readString();
        this.CarData = new ArrayList<CarDataIssuance>();
        in.readList(this.CarData, CarDataIssuance.class.getClassLoader());
    }

    public static final Creator<Issuance> CREATOR = new Creator<Issuance>() {
        @Override
        public Issuance createFromParcel(Parcel source) {
            return new Issuance(source);
        }

        @Override
        public Issuance[] newArray(int size) {
            return new Issuance[size];
        }
    };
}
