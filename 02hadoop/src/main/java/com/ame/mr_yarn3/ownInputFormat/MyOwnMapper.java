package com.ame.mr_yarn3.ownInputFormat;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * 自定义我们的map类，接收我们k1   v1  分别是  NullWritable,BytesWritable
 * 转换成新的  k2   v2   进行输出  ，类型分别是  文件名  和文件内容  文件名用 Text来表示，文件内容用BytesWritable来表示
 */
public class MyOwnMapper  extends Mapper<NullWritable,BytesWritable,Text,BytesWritable> {

    @Override
    protected void map(NullWritable key, BytesWritable value, Context context) throws IOException, InterruptedException {
        FileSplit inputSplit = (FileSplit) context.getInputSplit();
        //获取我们文件的名称
        String fileName = inputSplit.getPath().getName();

        context.write(new Text(fileName),value);
    }
}
