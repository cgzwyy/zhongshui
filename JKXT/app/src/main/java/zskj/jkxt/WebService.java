package zskj.jkxt;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import zskj.jkxt.util.ConnectionChangeReceiver;

/**
 * Created by WYY on 2017/2/24.
 */

public class WebService {
    static public String namespace = "http://tempuri.org/";
    static public String serviceUrl = "http://192.168.117.58:8085/H9000Service.asmx";
    public static final String ERRORMSG = "获取数据失败";
    static WebService service = null;
    static int timeoutmiles = 2 * 1000;//TODO 改成20*1000或者30*1000 默认20*1000

    private WebService() {
    }

    public static WebService getInstance() {
        if (service == null) {
            service = new WebService();
        }
        return service;
    }

    public String isUser(String userName, String password) {
        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            return ERRORMSG;
        }
        String methodName = "isUser";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("username", userName);
        request.addProperty("password", password);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, timeoutmiles);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                return object.getProperty(0).toString();
            } else
                return ERRORMSG;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String getUserInfo() {
        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            return ERRORMSG;
        }
        String methodName = "getUserInfo";
        SoapObject request = new SoapObject(namespace, methodName);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, timeoutmiles);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                return object.getProperty(0).toString();
            } else
                return ERRORMSG;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addUser(String userName, String password, String rights, String range, String level) {
        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            return ERRORMSG;
        }
        String methodName = "addUser";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("username", userName);
        request.addProperty("password", password);
        request.addProperty("rights", rights);
        request.addProperty("range", range);
        request.addProperty("level", level);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, timeoutmiles);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                return object.getProperty(0).toString();
            } else
                return ERRORMSG;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String updateUser(String userName, String password, String rights, String range, String level) {
        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            return ERRORMSG;
        }
        String methodName = "updateUser";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("username", userName);
        request.addProperty("password", password);
        request.addProperty("rights", rights);
        request.addProperty("range", range);
        request.addProperty("level", level);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, timeoutmiles);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                return object.getProperty(0).toString();
            } else
                return ERRORMSG;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String deleteUser(String userName) {
        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            return ERRORMSG;
        }
        String methodName = "deleteUser";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("username", userName);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, timeoutmiles);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                return object.getProperty(0).toString();
            } else
                return ERRORMSG;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String getStationInfo(String Str_StationCode) {
        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            return ERRORMSG;
        }
        String methodName = "GetStationInfo";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("Str_StationCode", Str_StationCode);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, timeoutmiles);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                return object.getProperty(0).toString();
            } else
                return ERRORMSG;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String getStationName(String ranges) {
        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            return ERRORMSG;
        }
        String methodName = "GetStationName";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("ranges", ranges);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, timeoutmiles);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                return object.getProperty(0).toString();
            } else
                return ERRORMSG;
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    public String getAlarmData(String sdate, String stime, String ranges, String level) {
        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            return ERRORMSG;
        }
        String methodName = "GetAlarmData";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("sdate", sdate);
        request.addProperty("stime", stime);
        request.addProperty("ranges", ranges);
        request.addProperty("level", level);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, timeoutmiles);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                return object.getProperty(0).toString();
            } else
                return ERRORMSG;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String getStationPower(String sdate, String time, String station_names) {

        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            return ERRORMSG;
        }
        String methodName = "GetStationPower";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("sdate", sdate);
        request.addProperty("time", time);
        request.addProperty("station_name", station_names);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, timeoutmiles);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                return object.getProperty(0).toString();
            } else
                return ERRORMSG;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
