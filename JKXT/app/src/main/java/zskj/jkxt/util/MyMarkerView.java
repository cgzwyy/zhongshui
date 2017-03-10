package zskj.jkxt.util;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
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

    @Override
    public void refreshContent(Entry entry, Highlight highlight) {

    }

    @Override
    public int getXOffset(float v) {
        return 0;
    }

    @Override
    public int getYOffset(float v) {
        return 0;
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content
//    @Override
    public void refreshContent(Entry e, int dataSetIndex) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {

//            tvContent.setText("" + Utils.formatNumber(e.getVal(), 0, true));
            tvContent.setText("" +e.getVal());
        }
    }
}
