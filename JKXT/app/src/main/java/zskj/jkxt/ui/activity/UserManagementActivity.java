package zskj.jkxt.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import zskj.jkxt.ui.widget.UserDetailPopu;

import static zskj.jkxt.R.id.deleteUser;

public class UserManagementActivity extends AppCompatActivity {

    ImageView newUser;
    ListView lv_user_list;
    Map<Integer,User> map = new HashMap<>();
    UserManagementTask mTask;
    int num = 0;
    DeleteUserTask mDeleteUserTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        newUser = (ImageView) this.findViewById(R.id.addUser);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"add User",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserManagementActivity.this,AddUserActivity.class);
                startActivity(intent);
            }
        });

        num = 0;
        lv_user_list = (ListView) this.findViewById(R.id.lv_user_list);
        getUserData();
    }

    /**
     * 获取风机详情
     *
     * @param
     */
    private void getUserData() {
        if (mTask != null) {//不为null 说明操作正在进行，规避多次点击登录按钮操作
            Toast.makeText(getApplicationContext(), "加载中，请稍候...", Toast.LENGTH_SHORT).show();
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
                    User user = map.get(position);
                    showDetailPopu(user);
                }
            });
        }
    }
    UserDetailPopu mPopu;

    private void showDetailPopu(User user) {
        if (mPopu == null)
            mPopu = new UserDetailPopu(this);

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        mPopu.setWidth((int) (d.getWidth() * 0.8));

        backgroundAlpha(0.5f);
        mPopu.setUser(user);
        mPopu.showAtLocation(getWindow().getDecorView().findViewById(android.R.id.content), Gravity.CENTER_VERTICAL, 0, 0);

        mPopu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    //设置背景透明度，1f为不透明
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
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
                convertView = View.inflate(getApplicationContext(), R.layout.item_user, null);
                holder.userId = (TextView) convertView.findViewById(R.id.userId);
                holder.userName = (TextView) convertView.findViewById(R.id.userName);
                holder.userRights = (TextView) convertView.findViewById(R.id.userRights);
                holder.userRange = (TextView) convertView.findViewById(R.id.userRange);
                holder.userLevel = (TextView) convertView.findViewById(R.id.userLevel);
                holder.updateUser = (ImageView) convertView.findViewById(R.id.updateUser);
                holder.deleteUser = (ImageView) convertView.findViewById(deleteUser);
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
                    Intent intent = new Intent(UserManagementActivity.this,UpdateUserActivity.class);
                    intent.putExtra("user",model);
                    startActivity(intent);
                }
            });

            holder.deleteUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder normalDialog =new AlertDialog.Builder(UserManagementActivity.this);
                    normalDialog.setTitle("提示");
                    normalDialog.setMessage("确认删除？");
                    normalDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteUser(model.userName);
                        }
                    });
                    normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
                        }
                    });
                    normalDialog.show();
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

    private void deleteUser(String userName) {
        if (TextUtils.isEmpty(userName) )
            return;
        if (mDeleteUserTask != null) {//不为null 说明操作正在进行，规避多次点击登录按钮操作
            Toast.makeText(getApplicationContext(), "删除数据中，请稍候...", Toast.LENGTH_SHORT).show();
            return;
        }
        mDeleteUserTask = new DeleteUserTask(userName);
        mDeleteUserTask.execute();
    }

    private class DeleteUserTask extends AsyncTask<Void, Void, String> {

        String userName = "";

        DeleteUserTask(String userName) {
            this.userName = userName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progress(true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return WebService.getInstance().deleteUser(userName);
        }

        @Override
        protected void onPostExecute(String result) {
            mTask = null;
//            progress(false);
            deleteResult(result);
        }

        @Override
        protected void onCancelled() {
            mTask = null;
//            progress(false);
        }
    }

    public void deleteResult(String result){
        if(TextUtils.isEmpty(result)) {
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            return;
        }

        if(result.equals("true")){
            getUserData();
        }else{
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
        }

    }
}
