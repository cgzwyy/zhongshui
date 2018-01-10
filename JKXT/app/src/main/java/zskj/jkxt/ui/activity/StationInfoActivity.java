package zskj.jkxt.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import zskj.jkxt.R;
import zskj.jkxt.WebService;
import zskj.jkxt.domain.Station;

public class StationInfoActivity extends Activity {

    ImageView back_jkinfo;
    ListView lv_staion_info;
//    List<StationInfo> stationsInfo = new ArrayList<>();
    List<Station> list = new ArrayList<Station>();
    String ranges;
    GetElecInfoTask mTask;
    stationInfoAdapter mAdapter;
    DecimalFormat df = new DecimalFormat("0.00");  //数据格式转换，四舍五入，保留两位小数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_info);
        ranges = getIntent().getStringExtra("ranges");

        back_jkinfo = (ImageView) this.findViewById(R.id.back_jkinfo);
        back_jkinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
//                Intent intent = new Intent();
//                intent.putExtra("ranges",ranges);
////                startActivity(intent);
//                setResult(4,intent);
                finish();
            }
        });

        lv_staion_info = (ListView) this.findViewById(R.id.lv_station_info);
        getStationsData();

        lv_staion_info.setAdapter(mAdapter = new stationInfoAdapter());
        lv_staion_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getApplicationContext(), "获取用户信息失败 " + i, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(StationInfoActivity.this,MonitorDetailActivity.class);
                Station model = list.get(i);
                intent.putExtra("model",model);
                startActivity(intent);
            }
        });

    }

    private void getStationsData() {
        String stations = "";
        try {
            InputStream in = getResources().openRawResource(R.raw.stations);//获取
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            stations = new String(buffer, "UTF-8");
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(stations)) {
            if (list != null && list.size() > 0)
                list.clear();
            String station[] = stations.split(";");
            for (int i = 0; i < station.length; i++) {
                String[] module = station[i].split(",", 3);
                if(ranges.contains(module[0])){
                    Station model = new Station();
                    model.columnAddress = module[0]; //位置
                    model.columnName = module[1];  //厂站类型
                    model.columnValue = module[2];  //编号
                    model.stationElec = "0.00";
                    list.add(model);
                }
            }
            if(!list.isEmpty()){
                if (mTask != null)
                    return;
                mTask = new GetElecInfoTask();
                mTask.execute();
            }
        }
    }

    private class GetElecInfoTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgress(true);
        }

        @Override
        protected String doInBackground(Void... voids) {
//            Log.e("ranges","-------->"+ranges);
            return WebService.getInstance().getStationsElec(ranges);
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
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject data = obj.optJSONObject("data");
                for(int i=0;i<list.size();i++){
                    Station tmp_station = list.get(i);
                    if(data.has(tmp_station.columnAddress)){
//                        Log.e("xxx","------------>"+data.optString(tmp_station.columnAddress));
                        tmp_station.stationElec = df.format(Double.valueOf(data.optString(tmp_station.columnAddress)));
//                        tmp_station.setStationElec();
                        list.set(i,tmp_station);
                    }
                }
                if (mAdapter != null && list != null)
                    mAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(getApplicationContext(), "获取用户信息失败1", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "获取用户信息失败2", Toast.LENGTH_SHORT).show();
        }
    }

    class stationInfoAdapter extends BaseAdapter {
        private int[] colors = new int[]{0x30FF0000, 0x300000FF};

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(getApplicationContext(), R.layout.item_station_info, null);
                holder.stationType = (ImageView) convertView.findViewById(R.id.stations_type);
                holder.stationName = (TextView) convertView.findViewById(R.id.station_name);
                holder.stationElec = (TextView) convertView.findViewById(R.id.station_elec);
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

            final Station model = list.get(position);
//            holder.stationType.setText(model.stationType);
            if(model.columnName.toString().contains("光伏")){
                holder.stationType.setImageResource(R.drawable.gf2);
            }else{
                holder.stationType.setImageResource(R.drawable.fj2);
            }
            holder.stationName.setText(model.columnAddress);
            holder.stationElec.setText(model.stationElec);

            return convertView;
        }

    }

    static class ViewHolder {
        ImageView stationType;
        TextView stationName;
        TextView stationElec;
    }
}
