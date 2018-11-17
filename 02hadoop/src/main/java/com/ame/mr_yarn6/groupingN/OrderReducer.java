package com.ame.mr_yarn6.groupingN;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class OrderReducer extends Reducer<OrderBean,Text,OrderBean,Text> {

    @Override
    protected void reduce(OrderBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
       // context.write(key,NullWritable.get());
        int i = 0;
        for (Text value : values) {
            //只输出两次就跳出for循环
            if(i >=2){
                break;
            }
            i++;
            String[] split = value.toString().split("\t");
            context.write(key,new Text(split[2]));
            //判断如果大于或者等于2，就跳出for循环
        }

    }
}
