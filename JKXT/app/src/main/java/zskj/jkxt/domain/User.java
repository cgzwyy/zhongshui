package zskj.jkxt.domain;

import java.io.Serializable;

/**
 * Created by WYY on 2017/3/29.
 */

public class User implements Serializable {

    private static final long serialVersionUID = -2430426435250995216L;

    public int userId;//编号
    public String userName;//类型名
    public String userPassword;//日期
    public String userRights;//时间
    public String userRange;//事项内容
    public String userLevel;//事项内容

}
