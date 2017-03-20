package zskj.jkxt.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import zskj.jkxt.R;
import zskj.jkxt.domain.AlarmData;

/**
 * Created by WYY on 2017/3/14.
 */

public class AddressFragment extends Fragment {

    ProgressDialog dialog = null;   //进度对话框
    ListView lv_alarm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_alarm_test, container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("加载中...");     //设置提示信息
        dialog.setCanceledOnTouchOutside(false);   //设置在点击Dialog外是否取消Dialog进度条

        lv_alarm = (ListView) view.findViewById(R.id.lv_alarm);

        GetDataTop10();

        return view;
    }

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
    private void GetData() {
        dialog.show();   //显示进度条对话框
        new Thread() {
            public void run(){
                WebService.getInstance().GetAlarmData("20170317","0", new RequestCallback() {

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
            holder.alarm_type.setText(model.alarm_type);
//            holder.alarm_time.setText(model.alarm_date);
            SimpleDateFormat formatter = new SimpleDateFormat("HHmmssSSS");
            SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss:SSS");
            String formatStr = null;
            String s = model.alarm_time;
            try {
                formatStr = formatter2.format(formatter.parse(String.format("%1$0"+(9-s.length())+"d",0)+s));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.alarm_time.setText(formatStr);
            holder.alarm_content.setText(model.alarm_content);
            return convertView;
        }

    }

    static class ViewHolder {
        TextView alarm_type;
        TextView alarm_time;
        TextView alarm_content;
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
}
