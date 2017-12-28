package zskj.jkxt.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import zskj.jkxt.R;
import zskj.jkxt.ui.fragment.JKInfoFragment;
import zskj.jkxt.ui.fragment.PowerFragment;
import zskj.jkxt.ui.fragment.UserManagementFragment;
import zskj.jkxt.ui.fragment.WarnFragment;

/**
 * @author huanan
 *         首页
 */
public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
//    private StationFragment mStationFrag;
    private JKInfoFragment mJKInfoFragment;
    private PowerFragment mPowerFrag;
    private WarnFragment mWarnFrag;
    private UserManagementFragment mSetFrag;
    //当前fragment
    private Fragment currentFrag;
    //manager
    FragmentManager manager;
    //view
    RadioGroup rg_menu;
    RadioButton rb_station, rb_power, rb_warn, rb_set;
    String rights, ranges, level;
    int tabId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        rights = getIntent().getStringExtra("rights");
        ranges = getIntent().getStringExtra("ranges");
        level = getIntent().getStringExtra("level");

        initViews();//初始化控件
        initData();
    }

    private void initData() {
        manager = getSupportFragmentManager();
        rg_menu.check(tabId);
    }

    private void initViews() {
        rg_menu = (RadioGroup) this.findViewById(R.id.rg_menu);
        rb_station = (RadioButton) this.findViewById(R.id.rb_station);
        rb_power = (RadioButton) this.findViewById(R.id.rb_power);
        rb_warn = (RadioButton) this.findViewById(R.id.rb_warn);
        rb_set = (RadioButton) this.findViewById(R.id.rb_set);

        //优先显示，逆序排列
        if (!TextUtils.isEmpty(rights)) {
            if (rights.contains(rb_warn.getText().toString())) {
                rb_warn.setVisibility(View.VISIBLE);
                tabId = R.id.rb_warn;
            }
            if (rights.contains(rb_power.getText().toString())) {
                rb_power.setVisibility(View.VISIBLE);
                tabId = R.id.rb_power;
            }
            if (rights.contains(rb_station.getText().toString())) {
                rb_station.setVisibility(View.VISIBLE);
                tabId = R.id.rb_station;
            }
        }
        if (!TextUtils.isEmpty(level) && level.equals("1")) {
            rb_set.setVisibility(View.VISIBLE);
        }

        rg_menu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                FragmentTransaction transaction = manager.beginTransaction();
                if (currentFrag != null)
                    transaction.hide(currentFrag);
                switch (i) {
                    case R.id.rb_station:
//                        if (mStationFrag == null) {
//                            mStationFrag = new StationFragment();
//                            mStationFrag.setRanges(ranges);
//                            transaction.add(R.id.fl_container, mStationFrag, "station");
//                        } else {
//                            transaction.show(mStationFrag);
//                        }
                        if (mJKInfoFragment == null) {
                            mJKInfoFragment = new JKInfoFragment();
//                            mJKInfoFragment.setRanges(ranges);
                            transaction.add(R.id.fl_container, mJKInfoFragment, "station");
                        } else {
                            transaction.show(mJKInfoFragment);
                        }
                        rb_station.setTextColor(getResources().getColor(R.color.text_color));
                        rb_power.setTextColor(getResources().getColor(R.color.black));
                        rb_warn.setTextColor(getResources().getColor(R.color.black));
                        rb_set.setTextColor(getResources().getColor(R.color.black));
                        currentFrag = mJKInfoFragment;
                        break;
                    case R.id.rb_power:
                        if (mPowerFrag == null) {
                            mPowerFrag = PowerFragment.getInstance(ranges);
                            transaction.add(R.id.fl_container, mPowerFrag, "power");
                        } else {
                            transaction.show(mPowerFrag);
                        }
                        rb_station.setTextColor(getResources().getColor(R.color.black));
                        rb_power.setTextColor(getResources().getColor(R.color.text_color));
                        rb_warn.setTextColor(getResources().getColor(R.color.black));
                        rb_set.setTextColor(getResources().getColor(R.color.black));
                        currentFrag = mPowerFrag;
                        break;
                    case R.id.rb_warn:
                        if (mWarnFrag == null) {
                            mWarnFrag = WarnFragment.getInstance(ranges, level);
                            transaction.add(R.id.fl_container, mWarnFrag, "warn");
                        } else {
                            transaction.show(mWarnFrag);
                        }
                        rb_station.setTextColor(getResources().getColor(R.color.black));
                        rb_power.setTextColor(getResources().getColor(R.color.black));
                        rb_warn.setTextColor(getResources().getColor(R.color.text_color));
                        rb_set.setTextColor(getResources().getColor(R.color.black));
                        currentFrag = mWarnFrag;
                        break;
                    case R.id.rb_set:
                        if (mSetFrag == null) {
                            mSetFrag = new UserManagementFragment();
                            transaction.add(R.id.fl_container, mSetFrag, "set");
                        } else {
                            transaction.show(mSetFrag);
                        }
                        rb_station.setTextColor(getResources().getColor(R.color.black));
                        rb_power.setTextColor(getResources().getColor(R.color.black));
                        rb_warn.setTextColor(getResources().getColor(R.color.black));
                        rb_set.setTextColor(getResources().getColor(R.color.text_color));
                        currentFrag = mSetFrag;
                        break;
                }
                transaction.commit();
            }
        });
    }

    //重要，不要删
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "onConfigurationChanged");
    }
}
