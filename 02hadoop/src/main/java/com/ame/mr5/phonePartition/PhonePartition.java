package com.ame.mr5.phonePartition;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class PhonePartition extends Partitioner<Text,FlowBean> {

    /**
     * 判断我们的手机号以什么数字开头
     *
     * @param text
     * @param flowBean
     * @param i
     * @return
     */
    @Override
    public int getPartition(Text text, FlowBean flowBean, int i) {
        String phone = text.toString();
        if (phone.startsWith("135")){
            return 0;
        }else if(phone.startsWith("136")){
            return 1;
        }else if (phone.startsWith("137")){
            return 2;
        }else if(phone.startsWith("138")){
            return 3;
        }else if(phone.startsWith("139")){
            return 4;
        }else{
            return 5;
        }

    }
}
