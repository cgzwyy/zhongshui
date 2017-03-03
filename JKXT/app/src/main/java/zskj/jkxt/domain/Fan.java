package zskj.jkxt.domain;

import java.io.Serializable;

/**
 * Created by WYY on 2017/3/1.
 */

public class Fan implements Serializable {



    /**
     *
     */
    private static final long serialVersionUID = -2430426435250995216L;

    public String fan_number;//风机编号
    public String fan_speed;//风速
    public String fan_active_power;//有功功率
    public String fan_revs;//转速
    public String fan_state;//状态

}
