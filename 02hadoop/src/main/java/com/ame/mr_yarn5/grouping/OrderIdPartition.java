package com.ame.mr_yarn5.grouping;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class OrderIdPartition extends Partitioner<OrderBean,NullWritable> {

    /**
     * 自定义我们的分区规则，按照我们的orderId来进行分区，相同的orderId发送到同一个reduce里面去
     *
     * return (key.hashCode() & Integer.MAX_VALUE) % numReduceTasks;
     *
     * @param orderBean   就是我们的key2
     * @param nullWritable  就是我们的value2
     * @param numReduceTasks 我们reduce的个数
     * @return
     */
    @Override
    public int getPartition(OrderBean orderBean, NullWritable nullWritable, int numReduceTasks) {

        //相同的orderId算出来的分区号一定是一样的，这样就可以保证相同的orderId的数据发送到同一个reduce里面去
       int partition =  ( orderBean.getOrderId().hashCode() & Integer.MAX_VALUE) %numReduceTasks;

        return partition;
    }
}
