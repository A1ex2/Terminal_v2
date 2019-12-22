package ua.org.algoritm.terminal.ConnectTo1c;

import android.content.SharedPreferences;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.util.ArrayList;

import ua.org.algoritm.terminal.Activity.CarActivityIssuance;
import ua.org.algoritm.terminal.Activity.CarActivityMoving;
import ua.org.algoritm.terminal.Activity.CarDataList;
import ua.org.algoritm.terminal.Activity.DetailReception;
import ua.org.algoritm.terminal.Activity.Password;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.MainActivity;
import ua.org.algoritm.terminal.Objects.CarData;
import ua.org.algoritm.terminal.Objects.Reception;
import ua.org.algoritm.terminal.Objects.Sector;
import ua.org.algoritm.terminal.Objects.User;
import ua.org.algoritm.terminal.ui.acceptance.AcceptanceFragment;
import ua.org.algoritm.terminal.ui.issuance.IssuanceFragment;

import static android.content.Context.MODE_PRIVATE;


public class SOAP_Dispatcher extends Thread {

    public static final Integer soapParam_timeout = 100;
    public static String soapParam_pass = "31415926";
    public static String soapParam_user = "Администратор";
    //public static String soapParam_URL = "http://gate.algoritm.org.ua:8778/blg_log_test/ws/terminal.1cws";
    //public static String soapParam_URL = "http://192.168.1.10/blg/ws/terminal.1cws";
    //public static String soapParam_URL = "http://217.25.195.61:83/blg_log_dev/ws/terminal.1cws";
//    public static String soapParam_URL = "http://192.168.1.4:8090/blg_log/ws/terminal.1cws";
    public static String soapParam_URL;
    public String string_Inquiry;

    int timeout;
    String URL;
    String user;
    String pass;
    int ACTION;
    SoapObject soap_Response;
    final String NAMESPACE = "www.URI.com";//"ReturnPhones_XDTO";
    String mSoapParam_URL;

    public SOAP_Dispatcher(int SOAP_ACTION, String sParam_user, String sParam_pass) {
        setSoapParamURL();

        timeout = soapParam_timeout;
        URL = soapParam_URL;
        user = sParam_user;
        pass = sParam_pass;
        ACTION = SOAP_ACTION;
        mSoapParam_URL = soapParam_URL;
    }

    public SOAP_Dispatcher(int SOAP_ACTION) {
        setSoapParamURL();

        timeout = soapParam_timeout;
        URL = soapParam_URL;
        user = soapParam_user;
        pass = soapParam_pass;
        ACTION = SOAP_ACTION;
        mSoapParam_URL = soapParam_URL;
    }

    private void setSoapParamURL() {
        String server = SharedData.API;
        if (server.equals("")) {
            // server = "http://217.25.195.61:83/blg_log_dev";
            server = "http://192.168.1.10/blg_log";
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
                CarDataList.soapHandler.sendEmptyMessage(DetailReception.ACTION_ConnectionError);
            }

        } else if (ACTION == CarActivityMoving.ACTION_SET_MOVING_CAR) {
            if (soap_Response != null) {
                CarActivityMoving.soapParam_Response = soap_Response;
                CarActivityMoving.soapHandler.sendEmptyMessage(ACTION);
            } else {
                CarActivityMoving.soapHandler.sendEmptyMessage(DetailReception.ACTION_ConnectionError);
            }
        } else if (ACTION == IssuanceFragment.ACTION_LIST) {
            if (soap_Response != null) {
                IssuanceFragment.soapParam_Response = soap_Response;
                IssuanceFragment.soapHandler.sendEmptyMessage(ACTION);
            } else {
                IssuanceFragment.soapHandler.sendEmptyMessage(DetailReception.ACTION_ConnectionError);
            }
        } else if (ACTION == CarActivityIssuance.ACTION_SET_ISSUANCE_CAR) {
            if (soap_Response != null) {
                CarActivityIssuance.soapParam_Response = soap_Response;
                CarActivityIssuance.soapHandler.sendEmptyMessage(ACTION);
            } else {
                CarActivityIssuance.soapHandler.sendEmptyMessage(DetailReception.ACTION_ConnectionError);
            }
        }
    }

    private void setIssuanceCB() {
        String method = "setIssuance";
        String action = NAMESPACE + "#setIssuance:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        request.addProperty("CarDataIssuance", string_Inquiry);
        soap_Response = callWebService(request, action);
    }

    private void getIssuanceList() {
        String method = "GetIssuanceList";
        String action = NAMESPACE + "#GetIssuanceList:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        soap_Response = callWebService(request, action);
    }

    private void setMovingCB() {
        String method = "setMoving";
        String action = NAMESPACE + "#setMoving:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        request.addProperty("CarData", string_Inquiry);
        soap_Response = callWebService(request, action);
    }

    private void getCarList() {
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

        String method = "checkUpdate";
        String action = NAMESPACE + "#checkUpdate:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        request.addProperty("Version", SharedData.VERSION);
        soap_Response = callWebService(request, action);

    }

    private void getApplication() {

        String method = "getApplication";
        String action = NAMESPACE + "#getApplication:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        soap_Response = callWebService(request, action);

    }

    void getReceptionList() {

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getLoginList() {

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

        String method = "Login";
        String action = NAMESPACE + "#Login:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);

        request.addProperty("Login", Password.mLogin);

        String wsParam_PassHash = AeSimpleSHA1.getPassHash(Password.mPassword);
        request.addProperty("Password", wsParam_PassHash);
        soap_Response = callWebService(request, action);

    }

    void getSectors() {

        String method = "GetSectorList";
        String action = NAMESPACE + "#returnSectors:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        soap_Response = callWebService(request, action);

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
    }

    private SoapObject callWebService(SoapObject request, String action) {

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        HttpTransportSE androidHttpTransport = new HttpTransportBasicAuthSE(URL, user, pass);
        androidHttpTransport.debug = false;

        try {
            androidHttpTransport.call(action, envelope);
            return (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}

