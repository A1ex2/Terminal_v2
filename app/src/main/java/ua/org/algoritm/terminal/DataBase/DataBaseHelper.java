package ua.org.algoritm.terminal.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ua.org.algoritm.terminal.Objects.ActInspection;
import ua.org.algoritm.terminal.Objects.CarData;
import ua.org.algoritm.terminal.Objects.CarDataOutfit;
import ua.org.algoritm.terminal.Objects.Damage;
import ua.org.algoritm.terminal.Objects.Equipment;
import ua.org.algoritm.terminal.Objects.Inspection;
import ua.org.algoritm.terminal.Objects.Photo;
import ua.org.algoritm.terminal.Objects.PhotoActInspection;
import ua.org.algoritm.terminal.Objects.Reception;
import ua.org.algoritm.terminal.Objects.Sector;
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
                + "detail TEXT NOT NULL,"
                + "detailID TEXT NOT NULL,"
                + "typeDetail TEXT NOT NULL,"
                + "mDegreesDamage TEXT NOT NULL,"
                + "mDegreesDamageID TEXT NOT NULL,"
                + "mClassificationDamage TEXT NOT NULL,"
                + "mClassificationDamageID TEXT NOT NULL,"
                + "mOriginDamage TEXT NOT NULL,"
                + "mOriginDamageID TEXT NOT NULL,"
                + "detailDamage TEXT NOT NULL,"
                + "commentDamage TEXT NOT NULL,"
                + "widthDamage TEXT NOT NULL,"
                + "heightDamage TEXT NOT NULL)");
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
                    + "detail TEXT NOT NULL,"
                    + "detailID TEXT NOT NULL,"
                    + "typeDetail TEXT NOT NULL,"
                    + "mDegreesDamage TEXT NOT NULL,"
                    + "mDegreesDamageID TEXT NOT NULL,"
                    + "mClassificationDamage TEXT NOT NULL,"
                    + "mClassificationDamageID TEXT NOT NULL,"
                    + "mOriginDamage TEXT NOT NULL,"
                    + "mOriginDamageID TEXT NOT NULL,"
                    + "detailDamage TEXT NOT NULL,"
                    + "commentDamage TEXT NOT NULL,"
                    + "widthDamage TEXT NOT NULL,"
                    + "heightDamage TEXT NOT NULL)");
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

        return carDataArrayList;
    }

    public void deleteCarData(CarData carData) {
        SQLiteDatabase db = getWritableDatabase();

        String receptionID = carData.getReceptionID();
        String carID = carData.getCarID();

        db.delete("CarData", "ReceptionID=? and carID=?", new String[]{receptionID, carID});
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
        return mPhotos;
    }

    public long insertPhotoActInspection(String actID, String listObject, String
            objectID, String currentPhotoPath) {
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

        return mPhotoArrayList;
    }

    public ArrayList<PhotoActInspection> getPhotoListActInspection(String actID, String
            objectID) {
        ArrayList<PhotoActInspection> mPhotoArrayList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;

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

        return mPhotoArrayList;
    }

    public void deletePhotoActInspection(String currentPhotoPath) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete("ActInspectionPhoto", "currentPhotoPath='" + currentPhotoPath + "'", null);
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
        for (int i = 0; i < receptions.size(); i++) {
            insertReception(receptions.get(i));
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

        return carDatas;
    }

    public void insertActInspectionList(ArrayList<ActInspection> actInspections) {
        for (int i = 0; i < actInspections.size(); i++) {
            insertActInspection(actInspections.get(i));
        }
    }

    public void insertActInspection(ActInspection actInspection) {
        SQLiteDatabase db = getReadableDatabase();
        long id = 0;

        try {
            boolean exist = getActInspectionExist(actInspection.getReceptionID(), actInspection.getCarID());

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

            if (exist) {
                id = db.update("ActInspection", values, "ReceptionID=? and ID=?", new String[]{actInspection.getReceptionID(), actInspection.getCarID()});
            } else {
                id = db.insert("ActInspection", null, values);
            }

            if (id != -1){
                for (int i = 0; i < actInspection.getEquipments().size(); i++) {
                    insertEquipmentActInspection(actInspection.getID(), actInspection.getEquipments().get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        return false;
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
                    actInspection.setTypeMachine(cursor.getString(cursor.getColumnIndex("TypeMachineID")));
                    actInspection.setTypeMachine(cursor.getString(cursor.getColumnIndex("TypeMachine")));
                    actInspection.setPerformed(cursor.getInt(cursor.getColumnIndex("performed")) == 0 ? false : true);

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

        return actInspections;
    }

    public void insertEquipmentActInspection(String ActID, Equipment equipment) {
        SQLiteDatabase db = getReadableDatabase();
        long id = 0;

        try {
            boolean exist = getExist("Equipment","ActID = '" + ActID + "' and equipmentID = '" + equipment.getEquipmentID() + "'");

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
    }

    public void insertInspectionActInspection(String ActID, Inspection inspection) {
        SQLiteDatabase db = getReadableDatabase();
        long id = 0;

        try {
            boolean exist = getExist("Inspection","ActID = '" + ActID + "' and ID = '" + inspection.getID() + "'");

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
    }

    public void insertTypesPhotoActInspection(String ActID, TypesPhoto typesPhoto) {
        SQLiteDatabase db = getReadableDatabase();
        long id = 0;

        try {
            boolean exist = getExist("Inspection","ActID = '" + ActID + "' and typePhotoID = '" + typesPhoto.getTypePhotoID() + "'");

            ContentValues values = new ContentValues();
            values.put("ActID", ActID);
            values.put("typePhotoID", typesPhoto.getTypePhotoID());
            values.put("typePhoto", typesPhoto.getTypePhoto());
            values.put("listObject", typesPhoto.getListObject());

            if (exist) {
                id = db.update("Inspection", values, "ActID=? and typePhotoID=?", new String[]{ActID, typesPhoto.getTypePhotoID()});
            } else {
                id = db.insert("Inspection", null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertDamageActInspection(String ActID, Damage damage) {
//        SQLiteDatabase db = getReadableDatabase();
//        long id = 0;
//
//        try {
//            boolean exist = getExist("Inspection","ActID = '" + ActID + "' and detailID = '" + damage.getDetail().getDetailID() + "'");
//
//            ContentValues values = new ContentValues();
//            values.put("ActID", ActID);
//            values.put("typePhotoID", typesPhoto.getTypePhotoID());
//            values.put("typePhoto", typesPhoto.getTypePhoto());
//            values.put("listObject", typesPhoto.getListObject());
//
//            if (exist) {
//                id = db.update("Inspection", values, "ActID=? and typePhotoID=?", new String[]{ActID, damage.getTypePhotoID()});
//            } else {
//                id = db.insert("Inspection", null, values);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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
            if (cursor != null){
                cursor.close();
            }
        }
        return false;
    }

}
