package com.ame.mr4.sortUpCountFlow;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlowSortMapper extends Mapper<LongWritable,Text,FlowBeanSort,Text> {


    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
       //13480253104	3	3	180	180
        String[] split = value.toString().split("\t");
        FlowBeanSort flowBeanSort = new FlowBeanSort();
        flowBeanSort.setUpFlow(Integer.parseInt(split[1]));
        flowBeanSort.setDownFlow(Integer.parseInt(split[2]));
        flowBeanSort.setUpCountFlow(Integer.parseInt(split[3]));
        flowBeanSort.setDownCountFlow(Integer.parseInt(split[4]));
        context.write(flowBeanSort,new Text(split[0]));

    }
}
