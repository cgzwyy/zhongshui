package zskj.jkxt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import zskj.jkxt.R;
import zskj.jkxt.domain.Station;

public class StationActivity extends Activity {

    GridView lv_station_name;
    List<Station> list = new ArrayList<Station>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        lv_station_name = (GridView) this.findViewById(R.id.lv_station_name);
        getData();
        lv_station_name.setAdapter(new mAdapter(list));
        lv_station_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StationActivity.this, MonitorDetailActivity.class);
                intent.putExtra("model",list.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Intent intent = new Intent(StationActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private List<Station> getData() {
        String stations = "";
        try {
            InputStream in = getResources().openRawResource(R.raw.stations);//获取
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            stations = EncodingUtils.getString(buffer, "UTF-8");
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!TextUtils.isEmpty(stations)) {
            String station[] = stations.split(";");
            for (int i = 0; i < station.length; i++) {
                Station model = new Station();
                model.columnAddress = station[i].split(",", 3)[0]; //位置
                model.columnName = station[i].split(",", 3)[1];  //厂站类型
                model.columnValue = station[i].split(",", 3)[2];  //编号

                list.add(model);
            }
        }
        return list;
    }

    class mAdapter extends BaseAdapter {
        List<Station> list;

        public mAdapter(List<Station> list) {
            super();
            this.list = list;
            if (list == null)
                list = new ArrayList<Station>();
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
                convertView = View.inflate(getApplicationContext(), R.layout.item_station, null);
                holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.image = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Station model = list.get(position);
            holder.tv_address.setText(model.columnAddress);
            holder.tv_name.setText(model.columnName);
            if(model.columnName.toString().indexOf("光伏") != -1){
                holder.image.setBackground(getResources().getDrawable(R.drawable.gf));
            }else{
                holder.image.setBackground(getResources().getDrawable(R.drawable.fd));
            }
            return convertView;
        }

    }

    static class ViewHolder {
        ImageView image;
        TextView tv_address;
        TextView tv_name;
    }
}
