package com.ame.mr_yarn6.groupingN;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 定义我们key2的类型
 */
public class OrderBean implements WritableComparable<OrderBean> {
   private String orderId;
   private Double price;


    @Override
    public String toString() {
        return orderId + "\t"+price;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * 排序，按照我们的金额来进行排序，
     * 首先比较我们的roderid，如果orderId不相同，需不需要比较价格
     * @param o
     * @return
     */
    @Override
    public int compareTo(OrderBean o) {
        //比较订单的id是否相同
        int i = this.orderId.compareTo(o.orderId);
        if(i == 0 ){
            //如果两个订单的id相同了，才有比较价格的必要
            i = this.price.compareTo(o.price);
        }
        return -i;
    }

    /**
     * 序列化的方法
     * @param out
     * @throws IOException
     */
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(orderId);
        out.writeDouble(price);
    }

    /**
     * 反序列化的方法
     * @param in
     * @throws IOException
     */
    @Override
    public void readFields(DataInput in) throws IOException {
        this.orderId = in.readUTF();
        this.price = in.readDouble();

    }
}
