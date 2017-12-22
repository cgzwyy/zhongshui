package zskj.jkxt.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import zskj.jkxt.R;
import zskj.jkxt.domain.Fan;


/**
 * Created by huanan on 2017/3/6.
 */
public class StationDetailPopu extends PopupWindow {


    TextView fan_detail_speed;
    TextView fan_detail_power;
    TextView fan_detail_revs;
    TextView fan_detail_state;
    Context mContext;

    Fan mFan;


    public StationDetailPopu(Context context) {
        super(context);
        mContext = context;
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable dw = new ColorDrawable(Color.WHITE);
        setBackgroundDrawable(dw);
        setFocusable(true);
        View root = View.inflate(context, R.layout.fan_detail_dialog, null);
        fan_detail_speed = (TextView) root.findViewById(R.id.fan_detail_speed);
        fan_detail_power = (TextView) root.findViewById(R.id.fan_detail_power);
        fan_detail_revs = (TextView) root.findViewById(R.id.fan_detail_revs);
        fan_detail_state = (TextView) root.findViewById(R.id.fan_detail_state);
        setContentView(root);
    }

    public void setFan(Fan fan, int flag) {
        mFan = fan;
        initPage(flag);
    }

    private void initPage(int flag) {
        if(flag == 1){ //光伏
            fan_detail_speed.setText(mContext.getResources().getString(R.string.electricity_unit2 , mFan.fan_speed));
            fan_detail_power.setText(mContext.getResources().getString(R.string.active_power_unit2, mFan.fan_active_power));
            fan_detail_revs.setText(mContext.getResources().getString(R.string.efficiency_unit2, mFan.fan_revs));
            if(mFan.fan_state.equals("1") || mFan.fan_state.equals("1.00")){  //发电
                fan_detail_state.setText(mContext.getResources().getString(R.string.efficiency_state2, "发电"));
            }else{
                fan_detail_state.setText(mContext.getResources().getString(R.string.efficiency_state2, "停用"));
            }

        }else{ //风机
            fan_detail_speed.setText(mContext.getResources().getString(R.string.speed_unit2, mFan.fan_speed));
            fan_detail_power.setText(mContext.getResources().getString(R.string.active_power_unit3, mFan.fan_active_power));
            fan_detail_revs.setText(mContext.getResources().getString(R.string.revs_unit2, mFan.fan_revs));
            if(mFan.fan_state.equals("2") || mFan.fan_state.equals("2.00")){  //发电
                fan_detail_state.setText(mContext.getResources().getString(R.string.efficiency_state, "发电"));
            }else{
                fan_detail_state.setText(mContext.getResources().getString(R.string.efficiency_state, "停用"));
            }
        }
    }


}
