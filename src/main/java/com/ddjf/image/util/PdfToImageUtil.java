package com.ddjf.image.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.imageio.ImageIO;

import com.aliyun.oss.OSSClient;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PdfToImageUtil {
	
	private static Logger logger = LoggerFactory.getLogger(PdfToImageUtil.class);
	
	private static final String SPOT = ".";
	private static final String SLASH = "/";

    /**
     * PDF转JPG:直接转换到影像服务器本地
     * @param filePath
     * @param fileName
     * @return
     */
    public static boolean pdfToJpg(String filePath, String fileName){
        boolean flag = false;
        logger.warn("begin pdfToJpg==========" + DateUtil.formate(new Date()));
        File file = new File(filePath + SLASH + fileName);
        try {
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            String prefix = fileName.substring(0, fileName.indexOf(SPOT));
            for (int i = 0; i < pageCount; i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 150);
                String pathname = filePath + SLASH + prefix + "_" + i + ".jpg";
                logger.warn("pathname={}", pathname);
                ImageIO.write(image, "JPG", new File(pathname));
            }
            flag = true;
        } catch (IOException e) {
            flag = false;
            logger.error("pdfToJpg转换错误", e);
        }
        logger.warn("end pdfToJpg==========" + DateUtil.formate(new Date()));
        return flag;
    }
    /**
     *PDF转JPG: 转换到OSS存储服务器
     * @param filePath
     * @param fileName
     * @return
     */
	public static boolean pdfToOSSJpg(String filePath, String fileName){
		boolean flag = false;
		File file = new File(filePath + SLASH + fileName);
        //1.转码
        logger.info("1:将PDF文件依次转成临时jpg文件--开始,放在"+filePath);
        boolean b = pdfToJpg(filePath, fileName);
        if(!b){
            logger.error("pdfToJpg 转换失败。");
            return flag;
        }
        logger.info("1:将PDF文件依次转成临时jpg文件--完成。");

        //2.转码成功后再次上传到OSS
        logger.info("2:将转成临时jpg文件及原文件上传到OSS--开始。");
        FileUtil.localfileUploadToOSS(filePath);
        logger.info("2:将转成临时jpg文件及原文件上传到OSS--完成。");

        //3.删除临时文件（也可以保留）
        logger.info("3:将转成临时文件全部删除--开始。");
        boolean IsDelete = FileUtil.deleteDir(new File(filePath));
        if(IsDelete){
            logger.info("将临时文件全部删除成功。");
        }
        logger.info("3:将转成临时文件全部删除--结束。");

        flag = true;
        return flag;
	}

}
