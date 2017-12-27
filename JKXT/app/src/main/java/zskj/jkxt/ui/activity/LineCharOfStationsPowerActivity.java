package zskj.jkxt.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import zskj.jkxt.R;
import zskj.jkxt.ui.widget.MyMarkerView;
import zskj.jkxt.util.DemoData;

public class LineCharOfStationsPowerActivity extends AppCompatActivity {

    private LineChart mChart;

    ArrayList<Entry> station1Data = new ArrayList<>();
    ArrayList<Entry> station2Data = new ArrayList<>();
    ArrayList<Entry> station3Data = new ArrayList<>();
    ArrayList<Entry> station4Data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_char_of_stations_power);

        mChart = (LineChart) this.findViewById(R.id.lineChart_stationsPower);
        setTmpData();
        refreshChartSet();
    }

    public void setTmpData(){
        for(int i=1;i<=10;i++){
            station1Data.add(new Entry(i,(float) (Math.random() * 20) + 15));
            station2Data.add(new Entry(i,(float) (Math.random() * 50) + 15));
            station3Data.add(new Entry(i,(float) (Math.random() * 80) + 15));
            station4Data.add(new Entry(i,(float) (Math.random() * 120) + 15));
        }
    }

    LineDataSet station1 = null;
    LineDataSet station2 = null;
    LineDataSet station3 = null;
    LineDataSet station4 = null;
    LineData dataline = null;

    public void refreshChartSet() {
        if (station1 == null) {
            station1 = createDataSet(station1Data, "场站1", DemoData.mColors[0]);
        }
        if (station2 == null) {
            station2 = createDataSet(station2Data, "场站2", DemoData.mColors[1]);
        }
        if (station3 == null) {
            station3 = createDataSet(station3Data, "场站3", DemoData.mColors[2]);
        }
        if (station4 == null) {
            station4 = createDataSet(station4Data, "场站4", DemoData.mColors[3]);
        }

        station1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        station2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        station3.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        station4.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        dataline = new LineData();
        dataline.setValueTextColor(Color.WHITE);
        dataline.setValueTextSize(9f);
//            dataline.addDataSet(forecastline);
//            dataline.addDataSet(pline);
        if(station1 != null && station1.getEntryCount()>0) {
            dataline.addDataSet(station1);
        }
        if(station2 != null && station2.getEntryCount()>0){
            dataline.addDataSet(station2);
        }
        if(station3 != null && station3.getEntryCount()>0) {
            dataline.addDataSet(station3);
        }
        if(station4 != null && station4.getEntryCount()>0){
            dataline.addDataSet(station4);
        }
        mChart.setData(dataline);
        mChart.notifyDataSetChanged();
        initChart();
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
        //设置某个点数值显示方式
        MyMarkerView mv = new MyMarkerView(getApplicationContext(), R.layout.custom_marker_view);
        mv.setChartView(mChart);
        mChart.setMarker(mv);
        mChart.animateX(2500);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setScaleXEnabled(true);
        mChart.setScaleYEnabled(true);
        mChart.setPinchZoom(false);
        Description des = new Description();
        des.setText("有功功率曲线");
        mChart.setDescription(des);
//        mChart.getDescription().setEnabled(false);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(true);
        xAxis.resetAxisMaximum();
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
}
