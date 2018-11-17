package com.ame.mr1.partition;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class PartitionerOwn extends Partitioner<Text,NullWritable> {
    /**
     * 这个方法就是我们自己来定义如何分区
     * @param text  key2那一行数据
     * @param nullWritable  v2
     * @param i  reduceTask的数量
     * @return
     */
    @Override
    public int getPartition(Text text, NullWritable nullWritable, int i) {
        String[] split = text.toString().split("\t");
        String gameResult = split[5];
       if(null != gameResult && "" != gameResult){
           //判断如果开奖结果大于15，那么这些大于15的数据，都去到0号分区里面
           if(Integer.parseInt(gameResult) >15){
               return 0;
           }else{
               //开奖结果小于等于15的数据，都去往一号分区里面去
               return 1;
           }
       }
        return 0;
    }
}
