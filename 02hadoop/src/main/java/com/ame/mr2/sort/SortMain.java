package com.ame.mr2.sort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class SortMain extends Configured implements Tool{
    @Override
    public int run(String[] args) throws Exception {
        //获取job对象
        Job job = Job.getInstance(super.getConf(), "combiner");

        job.setJarByClass(SortMain.class);

        //第一步：读取文件，解析成key,value对
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,new Path("file:///G:\\input"));

        //第二步：设置我们的mapper类
        job.setMapperClass(SortMapper.class);
        //设置我们key2  value2的输出类型
        job.setMapOutputKeyClass(PairSort.class);
        job.setMapOutputValueClass(Text.class);

        // 第三到六步 全部省略

        //设置第五步：规约：
        job.setCombinerClass(MyCombiner.class);
        //第七步：reduce阶段
        job.setReducerClass(SortReducer.class);
        job.setOutputKeyClass(PairSort.class);
        job.setOutputValueClass(NullWritable.class);

        //第八步：数据输出
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,new Path("file:///G:\\outSort"));
        //提交任务
        boolean b = job.waitForCompletion(true);
        return b?0:1;
    }

    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new SortMain(), args);
        System.exit(run);

    }

}
