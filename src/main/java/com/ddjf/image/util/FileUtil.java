package com.ddjf.image.util;

import com.aliyun.oss.OSSClient;
import com.ddjf.image.properties.ImageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileUtil {
    private static  Logger logger  = LoggerFactory.getLogger(FileUtil.class);
	private static String scriptPath = ImageProperties.INSTANCE().getScriptPath();

	public static byte[] fileToBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public static String bytesToFile(byte[] buf, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		String path = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			path = filePath + "/" + fileName;
			file = new File(path);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return path;
	}

	/**
	 * 根据
	 * @param filePath
	 * @return
	 */
	public static boolean createFilePath(String filePath){

		boolean  flag  =  false;
		File dir = new File(filePath);
		if (!dir.exists()) {
			logger.warn("目标路径不存在，自动生成文件夹。");
			flag = dir.mkdirs();
		}
		return flag;
	}

	public static String insToFile(InputStream ins, String filePath, String fileName) {
		OutputStream os = null;
		String path = null;
		File file = null;
		try {
			//自动判断目标文件夹是否存在
			createFilePath(filePath);

			path = filePath + Constant.SEPARATE_SLASH + fileName;
			file = new File(path);
			os = new FileOutputStream(file);
			byte[] buffer = new byte[8192];
			int bytesRead = 0;
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			path = null;
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return path;
	}
	
	public static String getFileName(String filePath){
		return filePath.substring(filePath.lastIndexOf("/")+1);
	}

	/**
	 * 将InputStream 转换为 ByteArrayOutputStream 实现对象的拷贝
	 * @param inputStream
	 * @return
	 */
	public static byte[] inputStream2ByteArray(InputStream inputStream){

		byte[] res = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try{
			byte[] buffer = new byte[1024];
			int len;
			while ((len = inputStream.read(buffer)) > -1 ) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
			baos.close();

			res = baos.toByteArray();
		}catch (IOException e){
			logger.error("InputStream转化为byte[] 出错。"+e.getMessage());
            e.printStackTrace();
		}

		return res;
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * @param dir 将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful.
	 *                 If a deletion fails, the method stops attempting to
	 *                 delete and returns "false".
	 */
	public  static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			//递归删除目录中的子目录下
			for (int i=0; i<children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		logger.warn("删除:"+dir.getAbsolutePath());
		// 目录此时为空，可以删除
		return dir.delete();
	}

	/**
	 * 服务器临时文件上传到OSS
	 * @param filePath
	 */
	public static void localfileUploadToOSS(String filePath){
		logger.info("localfileUploadToOSS--开始");
		OSSClient ossClinet = OSSUtil.getOSSClinet();
		String buketName = OSSUtil.getBuketName();
		File tempFile = new File(filePath);
		File[] fileList = tempFile.listFiles();
		String tempFilePath = "";
		InputStream inps = null;
		logger.info("把转化后的文件都上传到OSS.");
		for(File f : fileList){
			//把转化后的文件都上传到OSS
			String name = f.getName().toLowerCase();
			if(name.endsWith("jpg") || name.endsWith("html")){
				tempFilePath = f.getAbsolutePath();
				inps = new ByteArrayInputStream(FileUtil.fileToBytes(tempFilePath));
				OSSUtil.putObject(ossClinet,buketName,tempFilePath,inps);
			}
		}
		logger.info("localfileUploadToOSS--结束");
	}

	/** 文件重命名
	 * @param path 文件目录
	 * @param oldname  原来的文件名
	 * @param newname 新文件名
	 */
	public void renameFile(String path,String oldname,String newname){

		if(!oldname.equals(newname)){
			//新的文件名和以前文件名不同时,才有必要进行重命名
			File oldfile=new File(path + Constant.SEPARATE_SLASH + oldname);
			File newfile=new File(path + Constant.SEPARATE_SLASH + newname);
			if(!oldfile.exists()){
				logger.error("源文件不存在。");
				return;
			}
			if(newfile.exists())
				//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
				logger.info(newname+"已经存在！");
			else{
				oldfile.renameTo(newfile);
			}
		}else{
			logger.info("新文件名和旧文件名相同，不进行重命名。");
		}
	}

	/**
	 * 执行command命令
	 * @param command
	 */
	public static  void excuteCommand(String command){
		logger.info("执行转换命令--开始");
		logger.info("脚本命令："+command);

		try{
			Runtime run = Runtime.getRuntime();
			//1.转换
			Process proc = run.exec(command);
			//获取执行命令的反馈结果信息，方便追踪问题
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line;
			while((line = br.readLine())!=null ){
				logger.warn("br.line={}", line);
			}
			BufferedReader err = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			while((line = err.readLine())!=null ){
				logger.warn("err.line={}", line);
			}
			proc.waitFor();
			br.close();
			err.close();
			logger.info("执行转换命令--结束");
		}catch (Exception e){
			logger.error("执行转换文件命令出错。"+e.getMessage());
			e.printStackTrace();

		}
	}
}
