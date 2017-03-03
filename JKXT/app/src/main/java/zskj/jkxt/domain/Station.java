package zskj.jkxt.domain;

import java.io.Serializable;

/**
 * Created by WYY on 2017/2/28.
 */

public class Station implements Serializable {



    /**
     *
     */
    private static final long serialVersionUID = -2430426435250995216L;


    public String columnAddress;//位置
    public String columnName;//厂站类型
    public String columnValue;//编号

    public String getColumnAddress() {
        return columnAddress;
    }

    public void setColumnAddress(String columnAddress) {
        this.columnAddress = columnAddress;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue;
    }
}
