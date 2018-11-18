package com.ame.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class MyKafkaProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "node01:9092,node02:9092,node03:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //自定义分区需要添加上我们自定义分区的那个类
        props.put("partitioner.class", "cn.itcast.kafka.kafkaPartitioner.MyOwnPartitioner");


        Producer<String, String> producer = new KafkaProducer<String,String>(props);
        for (int i = 0; i < 100; i++){
            /// /    producer.send(new ProducerRecord<String, String>("test", Integer.toString(i), Integer.toString(i)));
            //kafka的第一种分区方式，如果给定了分区号，那么就直接将数据发送到指定的分区号里面去
            //producer.send(new ProducerRecord<String, String>("test",2,"helloworld",i+""));
            //kafka的第二种分区策略，没有给定分区号，给定了数据的key，那么就通过key取hashcode，将数据均匀的发送到三台机器里面去
            //注意如果实际工作当中，要通过key取上hashcode来进行分区，那么就一定要 保证key的变化，否则，数据就会全部去往一个分区里面
           /// producer.send(new ProducerRecord<String, String>("test",i+"",i+""));
            //kafka的第三种分区策略，既没有给定分区号，也没有给定数据的key值，那么就会按照轮循的方式进行数的发送
            producer.send(new ProducerRecord<String, String>("test",i+""));
        //kafka的第四种分区策略，自定义分区类，实现我们数据的分区

        }
        producer.close();
    }

}
