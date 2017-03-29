package zskj.jkxt.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

import zskj.jkxt.R;
import zskj.jkxt.api.WebService;
import zskj.jkxt.domain.User;

/**
 * Created by WYY on 2017/3/14.
 */

public class UserManagementFragment extends Fragment {

    Context mContext;
    ImageView newUser;
    ListView lv_user_list;
    Map<Integer,User> map = new HashMap<>();
    UserManagementTask mTask;
    int num = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_user_management, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newUser = (ImageView) getView().findViewById(R.id.addUser);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"add User",Toast.LENGTH_SHORT).show();
            }
        });

        num = 0;
        lv_user_list = (ListView) getView().findViewById(R.id.lv_user_list);
        getUserData();
    }

    /**
     * 获取风机详情
     *
     * @param
     */
    private void getUserData() {
        if (mTask != null) {//不为null 说明操作正在进行，规避多次点击登录按钮操作
            Toast.makeText(mContext, "加载中，请稍候...", Toast.LENGTH_SHORT).show();
            return;
        }
        mTask = new UserManagementTask();
        mTask.execute();
    }

    /**
     * 风机详情任务task
     */
    private class UserManagementTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return WebService.getInstance().GetUserInfo();
        }

        @Override
        protected void onPostExecute(String result) {
            mTask = null;
            dealResult(result);
        }

        @Override
        protected void onCancelled() {
            mTask = null;
        }
    }

    private void dealResult(String result) {
        Log.e("resutl-->",result);
        if (map == null) {
            map = new HashMap<>();
        }else{
            map.clear();
            num = 0;
        }
        if (!result.contains("{")) {
            return;
        } else {
            JsonParser parser = new JsonParser();//创建JSON解析器
            JsonObject object = (JsonObject) parser.parse(result); //创建JsonObject对象
            JsonArray users = object.get("userlist").getAsJsonArray(); //得到为json的数组

            for (int i = 0; i < users.size(); i++) {
                JsonObject subObject = users.get(i).getAsJsonObject();

                User user = new User();
                user.userId = num + "";
                user.userName = subObject.get("userName").getAsString();
                user.userPassword = subObject.get("userPassword").getAsString();
                user.userRights = subObject.get("userRights").getAsString();
                user.userRange = subObject.get("userRange").getAsString();
                user.userLevel = subObject.get("userLevel").getAsString();
                map.put(num++, user);
            }

            lv_user_list.setAdapter(new userListAdapter(map));
            lv_user_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(mContext,"item click  " + map.get(position).userName,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    class userListAdapter extends BaseAdapter {
        private int[] colors = new int[]{0x30FF0000, 0x300000FF};
        Map<Integer,User> map;

        public userListAdapter(Map<Integer,User> map){
            super();
            this.map = map;
            if(map == null){
                map = new HashMap<>();
            }
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return map.size();
        }

        public Map<Integer,User> getData() {
            // TODO Auto-generated method stub
            return map;
        }

        public int getMinNum() {
            // TODO Auto-generated method stub
            return Integer.valueOf(map.get(0).userId);
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return map.get(position);
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
                convertView = View.inflate(mContext, R.layout.item_user, null);
                holder.userId = (TextView) convertView.findViewById(R.id.userId);
                holder.userName = (TextView) convertView.findViewById(R.id.userName);
                holder.userRights = (TextView) convertView.findViewById(R.id.userRights);
                holder.userRange = (TextView) convertView.findViewById(R.id.userRange);
                holder.userLevel = (TextView) convertView.findViewById(R.id.userLevel);
                holder.updateUser = (ImageView) convertView.findViewById(R.id.updateUser);
                holder.deleteUser = (ImageView) convertView.findViewById(R.id.deleteUser);
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

            final User model = map.get(position);
            holder.userId.setText(model.userId);
            holder.userName.setText(model.userName);
            holder.userRights.setText(model.userRights);
            holder.userRange.setText(model.userRange);
            holder.userLevel.setText(model.userLevel);
            holder.updateUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"update  " + model.userName,Toast.LENGTH_SHORT).show();
                }
            });

            holder.deleteUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"delete  "+model.userName,Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }

    }

    static class ViewHolder {
        TextView userId;
        TextView userName;
        TextView userRights;
        TextView userRange;
        TextView userLevel;
        ImageView updateUser;
        ImageView deleteUser;
    }
}
