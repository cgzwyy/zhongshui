package zskj.jkxt.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.DecimalFormat;
import java.util.ArrayList;

import zskj.jkxt.R;
import zskj.jkxt.api.RequestCallback;
import zskj.jkxt.api.WebService;

//test
public class TestActivity extends Activity {

    ProgressDialog dialog = null;   //进度对话框

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
//
//        LineChart chart = (LineChart) findViewById(R.id.chart1);
//
//
//        // 制作7个数据点（沿x坐标轴）
//        LineData mLineData = makeLineData(20);
//        setChartStyle(chart, mLineData, Color.WHITE);
//        XAxis xAxis = chart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的位置
//        xAxis.setLabelsToSkip(4);
//
//    }
//
//    // 设置chart显示的样式
//    private void setChartStyle(LineChart mLineChart, LineData lineData,
//                               int color) {
//        // 是否在折线图上添加边框
//        mLineChart.setDrawBorders(false);
//
//        mLineChart.setDescription("描述@ZhangPhil");// 数据描述
//
//        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
//        mLineChart
//                .setNoDataTextDescription("如果传给MPAndroidChart的数据为空，那么你将看到这段文字。@Zhang Phil");
//
//        // 是否绘制背景颜色。
//        // 如果mLineChart.setDrawGridBackground(false)，
//        // 那么mLineChart.setGridBackgroundColor(Color.CYAN)将失效;
//        mLineChart.setDrawGridBackground(false);
//        mLineChart.setGridBackgroundColor(Color.CYAN);
//
//        // 触摸
//        mLineChart.setTouchEnabled(true);
//
//        // 拖拽
//        mLineChart.setDragEnabled(true);
//
//        // 缩放
//        mLineChart.setScaleEnabled(true);
//
//        mLineChart.setPinchZoom(false);
//
//        // 设置背景
//        mLineChart.setBackgroundColor(color);
//
//        // 设置x,y轴的数据
//        mLineChart.setData(lineData);
//
//        // 设置比例图标示，就是那个一组y的value的
//        Legend mLegend = mLineChart.getLegend();
//
//        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
//        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
//        mLegend.setFormSize(15.0f);// 字体
//        mLegend.setTextColor(Color.BLUE);// 颜色
//
//        // 沿x轴动画，时间2000毫秒。
//        mLineChart.animateX(2000);
//    }
//
//    /**
//     * @param count
//     *            数据点的数量。
//     * @return
//     */
//    private LineData makeLineData(int count) {
//        ArrayList<String> x = new ArrayList<String>();
//        for (int i = 0; i < count; i++) {
//            // x轴显示的数据
//            x.add("x:" + i);
//        }
//
//        // y轴的数据
//        ArrayList<Entry> y = new ArrayList<Entry>();
//        for (int i = 0; i < count; i++) {
//            float val = (float) (Math.random() * 100);
//            Entry entry = new Entry(val, i);
//            y.add(entry);
//        }
//
//        // y轴数据集
//        LineDataSet mLineDataSet = new LineDataSet(y, "测试数据集。by ZhangPhil");
//
//        // 用y轴的集合来设置参数
//        // 线宽
//        mLineDataSet.setLineWidth(3.0f);
//
//        // 显示的圆形大小
//        mLineDataSet.setCircleSize(5.0f);
//
//        // 折线的颜色
//        mLineDataSet.setColor(Color.DKGRAY);
//
//        // 圆球的颜色
//        mLineDataSet.setCircleColor(Color.GREEN);
//
//        // 设置mLineDataSet.setDrawHighlightIndicators(false)后，
//        // Highlight的十字交叉的纵横线将不会显示，
//        // 同时，mLineDataSet.setHighLightColor(Color.CYAN)失效。
//        mLineDataSet.setDrawHighlightIndicators(true);
//
//        // 按击后，十字交叉线的颜色
//        mLineDataSet.setHighLightColor(Color.CYAN);
//
//        // 设置这项上显示的数据点的字体大小。
//        mLineDataSet.setValueTextSize(10.0f);
//
//        // mLineDataSet.setDrawCircleHole(true);
//
//        // 改变折线样式，用曲线。
//        // mLineDataSet.setDrawCubic(true);
//        // 默认是直线
//        // 曲线的平滑度，值越大越平滑。
//        // mLineDataSet.setCubicIntensity(0.2f);
//
//        // 填充曲线下方的区域，红色，半透明。
//        // mLineDataSet.setDrawFilled(true);
//        // mLineDataSet.setFillAlpha(128);
//        // mLineDataSet.setFillColor(Color.RED);
//
//        // 填充折线上数据点、圆球里面包裹的中心空白处的颜色。
//        mLineDataSet.setCircleColorHole(Color.YELLOW);
//
//        // 设置折线上显示数据的格式。如果不设置，将默认显示float数据格式。
//        mLineDataSet.setValueFormatter(new ValueFormatter() {
//
//            @Override
//            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
//                int n = (int) value;
//                String s = "y:" + n;
//                return s;
//            }
//        });
//
//        ArrayList<LineDataSet> mLineDataSets = new ArrayList<LineDataSet>();
//        mLineDataSets.add(mLineDataSet);
//
//        LineData mLineData = new LineData(x, mLineDataSets);
//
//        return mLineData;
//    }

    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_test);
        dialog = new ProgressDialog(this);
        dialog.setMessage("加载中...");     //设置提示信息
        dialog.setCanceledOnTouchOutside(false);   //设置在点击Dialog外是否取消Dialog进度条

        mChart = (LineChart) findViewById(R.id.chart1);

        // 设置在Y轴上是否是从0开始显示
//        mChart.setStartAtZero(true);
//        //是否在Y轴显示数据，就是曲线上的数据
//        mChart.setDrawYValues(true);
//        //设置网格
//        mChart.setDrawBorder(true);
//        mChart.setBorderPositions(new BarLineChartBase.BorderPosition[]{
//                BarLineChartBase.BorderPosition.BOTTOM});
        //在chart上的右下角加描述
//        mChart.setDescription("曲线图");
        //设置Y轴上的单位
//        mChart.setUnit("￥");
        //设置透明度
        mChart.setAlpha(0.8f);
        //设置网格底下的那条线的颜色
        mChart.setBorderColor(Color.rgb(213, 216, 214));
        //设置Y轴前后倒置
//        mChart.setInvertYAxisEnabled(false);
//        //设置高亮显示
//        mChart.setHighlightEnabled(true);
        //设置是否可以触摸，如为false，则不能拖动，缩放等
        mChart.setTouchEnabled(true);
        //设置是否可以拖拽，缩放
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        //设置是否能扩大扩小
        mChart.setPinchZoom(true);
        // 设置背景颜色
        // mChart.setBackgroundColor(Color.GRAY);
        //设置点击chart图对应的数据弹出标注
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
//        // define an offset to change the original position of the marker
//        // (optional)
//        mv.setOffsets(-mv.getMeasuredWidth() / 2, -mv.getMeasuredHeight());
//        // set the marker to the chart
//        mChart.setMarkerView(mv);
//        // enable/disable highlight indicators (the lines that indicate the
//        // highlighted Entry)
//        mChart.setHighlightIndicatorEnabled(false);
//        //设置字体格式，如正楷
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "OpenSans-Regular.ttf");
//        mChart.setValueTypeface(tf);
//
//        XLabels xl = mChart.getXLabels();
////		xl.setAvoidFirstLastClipping(true);
////		xl.setAdjustXLabels(true);
//        xl.setPosition(XLabels.XLabelPosition.BOTTOM); // 设置X轴的数据在底部显示
//        xl.setTypeface(tf); // 设置字体
//        xl.setTextSize(10f); // 设置字体大小
//        xl.setSpaceBetweenLabels(10); // 设置数据之间的间距
//
//
//
//        YLabels yl = mChart.getYLabels();
//        // yl.setPosition(YLabelPosition.LEFT_INSIDE); // set the position
//        yl.setTypeface(tf); // 设置字体
//        yl.setTextSize(10f); // s设置字体大小
//        yl.setLabelCount(5); // 设置Y轴最多显示的数据个数


        XAxis xAxis = mChart.getXAxis();
//        xAxis.setSpaceBetweenLabels(4); //设置标签字符间的空隙
//        xAxis.setLabelsToSkip(4);  //设置在”绘制下一个标签”时，要忽略的标签数
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.RED);
        xAxis.setTypeface(tf);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setStartAtZero(true); //则无论图表显示的是哪种类型的数据，该轴最小值总是0
//        setAxisMaxValue(float max) : 设置该轴的最大值。 如果设置了，这个值将不会是根据提供的数据计算出来的。
//        resetAxisMaxValue() : 调用此方法撤销先前设置的最大值。 通过这样做，你将再次允许轴自动计算出它的最大值。
//        setAxisMinValue(float min) : 设置该轴的自定义最小值。 如果设置了，这个值将不会是根据提供的数据计算出来的。
//        resetAxisMinValue() : 调用此撤销先前设置的最小值。 通过这样做，你将再次允许轴自动计算它的最小值。
        yAxis.setTypeface(tf); // set a different font
        yAxis.setTextSize(12f); // set the textsize
        yAxis.setAxisMaxValue(100f); // the axis maximum is 100
        yAxis.setTextColor(Color.BLACK);
        yAxis.setLabelCount(6, false);

        // 加载数据
        tmpSetData();
        //从X轴进入的动画
        mChart.animateX(4000);
        mChart.animateY(3000);   //从Y轴进入的动画
        mChart.animateXY(3000, 3000);    //从XY轴一起进入的动画

        //设置最小的缩放
        mChart.setScaleMinima(0.5f, 1f);
        //设置视口
        // mChart.centerViewPort(10, 50);

        // get the legend (only possible after setting data)
//        Legend l = mChart.getLegend();
//        l.setForm(Legend.LegendForm.LINE);  //设置图最下面显示的类型
//        l.setTypeface(tf);
//        l.setTextSize(15);
//        l.setTextColor(Color.rgb(104, 241, 175));//图注字体颜色
//        l.setFormSize(30f); // set the size of the legend forms/shapes 设置图注线的长度
//        l.setFormToTextSpace(20f); //设置图注和标题的宽度
        // 刷新图表
        mChart.invalidate();
    }

    private void setData() {
        String[] aa = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};
        String[] bb = {"122.00", "234.34", "85.67", "117.90", "332.33", "113.33", "120.78", "122.00", "234.34", "85.67", "117.90", "332.33", "113.33", "120.78", "122.00", "234.34", "85.67", "117.90", "332.33", "113.33", "120.78", "332.33", "113.33", "120.78"};
        String[] cc = {"22.00", "34.34", "85.67", "17.90", "32.33", "13.33", "20.78", "22.00", "34.34", "85.67", "17.90", "32.33", "13.33", "20.78", "22.00", "34.34", "85.67", "17.90", "32.33", "13.33", "20.78", "32.33", "13.33", "20.78", "22.00", "34.34", "85.67", "17.90", "32.33", "13.33", "20.78", "22.00", "34.34", "85.67", "17.90", "32.33", "13.33", "20.78", "22.00", "34.34", "85.67", "17.90", "32.33", "13.33", "20.78", "32.33", "13.33", "20.78"};

//        int max = bb.length>cc.length?bb.length:cc.length;
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < aa.length; i++) {
            xVals.add(aa[i]);
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < bb.length; i++) {
            yVals.add(new Entry(Float.parseFloat(bb[i]), i));
        }
        ArrayList<Entry> zVals = new ArrayList<Entry>();
        zVals.add(new Entry(Float.parseFloat(cc[1]), 1));
        zVals.add(new Entry(Float.parseFloat(cc[2]), 3));
        zVals.add(new Entry(Float.parseFloat(cc[3]), 4));
        zVals.add(new Entry(Float.parseFloat(cc[4]), 8));
        zVals.add(new Entry(Float.parseFloat(cc[5]), 10));

//        for (int i = 0; i < cc.length; i++) {
//            zVals.add(new Entry(Float.parseFloat(cc[i]), i));
//        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "DataSet Line");
        LineDataSet set2 = new LineDataSet(zVals, "DataSet Line2");

//        set1.setDrawCubic(true);  //设置曲线为圆滑的线
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(false);  //设置包括的范围区域填充颜色
        set1.setDrawCircles(true);  //设置有圆点
        set1.setLineWidth(2f);    //设置线的宽度
        set1.setCircleSize(5f);   //设置小圆的大小
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setColor(Color.rgb(104, 241, 175));    //设置曲线的颜色

//        set2.setDrawCubic(true);  //设置曲线为圆滑的线
        set2.setCubicIntensity(0.2f);
        set2.setDrawFilled(false);  //设置包括的范围区域填充颜色
        set2.setDrawCircles(false);  //设置有圆点
        set2.setLineWidth(2f);    //设置线的宽度
        set2.setCircleSize(5f);   //设置小圆的大小
        set2.setHighLightColor(Color.rgb(243, 113, 113));
        set2.setColor(Color.rgb(10, 24, 17));    //设置曲线的颜色

        // create a data object with the datasets
//        LineData data = new LineData(xVals, set1);
        LineData data = new LineData(set1);
        data.addDataSet(set2);
        // set data
//        mChart.setData(data);

    }

    private void tmpSetData() {
        dialog.show();   //显示进度条对话框
        Log.e("tmpsetdata---->", "start");
        new Thread() {
            public void run(){
                WebService.getInstance().GetStationP("17221","Gbe_F12All_Wh","Gbe_F13All_Wh", new RequestCallback() {

                    @Override
                    public void onSuccess(final String result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();  //删除该进度条
                                setDataDetail(result);
//                                setData();
                            }
                        });
                    }

                    @Override
                    public void onFail(final String errorMsg) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();  //删除该进度条
                                // Toast：简易的消息提示框，自动消失
                                // 第一个参数：当前的上下文环境。可用getApplicationContext()或this
                                // 第二个参数：要显示的字符串。也可是R.string中字符串ID
                                // 第三个参数：显示的时间长短。Toast默认的有两个LENGTH_LONG(长)和LENGTH_SHORT(短)，也可以使用毫秒，如2000ms
                                Toast.makeText(getApplicationContext(), "获取数据失败！"+errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        }.start();
        Log.e("tmpsetdata---->", "end");
    }

    public void setDataDetail(String result) {

        Log.e("setDataDetail---->", "start");
        //设置x轴数据
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i <= 1440; i++) {
            xVals.add(String.valueOf(i));
        }

        if (!result.contains("{")) {
            Toast.makeText(getApplicationContext(), "获取数据失败！", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonParser parser = new JsonParser();//创建JSON解析器
        JsonObject object = (JsonObject) parser.parse(result); //创建JsonObject对象
        JsonArray parray = object.get("pcode").getAsJsonArray(); //得到为json的数组
        JsonArray forecast_parray = object.get("forecast_pcode").getAsJsonArray(); //得到为json的数组

        DecimalFormat df = new DecimalFormat("0.00");  //数据格式转换，四舍五入，保留两位小数

        ArrayList<Entry> yVals = new ArrayList<Entry>();

//        for (int i = 0; i < bb.length; i++) {
//            yVals.add(new Entry(Float.parseFloat(bb[i]), i));
//        }

        for (int i = 0; i < parray.size(); i++) {
            JsonObject subObject = parray.get(i).getAsJsonObject();
            Log.e("setdata------>",subObject.get("time").getAsString() + "---------------" + subObject.get("data").getAsString());
            yVals.add(new Entry(Float.parseFloat(df.format(Double.valueOf(subObject.get("data").getAsString()))), subObject.get("time").getAsInt()));
        }

        ArrayList<Entry> zVals = new ArrayList<Entry>();
        for(int i=0;i<forecast_parray.size();i++){
            JsonObject subObject=forecast_parray.get(i).getAsJsonObject();
            zVals.add(new Entry(Float.parseFloat(df.format(Double.valueOf(subObject.get("data").getAsString()))), subObject.get("time").getAsInt()));
        }

        LineDataSet set1 = new LineDataSet(yVals, "DataSet Line");
        LineDataSet set2 = new LineDataSet(zVals, "DataSet Line2");

//        set1.setDrawCubic(true);  //设置曲线为圆滑的线
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(false);  //设置包括的范围区域填充颜色
        set1.setDrawCircles(false);  //设置有圆点
        set1.setLineWidth(1f);    //设置线的宽度
        set1.setCircleSize(5f);   //设置小圆的大小
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setColor(Color.rgb(104, 241, 175));    //设置曲线的颜色

//        set2.setDrawCubic(true);  //设置曲线为圆滑的线
        set2.setCubicIntensity(0.2f);
        set2.setDrawFilled(false);  //设置包括的范围区域填充颜色
        set2.setDrawCircles(false);  //设置有圆点
        set2.setLineWidth(2f);    //设置线的宽度
        set2.setCircleSize(5f);   //设置小圆的大小
        set2.setHighLightColor(Color.rgb(243, 113, 113));
        set2.setColor(Color.rgb(10, 24, 17));    //设置曲线的颜色

        // create a data object with the datasets
//        LineData data = new LineData(xVals, set1);
        LineData data = new LineData(set1);
        data.addDataSet(set2);
        // set data
        //mChart.setData(data);
    }
}
