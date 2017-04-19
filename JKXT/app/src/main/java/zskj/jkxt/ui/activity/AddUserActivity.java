package zskj.jkxt.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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


public class AddUserActivity extends AppCompatActivity {

    CheckBox mCheckBox;
    RadioGroup rg_range;
    Map<Integer,String> rights = new HashMap<>();
    Map<Integer,String> range = new HashMap<>();
    Button register; //确认
    Button cancel; //取消
    EditText etName,etPassword,etPasswordConfirm;
    CheckBox cbStation,cbPower,cbAlarm;
    RadioGroup rgLevel;
    User mUser;
    String confirmPassword = "";
    String level = "普通用户";
    AddUserTask mTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        etName = (EditText) this.findViewById(R.id.etName);
        etPassword = (EditText) this.findViewById(R.id.etPassword);
        etPasswordConfirm = (EditText) this.findViewById(R.id.etPasswordConfirm);
        mUser = new User();
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mUser.userName = etName.getText().toString();
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mUser.userPassword = etPassword.getText().toString();
            }
        });
        etPasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                confirmPassword = etPasswordConfirm.getText().toString();
            }
        });

        cbStation = (CheckBox) this.findViewById(R.id.cbStation);
        cbPower = (CheckBox) this.findViewById(R.id.cbPower);
        cbAlarm = (CheckBox) this.findViewById(R.id.cbAlarm);

        if(rights == null)
            rights = new HashMap<>();
        rights.clear();
        rights.put(cbStation.getId(),cbStation.getText().toString());
        cbStation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rights.put(cbStation.getId(),cbStation.getText().toString());
                }else{
                    rights.remove(cbStation.getId());
                }
            }
        });
        cbPower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rights.put(cbPower.getId(),cbPower.getText().toString());
                }else{
                    rights.remove(cbPower.getId());
                }
            }
        });
        cbAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rights.put(cbAlarm.getId(),cbAlarm.getText().toString());
                }else{
                    rights.remove(cbAlarm.getId());
                }
            }
        });


//        rbLevelOne = (RadioButton) this.findViewById(R.id.rbLevelOne);
//        rbLevelTwo = (RadioButton) this.findViewById(R.id.rbLevelTwo);
        rgLevel = (RadioGroup) this.findViewById(R.id.rgLevel);
        rgLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton)findViewById(rgLevel.getCheckedRadioButtonId());
                level=radioButton.getText().toString();
            }
        });

        rg_range = (RadioGroup) this.findViewById(R.id.rg_range);

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
        if(range == null){
            range = new HashMap<>();
        }
        range.clear();
        if (!TextUtils.isEmpty(stations)) {
            String station[] = stations.split(";");
            for (int i = 0; i < station.length; i++) {
                final String[] module = station[i].split(",", 3);
                final int finalI = i;
                if(i == 0) {
                    mCheckBox = (CheckBox) this.findViewById(R.id.cbstation1);
                    mCheckBox.setText(module[0]);
                    mCheckBox.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                    range.put(finalI,module[0]);
                    mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                range.put(finalI,module[0]);
                            }else{
                                range.remove(finalI);
                            }
                        }
                    });
                }else{
                    mCheckBox = new CheckBox(this);
                    mCheckBox.setText(module[0]);
                    mCheckBox.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                    mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                range.put(finalI,module[0]);
                            }else{
                                range.remove(finalI);
                            }
                        }
                    });
                    rg_range.addView(mCheckBox, LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                }
            }
        }

        register = (Button) this.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userRange = "";
                Iterator iterator = range.keySet().iterator();
                while(iterator.hasNext()){
                    int key = (int) iterator.next();
                    userRange += range.get(key) + ",";
                }
                if(range.size() == 0){
                    mUser.userRange = "";
                }else{
                    mUser.userRange = userRange.substring(0,userRange.length()-1);
                }

                String userRights = "";
                Iterator iterator2 = rights.keySet().iterator();
                while(iterator2.hasNext()){
                    int key = (int) iterator2.next();
                    userRights += rights.get(key) + ",";
                }
                if(rights.size() == 0){
                    mUser.userRights = "";
                }else{
                    mUser.userRights = userRights.substring(0,userRights.length()-1);
                }

                if(level.equals("普通用户")){
                    mUser.userLevel = "2";
                }else{
                    mUser.userLevel = "1";
                }

                if(mUser.userName.equals("")){
                    Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_LONG).show();
                    return;
                }else if(!mUser.userPassword.equals(confirmPassword)){
                    Toast.makeText(getApplicationContext(), "两次输入密码不一致", Toast.LENGTH_LONG).show();
                    return;
                }else if(mUser.userRights.equals("")){
                    Toast.makeText(getApplicationContext(), "权限不能为空", Toast.LENGTH_LONG).show();
                    return;
                }else if(mUser.userRange.equals("")){
                    Toast.makeText(getApplicationContext(), "范围不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
//                Toast.makeText(getApplicationContext(), mUser.userName +"\n" + mUser.userPassword +
//                        "\n" + confirmPassword + "\n" + mUser.userRights + "\n" + mUser.userRange +
//                        "\n" + mUser.userLevel, Toast.LENGTH_LONG).show();

                addUser(mUser.userName , mUser.userPassword , mUser.userRights , mUser.userRange , mUser.userLevel);
            }
        });

        cancel = (Button) this.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addUser(String userName,String password,String rights,String range,String level) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(rights) || TextUtils.isEmpty(range) || TextUtils.isEmpty(level))
            return;
        if (mTask != null) {//不为null 说明操作正在进行，规避多次点击登录按钮操作
            Toast.makeText(getApplicationContext(), "插入数据中，请稍候...", Toast.LENGTH_SHORT).show();
            return;
        }
        mTask = new AddUserTask(userName,password,rights,range,level);
        mTask.execute();
    }

    private class AddUserTask extends AsyncTask<Void, Void, String> {

        String userName = "";
        String password = "";
        String rights = "";
        String range = "";
        String level = "";

        AddUserTask(String userName,String password,String rights,String range,String level) {
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
            return WebService.getInstance().addUser(userName,password,rights,range,level);
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
////            Intent intent = new Intent(AddUserActivity.this,UserManagementActivity.class);
////            startActivity(intent);
//            setResult(1);
//            finish();
//        }else{
//            etName.setError("用户名重复");
////            Toast.makeText(getApplicationContext(),"用户名重复",Toast.LENGTH_LONG).show();
//        }

    }

}
