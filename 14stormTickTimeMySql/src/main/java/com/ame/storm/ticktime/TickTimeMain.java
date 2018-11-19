package com.ame.storm.ticktime;

import com.google.common.collect.Maps;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.jdbc.bolt.JdbcInsertBolt;
import org.apache.storm.jdbc.common.ConnectionProvider;
import org.apache.storm.jdbc.common.HikariCPConnectionProvider;
import org.apache.storm.jdbc.mapper.JdbcMapper;
import org.apache.storm.jdbc.mapper.SimpleJdbcMapper;
import org.apache.storm.topology.TopologyBuilder;

import java.util.Map;

public class TickTimeMain  {
    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        Map hikariConfigMap = Maps.newHashMap();
        hikariConfigMap.put("dataSource.url", "jdbc:mysql://192.168.25.109/log_monitor");
        hikariConfigMap.put("dataSource.url", "jdbc:mysql://192.168.25.26/log_monitor");
        hikariConfigMap.put("dataSource.user","root");
        hikariConfigMap.put("dataSource.password","admin");
        ConnectionProvider connectionProvider = new HikariCPConnectionProvider(hikariConfigMap);

        String tableName = "user";
        JdbcMapper simpleJdbcMapper = new SimpleJdbcMapper(tableName, connectionProvider);

     /*   JdbcInsertBolt userPersistanceBolt = new JdbcInsertBolt(connectionProvider, simpleJdbcMapper)
                .withTableName("user")
                .withQueryTimeoutSecs(30);*/
     //   Or
        JdbcInsertBolt userPersistanceBolt = new JdbcInsertBolt(connectionProvider, simpleJdbcMapper)
                .withInsertQuery("insert into user values (?,?,?)")
                .withQueryTimeoutSecs(30);


        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("randomSpout",new RandomSpout());
        builder.setBolt("tickTimeBolt",new TickTimeBolt()).localOrShuffleGrouping("randomSpout");
        //添加我们storm与jdbc整合的bolt
        builder.setBolt("jdbcBolt" ,userPersistanceBolt).localOrShuffleGrouping("tickTimeBolt");

        Config config = new Config();
        if(null  != args && args.length >0){
            StormSubmitter.submitTopology(args[0],config,builder.createTopology());
        }else{
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("xxx",config,builder.createTopology());
        }
    }
}
