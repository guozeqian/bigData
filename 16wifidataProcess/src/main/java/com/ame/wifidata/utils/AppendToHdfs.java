package com.ame.wifidata.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.*;
import java.net.URI;
import java.util.Date;

public class AppendToHdfs implements Serializable{

/**
	 * 
	 */
	private static final long serialVersionUID = -1361504775153512786L;
	
	
	public  static String hdfsOrig = "hdfs://192.168.52.100:8020";
	
	public static  void appendToHdfs(File  file,String dataType) throws Exception{
		Date date = new Date();
		//String finalPath = hdfsOrig+File.separator+"wxcm"+File.separator+type+File.separator+DateUtils.getCurrentYear(date)+File.separator+DateUtils.getCurrentMonth(date)+File.separator+DateUtils.getCurrentDay(date)+File.separator+type+".txt";
		//  hdfs://192.168.52.100:8020/wxcm/YT1013/2018/10/29/YT1013.txt
		String finalPath = hdfsOrig+"/wxcm/"+dataType+"/"+DateUtils.getCurrentYear(date)+"/"+DateUtils.getCurrentMonth(date)+"/"+DateUtils.getCurrentDay(date)+"/"+dataType+".txt";
		Configuration conf = new Configuration();
	    conf.setBoolean("dfs.support.append", true);  
	    conf .set("dfs.client.block.write.replace-datanode-on-failure.policy" ,"NEVER" );
        conf .set("dfs.client.block.write.replace-datanode-on-failure.enable" ,"true" );
		FileSystem hdfs = FileSystem.get(new URI(finalPath), conf); 
		 boolean file2 = hdfs.isFile(new Path(finalPath));
       //  boolean exists = hdfs.exists(new Path(finalPath));
		 if(!file2){
			 boolean createNewFile = hdfs.createNewFile(new Path(finalPath));
			 hdfs.close();
			 hdfs= FileSystem.get(conf);
			 //FSDataOutputStream create = hdfs.create(new Path(finalPath));
		 }
		 FileSystem fs = FileSystem.get(URI.create(finalPath), conf);
		 InputStream in = new   BufferedInputStream(new FileInputStream(file.getAbsolutePath()));  
         OutputStream out = fs.append(new Path(finalPath));  
         IOUtils.copyBytes(in, out, 4096, true);  
         long len = fs.getFileStatus(new Path(finalPath)).getLen();
        // fs.rename(new Path(finalPath), new Path(hdfsOrig+File.separator+type+File.separator+DateUtils.getCurrentYear(date)+File.separator+DateUtils.getCurrentMonth(date)+File.separator+DateUtils.getCurrentDay(date)+File.separator+type+"_"+Uuid16.create().toString()+".txt"));
         fs.rename(new Path(finalPath), new Path(hdfsOrig+"/wxcm/"+dataType+"/"+DateUtils.getCurrentYear(date)+"/"+DateUtils.getCurrentMonth(date)+"/"+DateUtils.getCurrentDay(date)+"/"+dataType+"_"+Uuid16.create().toString()+".txt"));
	
	}
}
