package zskj.jkxt.ui.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.kelin.scrollablepanel.library.ScrollablePanel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import zskj.jkxt.R;
import zskj.jkxt.WebService;
import zskj.jkxt.ui.widget.StatisticalDataAdapter;

public class StatisticalDataActivity extends Activity {

    DecimalFormat df = new DecimalFormat("0.00");  //数据格式转换，四舍五入，保留两位小数
    ImageView back_jkinfo3;
    String ranges;
    GetStatisticalDataTask mTask;
    List<String> colInfoList = new ArrayList<>();
    List<String> stationInfoList = new ArrayList<>();
    List<List<String>> dataInfoList = new ArrayList<>();
    StatisticalDataAdapter statisticalDataAdapter;
    ScrollablePanel scrollablePanel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical_data);
        ranges = getIntent().getStringExtra("ranges");

        back_jkinfo3 = (ImageView) this.findViewById(R.id.back_jkinfo3);
        back_jkinfo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        scrollablePanel = (ScrollablePanel) findViewById(R.id.scrollable_staticstical_data);
        statisticalDataAdapter = new StatisticalDataAdapter();
        generateTestData();
        scrollablePanel.setPanelAdapter(statisticalDataAdapter);
    }

    private void generateTestData() {
//        stationInfoList.clear(); //行数据
//        for (int i = 0; i < 20; i++) {
//            stationInfoList.add("统计数据" + i);
//        }
//        statisticalDataAdapter.setStationInfoList(stationInfoList);

//        colInfoList.clear();  //列数据
//        for (int i = 0; i < 6; i++) {
//            colInfoList.add("乐平" + i);
//        }
//        statisticalDataAdapter.setColInfoList(colInfoList);

//        dataInfoList.clear();
//        for (int i = 0; i < 6; i++) {
//            List<String> tmpDataList = new ArrayList<>();
//            for(int j=0;j< 20; j++) {
//                tmpDataList.add(df.format(Math.random() * (20 + j)) + "");
//            }
//            dataInfoList.add(tmpDataList);
//        }
//        statisticalDataAdapter.setDataInfoList(dataInfoList);

        if (mTask != null)
            return;
        mTask = new GetStatisticalDataTask();
        mTask.execute();

    }

    private class GetStatisticalDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgress(true);
        }

        @Override
        protected String doInBackground(Void... voids) {
//            Log.e("ranges","-------->"+ranges);
            return WebService.getInstance().getStatisticalData(ranges);
        }

        @Override
        protected void onPostExecute(String result) {
            mTask = null;
//            showProgress(false);
            dealResult(result);
        }

        @Override
        protected void onCancelled() {
            mTask = null;
//            showProgress(false);
        }
    }

    private void dealResult(String result) {
        try {
            if(result != null && result.toString() != null){
                JSONObject obj = new JSONObject(result);
                int code = obj.optInt("code");
                if (code == 0) {
                    String msg = obj.optString("msg");
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                //JSONObject data = obj.optJSONObject("data");
                //JSONArray userLists = data.optJSONArray("userlist");
                stationInfoList.clear(); //行数据
                String[] types = obj.optString("stationInfo").split(",");
                for(int i=0;i<types.length;i++){
                    stationInfoList.add(types[i]);
                }

                JSONObject data = obj.optJSONObject("data");
                colInfoList.clear(); //列数据
                dataInfoList.clear(); //值
                Iterator<String> iterator = data.keys();
                while(iterator.hasNext()){
                    String key = iterator.next();
                    colInfoList.add(key);
                    String[] values = data.getString(key).split(",");
                    Map<String,String> tmpMap = new HashMap<>();
                    for(int i=0;i<values.length;i++){
                        String[] detail = values[i].split("\"");
                        tmpMap.put(detail[1],detail[3]);
                    }

                    List<String> tmpDataList = new ArrayList<>();
                    for(int i=0;i<stationInfoList.size();i++){
                        if(tmpMap.containsKey(stationInfoList.get(i))){
                            tmpDataList.add(df.format(Double.valueOf(tmpMap.get(stationInfoList.get(i)))));
                        }else{
                            tmpDataList.add("0.00");
                        }
                    }
                    dataInfoList.add(tmpDataList);
                }
                statisticalDataAdapter.setColInfoList(colInfoList);

//                for (int i = 0; i < 20; i++) {
//                    stationInfoList.add("统计数据" + i);
//                }
                statisticalDataAdapter.setStationInfoList(stationInfoList);

//                for (int i = 0; i < colInfoList.size(); i++) {
//                    List<String> tmpDataList = new ArrayList<>();
//                    for(int j=0;j< stationInfoList.size(); j++) {
//                        tmpDataList.add(df.format(Math.random() * (20 + j)) + "");
//                    }
//                    dataInfoList.add(tmpDataList);
//                }
                statisticalDataAdapter.setDataInfoList(dataInfoList);
                scrollablePanel.setPanelAdapter(statisticalDataAdapter);

            }else{
                Toast.makeText(getApplicationContext(), "获取运行统计数据失败1", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "获取运行统计数据失败2", Toast.LENGTH_SHORT).show();
        }
    }
}
