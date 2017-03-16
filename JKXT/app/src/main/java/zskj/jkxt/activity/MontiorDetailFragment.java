package zskj.jkxt.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import zskj.jkxt.R;
import zskj.jkxt.api.RequestCallback;
import zskj.jkxt.api.WebService;
import zskj.jkxt.domain.Fan;
import zskj.jkxt.domain.Station;
import zskj.jkxt.util.Sort;

/**
 * Created by WYY on 2017/3/15.
 */

public class MontiorDetailFragment extends Fragment {

    Station model; //接收参数
    TextView station_address,station_type;
    TextView plan,wind_speed,total_active_power,daily_electricity;
    GridView monitor_detail; //风机信息列表
    Map<Integer,Fan> map = new HashMap<Integer, Fan>();
    Button back,refresh;

    ProgressDialog dialog = null;   //进度对话框

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_monitor_detail, container, false);

        //接收传入的参数
        model = (Station) getArguments().getSerializable("model");
        //设置页面
        station_address = (TextView) view.findViewById(R.id.station_address);
        station_type = (TextView) view.findViewById(R.id.station_type);

        station_address.setText(model.columnAddress.toString());
        station_type.setText(model.columnName.toString().concat(getResources().getString(R.string.monitor_page)));

        plan = (TextView) view.findViewById(R.id.plan); //省调计划
        wind_speed = (TextView) view.findViewById(R.id.wind_speed); //风速
        total_active_power = (TextView) view.findViewById(R.id.total_active_power); //瞬时总有功
        daily_electricity = (TextView) view.findViewById(R.id.daily_electricity);  //当日发电量

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("加载中...");     //设置提示信息
        dialog.setCanceledOnTouchOutside(false);   //设置在点击Dialog外是否取消Dialog进度条

        monitor_detail = (GridView) view.findViewById(R.id.monitor_detail);
        getData(model.columnValue);
//        getData("qh");

        back = (Button) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                FragmentManager manager = getFragmentManager();
                manager.popBackStack();
            }
        });

        refresh = (Button) view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(model.columnValue);
            }
        });

        return view;
    }

    public void setMonitorDetail(String result){
        map = getFansInfo(result);
        if(map.isEmpty()){ //map为空，未获取到数据
            Toast.makeText(getActivity(), "获取数据失败:" + result, Toast.LENGTH_SHORT).show();
            return;
        }
        monitor_detail.setAdapter(new MyMonitorAdapter(getActivity(),map));

        final Dialog little_dialog = new Dialog(getActivity());
        little_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //隐藏dialog的title
        little_dialog.setCanceledOnTouchOutside(true);
        monitor_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                little_dialog.setContentView(R.layout.fan_detail_dialog);

                Window dialogWindow = little_dialog.getWindow();
                dialogWindow.setGravity(Gravity.CENTER);

                WindowManager m = getActivity().getWindowManager();
                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
                WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
                dialogWindow.setAttributes(p);

                TextView fan_detail_speed = (TextView) little_dialog.findViewById(R.id.fan_detail_speed);
                TextView fan_detail_power = (TextView) little_dialog.findViewById(R.id.fan_detail_power);
                TextView fan_detail_revs = (TextView) little_dialog.findViewById(R.id.fan_detail_revs);

                Fan fan = map.get(position);
                fan_detail_speed.setText(fan.fan_speed.concat(getResources().getString(R.string.speed_unit)));
                fan_detail_power.setText(fan.fan_active_power.concat(getResources().getString(R.string.active_power_unit)));
                fan_detail_revs.setText(fan.fan_revs.concat(getResources().getString(R.string.revs_unit)));

                little_dialog.show();
            }
        });
    }

    private void getData(final String Str_StationCode ){
        dialog.show();   //显示进度条对话框
        new Thread() {
            public void run(){
                WebService.getInstance().GetStationInfo(Str_StationCode, new RequestCallback() {

                    @Override
                    public void onSuccess(final String result) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();  //删除该进度条
                                setMonitorDetail(result);
                            }
                        });
                    }

                    @Override
                    public void onFail(final String errorMsg) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();  //删除该进度条
                                Log.e("fail--" , "fail");
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
    }

    public Map<Integer,Fan> getFansInfo(String result){

        if(!result.contains("{")){
            map.clear();
            return map;
        }

        JsonParser parser = new JsonParser();//创建JSON解析器
        JsonObject object=(JsonObject) parser.parse(result); //创建JsonObject对象
        JsonArray array=object.get("list").getAsJsonArray(); //得到为json的数组

        DecimalFormat df = new DecimalFormat("0.00");  //数据格式转换，四舍五入，保留两位小数

        plan.setText(df.format(Double.valueOf(object.get("省调计划").getAsString())));
        wind_speed.setText(df.format(Double.valueOf(object.get("总辐射瞬时值").getAsString())));
        total_active_power.setText(df.format(Double.valueOf(object.get("瞬时总有功").getAsString())));
        daily_electricity.setText(df.format(Double.valueOf(object.get("当日发电量").getAsString())));

        Fan[] fans = new Fan[array.size()];

        for(int i=0;i<array.size();i++){
            JsonObject subObject=array.get(i).getAsJsonObject();
            Fan fan = new Fan();
            fan.fan_number = subObject.get("编号").getAsString();
            fan.fan_speed = df.format(Double.valueOf(subObject.get("风速").getAsString()));
            fan.fan_active_power = df.format(Double.valueOf(subObject.get("有功功率").getAsString()));
            fan.fan_revs = df.format(Double.valueOf(subObject.get("转速").getAsString()));
            fan.fan_state = df.format(Double.valueOf(subObject.get("风机运行状态").getAsString()));
//            Log.e("result","--------------------->"+fan.fan_number+"--------"+fan.fan_speed+"-----------"
//                    +fan.fan_state+"-----------"+fan.fan_active_power+"--------"+fan.fan_revs+"-------------"+Integer.valueOf(fan.fan_number.substring(1,fan.fan_number.length())));
//            map.put(Integer.valueOf(fan.fan_number.substring(1,fan.fan_number.length()))-1,fan);
            fans[i] = fan;
        }

        Log.e("fans  size---",fans.length+"");

        return Sort.fanSort(fans);
    }

    class MyMonitorAdapter extends BaseAdapter {
        private Context context;
        Map<Integer,Fan> map;

        public MyMonitorAdapter(Context context,Map<Integer,Fan> map) {
            super();
            this.context = context;
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
                convertView = View.inflate(context, R.layout.item_monitor_detail, null);
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
            Log.e("result position","------------"+position);
            holder.fan_number.setText(model.fan_number);
            holder.fan_speed.setText(model.fan_speed.concat(getResources().getString(R.string.speed_unit)));
            holder.fan_active_power.setText(model.fan_active_power.concat(getResources().getString(R.string.active_power_unit)));
            holder.fan_revs.setText(model.fan_revs.concat(getResources().getString(R.string.revs_unit)));
            if(Double.valueOf(model.fan_state.trim()) >= 5){ //停机
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