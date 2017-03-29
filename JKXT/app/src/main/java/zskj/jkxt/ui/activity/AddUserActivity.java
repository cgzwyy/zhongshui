package zskj.jkxt.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import zskj.jkxt.R;


public class AddUserActivity extends AppCompatActivity {

    RadioGroup rgStationList;
    CheckBox mCheckBox;
    String test = "鄯善,托克逊,布尔津,青河,鄯善二期";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        rgStationList = (RadioGroup) this.findViewById(R.id.rgStationList);
        String[] test2 = test.split(",");
        for(int i=0;i<test2.length;i++){
            if(i == 0) {
                mCheckBox = (CheckBox) this.findViewById(R.id.cbstation1);
                mCheckBox.setText(test2[i]);
            }else{
                mCheckBox = new CheckBox(this);
                mCheckBox.setId(i);
                mCheckBox.setText(test2[i]);
                rgStationList.addView(mCheckBox, LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        }

    }
}
