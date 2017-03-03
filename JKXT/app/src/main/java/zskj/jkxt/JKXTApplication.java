package zskj.jkxt;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.InputStream;

import zskj.jkxt.api.ConnectionChangeReceiver;

/**
 * Created by WYY on 2017/2/24.
 */

public class JKXTApplication extends Application {
    public static String webServiceUrl = "";
    public static String stations = "";
    public static int NETWORK_FLAG = ConnectionChangeReceiver.NET_NONE;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        getUrl();
        NETWORK_FLAG = ConnectionChangeReceiver.ConnectionDetect(this);
    }

    private void getUrl() {
        SharedPreferences sp = this.getSharedPreferences("url", this.MODE_PRIVATE);
        webServiceUrl = sp.getString("url", "");

        if (TextUtils.isEmpty(webServiceUrl)) {
            webServiceUrl = readUrlFile();
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("url", webServiceUrl);
            editor.commit();
        }
    }

    private String readUrlFile() {
        String res = "";
        try {
            InputStream in = getResources().openRawResource(R.raw.url);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            //res = EncodingUtils.getString(buffer, "BIG5");
            res = new String(buffer,"BIG5");
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
