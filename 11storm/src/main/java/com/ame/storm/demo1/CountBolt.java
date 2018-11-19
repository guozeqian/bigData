package com.ame.storm.demo1;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 继承 BaseBasicBolt表明我们是一个标准的bolt组件
 */
public class CountBolt extends BaseBasicBolt {


    private Map<String,Integer> map  = new Hashtable<String,Integer>();

    //java.util.concurrent 这个包下面所有的类都是线程安全的
    //使用java.util.concurrent这个包可以解决我们多线程开发遇到的线程安全的问题 jdk1.5之后出来的新特性
    //这个全局变量，存放在jvm的方法区里面，方法区里面的东西都是线程共享的
    //通过添加static关键字之后，就会保证线程的安全，一次只允许一个线程来操作，操作完成之后才会允许下一个线程来操作
    private static ConcurrentHashMap<String,Integer> hashMap = new ConcurrentHashMap<String,Integer>();

    /**
     * 初始化的方法，初始化的时候会调用一次
     * @param stormConf
     * @param context
     */
    @Override
    public void prepare(Map stormConf, TopologyContext context) {
     //   map = new HashMap<String,Integer>();
    }

    /*
        这个方法也会反复的不断的被调用，上游有数据，这个方法就会被调用
         */
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {

    //如果每次都在这里new一个map出来，每次都是一个新的对象，没法计数
    //    ConcurrentHashMap<String,Integer> hashMap = new ConcurrentHashMap<String,Integer>();


        //获取上游发送的字符串
        String word = input.getStringByField("word");
        Integer nums = input.getIntegerByField("nums");

        //判断我们的单词是否出现在map当中
    /*    if(map.containsKey(word)) {
            map.put(word,map.get(word)+nums);
        }else{
            map.put(word,nums);
        }
         System.out.println(map.toString());
        */

        //使用ConcurrentHashMap来解决我们的多线程的问题
            if(hashMap.containsKey(word)) {
                hashMap.put(word,hashMap.get(word)+nums);
        }else{
                hashMap.put(word,nums);
        }

        System.out.println(hashMap.toString());


    }
    /**
     * 如果下游没有bolt之后，这个方法就不用再写了
     * @param declarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
