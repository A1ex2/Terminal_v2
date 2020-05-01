package ua.org.algoritm.terminal.ConnectTo1c;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import ua.org.algoritm.terminal.Objects.ActInspection;
import ua.org.algoritm.terminal.Objects.CarDataOutfit;
import ua.org.algoritm.terminal.Objects.Equipment;
import ua.org.algoritm.terminal.Objects.Inspection;
import ua.org.algoritm.terminal.Objects.OperationOutfits;
import ua.org.algoritm.terminal.Objects.OrderOutfit;
import ua.org.algoritm.terminal.Objects.Photo;

public class JsonParser {

    public static ArrayList<OrderOutfit> getOrderOutfit(String response) throws JSONException {
        ArrayList<OrderOutfit> mOrderOutfits = new ArrayList<>();
        JSONArray ordersJSON = new JSONArray(response);

        for (int i = 0; i < ordersJSON.length(); i++) {
            OrderOutfit orderOutfit = new OrderOutfit();
            JSONObject order = new JSONObject(ordersJSON.get(i).toString());

            orderOutfit.setID(order.getString("ID"));
            orderOutfit.setDescription(order.getString("Description"));
            orderOutfit.setResponsibleID(order.getString("ResponsibleID"));
            orderOutfit.setResponsible(order.getString("Responsible"));
            orderOutfit.setStateID(order.getString("StateID"));
            orderOutfit.setState(order.getString("State"));

            JSONArray CarDataOutfitsJSON = new JSONArray(order.getString("CarDataOutfit"));
            ArrayList<CarDataOutfit> mCarDataOutfits = new ArrayList<>();
            for (int j = 0; j < CarDataOutfitsJSON.length(); j++) {
                JSONObject car = new JSONObject(CarDataOutfitsJSON.get(j).toString());

                CarDataOutfit mCar = new CarDataOutfit();
                mCar.setCarID(car.getString("CarID"));
                mCar.setCar(car.getString("Car"));
                mCar.setSectorID(car.getString("SectorID"));
                mCar.setSector(car.getString("Sector"));
                mCar.setRow(car.getString("Row"));
                mCar.setBarCode(car.getString("BarCode"));

                JSONArray OperationsJSON = new JSONArray(car.getString("Operations"));
                ArrayList<OperationOutfits> mOperation = new ArrayList<>();
                for (int k = 0; k < OperationsJSON.length(); k++) {
                    JSONObject operation = new JSONObject(OperationsJSON.get(k).toString());

                    OperationOutfits mOperationOutfits = new OperationOutfits();
                    mOperationOutfits.setOperationID(operation.getString("OperationID"));
                    mOperationOutfits.setOperation(operation.getString("Operation"));
                    mOperationOutfits.setPerformed(operation.getBoolean("Performed"));
                    mOperationOutfits.setQuantityPhoto(operation.getInt("QuantityPhoto"));
                    try {
                        mOperationOutfits.setDescription(operation.getString("Description"));
                    } catch (Exception e) {
                    }

                    mOperation.add(mOperationOutfits);
                }
                mCar.setOperations(mOperation);

                JSONArray photoJSON = new JSONArray(car.getString("Photo"));
                ArrayList<Photo> mPhotos = new ArrayList<>();
                for (int k = 0; k < photoJSON.length(); k++) {
                    JSONObject photo = new JSONObject(photoJSON.get(k).toString());

                    Photo mPhoto = new Photo();
                    mPhoto.setOrderID(photo.getString("orderID"));
                    mPhoto.setCarID(photo.getString("carID"));
                    mPhoto.setName(photo.getString("name"));
                    mPhoto.setCurrentPhotoPathFTP(photo.getString("currentPhotoPathFTP"));

                    mPhotos.add(mPhoto);
                }
                mCar.setPhoto(mPhotos);

                mCarDataOutfits.add(mCar);
            }
            orderOutfit.setCarDataOutfit(mCarDataOutfits);

            mOrderOutfits.add(orderOutfit);
        }
        return mOrderOutfits;
    }

    public static ArrayList<ActInspection> getActInspection(String response) throws JSONException {
        ArrayList<ActInspection> mActInspection = new ArrayList<>();
        JSONArray ordersJSON = new JSONArray(response);

        for (int i = 0; i < ordersJSON.length(); i++) {
            ActInspection actInspection = new ActInspection();
            JSONObject actJSON = new JSONObject(ordersJSON.get(i).toString());

            actInspection.setID(actJSON.getString("ID"));
            actInspection.setDescription(actJSON.getString("Description"));

            actInspection.setStateID(actJSON.getString("StateID"));
            actInspection.setState(actJSON.getString("State"));

            actInspection.setFormID(actJSON.getString("FormID"));
            actInspection.setForm(actJSON.getString("Form"));

            actInspection.setStorageID(actJSON.getString("StorageID"));
            actInspection.setStorage(actJSON.getString("Storage"));

            actInspection.setInspectionDatePlan(actJSON.getString("InspectionDatePlan"));
            actInspection.setInspectionDateFact(actJSON.getString("InspectionDateFact"));

            actInspection.setCarID(actJSON.getString("CarID"));
            actInspection.setCar(actJSON.getString("Car"));
            actInspection.setProductionDate(actJSON.getString("ProductionDate"));

            actInspection.setBarCode(actJSON.getString("BarCode"));

            actInspection.setSectorID(actJSON.getString("SectorID"));
            actInspection.setSector(actJSON.getString("Sector"));
            actInspection.setRow(actJSON.getString("Row"));

            JSONArray inspectionsJSON = new JSONArray(actJSON.getString("Inspections"));
            ArrayList<Inspection> mInspectionsList = new ArrayList<>();
            for (int j = 0; j < inspectionsJSON.length(); j++) {
                JSONObject inspectionJSON = new JSONObject(inspectionsJSON.get(j).toString());
                Inspection mInspection = new Inspection();

                mInspection.setID(inspectionJSON.getString("ID"));
                mInspection.setName(inspectionJSON.getString("name"));
                mInspection.setPerformed(inspectionJSON.getBoolean("Performed"));

                mInspectionsList.add(mInspection);
            }
            actInspection.setInspections(mInspectionsList);

            JSONArray equipmentsJSON = new JSONArray(actJSON.getString("Equipments"));
            ArrayList<Equipment> mEquipmentsList = new ArrayList<>();
            for (int j = 0; j < equipmentsJSON.length(); j++) {
                JSONObject equipmentJSON = new JSONObject(equipmentsJSON.get(j).toString());
                Equipment mEquipment = new Equipment();

                mEquipment.setEquipmentID(equipmentJSON.getString("EquipmentID"));
                mEquipment.setEquipment(equipmentJSON.getString("Equipment"));
                mEquipment.setQuantityPlan(equipmentJSON.getInt("QuantityPlan"));
                mEquipment.setQuantityFact(equipmentJSON.getInt("QuantityFact"));
//                mEquipment.setNamePhoto(equipmentJSON.getString("namePhoto"));
//                mEquipment.setCurrentPhotoPathFTP(equipmentJSON.getString("currentPhotoPathFTP"));

                mEquipmentsList.add(mEquipment);
            }
            actInspection.setEquipments(mEquipmentsList);

            mActInspection.add(actInspection);
        }
        return mActInspection;
    }
}
