package zskj.jkxt.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zskj.jkxt.R;
import zskj.jkxt.domain.AlarmData;

/**
 * Created by WYY on 2017/3/14.
 */

public class AddressFragment extends Fragment {

    ProgressDialog dialog = null;   //进度对话框
    ListView lv_alarm;
    String last_time = null; //取得的最后一条报警数据的时间
    int num = 0; //取得报警数据条数
    Map<Integer,AlarmData> map = new HashMap<>();
    int lastNum = 0; //更新前取得的报警数据条数
    String sdate = null; //报警数据日期
    int tmp = 0; //临时变量
    Button first_page,previous_page,next_page,last_page;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_alarm_test, container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("加载中...");     //设置提示信息
        dialog.setCanceledOnTouchOutside(false);   //设置在点击Dialog外是否取消Dialog进度条

        first_page = (Button) view.findViewById(R.id.first_page);
        previous_page = (Button) view.findViewById(R.id.previous_page);
        next_page = (Button) view.findViewById(R.id.next_page);
        last_page = (Button) view.findViewById(R.id.last_page);

        lv_alarm = (ListView) view.findViewById(R.id.lv_alarm);
        num = 0;
        last_time = "0";

        final Calendar ca = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        sdate = sdf.format(ca.getTime());

        sdate = "20170317";
        GetData(sdate,last_time);

        handler.postDelayed(runnable, 1000 * 10);

        //首页
        first_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num>10){ //总数据条数多于10条
                    tmpAdapter = (mAdapter) lv_alarm.getAdapter();
                    List<AlarmData> data = tmpAdapter.getData();
                    data.clear();
                    for(int i=0;i<10;i++){
                        data.add(map.get(i));
                    }
                    tmpAdapter.notifyDataSetChanged();
                }
            }
        });

        //上一页
        previous_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num > 10){
                    tmpAdapter = (mAdapter) lv_alarm.getAdapter();
                    int min = tmpAdapter.getMinNum(); //当前页面最小编号
                    if(min > 0){  //不是首页
                        List<AlarmData> data = tmpAdapter.getData();
                        data.clear();

                        for(int i=min-10;i<min;i++){
                            data.add(map.get(i));
                        }
                        tmpAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        //下一页
        next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmpAdapter = (mAdapter) lv_alarm.getAdapter();
                int min = tmpAdapter.getMinNum(); //当前页面最小编号
                if(num - min > 10){
                    List<AlarmData> data = tmpAdapter.getData();
                    data.clear();

                    for(int i=min+10;i<min+20 && i<num;i++){
                        data.add(map.get(i));
                    }
                    tmpAdapter.notifyDataSetChanged();
                }
            }
        });

        //尾页
        last_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmpAdapter = (mAdapter) lv_alarm.getAdapter();
                int min = tmpAdapter.getMinNum(); //当前页面最小编号
                if(num - min > 10){
                    List<AlarmData> data = tmpAdapter.getData();
                    data.clear();

                    for(int i=0;i<num%10;i++){
                        data.add(map.get(num/10*10+i));
                    }
                    tmpAdapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }

    private mAdapter tmpAdapter;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            while(Integer.valueOf(last_time) < Integer.valueOf("235959999") && tmp < 1 ){
                this.update();
                handler.postDelayed(this, 1000 * 60 * 10);// 间隔60 * 10秒
            }

        }
        void update() {
            Log.e("last_time---->", last_time + "");
            GetData("20170317",last_time);
            tmp++;
        }
    };

    private void GetDataTop10() {
        dialog.show();   //显示进度条对话框
        new Thread() {
            public void run(){
                WebService.getInstance().GetAlarmDataTop10(new RequestCallback() {

                    @Override
                    public void onSuccess(final String result) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<AlarmData> data = parserResult(result);
                                lv_alarm.setAdapter(new mAdapter(getActivity(),data));
                                dialog.dismiss();  //删除该进度条
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
    }
    private void GetData(final String sdate, final String stime) {
        dialog.show();   //显示进度条对话框
        new Thread() {
            public void run(){
                WebService.getInstance().GetAlarmData(sdate,stime, new RequestCallback() {

                    @Override
                    public void onSuccess(final String result) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setDataDetail(result);
//                                List<AlarmData> data = parserResult(result);
//                                lv_alarm.setAdapter(new mAdapter(getActivity(),data));
                                dialog.dismiss();  //删除该进度条
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
    }

    class mAdapter extends BaseAdapter {
        private int[] colors = new int[] { 0x30FF0000, 0x300000FF };
        private Context context;
        List<AlarmData> list;

        public mAdapter(Context context,List<AlarmData> list) {
            super();
            this.context = context;
            this.list = list;
            if (list == null)
                list = new ArrayList<AlarmData>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        public List<AlarmData> getData() {
            // TODO Auto-generated method stub
            return list;
        }
        public int getMinNum() {
            // TODO Auto-generated method stub
            return Integer.valueOf(list.get(0).alarm_num);
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.item_alarm, null);
                holder.alarm_num = (TextView) convertView.findViewById(R.id.alarm_num);
                holder.alarm_type = (TextView) convertView.findViewById(R.id.alarm_type);
                holder.alarm_time = (TextView) convertView.findViewById(R.id.alarm_time);
                holder.alarm_content = (TextView) convertView.findViewById(R.id.alarm_content);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            int colorPos = position % colors.length;
            if (colorPos == 1) {
                convertView.setBackgroundColor(getResources().getColor(R.color.paleblue));
            } else {
                convertView.setBackgroundColor(getResources().getColor(R.color.pale));
            }

            final AlarmData model = list.get(position);
            holder.alarm_num.setText(model.alarm_num);
            holder.alarm_type.setText(model.alarm_type);
//            holder.alarm_time.setText(model.alarm_date);
            SimpleDateFormat formatter = new SimpleDateFormat("HHmmssSSS");
            SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss:SSS");
            String formatStr = null;
            String s = model.alarm_time;
            String str ="000000000";
            try {
//                formatStr = formatter2.format(formatter.parse(String.format("%1$0"+(9-s.length())+"d",0)+s));

                formatStr = formatter2.format(formatter.parse(str.substring(0,9-s.length())+s));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.alarm_time.setText(formatStr);
            holder.alarm_content.setText(model.alarm_content);
            return convertView;
        }

    }

    static class ViewHolder {
        TextView alarm_num;
        TextView alarm_type;
        TextView alarm_time;
        TextView alarm_content;
    }

    private void setDataDetail(String result) {
        parserResults(result);
        if(lastNum == 0){
            List<AlarmData> data = new ArrayList<>();
            if(num > 0 && num%10 == 0){
                for(int i=0;i<10;i++){
                    data.add(map.get(num/10*10+i));
                }
            }else{
                for(int i=0;i<num%10;i++){
                    data.add(map.get(num/10*10+i));
                }
            }
            lv_alarm.setAdapter(new mAdapter(getActivity(),data));
        }else {
            tmpAdapter = (mAdapter) lv_alarm.getAdapter();
            List<AlarmData> data = tmpAdapter.getData();
            if(lastNum/10 == num/10){
                for(int i=lastNum;i<num;i++){
                    data.add(map.get(i));
                }
            }else{
                data.clear();
                for(int i=0;i<num%10;i++){
                    data.add(map.get(num/10*10+i));
                }
            }
            tmpAdapter.notifyDataSetChanged();
        }
    }
    private List<AlarmData> parserResult(String result) {
        List<AlarmData> list = new ArrayList<>();
        JsonParser parser = new JsonParser();//创建JSON解析器
        JsonObject object = (JsonObject) parser.parse(result); //创建JsonObject对象
        JsonArray alarms = object.get("AlarmData").getAsJsonArray(); //得到为json的数组

        for (int i = 0; i < alarms.size(); i++) {
            JsonObject subObject = alarms.get(i).getAsJsonObject();

            AlarmData alarmData = new AlarmData();
            alarmData.alarm_type = subObject.get("类型名").getAsString();
            alarmData.alarm_date = subObject.get("日期").getAsString();
            alarmData.alarm_time = subObject.get("时间").getAsString();
            alarmData.alarm_content = subObject.get("事项").getAsString();

            list.add(alarmData);
        }

        return list;
    }
    private void parserResults(String result) {
//        Map<Integer,AlarmData> map = new HashMap<>();
        if(map == null){
            map = new HashMap<>();
        }
        lastNum = map.size();
        if(!result.contains("{")){
            return;
        }else{
            JsonParser parser = new JsonParser();//创建JSON解析器
            JsonObject object = (JsonObject) parser.parse(result); //创建JsonObject对象
            JsonArray alarms = object.get("AlarmData").getAsJsonArray(); //得到为json的数组

            if(map == null){
                map = new HashMap<>();
            }
            lastNum = map.size();
            for (int i = 0; i < alarms.size(); i++) {
                JsonObject subObject = alarms.get(i).getAsJsonObject();

                AlarmData alarmData = new AlarmData();
                alarmData.alarm_type = subObject.get("类型名").getAsString();
                alarmData.alarm_date = subObject.get("日期").getAsString();
                alarmData.alarm_time = subObject.get("时间").getAsString();
                alarmData.alarm_content = subObject.get("事项").getAsString();
                alarmData.alarm_num = num+"";

                map.put(num++,alarmData);
            }
            last_time = map.get(num-1).alarm_time;
        }
    }
}
