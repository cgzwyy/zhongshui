package zskj.jkxt.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import zskj.jkxt.R;
import zskj.jkxt.ui.activity.StationInfoActivity;
import zskj.jkxt.util.DemoData;
import zskj.jkxt.util.RiseNumberTextView;

import static zskj.jkxt.util.DemoData.mColors;

/**
 * Created by WYY on 2017/12/22.
 */

public class JKInfoFragment extends Fragment{

    Context mContext;
    private LinearLayout ll_jkdl,ll_jkyg;
    private LinearLayout piechart_legendLayout;
    private LinearLayout barchart_legendLayout;
    private RiseNumberTextView jk_yg,jk_dl;
    //    private RefreshView rv_refresh;
    private PieChart jkdl_piechart;
    private BarChart dldb_barchart;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_jkinfo, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AssetManager mgr = mContext.getAssets();

        ll_jkdl = (LinearLayout) getView().findViewById(R.id.ll_jkdl);
        ll_jkdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), StationInfoActivity.class);
                startActivity(intent);
            }
        });

        ll_jkyg = (LinearLayout) getView().findViewById(R.id.ll_jkyg);
        ll_jkyg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        jk_dl = (RiseNumberTextView) getView().findViewById(R.id.jk_dl);
        startNumberAutoUp(jk_dl, "233.25");
        jk_yg = (RiseNumberTextView) getView().findViewById(R.id.jk_yg);
        startNumberAutoUp(jk_yg, "123");

        jkdl_piechart = (PieChart) getView().findViewById(R.id.jkdl_piechart);
        jkdl_piechart.setBackgroundColor(Color.WHITE);

        piechart_legendLayout = (LinearLayout) getView().findViewById(R.id.piechart_legendLayout);


        //设置饼图是否使用百分比
        jkdl_piechart.setUsePercentValues(true);
        jkdl_piechart.getDescription().setEnabled(false); //false，不显示饼图右下角文字描述

        jkdl_piechart.setCenterText(generateCenterSpannableText()); //设置中心文字样式

        jkdl_piechart.setDrawHoleEnabled(false); //false，设置不绘制中间圆盘，默认为true
//        jkdl_piechart.setHoleColor(Color.WHITE); //设置中间圆盘的颜色

        //与中间圆盘文字有关
//        jkdl_piechart.setTransparentCircleColor(Color.BLUE);
//        jkdl_piechart.setTransparentCircleAlpha(110);

        //设置中间圆盘的半径,值为所占饼图的百分比
        jkdl_piechart.setHoleRadius(58f);
        jkdl_piechart.setTransparentCircleRadius(61f);//设置中间透明圈的半径,值为所占饼图的百分比

        jkdl_piechart.setDrawCenterText(false); //false，不显示中心文字，默认为true

        jkdl_piechart.setRotationEnabled(false);//设置圆盘是否转动，默认转动
        jkdl_piechart.setHighlightPerTapEnabled(true);

        //设置显示半个饼图
//        jkdl_piechart.setMaxAngle(180f); // HALF CHART
//        jkdl_piechart.setRotationAngle(180f);
//        jkdl_piechart.setCenterTextOffset(0, -20);

        setData(2, 100);

        jkdl_piechart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
//        jkdl_piechart.setDrawEntryLabels(false); //设置在饼图中不显示图注

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
        jkdl_piechart.setEntryLabelTypeface(Typeface.createFromAsset(mgr,"OpenSans-Regular.ttf"));
        jkdl_piechart.setEntryLabelTextSize(12f);

        //柱状图
        dldb_barchart = (BarChart) getView().findViewById(R.id.dldb_barchart);
        barchart_legendLayout = (LinearLayout) getView().findViewById(R.id.barchart_legendLayout);

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
                return DemoData.stations[(int) value];
            }
        });
        xAxis.setLabelCount(4);

        dldb_barchart.getAxisLeft().setDrawGridLines(false);

        dldb_barchart.getLegend().setEnabled(false); //设置不显示x轴图注

        setBarChartData(4);
        dldb_barchart.setFitBars(true);

    }

    private void setBarChartData(int count) {

        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * count) + 15;
            yVals.add(new BarEntry(i, (int) val));

            LinearLayout.LayoutParams lp=new LinearLayout.
                    LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            lp.weight=1;//设置比重为1
            lp.setMargins(20,10,0,0);
            LinearLayout layout=new LinearLayout(mContext);//单个图例的布局
            layout.setOrientation(LinearLayout.HORIZONTAL);//水平排列
            layout.setGravity(Gravity.LEFT);//左对齐
            layout.setLayoutParams(lp);

            //添加color
            LinearLayout.LayoutParams colorLP=new LinearLayout.
                    LayoutParams(20,20);
            colorLP.setMargins(20, 10, 20, 0);
            LinearLayout colorLayout=new LinearLayout(mContext);
            colorLayout.setLayoutParams(colorLP);
            colorLayout.setBackgroundColor(mColors[i]);
            layout.addView(colorLayout);

            //添加label
            TextView labelTV=new TextView(mContext);
            labelTV.setText(DemoData.stations[i]+" ");
            layout.addView(labelTV);

            //添加data
            TextView dataTV=new TextView(mContext);
            dataTV.setText(((int) val)+"");
            layout.addView(dataTV);

            barchart_legendLayout.addView(layout);//legendLayout为外层布局即整个图例布局，是在xml文件中定义

        }

        BarDataSet set = new BarDataSet(yVals, "Data Set");
        set.setColors(DemoData.mColors);
        set.setDrawValues(false); //柱状图上不显示y轴的值

        BarData data = new BarData(set);

        dldb_barchart.setData(data);
        dldb_barchart.invalidate();
        dldb_barchart.animateXY(3000, 3000);
    }

    /**
     * 数字自动增长
     *
     * @param number    数值的目标数(最后的值)
     * @param view      自定义的RiseNumberTextView
     */
    private void startNumberAutoUp(RiseNumberTextView view, String number) {
        view.withNumber(Float.parseFloat(number));
        // 设置动画播放时间
        view.setDuration(1500);
        // 开始播放动画
        view.start();
    }

    private void setData(int count, float range) {

        ArrayList<PieEntry> values = new ArrayList<PieEntry>();

        for (int i = 0; i < count; i++) {
            float xValue = (float) ((Math.random() * range) + range / 5);
            Log.e("xValue","----------->"+xValue);
            values.add(new PieEntry(xValue, DemoData.jkdl_Pie[i]));
//            values.add(new PieEntry((float) ((Math.random() * range) + range / 5), mParties[i % mParties.length]));

            LinearLayout.LayoutParams lp=new LinearLayout.
                    LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            lp.weight=1;//设置比重为1
            LinearLayout layout=new LinearLayout(mContext);//单个图例的布局
            layout.setOrientation(LinearLayout.VERTICAL);//垂直排列
            layout.setGravity(Gravity.LEFT);//左对齐
            layout.setLayoutParams(lp);

            //添加color
            LinearLayout.LayoutParams colorLP=new LinearLayout.
                    LayoutParams(20,20);
            colorLP.setMargins(0, 0, 20, 0);
            LinearLayout colorLayout=new LinearLayout(mContext);
            colorLayout.setLayoutParams(colorLP);
            colorLayout.setBackgroundColor(DemoData.mColors[i]);
            layout.addView(colorLayout);

            //添加label
            TextView labelTV=new TextView(mContext);
            labelTV.setText(DemoData.jkdl_Pie[i]+" ");
            layout.addView(labelTV);

            //添加data
            TextView dataTV=new TextView(mContext);
            dataTV.setText(xValue+"");
            layout.addView(dataTV);

            piechart_legendLayout.addView(layout);//legendLayout为外层布局即整个图例布局，是在xml文件中定义
        }

        PieDataSet dataSet = new PieDataSet(values, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(DemoData.mColors);
//        dataSet.setDrawValues(false); //false，饼图上不显示比例值，默认为true
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
//        data.setValueTypeface(mTfLight);
        jkdl_piechart.setData(data);

        jkdl_piechart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {

//        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
//        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        SpannableString s = new SpannableString("test");
        return s;
    }
}
