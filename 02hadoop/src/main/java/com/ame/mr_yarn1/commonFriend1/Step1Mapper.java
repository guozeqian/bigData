package com.ame.mr_yarn1.commonFriend1;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


public class Step1Mapper  extends Mapper<LongWritable,Text,Text,Text>{

    /**
     * A:B,C,D,F,E,O
     * @param key
     * @param value
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split(":");
        String[] split1 = split[1].split(",");
        for (String friend : split1) {
            context.write(new Text(friend),new Text(split[0]));

        }


    }
}
