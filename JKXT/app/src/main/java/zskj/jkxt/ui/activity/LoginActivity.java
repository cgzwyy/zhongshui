package zskj.jkxt.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import zskj.jkxt.R;
import zskj.jkxt.WebService;

public class LoginActivity extends Activity {

    private UserLoginTask mAuthTask = null;

    private EditText mUserNameView, mPasswordView;
    private Button mSignBtn;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserNameView = (EditText) this.findViewById(R.id.userName);
        mPasswordView = (EditText) this.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_ACTION_GO) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mSignBtn = (Button) this.findViewById(R.id.sign_in_button);
        mSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mProgressView = findViewById(R.id.login_progress);
    }

    //密码可以为空
    private void attemptLogin() {
        if (mAuthTask != null) {//不为null 说明登录操作正在进行，规避多次点击登录按钮操作
            return;
        }
        mUserNameView.setError(null);
        mPasswordView.setError(null);
        String userName = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(userName)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            mAuthTask = new UserLoginTask(userName, password);
            mAuthTask.execute();
        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mUser;
        private final String mPassword;

        UserLoginTask(String userName, String password) {
            mUser = userName;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(Void... params) {
            return WebService.getInstance().isUser(mUser, mPassword);
        }

        @Override
        protected void onPostExecute(String result) {
            mAuthTask = null;
            showProgress(false);
            parserResult(result);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private void parserResult(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            int code = obj.optInt("code");
            if (code == 0) {
                String msg = obj.optString("msg");
                mPasswordView.setError(msg); //密码错误
                mPasswordView.requestFocus();
                return;
            }
            JSONObject data = obj.optJSONObject("data");
            String rights = data.optString("rights");
            String ranges = data.optString("ranges");
            String level = data.optString("level");
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, MainActivity.class);
            intent.putExtra("rights", rights);
            intent.putExtra("ranges", ranges);
            intent.putExtra("level", level);
            startActivity(intent);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
            mPasswordView.setError(result); //密码错误
            mPasswordView.requestFocus();
        }
//        Intent intent = new Intent();
//        intent.setClass(LoginActivity.this, MainActivity.class);
//        intent.putExtra("rights", "场站,全场功率,实时告警");
//        intent.putExtra("ranges", "布尔津,青河光伏,托克逊,鄯善二期,鄯善");
//        intent.putExtra("level", "1");
//        startActivity(intent);
//        finish();
    }

    /**
     * Shows the progress UI and hides the login form.
     * 显示进度条界面，关闭登录窗体
     */
    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }


    }

}

