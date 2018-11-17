package com.ame.mr1.partition;

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

//PartitionerOwn自定义分区类,可以指定条件指定分区位置
public class PartitionerMain extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        //获取一个job类，用于组装我们的MR任务
        Job job = Job.getInstance(super.getConf(), PartitionerMain.class.getSimpleName());
        //打包运行必须要的
        job.setJarByClass(PartitionerMain.class);

        //第一步：读取文件，解析成key,value对
        job.setInputFormatClass(TextInputFormat.class);
        //注意，分区的案例，不能使用本地模式运行  使用file:///不好使报错，只能打包到集群上面去运行
        TextInputFormat.addInputPath(job, new Path(args[0]));

        //第二步：自定义map逻辑
        job.setMapperClass(PartitionerMapper.class);
        //设置我们key2  value2  的类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        //第三步：设置我们的分区类,使用我们自定义的分区类来进行分区
        job.setPartitionerClass(PartitionerOwn.class);

        //第四步：排序  第五步：规约  第六步：分组

        //第七步：自定义reduce逻辑
        job.setReducerClass(PartitionerReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);
        //设置我们reduceTask的个数
        job.setNumReduceTasks(2);

        //第八步：设置输出类
        job.setOutputFormatClass(TextOutputFormat.class);
        //我们的输出路径不写死，通过参数传递进来
        TextOutputFormat.setOutputPath(job, new Path(args[1]));
        //提交任务
        boolean b = job.waitForCompletion(true);
        return b ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new PartitionerMain(), args);
        System.exit(run);
    }

}
