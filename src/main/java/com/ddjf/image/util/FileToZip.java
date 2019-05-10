package com.ddjf.image.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileToZip {

	private static Logger logger  = LoggerFactory.getLogger(FileToZip.class);

	/**
	 * 将EndOperation文件夹下源文件，打包成fileName名称ZIP文件，并保存到zipFilePath
	 * @param sourceFilePath 待压缩的文件路径
	 * @param zipFilePath    压缩后存放路径
	 * @param fileName       压缩后文件的名称
	 * return flag
	 */
	public static boolean fileToZip(String sourceFilePath,String zipFilePath,String fileName){
		boolean flag = false;
		//定义变量
		File sourceFile = null;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		String sourceFilePathZ[];
		String sZipFile = zipFilePath+"/"+fileName+".zip";
		try {
			
			File zipFile = new File(sZipFile);
			if(zipFile.exists()){
				System.out.println(">>>>>>>>>>>>"+ zipFilePath +" 目录下存在名字为："+ fileName +".zip 的打包文件<<<<<<<<<<<<<");
			}else{
				fos = new FileOutputStream(zipFile);
				zos = new ZipOutputStream(new BufferedOutputStream(fos));
				sourceFilePathZ = sourceFilePath.split("@");
				for(int i = 0;i < sourceFilePathZ.length;i++){
					sourceFile = new File(sourceFilePathZ[i]);
					byte[] bufs = new byte[1024*10];
					//创建ZIP实体，并添加进压缩文件
					ZipEntry zipEntry = new ZipEntry(sourceFile.getName());
					zos.putNextEntry(zipEntry);
					
					//读取待压缩的文件并写进压缩包
					fis = new FileInputStream(sourceFile);
					bis = new BufferedInputStream(fis,1024*10);
					int read = 0;
					while((read = bis.read(bufs, 0, 1024*10)) != -1){
						zos.write(bufs, 0, read);
					}
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally {
			//关闭流
			try {
				if(null != bis) bis.close();
				if(null != zos) zos.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
		return flag;
	}
	
	/**
	 * 文件下载
	 * @param sFilePath	文件路径
	 * @return
	 */
	public static boolean getFileDown(String sFilePath){
		boolean flag = false;
		
		return flag;
	}
	
	/**
	 * 用zip4j方式将EndOperation文件夹下源文件，打包成fileName名称ZIP文件并加密，并保存到zipFilePath
	 * @param sourceFilePath 待压缩的文件路径
	 * @param zipFilePath    压缩后存放路径
	 * @param fileName       压缩后文件的名称
	 * return flag
	 */
	public static boolean fileToZip4j(String sourceFilePath,String zipFilePath,String fileName){
		logger.info("zip打包文件信息--开始.");

		boolean flag = false;
		ZipFile zipFile = null;
		ArrayList<File> filesToAdd = new ArrayList<File>();
		ZipParameters parameters = new ZipParameters();
		String sourceFilePathZ[];
		String sZipFile = zipFilePath+ Constant.SEPARATE_SLASH +fileName;
		//先确定路径是否存在
		FileUtil.createFilePath(zipFilePath);

		try {
			File zFile = new File(sZipFile);
			if(zFile.exists()){
				logger.info("存在旧的zip文件，删除处理="+sZipFile);
				zFile.delete();
			}
			zipFile = new ZipFile(sZipFile);
			sourceFilePathZ = sourceFilePath.split("@");
			for(int i = 0; i < sourceFilePathZ.length; i++){
				filesToAdd.add(new File(sourceFilePathZ[i]));
			}
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			/*
			parameters.setEncryptFiles(true);
			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
			parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
			parameters.setPassword("dspm123");
			*/
			zipFile.addFiles(filesToAdd, parameters);
			flag = true;
			logger.info("zip打包文件信息--结束.");
		} catch (ZipException e) {
			logger.error("zip打包文件出错。"+e.getMessage());
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
	
	public static void fileChannelCopy(String source, String dest) {
		fileChannelCopy(new File(source), new File(dest));
	}
	
	public static void fileChannelCopy(File s, File t) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fis = new FileInputStream(s);
            fos = new FileOutputStream(t);
            in = fis.getChannel();//得到对应的文件通道
            out = fos.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
            	if(fis != null)
            		fis.close();
            	if(in != null)
            		in.close();
                if(fos != null)
                	fos.close();
                if(out != null)
                	out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	public static void main(String [] args){
		String sourceFilePath = "D:/test/download/001.jpg@D:/test/download/002.jpg@D:/test/download/003.jpg";
		String zipFilePath = "D:/test/download";
		String fileName = "abcd";
		boolean flag = FileToZip.fileToZip4j(sourceFilePath, zipFilePath, fileName);
		if(flag){
			System.out.println(">>>>>>>>文件打包成功<<<<<<<<");
		}else{
			System.out.println(">>>>>>>>文件打包失败<<<<<<<<");
		}
	}
}
