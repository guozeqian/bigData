package com.ame.mr_yarn6.groupingN;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class OrderIdWritableComparator extends WritableComparator {

    /**
     * 覆写我们无参构造器，然后指定我们的框架反射出来的两个比较的类型是
     * 哪一个java类
     */
    public OrderIdWritableComparator(){
        super(OrderBean.class,true);

    }



    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        OrderBean first = (OrderBean) a;
        OrderBean second = (OrderBean)b ;
        //比较两个orderId是否相同，如果相同，就会把相同的orderId的数据弄到一个集合里面去
        return first.getOrderId().compareTo(second.getOrderId());

      //  return super.compare(a, b);
    }
}
