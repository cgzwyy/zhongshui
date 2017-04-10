package zskj.jkxt.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.SuperscriptSpan;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import zskj.jkxt.R;
import zskj.jkxt.api.WebService;
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
    DecimalFormat df = new DecimalFormat("0.00");  //数据格式转换，四舍五入，保留两位小数
    MonitorDetailTask mTask;
    RotateAnimation rotateAnimation;

    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_detail);
        //接收传入的参数
        model = (Station) getIntent().getSerializableExtra("model");
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
            return WebService.getInstance().GetStationInfo(model.columnValue);
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
        if (!result.contains("{")) {//非法数据
//            testData();
//            monitorAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            return;
        }
        JsonParser parser = new JsonParser();//创建JSON解析器
        JsonObject object = (JsonObject) parser.parse(result); //创建JsonObject对象

        tv_plan.setText(df.format(Double.valueOf(object.get("省调计划").getAsString())));
        if (colName.contains("光伏")) {
            tv_speed.setText(df.format(Double.valueOf(object.get("总辐射瞬时值").getAsString())));
        } else {
            tv_speed.setText(df.format(Double.valueOf(object.get("平均风速").getAsString())));
        }
        tv_power.setText(df.format(Double.valueOf(object.get("瞬时总有功").getAsString())));
        tv_electricity.setText(df.format(Double.valueOf(object.get("当日发电量").getAsString())));

        JsonArray array = object.get("list").getAsJsonArray(); //得到为json的数组

        if (array != null && array.size() > 0) {
            fanList.clear();
            for (int i = 0; i < array.size(); i++) {
                JsonObject subObject = array.get(i).getAsJsonObject();
                Fan fan = new Fan();
                fan.fan_number = subObject.get("编号").getAsString();
                if (colName.contains("光伏")) {
                    fan.fan_speed = df.format(Double.valueOf(subObject.get("逆变器效率").getAsString()));
                    fan.fan_revs = df.format(Double.valueOf(subObject.get("逆变器日发电量").getAsString()));
                    fan.fan_state = df.format(Double.valueOf(subObject.get("逆变器状态").getAsString()));
                } else {
                    fan.fan_speed = df.format(Double.valueOf(subObject.get("风速").getAsString()));
                    fan.fan_revs = df.format(Double.valueOf(subObject.get("转速").getAsString()));
                    fan.fan_state = df.format(Double.valueOf(subObject.get("风机运行状态").getAsString()));
                }
                fan.fan_active_power = df.format(Double.valueOf(subObject.get("有功功率").getAsString()));
                fanList.add(fan);
            }
        }
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
                holder.fan_state = (TextView) convertView.findViewById(R.id.fan_state);
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
            } else {
                holder.fan_speed.setText(getResources().getString(R.string.speed_unit, model.fan_speed));
                holder.fan_active_power.setText(getResources().getString(R.string.active_power_unit, model.fan_active_power));
                holder.fan_revs.setText(getResources().getString(R.string.revs_unit, model.fan_revs));
            }
            if (Double.valueOf(model.fan_state.trim()) >= 5) { //停机
                if (mStation.columnName.contains("光伏")) {
                    holder.fan_picture.setImageResource(R.drawable.gf2_2);
                } else {
                    holder.fan_picture.setImageResource(R.drawable.fj_05);
                }
                holder.fan_state.setText(getResources().getString(R.string.stop));
                holder.fan_state.setVisibility(View.VISIBLE);
            } else {
                if (mStation.columnName.contains("光伏")) {
                    holder.fan_picture.setImageResource(R.drawable.gf2_1);
                } else {
                    holder.fan_picture.setImageResource(R.drawable.fj_04);
                }
                holder.fan_state.setText("");
                holder.fan_state.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView fan_picture;
        TextView fan_number;
        TextView fan_state;
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

    //TODO delete
    private void testData() {
        fanList.clear();
        for (int i = 0; i < 10; i++) {
            Fan fan = new Fan();
            fan.fan_number = "编号：" + i;
            fan.fan_speed = "123.33";
            fan.fan_active_power = "123.44";
            fan.fan_revs = "123.55";
            fan.fan_state = "123.66";
            fanList.add(fan);
        }
    }
}
