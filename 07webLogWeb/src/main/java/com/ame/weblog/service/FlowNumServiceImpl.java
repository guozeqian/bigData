package com.ame.weblog.service;

import com.ame.weblog.mapper.FlowNumMapper;
import com.ame.weblog.pojo.FlowNumPojo;
import com.ame.weblog.pojo.FlowReturnPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FlowNumServiceImpl implements FlowNumService {

    @Autowired
    private FlowNumMapper flowNumMapper;


    @Override
    public FlowReturnPojo getAllFlowNum() {
        FlowReturnPojo flowReturnPojo = new FlowReturnPojo();
         List<String> dates = new ArrayList<String>();
         List<String> uvs = new ArrayList<String>();
         List<String> new_uvs = new ArrayList<String>();


        List<FlowNumPojo> flowNumList = flowNumMapper.getAllFlowNum();
        for (FlowNumPojo flowNumPojo : flowNumList) {
            dates.add(flowNumPojo.getDateStr());
            uvs.add(flowNumPojo.getuVNum());
            new_uvs.add(flowNumPojo.getNewUvNum());

        }
        flowReturnPojo.setDates(dates);
        flowReturnPojo.setNew_uvs(new_uvs);
        flowReturnPojo.setUvs(uvs);

        return flowReturnPojo;
    }
}
