package com.ame.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class MyKafkaConsumer {
    public static void main(String[] args) {
        /**
         * 自动提交offset
         *
         */

      /*  //这种写法是自动提交offset  偏移量，记录了我们消费到哪一条数据来了
        //offset记录了我们消息消费的偏移量，就是说我们上一次消费到了哪里
        //在kafka新的版本当中，这个offset保存在了一个默认的topic当中
        //每次消费数据之前，获取一下offset偏移量的值，就知道我们该要从哪一条数据消费
        //消费完成之后，offset的值要不要更新。消费完成之后，offset的值一定要更新，才不会造成重复消费的问题
        Properties props = new Properties();
        props.put("bootstrap.servers", "node01:9092,node02:9092,node03:9092");
        //设置我们的消费是属于哪一个组的，这个组名随便取，与别人的不重复即可
        props.put("group.id", "test");
        //设置我们的offset值自动提交
        props.put("enable.auto.commit", "true");
        //offset的值自动提交的频率 1 提交   1.5 消费了500调数据  1.6秒宕机了  2 提交offset
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String,String>(props);
        //消费者订阅我们的topic
        consumer.subscribe(Arrays.asList("test"));
        //相当于开启了一个线程，一直在运行，等待topic当中有数据就去拉取数据
        while (true) {
            //push  poll
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }*/

      /*
      手动提交offset    如何保证spark消费kafka当中的数据 exactly  once
      如果数据正常处理，提交offset，如果数据处理失败，不要提交offset
       */
        Properties props = new Properties();
        props.put("bootstrap.servers", "node01:9092,node02:9092,node03:9092");
        props.put("group.id", "test");
        //关闭我们的offset的自动提交
        props.put("enable.auto.commit", "false");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList("test"));
        final int minBatchSize = 200;
        List<ConsumerRecord<String, String>> buffer = new ArrayList<ConsumerRecord<String, String>>();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                buffer.add(record);
            }
            if (buffer.size() >= minBatchSize) {
             //   insertIntoDb(buffer);
                //手动提交offset的值
                consumer.commitSync();
                buffer.clear();
            }
        }
    }
}
