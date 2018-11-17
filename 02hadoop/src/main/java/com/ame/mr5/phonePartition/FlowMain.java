package com.ame.mr5.phonePartition;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class FlowMain extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        //组装我们的程序运行
        Job job = Job.getInstance(super.getConf(), "flowCount");

        //打包运行的时候，设置我们 的main方法在哪里java文件里面
        job.setJarByClass(FlowMain.class);
        //第一步：读取文件，解析成key,value对
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,new Path(args[0]));

        //第二步：自定义map逻辑
        job.setMapperClass(FlowMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);


        //第三到六步全部省略
        job.setPartitionerClass(PhonePartition.class);




        //第七步：自定义reduce逻辑
        job.setReducerClass(FlowReducer.class);
        //设置reduce的个数为6个，每个reduce刚好处理一个分区里面的数据
        job.setNumReduceTasks(6);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        //第八步：输出数据
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,new Path(args[1]));
        boolean b = job.waitForCompletion(true);
        return b?0:1;
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        //开启我们map阶段的数据压缩
        configuration.set("mapreduce.map.output.compress","true");
        configuration.set("mapreduce.map.output.compress.codec", "org.apache.hadoop.io.compress.SnappyCodec");

        //开启我们reduce阶段的数据压缩
        configuration.set("mapreduce.output.fileoutputformat.compress","true");
        configuration.set("mapreduce.output.fileoutputformat.compress.type","RECORD");
        configuration.set("mapreduce.output.fileoutputformat.compress.codec","org.apache.hadoop.io.compress.SnappyCodec");

        int run = ToolRunner.run(configuration, new FlowMain(), args);
        System.exit(run);
    }

}
