package zskj.jkxt.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import zskj.jkxt.R;

/**
 * Created by huanan on 2017/3/10.
 */
public class ChartActivity extends Activity {

    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_test);
        mChart = (LineChart) findViewById(R.id.chart1);
        // 设置在Y轴上是否是从0开始显示
//        mChart.setStartAtZero(true);
//        //是否在Y轴显示数据，就是曲线上的数据
//        mChart.setDrawYValues(true);
//        //设置网格
//        mChart.setDrawBorder(true);
//        mChart.setBorderPositions(new BarLineChartBase.BorderPosition[]{
//                BarLineChartBase.BorderPosition.BOTTOM});
//        //设置Y轴上的单位
////        mChart.setUnit("￥");
//        //设置透明度
//        mChart.setAlpha(0.8f);
//        //设置网格底下的那条线的颜色
//        mChart.setBorderColor(Color.rgb(213, 216, 214));
//
//        mChart.setDragEnabled(true);
//        mChart.setScaleEnabled(true);
//
//        //设置是否能扩大扩小
//        mChart.setPinchZoom(false);
//        //设置点击chart图对应的数据弹出标注
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
//        // define an offset to change the original position of the marker
//        // (optional)
//        mv.setOffsets(-mv.getMeasuredWidth() / 2, -mv.getMeasuredHeight());
//        // set the marker to the chart
//        mChart.setMarkerView(mv);
//        // enable/disable highlight indicators (the lines that indicate the
//        // highlighted Entry)
//        mChart.setHighlightIndicatorEnabled(false);
//
//        XLabels xl = mChart.getXLabels();
////		xl.setAvoidFirstLastClipping(true);
////		xl.setAdjustXLabels(true);
//        xl.setPosition(XLabels.XLabelPosition.BOTTOM); // 设置X轴的数据在底部显示
//        xl.setTextSize(10f); // 设置字体大小
//        xl.setSpaceBetweenLabels(2); // 设置数据之间的间距
//
//        YLabels yl = mChart.getYLabels();
//        // yl.setPosition(YLabelPosition.LEFT_INSIDE); // set the position
//        yl.setTextSize(10f); // s设置字体大小
//        yl.setLabelCount(5); // 设置Y轴最多显示的数据个数
//        // 加载数据
//        setData();
//        //从X轴进入的动画
////        mChart.animateX(4000);
////        mChart.animateY(3000);   //从Y轴进入的动画
//        mChart.animateXY(3000, 3000);    //从XY轴一起进入的动画
//
//        //设置最小的缩放
//        mChart.setScaleMinima(0.5f, 1f);
//        //设置视口
//        // mChart.centerViewPort(10, 50);
//
//        // get the legend (only possible after setting data)
//        Legend l = mChart.getLegend();
//        l.setForm(Legend.LegendForm.LINE);  //设置图最下面显示的类型
//        l.setTextSize(15);
//        l.setTextColor(Color.rgb(104, 241, 175));//图注字体颜色
//        l.setFormSize(30f); // set the size of the legend forms/shapes 设置图注线的长度
////        l.setFormToTextSpace(20f); //设置图注和标题的宽度
//        // 刷新图表
//        mChart.invalidate();
    }

    static Speed speed = null;

    private void setData() {
        ArrayList<Speed> dataset = getData();
        if (dataset == null || dataset.size() <= 0)
            return;

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();


        for (int i = 0; i < dataset.size(); i++) {
            speed = dataset.get(i);
            xVals.add(String.valueOf(speed.time));
            yVals.add(new Entry(speed.data, i));
        }
        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "Speed one");

//        set1.setDrawCubic(true);  //设置曲线为圆滑的线
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(false);  //设置包括的范围区域填充颜色
        set1.setDrawCircles(false);  //设置有圆点
        set1.setLineWidth(2f);    //设置线的宽度
        set1.setCircleSize(5f);   //设置小圆的大小
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setColor(Color.rgb(104, 241, 175));    //设置曲线的颜色

//        set2.setDrawCubic(true);  //设置曲线为圆滑的线
//        set2.setCubicIntensity(0.2f);
//        set2.setDrawFilled(false);  //设置包括的范围区域填充颜色
//        set2.setDrawCircles(false);  //设置有圆点
//        set2.setLineWidth(2f);    //设置线的宽度
//        set2.setCircleSize(5f);   //设置小圆的大小
//        set2.setHighLightColor(Color.rgb(243, 113, 113));
//        set2.setColor(Color.rgb(10, 24, 17));    //设置曲线的颜色

        // create a data object with the datasets
//        LineData data = new LineData(xVals, set1);
//        data.addDataSet(set2);
        // set data
//        mChart.setData(data);
    }


    private ArrayList<Speed> getData() {
        ArrayList<Speed> dataset = new ArrayList<Speed>();
        try {
            InputStream is = getAssets().open("tmpdata.txt");
            String data = getStringFromInputStream(is);
            JSONObject object = new JSONObject(data);
            JSONArray pcode = object.optJSONArray("pcode");
            if (pcode != null && pcode.length() > 0) {
                for (int i = 0; i < pcode.length(); i++) {
                    JSONObject obj = pcode.optJSONObject(i);
                    Speed speed = new Speed();
                    speed.time = obj.optInt("time");
                    speed.data = obj.optInt("data");
                    dataset.add(speed);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataset;
    }

    public static class Speed {
        int time;
        int data;
    }

    private String getStringFromInputStream(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }
        return buffer.toString();
    }

}
