package com.ame.weblog.pojo;

import java.util.List;

public class FlowReturnPojo {

    private List<String> dates;
    private List<String> uvs;
    private List<String> new_uvs;

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public List<String> getUvs() {
        return uvs;
    }

    public void setUvs(List<String> uvs) {
        this.uvs = uvs;
    }

    public List<String> getNew_uvs() {
        return new_uvs;
    }

    public void setNew_uvs(List<String> new_uvs) {
        this.new_uvs = new_uvs;
    }
}
