package zskj.jkxt.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import zskj.jkxt.R;
import zskj.jkxt.api.WebService;
import zskj.jkxt.domain.User;

public class UpdateUserActivity extends AppCompatActivity {

    User mUser,newUser;
    TextView update_userName;
    EditText update_userPassword;
    CheckBox update_rightsStation,update_rightsPower,update_rightsAlarm;
    Map<String,Boolean> update_right = new HashMap<>();
    RadioGroup update_userLevel;
    RadioButton update_rbLevelOne,update_rbLevelTwo;
    String update_level;
    Map<String,Boolean> update_range = new HashMap<>();
    RadioGroup update_userRange;
    CheckBox update_rangeStation;
    Button update_update,update_cancel;
    UpdateUserTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        mUser = (User) getIntent().getSerializableExtra("user");

        newUser = mUser;

        update_userName = (TextView) this.findViewById(R.id.update_userName);
        update_userName.setText(mUser.userName);

        update_userPassword = (EditText) this.findViewById(R.id.update_userPassword);
        update_userPassword.setText(mUser.userPassword);
        update_userPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newUser.userPassword = update_userPassword.getText().toString();
            }
        });

        if(update_right == null){
            update_right = new HashMap<>();
        }
        update_right.clear();
        String[] userRights = mUser.userRights.split(",");
        for(int i=0;i<userRights.length;i++){
            update_right.put(userRights[i],true);
        }

        update_rightsStation = (CheckBox) this.findViewById(R.id.update_rightsStation);
        update_rightsPower = (CheckBox) this.findViewById(R.id.update_rightsPower);
        update_rightsAlarm = (CheckBox) this.findViewById(R.id.update_rightsAlarm);
        if(update_right.containsKey(update_rightsStation.getText().toString()))
            update_rightsStation.setChecked(true);
        update_rightsStation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    update_right.put(update_rightsStation.getText().toString(),true);
                }else{
                    update_right.remove(update_rightsStation.getText().toString());
                }
            }
        });
        if(update_right.containsKey(update_rightsPower.getText().toString()))
            update_rightsPower.setChecked(true);
        update_rightsPower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    update_right.put(update_rightsPower.getText().toString(),true);
                }else{
                    update_right.remove(update_rightsPower.getText().toString());
                }
            }
        });
        if(update_right.containsKey(update_rightsAlarm.getText().toString()))
            update_rightsAlarm.setChecked(true);
        update_rightsAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    update_right.put(update_rightsAlarm.getText().toString(),true);
                }else{
                    update_right.remove(update_rightsAlarm.getText().toString());
                }
            }
        });

        update_rbLevelOne = (RadioButton) this.findViewById(R.id.update_rbLevelOne);
        update_rbLevelTwo = (RadioButton) this.findViewById(R.id.update_rbLevelTwo);
        if(mUser.userLevel.equals("1")){
            update_rbLevelOne.setChecked(true);
            update_level = update_rbLevelOne.getText().toString();
        }else{
            update_rbLevelTwo.setChecked(true);
            update_level = update_rbLevelTwo.getText().toString();
        }
        update_userLevel = (RadioGroup) this.findViewById(R.id.update_userLevel);
        update_userLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton)findViewById(update_userLevel.getCheckedRadioButtonId());
                update_level=radioButton.getText().toString();
            }
        });
        if(update_range == null){
            update_range = new HashMap<>();
        }
        update_range.clear();
        String[] userRanges = mUser.userRange.split(",");
        for(int i=0;i<userRanges.length;i++){
            update_range.put(userRanges[i],true);
        }
        update_userRange = (RadioGroup) this.findViewById(R.id.update_userRange);
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
            String station[] = stations.split(";");
            for (int i = 0; i < station.length; i++) {
                final String[] module = station[i].split(",", 3);
                if(i == 0) {
                    update_rangeStation = (CheckBox) this.findViewById(R.id.update_rangeStation1);
                    update_rangeStation.setText(module[0]);
                    update_rangeStation.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                    update_rangeStation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                update_range.put(module[0],true);
                            }else{
                                update_range.remove(module[0]);
                            }
                        }
                    });
                }else{
                    update_rangeStation = new CheckBox(this);
                    update_rangeStation.setText(module[0]);
                    update_rangeStation.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                    update_rangeStation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                Log.e("Listener2--->","checked");
                                update_range.put(module[0],true);
                            }else{
                                Log.e("Listener2--->","unchecked");
                                update_range.remove(module[0]);
                            }
                        }
                    });
                    update_userRange.addView(update_rangeStation, LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                }
                if(update_range.containsKey(module[0])){
                    update_rangeStation.setChecked(true);
                }
            }
        }

        update_update = (Button) this.findViewById(R.id.update_update);
        update_cancel = (Button) this.findViewById(R.id.update_cancel);
        update_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        update_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userRange = "";
                Iterator iterator = update_range.keySet().iterator();
                while(iterator.hasNext()){
                    String key = (String) iterator.next();
                    userRange += key + ",";
                }
                if(update_range.size() == 0){
                    newUser.userRange = "";
                }else{
                    newUser.userRange = userRange.substring(0,userRange.length()-1);
                }

                if(update_level.equals("超级用户")){
                    newUser.userLevel = "1";
                }else{
                    newUser.userLevel = "2";
                }

                String userRights = "";
                Iterator iterator2 = update_right.keySet().iterator();
                while(iterator2.hasNext()){
                    String key = (String) iterator2.next();
                    userRights += key + ",";
                }
                if(update_right.size() == 0){
                    newUser.userRights = "";
                }else{
                    newUser.userRights = userRights.substring(0,userRights.length()-1);
                }
                if(newUser.userName.equals("")){
                    Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_LONG).show();
                    return;
                }else if(newUser.userRights.equals("")){
                    Toast.makeText(getApplicationContext(), "权限不能为空", Toast.LENGTH_LONG).show();
                    return;
                }else if(newUser.userRange.equals("")){
                    Toast.makeText(getApplicationContext(), "范围不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
//                Toast.makeText(getApplicationContext(), mUser.userName +"\n" + mUser.userPassword +
//                        "\n" + confirmPassword + "\n" + mUser.userRights + "\n" + mUser.userRange +
//                        "\n" + mUser.userLevel, Toast.LENGTH_LONG).show();

                updateUser(newUser.userName , newUser.userPassword , newUser.userRights , newUser.userRange , newUser.userLevel);
            }
        });
    }

    private void updateUser(String userName,String password,String rights,String range,String level) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(rights) || TextUtils.isEmpty(range) || TextUtils.isEmpty(level))
            return;
        if (mTask != null) {//不为null 说明操作正在进行，规避多次点击登录按钮操作
            Toast.makeText(getApplicationContext(), "修改数据中，请稍候...", Toast.LENGTH_SHORT).show();
            return;
        }
        mTask = new UpdateUserTask(userName,password,rights,range,level);
        mTask.execute();
    }

    private class UpdateUserTask extends AsyncTask<Void, Void, String> {

        String userName = "";
        String password = "";
        String rights = "";
        String range = "";
        String level = "";

        UpdateUserTask(String userName,String password,String rights,String range,String level) {
            this.userName = userName;
            this.password = password;
            this.rights = rights;
            this.range = range;
            this.level = level;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progress(true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return WebService.getInstance().updateUser(userName,password,rights,range,level);
        }

        @Override
        protected void onPostExecute(String result) {
            mTask = null;
//            progress(false);
            dealResult(result);
        }

        @Override
        protected void onCancelled() {
            mTask = null;
//            progress(false);
        }
    }

    public void dealResult(String result){
        try {
            JSONObject obj = new JSONObject(result);
            int code = obj.optInt("code");
            if (code == 0) {
                String msg = obj.optString("msg");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                return;
            }
            if (obj.optString("data").equals("true")) {
                setResult(1);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
//        if(TextUtils.isEmpty(result)) {
//            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        if(result.equals("true")){
////            Intent intent = new Intent(UpdateUserActivity.this,UserManagementActivity.class);
////            startActivity(intent);
////            Intent intent = new Intent(UpdateUserActivity.this, MainActivity.class);
////            startActivityForResult(intent,1);
//            setResult(1);
//            finish();
//        }else{
//            Toast.makeText(getApplicationContext(),"修改失败，请检查输入或网络",Toast.LENGTH_LONG).show();
//        }

    }
}
