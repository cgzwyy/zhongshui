package zskj.jkxt.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WYY on 2018/1/2.
 */

public class JKInfo implements Serializable {
    private static final long serialVersionUID = -2430426435250995216L;
    public String jk_jrfd;//今日发电
    public String jk_jryg;//今日有功
    public String jk_ljfd;//累计发电
    public String jk_jhfd;//计划发电
    public Map<String,String> jk_rfdl;//日发电量，柱状图数据
    public Map<String,String> jk_pie_data;//饼图数据

    public JKInfo(){
        this.jk_jrfd = "0.00";
        this.jk_jryg = "0.00";
        this.jk_ljfd = "0.00";
        this.jk_jhfd = "0.00";
        this.jk_rfdl = new HashMap<>();
        this.jk_pie_data = new HashMap<>();
    }
}
