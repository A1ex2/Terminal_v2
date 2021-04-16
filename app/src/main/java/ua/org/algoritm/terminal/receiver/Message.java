package ua.org.algoritm.terminal.receiver;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Dispatcher;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.ConnectTo1c.HttpTransportBasicAuthSE;
import ua.org.algoritm.terminal.R;

public class Message {
    public static final int ACTION = 333333;

    public Boolean isMessage = false;
    public String text = "";

    private String user;
    private String pass;
    private Context mContext;

    public Message(String sParam_user, String sParam_pass, Context context) {
        user = sParam_user;
        pass = sParam_pass;
        mContext = context;
    }

    public void getNotifications() {

        SharedData.updateActInspectionListDB = true;

        SOAP_Dispatcher soap_dispatcher = new SOAP_Dispatcher(ACTION, user, pass, mContext);
        soap_dispatcher.GetActInspection();

            if (SharedData.newActInspection) {
                isMessage = true;
                text = "Загружены новые акты на осмотр.";
                SharedData.newActInspection = false;
            } else {
                isMessage = false;
            }
    }
}
