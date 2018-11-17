package com.ame.mr6.reduceJoin;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class ReduceJoinReducer extends Reducer<Text,Text,Text,Text> {

    /**
     * 商品表数据
     pid
     p0001,小米5,1000,2000
     p0002,锤子T1,1000,3000

     订单表数据
     pid
     1001,20150710,p0001,2
     1002,20150710,p0002,3
     1002,20150710,p0003,3
     * @param key
     * @param values
     * @param context
     * @throws IOException
     * @throws InterruptedException
     *
     * p0001     p0001,小米5,1000,2000 1001,20150710,p0001,2
     * p0002     p0002,锤子T1,1000,3000      1002,20150710,p0002,3
     * p0003     1002,20150710,p0003,3
     */
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        String firstPart  ="";
        String secondPart = "";
        for (Text value : values) {
            if(value.toString().startsWith("p")){
                firstPart = value.toString();
            }else{
                secondPart = value.toString();
            }
        }
        context.write(key,new Text(firstPart+"\t"+secondPart));
    }
}
