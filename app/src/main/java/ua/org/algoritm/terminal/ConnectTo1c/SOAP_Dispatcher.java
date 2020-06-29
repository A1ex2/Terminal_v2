package ua.org.algoritm.terminal.ConnectTo1c;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ua.org.algoritm.terminal.Activity.ActInspectionActivity;
import ua.org.algoritm.terminal.Activity.CarActivityIssuance;
import ua.org.algoritm.terminal.Activity.CarActivityMoving;
import ua.org.algoritm.terminal.Activity.CarActivityOrderOutfit;
import ua.org.algoritm.terminal.Activity.CarDataList;
import ua.org.algoritm.terminal.Activity.DetailOrderOutfit;
import ua.org.algoritm.terminal.Activity.DetailReception;
import ua.org.algoritm.terminal.Activity.Password;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.MainActivity;
import ua.org.algoritm.terminal.Objects.ActInspection;
import ua.org.algoritm.terminal.Objects.CarData;
import ua.org.algoritm.terminal.Objects.CarDataIssuance;
import ua.org.algoritm.terminal.Objects.CarDataOutfit;
import ua.org.algoritm.terminal.Objects.Issuance;
import ua.org.algoritm.terminal.Objects.OperationOutfits;
import ua.org.algoritm.terminal.Objects.OrderOutfit;
import ua.org.algoritm.terminal.Objects.Reception;
import ua.org.algoritm.terminal.Objects.Sector;
import ua.org.algoritm.terminal.Objects.User;
import ua.org.algoritm.terminal.ui.acceptance.AcceptanceFragment;
import ua.org.algoritm.terminal.ui.act.ActInspectionFragment;
import ua.org.algoritm.terminal.ui.issuance.IssuanceFragment;
import ua.org.algoritm.terminal.ui.order.OrderOutfitFragment;

import static android.content.Context.MODE_PRIVATE;


public class SOAP_Dispatcher extends Thread {

    public static final Integer soapParam_timeout = 220000;
    public static String soapParam_pass = "31415926";
    public static String soapParam_user = "Администратор";
    public static String soapParam_URL;
    public String string_Inquiry;
    public Context mContext;

    int timeout;
    String URL;
    String user;
    String pass;
    int ACTION;
    SoapObject soap_Response;
    String soap_ResponseString;
    final String NAMESPACE = "www.URI.com";//"ReturnPhones_XDTO";
    String mSoapParam_URL;
    int attempt;
    Boolean thisGet;

    public SOAP_Dispatcher(int SOAP_ACTION, String sParam_user, String sParam_pass, Context context) {
        setSoapParamURL();

        timeout = soapParam_timeout;
        URL = soapParam_URL;
        user = sParam_user;
        pass = sParam_pass;
        ACTION = SOAP_ACTION;
        mSoapParam_URL = soapParam_URL;
        mContext = context;
        attempt = 0;
        thisGet = false;
    }

    public SOAP_Dispatcher(int SOAP_ACTION, Context context) {
        setSoapParamURL();

        timeout = soapParam_timeout;
        URL = soapParam_URL;
        user = soapParam_user;
        pass = soapParam_pass;
        ACTION = SOAP_ACTION;
        mSoapParam_URL = soapParam_URL;
        mContext = context;
        attempt = 0;
        thisGet = false;
    }

    private void setSoapParamURL() {
        String server;
        try {
            server = SharedData.API;
            if (server.equals("")) {
                server = "http://217.25.195.61:83/blg_log";
                //server = "http://192.168.1.10/blg_log";
            }
        } catch (Exception e) {
            server = "http://217.25.195.61:83/blg_log";
        }

        soapParam_URL = server + "/ws/terminal.1cws";
    }

    @Override
    public void run() {

        switch (ACTION) {
            case Password.ACTION_VERIFY:
                login();
                break;

            case Password.ACTION_LOGIN_LIST:
                getLoginList();
                break;

            case AcceptanceFragment.ACTION_RECEPTION_LIST:
                getReceptionList();
                if (SharedData.isActInspection) {
                    GetActInspection();
                }
                break;

            case DetailReception.ACTION_UPDATE_RECEPTION:
                getReceptionList();
                break;

            case MainActivity.ACTION_SECTORS_LIST:
                getSectors();
                break;

            case Password.ACTION_UPDATE:
                checkUpdate();
                break;

            case Password.ACTION_UPDATE_NEW_VERSION:
                getApplication();
                break;

            case DetailReception.ACTION_SET_RECEPTION:
                setCB();
                break;

            case CarDataList.ACTION_CAR_LIST:
                getCarList();
                break;

            case CarActivityMoving.ACTION_SET_MOVING_CAR:
                setMovingCB();
                break;

            case IssuanceFragment.ACTION_LIST:
                getIssuanceList();
                break;

            case CarActivityIssuance.ACTION_SET_ISSUANCE_CAR:
                setIssuanceCB();
                break;

            case OrderOutfitFragment.ACTION_LIST:
                GetOrderOutfitsList();
                break;

            case CarActivityOrderOutfit.ACTION_SET_CAR_Outfit:
                setOrderOutfit();
                break;
            case DetailOrderOutfit.ACTION_SET_Outfit:
                setStatusOutfit();
                break;
            case ActInspectionFragment.ACTION_LIST:
                GetActInspection();
                break;
            case ActInspectionActivity.ACTION_SET_ACT:
                setActInspection();
                break;
            case ActInspectionActivity.ACTION_SET_ACT_Performed:
                setStatusActInspection();
                break;
        }

        if (ACTION == Password.ACTION_VERIFY | ACTION == Password.ACTION_LOGIN_LIST
                | ACTION == Password.ACTION_UPDATE | ACTION == Password.ACTION_UPDATE_NEW_VERSION) {

            if (soap_Response != null & ACTION == Password.ACTION_VERIFY) {
                Password.soapParam_Response = soap_Response;
                Password.soapHandler.sendEmptyMessage(ACTION);

            } else if (soap_Response != null & ACTION == Password.ACTION_LOGIN_LIST) {
                Password.soapHandler.sendEmptyMessage(ACTION);

            } else if (soap_Response != null & ACTION == Password.ACTION_UPDATE
                    | ACTION == Password.ACTION_UPDATE_NEW_VERSION) {
                Password.soapParam_Response_Update = soap_Response;
                Password.soapHandler.sendEmptyMessage(ACTION);

            } else {
                Password.soapHandler.sendEmptyMessage(Password.ACTION_ConnectionError);
            }

        } else if (ACTION == MainActivity.ACTION_SECTORS_LIST) {
            if (soap_Response != null) {
                MainActivity.soapParam_Response = soap_Response;
                MainActivity.soapHandler.sendEmptyMessage(ACTION);
            } else {
                MainActivity.soapHandler.sendEmptyMessage(MainActivity.ACTION_ConnectionError);
            }

        } else if (ACTION == AcceptanceFragment.ACTION_RECEPTION_LIST) {
            if (soap_Response != null) {
                AcceptanceFragment.soapHandler.sendEmptyMessage(ACTION);
            } else {
                AcceptanceFragment.soapHandler.sendEmptyMessage(MainActivity.ACTION_ConnectionError);
            }

        } else if (ACTION == DetailReception.ACTION_SET_RECEPTION | ACTION == DetailReception.ACTION_SET_RECEPTION |
                ACTION == DetailReception.ACTION_UPDATE_RECEPTION) {

            if (soap_Response != null & ACTION == DetailReception.ACTION_SET_RECEPTION) {
                DetailReception.soapParam_Response = soap_Response;
                DetailReception.soapHandler.sendEmptyMessage(ACTION);
            } else if (soap_Response != null & ACTION == DetailReception.ACTION_UPDATE_RECEPTION) {
                DetailReception.soapHandler.sendEmptyMessage(ACTION);
            } else {
                DetailReception.soapHandler.sendEmptyMessage(DetailReception.ACTION_ConnectionError);
            }

        } else if (ACTION == CarDataList.ACTION_CAR_LIST) {
            if (soap_Response != null) {
                CarDataList.soapParam_Response = soap_Response;
                CarDataList.soapHandler.sendEmptyMessage(ACTION);
            } else {
                CarDataList.soapHandler.sendEmptyMessage(CarDataList.ACTION_ConnectionError);
            }

        } else if (ACTION == CarActivityMoving.ACTION_SET_MOVING_CAR) {
            if (soap_Response != null) {
                CarActivityMoving.soapParam_Response = soap_Response;
                CarActivityMoving.soapHandler.sendEmptyMessage(ACTION);
            } else {
                CarActivityMoving.soapHandler.sendEmptyMessage(CarActivityMoving.ACTION_ConnectionError);
            }
        } else if (ACTION == IssuanceFragment.ACTION_LIST) {
            if (soap_Response != null) {
                IssuanceFragment.soapParam_Response = soap_Response;
                IssuanceFragment.soapHandler.sendEmptyMessage(ACTION);
            } else {
                IssuanceFragment.soapHandler.sendEmptyMessage(IssuanceFragment.ACTION_ConnectionError);
            }
        } else if (ACTION == CarActivityIssuance.ACTION_SET_ISSUANCE_CAR) {
            if (soap_Response != null) {
                CarActivityIssuance.soapParam_Response = soap_Response;
                CarActivityIssuance.soapHandler.sendEmptyMessage(ACTION);
            } else {
                CarActivityIssuance.soapHandler.sendEmptyMessage(CarActivityIssuance.ACTION_ConnectionError);
            }
        } else if (ACTION == OrderOutfitFragment.ACTION_LIST) {
            if (!soap_ResponseString.equals("")) {
                OrderOutfitFragment.soapHandler.sendEmptyMessage(ACTION);
            } else {
                OrderOutfitFragment.soapHandler.sendEmptyMessage(OrderOutfitFragment.ACTION_ConnectionError);
            }
        } else if (ACTION == CarActivityOrderOutfit.ACTION_SET_CAR_Outfit) {
            if (soap_Response != null) {
                CarActivityOrderOutfit.soapParam_Response = soap_Response;
                CarActivityOrderOutfit.soapHandler.sendEmptyMessage(ACTION);
            } else {
                CarActivityOrderOutfit.soapHandler.sendEmptyMessage(CarActivityOrderOutfit.ACTION_ConnectionError);
            }
        } else if (ACTION == DetailOrderOutfit.ACTION_SET_Outfit) {
            if (soap_Response != null) {
                DetailOrderOutfit.soapParam_Response = soap_Response;
                DetailOrderOutfit.soapHandler.sendEmptyMessage(ACTION);
            } else {
                DetailOrderOutfit.soapHandler.sendEmptyMessage(DetailOrderOutfit.ACTION_ConnectionError);
            }
        } else if (ACTION == ActInspectionFragment.ACTION_LIST) {
            if (!soap_ResponseString.equals("")) {
                ActInspectionFragment.soapHandler.sendEmptyMessage(ACTION);
            } else {
                ActInspectionFragment.soapHandler.sendEmptyMessage(ActInspectionFragment.ACTION_ConnectionError);
            }
        } else if (ACTION == ActInspectionActivity.ACTION_SET_ACT) {
            if (soap_Response != null) {
                ActInspectionActivity.soapParam_Response = soap_Response;
                ActInspectionActivity.soapHandler.sendEmptyMessage(ACTION);
            } else {
                ActInspectionActivity.soapHandler.sendEmptyMessage(ActInspectionActivity.ACTION_ConnectionError);
            }
        } else if (ACTION == ActInspectionActivity.ACTION_SET_ACT_Performed) {
            if (soap_Response != null) {
                ActInspectionActivity.soapParam_Response = soap_Response;
                ActInspectionActivity.soapHandler.sendEmptyMessage(ACTION);
            } else {
                ActInspectionActivity.soapHandler.sendEmptyMessage(ActInspectionActivity.ACTION_ConnectionError);
            }
        }
    }

    private void setActInspection() {
        String method = "setActInspection";
        String action = NAMESPACE + "#setActInspection:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        request.addProperty("ActInspection", string_Inquiry);

        soap_Response = callWebService(request, action);
    }

    private void setStatusActInspection() {
        String method = "setStatusActInspection";
        String action = NAMESPACE + "#setStatusActInspection:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        request.addProperty("ActInspection", string_Inquiry);

        soap_Response = callWebService(request, action);
    }

    private void setStatusOutfit() {
        String method = "setStatusOrderOutfit";
        String action = NAMESPACE + "#setStatusOrderOutfit:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        request.addProperty("CarData", string_Inquiry);

        soap_Response = callWebService(request, action);
    }

    private void setOrderOutfit() {
        String method = "setOrderOutfit";
        String action = NAMESPACE + "#setOrderOutfit:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        request.addProperty("CarData", string_Inquiry);

        soap_Response = callWebService(request, action);

        //new FilesUploadingTask("/storage/emulated/0/Android/data/ua.org.algoritm.terminal/files/Pictures/JPEG_20200223_145617_1416345249.jpg", user, pass).execute();
    }

    private void setIssuanceCB() {
        String method = "setIssuance";
        String action = NAMESPACE + "#setIssuance:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        request.addProperty("CarDataIssuance", string_Inquiry);
        soap_Response = callWebService(request, action);
    }

    private void getIssuanceList() {
        thisGet = true;

        String method = "GetIssuanceListNew";
        String action = NAMESPACE + "#GetIssuanceListNew:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        soap_Response = callWebService(request, action);

        try {
            int count = soap_Response.getPropertyCount();
            ArrayList<Issuance> mIssuances = SharedData.ISSUANCE;
            mIssuances.clear();

            for (int i = 0; i < count; i++) {
                SoapObject issuanceList = (SoapObject) soap_Response.getProperty(i);

                Issuance issuance = new Issuance();
                issuance.setID(issuanceList.getPropertyAsString("ID"));
                issuance.setDescription(issuanceList.getPropertyAsString("Description"));
                issuance.setAutoNumber(issuanceList.getPropertyAsString("AutoNumber"));
                issuance.setDriver(issuanceList.getPropertyAsString("Driver"));
                issuance.setDriverPhone(issuanceList.getPropertyAsString("DriverPhone"));

                ArrayList<CarDataIssuance> carDataList = new ArrayList<>();

                for (int j = 0; j < issuanceList.getPropertyCount(); j++) {
                    PropertyInfo pi = new PropertyInfo();
                    issuanceList.getPropertyInfo(j, pi);
                    Object property = issuanceList.getProperty(j);
                    if (pi.name.equals("CarDataIssuance") && property instanceof SoapObject) {
                        SoapObject carDetail = (SoapObject) property;

                        CarDataIssuance mCarDataIssuance = new CarDataIssuance();

                        mCarDataIssuance.setIssuanceID(carDetail.getPrimitivePropertyAsString("IssuanceID"));

                        mCarDataIssuance.setDescription(issuance.getDescription());
                        mCarDataIssuance.setAutoNumber(issuance.getAutoNumber());
                        mCarDataIssuance.setDriver(issuance.getDriver());
                        mCarDataIssuance.setDriverPhone(issuance.getDriverPhone());

                        mCarDataIssuance.setCarID(carDetail.getPrimitivePropertyAsString("CarID"));
                        mCarDataIssuance.setCar(carDetail.getPrimitivePropertyAsString("Car"));
                        mCarDataIssuance.setSectorID(carDetail.getPrimitivePropertyAsString("SectorID"));
                        mCarDataIssuance.setSector(carDetail.getPrimitivePropertyAsString("Sector"));
                        mCarDataIssuance.setRow(carDetail.getPrimitivePropertyAsString("Row"));

                        carDataList.add(mCarDataIssuance);
                    }
                }
                issuance.setCarData(carDataList);

                mIssuances.add(issuance);
            }

            SharedData.updateReceptionsDB();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void GetOrderOutfitsList() {
        thisGet = true;

        String method = "GetOrderOutfitsList";
        String action = NAMESPACE + "#GetOrderOutfitsList:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        soap_ResponseString = callWebServiceString(request, action);

        try {

            ArrayList<OrderOutfit> mOrderOutfits = SharedData.ORDER_OUTFIT;
            mOrderOutfits.clear();
            mOrderOutfits.addAll(JsonParser.getOrderOutfit(soap_ResponseString));

            SharedData.checkPhoto(mOrderOutfits);

            for (int i = 0; i < mOrderOutfits.size(); i++) {
                SharedData.setPhoto(mOrderOutfits.get(i));
            }

//            GsonBuilder builder = new GsonBuilder();
//            Gson gson = builder.create();
//
//            ArrayList<OrderOutfit> mOrderOutfits = SharedData.ORDER_OUTFIT;
//            mOrderOutfits.clear();
//
//            Type itemsListType = new TypeToken<List<OrderOutfit>>() {}.getType();
//            List<OrderOutfit> listOrderOutfit = new Gson().fromJson(soap_ResponseString, itemsListType);
//
//            for (int i = 0; i < listOrderOutfit.size(); i++) {
//
//                //mOrderOutfits.add(listOrderOutfit[i]);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//        boolean test = true;
//        if (test) {
//            soap_ResponseString = "1";
//
//            ArrayList<OrderOutfit> mOrderOutfits = new ArrayList<>();
//
//            OrderOutfit orderOutfit = new OrderOutfit();
//
//            orderOutfit.setID("ID");
//            orderOutfit.setDescription("Description");
//            orderOutfit.setResponsibleID("ResponsibleID");
//            orderOutfit.setResponsible("Responsible");
//            orderOutfit.setStateID("StateID");
//            orderOutfit.setState("State");
//
//            ArrayList<CarDataOutfit> mCarDataOutfits = new ArrayList<>();
//
//            CarDataOutfit mCar = new CarDataOutfit();
//            mCar.setCarID("CarID");
//            mCar.setCar("Car");
//            mCar.setSectorID("SectorID");
//            mCar.setSector("Sector");
//            mCar.setRow("Row");
//            mCar.setBarCode("BarCode");
//
//            ArrayList<OperationOutfits> mOperation = new ArrayList<>();
//
//            OperationOutfits mOperationOutfits = new OperationOutfits();
//            mOperationOutfits.setOperationID("OperationID");
//            mOperationOutfits.setOperation("Operation");
//
//            mOperation.add(mOperationOutfits);
//
//            mCar.setOperations(mOperation);
//
//            mCarDataOutfits.add(mCar);
//
//            orderOutfit.setCarDataOutfit(mCarDataOutfits);
//
//            mOrderOutfits.add(orderOutfit);
//
//            ArrayList<OrderOutfit> _mOrderOutfits = SharedData.ORDER_OUTFIT;
//            _mOrderOutfits.clear();
//            _mOrderOutfits.addAll(mOrderOutfits);
//        }

    }

    private void GetActInspection() {
        if (SharedData.isOfflineReception & !SharedData.updateActInspectionListDB) {
            ArrayList<ActInspection> mActInspections = SharedData.ACT_INSPECTION;
            mActInspections.clear();

            soap_Response = new SoapObject();

            SharedData.getCatalog();
            SharedData.getActInspectionList();

            SharedData.checkPhotoAct();

            for (int i = 0; i < mActInspections.size(); i++) {
                SharedData.setPhotoActInspection(mActInspections.get(i));
            }

            return;
        }

        thisGet = true;

        String method = "GetActInspection";
        String action = NAMESPACE + "#GetActInspection:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        soap_ResponseString = callWebServiceString(request, action);

        try {

            ArrayList<ActInspection> mActInspections = SharedData.ACT_INSPECTION;
            mActInspections.clear();
            mActInspections.addAll(JsonParser.getActInspection(soap_ResponseString));

//            SharedData.checkPhoto(mOrderOutfits);

            SharedData.checkPhotoAct();

            for (int i = 0; i < mActInspections.size(); i++) {
                SharedData.setPhotoActInspection(mActInspections.get(i));
            }

            if (SharedData.isOfflineReception & SharedData.updateActInspectionListDB) {
                SharedData.updateActInspectionListDB = false;
                SharedData.insertCatalog();
                SharedData.insertActInspectionList();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMovingCB() {
        String method = "setMoving";
        String action = NAMESPACE + "#setMoving:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        request.addProperty("CarData", string_Inquiry);
        soap_Response = callWebService(request, action);
    }

    private void getCarList() {
        thisGet = true;

        String method = "GetCarList";
        String action = NAMESPACE + "#GetCarList:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        soap_Response = callWebService(request, action);
    }

    private void setCB() {
        String method = "setReception";
        String action = NAMESPACE + "#setReception:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        request.addProperty("Reception", string_Inquiry);
        soap_Response = callWebService(request, action);
    }

    private void checkUpdate() {
        thisGet = true;

        String method = "checkUpdate";
        String action = NAMESPACE + "#checkUpdate:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        request.addProperty("Version", SharedData.VERSION);
        soap_Response = callWebService(request, action);

    }

    private void getApplication() {
        thisGet = true;

        String method = "getApplication";
        String action = NAMESPACE + "#getApplication:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        soap_Response = callWebService(request, action);

    }

    void getReceptionList() {
        if (SharedData.isOfflineReception & !SharedData.updateReceptionListDB) {
            ArrayList<Reception> mReceptions = SharedData.RECEPTION;
            mReceptions.clear();

            soap_Response = new SoapObject();

            SharedData.getReceptionList();

            SharedData.updateReceptionsDB();
            return;
        }

        thisGet = true;

        String method = "GetReceptionList";
        String action = NAMESPACE + "#returnReceptionList:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        soap_Response = callWebService(request, action);

        try {
            int count = soap_Response.getPropertyCount();
            ArrayList<Reception> mReceptions = SharedData.RECEPTION;
            mReceptions.clear();

            for (int i = 0; i < count; i++) {
                SoapObject receptionList = (SoapObject) soap_Response.getProperty(i);

                Reception reception = new Reception();
                reception.setID(receptionList.getPropertyAsString("ID"));
                reception.setDescription(receptionList.getPropertyAsString("Description"));
                reception.setAutoNumber(receptionList.getPropertyAsString("AutoNumber"));
                reception.setDriver(receptionList.getPropertyAsString("Driver"));
                reception.setDriverPhone(receptionList.getPropertyAsString("DriverPhone"));
                reception.setInvoiceNumber(receptionList.getPropertyAsString("InvoiceNumber"));

                ArrayList<CarData> carDataList = new ArrayList<>();

                for (int j = 0; j < receptionList.getPropertyCount(); j++) {
                    PropertyInfo pi = new PropertyInfo();
                    receptionList.getPropertyInfo(j, pi);
                    Object property = receptionList.getProperty(j);
                    if (pi.name.equals("CarData") && property instanceof SoapObject) {
                        SoapObject carDetail = (SoapObject) property;

                        CarData carData = new CarData();
                        carData.setReceptionID(reception.getID());
                        carData.setCarID(carDetail.getPrimitivePropertyAsString("CarID"));
                        carData.setCar(carDetail.getPrimitivePropertyAsString("Car"));
                        carData.setBarCode(carDetail.getPrimitivePropertyAsString("BarCode"));
                        carData.setSectorID(carDetail.getPrimitivePropertyAsString("SectorID"));
                        carData.setSector(carDetail.getPrimitivePropertyAsString("Sector"));
                        carData.setRow(carDetail.getPrimitivePropertyAsString("Row"));
                        try {
                            carData.setProductionDate(carDetail.getPrimitivePropertyAsString("ProductionDate"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        carDataList.add(carData);
                    }
                }
                reception.setCarData(carDataList);

                mReceptions.add(reception);
            }

            SharedData.updateReceptionsDB();

            if (SharedData.isOfflineReception & SharedData.updateReceptionListDB) {
                SharedData.updateReceptionListDB = false;
                SharedData.insertReceptionList();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getLoginList() {
        thisGet = true;

        String method = "GetLoginList";
        String action = NAMESPACE + "#returnLoginList:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        soap_Response = callWebService(request, action);

        try {
            int count = soap_Response.getPropertyCount();
            ArrayList<User> users = SharedData.USERS;
            users.clear();

            for (int i = 0; i < count; i++) {
                SoapObject login = (SoapObject) soap_Response.getProperty(i);

                User user = new User();
                user.setName(login.getPropertyAsString("Description"));
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void login() {
        thisGet = true;

        String method = "Login";
        String action = NAMESPACE + "#Login:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);

        request.addProperty("Login", Password.mLogin);

        String wsParam_PassHash = AeSimpleSHA1.getPassHash(Password.mPassword);
        request.addProperty("Password", wsParam_PassHash);
        soap_Response = callWebService(request, action);

    }

    void getSectors() {
        thisGet = true;

        String method = "GetSectorList";
        String action = NAMESPACE + "#returnSectors:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        soap_Response = callWebService(request, action);

        try {
            int count = soap_Response.getPropertyCount();
            ArrayList<Sector> sectors = SharedData.SECTORS;
            sectors.clear();

            Sector mSector = new Sector();
            mSector.setID("0");
            mSector.setName("");
            sectors.add(mSector);

            for (int i = 0; i < count; i++) {
                SoapObject sectorList = (SoapObject) soap_Response.getProperty(i);

                Sector sector = new Sector();
                sector.setID(sectorList.getPrimitivePropertyAsString("ID"));
                sector.setName(sectorList.getPrimitivePropertyAsString("Name"));

                sectors.add(sector);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SoapObject callWebService(SoapObject request, String action) {

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        HttpTransportSE androidHttpTransport = new HttpTransportBasicAuthSE(URL, user, pass, timeout);
        androidHttpTransport.debug = false;

        try {
            androidHttpTransport.call(action, envelope);
            attempt = 100;

            return (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();

            Log.d("myLogsTerminal", "" + attempt + " / " + action + " / " + e.toString());
            attempt++;
            if (SharedData.isOnline(mContext)) {
                if (attempt < 50 && thisGet) {
                    return callWebService(request, action);
                }
            }
        }

        return null;
    }

    private String callWebServiceString(SoapObject request, String action) {

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        HttpTransportSE androidHttpTransport = new HttpTransportBasicAuthSE(URL, user, pass, timeout);
        androidHttpTransport.debug = false;

        try {
            androidHttpTransport.call(action, envelope);
            attempt = 100;

            return envelope.getResponse().toString();
        } catch (Exception e) {
            //e.printStackTrace();
            Log.d("myLogsTerminal", "" + attempt + " / " + action + " / " + e.toString());
            attempt++;
            if (SharedData.isOnline(mContext)) {
                if (attempt < 50 && thisGet) {
                    return callWebServiceString(request, action);
                }
            }
        }

        return "";
    }
}

