package zskj.jkxt.domain;

import java.io.Serializable;

/**
 * Created by WYY on 2017/2/28.
 */

public class Station implements Serializable {

    private static final long serialVersionUID = -2430426435250995216L;
    public String columnAddress;//位置
    public String columnName;//厂站类型
    public String columnValue;//编号
    public String stationElec;//日发电量

    public Station(){
        this.stationElec = "0.00";
    }

    public void setStationElec(String stationElec){
        this.stationElec = stationElec;
    }

    public static boolean isEmpty(Station station){
        if(station.columnValue == null || station.columnValue.length() == 0){
            return true;
        }
        return false;
    }
}
