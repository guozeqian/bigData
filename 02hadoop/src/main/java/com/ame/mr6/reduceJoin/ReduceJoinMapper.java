package com.ame.mr6.reduceJoin;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class ReduceJoinMapper extends Mapper<LongWritable,Text,Text,Text> {

    Text text = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
       //可以判断数据是从哪个文件里面来的
        //获取文件的切片
        FileSplit inputSplit = (FileSplit) context.getInputSplit();
        //获取到了我们的文件名
        String name = inputSplit.getPath().getName();


        String s = value.toString();
        if(s.startsWith("p")){
            //表示我们拿到的是商品表的数据
            /**
             * p0001,小米5,1000,2000
             p0002,锤子T1,1000,3000
             */
            String[] split = s.split(",");
            text.set(split[0]);
            context.write(text,value);


        }else{
            //这个文件是订单表的数据
            /**
             * 1001,20150710,p0001,2
             1002,20150710,p0002,3
             1002,20150710,p0003,3
             */
            String[] split = s.split(",");
            text.set(split[2]);
            context.write(text,value);


        }



    }
}
