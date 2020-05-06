package ua.org.algoritm.terminal.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CarData implements Parcelable {
  private String ReceptionID;
  private String carID;
  private String car;
  private String barCode;
  private String sectorID;
  private String sector;
  private String row;
  private Date productionDate;

  public boolean saveCB = false;

  public CarData() {
  }

  public String getCarID() {
    return carID;
  }

  public void setCarID(String carID) {
    this.carID = carID;
  }

  public String getReceptionID() {
    return ReceptionID;
  }

  public void setReceptionID(String receptionID) {
    ReceptionID = receptionID;
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

  public Date getProductionDate() {
    return productionDate;
  }

  public String getProductionDateString() {
    String date = "";
    if (productionDate.getTime() > 0) {
      String pattern = "dd.MM.yyyy";
      DateFormat df = new SimpleDateFormat(pattern);
      date = df.format(productionDate);
    }
    return date;
  }

  public void setProductionDate(Date productionDate) {
    this.productionDate = productionDate;
  }

  public void setProductionDate(String string) throws ParseException {
    DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    if (string.equals("")){
      string = "01.01.0001";
    }

    Date date = format.parse(string);
    this.productionDate = date;
  }

  @Override
  public String toString() {
    return "" + car + " "
            + barCode + " "
            + sector + " "
            + row + " "
            + getProductionDateString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.ReceptionID);
    dest.writeString(this.carID);
    dest.writeString(this.car);
    dest.writeString(this.barCode);
    dest.writeString(this.sectorID);
    dest.writeString(this.sector);
    dest.writeString(this.row);
    dest.writeLong(this.productionDate != null ? this.productionDate.getTime() : -1);
    dest.writeByte(this.saveCB ? (byte) 1 : (byte) 0);
  }

  protected CarData(Parcel in) {
    this.ReceptionID = in.readString();
    this.carID = in.readString();
    this.car = in.readString();
    this.barCode = in.readString();
    this.sectorID = in.readString();
    this.sector = in.readString();
    this.row = in.readString();
    long tmpProductionDate = in.readLong();
    this.productionDate = tmpProductionDate == -1 ? null : new Date(tmpProductionDate);
    this.saveCB = in.readByte() != 0;
  }

  public static final Creator<CarData> CREATOR = new Creator<CarData>() {
    @Override
    public CarData createFromParcel(Parcel source) {
      return new CarData(source);
    }

    @Override
    public CarData[] newArray(int size) {
      return new CarData[size];
    }
  };
}

