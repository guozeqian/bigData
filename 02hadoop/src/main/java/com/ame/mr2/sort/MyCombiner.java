package com.ame.mr2.sort;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 注意自定义combiner的话，我们这里的输入类型以及输出类型，都是我们的key2  value2
 * 可以减少我们输出到reduce的key2的个数
 */
public class MyCombiner extends Reducer<PairSort,Text,PairSort,Text> {

    @Override
    protected void reduce(PairSort key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        //这里面写我们规约的逻辑
        for (Text value : values) {
            context.write(key,value);
        }

    }
}
