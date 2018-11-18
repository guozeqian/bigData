package com.ame.weblog.controller;

import com.ame.weblog.pojo.AvgReturnPojo;
import com.ame.weblog.pojo.FlowReturnPojo;
import com.ame.weblog.service.AvgNumService;
import com.ame.weblog.service.FlowNumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private AvgNumService avgNumService;

    @RequestMapping("/index.action")
    public String skipToIndex() {
        return "index";
    }


    @RequestMapping("/avgPvNum.action")
    @ResponseBody
    public AvgReturnPojo getAvgNum() {

        AvgReturnPojo returnPojo = avgNumService.getAvgReturnPojo();


        return returnPojo;
    }

    @Autowired
    private FlowNumService flowNumService;


    @RequestMapping("/flowNum.action")
    @ResponseBody
    public FlowReturnPojo getFlowNum() {
        FlowReturnPojo flowReturnPojo = flowNumService.getAllFlowNum();


        return flowReturnPojo;
    }


 }
