package zskj.jkxt.domain;

import java.io.Serializable;

/**
 * Created by WYY on 2017/12/22.
 */

public class StationInfo implements Serializable {

    private static final long serialVersionUID = -2430426435250995216L;
    public String stationType;//厂站类型
    public String stationName;//场站名称
    public String elecValue;//今日电量

//    public static boolean isEmpty(Station station) {
//        if (station.columnValue == null || station.columnValue.length() == 0) {
//            return true;
//        }
//        return false;
//    }
}
