package zskj.jkxt.ui.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import zskj.jkxt.R;
import zskj.jkxt.WebService;
import zskj.jkxt.domain.User;

public class UpdateUserActivity extends AppCompatActivity implements View.OnClickListener {

    User mUser;
    //账号密码
    TextView update_userName;
    EditText update_userPassword;//密码
    //权限
    CheckBox update_rightsStation, update_rightsPower, update_rightsAlarm;
    //用户级别
    RadioGroup update_userLevel;
    RadioButton update_rbLevelOne, update_rbLevelTwo;
    //管理范围
    LinearLayout update_userRange;
    String[] stations;//所有 站点
    //按钮
    Button update_update, update_cancel;
    //pro
    private View mProgressView;
    UpdateUserTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        mUser = (User) getIntent().getSerializableExtra("user");
        initView();
        initData();
    }

    private void initView() {
        update_userName = (TextView) this.findViewById(R.id.update_userName);
        update_userPassword = (EditText) this.findViewById(R.id.update_userPassword);
        update_rightsStation = (CheckBox) this.findViewById(R.id.update_rightsStation);
        update_rightsPower = (CheckBox) this.findViewById(R.id.update_rightsPower);
        update_rightsAlarm = (CheckBox) this.findViewById(R.id.update_rightsAlarm);

        update_userLevel = (RadioGroup) this.findViewById(R.id.update_userLevel);
        update_rbLevelOne = (RadioButton) this.findViewById(R.id.update_rbLevelOne);
        update_rbLevelTwo = (RadioButton) this.findViewById(R.id.update_rbLevelTwo);

        update_userRange = (LinearLayout) this.findViewById(R.id.update_userRange);
        getStation();
        if (stations != null && stations.length > 0) {
            for (int i = 0; i < stations.length; i++) {
                CheckBox child = new CheckBox(this);
                child.setText(stations[i]);
                child.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                update_userRange.addView(child);
            }
        }

        update_update = (Button) this.findViewById(R.id.update_update);
        update_cancel = (Button) this.findViewById(R.id.update_cancel);

        update_update.setOnClickListener(this);
        update_cancel.setOnClickListener(this);

        mProgressView = findViewById(R.id.login_progress);

    }

    private void initData() {
        if (mUser == null)
            return;
        update_userName.setText(mUser.userName);
        update_userPassword.setText(mUser.userPassword);
        String[] userRights = null;
        if (!TextUtils.isEmpty(mUser.userRights)) {
            userRights = mUser.userRights.split(",");
        }
        if (userRights != null && userRights.length > 0) {
            for (int i = 0; i < userRights.length; i++) {
                if (userRights[i].equals(update_rightsStation.getText().toString())) {
                    update_rightsStation.setChecked(true);
                    continue;
                }
                if (userRights[i].equals(update_rightsPower.getText().toString())) {
                    update_rightsPower.setChecked(true);
                    continue;
                }
                if (userRights[i].equals(update_rightsAlarm.getText().toString())) {
                    update_rightsAlarm.setChecked(true);
                    continue;
                }
            }
        }
        if (!TextUtils.isEmpty(mUser.userLevel)) {
            if (mUser.userLevel.equals("1")) {
                update_rbLevelOne.setChecked(true);
            } else {
                update_rbLevelTwo.setChecked(true);
            }
        }
        String[] userRanges = null;
        if (!TextUtils.isEmpty(mUser.userRange)) {
            userRanges = mUser.userRange.split(",");
        }
        if (userRanges != null && userRanges.length > 0) {
            List<String> rangList = Arrays.asList(userRanges);
            for (int i = 0; i < update_userRange.getChildCount(); i++) {
                CheckBox child = (CheckBox) update_userRange.getChildAt(i);
                String childText = child.getText().toString();
                if (rangList.contains(childText)) {
                    child.setChecked(true);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.update_cancel:
                finish();
                break;
            case R.id.update_update:
                updateUser();
                break;
        }

    }

    private void updateUser() {
        String userName = update_userName.getText().toString();
        String pwd = update_userPassword.getText().toString();
        if (TextUtils.isEmpty(pwd))
            return;
        String rights = "";
        if (update_rightsStation.isChecked())
            rights += update_rightsStation.getText().toString();
        if (update_rightsPower.isChecked())
            rights += TextUtils.isEmpty(rights) ? update_rightsPower.getText().toString() : "," + update_rightsPower.getText().toString();
        if (update_rightsAlarm.isChecked())
            rights += TextUtils.isEmpty(rights) ? update_rightsAlarm.getText().toString() : "," + update_rightsAlarm.getText().toString();
        if (TextUtils.isEmpty(rights))
            return;
        String range = "";
        for (int i = 0; i < update_userRange.getChildCount(); i++) {
            CheckBox child = (CheckBox) update_userRange.getChildAt(i);
            if (child.isChecked())
                range += TextUtils.isEmpty(range) ? child.getText().toString() : "," + child.getText().toString();
        }
        if (TextUtils.isEmpty(range))
            return;
        String level = "0";
        if (update_rbLevelOne.isChecked())
            level = "1";
        updateUser(userName, pwd, rights, range, level);
    }


    private void updateUser(String userName, String password, String rights, String range, String level) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(rights) || TextUtils.isEmpty(range) || TextUtils.isEmpty(level))
            return;
        if (mTask != null) {//不为null 说明操作正在进行，规避多次点击登录按钮操作
            Toast.makeText(getApplicationContext(), "修改数据中，请稍候...", Toast.LENGTH_SHORT).show();
            return;
        }
        mTask = new UpdateUserTask(userName, password, rights, range, level);
        mTask.execute();
    }


    private class UpdateUserTask extends AsyncTask<Void, Void, String> {

        String userName = "";
        String password = "";
        String rights = "";
        String range = "";
        String level = "";

        UpdateUserTask(String userName, String password, String rights, String range, String level) {
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
            return WebService.getInstance().updateUser(userName, password, rights, range, level);
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

    }


    private void getStation() {
        String station = "";
        try {
            InputStream in = getResources().openRawResource(R.raw.stations);//获取
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            station = new String(buffer, "UTF-8");
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(station)) {
            String[] dataSet = station.split(";");
            if (dataSet != null && dataSet.length > 0) {
                stations = new String[dataSet.length];
                for (int i = 0; i < dataSet.length; i++) {
                    String[] data = dataSet[i].split(",");
                    stations[i] = data[0];
                }
            }
        }
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
