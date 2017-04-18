package zskj.jkxt.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import zskj.jkxt.R;
import zskj.jkxt.api.WebService;
import zskj.jkxt.ui.widget.MyMarkerView;
import zskj.jkxt.util.DateTimePickDialogUtil;

/**
 * Created by WYY on 2017/3/14.
 * 全场功率页面
 */

public class PowerFragment extends Fragment {

    public static PowerFragment getInstance(String ranges) {
        PowerFragment powerFragment = new PowerFragment();
        Bundle b = new Bundle();
        b.putString("ranges", ranges);
        powerFragment.setArguments(b);
        return powerFragment;
    }

    private static final String TAG = "PowerFragment";

    //view
    Activity mContext;
    private Button select_time, select_station, sure;
    private View mProgressView;
    private LineChart mChart;
    //dataSet
    String ranges;
    String[] stations;
    private String station_name;
    private int last_time = 0;
    private int period = 1 * 60 * 1000;//1分钟刷新一次


    ArrayList<Entry> forecastData = new ArrayList<>();
    ArrayList<Entry> pData = new ArrayList<>();


    //task
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
    DecimalFormat df = new DecimalFormat("0.00");  //数据格式转换，四舍五入，保留两位小数
    GetStationNameTask mGetStationNameTask;
    GetPowerDataTask mGetPowerDataTask;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_power, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        //初始化获取场站信息
        getStationName();
    }

    private void initView() {
        select_time = (Button) getView().findViewById(R.id.select_time);
        select_station = (Button) getView().findViewById(R.id.select_station);
        sure = (Button) getView().findViewById(R.id.sure);
        mChart = (LineChart) getView().findViewById(R.id.chart);
        mProgressView = getView().findViewById(R.id.progress);
        select_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        getActivity(), select_time.getText().toString());
                dateTimePicKDialog.dateTimePicKDialog(select_time);
            }
        });
        select_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stations != null && stations.length > 0) {//有数据则显示选择框，无数据，请求
                    showStatiosDialog();
                    return;
                }
                getStationName();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destoryTimer();
                //还原time
                last_time = 0;
                if (select_time.getText().toString().equals(sdf.format(new Date()))) {//如果是今天，启动轮循
                    polling();
                } else {
                    getStationPower(select_time.getText().toString(), station_name);
                }
            }
        });
//        initChart();
    }

    private void initData() {
        ranges = getArguments().getString("ranges");
        select_time.setText(sdf.format(new Date()));
    }

    /**
     * 获取场站
     */
    private void getStationName() {
        if (mGetStationNameTask != null) {//不为null 说明操作正在进行，规避多次点击登录按钮操作
            Toast.makeText(mContext, "正在加厂站列表，请稍候...", Toast.LENGTH_SHORT).show();
            return;
        }
        mGetStationNameTask = new GetStationNameTask();
        mGetStationNameTask.execute();
    }

    /**
     * 获取场站task
     */
    private class GetStationNameTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return WebService.getInstance().getStationName(ranges);
        }

        @Override
        protected void onPostExecute(String result) {
            mGetStationNameTask = null;
            parserResult(result);
            //获取完默认展示第一个场站信息
            if (stations != null && stations.length > 0)
                setStation(stations[0]);

        }

        @Override
        protected void onCancelled() {
            mGetStationNameTask = null;
        }
    }


    private void parserResult(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            int code = obj.optInt("code");
            if (code == 0) {
                String msg = obj.optString("msg");
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                return;
            }
            JSONArray data = obj.optJSONArray("data");
            if (data != null && data.length() > 0) {
                stations = new String[data.length()];
                for (int i = 0; i < data.length(); i++) {
                    JSONObject object = data.optJSONObject(i);
                    stations[i] = object.getString("StationName");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mContext, WebService.ERRORMSG, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示场站列表
     */
    AlertDialog.Builder builder = null;

    private void showStatiosDialog() {
        if (stations == null || stations.length <= 0)
            return;
        if (builder == null) {
            builder = new AlertDialog.Builder(mContext);
            builder.setTitle("请选择场站");
        }
        builder.setItems(stations, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setStation(stations[i]);
            }
        });
        builder.create();
        builder.show();
    }

    /**
     * 设置当前场站名
     *
     * @param name
     */
    private void setStation(String name) {
        station_name = name;
        select_station.setText(station_name);
    }

    /**
     * 获取场站功率信息
     *
     * @param sdate        日期（精确到天）
     * @param station_name 场站名 最好以场站ID索引
     */
    private void getStationPower(String sdate, String station_name) {
        if (TextUtils.isEmpty(sdate) || TextUtils.isEmpty(station_name)) {
            Toast.makeText(mContext, "请选择日期和场站", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mGetPowerDataTask != null)
            return;
        mGetPowerDataTask = new GetPowerDataTask(sdate, station_name);
        mGetPowerDataTask.execute();
    }

    /**
     * 获取场站功率task
     */
    private class GetPowerDataTask extends AsyncTask<Void, Void, String> {
        String sdate;
        String station_name;

        GetPowerDataTask(String sdate, String station_name) {
            this.sdate = sdate;
            this.station_name = station_name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (last_time == 0)
                showProgress(true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return WebService.getInstance().getStationPower(sdate, String.valueOf(last_time), station_name);
        }

        @Override
        protected void onPostExecute(String result) {
//            Log.e(TAG, "result:" + result);
            mGetPowerDataTask = null;
            showProgress(false);
            setDataDetail(result);
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
            mGetPowerDataTask = null;
        }
    }


    /**
     * 解析数据刷新界面
     *
     * @param result
     */
    public void setDataDetail(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            int code = obj.optInt("code");
            if (code == 0) {
                String msg = obj.optString("msg");
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                return;
            }
            if (last_time == 0) {
                forecastData.clear();
                pData.clear();
            }
            JSONObject data = obj.optJSONObject("data");
            if (data != null) {
                JSONArray pcode = data.optJSONArray("pcode");
                JSONArray fpcode = data.optJSONArray("forecast_pcode");
                Log.e("data size-->",pcode.length() + "   " + fpcode.length());
                if (pcode != null && pcode.length() > 0) {
                    int time = 0;
                    for (int i = 0; i < pcode.length(); i++) {
                        JSONObject detail = pcode.optJSONObject(i);
                        try {
                            pData.add(new Entry(detail.optInt("time"), Float.parseFloat(df.format(Double.valueOf(detail.optString("data"))))));
                            if (detail.optInt("time") > time)
                                time = detail.optInt("time");
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    last_time = time;
                }
                if (fpcode != null && fpcode.length() > 0) {
                    for (int i = 0; i < fpcode.length(); i++) {
                        JSONObject fdetail = fpcode.optJSONObject(i);
                        try {
                            forecastData.add(new Entry(fdetail.optInt("time"), Float.parseFloat(df.format(Double.valueOf(fdetail.optString("data"))))));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            Log.e("data size-->",pData.size() + "   " + forecastData.size());
            refreshChartSet();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mContext, WebService.ERRORMSG, Toast.LENGTH_SHORT).show();
        }


    }

    //-----------------------------------------------定时操作BEGIN-------------------------------------------------------
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mGetPowerDataTask != null)
                mGetPowerDataTask.cancel(true);//如果正在执行，取消掉 网络请求超时时间设置要小于轮循间隔时间，则可避免
            getStationPower(select_time.getText().toString(), station_name);
        }
    };

    Timer mTimer;

    private void polling() {
        destoryTimer();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 0, period);
    }

    private void destoryTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
    //------------------------------------------定时操作 END -----------------------------------------------

    LineDataSet forecastline = null;
    LineDataSet pline = null;
    LineData dataline = null;

    public void refreshChartSet() {
        dataline = mChart.getData();
        if (dataline != null) {
            forecastline = (LineDataSet) dataline.getDataSetByIndex(0);
            pline = (LineDataSet) dataline.getDataSetByIndex(1);
            dataline.clearValues();
            dataline.addDataSet(forecastline);
            dataline.addDataSet(pline);
            dataline.notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRangeMaximum(120);
        } else {
            if (pline == null) {
                pline = createDataSet(pData, "实时出力", Color.RED);
            }
            if (forecastline == null) {
                forecastline = createDataSet(forecastData, "功率预测", Color.BLUE);
            }
            dataline = new LineData();
            dataline.setValueTextColor(Color.WHITE);
            dataline.setValueTextSize(9f);
            dataline.addDataSet(forecastline);
            dataline.addDataSet(pline);
            mChart.setData(dataline);
            mChart.notifyDataSetChanged();
            initChart();
        }
        mChart.invalidate();
    }

    private LineDataSet createDataSet(List<Entry> data, String label, int color) {
        LineDataSet dataset = new LineDataSet(data, label);
        dataset.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataset.setValueTextColor(ColorTemplate.getHoloBlue());
        dataset.setLineWidth(1.5f);
        dataset.setDrawCircles(false);
        dataset.setDrawValues(false);
        dataset.setFillAlpha(65);
        dataset.setFillColor(ColorTemplate.getHoloBlue());
        dataset.setHighLightColor(Color.rgb(244, 117, 117));
        dataset.setDrawCircleHole(false);
        dataset.setColor(color);
        return dataset;
    }


    private void initChart() {
        mChart.setDragDecelerationFrictionCoef(0.9f);
        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);
        mv.setChartView(mChart);
        mChart.setMarker(mv);
        mChart.animateX(2500);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int hour = (int) value / 60;
                int min = (int) value % 60;
                String result = null;
                if (hour < 10) {
                    result = "0" + hour + ":";
                } else {
                    result = hour + ":";
                }

                if (min < 10) {
                    result += "0" + min;
                } else {
                    result += min;
                }
                return result;
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setTextColor(Color.rgb(255, 192, 56));
        leftAxis.setDrawLabels(true);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        destoryTimer();
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
