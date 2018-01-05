package zskj.jkxt.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import zskj.jkxt.R;
import zskj.jkxt.WebService;
import zskj.jkxt.domain.JKInfo;
import zskj.jkxt.ui.activity.StationInfoActivity;
import zskj.jkxt.ui.activity.StatisticalDataActivity;
import zskj.jkxt.util.DemoData;
import zskj.jkxt.util.RiseNumberTextView;

import static zskj.jkxt.util.DemoData.mColors;

/**
 * Created by WYY on 2017/12/22.
 */

public class JKInfoFragment extends Fragment{

    Context mContext;
    private static final String TAG = "JKInfoFragment";
    private LinearLayout ll_jkdl,ll_jkyg;
    private LinearLayout piechart_legendLayout;
    private LinearLayout barchart_legendLayout;
    private RiseNumberTextView jk_yg,jk_dl;
    //    private RefreshView rv_refresh;
    private PieChart jkdl_piechart;
    private BarChart dldb_barchart;
    private SwipeRefreshLayout mRefreshLayout;
    private AssetManager mgr;
    private TextView jk_jhfd;
//    float database = 35.0f;
    String ranges;
//    private View mProgressView;
    GetJKInfoTask mTask;
    JKInfo mJKInfo;
    DecimalFormat df = new DecimalFormat("0.00");  //数据格式转换，四舍五入，保留两位小数
    String[] Xbar_name;

    public void setRanges(String ranges){
        Log.e(TAG,"---->"+ranges);
        this.ranges = ranges;
    }

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
        mgr = mContext.getAssets();
        initView();
        getData();
    }

    /**
     * 初始化view
     */
    private void initView() {
//        Log.e(TAG,"---->initView() start");
        mRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.refresh_jkinfo);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "REFRESH.........................");//TODO
//                database++;
                getData();
                mRefreshLayout.setRefreshing(false);
            }
        });
        ll_jkdl = (LinearLayout) getView().findViewById(R.id.ll_jkdl);
        ll_jkyg = (LinearLayout) getView().findViewById(R.id.ll_jkyg);
        jk_dl = (RiseNumberTextView) getView().findViewById(R.id.jk_dl);
        jk_yg = (RiseNumberTextView) getView().findViewById(R.id.jk_yg);
        jk_jhfd = (TextView) getView().findViewById(R.id.jk_jhfd);
        jkdl_piechart = (PieChart) getView().findViewById(R.id.jkdl_piechart);
        piechart_legendLayout = (LinearLayout) getView().findViewById(R.id.piechart_legendLayout);
        dldb_barchart = (BarChart) getView().findViewById(R.id.dldb_barchart);
        barchart_legendLayout = (LinearLayout) getView().findViewById(R.id.barchart_legendLayout);
//        mProgressView = getView().findViewById(R.id.jkinfo_progress);
        ll_jkdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), StationInfoActivity.class);
                intent.putExtra("ranges",ranges);
//                intent.putExtra("flag",1);
//                startActivityForResult(intent,1);
                startActivity(intent);
            }
        });
        ll_jkyg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), StatisticalDataActivity.class);
                intent.putExtra("ranges",ranges);
                startActivity(intent);
            }
        });
        initPie();
        initBar();
    }

    private void initPie() {
        //饼状图
        jkdl_piechart.setBackgroundColor(Color.WHITE);

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

//        setPicCharData(2, 100);

        jkdl_piechart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        jkdl_piechart.setDrawEntryLabels(false); //设置在饼图中不显示图注

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

    }

    private void initBar() {
        //柱状图
        dldb_barchart.getDescription().setEnabled(false);
        dldb_barchart.setScaleEnabled(false);//启用/禁用缩放图表上的两个轴。
//        dldb_barchart.setPinchZoom(false);//如果设置为true，捏缩放功能。 如果false，x轴和y轴可分别放大。

        dldb_barchart.setDrawBarShadow(false);
        dldb_barchart.setDrawGridBackground(false);

        dldb_barchart.getAxisLeft().setDrawGridLines(false);

        dldb_barchart.getLegend().setEnabled(false); //设置不显示x轴图注

//        setBarChartData(4);
        dldb_barchart.setFitBars(true);
    }

    private void getData() {
        if (mTask != null)
            return;
        mTask = new GetJKInfoTask();
        mJKInfo = new JKInfo();
        mTask.execute();
    }

    private void setData(JKInfo mJKInfo) {
//        startNumberAutoUp(jk_dl, 233.25 + database + "");
//        startNumberAutoUp(jk_yg, 152.30 + database + "");
//        setPieChartData(2, 100);
//        setBarChartData(4);
        startNumberAutoUp(jk_dl, mJKInfo.jk_jrfd);
        startNumberAutoUp(jk_yg, mJKInfo.jk_jryg);
        jk_jhfd.setText(mJKInfo.jk_jhfd);
        setPieChartData(mJKInfo.jk_pie_data);
        setBarChartData(mJKInfo.jk_rfdl);
    }


    private void setBarChartData(Map<String,String> map) {

        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        barchart_legendLayout.removeAllViews();
        Xbar_name = new String[map.size()];
        Iterator<String> iterator = map.keySet().iterator();

        int i = 0;
        while (iterator.hasNext()){
            String key = iterator.next();
            String value = map.get(key);
            yVals.add(new BarEntry(i, Float.parseFloat(value)));
            Xbar_name[i] = key;
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
            labelTV.setText(key + "    ");
            labelTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.content_text_size));
            labelTV.setTextColor(getResources().getColor(R.color.gray));
            layout.addView(labelTV);

            //添加data
            TextView dataTV=new TextView(mContext);
            dataTV.setText(value);
            dataTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.content_text_size));
            dataTV.setTextColor(getResources().getColor(R.color.gray));
            layout.addView(dataTV);

            barchart_legendLayout.addView(layout);//legendLayout为外层布局即整个图例布局，是在xml文件中定义
            i++;
        }

        BarDataSet set = new BarDataSet(yVals, "Data Set");
        set.setColors(DemoData.mColors);
        set.setDrawValues(true); //柱状图上显示y轴的值

        BarData data = new BarData(set);

        dldb_barchart.setData(data);
        dldb_barchart.invalidate();
        dldb_barchart.animateXY(3000, 3000);

        XAxis xAxis = dldb_barchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return Xbar_name[(int) value];
            }
        });
        xAxis.setLabelCount(map.size());
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

    private void setPieChartData(Map<String,String> map) {

        ArrayList<PieEntry> values = new ArrayList<PieEntry>();
        piechart_legendLayout.removeAllViews();
        Iterator<String> iter = map.keySet().iterator();
        int i=0;
        while(iter.hasNext()){
            String key = iter.next();
            String value = map.get(key);
            values.add(new PieEntry(Float.parseFloat(value), key));

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
            colorLP.setMargins(0, 20, 20, 0);
            LinearLayout colorLayout=new LinearLayout(mContext);
            colorLayout.setLayoutParams(colorLP);
            colorLayout.setBackgroundColor(DemoData.mColors[i]);
            layout.addView(colorLayout);

            //添加label
            TextView labelTV=new TextView(mContext);
            labelTV.setText(key);
            labelTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.content_text_size));
            labelTV.setTextColor(getResources().getColor(R.color.gray));
            layout.addView(labelTV);

            //添加data
            TextView dataTV=new TextView(mContext);
            dataTV.setText(value);
            dataTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.content_text_size));
            dataTV.setTextColor(getResources().getColor(R.color.gray));
            layout.addView(dataTV);

            piechart_legendLayout.addView(layout);//legendLayout为外层布局即整个图例布局，是在xml文件中定义
            i++;
        }

        PieDataSet dataSet = new PieDataSet(values, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(DemoData.mColors);
//        dataSet.setDrawValues(false); //false，饼图上不显示比例值，默认为true
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(16f);
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

    private class GetJKInfoTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgress(true);
        }

        @Override
        protected String doInBackground(Void... voids) {
//            Log.e("ranges","-------->"+ranges);
            return WebService.getInstance().getJKInfo(ranges);
        }

        @Override
        protected void onPostExecute(String result) {
            mTask = null;
//            showProgress(false);
            dealResult(result);
        }

        @Override
        protected void onCancelled() {
            mTask = null;
//            showProgress(false);
        }
    }

    private void dealResult(String result) {
        try {
            if(result != null && result.toString() != null){
                JSONObject obj = new JSONObject(result);
                int code = obj.optInt("code");
                if (code == 0) {
                    String msg = obj.optString("msg");
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                //JSONObject data = obj.optJSONObject("data");
                //JSONArray userLists = data.optJSONArray("userlist");
                JSONObject data = obj.optJSONObject("data");
                if(data.has("今日发电")){
                    mJKInfo.jk_jrfd = df.format(Double.valueOf(data.optString("今日发电")));
                }else{
                    mJKInfo.jk_jrfd = "0.00";
                }
                if(data.has("今日有功")){
                    mJKInfo.jk_jryg = df.format(Double.valueOf(data.optString("今日有功")));
                }else{
                    mJKInfo.jk_jryg = "0.00";
                }
                if(data.has("累计发电")){
                    mJKInfo.jk_ljfd = df.format(Double.valueOf(data.optString("累计发电")));
                    mJKInfo.jk_pie_data.put("已发电量",mJKInfo.jk_ljfd);
                }else{
                    mJKInfo.jk_pie_data.put("已发电量","0.00");
                }
                if(data.has("计划发电")){
                    mJKInfo.jk_jhfd = df.format(Double.valueOf(data.optString("计划发电")));
                    float syfd = Float.parseFloat(mJKInfo.jk_jhfd) - Float.parseFloat(mJKInfo.jk_ljfd);
                    if(syfd < 0){
                        mJKInfo.jk_pie_data.put("待发电量","0.00");
                    }else{
                        mJKInfo.jk_pie_data.put("待发电量",df.format(syfd));
                    }

                }else{
                    mJKInfo.jk_pie_data.put("待发电量","0.00");
                }
                if(data.has("日发电量")){
                    JSONArray rfdl = data.getJSONArray("日发电量");
                    if(rfdl.length() == 1){
                        JSONObject rfdl_detail = rfdl.getJSONObject(0);
                        String[] tmp = ranges.split(",");
                        for(int i=0;i<tmp.length;i++){
                            if(rfdl_detail.has(tmp[i])){
                                mJKInfo.jk_rfdl.put(tmp[i],rfdl_detail.optString(tmp[i]));
                            }else{
                                mJKInfo.jk_rfdl.put(tmp[i],"0.00");
                            }
                        }
                    }
                }
                setData(mJKInfo);
            }else{
                Toast.makeText(mContext, "获取用户信息失败1", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "获取用户信息失败2", Toast.LENGTH_SHORT).show();
        }
    }

//    private void showProgress(final boolean show) {
//        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//    }
}
