package zskj.jkxt.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import zskj.jkxt.R;
import zskj.jkxt.WebService;
import zskj.jkxt.domain.Fan;
import zskj.jkxt.domain.Station;
import zskj.jkxt.ui.widget.StationDetailPopu;

public class MonitorDetailActivity extends Activity {

    //data
    Station model; //接收参数
    //view
    ImageView back, refresh;
    TextView station_address, station_type;
    TextView tv_plan_title, tv_plan;
    TextView tv_speed_title, tv_speed;
    TextView tv_power_title, tv_power;
    TextView tv_electricity_title, tv_electricity;

    //grid
    GridView monitor_detail; //风机信息列表

    ArrayList<Fan> fanList = new ArrayList<>();
    MonitorAdapter monitorAdapter;
    DecimalFormat df = new DecimalFormat("0.0");  //数据格式转换，四舍五入，保留一位小数
    MonitorDetailTask mTask;
    RotateAnimation rotateAnimation;

    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_detail);
        //接收传入的参数
        model = (Station) getIntent().getSerializableExtra("model");
        Log.e("接收到的参数，model----->",model.columnAddress + "  " + model.columnValue + "    " + model.columnName);
        initView();
        initData();
        refresh.performClick();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        refresh = (ImageView) this.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDetail(model);
            }
        });
        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setDuration(2000);

        //设置页面
        station_address = (TextView) this.findViewById(R.id.station_address);
        station_type = (TextView) this.findViewById(R.id.station_type);

        tv_plan = (TextView) this.findViewById(R.id.tv_plan); //省调计划
        tv_speed = (TextView) this.findViewById(R.id.tv_speed); //风速
        tv_power = (TextView) this.findViewById(R.id.tv_power); //瞬时总有功
        tv_electricity = (TextView) this.findViewById(R.id.tv_electricity);  //当日发电量
        tv_plan_title = (TextView) this.findViewById(R.id.tv_plan_title); //省调计划
        tv_speed_title = (TextView) this.findViewById(R.id.tv_speed_title); //风速
        tv_power_title = (TextView) this.findViewById(R.id.tv_power_title); //瞬时总有功
        tv_electricity_title = (TextView) this.findViewById(R.id.tv_electricity_title);  //当日发电量

        if (model.columnName.contains("光伏")) {
            flag = 1;
        }

        monitor_detail = (GridView) this.findViewById(R.id.monitor_detail);
        monitor_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fan fan = fanList.get(position);
                showDetailPopu(fan);
            }
        });
    }

    SpannableString msp = null;

    private void initData() {
        station_address.setText(model.columnAddress.toString());
        station_type.setText(model.columnName.toString().concat(getResources().getString(R.string.monitor_page)));
        SpannableString plan = new SpannableString(getResources().getString(R.string.title_plan));
        plan.setSpan(new ForegroundColorSpan(Color.GRAY), 4, plan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_plan_title.setText(plan);
        if (model.columnName.contains("光伏")) {
            msp = new SpannableString(getResources().getString(R.string.title_radiation));
            msp.setSpan(new SuperscriptSpan(), 9, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);   //上标
            SpannableString speed = new SpannableString(msp);
            speed.setSpan(new ForegroundColorSpan(Color.GRAY), 4, speed.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_speed_title.setText(speed);
        } else {
            SpannableString speed = new SpannableString(getResources().getString(R.string.title_speed));
            speed.setSpan(new ForegroundColorSpan(Color.GRAY), 4, speed.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_speed_title.setText(speed);
        }
        SpannableString power = new SpannableString(getResources().getString(R.string.title_power));
        power.setSpan(new ForegroundColorSpan(Color.GRAY), 5, power.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_power_title.setText(power);
        SpannableString elec = new SpannableString(getResources().getString(R.string.title_electricity));
        elec.setSpan(new ForegroundColorSpan(Color.GRAY), 5, elec.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_electricity_title.setText(elec);

        monitorAdapter = new MonitorAdapter(model);
        monitor_detail.setAdapter(monitorAdapter);
    }


    /**
     * 获取风机详情
     *
     * @param model
     */
    private void getDetail(Station model) {
        if (Station.isEmpty(model))
            return;
        if (mTask != null) {//不为null 说明操作正在进行，规避多次点击登录按钮操作
            Toast.makeText(getApplicationContext(), "加载中，请稍候...", Toast.LENGTH_SHORT).show();
            return;
        }
        mTask = new MonitorDetailTask(model);
        mTask.execute();
    }

    /**
     * 风机详情任务task
     */
    private class MonitorDetailTask extends AsyncTask<Void, Void, String> {

        Station model = null;

        MonitorDetailTask(Station model) {
            this.model = model;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress(true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return WebService.getInstance().getStationInfo(model.columnValue);
        }

        @Override
        protected void onPostExecute(String result) {
            mTask = null;
            progress(false);
            dealResult(result, model.columnName);
        }

        @Override
        protected void onCancelled() {
            mTask = null;
            progress(false);
        }
    }

    /**
     * 数据解析与界面刷新
     *
     * @param result
     */
    private void dealResult(String result, String colName) {

        try {
            if(result != null && result.toString() != null){
                JSONObject obj = new JSONObject(result);
                //Log.e("getStationInfo------>",result);
                int code = obj.optInt("code");
                if (code == 0) {
                    String msg = obj.optString("msg");
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject data = obj.optJSONObject("data");
                if (data == null)
                    return;
//            tv_plan.setText(df.format(Double.valueOf(data.optString("省调计划"))));
                if(data.has("省调计划")){
                    tv_plan.setText(df.format(Double.valueOf(data.optString("省调计划"))));
                }else{
                    tv_plan.setText("0.0");
                }
                if (colName.contains("光伏")) {
//                tv_speed.setText(df.format(Double.valueOf(data.optString("总辐射瞬时值"))));
                    if(data.has("总辐射瞬时值")){
                        tv_speed.setText(df.format(Double.valueOf(data.optString("总辐射瞬时值"))));
                    }else{
                        tv_speed.setText("0.0");
                    }
                } else {
//                tv_speed.setText(df.format(Double.valueOf(data.optString("平均风速"))));
                    if(data.has("平均风速")){
                        tv_speed.setText(df.format(Double.valueOf(data.optString("平均风速"))));
                    }else{
                        tv_speed.setText("0.0");
                    }
                }
//            tv_power.setText(df.format(Double.valueOf(data.optString("瞬时总有功"))));
//            tv_electricity.setText(df.format(Double.valueOf(data.optString("当日发电量"))));
                if(data.has("瞬时总有功")){
                    tv_power.setText(df.format(Double.valueOf(data.optString("瞬时总有功"))));
                }else{
                    tv_power.setText("0.0");
                }
                if(data.has("当日发电量")){
                    tv_electricity.setText(df.format(Double.valueOf(data.optString("当日发电量"))));
                }else{
                    tv_electricity.setText("0.0");
                }

//            Log.e("当日发电量---->",df.format(Double.valueOf(data.optString("当日发电量"))));
                JSONArray list = data.optJSONArray("list");
                if (list != null && list.length() > 0) {
                    fanList.clear();
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject object = list.optJSONObject(i);
                        Fan fan = new Fan();
                        fan.fan_number = object.optString("编号");
//                    Log.e("fan  fan_number------>",fan.fan_number);
                        if (colName.contains("光伏")) {
                            if(object.has("逆变器日发电量")){
                                fan.fan_speed = df.format(Double.valueOf(object.optString("逆变器日发电量")));
                            }else{
                                fan.fan_speed = "0.0";
                            }
                            if(object.has("逆变器效率")){
                                fan.fan_revs = df.format(Double.valueOf(object.optString("逆变器效率")));
                            }else{
                                fan.fan_revs = "0.0";
                            }
                            if(object.has("逆变器状态")){
                                fan.fan_state = df.format(Double.valueOf(object.optString("逆变器状态")));
                            }else{
                                fan.fan_state = "0.0";
                            }
                            if(object.has("有功功率")){
                                fan.fan_active_power = df.format(Double.valueOf(object.optString("有功功率")));
                            }else{
                                fan.fan_active_power = "0.0";
                            }
                        } else {
                            if(object.has("风速")){
                                fan.fan_speed = df.format(Double.valueOf(object.optString("风速")));
                            }else{
                                fan.fan_speed = "0.0";
                            }
                            if(object.has("转速")){
                                fan.fan_revs = df.format(Double.valueOf(object.optString("转速")));
                            }else{
                                fan.fan_revs = "0.0";
                            }
                            if(object.has("风机运行状态")){
                                fan.fan_state = df.format(Double.valueOf(object.optString("风机运行状态")));
                            }else{
                                fan.fan_state = "0.0";
                            }
                            if(object.has("有功功率")){
                                fan.fan_active_power = df.format(Double.valueOf(object.optString("有功功率")));
                            }else{
                                fan.fan_active_power = "0.0";
                            }
//                        fan.fan_speed = df.format(Double.valueOf(object.optString("风速")));
//                        fan.fan_revs = df.format(Double.valueOf(object.optString("转速")));
//                        fan.fan_state = df.format(Double.valueOf(object.optString("风机运行状态")));
//                        fan.fan_active_power = df.format(Double.valueOf(object.optString("有功功率")));
                        }

                        fanList.add(fan);
                    }
                }
                Log.e("fan  nums------>",fanList.size()+"");
                Collections.sort(fanList, new Comparator<Fan>() {
                    @Override
                    public int compare(Fan fan, Fan t1) {
                        String fan_number = fan.fan_number;
                        String fan_number1 = t1.fan_number;
                        int flag = fan_number.compareTo(fan_number1) > 0 ? 1 : 0;
                        return flag;
                    }
                });
                monitorAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(this, "查询电场信息失败1", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "查询电场信息失败2", Toast.LENGTH_SHORT).show();
        }


    }

    class MonitorAdapter extends BaseAdapter {

        Station mStation = null;

        MonitorAdapter(Station mStation) {
            this.mStation = mStation;
        }

        @Override
        public int getCount() {
            return fanList.size();
        }

        @Override
        public Object getItem(int position) {
            return fanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(getApplicationContext(), R.layout.item_monitor_detail, null);
                holder.fan_picture = (ImageView) convertView.findViewById(R.id.fan_picture);
                holder.fan_number = (TextView) convertView.findViewById(R.id.fan_number);
//                holder.fan_state = (TextView) convertView.findViewById(R.id.fan_state);
                holder.fan_speed = (TextView) convertView.findViewById(R.id.fan_speed);
                holder.fan_active_power = (TextView) convertView.findViewById(R.id.fan_active_power);
                holder.fan_revs = (TextView) convertView.findViewById(R.id.fan_revs);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Fan model = fanList.get(position);
            holder.fan_number.setText(model.fan_number);

            if (mStation.columnName.contains("光伏")) {
                holder.fan_speed.setText(getResources().getString(R.string.electricity_unit, model.fan_speed));
                holder.fan_active_power.setText(getResources().getString(R.string.active_power_unit, model.fan_active_power));
                holder.fan_revs.setText(getResources().getString(R.string.efficiency_unit, model.fan_revs));
                if (Double.valueOf(model.fan_state.trim()) == 1) { //发电
                    holder.fan_picture.setImageResource(R.drawable.gf2);
//                    holder.fan_state.setText(getResources().getString(R.string.stop));
                }else {
                    holder.fan_picture.setImageResource(R.drawable.gf3);
//                    holder.fan_state.setText(getResources().getString(R.string.run));
                }
//                holder.fan_state.setVisibility(View.INVISIBLE);
            } else {
                holder.fan_speed.setText(getResources().getString(R.string.speed_unit, model.fan_speed));
                holder.fan_active_power.setText(getResources().getString(R.string.active_power_unit, model.fan_active_power));
                holder.fan_revs.setText(getResources().getString(R.string.revs_unit, model.fan_revs));
                if (Double.valueOf(model.fan_state.trim()) == 2) { //发电
//                    Glide.with(MonitorDetailActivity.this).load(R.drawable.fj_032).asGif().dontAnimate()
//                            .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.fan_picture);
                    holder.fan_picture.setImageResource(R.drawable.fj2);
//                    holder.fan_state.setText(getResources().getString(R.string.run));
                }else {
                    holder.fan_picture.setImageResource(R.drawable.fj3);
//                    holder.fan_state.setText(getResources().getString(R.string.stop));
                }
//                holder.fan_state.setVisibility(View.INVISIBLE);
            }
//            if (Double.valueOf(model.fan_state.trim()) >= 5) { //停机
//                if (mStation.columnName.contains("光伏")) {
//                    // TDTD
////                    Glide.with(MonitorDetailActivity.this).load(R.drawable.gf2_2).asGif().into(holder.fan_picture);
//                    holder.fan_picture.setImageResource(R.drawable.gf2_2);
//                } else {
//                    holder.fan_picture.setImageResource(R.drawable.fj_05);
//                }
//                holder.fan_state.setText(getResources().getString(R.string.stop));
//                holder.fan_state.setVisibility(View.VISIBLE);
//            } else {
//                if (mStation.columnName.contains("光伏")) {
//                    holder.fan_picture.setImageResource(R.drawable.gf2_1);
//                } else {
////                    holder.fan_picture.setImageResource(R.drawable.fj_04);
//                    Glide.with(MonitorDetailActivity.this).load(R.drawable.fj_032).asGif().into(holder.fan_picture);
//                }
//                holder.fan_state.setText("");
//                holder.fan_state.setVisibility(View.INVISIBLE);
//            }
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView fan_picture;
        TextView fan_number;
//        TextView fan_state;
        TextView fan_speed;
        TextView fan_active_power;
        TextView fan_revs;
    }


    private void progress(boolean pro) {
        if (pro) {
            refresh.startAnimation(rotateAnimation);
        } else {
            rotateAnimation.cancel();
            refresh.clearAnimation();
        }
    }

    StationDetailPopu mPopu;

    private void showDetailPopu(Fan fan) {
        if (mPopu == null)
            mPopu = new StationDetailPopu(this);

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        mPopu.setWidth((int) (d.getWidth() * 0.65));

        backgroundAlpha(0.5f);
        mPopu.setFan(fan, flag);
        mPopu.showAtLocation(getWindow().getDecorView().findViewById(android.R.id.content), Gravity.CENTER_VERTICAL, 0, 0);

        mPopu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    //设置背景透明度，1f为不透明
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}
