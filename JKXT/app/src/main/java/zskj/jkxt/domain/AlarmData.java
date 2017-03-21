package zskj.jkxt.domain;

import java.io.Serializable;

/**
 * Created by WYY on 2017/3/17.
 */

public class AlarmData implements Serializable {

    private static final long serialVersionUID = -2430426435250995216L;

    public String alarm_num;//编号
    public String alarm_type;//类型名
    public String alarm_date;//日期
    public String alarm_time;//时间
    public String alarm_content;//事项内容
}
