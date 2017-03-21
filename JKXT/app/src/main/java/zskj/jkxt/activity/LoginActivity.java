package zskj.jkxt.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import zskj.jkxt.R;
import zskj.jkxt.api.RequestCallback;
import zskj.jkxt.api.WebService;

/**
 * A login screen that offers login via email/password.
 * 登录界面有email和password两个输入框
 */
public class LoginActivity extends Activity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     * 声明用户登录引用，方便随时撤销登录请求
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mUserNameView, mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserNameView = (EditText) this.findViewById(R.id.userName);

        mPasswordView = (EditText) this.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                //当用户处在输入密码编辑框，软键盘打开，输入确定按钮(EditorInfo.IME_NUL)时尝试登录
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSingInButton = (Button) this.findViewById(R.id.sign_in_button);
        //登录按钮点击事件
        mSingInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        //登录界面和进度条界面
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userName = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        //检查密码有效性
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(userName)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        }

        //取消登录
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            isUser(userName, password);
//            mAuthTask.execute();
//            if(isUser(userName,password)){
//                Intent intent = new Intent();
//                intent.setClass(LoginActivity.this,StationActivity.class);
//                startActivity(intent);
//            }else{
//                showProgress(false);
//                mUserNameView.setError(getString(R.string.error_incorrect_username_password)); //密码错误
//                mUserNameView.requestFocus();
//            }
        }
    }

    //验证用户名密码是否正确
    private void isUser(final String userName, final String password) {
//        dialog.show();
        new Thread() {
            public void run() {
                Log.e("tag---->", "dsf");
                WebService.getInstance().isUser(userName, password, new RequestCallback() {

                    @Override
                    public void onSuccess(final String result) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
//                                dialog.dismiss();
                                int flag = parserResult(result);
                                if (flag >= 0) {
                                    Intent intent = new Intent();
                                    intent.setClass(LoginActivity.this, TabTestActivity.class);
                                    startActivity(intent);
                                } else {
                                    showProgress(false);
                                    mUserNameView.setError(getString(R.string.error_incorrect_username_password)); //密码错误
                                    mUserNameView.requestFocus();
                                }
                            }
                        });
                    }

                    @Override
                    public void onFail(final String errorMsg) {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
//                                dialog.dismiss();
//                                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                                showProgress(false);
                                mUserNameView.setError(getString(R.string.error_incorrect_username_password)); //密码错误
                                mUserNameView.requestFocus();
                            }
                        });

                    }
                });
            }
        }.start();
    }

    /**
     * Shows the progress UI and hides the login form.
     * 显示进度条界面，关闭登录窗体
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private int parserResult(String result) {
        if (result.equalsIgnoreCase("用户名或密码为空") || result.equalsIgnoreCase("用户名或密码错误")) {
            return -1;
        } else
            return Integer.parseInt(result);
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     * 显示异步登录注册任务，继承异步任务
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUser;
        private final String mPassword;

        UserLoginTask(String userName, String password) {
            mUser = userName;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.通过网络服务尝试获取授权

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            //验证用户名密码是否正确
//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.如果账户存在且密码匹配成功返回true
//                    return pieces[1].equals(mPassword);
//                }
//            }

            // TODO: register the new account here.注册
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password)); //密码错误
                mPasswordView.requestFocus();
            }
        }

        //取消任务
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

