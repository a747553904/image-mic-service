package com.ddjf.image.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ddjf.image.properties.ImageProperties;

public class PdfToHtmlUtil {
	
	private static Logger logger = LoggerFactory.getLogger(PdfToHtmlUtil.class);
	
	private static String scriptPath = ImageProperties.INSTANCE().getScriptPath();
	
	public static boolean pdfToHtml(String filePath, String fileName){
		boolean flag = false;
		logger.warn("begin pdfToHtml==========" + DateUtil.formate(new Date()));

		String shell = scriptPath + "pdf2html.sh";
        StringBuilder command = new StringBuilder();
        command.append(shell).append(" ")
                    .append(filePath).append(" ")
                    .append(fileName);
        logger.warn("command={}", command.toString());

        FileUtil.excuteCommand(command.toString());
        flag = true;
        logger.warn("end pdfToHtml==========" + DateUtil.formate(new Date()));
        return flag;
	}

    /**
     *pdf转html并上传到OSS
     * @param filePath
     * @param fileName
     * @return
     */
    public static boolean pdfToOSSHtml(String filePath, String fileName){
        boolean flag = false;
        logger.warn("begin pdfToHtml==========" + DateUtil.formate(new Date()));
        try {
            //1.转码
            boolean isSuccess = pdfToHtml(filePath, fileName);
            if(!isSuccess){
               logger.error("pdfToHtml 转码失败。");
               return flag;
            }

            //2.上传到OSS
            logger.info("2:将转成临时html文件及原文件上传到OSS--开始。");
            FileUtil.localfileUploadToOSS(filePath);
            logger.info("2:将转成临时html文件及原文件上传到OSS--结束。");

            //3.删除临时文件
            logger.info("3:将转成临时文件全部删除--开始。");
            boolean IsDelete = FileUtil.deleteDir(new File(filePath));
            if(IsDelete){
                logger.info("将临时文件全部删除成功。");
            }
            logger.info("3:将转成临时文件全部删除--结束。");

            flag = true;
        } catch (Exception e) {
            logger.error("pdfToHtml转换错误", e);
        }
        logger.warn("end pdfToHtml==========" + DateUtil.formate(new Date()));
        return flag;
    }

}
