package zskj.jkxt.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by WYY on 2017/2/24.
 */

public class ConnectionChangeReceiver extends BroadcastReceiver {
    public static final int NET_NONE = 0;
    public static final int NET_DATA = 2;
    public static final int NET_WIFI = 3;

    public static int ConnectionDetect(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();
        if (activeInfo == null || !activeInfo.isAvailable()) {
            Log.e("there one","------------------");
            return NET_NONE;
        } else {
            if (mobileInfo == null) {
                Log.e("there two","------------------");
                Toast.makeText(context, "没有网络", Toast.LENGTH_SHORT).show();
                return NET_NONE;
            }
            if (mobileInfo.isConnected()) {
                Log.e("there 3","------------------");
                Toast.makeText(context, "数据流量", Toast.LENGTH_SHORT).show();
                return NET_DATA;
            } else if (wifiInfo.isConnected()) {
                Log.e("there 4","------------------");
                Toast.makeText(context, "Wifi", Toast.LENGTH_SHORT).show();
                return NET_WIFI;
            } else {
                Log.e("there 5","------------------");
                Toast.makeText(context, "未知网络", Toast.LENGTH_SHORT).show();
                return NET_NONE;
            }
        }
    }

    public static void ConnectionAutoDetect(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();
        if (activeInfo == null || !activeInfo.isAvailable()) {
            Toast.makeText(context, "没有网络", Toast.LENGTH_SHORT).show();
        } else {
            if (mobileInfo != null) {
                if (mobileInfo.isConnected()) {
                    Toast.makeText(context, "2G/3G", Toast.LENGTH_SHORT).show();
                } else if (wifiInfo.isConnected()) {
                    Toast.makeText(context, "Wifi", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "没有网络", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectionAutoDetect(context);
    }
}
