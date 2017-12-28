package zskj.jkxt.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.kelin.scrollablepanel.library.ScrollablePanel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import zskj.jkxt.R;
import zskj.jkxt.ui.widget.StatisticalDataAdapter;

public class StatisticalDataActivity extends Activity {

    DecimalFormat df = new DecimalFormat("0.00");  //数据格式转换，四舍五入，保留两位小数
    ImageView back_jkinfo3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical_data);

        back_jkinfo3 = (ImageView) this.findViewById(R.id.back_jkinfo3);
        back_jkinfo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final ScrollablePanel scrollablePanel = (ScrollablePanel) findViewById(R.id.scrollable_staticstical_data);
        final StatisticalDataAdapter statisticalDataAdapter = new StatisticalDataAdapter();
        generateTestData(statisticalDataAdapter);
        scrollablePanel.setPanelAdapter(statisticalDataAdapter);
    }

    private void generateTestData(StatisticalDataAdapter statisticalDataAdapter) {
        List<String> stationInfoList = new ArrayList<>(); //行数据
        for (int i = 0; i < 20; i++) {
            stationInfoList.add("统计数据" + i);
        }
        statisticalDataAdapter.setStationInfoList(stationInfoList);

        List<String> colInfoList = new ArrayList<>();  //列数据
        for (int i = 0; i < 6; i++) {
            colInfoList.add("乐平" + i);
        }
        statisticalDataAdapter.setColInfoList(colInfoList);

        List<List<String>> dataInfoList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            List<String> tmpDataList = new ArrayList<>();
            for(int j=0;j< 20; j++) {
                tmpDataList.add(df.format(Math.random() * (20 + j)) + "");
            }
            dataInfoList.add(tmpDataList);
        }
        statisticalDataAdapter.setDataInfoList(dataInfoList);
    }
}
