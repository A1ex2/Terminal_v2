package ua.org.algoritm.terminal.DataBase;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.kobjects.base64.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import ua.org.algoritm.terminal.MainActivity;
import ua.org.algoritm.terminal.Objects.CarData;
import ua.org.algoritm.terminal.Objects.CarDataIssuance;
import ua.org.algoritm.terminal.Objects.CarDataOutfit;
import ua.org.algoritm.terminal.Objects.Issuance;
import ua.org.algoritm.terminal.Objects.OrderOutfit;
import ua.org.algoritm.terminal.Objects.Photo;
import ua.org.algoritm.terminal.Objects.Reception;
import ua.org.algoritm.terminal.Objects.Sector;
import ua.org.algoritm.terminal.Objects.User;

public class SharedData {
    public static ArrayList<Sector> SECTORS = new ArrayList<>();
    public static ArrayList<User> USERS = new ArrayList<>();
    public static ArrayList<Reception> RECEPTION = new ArrayList<>();
    public static ArrayList<Issuance> ISSUANCE = new ArrayList<>();
    public static ArrayList<OrderOutfit> ORDER_OUTFIT = new ArrayList<>();
    public static MainActivity app;
    public static String LOGIN;
    public static String PASSWORD;
    public static String API;
    public static String VERSION;

    public static String hostFTP;
    public static int portFTP;
    public static String usernameFTP;
    public static String passwordFTP;

    public static void updateReception(CarData carData) {
        boolean mFinish = false;
        for (int i = 0; i < RECEPTION.size(); i++) {
            Reception mReception = RECEPTION.get(i);
            if (mReception.getID().equals(carData.getReceptionID())) {
                ArrayList<CarData> carDataArrayList = mReception.getCarData();
                for (int j = 0; j < carDataArrayList.size(); j++) {
                    CarData mCarData = carDataArrayList.get(j);
                    if (mCarData.getCarID().equals(carData.getCarID())) {
                        mCarData.setBarCode(carData.getBarCode());
                        mCarData.setProductionDate(carData.getProductionDate());
                        mCarData.setSector(carData.getSector());
                        mCarData.setSectorID(carData.getSectorID());
                        mCarData.setRow(carData.getRow());

                        mFinish = true;
                        break;
                    }
                }
            }

            if (mFinish) {
                break;
            }
        }
    }

    public static void updateReceptionsDB() {
        DataBaseHelper helper = new DataBaseHelper(app);
        ArrayList<CarData> carDataArrayList = helper.getCarDataList();

        for (int i = 0; i < carDataArrayList.size(); i++) {
            CarData carData = carDataArrayList.get(i);
            if (!updateReceptionDB(carData)) {
                // delete
                helper.deleteCarData(carData);
            }

        }
    }

    public static boolean updateReceptionDB(CarData carData) {
        boolean mFinish = false;
        for (int i = 0; i < RECEPTION.size(); i++) {
            Reception mReception = RECEPTION.get(i);
            if (mReception.getID().equals(carData.getReceptionID())) {
                ArrayList<CarData> carDataArrayList = mReception.getCarData();
                for (int j = 0; j < carDataArrayList.size(); j++) {
                    CarData mCarData = carDataArrayList.get(j);
                    if (mCarData.getCarID().equals(carData.getCarID())) {
                        mCarData.setBarCode(carData.getBarCode());
                        mCarData.setProductionDate(carData.getProductionDate());
                        mCarData.setSector(getSector(carData.getSectorID()).getName());
                        mCarData.setSectorID(carData.getSectorID());
                        mCarData.setRow(carData.getRow());

                        mFinish = true;
                        break;
                    }
                }
            }

            if (mFinish) {
                break;
            }
        }

        return mFinish;
    }

    public static Reception getReception(String id) {
        Reception reception = new Reception();
        for (int i = 0; i < RECEPTION.size(); i++) {
            Reception mReception = RECEPTION.get(i);
            if (mReception.getID().equals(id)) {
                reception = mReception;
                break;
            }

        }
        return reception;
    }

    public static Issuance getIssuance(String id) {
        Issuance issuance = new Issuance();
        for (int i = 0; i < ISSUANCE.size(); i++) {
            Issuance mIssuance = ISSUANCE.get(i);
            if (mIssuance.getID().equals(id)) {
                issuance = mIssuance;
                break;
            }

        }
        return issuance;
    }

    public static Sector getSector(String id) {
        Sector sector = new Sector();
        for (int i = 0; i < SECTORS.size(); i++) {
            Sector mSector = SECTORS.get(i);
            if (mSector.getID().equals(id)) {
                sector = mSector;
                break;
            }

        }
        return sector;
    }

    public static void deleteCarData(String carID, Reception reception) {
        ArrayList<CarData> carDataArrayList = reception.getCarData();
        for (int i = 0; i < carDataArrayList.size(); i++) {
            CarData mCarData = carDataArrayList.get(i);
            if (mCarData.getCarID().equals(carID)) {
                carDataArrayList.remove(i);
                break;
            }
        }
    }

    public static void deleteCarDataIssuance(String carID, Issuance issuance) {
        ArrayList<CarDataIssuance> carDataArrayList = issuance.getCarData();
        for (int i = 0; i < carDataArrayList.size(); i++) {
            CarDataIssuance mCarData = carDataArrayList.get(i);
            if (mCarData.getCarID().equals(carID)) {
                carDataArrayList.remove(i);
                break;
            }
        }
    }

    public static String clearBarcode(String tBarCode) {
        tBarCode = tBarCode.replace("*", "");
        tBarCode = tBarCode.replace(":", "");
        tBarCode = tBarCode.replace(";", "");
        tBarCode = tBarCode.replace("-", "");
        tBarCode = tBarCode.replace(" ", "");
        tBarCode = tBarCode.replace(".", "");
        tBarCode = tBarCode.replace("/", "");
        tBarCode = tBarCode.replace("_", "");

        return tBarCode;
    }

    public static boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Sector> getSectorIssuanceMoving() {
        ArrayList<Sector> mSectors = new ArrayList<>();

        for (int i = 0; i < SECTORS.size(); i++) {

            Sector mSector = SECTORS.get(i);
            if (mSector.getName().equals("Bufer")) {
                mSectors.add(mSector);
            }
        }

        return mSectors;
    }

    public static OrderOutfit getOrderOutfit(String id) {
        OrderOutfit orderOutfit = new OrderOutfit();
        for (int i = 0; i < ORDER_OUTFIT.size(); i++) {
            OrderOutfit mOrderOutfit = ORDER_OUTFIT.get(i);
            if (mOrderOutfit.getID().equals(id)) {
                orderOutfit = mOrderOutfit;
                break;
            }

        }
        return orderOutfit;

    }

    public static CarDataOutfit getCarOrderOutfit(String orderID, String carID) {
        CarDataOutfit carActivityOrderOutfit = new CarDataOutfit();

        OrderOutfit orderOutfit = getOrderOutfit(orderID);
        ArrayList<CarDataOutfit> carActivityOrderOutfits = orderOutfit.getCarDataOutfit();
        for (int i = 0; i < carActivityOrderOutfits.size(); i++) {
            CarDataOutfit mCarDataOutfit = carActivityOrderOutfits.get(i);
            if (mCarDataOutfit.getCarID().equals(carID)) {
                carActivityOrderOutfit = mCarDataOutfit;
                break;
            }
        }
        return carActivityOrderOutfit;
    }

//    public static void insertPhotoCarDataOutfit(String orderID, String carID, String currentPhotoPath) {
//        DataBaseHelper helper = new DataBaseHelper(app);
//
//        long id = helper.insertPhotoCarDataOutfit(orderID, carID, currentPhotoPath);
//        if (id == 0 ){
//
//        }
//    }

    public static void setPhoto(OrderOutfit orderOutfit) {
        DataBaseHelper helper = new DataBaseHelper(app);
        ArrayList<CarDataOutfit> mCarDataOutfits = orderOutfit.getCarDataOutfit();
        for (int i = 0; i < mCarDataOutfits.size(); i++) {
            CarDataOutfit mCar = mCarDataOutfits.get(i);
            mCar.setPhoto(helper.getPhotoList(orderOutfit.getID(), mCar.getCarID()));
        }
    }

    public static void deletePhoto(String currentPhotoPath) {
        try {
            DataBaseHelper helper = new DataBaseHelper(app);

            helper.deletePhoto(currentPhotoPath);
            File mFile = new File(currentPhotoPath);
            if (mFile.delete()) {
            }
        } catch (Exception e) {

        }
    }

    public static void checkPhoto(ArrayList<OrderOutfit> orderOutfits) {
        DataBaseHelper helper = new DataBaseHelper(app);
        ArrayList<Photo> mPhotos = helper.getPhotoAll();
        for (int i = 0; i < mPhotos.size(); i++) {
            Photo mPhoto = mPhotos.get(i);
            for (int j = 0; j <orderOutfits.size(); j++) {
                if (orderOutfits.get(j).getID().equals(mPhoto.getOrderID())){
                    break;
                } else {
                    try {
                        deletePhoto(mPhoto.getCurrentPhotoPath());
                    } catch (Exception e) {

                    }
                }
            }
        }
    }

    public static String toBase64(String currentPhotoPath) {
        String strFile = "";

        File file = new File(currentPhotoPath);

        try {

            byte[] bytesArray = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(bytesArray);
            fis.close();

            strFile = Base64.encode(bytesArray);

        } catch (IOException e) {

            e.printStackTrace();

        }

        return strFile;
    }

    public static byte[] getByte(String currentPhotoPath) {
        File file = new File(currentPhotoPath);
        byte[] bytesArray = new byte[(int) file.length()];

        try {

            FileInputStream fis = new FileInputStream(file);
            fis.read(bytesArray);
            fis.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return bytesArray;
    }
}