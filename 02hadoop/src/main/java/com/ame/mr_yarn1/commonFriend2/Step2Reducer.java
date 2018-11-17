package com.ame.mr_yarn1.commonFriend2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Step2Reducer extends Reducer<Text,Text,Text,Text> {

    /**
     * reduce阶段接收数据格式如下   A-E  <B,D >
     * @param key
     * @param values
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        StringBuffer buffer  = new StringBuffer();
        for (Text value : values) {
            buffer.append(value.toString()+"-");
        }
        //输出数据格式如下  A-E   B-D-
        context.write(key,new Text(buffer.toString()));
    }
}
