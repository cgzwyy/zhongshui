package zskj.jkxt;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;

import zskj.jkxt.util.ConnectionChangeReceiver;

/**
 * Created by WYY on 2017/2/24.
 */

public class WebService {
    static public String namespace = "http://tempuri.org/ns.xsd";
    static public String serviceUrl = "http://192.168.1.12:8081";
    public static final String ERRORMSG = "获取数据失败";
    static WebService service = null;
    static int timeoutmiles = 2 * 1000;//TODO 改成20*1000或者30*1000 默认20*1000

    private WebService() {
        serviceUrl = JKXTApplication.webServiceUrl;
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
//        request.addProperty("username", userName);
//        request.addProperty("password", password);
        try {
            request.addProperty("username", new String(userName.getBytes("gbk"),"ISO-8859-1"));
            request.addProperty("password", new String(password.getBytes("gbk"),"ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, timeoutmiles);
        try {
            ht.call(namespace + methodName, envelope);
            //SoapObject object = (SoapObject) envelope.bodyIn;
//            SoapObject object = (SoapObject) envelope.getResponse();
//            if (object != null && object.getProperty(0) != null) {
//                Log.e("resutl----->",object.getProperty(0).toString());
//                return object.getProperty(0).toString();
//            } else
//                return ERRORMSG;
            Object object = envelope.getResponse();
            if (object != null && object.toString() != null) {
//                Log.e("resutl----->",new String(object.toString().getBytes("ISO-8859-1"),"gbk"));
                return new String(object.toString().getBytes("ISO-8859-1"),"gbk");
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
//            SoapObject object = (SoapObject) envelope.bodyIn;
//            if (object != null && object.getProperty(0) != null) {
//                return object.getProperty(0).toString();
//            } else
//                return ERRORMSG;
            Object object = envelope.getResponse();
            if (object != null && object.toString() != null) {
//                Log.e("resutl----->",new String(object.toString().getBytes("ISO-8859-1"),"gbk"));
                return new String(object.toString().getBytes("ISO-8859-1"),"gbk");
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
//        request.addProperty("username", userName);
//        request.addProperty("password", password);
//        request.addProperty("rights", rights);
//        request.addProperty("range", range);
//        request.addProperty("level", level);
        try {
            request.addProperty("username", new String(userName.getBytes("gbk"),"ISO-8859-1"));
            request.addProperty("password", new String(password.getBytes("gbk"),"ISO-8859-1"));
            request.addProperty("rights", new String(rights.getBytes("gbk"),"ISO-8859-1"));
            request.addProperty("ranges", new String(range.getBytes("gbk"),"ISO-8859-1"));
            request.addProperty("level", new String(level.getBytes("gbk"),"ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, timeoutmiles);
        try {
            ht.call(namespace + methodName, envelope);
//            SoapObject object = (SoapObject) envelope.bodyIn;
//            if (object != null && object.getProperty(0) != null) {
//                return object.getProperty(0).toString();
//            } else
//                return ERRORMSG;
            Object object = envelope.getResponse();
            if (object != null && object.toString() != null) {
//                Log.e("resutl----->",new String(object.toString().getBytes("ISO-8859-1"),"gbk"));
                return new String(object.toString().getBytes("ISO-8859-1"),"gbk");
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
//        request.addProperty("username", userName);
//        request.addProperty("password", password);
//        request.addProperty("rights", rights);
//        request.addProperty("range", range);
//        request.addProperty("level", level);
        try {
            request.addProperty("username", new String(userName.getBytes("gbk"),"ISO-8859-1"));
            request.addProperty("password", new String(password.getBytes("gbk"),"ISO-8859-1"));
            request.addProperty("rights", new String(rights.getBytes("gbk"),"ISO-8859-1"));
            request.addProperty("ranges", new String(range.getBytes("gbk"),"ISO-8859-1"));
            request.addProperty("level", new String(level.getBytes("gbk"),"ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, timeoutmiles);
        try {
            ht.call(namespace + methodName, envelope);
//            SoapObject object = (SoapObject) envelope.bodyIn;
//            if (object != null && object.getProperty(0) != null) {
//                return object.getProperty(0).toString();
//            } else
//                return ERRORMSG;
            Object object = envelope.getResponse();
            if (object != null && object.toString() != null) {
//                Log.e("resutl----->",new String(object.toString().getBytes("ISO-8859-1"),"gbk"));
                return new String(object.toString().getBytes("ISO-8859-1"),"gbk");
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
//        request.addProperty("username", userName);
        try {
            request.addProperty("username", new String(userName.getBytes("gbk"),"ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, timeoutmiles);
        try {
            ht.call(namespace + methodName, envelope);
//            SoapObject object = (SoapObject) envelope.bodyIn;
//            if (object != null && object.getProperty(0) != null) {
//                return object.getProperty(0).toString();
//            } else
//                return ERRORMSG;
            Object object = envelope.getResponse();
            if (object != null && object.toString() != null) {
//                Log.e("resutl----->",new String(object.toString().getBytes("ISO-8859-1"),"gbk"));
                return new String(object.toString().getBytes("ISO-8859-1"),"gbk");
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
//        request.addProperty("stationCode", Str_StationCode);
        try {
            request.addProperty("stationCode", new String(Str_StationCode.getBytes("gbk"),"ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, timeoutmiles);
        try {
            ht.call(namespace + methodName, envelope);
//            SoapObject object = (SoapObject) envelope.bodyIn;
//            if (object != null && object.getProperty(0) != null) {
//                return object.getProperty(0).toString();
//            } else
//                return ERRORMSG;
            Object object = envelope.getResponse();
            if (object != null && object.toString() != null) {
//                Log.e("resutl----->",new String(object.toString().getBytes("ISO-8859-1"),"gbk"));
                return new String(object.toString().getBytes("ISO-8859-1"),"gbk");
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
        try {
            request.addProperty("ranges", new String(ranges.getBytes("gbk"),"ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, timeoutmiles);
        try {
            ht.call(namespace + methodName, envelope);
//            SoapObject object = (SoapObject) envelope.bodyIn;
//            if (object != null && object.getProperty(0) != null) {
//                return object.getProperty(0).toString();
//            } else
//                return ERRORMSG;
            Object object = envelope.getResponse();
            if (object != null && object.toString() != null) {
//                Log.e("resutl----->",new String(object.toString().getBytes("ISO-8859-1"),"gbk"));
                return new String(object.toString().getBytes("ISO-8859-1"),"gbk");
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
//        request.addProperty("sdate", sdate);
//        request.addProperty("stime", stime);
//        request.addProperty("ranges", ranges);
//        request.addProperty("level", level);
        try {
            request.addProperty("sdate", new String(sdate.getBytes("gbk"),"ISO-8859-1"));
            request.addProperty("stime", new String(stime.getBytes("gbk"),"ISO-8859-1"));
            request.addProperty("ranges", new String(ranges.getBytes("gbk"),"ISO-8859-1"));
            request.addProperty("level", new String(level.getBytes("gbk"),"ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, timeoutmiles);
        try {
            ht.call(namespace + methodName, envelope);
//            SoapObject object = (SoapObject) envelope.bodyIn;
//            if (object != null && object.getProperty(0) != null) {
//                return object.getProperty(0).toString();
//            } else
//                return ERRORMSG;
            Object object = envelope.getResponse();
            if (object != null && object.toString() != null) {
//                Log.e("resutl----->",new String(object.toString().getBytes("ISO-8859-1"),"gbk"));
                return new String(object.toString().getBytes("ISO-8859-1"),"gbk");
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
//        request.addProperty("sdate", sdate);
//        request.addProperty("stime", time);
//        request.addProperty("stationName", station_names);
        try {
            request.addProperty("sdate", new String(sdate.getBytes("gbk"),"ISO-8859-1"));
            request.addProperty("stime", new String(time.getBytes("gbk"),"ISO-8859-1"));
            request.addProperty("stationName", new String(station_names.getBytes("gbk"),"ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, timeoutmiles);
        try {
            ht.call(namespace + methodName, envelope);
//            SoapObject object = (SoapObject) envelope.bodyIn;
//            if (object != null && object.getProperty(0) != null) {
//                return object.getProperty(0).toString();
//            } else
//                return ERRORMSG;
            Object object = envelope.getResponse();
            if (object != null && object.toString() != null) {
//                Log.e("resutl----->",new String(object.toString().getBytes("ISO-8859-1"),"gbk"));
                return new String(object.toString().getBytes("ISO-8859-1"),"gbk");
            } else
                return ERRORMSG;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
