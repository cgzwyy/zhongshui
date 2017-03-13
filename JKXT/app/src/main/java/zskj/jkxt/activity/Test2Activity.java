package zskj.jkxt.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import zskj.jkxt.R;
import zskj.jkxt.util.MyMarkerView;

public class Test2Activity extends Activity {

    private LineChart mChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mChart = (LineChart) findViewById(R.id.chart1);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f); //持续滚动时的速度快慢，[0,1) 0代表立即停止。

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);  //设置图表背景 参数是个Color对象
//        mChart.setViewPortOffsets(0f, 0f, 0f, 0f);//设置图表显示的偏移量，不建议使用，因为会影响图表的自动计算


        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        // add data
        setData(100, 30);
        mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setEnabled(false);

        Typeface mTfLight = Typeface.createFromAsset(getAssets(),
                "OpenSans-Regular.ttf");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(10f);
//        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(true); //是否绘制坐标轴的线，即含有坐标的那条线，默认是true
        xAxis.setDrawGridLines(true);//是否显示X坐标轴上的刻度竖线，默认是true
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f); // one hour  设置x轴值之间的最小间隔。这可以用于在放大到为轴设置的小数位数不再允许在两个轴值之间进行区分的点时避免值重复。
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {  //将自定义格式化程序设置为轴。

            private SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Log.e("获取到的时间sssssssssss",value+"");
                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(170f);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.rgb(255, 192, 56));
        leftAxis.setDrawLabels(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void setData(int count, float range) {

        // now in hours
        long now = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());
        Log.e("获取到的时间-----",now+"");

        ArrayList<Entry> values = new ArrayList<Entry>();
        ArrayList<Entry> values2 = new ArrayList<Entry>();

        float from = now;

        // count = hours
        float to = now + count;

        // increment by 1 hour
        for (float x = from; x < to; x++) {

            float y = getRandom(range, 50);
            float z = getRandom(range, 20);
            values.add(new Entry(x, y)); // add one entry per hour
            values2.add(new Entry(x, z)); // add one entry per hour
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setValueTextColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(1.5f);//设置线的宽度
        set1.setDrawCircles(false);  //设置有圆点
        set1.setDrawValues(false);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);
        set1.setColor(Color.RED);

        LineDataSet set2 = new LineDataSet(values2, "DataSet 2");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set2.setColor(ColorTemplate.getHoloBlue());
        set2.setValueTextColor(ColorTemplate.getHoloBlue());
        set2.setLineWidth(1.5f);
        set2.setDrawCircles(false);
        set2.setDrawValues(false);
        set2.setFillAlpha(65);
        set2.setFillColor(ColorTemplate.getHoloBlue());
        set2.setHighLightColor(Color.rgb(244, 117, 117));
        set2.setDrawCircleHole(false);
        set2.setColor(Color.BLUE);

        // create a data object with the datasets
        LineData data = new LineData(set1);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);
        data.addDataSet(set2);

        // set data
        mChart.setData(data);
    }

    private float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }
}
