package com.ame.mr2.sort;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class SortReducer extends Reducer<PairSort,Text,PairSort,NullWritable> {

    public static enum  Counter{
        REDUCE_INPUT_KEY_TOAL,
        REDUCE_INPUT_VALUE_TOAL


    }


    /*
    集合当中有一个集合是这样的   (pairSort  <a,9   a,9>)
     */
    @Override
    protected void reduce(PairSort key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        context.getCounter(Counter.REDUCE_INPUT_KEY_TOAL).increment(1L);
        for (Text value : values) {
            context.getCounter(Counter.REDUCE_INPUT_VALUE_TOAL).increment(1L);
            context.write(key,NullWritable.get());
        }

    }
}
