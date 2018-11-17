package com.ame.mr_yarn2.index;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class IndexMapper extends Mapper<LongWritable,Text,Text,IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //获取我们文件的切片，获取到了这个切片就可以知道我们的数据是哪个文件里面来的
        FileSplit inputSplit = (FileSplit) context.getInputSplit();
        //获取我们的文件名 a.txt   b.txt   c.txt  等
        String fileName = inputSplit.getPath().getName();

        String[] split = value.toString().split(" ");
        for (String word : split) {
            context.write(new Text(word+"-"+fileName),new IntWritable(1));
        }
    }
}
