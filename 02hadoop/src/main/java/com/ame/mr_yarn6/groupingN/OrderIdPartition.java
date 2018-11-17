package com.ame.mr_yarn6.groupingN;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class OrderIdPartition extends Partitioner<OrderBean,Text> {

    /**
     * 自定义我们的分区规则，按照我们的orderId来进行分区，相同的orderId发送到同一个reduce里面去
     *
     * return (key.hashCode() & Integer.MAX_VALUE) % numReduceTasks;
     *
     * @param orderBean   就是我们的key2
     * @param text  就是我们的value2
     * @param numReduceTasks 我们reduce的个数
     * @return
     */
    @Override
    public int getPartition(OrderBean orderBean, Text text, int numReduceTasks) {

        //相同的orderId算出来的分区号一定是一样的，这样就可以保证相同的orderId的数据发送到同一个reduce里面去
       int partition =  ( orderBean.getOrderId().hashCode() & Integer.MAX_VALUE) %numReduceTasks;

        return partition;
    }
}
