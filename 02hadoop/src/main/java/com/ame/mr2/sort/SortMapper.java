package com.ame.mr2.sort;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class SortMapper extends Mapper<LongWritable,Text,PairSort,Text> {

    /**
     * 自定义map阶段，封装我们自定义的key2,然后对key2做排序
     * @param key
     * @param value
     * @param context
     * @throws IOException
     * @throws InterruptedException
    a	1
    a	9
    b	3
    a	7
    b	8
    b	10
    a	5
    a	9
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
      //通过context可以获取到我们的计数器
        Counter counter = context.getCounter("MAP_COUNTER", "MAP_INPUT_RECORDS");
        //统计我们map阶段输入了多少条数据
        counter.increment(1L);

        PairSort pairSort = new PairSort();
        String[] split = value.toString().split("\t");
        pairSort.setFirst(split[0]);
        pairSort.setSecond(Integer.parseInt(split[1]));
        //我们定义的泛型key2  value 是pairSort  Text
        context.write(pairSort,value);
    }
}
