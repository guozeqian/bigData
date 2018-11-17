package com.ame.mr_yarn4.ownOutputFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class MyOutputFormat extends FileOutputFormat<Text,NullWritable> {
    @Override
    public RecordWriter<Text, NullWritable> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        Configuration configuration = taskAttemptContext.getConfiguration();
        FileSystem fileSystem = FileSystem.get(configuration);
        //好评的输出流
        FSDataOutputStream goodComment = fileSystem.create(new Path("file:///F:\\自定义outputformat\\good_comment\\good_comment.txt"));
        //差评的输出流
        FSDataOutputStream badComment = fileSystem.create(new Path("file:///F:\\自定义outputformat\\bad_comment\\bad_comment.txt"));
        MyRecorderWriter myRecorderWriter = new MyRecorderWriter(goodComment,badComment);
        return myRecorderWriter;
    }





}
