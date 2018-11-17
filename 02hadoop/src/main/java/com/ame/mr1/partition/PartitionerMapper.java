package com.ame.mr1.partition;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class PartitionerMapper extends Mapper<LongWritable,Text,Text,NullWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        /**
         * 通过NullWritable.get()  获取一个空值
         */
        context.write(value,NullWritable.get());

    }
}
