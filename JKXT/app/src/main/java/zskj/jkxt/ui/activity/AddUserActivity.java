package zskj.jkxt.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import zskj.jkxt.JKXTApplication;
import zskj.jkxt.R;
import zskj.jkxt.WebService;


public class AddUserActivity extends AppCompatActivity {

    //用户名、密码
    EditText etName, etPassword, etPasswordConfirm;
    //权限
    CheckBox cbStation, cbPower, cbAlarm;
    //级别
    RadioGroup rgLevel;
    RadioButton rbLevelOne;
    RadioButton rbLevelTwo;
    //范围
    LinearLayout rg_range;
    //操作按钮
    Button register; //确认
    Button cancel; //取消
    //pro
    private View mProgressView;

    AddUserTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        initView();
    }

    private void initView() {
        etName = (EditText) this.findViewById(R.id.etName);
        etPassword = (EditText) this.findViewById(R.id.etPassword);
        etPasswordConfirm = (EditText) this.findViewById(R.id.etPasswordConfirm);
        cbStation = (CheckBox) this.findViewById(R.id.cbStation);
        cbPower = (CheckBox) this.findViewById(R.id.cbPower);
        cbAlarm = (CheckBox) this.findViewById(R.id.cbAlarm);
        rgLevel = (RadioGroup) this.findViewById(R.id.rgLevel);
        rbLevelOne = (RadioButton) this.findViewById(R.id.rbLevelOne);
        rbLevelTwo = (RadioButton) this.findViewById(R.id.rbLevelTwo);
        rg_range = (LinearLayout) this.findViewById(R.id.rg_range);
//        Log.e("stations length------>",JKXTApplication.stations.length+"");
//        if (JKXTApplication.stations != null)
//            Toast.makeText(getApplicationContext(), "stations:" + JKXTApplication.stations.toString(), Toast.LENGTH_SHORT).show();
        if (JKXTApplication.stations != null && JKXTApplication.stations.length > 0) {
            for (int i = 0; i < JKXTApplication.stations.length; i++) {
                CheckBox child = new CheckBox(this);
                child.setText(JKXTApplication.stations[i]);
                child.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                rg_range.addView(child);
            }
        }
        register = (Button) this.findViewById(R.id.register);
        cancel = (Button) this.findViewById(R.id.cancel);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mProgressView = findViewById(R.id.login_progress);
    }

    private void addUser() {
        if (mTask != null) {
            Toast.makeText(getApplicationContext(), "on Loading...", Toast.LENGTH_SHORT).show();
            return;
        }

        String userName = etName.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            etName.setError("username  is required");
            etName.requestFocus();
            return;
        }

        String pwd = etPassword.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            etPassword.setError("password  is required");
            etPassword.requestFocus();
            return;
        }

        String pwd_confirm = etPasswordConfirm.getText().toString();
        if (TextUtils.isEmpty(pwd_confirm) || !pwd.equals(pwd_confirm)) {
            etPasswordConfirm.setError("confirm the password");
            etPasswordConfirm.requestFocus();
            return;
        }

        String rights = "";
        if (cbStation.isChecked())
            rights += cbStation.getText().toString();
        if (cbPower.isChecked())
            rights += TextUtils.isEmpty(rights) ? cbPower.getText().toString() : "," + cbPower.getText().toString();
        if (cbAlarm.isChecked())
            rights += TextUtils.isEmpty(rights) ? cbAlarm.getText().toString() : "," + cbAlarm.getText().toString();
        if (TextUtils.isEmpty(rights)) {
            Toast.makeText(this, "choose user rights", Toast.LENGTH_SHORT).show();
            return;
        }

        String level = "0";
        if (rbLevelOne.isChecked())
            level = "1";

        String range = "";
        for (int i = 0; i < rg_range.getChildCount(); i++) {
            CheckBox child = (CheckBox) rg_range.getChildAt(i);
            if (child.isChecked())
                range += TextUtils.isEmpty(range) ? child.getText().toString() : "," + child.getText().toString();
        }
        if (TextUtils.isEmpty(range)) {
            Toast.makeText(this, "choose user control rang", Toast.LENGTH_SHORT).show();
            return;
        }
        mTask = new AddUserTask(userName, pwd, rights, range, level);
        mTask.execute();
    }

    private class AddUserTask extends AsyncTask<Void, Void, String> {

        String userName = "";
        String password = "";
        String rights = "";
        String range = "";
        String level = "";

        AddUserTask(String userName, String password, String rights, String range, String level) {
            this.userName = userName;
            this.password = password;
            this.rights = rights;
            this.range = range;
            this.level = level;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return WebService.getInstance().addUser(userName, password, rights, range, level);
        }

        @Override
        protected void onPostExecute(String result) {
            mTask = null;
            showProgress(false);
            dealResult(result);
        }

        @Override
        protected void onCancelled() {
            mTask = null;
            showProgress(false);
        }
    }

    public void dealResult(String result) {
        try {
            if(result != null && result.toString() != null){
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
            }else{
                Toast.makeText(getApplicationContext(), "新增用户信息失败1", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "新增用户信息失败2", Toast.LENGTH_SHORT).show();
        }
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

}
