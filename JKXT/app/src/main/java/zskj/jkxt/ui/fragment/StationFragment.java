package zskj.jkxt.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import zskj.jkxt.R;
import zskj.jkxt.ui.activity.MonitorDetailActivity;
import zskj.jkxt.domain.Station;

/**
 * Created by WYY on 2017/3/14.
 * 站点列表页
 */
public class StationFragment extends Fragment {

    Context mContext;
    List<Station> list = new ArrayList<Station>();
    GridView lv_station_name;
    MyAdapter mAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_station, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lv_station_name = (GridView) getView().findViewById(R.id.lv_station_name);
        lv_station_name.setAdapter(mAdapter = new MyAdapter());
        lv_station_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MonitorDetailActivity.class);
                intent.putExtra("model", list.get(position));
                startActivity(intent);
            }
        });
        getData();
    }

    private void getData() {
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
                Station model = new Station();
                model.columnAddress = module[0]; //位置
                model.columnName = module[1];  //厂站类型
                model.columnValue = module[2];  //编号
                list.add(model);
            }
            if (mAdapter != null && list != null)
                mAdapter.notifyDataSetChanged();
        }

    }


    class MyAdapter extends BaseAdapter {

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
                convertView = View.inflate(mContext, R.layout.item_station, null);
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
            if (model.columnName.toString().indexOf("光伏") != -1) {//.............stupid~
                holder.image.setBackground(getResources().getDrawable(R.drawable.gf));
            } else {
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
