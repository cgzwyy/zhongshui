package zskj.jkxt;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import java.io.InputStream;

import zskj.jkxt.util.ConnectionChangeReceiver;

/**
 * Created by WYY on 2017/2/24.
 */

public class JKXTApplication extends Application {

    private static final String sp_app = "config";
    public static String webServiceUrl = "";
    public static String[] stations;
    public static int NETWORK_FLAG = ConnectionChangeReceiver.NET_NONE;


    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
                .detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(
                new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
        NETWORK_FLAG = ConnectionChangeReceiver.ConnectionDetect(this);
        Log.e("NETWO0RK_FLAG", "------------------".concat(String.valueOf(NETWORK_FLAG)));

        getUrl();
        getStations();

    }

    private void getUrl() {
        SharedPreferences sp = this.getSharedPreferences(sp_app, this.MODE_PRIVATE);
        webServiceUrl = sp.getString("url", "");

        if (TextUtils.isEmpty(webServiceUrl)) {
            webServiceUrl = readUrlFile();
            if (!TextUtils.isEmpty(webServiceUrl)) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("url", webServiceUrl);
                editor.commit();
            }
        }
    }

    private void getStations() {
        //取SP内容
        SharedPreferences sp = this.getSharedPreferences(sp_app, this.MODE_PRIVATE);
        String station = sp.getString("station", "");
        //SP为空，读文件,寫進SP
        if(TextUtils.isEmpty(station)){
            station = readStationFile();
            if (!TextUtils.isEmpty(station)) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("station", station);
                editor.commit();
            }
        }
        //不为空，赋值stations[]
        if(!TextUtils.isEmpty(station)){
            String[] dataSet = station.split(";");
            if (dataSet != null && dataSet.length > 0) {
                stations = new String[dataSet.length];
                for (int i = 0; i < dataSet.length; i++) {
                    String[] data = dataSet[i].split(",");
                    stations[i] = data[0];
                }
            }
        }//再为空说明文件读取失败，Stations[]为null
    }

    private String readUrlFile() {
        String url = "";
        try {
            InputStream in = getResources().openRawResource(R.raw.url);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            //res = EncodingUtils.getString(buffer, "BIG5");
            url = new String(buffer, "BIG5");
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    private String readStationFile() {
        String station = "";
        try {
            InputStream in = getResources().openRawResource(R.raw.stations);//获取
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            station = new String(buffer, "UTF-8");
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return station;
    }
}
