package com.ame.wifidata.utils;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileOperateUtils implements Serializable {

	
	private static ConcurrentLinkedQueue<String> concurrentLinkedQueue =  new ConcurrentLinkedQueue<String>();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4763341832497838848L;

	/**
	 * 
	 */
	
	
	

	public static  synchronized void writeLine(File file,String fileContent){
		concurrentLinkedQueue.clear();
		concurrentLinkedQueue.add(fileContent);
		try {
			FileUtils.writeLines(file, concurrentLinkedQueue, "\r", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static BufferedWriter writer;
	private static File file;
	//数据存储的根路径
	//private String basePath="F:\\wifiDatas";
	
	private static  String basePath="/export/datas";
	
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	public static void  meargeToLargeFile(String dataType, String stringByField) throws Exception{
		String format2 = format.format(new Date());
		//File.separator  是文件的分隔符  linux 里面是这个  /   windows里面  \\
		//   /export/datas/YT1013/2018-10-29
		if(! new File(basePath+File.separator+dataType+File.separator+format2).exists()){
			//判断如果本地存放文件的文件夹不存在，那么就创建
			new File(basePath+File.separator+dataType+File.separator+format2).mkdirs();
		}
		//  /export/datas/YT1013/2018-10-29/YT1013.txt
		file = new File(basePath+File.separator+dataType+File.separator+format2+File.separator+dataType+".txt");
		if(!file.isFile()){
			//判断我们的文件路径，如果文件路径不存在，那么就创建文件
			file.createNewFile();
		}
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
		writer.write(stringByField);
		//换行操作
		writer.newLine();
		writer.flush();
		writer.close();
		//文件内容大于127.5M的时候进行上传HDFS
		/*if(FileUtils.sizeOf(file) > 133693440 ){
			//将文件上传到hdfs对应的位置去
			AppendToHdfs.appendToHdfs(file,dataType );
		}
		*/
		//判断文件大小，大于5KB，就上传到hdfs上面去
		if(FileUtils.sizeOf(file) > 5120 ){
			//将文件上传到hdfs对应的位置去
			AppendToHdfs.appendToHdfs(file,dataType );
		}
		
		
		
		
	}
	
	private static String[] dataTypeArray = new String[]{"YT1013","YT1020","YT1023","YT1033","YT1034"};
	
	public static void uploadYestData(){
		//将昨日的所有数据都上传到hdfs上面去
		for (String string : dataTypeArray) {
			//  /export/datas/YT1013/2018-10-28/yt1013.txt
			File file  = new File(basePath+File.separator+ string+File.separator+DateUtils.getBeforeDay(-1)+File.separator+string+".txt");
			if(file.isFile()&& file.exists()){
				try {
					AppendToHdfs.appendToHdfs(file,string);
					file.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	
	
	
	
	

}
