package zskj.jkxt.api;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import zskj.jkxt.R;
import zskj.jkxt.util.DateTimePickDialogUtil;
import zskj.jkxt.util.MyMarkerView;

/**
 * Created by WYY on 2017/3/14.
 */

public class FrdFragment extends Fragment {

    ProgressDialog dialog = null;   //进度对话框
    private LineChart mChart;
    private Button select_time,select_station;
    private TextView selected_time;
//    private TextView selected_station;
    private Spinner spinner_station;
    int mYear, mMonth, mDay;
    private int station_choice;
    private ArrayAdapter<String> adapter;
    private Button sure;
    private String date; //chart 日期
    private String station_name;
    private int last_time = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_test, container, false);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("加载中...");     //设置提示信息
        dialog.setCanceledOnTouchOutside(false);   //设置在点击Dialog外是否取消Dialog进度条

        mChart = (LineChart) view.findViewById(R.id.chart1);

        select_time = (Button) view.findViewById(R.id.select_time);
        selected_time = (TextView) view.findViewById(R.id.selected_time);

        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

        selected_time.setText(sdf.format(ca.getTime()));
        date = selected_time.getText().toString();

        selected_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        getActivity(), selected_time.getText().toString());
                selected_time.setText("");
                dateTimePicKDialog.dateTimePicKDialog(selected_time);
                date = selected_time.getText().toString();
            }
        });
//        select_time.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//
//                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
//                        getActivity(), mYear + "年" + (mMonth+1) + "月" + mDay + "日");
//                dateTimePicKDialog.dateTimePicKDialog(selected_time);
//
//            }
//        });

        select_station = (Button) view.findViewById(R.id.select_station);
//        selected_station = (TextView) view.findViewById(R.id.selected_station);
        spinner_station = (Spinner) view.findViewById(R.id.spinner_station);
//        station_name = spinner_station.getSelectedItem().toString();
        GetStationName();
//        select_station.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                GetStationName();
//            }
//        });

        sure = (Button) view.findViewById(R.id.sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData(date,station_name);
                handler.postDelayed(runnable, 1000 * 10);
            }
        });

        return view;
    }
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            while(last_time < 1440){
                this.update();
                Log.e("last_time----1-------->",last_time+"");
                handler.postDelayed(this, 1000 * 60);// 间隔10秒
            }

        }
        void update() {
            GetNewData(date, last_time+"", station_name);
            last_time += 100;
        }
    };

    private void GetData(final String sdate, final String station_names) {
        dialog.show();   //显示进度条对话框
//        Log.e("tmpsetdata---->", "start");
        new Thread() {
            public void run(){
                WebService.getInstance().GetStationP2(sdate, station_names, new RequestCallback() {

                    @Override
                    public void onSuccess(final String result) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setDataDetail(result);
                                dialog.dismiss();  //删除该进度条
//                                setData(100, 30);
                            }
                        });
                    }

                    @Override
                    public void onFail(final String errorMsg) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();  //删除该进度条
                                // Toast：简易的消息提示框，自动消失
                                // 第一个参数：当前的上下文环境。可用getApplicationContext()或this
                                // 第二个参数：要显示的字符串。也可是R.string中字符串ID
                                // 第三个参数：显示的时间长短。Toast默认的有两个LENGTH_LONG(长)和LENGTH_SHORT(短)，也可以使用毫秒，如2000ms
                                Toast.makeText(getActivity(), "获取数据失败！"+errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        }.start();
//        Log.e("tmpsetdata---->", "end");
    }
    private void GetNewData(final String sdate, final String time, final String station_names) {
        new Thread() {
            public void run(){
                WebService.getInstance().GetStationP3(sdate, time, station_names, new RequestCallback() {

                    @Override
                    public void onSuccess(final String result) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setNewDataDetail(result);
                            }
                        });
                    }

                    @Override
                    public void onFail(final String errorMsg) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "获取数据失败！"+errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        }.start();
//        Log.e("tmpsetdata---->", "end");
    }

    public void setDataDetail(String result) {
        if (!result.contains("{")) {
            Toast.makeText(getActivity(), "获取数据失败：" + result, Toast.LENGTH_SHORT).show();
            return;
        }

        JsonParser parser = new JsonParser();//创建JSON解析器
        JsonObject object = (JsonObject) parser.parse(result); //创建JsonObject对象
        JsonArray parray = object.get("pcode").getAsJsonArray(); //得到为json的数组
        JsonArray forecast_parray = object.get("forecast_pcode").getAsJsonArray(); //得到为json的数组

        DecimalFormat df = new DecimalFormat("0.00");  //数据格式转换，四舍五入，保留两位小数

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        last_time = 0;
        for (int i = 0; i < parray.size(); i++) {
            JsonObject subObject = parray.get(i).getAsJsonObject();
//            Log.e("setdata------>",subObject.get("time").getAsString() + "---------------" + subObject.get("data").getAsString());
            yVals.add(new Entry(subObject.get("time").getAsFloat(), Float.parseFloat(df.format(Double.valueOf(subObject.get("data").getAsString())))));
            if(subObject.get("time").getAsInt() > last_time)
                last_time = subObject.get("time").getAsInt();
        }

        ArrayList<Entry> zVals = new ArrayList<Entry>();
        for(int i=0;i<forecast_parray.size();i++){
            JsonObject subObject=forecast_parray.get(i).getAsJsonObject();
//            Log.e("forecast_parray->",subObject.get("time").getAsString() + "---------------" + subObject.get("data").getAsString());
            zVals.add(new Entry(subObject.get("time").getAsFloat(), Float.parseFloat(df.format(Double.valueOf(subObject.get("data").getAsString())))));
            if(subObject.get("time").getAsInt() > last_time)
                last_time = subObject.get("time").getAsInt();
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "实时出力");
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
//        set1.setLabel("line one");

        LineDataSet set2 = new LineDataSet(zVals, "功率预测");
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
        otherChartSet();
    }
    public void setNewDataDetail(String result) {

        if (!result.contains("{")) {
           return;
        }

        JsonParser parser = new JsonParser();//创建JSON解析器
        JsonObject object = (JsonObject) parser.parse(result); //创建JsonObject对象
        JsonArray parray = object.get("pcode").getAsJsonArray(); //得到为json的数组
        JsonArray forecast_parray = object.get("forecast_pcode").getAsJsonArray(); //得到为json的数组

        DecimalFormat df = new DecimalFormat("0.00");  //数据格式转换，四舍五入，保留两位小数

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < parray.size(); i++) {
            JsonObject subObject = parray.get(i).getAsJsonObject();
//            Log.e("setdata------>",subObject.get("time").getAsString() + "---------------" + subObject.get("data").getAsString());
            yVals.add(new Entry(subObject.get("time").getAsFloat(), Float.parseFloat(df.format(Double.valueOf(subObject.get("data").getAsString())))));
            if(subObject.get("time").getAsInt() > last_time)
                last_time = subObject.get("time").getAsInt();
        }

        ArrayList<Entry> zVals = new ArrayList<Entry>();
        for(int i=0;i<forecast_parray.size();i++){
            JsonObject subObject=forecast_parray.get(i).getAsJsonObject();
//            Log.e("forecast_parray->",subObject.get("time").getAsString() + "---------------" + subObject.get("data").getAsString());
            zVals.add(new Entry(subObject.get("time").getAsFloat(), Float.parseFloat(df.format(Double.valueOf(subObject.get("data").getAsString())))));
            if(subObject.get("time").getAsInt() > last_time)
                last_time = subObject.get("time").getAsInt();
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "实时出力");
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
//        set1.setLabel("line one");

        LineDataSet set2 = new LineDataSet(zVals, "功率预测");
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
        otherChartSet();
    }

    private void otherChartSet(){

        // no description text
//        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f); //持续滚动时的速度快慢，[0,1) 0代表立即停止。

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // set an alternative background color
//        mChart.setBackgroundColor(Color.WHITE);  //设置图表背景 参数是个Color对象
//        mChart.setViewPortOffsets(0f, 0f, 0f, 0f);//设置图表显示的偏移量，不建议使用，因为会影响图表的自动计算


        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        // add data
//        setData(100, 30);
//        GetData();
        mChart.animateX(2500);
//        mChart.invalidate();

        // get the legend (only possible after setting data)
//        Legend l = mChart.getLegend();
//        // modify the legend ...
//        l.setEnabled(false);

        Typeface mTfLight = Typeface.createFromAsset(getActivity().getAssets(),
                "OpenSans-Regular.ttf");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(10f);
//        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(true); //是否绘制坐标轴的线，即含有坐标的那条线，默认是true
        xAxis.setDrawGridLines(true);//是否显示X坐标轴上的刻度竖线，默认是true
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setCenterAxisLabels(false);
        xAxis.setGranularity(1f); // one hour  设置x轴值之间的最小间隔。这可以用于在放大到为轴设置的小数位数不再允许在两个轴值之间进行区分的点时避免值重复。
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {  //将自定义格式化程序设置为轴。

//            private SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
//                long millis = TimeUnit.HOURS.toMillis((long) value);
//                return mFormat.format(new Date(millis));
                int hour = (int)value / 60;
                int min = (int)value % 60;
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
                return result;
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setTextColor(Color.rgb(255, 192, 56));
        leftAxis.setDrawLabels(true);
//        leftAxis.setStartAtZero(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void showSingleChoiceDialog(final Context context, String result){
        if(result.isEmpty() || result.equals(""))
            return;
        final String[] items = result.split(",");
        station_choice = -1;
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(context);
        singleChoiceDialog.setTitle("请选择一个场站");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        station_choice = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (station_choice != -1) {
//                            selected_station.setText(items[station_choice]);
//                            GetData();
                        }
                    }
                });
        singleChoiceDialog.show();
    }

    private void setSpinnerData(final Context context, String result){
        Log.e("result-->",result);
        if(result.isEmpty() || result.equals(""))
            return;
        final String[] items = result.split(",");

        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,items);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner_station.setAdapter(adapter);

        //添加事件Spinner事件监听
        spinner_station.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                station_name = spinner_station.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //设置默认值
        spinner_station.setVisibility(View.VISIBLE);

    }

    private void GetStationName() {
        dialog.show();   //显示进度条对话框
//        Log.e("tmpsetdata---->", "start");
        new Thread() {
            public void run(){
                WebService.getInstance().GetStationName(new RequestCallback() {

                    @Override
                    public void onSuccess(final String result) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();  //删除该进度条
                                Log.e("result-->",result);
                                setSpinnerData(getActivity(),result);
//                                showSingleChoiceDialog(getActivity(),result);
//                                setData(100, 30);
                            }
                        });
                    }

                    @Override
                    public void onFail(final String errorMsg) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();  //删除该进度条
                                // Toast：简易的消息提示框，自动消失
                                // 第一个参数：当前的上下文环境。可用getApplicationContext()或this
                                // 第二个参数：要显示的字符串。也可是R.string中字符串ID
                                // 第三个参数：显示的时间长短。Toast默认的有两个LENGTH_LONG(长)和LENGTH_SHORT(短)，也可以使用毫秒，如2000ms
                                Toast.makeText(getActivity(), "获取数据失败！"+errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        }.start();
//        Log.e("tmpsetdata---->", "end");
    }
}
