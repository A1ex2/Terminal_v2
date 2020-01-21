package ua.org.algoritm.terminal.DataBase;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;

import ua.org.algoritm.terminal.MainActivity;
import ua.org.algoritm.terminal.Objects.CarData;
import ua.org.algoritm.terminal.Objects.Reception;
import ua.org.algoritm.terminal.Objects.Sector;
import ua.org.algoritm.terminal.Objects.User;
import ua.org.algoritm.terminal.R;

public class SharedData {
    public static ArrayList<Sector> SECTORS = new ArrayList<>();
    public static ArrayList<User> USERS = new ArrayList<>();
    public static ArrayList<Reception> RECEPTION = new ArrayList<>();
    public static MainActivity app;
    public static String LOGIN;
    public static String PASSWORD;
    public static String API;
    public static String VERSION;


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

    public static String clearBarcode(String tBarCode){
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

    public static boolean isOnline(Context context){
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
}