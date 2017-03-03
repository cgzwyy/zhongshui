package zskj.jkxt.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import zskj.jkxt.R;
import zskj.jkxt.domain.Fan;
import zskj.jkxt.domain.Station;
import zskj.jkxt.util.Sort;

public class MonitorDetailActivity extends AppCompatActivity {

    Station model; //接收参数
    TextView station_address,station_type;
    TextView plan,wind_speed,total_active_power,daily_electricity;
    GridView monitor_detail; //风机信息列表
    Map<Integer,Fan> map = new HashMap<Integer, Fan>();
    Button back,refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_detail);
        //接收传入的参数
        model = (Station) getIntent().getSerializableExtra("model");
        //设置页面
        station_address = (TextView) this.findViewById(R.id.station_address);
        station_type = (TextView) this.findViewById(R.id.station_type);

        station_address.setText(model.columnAddress.toString());
        station_type.setText(model.columnName.toString().concat(getResources().getString(R.string.monitor_page)));

        plan = (TextView) this.findViewById(R.id.plan); //省调计划
        wind_speed = (TextView) this.findViewById(R.id.wind_speed); //风速
        total_active_power = (TextView) this.findViewById(R.id.total_active_power); //瞬时总有功
        daily_electricity = (TextView) this.findViewById(R.id.daily_electricity);  //当日发电量
        //获取场站相关信息
        getStationData();

        monitor_detail = (GridView) this.findViewById(R.id.monitor_detail);
        setMonitorDetail();

        back = (Button) this.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refresh = (Button) this.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStationData();
                setMonitorDetail();
            }
        });

    }

    public void getStationData(){
        String data = "{\"场站名称\":\"布尔津\",\"省调计划\":\"6\",\"平均风速\":\"3.2343242342\",\"当日发电量\":\"2.178323\",\"瞬时总有功\":\"1.17\"}";

        JsonParser parser = new JsonParser();
        JsonObject object=(JsonObject) parser.parse(data);

        DecimalFormat df = new DecimalFormat("#.00");

        plan.setText(df.format(Double.valueOf(object.get("省调计划").getAsString())));
        wind_speed.setText(df.format(Double.valueOf(object.get("平均风速").getAsString())));
        total_active_power.setText(df.format(Double.valueOf(object.get("瞬时总有功").getAsString())));
        daily_electricity.setText(df.format(Double.valueOf(object.get("当日发电量").getAsString())));
    }

    public void setMonitorDetail(){
        map = getFansInfo();
        monitor_detail.setAdapter(new MyMonitorAdapter(map));
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //隐藏dialog的title
        monitor_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.setContentView(R.layout.fan_detail_dialog);

                Window dialogWindow = dialog.getWindow();
                dialogWindow.setGravity(Gravity.CENTER);

                WindowManager m = getWindowManager();
                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
                WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
                dialogWindow.setAttributes(p);

                TextView fan_detail_speed = (TextView) dialog.findViewById(R.id.fan_detail_speed);
                TextView fan_detail_power = (TextView) dialog.findViewById(R.id.fan_detail_power);
                TextView fan_detail_revs = (TextView) dialog.findViewById(R.id.fan_detail_revs);

                Fan fan = map.get(position);
                fan_detail_speed.setText(fan.fan_speed.concat(getResources().getString(R.string.speed_unit)));
                fan_detail_power.setText(fan.fan_active_power.concat(getResources().getString(R.string.active_power_unit)));
                fan_detail_revs.setText(fan.fan_revs.concat(getResources().getString(R.string.revs_unit)));

                dialog.show();
            }
        });
    }

    public Map<Integer,Fan> getFansInfo(){
        String data = "{\"list\":[{\"编号\":\"#01\",\"风机运行状态\":\"1\",\"风速\":\"4.20\",\"有功功率\":\"-17.7000001\",\"转速\":\"1102\"}," +
                "{\"编号\":\"#03\",\"风机运行状态\":\"2\",\"风速\":\"4.2\",\"有功功率\":\"17.7000001\",\"转速\":\"1102.6\"}," +
                "{\"编号\":\"#02\",\"风机运行状态\":\"9\",\"风速\":\"4.20\",\"有功功率\":\"-17.7000001\",\"转速\":\"1102\"}," +
                "{\"编号\":\"#05\",\"风机运行状态\":\"3\",\"风速\":\"4.20\",\"有功功率\":\"-17.7000001\",\"转速\":\"11.7557567\"}," +
                "{\"编号\":\"#04\",\"风机运行状态\":\"5\",\"风速\":\"4.20\",\"有功功率\":\"-17.7000001\",\"转速\":\"1102\"}," +
                "{\"编号\":\"#06\",\"风机运行状态\":\"7\",\"风速\":\"4.20\",\"有功功率\":\"-17.7000001\",\"转速\":\"1102\"}," +
                "{\"编号\":\"#08\",\"风机运行状态\":\"2\",\"风速\":\"4.2\",\"有功功率\":\"17.7000001\",\"转速\":\"1102.6\"}," +
                "{\"编号\":\"#09\",\"风机运行状态\":\"9\",\"风速\":\"4.20\",\"有功功率\":\"-17.7000001\",\"转速\":\"1102\"}," +
                "{\"编号\":\"#10\",\"风机运行状态\":\"3\",\"风速\":\"4.20\",\"有功功率\":\"-17.7000001\",\"转速\":\"11.7557567\"}," +
                "{\"编号\":\"#13\",\"风机运行状态\":\"5\",\"风速\":\"4.20\",\"有功功率\":\"-17.7000001\",\"转速\":\"1102\"}," +
                "{\"编号\":\"#15\",\"风机运行状态\":\"7\",\"风速\":\"4.20\",\"有功功率\":\"-17.7000001\",\"转速\":\"1102\"}," +
                "{\"编号\":\"#07\",\"风机运行状态\":\"5\",\"风速\":\"4.20\",\"有功功率\":\"-17.7000001\",\"转速\":\"1102\"}]}";

        JsonParser parser = new JsonParser();//创建JSON解析器
        JsonObject object=(JsonObject) parser.parse(data); //创建JsonObject对象
        JsonArray array=object.get("list").getAsJsonArray(); //得到为json的数组

        DecimalFormat df = new DecimalFormat("#.00");  //数据格式转换，四舍五入，保留两位小数

        Fan[] fans = new Fan[array.size()];

        for(int i=0;i<array.size();i++){
            JsonObject subObject=array.get(i).getAsJsonObject();
            Fan fan = new Fan();
            fan.fan_number = subObject.get("编号").getAsString();
            fan.fan_speed = df.format(Double.valueOf(subObject.get("风速").getAsString()));
            fan.fan_active_power = df.format(Double.valueOf(subObject.get("有功功率").getAsString()));
            fan.fan_revs = df.format(Double.valueOf(subObject.get("转速").getAsString()));
            fan.fan_state = subObject.get("风机运行状态").getAsString();
            Log.e("result","--------------------->"+fan.fan_number+"--------"+fan.fan_speed+"-----------"
                    +fan.fan_state+"-----------"+fan.fan_active_power+"--------"+fan.fan_revs+"-------------"+Integer.valueOf(fan.fan_number.substring(1,fan.fan_number.length())));
//            map.put(Integer.valueOf(fan.fan_number.substring(1,fan.fan_number.length()))-1,fan);
            fans[i] = fan;
        }

        return Sort.fanSort(fans);
    }

    class MyMonitorAdapter extends BaseAdapter{
        Map<Integer,Fan> map;

        public MyMonitorAdapter(Map<Integer,Fan> map) {
            super();
            this.map = map;
            if (map == null)
                map = new HashMap<Integer, Fan>();
        }

        @Override
        public int getCount() {
            return map.size();
        }

        @Override
        public Object getItem(int position) {
            return map.get(position);
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

            final Fan model = map.get(position);
            Log.e("result","------------"+position);
            holder.fan_number.setText(model.fan_number);
            holder.fan_speed.setText(model.fan_speed.concat(getResources().getString(R.string.speed_unit)));
            holder.fan_active_power.setText(model.fan_active_power.concat(getResources().getString(R.string.active_power_unit)));
            holder.fan_revs.setText(model.fan_revs.concat(getResources().getString(R.string.revs_unit)));
            if(Integer.valueOf(model.fan_state) >= 5){ //停机
                holder.fan_picture.setImageResource(R.drawable.fj_05);
                holder.fan_state.setText(getResources().getString(R.string.stop));
                holder.fan_state.setVisibility(View.VISIBLE);
            }else{
                holder.fan_picture.setImageResource(R.drawable.fj_04);
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
}
