package com.ame.weblog.service;

import com.ame.weblog.mapper.AvgNumMapper;
import com.ame.weblog.pojo.AvgPvNumPojo;
import com.ame.weblog.pojo.AvgReturnPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AvgNumServiceImpl implements AvgNumService {

    @Autowired
    private AvgNumMapper avgNumMapper;


    @Override
    public AvgReturnPojo getAvgReturnPojo() {
        AvgReturnPojo avgReturnPojo = new AvgReturnPojo();
        ArrayList<String> dates = new ArrayList<String>();
        ArrayList<String> data  = new ArrayList<String>();


        List<AvgPvNumPojo> avgPvNumPojoList = avgNumMapper.getAllAvgNum();
        for (AvgPvNumPojo avgPvNumPojo : avgPvNumPojoList) {
            dates.add(avgPvNumPojo.getDateStr());
            data.add(avgPvNumPojo.getAvgPvNum());
        }

        avgReturnPojo.setData(data);
        avgReturnPojo.setDates(dates);

        return avgReturnPojo;
    }
}
