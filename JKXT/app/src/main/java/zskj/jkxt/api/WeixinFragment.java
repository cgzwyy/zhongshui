package zskj.jkxt.api;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import zskj.jkxt.activity.MonitorDetailActivity;
import zskj.jkxt.domain.Station;

/**
 * Created by WYY on 2017/3/14.
 */

public class WeixinFragment extends Fragment {

    // Fragment管理对象
    private FragmentManager manager;
    private FragmentTransaction ft;

    GridView lv_station_name;
    List<Station> list = new ArrayList<Station>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_station, container, false);

        manager = getFragmentManager();

        lv_station_name = (GridView) view.findViewById(R.id.lv_station_name);
        getData();
        lv_station_name.setAdapter(new mAdapter(getActivity(),list));
        lv_station_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MonitorDetailActivity.class);
                intent.putExtra("model",list.get(position));
                startActivity(intent);
//                MontiorDetailFragment montiorDetailFragment = new MontiorDetailFragment();
////                SettingFragment montiorDetailFragment = new SettingFragment();
//                ft = manager.beginTransaction();
//                //当前的fragment会被myJDEditFragment替换
//                ft.replace(R.id.id_content, montiorDetailFragment);
//                Bundle model = new Bundle();
//                model.putSerializable("model",list.get(position));
//                montiorDetailFragment.setArguments(model);
//                ft.addToBackStack(null);
//                ft.commit();
            }
        });

        return view;
    }

    private List<Station> getData() {
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

        list.clear();

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
        private Context context;
        List<Station> list;

        public mAdapter(Context context,List<Station> list) {
            super();
            this.context = context;
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
                convertView = View.inflate(context, R.layout.item_station, null);
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
