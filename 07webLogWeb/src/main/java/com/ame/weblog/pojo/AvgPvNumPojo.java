package com.ame.weblog.pojo;

public class AvgPvNumPojo {


    private Integer  id;
    private String dateStr;
    private String avgPvNum;

    /*id  int(11) NULL
    dateStr varchar(255) NULL  hello
    avgPvNum    decimal(6,2) NULL*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getAvgPvNum() {
        return avgPvNum;
    }

    public void setAvgPvNum(String avgPvNum) {
        this.avgPvNum = avgPvNum;
    }
}
