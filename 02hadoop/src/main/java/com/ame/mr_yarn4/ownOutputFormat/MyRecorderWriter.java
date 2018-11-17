package com.ame.mr_yarn4.ownOutputFormat;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

public class MyRecorderWriter extends RecordWriter<Text,NullWritable> {

    public  MyRecorderWriter(){

    }
//定义我们两个输出流，分别输出到不同的文件夹下面去
    private FSDataOutputStream goodStream;
    private  FSDataOutputStream badStream;


    public MyRecorderWriter(FSDataOutputStream goodStream,FSDataOutputStream badStream){
        this.goodStream = goodStream;
        this.badStream  = badStream;
    }





    /**
     * 将我们的数据往外写
     * @param text  我们一行的评论数据
     * @param nullWritable
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void write(Text text, NullWritable nullWritable) throws IOException, InterruptedException {
        //1	2018-03-15 22:29:06	2018-03-15 22:29:06	我想再来一个	\N	1	3	hello	来就来吧	0	2018-03-14 22:29:03
        String[] split = text.toString().split("\t");
        //获取到我们评论的状态
        String commentStatus = split[9];
        //根据评论的状态来判断我们的评论究竟是好评还是中评，还是差评
        if(Integer.parseInt(commentStatus) <= 1){
            //我们的好评数据
            //我们可以通过输出流，往外写出去数据
            goodStream.write(text.toString().getBytes());
            //对我们的数据进行换行
            goodStream.write("\r\n".getBytes());

        }else{
            //差评数据
            badStream.write(text.toString().getBytes());
            badStream.write("\r\n".getBytes());
        }



    }

    @Override
    public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        IOUtils.closeStream(goodStream);
        IOUtils.closeStream(badStream);
    }
}
