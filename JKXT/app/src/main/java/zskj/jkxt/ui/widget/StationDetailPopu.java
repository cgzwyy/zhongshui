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
        setContentView(root);
    }

    public void setFan(Fan fan, int flag) {
        mFan = fan;
        initPage(flag);
    }

    private void initPage(int flag) {
        if(flag == 1){
            fan_detail_speed.setText(mContext.getResources().getString(R.string.electricity_unit, mFan.fan_speed));
            fan_detail_power.setText(mContext.getResources().getString(R.string.active_power_unit, mFan.fan_active_power));
            fan_detail_revs.setText(mContext.getResources().getString(R.string.efficiency_unit, mFan.fan_revs));
        }else{
            fan_detail_speed.setText(mContext.getResources().getString(R.string.speed_unit, mFan.fan_speed));
            fan_detail_power.setText(mContext.getResources().getString(R.string.active_power_unit, mFan.fan_active_power));
            fan_detail_revs.setText(mContext.getResources().getString(R.string.revs_unit, mFan.fan_revs));
        }
    }


}
