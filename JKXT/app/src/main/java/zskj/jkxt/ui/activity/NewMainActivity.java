package zskj.jkxt.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import zskj.jkxt.R;
import zskj.jkxt.util.DemoBase;
import zskj.jkxt.util.RiseNumberTextView;

public class NewMainActivity extends DemoBase implements View.OnClickListener {

    public static final String TAG = "NewMainActivity";
    private LinearLayout ll_jkdl, ll_jkyg;
    private LinearLayout piechart_legendLayout;
    private LinearLayout barchart_legendLayout;
    private RiseNumberTextView jk_yg, jk_dl;
    //    private RefreshView rv_refresh;
    private PieChart jkdl_piechart;
    private BarChart dldb_barchart;
    private SwipeRefreshLayout mRefreshLayout;
    DataInfo info = null;
    float database = 35.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        initView();
        getData(); //TODO 从服务器获取
    }
    /**
     * 初始化view
     */
    private void initView() {
        mRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.refresh_layout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "REFRESH.........................");//TODO
                database++;
                getData();
                mRefreshLayout.setRefreshing(false);
            }
        });
        ll_jkdl = (LinearLayout) this.findViewById(R.id.ll_jkdl);
        ll_jkyg = (LinearLayout) this.findViewById(R.id.ll_jkyg);
        jk_dl = (RiseNumberTextView) this.findViewById(R.id.jk_dl);
        jk_yg = (RiseNumberTextView) this.findViewById(R.id.jk_yg);
        jkdl_piechart = (PieChart) this.findViewById(R.id.jkdl_piechart);
        piechart_legendLayout = (LinearLayout) this.findViewById(R.id.piechart_legendLayout);
        dldb_barchart = (BarChart) this.findViewById(R.id.dldb_barchart);
        barchart_legendLayout = (LinearLayout) this.findViewById(R.id.barchart_legendLayout);
        ll_jkdl.setOnClickListener(this);
        ll_jkyg.setOnClickListener(this);
        initPie();
        initBar();
    }

    private void getData() {
        info = new DataInfo();
        info.elec = 233.25f;
        info.power = 152.30f;
        info.pie = new HashMap<>();
        for (int i = 0; i < jkdl_Pie.length; i++) {
            info.pie.put(jkdl_Pie[i], 32.62f + database + i * 2);
        }
        info.bar = new HashMap<>();
        for (int i = 0; i < stations.length; i++) {
            info.bar.put(stations[i], database + i * 5);
        }
        setData();
    }

    private void setData() {
        startNumberAutoUp(jk_dl, info.elec + database);
        startNumberAutoUp(jk_yg, info.power + database);
        setPieData();
        setBarChartData();
    }

    private void initPie() {
        //饼状图
        jkdl_piechart.setBackgroundColor(Color.WHITE);
        jkdl_piechart.setUsePercentValues(true);//设置饼图是否使用百分比
        jkdl_piechart.getDescription().setEnabled(false); //false，不显示饼图右下角文字描述
//        jkdl_piechart.setCenterText(generateCenterSpannableText()); //设置中心文字样式
        jkdl_piechart.setDrawHoleEnabled(false); //false，设置不绘制中间圆盘，默认为true
//        jkdl_piechart.setHoleColor(Color.WHITE); //设置中间圆盘的颜色
//        jkdl_piechart.setTransparentCircleColor(Color.BLUE);  //与中间圆盘文字有关
//        jkdl_piechart.setTransparentCircleAlpha(110);//设置中间圆盘的半径,值为所占饼图的百分比
        jkdl_piechart.setHoleRadius(58f);
        jkdl_piechart.setTransparentCircleRadius(61f);//设置中间透明圈的半径,值为所占饼图的百分比
        jkdl_piechart.setDrawCenterText(false); //false，不显示中心文字，默认为true
        jkdl_piechart.setRotationEnabled(false);//设置圆盘是否转动，默认转动
        jkdl_piechart.setHighlightPerTapEnabled(true);
        //设置显示半个饼图
//        jkdl_piechart.setMaxAngle(180f); // HALF CHART
//        jkdl_piechart.setRotationAngle(180f);
//        jkdl_piechart.setCenterTextOffset(0, -20);
        Legend l = jkdl_piechart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);//图注显示位置，上部、居中、横向
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setEnabled(false); //false，不显示图注，默认为true
        // entry label styling
        jkdl_piechart.setEntryLabelColor(Color.WHITE);
        jkdl_piechart.setEntryLabelTypeface(mTfRegular);
        jkdl_piechart.setEntryLabelTextSize(12f);

    }

    private void initBar() {
        //柱状图
        dldb_barchart.getDescription().setEnabled(false);
        dldb_barchart.setPinchZoom(false);
        dldb_barchart.setDrawBarShadow(false);
        dldb_barchart.setDrawGridBackground(false);
        XAxis xAxis = dldb_barchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return stations[(int) value];
            }
        });
        xAxis.setLabelCount(4);
        dldb_barchart.getAxisLeft().setDrawGridLines(false);
        dldb_barchart.getLegend().setEnabled(false); //设置不显示x轴图注
        dldb_barchart.setFitBars(true);
    }

    int position = 0;

    private void setBarChartData() {
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        barchart_legendLayout.removeAllViews();
        for (String key : info.bar.keySet()) {
            yVals.add(new BarEntry(position % info.bar.keySet().size(), info.bar.get(key)));
            LinearLayout.LayoutParams lp = new LinearLayout.
                    LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.weight = 1;//设置比重为1
            lp.setMargins(20, 10, 0, 0);
            LinearLayout layout = new LinearLayout(this);//单个图例的布局
            layout.setOrientation(LinearLayout.HORIZONTAL);//水平排列
            layout.setGravity(Gravity.LEFT);//左对齐
            layout.setLayoutParams(lp);

            //添加color
            LinearLayout.LayoutParams colorLP = new LinearLayout.
                    LayoutParams(20, 20);
            colorLP.setMargins(20, 10, 20, 0);
            LinearLayout colorLayout = new LinearLayout(this);
            colorLayout.setLayoutParams(colorLP);
            colorLayout.setBackgroundColor(mColors[position % mColors.length]);
            layout.addView(colorLayout);

            //添加label
            TextView labelTV = new TextView(this);
            labelTV.setText(key);
            layout.addView(labelTV);

            //添加data
            TextView dataTV = new TextView(this);
            dataTV.setText(String.valueOf(info.bar.get(key)));
            layout.addView(dataTV);
            barchart_legendLayout.addView(layout);//legendLayout为外层布局即整个图例布局，是在xml文件中定义
            position++;
        }

        BarDataSet set = new BarDataSet(yVals, "Data Set");
        set.setColors(mColors);
        set.setDrawValues(false); //柱状图上不显示y轴的值

        BarData data = new BarData(set);

        dldb_barchart.setData(data);
        dldb_barchart.invalidate();
        dldb_barchart.animateXY(3000, 3000);
    }

    private void startNumberAutoUp(RiseNumberTextView view, float number) {
        view.withNumber(number);
        view.setDuration(1500);
        view.start();
    }

    int color = 0;

    private void setPieData() {
        ArrayList<PieEntry> values = new ArrayList<PieEntry>();
        piechart_legendLayout.removeAllViews();
        for (String key : info.pie.keySet()) {
            values.add(new PieEntry(info.pie.get(key), key));
            LinearLayout.LayoutParams lp = new LinearLayout.
                    LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.weight = 1;//设置比重为1
            LinearLayout layout = new LinearLayout(this);//单个图例的布局
            layout.setOrientation(LinearLayout.VERTICAL);//垂直排列
            layout.setGravity(Gravity.LEFT);//左对齐
            layout.setLayoutParams(lp);

            //添加color
            LinearLayout.LayoutParams colorLP = new LinearLayout.
                    LayoutParams(20, 20);
            colorLP.setMargins(0, 0, 20, 0);
            LinearLayout colorLayout = new LinearLayout(this);
            colorLayout.setLayoutParams(colorLP);
            colorLayout.setBackgroundColor(mColors[color % 10]);
            color++;
            layout.addView(colorLayout);

            //添加label
            TextView labelTV = new TextView(this);
            labelTV.setText(key);
            layout.addView(labelTV);

            //添加data
            TextView dataTV = new TextView(this);
            dataTV.setText(String.valueOf(info.pie.get(key)));
            layout.addView(dataTV);
            piechart_legendLayout.addView(layout);//legendLayout为外层布局即整个图例布局，是在xml文件中定义
        }
        PieDataSet dataSet = new PieDataSet(values, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(mColors);
//        dataSet.setDrawValues(false); //false，饼图上不显示比例值，默认为true
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mTfLight);
        jkdl_piechart.setData(data);
        jkdl_piechart.invalidate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_jkdl:
                Intent intent = new Intent();
                intent.setClass(NewMainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_jkyg:
                break;
        }

    }

    class DataInfo {
        public float elec;//电量
        public float power;//有功
        public HashMap<String, Float> pie;
        public HashMap<String, Float> bar;
    }
}
