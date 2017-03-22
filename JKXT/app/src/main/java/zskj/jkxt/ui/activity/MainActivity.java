package zskj.jkxt.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import zskj.jkxt.R;
import zskj.jkxt.ui.fragment.WarnFragment;
import zskj.jkxt.ui.fragment.PowerFragment;
import zskj.jkxt.ui.fragment.SettingFragment;
import zskj.jkxt.ui.fragment.StationFragment;

/**
 * @author huanan
 *         首页
 */
public class MainActivity extends FragmentActivity {

    private StationFragment mStationFrag;
    private PowerFragment mPowerFrag;
    private WarnFragment mWarnFrag;
    private SettingFragment mSetFrag;
    //当前fragment
    private Fragment currentFrag;
    //manager
    FragmentManager manager;
    //view
    RadioGroup rg_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initViews();//初始化控件
        initData();
    }

    private void initData() {
        manager = getSupportFragmentManager();
        rg_menu.check(R.id.rb_station);
    }

    private void initViews() {
        rg_menu = (RadioGroup) this.findViewById(R.id.rg_menu);
        rg_menu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                FragmentTransaction transaction = manager.beginTransaction();
                if (currentFrag != null)
                    transaction.hide(currentFrag);
                switch (i) {
                    case R.id.rb_station:
                        if (mStationFrag == null) {
                            mStationFrag = new StationFragment();
                            transaction.add(R.id.fl_container, mStationFrag, "station");
                        } else {
                            transaction.show(mStationFrag);
                        }
                        currentFrag = mStationFrag;
                        break;
                    case R.id.rb_power:
                        if (mPowerFrag == null) {
                            mPowerFrag = new PowerFragment();
                            transaction.add(R.id.fl_container, mPowerFrag, "power");
                        } else {
                            transaction.show(mPowerFrag);
                        }
                        currentFrag = mPowerFrag;
                        break;
                    case R.id.rb_warn:
                        if (mWarnFrag == null) {
                            mWarnFrag = new WarnFragment();
                            transaction.add(R.id.fl_container, mWarnFrag, "warn");
                        } else {
                            transaction.show(mWarnFrag);
                        }
                        currentFrag = mWarnFrag;
                        break;
                    case R.id.rb_set:
                        if (mSetFrag == null) {
                            mSetFrag = new SettingFragment();
                            transaction.add(R.id.fl_container, mSetFrag, "set");
                        } else {
                            transaction.show(mSetFrag);
                        }
                        currentFrag = mSetFrag;
                        break;
                }
                transaction.commit();
            }
        });
    }
}
