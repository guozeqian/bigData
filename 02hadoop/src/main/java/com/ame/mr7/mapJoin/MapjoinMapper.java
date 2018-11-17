package com.ame.mr7.mapJoin;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class MapjoinMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

    Map<String, String> map = new HashMap<String, String>();


    /**
     * 初始化的方法，最开始的时候调用一次
     * 在这个方法里面可以获取到我们的缓存文件
     *
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        //获取到了缓存文件的列表
        URI[] cacheFiles = DistributedCache.getCacheFiles(configuration);
        //获取我们的分布式文件系统
        FileSystem fileSystem = FileSystem.get(cacheFiles[0], configuration);
        //把我们分布式缓存的文件读成一个流
        FSDataInputStream inputStream = fileSystem.open(new Path(cacheFiles[0]));
        //通过BufferedReader来读取我们的输入流
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            //p0001,小米5,1000,2000
            //往下读一行
            String[] split = line.split(",");
            map.put(split[0], line);
        }
        IOUtils.closeQuietly(inputStream);
        fileSystem.close();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] split = line.split(",");
        //从map当中获取商品表的数据
        String product_line = map.get(split[2]);
        context.write(new Text(product_line + "\t" + value.toString()), NullWritable.get());

    }
}
