package zskj.jkxt.api;

import android.os.StrictMode;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import zskj.jkxt.JKXTApplication;

/**
 * Created by WYY on 2017/2/24.
 */

public class WebService {
    static public String namespace = "http://tempuri.org/";
    static public String serviceUrl = "http://192.168.11.77/H9000Service.asmx";
    static final String ERRORMSG = "获取数据失败";
    static WebService service = null;

    // static public String namespace = "http://WebXml.com.cn/";
    // static public String serviceUrl =
    // "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx";
    private WebService() {
    }

    public static WebService getInstance() {
        if (service == null) {
            service = new WebService();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
                    .detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
        }
        return service;
    }

    public void isUser(String userName, String password, RequestCallback callback) {
        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            callback.onFail(ERRORMSG);
            return;
        }
        String methodName = "isUser";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("userName", userName);
        request.addProperty("password", password);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                callback.onSuccess(object.getProperty(0).toString());
                System.out.println("result-------------->" + object.getProperty(0).toString());
            }
            else
                callback.onFail(ERRORMSG);
        } catch (Exception e) {
            callback.onFail(e.getMessage());
        }
    }
}
