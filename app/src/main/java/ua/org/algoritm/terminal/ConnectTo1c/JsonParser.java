package ua.org.algoritm.terminal.ConnectTo1c;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.Objects.ActInspection;
import ua.org.algoritm.terminal.Objects.CarDataOutfit;
import ua.org.algoritm.terminal.Objects.ClassificationDamage;
import ua.org.algoritm.terminal.Objects.DegreesDamage;
import ua.org.algoritm.terminal.Objects.Detail;
import ua.org.algoritm.terminal.Objects.Equipment;
import ua.org.algoritm.terminal.Objects.Inspection;
import ua.org.algoritm.terminal.Objects.OperationOutfits;
import ua.org.algoritm.terminal.Objects.OrderOutfit;
import ua.org.algoritm.terminal.Objects.OriginDamage;
import ua.org.algoritm.terminal.Objects.Photo;
import ua.org.algoritm.terminal.Objects.Scheme;
import ua.org.algoritm.terminal.Objects.TypeDamage;
import ua.org.algoritm.terminal.Objects.TypeDamagePhoto;
import ua.org.algoritm.terminal.Objects.TypesPhoto;

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

    public static ArrayList<ActInspection> getActInspection(String root) throws JSONException {

        JSONObject rootJSON = new JSONObject(root);
        String actsJSON = rootJSON.getString("Act");
        String schemesJSON = rootJSON.getString("Schemes");

        String typesDamageJSON = rootJSON.getString("TypesDamage");
        String degreesDamageJSON = rootJSON.getString("DegreesDamage");
        String classificationDamageJSON = rootJSON.getString("ClassificationDamage");
        String originDamageJSON = rootJSON.getString("OriginDamage");
        String typeDamagePhotoJSON = rootJSON.getString("TypesDamagePhotos");

        ArrayList<ActInspection> mActInspection = new ArrayList<>();
        JSONArray mActJSON = new JSONArray(actsJSON);

        for (int i = 0; i < mActJSON.length(); i++) {
            ActInspection actInspection = new ActInspection();
            JSONObject actJSON = new JSONObject(mActJSON.get(i).toString());

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

            actInspection.setTypeMachineID(actJSON.getString("TypeMachineID"));
            actInspection.setTypeMachine(actJSON.getString("TypeMachine"));

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

            JSONArray typePhotosJSON = new JSONArray(actJSON.getString("TypePhotos"));
            ArrayList<TypesPhoto> mTypesPhotoList = new ArrayList<>();
            for (int j = 0; j < typePhotosJSON.length(); j++) {
                JSONObject typePhotoJSON = new JSONObject(typePhotosJSON.get(j).toString());

                TypesPhoto mTypesPhoto = new TypesPhoto();
                mTypesPhoto.setTypePhotoID(typePhotoJSON.getString("typePhotoID"));
                mTypesPhoto.setTypePhoto(typePhotoJSON.getString("typePhoto"));

                mTypesPhotoList.add(mTypesPhoto);
            }
            actInspection.setTypesPhotos(mTypesPhotoList);

            mActInspection.add(actInspection);
        }

        ArrayList<Scheme> mSchemes = new ArrayList<>();
        JSONArray mSchemesJSON = new JSONArray(schemesJSON);

        for (int i = 0; i < mSchemesJSON.length(); i++) {
            Scheme mScheme = new Scheme();
            JSONObject schemeJSON = new JSONObject(mSchemesJSON.get(i).toString());

            mScheme.setID(schemeJSON.getString("ID"));
            mScheme.setName(schemeJSON.getString("Name"));
            mScheme.setTypeMachineID(schemeJSON.getString("TypeMachineID"));
            mScheme.setTypeMachine(schemeJSON.getString("TypeMachine"));
            mScheme.setViewSchemesID(schemeJSON.getString("ViewSchemesID"));
            mScheme.setViewSchemes(schemeJSON.getString("ViewSchemes"));
            mScheme.setSVG(schemeJSON.getString("SVG"));

            JSONArray mDetailsJSON = new JSONArray(schemeJSON.getString("Details"));
            ArrayList<Detail> mDetails = new ArrayList<>();
            for (int j = 0; j < mDetailsJSON.length(); j++) {
                Detail detail = new Detail();

                JSONObject detailJSON = new JSONObject(mDetailsJSON.get(j).toString());

                detail.setID(detailJSON.getString("ID"));
                detail.setTempID(Integer.parseInt(detailJSON.getString("ID").replace("p", "")));
                detail.setDetailID(detailJSON.getString("detailID"));
                detail.setDetailName(detailJSON.getString("detailName"));

                mDetails.add(detail);
            }
            mScheme.setDetails(mDetails);

            mSchemes.add(mScheme);
        }

        SharedData.SCHEMES = mSchemes;

        ArrayList<TypeDamage> mTypesDamages = new ArrayList<>();
        JSONArray mTypesDamagesJSON = new JSONArray(typesDamageJSON);
        for (int i = 0; i < mTypesDamagesJSON.length(); i++) {
            TypeDamage typeDamage = new TypeDamage();

            JSONObject typeDamageJSON = new JSONObject(mTypesDamagesJSON.get(i).toString());
            typeDamage.setID(typeDamageJSON.getString("ID"));
            typeDamage.setName(typeDamageJSON.getString("name"));

            mTypesDamages.add(typeDamage);

        }
        SharedData.TypesDamages = mTypesDamages;

        ArrayList<DegreesDamage> mDegreesDamages = new ArrayList<>();
        JSONArray mDegreesDamagesJSON = new JSONArray(degreesDamageJSON);
        for (int i = 0; i < mDegreesDamagesJSON.length(); i++) {
            DegreesDamage degreesDamage = new DegreesDamage();

            JSONObject typeDamageJSON = new JSONObject(mDegreesDamagesJSON.get(i).toString());
            degreesDamage.setID(typeDamageJSON.getString("ID"));
            degreesDamage.setName(typeDamageJSON.getString("name"));

            mDegreesDamages.add(degreesDamage);

        }
        SharedData.DegreesDamages = mDegreesDamages;

        ArrayList<ClassificationDamage> mClassificationDamage = new ArrayList<>();
        JSONArray mClassificationDamageJSON = new JSONArray(classificationDamageJSON);
        for (int i = 0; i < mClassificationDamageJSON.length(); i++) {
            ClassificationDamage classificationDamage = new ClassificationDamage();

            JSONObject typeDamageJSON = new JSONObject(mClassificationDamageJSON.get(i).toString());
            classificationDamage.setID(typeDamageJSON.getString("ID"));
            classificationDamage.setName(typeDamageJSON.getString("name"));

            mClassificationDamage.add(classificationDamage);
        }
        SharedData.ClassificationDamages = mClassificationDamage;

        ArrayList<OriginDamage> mOriginDamage = new ArrayList<>();
        JSONArray mOriginDamageJSON = new JSONArray(originDamageJSON);
        for (int i = 0; i < mOriginDamageJSON.length(); i++) {
            OriginDamage originDamage = new OriginDamage();

            JSONObject typeDamageJSON = new JSONObject(mOriginDamageJSON.get(i).toString());
            originDamage.setID(typeDamageJSON.getString("ID"));
            originDamage.setName(typeDamageJSON.getString("name"));

            mOriginDamage.add(originDamage);
        }
        SharedData.OriginDamages = mOriginDamage;

        ArrayList<TypeDamagePhoto> mTypeDamagePhoto = new ArrayList<>();
        JSONArray mTypeDamagePhotoJSON = new JSONArray(typeDamagePhotoJSON);
        for (int i = 0; i < mTypeDamagePhotoJSON.length(); i++) {
            TypeDamagePhoto typeDamagePhoto = new TypeDamagePhoto();

            JSONObject typeDamageJSON = new JSONObject(mTypeDamagePhotoJSON.get(i).toString());
            typeDamagePhoto.setID(typeDamageJSON.getString("ID"));
            typeDamagePhoto.setName(typeDamageJSON.getString("name"));

            mTypeDamagePhoto.add(typeDamagePhoto);
        }
        SharedData.TypeDamagePhotos = mTypeDamagePhoto;

        return mActInspection;
    }
}
