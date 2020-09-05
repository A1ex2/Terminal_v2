package ua.org.algoritm.terminal.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ua.org.algoritm.terminal.Objects.ActInspection;
import ua.org.algoritm.terminal.Objects.CarData;
import ua.org.algoritm.terminal.Objects.CarDataOutfit;
import ua.org.algoritm.terminal.Objects.ClassificationDamage;
import ua.org.algoritm.terminal.Objects.Damage;
import ua.org.algoritm.terminal.Objects.DegreesDamage;
import ua.org.algoritm.terminal.Objects.Detail;
import ua.org.algoritm.terminal.Objects.Equipment;
import ua.org.algoritm.terminal.Objects.Inspection;
import ua.org.algoritm.terminal.Objects.OriginDamage;
import ua.org.algoritm.terminal.Objects.Photo;
import ua.org.algoritm.terminal.Objects.PhotoActInspection;
import ua.org.algoritm.terminal.Objects.Reception;
import ua.org.algoritm.terminal.Objects.Scheme;
import ua.org.algoritm.terminal.Objects.Sector;
import ua.org.algoritm.terminal.Objects.TypeDamage;
import ua.org.algoritm.terminal.Objects.TypeDamagePhoto;
import ua.org.algoritm.terminal.Objects.TypesPhoto;
import ua.org.algoritm.terminal.Objects.User;

import java.io.File;
import java.util.ArrayList;


public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context) {
        super(context, "MyBD.db", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE CarData ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ReceptionID TEXT NOT NULL,"
                + "carID TEXT NOT NULL,"
                + "barCode TEXT NOT NULL,"
                + "sectorID TEXT NOT NULL,"
                + "mRow INTEGER NOT NULL,"
                + "productionDate TEXT NOT NULL)");

        db.execSQL("CREATE TABLE CarDataOutfitPhoto ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "orderID TEXT NOT NULL,"
                + "carID TEXT NOT NULL,"
                + "currentPhotoPath TEXT NOT NULL)");

        db.execSQL("CREATE TABLE ActInspectionPhoto ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "actID TEXT NOT NULL,"
                + "listObject TEXT NOT NULL,"
                + "objectID TEXT NOT NULL,"
                + "currentPhotoPath TEXT NOT NULL)");

        db.execSQL("CREATE TABLE Receptions ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ID TEXT NOT NULL,"
                + "description TEXT NOT NULL,"
                + "autoNumber TEXT NOT NULL,"
                + "driver TEXT NOT NULL,"
                + "driverPhone TEXT NOT NULL,"
                + "invoiceNumber TEXT NOT NULL)");

        db.execSQL("CREATE TABLE CarDataReceptions ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ReceptionID TEXT NOT NULL,"
                + "carID TEXT NOT NULL,"
                + "car TEXT NOT NULL,"
                + "barCode TEXT NOT NULL,"
                + "sectorID TEXT NOT NULL,"
                + "sector TEXT NOT NULL,"
                + "_row TEXT NOT NULL,"
                + "productionDate TEXT NOT NULL)");

        db.execSQL("CREATE TABLE ActInspection ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ReceptionID TEXT NOT NULL,"
                + "ID TEXT NOT NULL,"
                + "description TEXT NOT NULL,"
                + "stateID TEXT NOT NULL,"
                + "state TEXT NOT NULL,"
                + "formID TEXT NOT NULL,"
                + "form TEXT NOT NULL,"
                + "truckPosition INTEGER NOT NULL,"
                + "truckPositionDirection TEXT NOT NULL,"
                + "run TEXT NOT NULL,"
                + "storageID TEXT NOT NULL,"
                + "storage TEXT NOT NULL,"
                + "inspectionDatePlan TEXT NOT NULL,"
                + "inspectionDateFact TEXT NOT NULL,"
                + "carID TEXT NOT NULL,"
                + "car TEXT NOT NULL,"
                + "productionDate TEXT NOT NULL,"
                + "barCode TEXT NOT NULL,"
                + "sectorID TEXT NOT NULL,"
                + "sector TEXT NOT NULL,"
                + "_row TEXT NOT NULL,"
                + "TypeMachineID TEXT NOT NULL,"
                + "TypeMachine TEXT NOT NULL,"
                + "sendPerformed INTEGER NOT NULL,"
                + "performed INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE Equipment ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ActID TEXT NOT NULL,"
                + "equipmentID TEXT NOT NULL,"
                + "equipment TEXT NOT NULL,"
                + "listObject TEXT NOT NULL,"
                + "quantityPlan INTEGER NOT NULL,"
                + "quantityFact INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE Inspection ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ActID TEXT NOT NULL,"
                + "ID TEXT NOT NULL,"
                + "name TEXT NOT NULL,"
                + "performed INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE TypesPhoto ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ActID TEXT NOT NULL,"
                + "typePhotoID TEXT NOT NULL,"
                + "typePhoto TEXT NOT NULL,"
                + "listObject TEXT NOT NULL)");

        db.execSQL("CREATE TABLE Damage ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ActID TEXT NOT NULL,"
                + "detailID TEXT NOT NULL,"
                + "mTypeDamageID TEXT NOT NULL,"
                + "typeDetail TEXT NOT NULL,"
                + "mDegreesDamageID TEXT NOT NULL,"
                + "mClassificationDamageID TEXT NOT NULL,"
                + "mOriginDamageID TEXT NOT NULL,"
                + "detailDamage TEXT NOT NULL,"
                + "commentDamage TEXT NOT NULL,"
                + "widthDamage TEXT NOT NULL,"
                + "heightDamage TEXT NOT NULL)");

        db.execSQL("CREATE TABLE Scheme ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ID TEXT NOT NULL,"
                + "Name TEXT NOT NULL,"
                + "TypeMachineID TEXT NOT NULL,"
                + "TypeMachine TEXT NOT NULL,"
                + "ViewSchemesID TEXT NOT NULL,"
                + "ViewSchemes TEXT NOT NULL,"
                + "SVG TEXT NOT NULL)");

        db.execSQL("CREATE TABLE Detail ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "typeDetail TEXT NOT NULL,"
                + "ID TEXT NOT NULL,"
                + "tempID INTEGER NOT NULL,"
                + "detailID TEXT NOT NULL,"
                + "detailName TEXT NOT NULL)");

        db.execSQL("CREATE TABLE TypesDamages ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ID TEXT NOT NULL,"
                + "name TEXT NOT NULL)");

        db.execSQL("CREATE TABLE DegreesDamages ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ID TEXT NOT NULL,"
                + "name TEXT NOT NULL)");

        db.execSQL("CREATE TABLE ClassificationDamages ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ID TEXT NOT NULL,"
                + "name TEXT NOT NULL)");

        db.execSQL("CREATE TABLE OriginDamages ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ID TEXT NOT NULL,"
                + "name TEXT NOT NULL)");

        db.execSQL("CREATE TABLE TypeDamagePhotos ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ID TEXT NOT NULL,"
                + "name TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 2) {
            db.execSQL("CREATE TABLE CarDataOutfitPhoto ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "orderID TEXT NOT NULL,"
                    + "carID TEXT NOT NULL,"
                    + "currentPhotoPath TEXT NOT NULL)");
        }
        if (newVersion == 3) {
            db.execSQL("CREATE TABLE ActInspectionPhoto ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "actID TEXT NOT NULL,"
                    + "listObject TEXT NOT NULL,"
                    + "objectID TEXT NOT NULL,"
                    + "currentPhotoPath TEXT NOT NULL)");
        }

        if (newVersion == 4) {

            db.execSQL("CREATE TABLE Receptions ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "ID TEXT NOT NULL,"
                    + "description TEXT NOT NULL,"
                    + "autoNumber TEXT NOT NULL,"
                    + "driver TEXT NOT NULL,"
                    + "driverPhone TEXT NOT NULL,"
                    + "invoiceNumber TEXT NOT NULL)");

            db.execSQL("CREATE TABLE CarDataReceptions ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "ReceptionID TEXT NOT NULL,"
                    + "carID TEXT NOT NULL,"
                    + "car TEXT NOT NULL,"
                    + "barCode TEXT NOT NULL,"
                    + "sectorID TEXT NOT NULL,"
                    + "sector TEXT NOT NULL,"
                    + "_row TEXT NOT NULL,"
                    + "productionDate TEXT NOT NULL)");

            db.execSQL("CREATE TABLE ActInspection ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "ReceptionID TEXT NOT NULL,"
                    + "ID TEXT NOT NULL,"
                    + "description TEXT NOT NULL,"
                    + "stateID TEXT NOT NULL,"
                    + "state TEXT NOT NULL,"
                    + "formID TEXT NOT NULL,"
                    + "form TEXT NOT NULL,"
                    + "truckPosition INTEGER NOT NULL,"
                    + "truckPositionDirection TEXT NOT NULL,"
                    + "run TEXT NOT NULL,"
                    + "storageID TEXT NOT NULL,"
                    + "storage TEXT NOT NULL,"
                    + "inspectionDatePlan TEXT NOT NULL,"
                    + "inspectionDateFact TEXT NOT NULL,"
                    + "carID TEXT NOT NULL,"
                    + "car TEXT NOT NULL,"
                    + "productionDate TEXT NOT NULL,"
                    + "barCode TEXT NOT NULL,"
                    + "sectorID TEXT NOT NULL,"
                    + "sector TEXT NOT NULL,"
                    + "_row TEXT NOT NULL,"
                    + "TypeMachineID TEXT NOT NULL,"
                    + "TypeMachine TEXT NOT NULL,"
                    + "sendPerformed INTEGER NOT NULL,"
                    + "performed INTEGER NOT NULL)");

            db.execSQL("CREATE TABLE Equipment ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "ActID TEXT NOT NULL,"
                    + "equipmentID TEXT NOT NULL,"
                    + "equipment TEXT NOT NULL,"
                    + "listObject TEXT NOT NULL,"
                    + "quantityPlan INTEGER NOT NULL,"
                    + "quantityFact INTEGER NOT NULL)");

            db.execSQL("CREATE TABLE Inspection ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "ActID TEXT NOT NULL,"
                    + "ID TEXT NOT NULL,"
                    + "name TEXT NOT NULL,"
                    + "performed INTEGER NOT NULL)");

            db.execSQL("CREATE TABLE TypesPhoto ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "ActID TEXT NOT NULL,"
                    + "typePhotoID TEXT NOT NULL,"
                    + "typePhoto TEXT NOT NULL,"
                    + "listObject TEXT NOT NULL)");

            db.execSQL("CREATE TABLE Damage ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "ActID TEXT NOT NULL,"
                    + "detailID TEXT NOT NULL,"
                    + "mTypeDamageID TEXT NOT NULL,"
                    + "typeDetail TEXT NOT NULL,"
                    + "mDegreesDamageID TEXT NOT NULL,"
                    + "mClassificationDamageID TEXT NOT NULL,"
                    + "mOriginDamageID TEXT NOT NULL,"
                    + "detailDamage TEXT NOT NULL,"
                    + "commentDamage TEXT NOT NULL,"
                    + "widthDamage TEXT NOT NULL,"
                    + "heightDamage TEXT NOT NULL)");

            db.execSQL("CREATE TABLE Scheme ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "ID TEXT NOT NULL,"
                    + "Name TEXT NOT NULL,"
                    + "TypeMachineID TEXT NOT NULL,"
                    + "TypeMachine TEXT NOT NULL,"
                    + "ViewSchemesID TEXT NOT NULL,"
                    + "ViewSchemes TEXT NOT NULL,"
                    + "SVG TEXT NOT NULL)");

            db.execSQL("CREATE TABLE Detail ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "ID TEXT NOT NULL,"
                    + "tempID INTEGER NOT NULL,"
                    + "detailID TEXT NOT NULL,"
                    + "detailName TEXT NOT NULL)");

            db.execSQL("CREATE TABLE TypesDamages ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "ID TEXT NOT NULL,"
                    + "name TEXT NOT NULL)");

            db.execSQL("CREATE TABLE DegreesDamages ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "ID TEXT NOT NULL,"
                    + "name TEXT NOT NULL)");

            db.execSQL("CREATE TABLE ClassificationDamages ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "ID TEXT NOT NULL,"
                    + "name TEXT NOT NULL)");

            db.execSQL("CREATE TABLE OriginDamages ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "ID TEXT NOT NULL,"
                    + "name TEXT NOT NULL)");

            db.execSQL("CREATE TABLE TypeDamagePhotos ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "ID TEXT NOT NULL,"
                    + "name TEXT NOT NULL)");

        }
    }

    public void insertCarData(CarData carData) {
        SQLiteDatabase db = getReadableDatabase();
        long id = 0;

        try {
            CarData dbCarData = getCarData(carData.getReceptionID(), carData.getCarID());
            if ((dbCarData.getCarID() != null) && (dbCarData.getCarID().equals(carData.getCarID()) & dbCarData.getReceptionID().equals(carData.getReceptionID()))) {
                updateCarData(carData);
            } else {

                ContentValues values = new ContentValues();
                values.put("ReceptionID", carData.getReceptionID());
                values.put("carID", carData.getCarID());
                values.put("barCode", carData.getBarCode());
                values.put("sectorID", carData.getSectorID());
                values.put("mRow", carData.getRow());
                values.put("productionDate", carData.getProductionDateString());

                id = db.insert("CarData", null, values);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void updateCarData(CarData carData) {
        SQLiteDatabase db = getReadableDatabase();
        int id = 0;

        try {
            ContentValues values = new ContentValues();
            values.put("ReceptionID", carData.getReceptionID());
            values.put("carID", carData.getCarID());
            values.put("barCode", carData.getBarCode());
            values.put("sectorID", carData.getSectorID());
            values.put("mRow", carData.getRow());
            values.put("productionDate", carData.getProductionDateString());


//            String receptionID = "'" + carData.getReceptionID() + "'";
//            String carID = "'" + carData.getCarID() + "'";
            String receptionID = carData.getReceptionID();
            String carID = carData.getCarID();

            id = db.update("CarData", values, "ReceptionID=? and carID=?", new String[]{receptionID, carID});

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public CarData getCarData(String receptionID, String carID) {
        CarData carData = new CarData();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            String select = "ReceptionID = '" + receptionID + "' and carID = '" + carID + "'";
            cursor = db.query("CarData", null, select, null, null, null, null);

            if (cursor.moveToNext()) {

                carData.setReceptionID(cursor.getString(cursor.getColumnIndex("ReceptionID")));
                carData.setCarID(cursor.getString(cursor.getColumnIndex("carID")));
                carData.setBarCode(cursor.getString(cursor.getColumnIndex("barCode")));
                carData.setSectorID(cursor.getString(cursor.getColumnIndex("sectorID")));
                carData.setRow(cursor.getString(cursor.getColumnIndex("mRow")));
                carData.setProductionDate(cursor.getString(cursor.getColumnIndex("productionDate")));
                carData.setBarCode(cursor.getString(cursor.getColumnIndex("barCode")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        db.close();

        return carData;
    }

    public ArrayList<CarData> getCarDataList() {
        ArrayList<CarData> carDataArrayList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query("CarData", null, null, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    CarData carData = new CarData();

                    carData.setReceptionID(cursor.getString(cursor.getColumnIndex("ReceptionID")));
                    carData.setCarID(cursor.getString(cursor.getColumnIndex("carID")));
                    carData.setBarCode(cursor.getString(cursor.getColumnIndex("barCode")));
                    carData.setSectorID(cursor.getString(cursor.getColumnIndex("sectorID")));
                    carData.setRow(cursor.getString(cursor.getColumnIndex("mRow")));
                    carData.setProductionDate(cursor.getString(cursor.getColumnIndex("productionDate")));
                    carData.setBarCode(cursor.getString(cursor.getColumnIndex("barCode")));

                    carDataArrayList.add(carData);

                    cursor.moveToNext();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        db.close();

        return carDataArrayList;
    }

    public void deleteCarData(CarData carData) {
        SQLiteDatabase db = getWritableDatabase();

        String receptionID = carData.getReceptionID();
        String carID = carData.getCarID();

        db.delete("CarData", "ReceptionID=? and carID=?", new String[]{receptionID, carID});

        db.close();
    }

    public void insertSectors(ArrayList<Sector> sectors) {
        SQLiteDatabase db = getReadableDatabase();
        long id = 0;

        try {
            for (int i = 0; i < sectors.size(); i++) {
                Sector sector = sectors.get(i);

                ContentValues values = new ContentValues();
                values.put(Sector.COLUM_NAME, sector.getName());
//                values.put(Sector.COLUM_GUID, sector.getGuid());

                id = db.insert(Sector.TABLE_NAME, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public long insertUser(User user) {
        SQLiteDatabase db = getReadableDatabase();
        long id = 0;

        try {

            ContentValues values = new ContentValues();
            values.put(User.COLUM_NAME, user.getName());

            id = db.insert(User.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return id;
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(User.TABLE_NAME, null, null, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    User user = new User();

                    user.setId(cursor.getLong(cursor.getColumnIndex(User.COLUM_ID)));
                    user.setName(cursor.getString(cursor.getColumnIndex(User.COLUM_NAME)));
                    users.add(user);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return users;
    }

    public long insertPhotoCarDataOutfit(String orderID, String carID, String currentPhotoPath) {
        SQLiteDatabase db = getReadableDatabase();
        long id = 0;

        try {
            ContentValues values = new ContentValues();
            values.put("orderID", orderID);
            values.put("carID", carID);
            values.put("currentPhotoPath", currentPhotoPath);

            id = db.insert("CarDataOutfitPhoto", null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return id;
    }

    public ArrayList<Photo> getPhotoList(String orderID, String carID) {
        ArrayList<Photo> mPhotoArrayList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            String select = "orderID = '" + orderID + "' and carID = '" + carID + "'";
            cursor = db.query("CarDataOutfitPhoto", null, select, null, null, null, null);
            //cursor = db.query("CarDataOutfitPhoto", null, null, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    Photo photo = new Photo();

                    String fileName = new File(cursor.getString(cursor.getColumnIndex("currentPhotoPath"))).getName();
                    photo.setName(fileName);

                    photo.setCurrentPhotoPath(cursor.getString(cursor.getColumnIndex("currentPhotoPath")));

                    photo.setOrderID(cursor.getString(cursor.getColumnIndex("orderID")));
                    photo.setCarID(cursor.getString(cursor.getColumnIndex("carID")));

                    mPhotoArrayList.add(photo);

                    cursor.moveToNext();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return mPhotoArrayList;
    }

    public ArrayList<Photo> getPhotoAll() {
        ArrayList<Photo> mPhotoArrayList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query("CarDataOutfitPhoto", null, null, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    Photo photo = new Photo();

                    String fileName = new File(cursor.getString(cursor.getColumnIndex("currentPhotoPath"))).getName();
                    photo.setName(fileName);

                    photo.setCurrentPhotoPath(cursor.getString(cursor.getColumnIndex("currentPhotoPath")));

                    photo.setOrderID(cursor.getString(cursor.getColumnIndex("orderID")));
                    photo.setCarID(cursor.getString(cursor.getColumnIndex("carID")));

                    mPhotoArrayList.add(photo);

                    cursor.moveToNext();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return mPhotoArrayList;
    }

    public void deletePhoto(String currentPhotoPath) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete("CarDataOutfitPhoto", "currentPhotoPath='" + currentPhotoPath + "'", null);
    }

    public ArrayList<Photo> getPhotoOutfit(String orderID) {
        ArrayList<Photo> mPhotos = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            String select = "orderID = '" + orderID + "'";
            cursor = db.query("CarDataOutfitPhoto", null, select, null, null, null, null);
            //cursor = db.query("CarDataOutfitPhoto", null, null, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    Photo photo = new Photo();

                    String fileName = new File(cursor.getString(cursor.getColumnIndex("currentPhotoPath"))).getName();
                    photo.setName(fileName);

                    photo.setCurrentPhotoPath(cursor.getString(cursor.getColumnIndex("currentPhotoPath")));

                    photo.setOrderID(cursor.getString(cursor.getColumnIndex("orderID")));
                    photo.setCarID(cursor.getString(cursor.getColumnIndex("carID")));

                    mPhotos.add(photo);

                    cursor.moveToNext();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return mPhotos;
    }

    public long insertPhotoActInspection(String actID, String listObject, String
            objectID, String currentPhotoPath) {

        if (actID == null){
            return -1;
        }

        SQLiteDatabase db = getReadableDatabase();
        long id = 0;

        try {
            ContentValues values = new ContentValues();
            values.put("actID", actID);
            values.put("listObject", listObject);
            values.put("objectID", objectID);
            values.put("currentPhotoPath", currentPhotoPath);

            id = db.insert("ActInspectionPhoto", null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return id;
    }

    public ArrayList<PhotoActInspection> getPhotoListActInspection(String actID, String
            listObject, String objectID) {
        ArrayList<PhotoActInspection> mPhotoArrayList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            String select = "actID = '" + actID + "' and listObject = '" + listObject + "' and objectID = '" + objectID + "'";
            cursor = db.query("ActInspectionPhoto", null, select, null, null, null, null);
            //cursor = db.query("ActInspectionPhoto", null, null, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    PhotoActInspection photo = new PhotoActInspection();

                    File file = new File(cursor.getString(cursor.getColumnIndex("currentPhotoPath")));
                    if (!file.isFile()) {
                        deletePhotoActInspection(cursor.getString(cursor.getColumnIndex("currentPhotoPath")));
                    }
                    String fileName = file.getName();
                    photo.setName(fileName);

                    photo.setCurrentPhotoPath(cursor.getString(cursor.getColumnIndex("currentPhotoPath")));

                    photo.setActID(cursor.getString(cursor.getColumnIndex("actID")));
                    photo.setListObject(cursor.getString(cursor.getColumnIndex("listObject")));
                    photo.setObjectID(cursor.getString(cursor.getColumnIndex("objectID")));

                    mPhotoArrayList.add(photo);

                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return mPhotoArrayList;
    }

    public ArrayList<PhotoActInspection> getPhotoListActInspection(String actID, String
            objectID) {
        ArrayList<PhotoActInspection> mPhotoArrayList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        deleteDamageActInspection(actID, objectID);

        try {
            String select = "actID = '" + actID + "' and objectID LIKE '%" + objectID + "%'";
            cursor = db.query("ActInspectionPhoto", null, select, null, null, null, null);
            //cursor = db.query("ActInspectionPhoto", null, null, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    PhotoActInspection photo = new PhotoActInspection();

                    File file = new File(cursor.getString(cursor.getColumnIndex("currentPhotoPath")));
                    if (!file.isFile()) {
                        deletePhotoActInspection(cursor.getString(cursor.getColumnIndex("currentPhotoPath")));
                    }
                    String fileName = file.getName();
                    photo.setName(fileName);

                    photo.setCurrentPhotoPath(cursor.getString(cursor.getColumnIndex("currentPhotoPath")));

                    photo.setActID(cursor.getString(cursor.getColumnIndex("actID")));
                    photo.setListObject(cursor.getString(cursor.getColumnIndex("listObject")));
                    photo.setObjectID(cursor.getString(cursor.getColumnIndex("objectID")));

                    mPhotoArrayList.add(photo);

                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return mPhotoArrayList;
    }

    public ArrayList<PhotoActInspection> getPhotoActInspectionAll() {
        ArrayList<PhotoActInspection> mPhotoArrayList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query("ActInspectionPhoto", null, null, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    PhotoActInspection photo = new PhotoActInspection();

                    String fileName = new File(cursor.getString(cursor.getColumnIndex("currentPhotoPath"))).getName();
                    photo.setName(fileName);

                    photo.setCurrentPhotoPath(cursor.getString(cursor.getColumnIndex("currentPhotoPath")));

                    photo.setActID(cursor.getString(cursor.getColumnIndex("actID")));
                    photo.setListObject(cursor.getString(cursor.getColumnIndex("listObject")));
                    photo.setObjectID(cursor.getString(cursor.getColumnIndex("objectID")));

                    mPhotoArrayList.add(photo);

                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return mPhotoArrayList;
    }

    public void deletePhotoActInspection(String currentPhotoPath) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete("ActInspectionPhoto", "currentPhotoPath='" + currentPhotoPath + "'", null);

        db.close();
    }

    public ArrayList<PhotoActInspection> getPhotoActInspection(String orderID) {
        ArrayList<PhotoActInspection> mPhotos = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            String select = "orderID = '" + orderID + "'";
            cursor = db.query("ActInspectionPhoto", null, select, null, null, null, null);
            //cursor = db.query("ActInspectionPhoto", null, null, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    PhotoActInspection photo = new PhotoActInspection();

                    String fileName = new File(cursor.getString(cursor.getColumnIndex("currentPhotoPath"))).getName();
                    photo.setName(fileName);

                    photo.setCurrentPhotoPath(cursor.getString(cursor.getColumnIndex("currentPhotoPath")));

                    photo.setActID(cursor.getString(cursor.getColumnIndex("actID")));
                    photo.setListObject(cursor.getString(cursor.getColumnIndex("listObject")));
                    photo.setObjectID(cursor.getString(cursor.getColumnIndex("objectID")));

                    mPhotos.add(photo);

                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return mPhotos;
    }


//    public ArrayList<Document> getDocuments(String typeDoc) {
//        ArrayList<Document> documents = new ArrayList<>();
//        SQLiteDatabase db = getWritableDatabase();
//        Cursor cursor = null;
//
//        try {
//            String select = Document.TABLE_NAME + "." + Document.COLUM_TYPE + "='" + typeDoc + "'";
//            cursor = db.query(Document.TABLE_NAME, null, select, null, null, null, null);
//
//            if (cursor.moveToNext()) {
//                while (!cursor.isAfterLast()) {
//                    Document document = new Document();
//
//                    //cursor.getColumnNames();
//
//                    document.id = cursor.getLong(cursor.getColumnIndex(Document.COLUM_ID));
//                    document.type = cursor.getString(cursor.getColumnIndex(Document.COLUM_TYPE));
//                    document.dateCreate = stringToDate(cursor.getString(cursor.getColumnIndex(Document.COLUM_DATE_CREATE)));
//                    documents.add(document);
//                    cursor.moveToNext();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//
//        return documents;
//    }

//    public long insertCell(Cell cell) {
//        SQLiteDatabase db = getReadableDatabase();
//        long id = 0;
//
//        try {
//            ContentValues values = new ContentValues();
//
//            values.put(Cell.COLUM_NAME, cell.name);
//            values.put(Cell.COLUM_ADDRESS, cell.address);
//
//            id = db.insert(Cell.TABLE_NAME, null, values);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return id;
//    }
//
//    public ArrayList<Cell> getCells() {
//        ArrayList<Cell> cells = new ArrayList<>();
//        SQLiteDatabase db = getWritableDatabase();
//        Cursor cursor = null;
//
//        try {
//            cursor = db.query(Cell.TABLE_NAME, null, null, null, null, null, null);
//
//            if (cursor.moveToNext()) {
//                while (!cursor.isAfterLast()) {
//                    Cell cell = new Cell();
//                    cell.setId(cursor.getLong(cursor.getColumnIndex(Cell.COLUM_ID)));
//                    cell.setName(cursor.getString(cursor.getColumnIndex(Cell.COLUM_NAME)));
//                    cell.setAddress(cursor.getString(cursor.getColumnIndex(Cell.COLUM_ADDRESS)));
//
//                    cells.add(cell);
//                    cursor.moveToNext();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//
//        return cells;
//    }
//
//    public Cell getCell(long id) {
//        Cell cell = new Cell();
//        SQLiteDatabase db = getWritableDatabase();
//        Cursor cursor = null;
//
//        try {
//            cursor = db.query(Cell.TABLE_NAME, null, Cell.COLUM_ID + " = " + id, null, null, null, null);
//
//            if (cursor.moveToNext()) {
//                cell.setId(cursor.getLong(cursor.getColumnIndex(Cell.COLUM_ID)));
//                cell.setName(cursor.getString(cursor.getColumnIndex(Cell.COLUM_NAME)));
//                cell.setAddress(cursor.getString(cursor.getColumnIndex(Cell.COLUM_NAME)));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//
//        return cell;
//    }
//
//    public Cell getCellToBarCode(String barCode) {
//        Cell cell = new Cell();
//        SQLiteDatabase db = getWritableDatabase();
//        Cursor cursor = null;
//
//        try {
//            String select = Cell.TABLE_NAME + "." + Cell.COLUM_ADDRESS + " = '" + barCode + "'";
//            cursor = db.query(Cell.TABLE_NAME, null, select, null, null, null, null);
//
//            if (cursor.moveToNext()) {
//                cell.setId(cursor.getLong(cursor.getColumnIndex(Cell.COLUM_ID)));
//                cell.setName(cursor.getString(cursor.getColumnIndex(Cell.COLUM_NAME)));
//                cell.setAddress(cursor.getString(cursor.getColumnIndex(Cell.COLUM_NAME)));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//
//        return cell;
//    }
//
//    public ArrayList<Cell> getCellsDocument(long id) {
//        ArrayList<Cell> cells = new ArrayList<>();
//        SQLiteDatabase db = getWritableDatabase();
//        Cursor cursor = null;
//
//        try {
//            String select = ProductsOfDocument.TABLE_NAME + "." + ProductsOfDocument.COLUM_ID_DOCUMENT + "=" + id;
//            String group = ProductsOfDocument.COLUM_ID_CELL;
//
//            cursor = db.query(ProductsOfDocument.TABLE_NAME, null, select, null, group, null, null);
//
//            if (cursor.moveToNext()) {
//                while (!cursor.isAfterLast()) {
//
//                    long idCell = cursor.getLong(cursor.getColumnIndex(ProductsOfDocument.COLUM_ID_CELL));
//                    Cell cell = getCell(idCell);
//                    cells.add(cell);
//                    cursor.moveToNext();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return cells;
//    }
//
//    public long insertProduct(Product product) {
//        SQLiteDatabase db = getReadableDatabase();
//        long id = 0;
//
//        try {
//            ContentValues values = new ContentValues();
//
//            values.put(Product.COLUM_NAME, product.getName());
//            values.put(Product.COLUM_VENDOR_CODE, product.getVendorCode());
//
//            id = db.insert(Product.TABLE_NAME, null, values);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return id;
//    }
//
//    public long insertProductCell(long idDocument, long idCell, long idProduct, long quantity) {
//        SQLiteDatabase db = getReadableDatabase();
//        long id = 0;
//
//        try {
//            ContentValues values = new ContentValues();
//
//            values.put(ProductsOfDocument.COLUM_ID_DOCUMENT, idDocument);
//            values.put(ProductsOfDocument.COLUM_ID_CELL, idCell);
//            values.put(ProductsOfDocument.COLUM_ID_PRODUCT, idProduct);
//            values.put(ProductsOfDocument.COLUM_GUANTITY, quantity);
//
//            id = db.insert(ProductsOfDocument.TABLE_NAME, null, values);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return id;
//    }
//
//    public ArrayList<ProductsOfDocument> getProductsCellDocument(long idDocument, long idCell) {
//        ArrayList<ProductsOfDocument> products = new ArrayList<>();
//        SQLiteDatabase db = getWritableDatabase();
//        Cursor cursor = null;
//
//        try {
//            String select = ProductsOfDocument.TABLE_NAME + "."
//                    + ProductsOfDocument.COLUM_ID_DOCUMENT + " = " + idDocument + " AND "
//                    + ProductsOfDocument.COLUM_ID_CELL + " = " + idCell;
//            ;
//            cursor = db.query(ProductsOfDocument.TABLE_NAME, null, select, null, null, null, null);
//
//            if (cursor.moveToNext()) {
//                while (!cursor.isAfterLast()) {
//                    ProductsOfDocument product = new ProductsOfDocument();
//
//                    Product mProduct = getProduct(cursor.getLong(cursor.getColumnIndex(ProductsOfDocument.COLUM_ID_PRODUCT)));
//                    product.setProduct(mProduct);
//
//                    Cell mCell = getCell(cursor.getLong(cursor.getColumnIndex(ProductsOfDocument.COLUM_ID_CELL)));
//                    product.setCell(mCell);
//
//                    product.setId(cursor.getLong(cursor.getColumnIndex(ProductsOfDocument.COLUM_ID)));
//                    product.setQuantity(cursor.getLong(cursor.getColumnIndex(ProductsOfDocument.COLUM_GUANTITY)));
//
//                    products.add(product);
//                    cursor.moveToNext();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//
//        return products;
//    }
//
//    public ArrayList<Product> getProducts() {
//        ArrayList<Product> products = new ArrayList<>();
//        SQLiteDatabase db = getWritableDatabase();
//        Cursor cursor = null;
//
//        try {
//            cursor = db.query(Product.TABLE_NAME, null, null, null, null, null, null);
//
//            if (cursor.moveToNext()) {
//                while (!cursor.isAfterLast()) {
//                    Product product = new Product();
//                    product.setId(cursor.getLong(cursor.getColumnIndex(Product.COLUM_ID)));
//                    product.setName(cursor.getString(cursor.getColumnIndex(Product.COLUM_NAME)));
//                    product.setVendorCode(cursor.getString(cursor.getColumnIndex(Product.COLUM_VENDOR_CODE)));
//
//                    products.add(product);
//                    cursor.moveToNext();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//
//        return products;
//    }
//
//    public Product getProductToBarCode(String barCode) {
//        Product product = new Product();
//        SQLiteDatabase db = getWritableDatabase();
//        Cursor cursor = null;
//
//        try {
//            String select = Product.TABLE_NAME + "." + Product.COLUM_VENDOR_CODE + " = '" + barCode + "'";
//            cursor = db.query(Product.TABLE_NAME, null, select, null, null, null, null);
//
//            if (cursor.moveToNext()) {
//                product.setId(cursor.getLong(cursor.getColumnIndex(Product.COLUM_ID)));
//                product.setName(cursor.getString(cursor.getColumnIndex(Product.COLUM_NAME)));
//                product.setVendorCode(cursor.getString(cursor.getColumnIndex(Product.COLUM_VENDOR_CODE)));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//
//        return product;
//    }
//
//    public Product getProduct(long id) {
//        Product product = new Product();
//        SQLiteDatabase db = getWritableDatabase();
//        Cursor cursor = null;
//
//        try {
//            cursor = db.query(Product.TABLE_NAME, null, Product.COLUM_ID + " = " + id, null, null, null, null);
//
//            if (cursor.moveToNext()) {
//                product.setId(cursor.getLong(cursor.getColumnIndex(Product.COLUM_ID)));
//                product.setName(cursor.getString(cursor.getColumnIndex(Product.COLUM_NAME)));
//                product.setVendorCode(cursor.getString(cursor.getColumnIndex(Product.COLUM_VENDOR_CODE)));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//
//        return product;
//    }
//
//    private String dateToFormat(Date date) {
//        String retval = "";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        if (date == null) {
//            return retval;
//        }
//        retval = sdf.format(date);
//        return retval;
//    }
//
//    private Date stringToDate(String text) {
//        Date mDate = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//
//        if (!text.isEmpty()) {
//            try {
//                mDate = sdf.parse(text);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        return mDate;
//    }
//
//    public void deleteProductOfDocument(ProductsOfDocument productsOfDocument) {
//        SQLiteDatabase db = getWritableDatabase();
//        db.delete(ProductsOfDocument.TABLE_NAME, ProductsOfDocument.COLUM_ID + "=" + productsOfDocument.getId(), null);
//    }
//
//    public void deleteCell(Cell cell) {
//        SQLiteDatabase db = getWritableDatabase();
//        db.delete(Cell.TABLE_NAME, cell.COLUM_ID + "=" + cell.getId(), null);
//    }
//
//    public void deleteCellsAll() {
//        ArrayList<Cell> mCells = getCells();
//        for (Cell cell : mCells) {
//            deleteCell(cell);
//        }
//    }
//
//    public void deleteProduct(Product product) {
//        SQLiteDatabase db = getWritableDatabase();
//        db.delete(Product.TABLE_NAME, Product.COLUM_ID + "=" + product.getId(), null);
//    }
//
//    public void deleteProductAll() {
//        ArrayList<Product> mProducts = getProducts();
//        for (Product product : mProducts) {
//            deleteProduct(product);
//        }
//    }
//
//    public ArrayList<Cell> initialiseCellData() {
//        ArrayList<Cell> cellArrayList = new ArrayList<>();
//
//        int count = soapParam_Response.getPropertyCount();
//
//        for (int i = 0; i < count; i++) {
//            SoapObject property = (SoapObject) soapParam_Response.getProperty(i);
//            //Log.i("initialiseEmploy", ""+property.toString());
//            if (property instanceof SoapObject) {
//                SoapObject info = (SoapObject) property;
//                String name = info.getProperty("name").toString();
//                String address = info.getProperty("address").toString();
//                Cell cell = new Cell(name, address);
//                cellArrayList.add(cell);
//            }
//        }
//        return cellArrayList;
//    }
//
//    public ArrayList<Product> initialiseProductData() {
//        ArrayList<Product> productArrayList = new ArrayList<>();
//
//        int count = soapParam_Response.getPropertyCount();
//
//        for (int i = 0; i < count; i++) {
//            SoapObject property = (SoapObject) soapParam_Response.getProperty(i);
//            //Log.i("initialiseEmploy", ""+property.toString());
//            if (property instanceof SoapObject) {
//                SoapObject info = (SoapObject) property;
//                String name = info.getProperty("name").toString();
//                String vendorCode = info.getProperty("vendorCode").toString();
//                String barcode = info.getProperty("barcode").toString();
//
//                Product product = new Product();
//                product.setName(name);
//                product.setVendorCode(vendorCode);
//                product.setBarcode(barcode);
//
//                productArrayList.add(product);
//            }
//        }
//        return productArrayList;
//    }

    public void insertReceptionList(ArrayList<Reception> receptions) {
        checkReceptions(receptions);

        for (int i = 0; i < receptions.size(); i++) {
            insertReception(receptions.get(i));
        }
    }

    private void checkReceptions(ArrayList<Reception> receptions) {
        ArrayList<Reception> receptionListDB = getReceptionList();

        for (int i = 0; i < receptionListDB.size(); i++) {
            boolean delete = true;

            for (int j = 0; j < receptions.size(); j++) {
                if (receptionListDB.get(i).getID().equals(receptions.get(j).getID())) {
                    delete = false;
                    break;
                }
            }
            if (delete) {
                for (int j = 0; j < receptionListDB.get(i).getCarData().size(); j++) {
                    CarData car = receptionListDB.get(i).getCarData().get(j);
                    ActInspection act = SharedData.getActInspectionCar(car.getCarID());
                    deleteAct(act.getID());

                    SQLiteDatabase db = getWritableDatabase();
                    db.delete("Receptions", "ID=?", new String[]{receptionListDB.get(i).getID()});
                    db.close();
                }
            }
        }
    }

    public void insertReception(Reception reception) {
        SQLiteDatabase db = getReadableDatabase();
        long id = 0;

        try {
            boolean exist = getReceptionExist(reception.getID());

            ContentValues values = new ContentValues();
            values.put("ID", reception.getID());
            values.put("description", reception.getDescription());
            values.put("autoNumber", reception.getAutoNumber());
            values.put("driver", reception.getDriver());
            values.put("driverPhone", reception.getDriverPhone());
            values.put("invoiceNumber", reception.getInvoiceNumber());

            if (exist) {
                id = db.update("Receptions", values, "ID=?", new String[]{reception.getID()});
            } else {
                id = db.insert("Receptions", null, values);
            }

            reception.isSave = id != -1 ? true : false;

            if (reception.isSave) {
                for (int i = 0; i < reception.getCarData().size(); i++) {
                    insertCarDataReceptions(reception.getCarData().get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public boolean getReceptionExist(String receptionID) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            String select = "ID = '" + receptionID + "'";
            cursor = db.query("Receptions", null, select, null, null, null, null);

            if (cursor.moveToNext()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return false;
    }

    public ArrayList<Reception> getReceptionList() {
        ArrayList<Reception> receptions = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query("Receptions", null, null, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    Reception reception = new Reception();

                    reception.setID(cursor.getString(cursor.getColumnIndex("ID")));
                    reception.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                    reception.setAutoNumber(cursor.getString(cursor.getColumnIndex("autoNumber")));
                    reception.setDriver(cursor.getString(cursor.getColumnIndex("driver")));
                    reception.setDriverPhone(cursor.getString(cursor.getColumnIndex("driverPhone")));
                    reception.setInvoiceNumber(cursor.getString(cursor.getColumnIndex("invoiceNumber")));

                    reception.setCarData(getCarDataReceptionsList(reception.getID()));

                    receptions.add(reception);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return receptions;
    }

    public void insertCarDataReceptions(CarData carData) {
        SQLiteDatabase db = getReadableDatabase();
        long id = 0;

        try {
            boolean exist = getCarDataReceptionsExist(carData.getReceptionID(), carData.getCarID());

            ContentValues values = new ContentValues();
            values.put("ReceptionID", carData.getReceptionID());
            values.put("carID", carData.getCarID());
            values.put("car", carData.getCar());
            values.put("barCode", carData.getBarCode());
            values.put("sectorID", carData.getSectorID());
            values.put("sector", carData.getSector());
            values.put("_row", carData.getRow());
            values.put("productionDate", carData.getProductionDateString());

            if (exist) {
                id = db.update("CarDataReceptions", values, "ReceptionID=? and carID=?", new String[]{carData.getReceptionID(), carData.getCarID()});
            } else {
                id = db.insert("CarDataReceptions", null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getCarDataReceptionsExist(String receptionID, String carID) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            String select = "ReceptionID = '" + receptionID + "' and carID = '" + carID + "'";
            cursor = db.query("CarDataReceptions", null, select, null, null, null, null);

            if (cursor.moveToNext()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return false;
    }

    public ArrayList<CarData> getCarDataReceptionsList(String receptionID) {
        ArrayList<CarData> carDatas = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            String select = "ReceptionID = '" + receptionID + "'";
            cursor = db.query("CarDataReceptions", null, select, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    CarData carData = new CarData();

                    carData.setReceptionID(cursor.getString(cursor.getColumnIndex("ReceptionID")));
                    carData.setCarID(cursor.getString(cursor.getColumnIndex("carID")));
                    carData.setCar(cursor.getString(cursor.getColumnIndex("car")));
                    carData.setBarCode(cursor.getString(cursor.getColumnIndex("barCode")));
                    carData.setSectorID(cursor.getString(cursor.getColumnIndex("sectorID")));
                    carData.setSector(cursor.getString(cursor.getColumnIndex("sector")));
                    carData.setRow(cursor.getString(cursor.getColumnIndex("_row")));
                    carData.setProductionDate(cursor.getString(cursor.getColumnIndex("productionDate")));

                    carDatas.add(carData);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return carDatas;
    }

    public void insertActInspectionList(ArrayList<ActInspection> actInspections) {
        for (int i = 0; i < actInspections.size(); i++) {
            insertActInspection(actInspections.get(i));
        }
    }

    public void insertActInspectionListDownload(ArrayList<ActInspection> actInspections) {
        for (int i = 0; i < actInspections.size(); i++) {
            ActInspection actInspection = actInspections.get(i);
            boolean exist = getActInspectionExist(actInspection.getReceptionID(), actInspection.getID());
            if (exist) {
                actInspection = getActInspection(actInspection.getReceptionID(), actInspection.getID());
            }

            insertActInspection(actInspection);
        }
    }

    public void insertActInspection(ActInspection actInspection) {
        SQLiteDatabase db = getReadableDatabase();
        long id = 0;

        try {
            boolean exist = getActInspectionExist(actInspection.getReceptionID(), actInspection.getID());

            ContentValues values = new ContentValues();
            values.put("ReceptionID", actInspection.getReceptionID());
            values.put("ID", actInspection.getID());
            values.put("description", actInspection.getDescription());
            values.put("stateID", actInspection.getStateID());
            values.put("state", actInspection.getState());
            values.put("formID", actInspection.getFormID());
            values.put("form", actInspection.getForm());
            values.put("truckPosition", actInspection.getTruckPosition());
            values.put("truckPositionDirection", actInspection.getTruckPositionDirection());
            values.put("run", actInspection.getRun());
            values.put("storageID", actInspection.getStorageID());
            values.put("storage", actInspection.getStorage());
            values.put("inspectionDatePlan", actInspection.getInspectionDatePlanString());
            values.put("inspectionDateFact", actInspection.getInspectionDateFactString());
            values.put("carID", actInspection.getCarID());
            values.put("car", actInspection.getCar());
            values.put("productionDate", actInspection.getProductionDate());
            values.put("barCode", actInspection.getBarCode());
            values.put("sectorID", actInspection.getSectorID());
            values.put("sector", actInspection.getSector());
            values.put("_row", actInspection.getRow());
            values.put("TypeMachineID", actInspection.getTypeMachineID());
            values.put("TypeMachine", actInspection.getTypeMachine());
            values.put("performed", actInspection.isPerformed() ? 1 : 0);
            values.put("sendPerformed", actInspection.sendPerformed ? 1 : 0);

            if (exist) {
                id = db.update("ActInspection", values, "ReceptionID=? and ID=?", new String[]{actInspection.getReceptionID(), actInspection.getID()});
            } else {
                id = db.insert("ActInspection", null, values);
            }

            if (id != -1) {
                insertReception(SharedData.getReception(actInspection.getReceptionID()));

                for (int i = 0; i < actInspection.getEquipments().size(); i++) {
                    insertEquipmentActInspection(actInspection.getID(), actInspection.getEquipments().get(i));
                }

                for (int i = 0; i < actInspection.getInspections().size(); i++) {
                    insertInspectionActInspection(actInspection.getID(), actInspection.getInspections().get(i));
                }

                for (int i = 0; i < actInspection.getTypesPhotos().size(); i++) {
                    insertTypesPhotoActInspection(actInspection.getID(), actInspection.getTypesPhotos().get(i));
                }

                for (int i = 0; i < actInspection.getDamages().size(); i++) {
                    insertDamageActInspection(actInspection.getID(), actInspection.getDamages().get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public boolean getActInspectionExist(String receptionID, String id) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            String select = "ReceptionID = '" + receptionID + "' and ID = '" + id + "'";
            cursor = db.query("ActInspection", null, select, null, null, null, null);

            if (cursor.moveToNext()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return false;
    }

    public ActInspection getActInspection(String receptionID, String id) {
        ActInspection actInspection = new ActInspection();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            String select = "ReceptionID = '" + receptionID + "' and ID = '" + id + "'";
            cursor = db.query("ActInspection", null, select, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    actInspection = new ActInspection();

                    actInspection.setReceptionID(cursor.getString(cursor.getColumnIndex("ReceptionID")));
                    actInspection.setID(cursor.getString(cursor.getColumnIndex("ID")));
                    actInspection.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                    actInspection.setStateID(cursor.getString(cursor.getColumnIndex("stateID")));
                    actInspection.setState(cursor.getString(cursor.getColumnIndex("state")));
                    actInspection.setFormID(cursor.getString(cursor.getColumnIndex("formID")));
                    actInspection.setForm(cursor.getString(cursor.getColumnIndex("form")));
                    actInspection.setTruckPosition(cursor.getInt(cursor.getColumnIndex("truckPosition")));
                    actInspection.setTruckPositionDirection(cursor.getString(cursor.getColumnIndex("truckPositionDirection")));
                    actInspection.setRun(cursor.getString(cursor.getColumnIndex("run")));
                    actInspection.setStorageID(cursor.getString(cursor.getColumnIndex("storageID")));
                    actInspection.setStorage(cursor.getString(cursor.getColumnIndex("storage")));
                    actInspection.setInspectionDatePlan(cursor.getString(cursor.getColumnIndex("inspectionDatePlan")));
                    actInspection.setInspectionDateFact(cursor.getString(cursor.getColumnIndex("inspectionDateFact")));
                    actInspection.setCarID(cursor.getString(cursor.getColumnIndex("carID")));
                    actInspection.setCar(cursor.getString(cursor.getColumnIndex("car")));
                    actInspection.setProductionDate(cursor.getString(cursor.getColumnIndex("productionDate")));
                    actInspection.setBarCode(cursor.getString(cursor.getColumnIndex("barCode")));
                    actInspection.setSectorID(cursor.getString(cursor.getColumnIndex("sectorID")));
                    actInspection.setSector(cursor.getString(cursor.getColumnIndex("sector")));
                    actInspection.setRow(cursor.getString(cursor.getColumnIndex("_row")));
                    actInspection.setTypeMachineID(cursor.getString(cursor.getColumnIndex("TypeMachineID")));
                    actInspection.setTypeMachine(cursor.getString(cursor.getColumnIndex("TypeMachine")));
                    actInspection.setPerformed(cursor.getInt(cursor.getColumnIndex("performed")) == 0 ? false : true);
                    actInspection.sendPerformed = cursor.getInt(cursor.getColumnIndex("sendPerformed")) == 0 ? false : true;

                    actInspection.setEquipments(getEquipmentActInspection(actInspection.getID()));
                    actInspection.setInspections(getInspectionActInspection(actInspection.getID()));
                    actInspection.setTypesPhotos(getTypesPhotoActInspection(actInspection.getID()));
                    actInspection.setDamages(getDamageActInspection(actInspection));

                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return actInspection;
    }

    public ArrayList<ActInspection> getActInspectionList() {
        ArrayList<ActInspection> actInspections = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query("ActInspection", null, null, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    ActInspection actInspection = new ActInspection();

                    actInspection.setReceptionID(cursor.getString(cursor.getColumnIndex("ReceptionID")));
                    actInspection.setID(cursor.getString(cursor.getColumnIndex("ID")));
                    actInspection.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                    actInspection.setStateID(cursor.getString(cursor.getColumnIndex("stateID")));
                    actInspection.setState(cursor.getString(cursor.getColumnIndex("state")));
                    actInspection.setFormID(cursor.getString(cursor.getColumnIndex("formID")));
                    actInspection.setForm(cursor.getString(cursor.getColumnIndex("form")));
                    actInspection.setTruckPosition(cursor.getInt(cursor.getColumnIndex("truckPosition")));
                    actInspection.setTruckPositionDirection(cursor.getString(cursor.getColumnIndex("truckPositionDirection")));
                    actInspection.setRun(cursor.getString(cursor.getColumnIndex("run")));
                    actInspection.setStorageID(cursor.getString(cursor.getColumnIndex("storageID")));
                    actInspection.setStorage(cursor.getString(cursor.getColumnIndex("storage")));
                    actInspection.setInspectionDatePlan(cursor.getString(cursor.getColumnIndex("inspectionDatePlan")));
                    actInspection.setInspectionDateFact(cursor.getString(cursor.getColumnIndex("inspectionDateFact")));
                    actInspection.setCarID(cursor.getString(cursor.getColumnIndex("carID")));
                    actInspection.setCar(cursor.getString(cursor.getColumnIndex("car")));
                    actInspection.setProductionDate(cursor.getString(cursor.getColumnIndex("productionDate")));
                    actInspection.setBarCode(cursor.getString(cursor.getColumnIndex("barCode")));
                    actInspection.setSectorID(cursor.getString(cursor.getColumnIndex("sectorID")));
                    actInspection.setSector(cursor.getString(cursor.getColumnIndex("sector")));
                    actInspection.setRow(cursor.getString(cursor.getColumnIndex("_row")));
                    actInspection.setTypeMachineID(cursor.getString(cursor.getColumnIndex("TypeMachineID")));
                    actInspection.setTypeMachine(cursor.getString(cursor.getColumnIndex("TypeMachine")));
                    actInspection.setPerformed(cursor.getInt(cursor.getColumnIndex("performed")) == 0 ? false : true);
                    actInspection.sendPerformed = cursor.getInt(cursor.getColumnIndex("sendPerformed")) == 0 ? false : true;

                    actInspection.setEquipments(getEquipmentActInspection(actInspection.getID()));
                    actInspection.setInspections(getInspectionActInspection(actInspection.getID()));
                    actInspection.setTypesPhotos(getTypesPhotoActInspection(actInspection.getID()));
                    actInspection.setDamages(getDamageActInspection(actInspection));

                    actInspections.add(actInspection);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return actInspections;
    }

    public void insertEquipmentActInspection(String ActID, Equipment equipment) {
        SQLiteDatabase db = getReadableDatabase();

        if (ActID == null){
            return;
        }

        long id = 0;

        try {
            boolean exist = getExist("Equipment", "ActID = '" + ActID + "' and equipmentID = '" + equipment.getEquipmentID() + "'");

            ContentValues values = new ContentValues();
            values.put("ActID", ActID);
            values.put("equipmentID", equipment.getEquipmentID());
            values.put("equipment", equipment.getEquipment());
            values.put("listObject", equipment.getListObject());
            values.put("quantityPlan", equipment.getQuantityPlan());
            values.put("quantityFact", equipment.getQuantityFact());

            if (exist) {
                id = db.update("Equipment", values, "ActID=? and equipmentID=?", new String[]{ActID, equipment.getEquipmentID()});
            } else {
                id = db.insert("Equipment", null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void insertInspectionActInspection(String ActID, Inspection inspection) {
        SQLiteDatabase db = getReadableDatabase();

        if (ActID == null){
            return;
        }

        long id = 0;

        try {
            boolean exist = getExist("Inspection", "ActID = '" + ActID + "' and ID = '" + inspection.getID() + "'");

            ContentValues values = new ContentValues();
            values.put("ActID", ActID);
            values.put("ID", inspection.getID());
            values.put("name", inspection.getName());
            values.put("performed", inspection.isPerformed() ? 1 : 0);

            if (exist) {
                id = db.update("Inspection", values, "ActID=? and ID=?", new String[]{ActID, inspection.getID()});
            } else {
                id = db.insert("Inspection", null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void insertTypesPhotoActInspection(String ActID, TypesPhoto typesPhoto) {
        SQLiteDatabase db = getReadableDatabase();

        if (ActID == null){
            return;
        }

        long id = 0;

        try {
            boolean exist = getExist("TypesPhoto", "ActID = '" + ActID + "' and typePhotoID = '" + typesPhoto.getTypePhotoID() + "'");

            ContentValues values = new ContentValues();
            values.put("ActID", ActID);
            values.put("typePhotoID", typesPhoto.getTypePhotoID());
            values.put("typePhoto", typesPhoto.getTypePhoto());
            values.put("listObject", typesPhoto.getListObject());

            if (exist) {
                id = db.update("TypesPhoto", values, "ActID=? and typePhotoID=?", new String[]{ActID, typesPhoto.getTypePhotoID()});
            } else {
                id = db.insert("TypesPhoto", null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void insertDamageActInspection(String ActID, Damage damage) {
        SQLiteDatabase db = getReadableDatabase();

        if (ActID == null){
            return;
        }

        long id = 0;

        try {
            String detailID = damage.getDetail() == null ? "" : damage.getDetail().getDetailID();
            String mTypeDamageID = damage.getTypeDamage() == null ? "" : damage.getTypeDamage().getID();
            String mDegreesDamageID = damage.getDegreesDamage() == null ? "" : damage.getDegreesDamage().getID();
            String mClassificationDamageID = damage.getClassificationDamage() == null ? "" : damage.getClassificationDamage().getID();
            String mOriginDamageID = damage.getOriginDamage() == null ? "" : damage.getOriginDamage().getID();

            boolean exist = getExist("Damage", "ActID = '" + ActID + "' and detailID = '" + detailID + "'");

            ContentValues values = new ContentValues();
            values.put("ActID", ActID);
            values.put("detailID", detailID);
            values.put("mTypeDamageID", mTypeDamageID);
            values.put("typeDetail", damage.getTypeDetail() == null ? "" : damage.getTypeDetail());
            values.put("mDegreesDamageID", mDegreesDamageID);
            values.put("mClassificationDamageID", mClassificationDamageID);
            values.put("mOriginDamageID", mOriginDamageID);
            values.put("detailDamage", damage.getDetailDamage() == null ? "" : damage.getDetailDamage());
            values.put("commentDamage", damage.getCommentDamage() == null ? "" : damage.getCommentDamage());
            values.put("widthDamage", damage.getWidthDamage() == null ? "" : damage.getWidthDamage());
            values.put("heightDamage", damage.getHeightDamage() == null ? "" : damage.getHeightDamage());

            if (!db.isOpen()){
                db = getReadableDatabase();
            }

            if (exist) {
                id = db.update("Damage", values, "ActID=? and detailID=?", new String[]{ActID, detailID});
            } else {
                id = db.insert("Damage", null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void deleteDamageActInspection(String ActID, String detailID) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.delete("Damage", "ActID = '" + ActID + "' and detailID = '" + detailID + "'", null);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Equipment> getEquipmentActInspection(String ActID) {
        ArrayList<Equipment> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            String select = "ActID = '" + ActID + "'";
            cursor = db.query("Equipment", null, select, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    Equipment equipment = new Equipment();

                    equipment.setEquipmentID(cursor.getString(cursor.getColumnIndex("equipmentID")));
                    equipment.setEquipment(cursor.getString(cursor.getColumnIndex("equipment")));
                    equipment.setListObject(cursor.getString(cursor.getColumnIndex("listObject")));
                    equipment.setQuantityPlan(cursor.getInt(cursor.getColumnIndex("quantityPlan")));
                    equipment.setQuantityFact(cursor.getInt(cursor.getColumnIndex("quantityFact")));

                    list.add(equipment);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return list;
    }

    public ArrayList<Inspection> getInspectionActInspection(String ActID) {
        ArrayList<Inspection> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            String select = "ActID = '" + ActID + "'";
            cursor = db.query("Inspection", null, select, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    Inspection inspection = new Inspection();

                    inspection.setID(cursor.getString(cursor.getColumnIndex("ID")));
                    inspection.setName(cursor.getString(cursor.getColumnIndex("name")));
                    inspection.setPerformed(cursor.getInt(cursor.getColumnIndex("performed")) == 0 ? false : true);

                    list.add(inspection);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return list;
    }

    public ArrayList<TypesPhoto> getTypesPhotoActInspection(String ActID) {
        ArrayList<TypesPhoto> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            String select = "ActID = '" + ActID + "'";
            cursor = db.query("TypesPhoto", null, select, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    TypesPhoto typesPhoto = new TypesPhoto();

                    typesPhoto.setTypePhotoID(cursor.getString(cursor.getColumnIndex("typePhotoID")));
                    typesPhoto.setTypePhoto(cursor.getString(cursor.getColumnIndex("typePhoto")));
                    typesPhoto.setListObject(cursor.getString(cursor.getColumnIndex("listObject")));

                    list.add(typesPhoto);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return list;
    }

    public ArrayList<Damage> getDamageActInspection(ActInspection actInspection) {
        ArrayList<Damage> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            String select = "ActID = '" + actInspection.getID() + "'";
            cursor = db.query("Damage", null, select, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    Damage damage = new Damage();

                    damage.setDetail(actInspection.getTypeMachineID(), cursor.getString(cursor.getColumnIndex("detailID")));
                    damage.setTypeDamage(cursor.getString(cursor.getColumnIndex("mTypeDamageID")));
                    damage.setTypeDetail(cursor.getString(cursor.getColumnIndex("typeDetail")));
                    damage.setDegreesDamage(cursor.getString(cursor.getColumnIndex("mDegreesDamageID")));
                    damage.setClassificationDamage(cursor.getString(cursor.getColumnIndex("mClassificationDamageID")));
                    damage.setOriginDamage(cursor.getString(cursor.getColumnIndex("mOriginDamageID")));
                    damage.setDetailDamage(cursor.getString(cursor.getColumnIndex("detailDamage")));
                    damage.setCommentDamage(cursor.getString(cursor.getColumnIndex("commentDamage")));
                    damage.setWidthDamage(cursor.getString(cursor.getColumnIndex("widthDamage")));
                    damage.setHeightDamage(cursor.getString(cursor.getColumnIndex("heightDamage")));

                    list.add(damage);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return list;
    }


    public void insertScheme() {
        SQLiteDatabase db = getReadableDatabase();
        long id = 0;
        try {
            removeAll("Scheme");

            for (int i = 0; i < SharedData.SCHEMES.size(); i++) {
                ContentValues values = new ContentValues();

                Scheme scheme = SharedData.SCHEMES.get(i);

                values.put("ID", scheme.getID());
                values.put("Name", scheme.getName());
                values.put("TypeMachineID", scheme.getTypeMachineID());
                values.put("TypeMachine", scheme.getTypeMachine());
                values.put("ViewSchemesID", scheme.getViewSchemesID());
                values.put("ViewSchemes", scheme.getViewSchemes());
                values.put("SVG", scheme.getSVG());

                id = db.insert("Scheme", null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void insertDetail() {
        SQLiteDatabase db = getReadableDatabase();
        long id = 0;
        try {
            removeAll("Detail");

            for (int i = 0; i < SharedData.DamageDefect.size(); i++) {
                Detail detail = SharedData.DamageDefect.get(i);

                ContentValues values = new ContentValues();

                values.put("typeDetail", "defect");
                values.put("ID", detail.getID());
                values.put("tempID", detail.getTempID());
                values.put("detailID", detail.getDetailID());
                values.put("detailName", detail.getDetailName());

                id = db.insert("Detail", null, values);
            }

            for (int i = 0; i < SharedData.SCHEMES.size(); i++) {
                Scheme scheme = SharedData.SCHEMES.get(i);

                for (int j = 0; j < scheme.getDetails().size(); j++) {
                    Detail detail = scheme.getDetails().get(j);

                    ContentValues values = new ContentValues();

                    values.put("typeDetail", scheme.getID());
                    values.put("ID", detail.getID());
                    values.put("tempID", detail.getTempID());
                    values.put("detailID", detail.getDetailID());
                    values.put("detailName", detail.getDetailName());

                    id = db.insert("Detail", null, values);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void insertTypesDamages() {
        SQLiteDatabase db = getReadableDatabase();
        long id = 0;
        try {
            removeAll("TypesDamages");

            for (int i = 0; i < SharedData.TypesDamages.size(); i++) {
                ContentValues values = new ContentValues();

                TypeDamage typeDamage = SharedData.TypesDamages.get(i);

                values.put("ID", typeDamage.getID());
                values.put("name", typeDamage.getName());

                id = db.insert("TypesDamages", null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void insertDegreesDamages() {
        SQLiteDatabase db = getReadableDatabase();
        long id = 0;
        try {
            removeAll("DegreesDamages");

            for (int i = 0; i < SharedData.DegreesDamages.size(); i++) {
                ContentValues values = new ContentValues();

                DegreesDamage degreesDamage = SharedData.DegreesDamages.get(i);

                values.put("ID", degreesDamage.getID());
                values.put("name", degreesDamage.getName());

                id = db.insert("DegreesDamages", null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void insertClassificationDamages() {
        SQLiteDatabase db = getReadableDatabase();
        long id = 0;
        try {
            removeAll("ClassificationDamages");

            for (int i = 0; i < SharedData.ClassificationDamages.size(); i++) {
                ContentValues values = new ContentValues();

                ClassificationDamage classificationDamage = SharedData.ClassificationDamages.get(i);

                values.put("ID", classificationDamage.getID());
                values.put("name", classificationDamage.getName());

                id = db.insert("ClassificationDamages", null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void insertOriginDamages() {
        SQLiteDatabase db = getReadableDatabase();
        long id = 0;
        try {
            removeAll("OriginDamages");

            for (int i = 0; i < SharedData.OriginDamages.size(); i++) {
                ContentValues values = new ContentValues();

                OriginDamage originDamage = SharedData.OriginDamages.get(i);

                values.put("ID", originDamage.getID());
                values.put("name", originDamage.getName());

                id = db.insert("OriginDamages", null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void insertTypeDamagePhotos() {
        SQLiteDatabase db = getReadableDatabase();
        long id = 0;
        try {
            removeAll("TypeDamagePhotos");

            for (int i = 0; i < SharedData.TypeDamagePhotos.size(); i++) {
                ContentValues values = new ContentValues();

                TypeDamagePhoto typeDamagePhoto = SharedData.TypeDamagePhotos.get(i);

                values.put("ID", typeDamagePhoto.getID());
                values.put("name", typeDamagePhoto.getName());

                id = db.insert("TypeDamagePhotos", null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void removeAll(String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(tableName, null, null);
        db.close();
    }

    public boolean getExist(String table, String select) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(table, null, select, null, null, null, null);

            if (cursor.moveToNext()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return false;
    }

    public ArrayList<Scheme> getScheme() {
        ArrayList<Scheme> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query("Scheme", null, null, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    Scheme scheme = new Scheme();

                    scheme.setID(cursor.getString(cursor.getColumnIndex("ID")));
                    scheme.setName(cursor.getString(cursor.getColumnIndex("Name")));
                    scheme.setTypeMachineID(cursor.getString(cursor.getColumnIndex("TypeMachineID")));
                    scheme.setTypeMachine(cursor.getString(cursor.getColumnIndex("TypeMachine")));
                    scheme.setViewSchemesID(cursor.getString(cursor.getColumnIndex("ViewSchemesID")));
                    scheme.setViewSchemes(cursor.getString(cursor.getColumnIndex("ViewSchemes")));
                    scheme.setSVG(cursor.getString(cursor.getColumnIndex("SVG")));

                    list.add(scheme);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return list;
    }

    public ArrayList<Detail> getDetail() {
        ArrayList<Detail> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            String select = "typeDetail = 'defect'";
            cursor = db.query("Detail", null, select, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    Detail detail = new Detail();

                    detail.setID(cursor.getString(cursor.getColumnIndex("ID")));
                    detail.setTempID(cursor.getInt(cursor.getColumnIndex("tempID")));
                    detail.setDetailID(cursor.getString(cursor.getColumnIndex("detailID")));
                    detail.setDetailName(cursor.getString(cursor.getColumnIndex("detailName")));

                    list.add(detail);
                    cursor.moveToNext();
                }
            }

            for (int i = 0; i < SharedData.SCHEMES.size(); i++) {
                Scheme scheme = SharedData.SCHEMES.get(i);
                scheme.getDetails().clear();

                select = "typeDetail = '" + scheme.getID() + "'";
                cursor = db.query("Detail", null, select, null, null, null, null);

                if (cursor.moveToNext()) {
                    while (!cursor.isAfterLast()) {
                        Detail detail = new Detail();

                        detail.setID(cursor.getString(cursor.getColumnIndex("ID")));
                        detail.setTempID(cursor.getInt(cursor.getColumnIndex("tempID")));
                        detail.setDetailID(cursor.getString(cursor.getColumnIndex("detailID")));
                        detail.setDetailName(cursor.getString(cursor.getColumnIndex("detailName")));

                        scheme.getDetails().add(detail);
                        cursor.moveToNext();
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return list;
    }

    public ArrayList<TypeDamage> getTypesDamages() {
        ArrayList<TypeDamage> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query("TypesDamages", null, null, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    TypeDamage typeDamage = new TypeDamage();

                    typeDamage.setID(cursor.getString(cursor.getColumnIndex("ID")));
                    typeDamage.setName(cursor.getString(cursor.getColumnIndex("name")));

                    list.add(typeDamage);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return list;
    }

    public ArrayList<DegreesDamage> getDegreesDamages() {
        ArrayList<DegreesDamage> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query("DegreesDamages", null, null, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    DegreesDamage degreesDamage = new DegreesDamage();

                    degreesDamage.setID(cursor.getString(cursor.getColumnIndex("ID")));
                    degreesDamage.setName(cursor.getString(cursor.getColumnIndex("name")));

                    list.add(degreesDamage);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return list;
    }

    public ArrayList<ClassificationDamage> getClassificationDamages() {
        ArrayList<ClassificationDamage> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query("ClassificationDamages", null, null, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    ClassificationDamage classificationDamage = new ClassificationDamage();

                    classificationDamage.setID(cursor.getString(cursor.getColumnIndex("ID")));
                    classificationDamage.setName(cursor.getString(cursor.getColumnIndex("name")));

                    list.add(classificationDamage);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return list;
    }

    public ArrayList<OriginDamage> getOriginDamages() {
        ArrayList<OriginDamage> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query("OriginDamages", null, null, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    OriginDamage originDamage = new OriginDamage();

                    originDamage.setID(cursor.getString(cursor.getColumnIndex("ID")));
                    originDamage.setName(cursor.getString(cursor.getColumnIndex("name")));

                    list.add(originDamage);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return list;
    }

    public ArrayList<TypeDamagePhoto> getTypeDamagePhotos() {
        ArrayList<TypeDamagePhoto> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query("TypeDamagePhotos", null, null, null, null, null, null);

            if (cursor.moveToNext()) {
                while (!cursor.isAfterLast()) {
                    TypeDamagePhoto typeDamagePhoto = new TypeDamagePhoto();

                    typeDamagePhoto.setID(cursor.getString(cursor.getColumnIndex("ID")));
                    typeDamagePhoto.setName(cursor.getString(cursor.getColumnIndex("name")));

                    list.add(typeDamagePhoto);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return list;
    }

    public void deleteAct(String id) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete("CarData", "carID=?", new String[]{id});
        db.delete("CarDataReceptions", "carID=?", new String[]{id});

        db.delete("ActInspection", "ID=?", new String[]{id});
        db.delete("Equipment", "ActID=?", new String[]{id});
        db.delete("Inspection", "ActID=?", new String[]{id});
        db.delete("TypesPhoto", "ActID=?", new String[]{id});
        db.delete("Damage", "ActID=?", new String[]{id});
        db.close();
    }
}
