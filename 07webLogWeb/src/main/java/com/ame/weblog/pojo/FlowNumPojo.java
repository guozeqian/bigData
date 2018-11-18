package com.ame.weblog.pojo;

public class FlowNumPojo {

/*
  `id` int(11) DEFAULT NULL,
  `dateStr` varchar(255) DEFAULT NULL,
  `pVNum` int(11) DEFAULT NULL,
  `uVNum` int(11) DEFAULT NULL,
  `iPNum` int(11) DEFAULT NULL,
  `newUvNum` int(11) DEFAULT NULL,
  `visitNum` int(11) DEFAULT NULL*/

    private Integer id;
    private String dateStr;
    private String pVNum;
    private String uVNum;
    private String iPNum;
    private String newUvNum;
    private String visitNum;


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

    public String getpVNum() {
        return pVNum;
    }

    public void setpVNum(String pVNum) {
        this.pVNum = pVNum;
    }

    public String getuVNum() {
        return uVNum;
    }

    public void setuVNum(String uVNum) {
        this.uVNum = uVNum;
    }

    public String getiPNum() {
        return iPNum;
    }

    public void setiPNum(String iPNum) {
        this.iPNum = iPNum;
    }

    public String getNewUvNum() {
        return newUvNum;
    }

    public void setNewUvNum(String newUvNum) {
        this.newUvNum = newUvNum;
    }

    public String getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(String visitNum) {
        this.visitNum = visitNum;
    }
}
