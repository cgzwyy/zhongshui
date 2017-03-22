package zskj.jkxt.ui.widget;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import zskj.jkxt.R;

/**
 * Created by WYY on 2017/3/7.
 */
public class MyMarkerView extends MarkerView {

    private TextView tvContent;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
            int hour = (int)e.getX() / 60;
            int min = (int)e.getX() % 60;
            String result = null;
            if(hour < 10){
                result = "0" + hour + ":";
            }else{
                result = hour + ":";
            }

            if(min < 10){
                result += "0" + min ;
            }else{
                result += min ;
            }
            tvContent.setText( result  +" : " + Utils.formatNumber(e.getY(), 0, true));
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}

//public class MyMarkerView extends MarkerView {
//
//    private TextView tvContent;
//
//    public MyMarkerView(Context context, int layoutResource) {
//        super(context, layoutResource);
//
//        tvContent = (TextView) findViewById(R.id.tvContent);
//    }
//
//    @Override
//    public void refreshContent(Entry entry, Highlight highlight) {
//
//    }
//
//    @Override
//    public int getXOffset(float v) {
//        return 0;
//    }
//
//    @Override
//    public int getYOffset(float v) {
//        return 0;
//    }
//
//    // callbacks everytime the MarkerView is redrawn, can be used to update the
//    // content
////    @Override
//    public void refreshContent(Entry e, int dataSetIndex) {
//
//        if (e instanceof CandleEntry) {
//
//            CandleEntry ce = (CandleEntry) e;
//
//            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
//        } else {
//
////            tvContent.setText("" + Utils.formatNumber(e.getVal(), 0, true));
//            tvContent.setText("" +e.getVal());
//        }
//    }
//}
