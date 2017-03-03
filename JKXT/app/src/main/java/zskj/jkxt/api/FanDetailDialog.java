package zskj.jkxt.api;

import android.app.Dialog;
import android.content.Context;

import zskj.jkxt.domain.Fan;

/**
 * Created by WYY on 2017/3/2.
 */

public class FanDetailDialog extends Dialog {
    private Fan mFan;

    public FanDetailDialog(Context context) {
        super(context);
    }

    public Fan getFan() {
        return mFan;
    }

    public void setFan(Fan fan) {
        mFan = fan;
    }

}
