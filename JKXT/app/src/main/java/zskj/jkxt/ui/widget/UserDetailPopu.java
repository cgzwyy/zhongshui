package zskj.jkxt.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import zskj.jkxt.R;
import zskj.jkxt.domain.User;

/**
 * Created by WYY on 2017/3/30.
 */

public class UserDetailPopu extends PopupWindow {


    TextView dialog_userName;
    TextView dialog_userRights;
    TextView dialog_userRange;
    TextView dialog_userLevel;
    Context mContext;

    User mUser;


    public UserDetailPopu(Context context) {
        super(context);
        mContext = context;
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable dw = new ColorDrawable(Color.WHITE);
        setBackgroundDrawable(dw);
        setFocusable(true);
        View root = View.inflate(context, R.layout.user_detail_dialog, null);
        dialog_userName = (TextView) root.findViewById(R.id.dialog_userName);
        dialog_userRights = (TextView) root.findViewById(R.id.dialog_userRights);
        dialog_userRange = (TextView) root.findViewById(R.id.dialog_userRange);
        dialog_userLevel = (TextView) root.findViewById(R.id.dialog_userLevel);
        setContentView(root);
    }

    public void setUser(User user) {
        mUser = user;
        initPage();
    }

    private void initPage() {
        dialog_userName.setText(mUser.userName);
        dialog_userRights.setText(mUser.userRights);
        dialog_userRange.setText(mUser.userRange);
        if(mUser.userLevel.equals("1"))
            dialog_userLevel.setText("超级用户");
        else
            dialog_userLevel.setText("普通用户");
    }


}