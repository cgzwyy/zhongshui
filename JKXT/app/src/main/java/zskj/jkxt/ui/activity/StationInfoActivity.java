package zskj.jkxt.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zskj.jkxt.R;
import zskj.jkxt.domain.Station;
import zskj.jkxt.domain.StationInfo;

public class StationInfoActivity extends Activity {

    ImageView back_jkinfo;
    ListView lv_staion_info;
    List<StationInfo> stationsInfo = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_info);

        back_jkinfo = (ImageView) this.findViewById(R.id.back_jkinfo);
        back_jkinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lv_staion_info = (ListView) this.findViewById(R.id.lv_station_info);

        for(int i=0;i<5;i++){
            StationInfo mStationInfo = new StationInfo();
            mStationInfo.stationType = "光伏" + i;
            mStationInfo.stationName = "乐平" + i;
            mStationInfo.elecValue = i * 10 + "";
            stationsInfo.add(mStationInfo);
        }

        Log.e("stationInfo size","---------->" + stationsInfo.size());
        lv_staion_info.setAdapter(new stationInfoAdapter());
        lv_staion_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getApplicationContext(), "获取用户信息失败 " + i, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(StationInfoActivity.this,MonitorDetailActivity.class);
                Station model = new Station();
                if(i % 2 == 0){
                    model.columnAddress = "乐平";
                    model.columnName = "光伏";
                    model.columnValue = "lp";
                }else{
                    model.columnAddress = "基隆嶂";
                    model.columnName = "风电场";
                    model.columnValue = "jl";
                }
                intent.putExtra("model",model);
                startActivity(intent);
            }
        });

    }



    class stationInfoAdapter extends BaseAdapter {
        private int[] colors = new int[]{0x30FF0000, 0x300000FF};

        @Override
        public int getCount() {
            return stationsInfo.size();
        }

        @Override
        public Object getItem(int position) {
            return stationsInfo.get(position);
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

            final StationInfo model = stationsInfo.get(position);
//            holder.stationType.setText(model.stationType);
            if(model.stationType.toString().equals("光伏3")){
                holder.stationType.setImageResource(R.drawable.gf2);
            }else{
                holder.stationType.setImageResource(R.drawable.fj2);
            }
            holder.stationName.setText(model.stationName);
            holder.stationElec.setText(model.elecValue);

            return convertView;
        }

    }

    static class ViewHolder {
        ImageView stationType;
        TextView stationName;
        TextView stationElec;
    }
}
