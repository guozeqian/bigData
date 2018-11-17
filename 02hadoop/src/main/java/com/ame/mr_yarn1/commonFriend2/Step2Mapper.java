package com.ame.mr_yarn1.commonFriend2;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Arrays;

public class Step2Mapper extends Mapper<LongWritable,Text,Text,Text> {


    /**
     * 用户     好友
     * E-A-J-F-	B
     * @param key
     * @param value
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split("\t");
        //按照  -  切割成为一个数组
        String[] user = split[0].split("-");
        //对我们的数组进行排序  避免出现  A-E   E-A  这种情况
        Arrays.sort(user);

        for(int i =0;i<user.length -1;i++){
            for(int j =i+1;j<user.length;j++){
                //数据往外发发送  A-E  B
                context.write(new Text(user[i]+"-"+user[j]),new Text(split[1]));
            }

        }






    }
}
