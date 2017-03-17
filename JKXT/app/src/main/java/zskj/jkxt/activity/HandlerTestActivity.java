package zskj.jkxt.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import zskj.jkxt.R;

public class HandlerTestActivity extends Activity {

    private TextView msg;
//    private Handler handler = new Handler();
//    private static int x = 0;
//    int mHour, mMin;
//    private Runnable runnable = new Runnable() {
//        public void run() {
//            this.update();
//            handler.postDelayed(this, 1000 * 10);// 间隔10秒
//        }
//        void update() {
//            msg.setText("你好~~" + (x++));
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_test);

        msg = (TextView) findViewById(R.id.txtMsg);

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
        String dateInString = "91438230";

        try {

            Date date = formatter.parse(dateInString);
            Log.e("--1---",formatter.format(date));
            msg.setText(formatter.format(date) +"--");

        } catch (ParseException e) {
            e.printStackTrace();
        }

//        handler.postDelayed(runnable, 1000 * 10);
//
//        final Calendar ca = Calendar.getInstance();
//        mHour = ca.get(Calendar.HOUR);
//        mMin = ca.get(Calendar.MINUTE);


    }
//    @Override
//    protected void onDestroy() {
//        handler.removeCallbacks(runnable); //停止刷新
//        super.onDestroy();
//    }
}
