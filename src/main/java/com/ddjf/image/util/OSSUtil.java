package com.ddjf.image.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.ddjf.image.controller.EcmPageController;
import org.apache.catalina.connector.Request;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import sun.nio.cs.ext.GBK;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 构建组：大道金服科技部
 * 作者:moyongfeng
 * 邮箱:moyongfeng@ddjf.com.cn
 * 日期:2018/1/8 11:29
 * 功能说明：使用阿里云OSS存储工具类
 * 主要包括，获取buket连接，图像上传下载查看等功能
 */
public class OSSUtil {

    private static Logger logger = LoggerFactory.getLogger(OSSUtil.class);
    //内网endpoint
    private static String endpointIternal;
    //外网endpoint
    private static String endpoint;
    //桶名
    private static String buketName;
    //用户名及访问凭证
    private static String accessKeyId;

    private static String accessKeySecret;

    public static String getBuketName() {
        return buketName;
    }

    public static void setBuketName(String buketName) {
        OSSUtil.buketName = buketName;
    }


    static{
        buketName = SpringContextUtil.getProperty("buketName");
        endpoint =  SpringContextUtil.getProperty("endpoint");
//        endpoint =  SpringContextUtil.getProperty("endpoint.internal");
        endpointIternal = SpringContextUtil.getProperty("endpoint.internal");
        accessKeyId = SpringContextUtil.getProperty("ak");
        accessKeySecret = SpringContextUtil.getProperty("sk");
    }

    /**
     * 使用系统默认配置获取OSSClient
     * @return OSSClient
     */
    public static OSSClient getOSSClinet(){
        return getOSSClinet(endpoint,accessKeyId,accessKeySecret);
    }
    /**
     * 获取OSSClient对象
     * @param endpoint
     * @param accessKeyId 用户
     * @param accessKeySecret 凭证
     * @return
     */
    public static OSSClient getOSSClinet(String endpoint,String accessKeyId,String accessKeySecret){

        OSSClient ossClient = null;
        try{
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
         }catch (Exception e){
             logger.error("获取OSSClient出错。"+e.getStackTrace());
         }
        return ossClient;
    }

    /**
     * 关闭client连接
     * @param ossClient
     */
    public static void closeOSSClient(OSSClient ossClient){

        if(ossClient != null){
            try{
                ossClient.shutdown();
            }catch (Exception e){
                logger.error("关闭OSSClient出错。"+e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取buckets信息
     * @param ossClient
     * 可带过滤条件
     * @param prefix ：过滤前缀
     * @param marker ：设定结果从marker之后按字母排序的第一个开始返回，可以不设定，不设定时从头开始返回
     * @param max :最大返回个数，默认100，最大1000
     * @return
     */
    public static List<Bucket> getBuckets(OSSClient ossClient,String prefix, String marker, String max){
        logger.info("获取buckets开始");
        ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
        if(StringUtils.isNotBlank(prefix)){
            listBucketsRequest.setPrefix(prefix);
        }
        if(StringUtils.isNotBlank(marker)){
            listBucketsRequest.setMarker(marker);
        }
        if(StringUtils.isNotBlank(max)){
            listBucketsRequest.setMaxKeys(new Integer(max));
        }
        BucketList bucketList = ossClient.listBuckets(listBucketsRequest);
        logger.info("获取buckets结束");
       return bucketList.getBucketList();
    }

    /**
     * 上传对象
     * @param ossClient
     * @param buketName :桶名
     * @param fileFullName ：全文件路径及名称
     *                       例子：wls/filesave/document/ddpm_image/image_document/2017/1121/ZZC0220171121001/10000096411503.png
     * @param inputStream ：对象流信息
     */
    public static void putObject(OSSClient ossClient, String buketName,String fileFullName, InputStream inputStream){
        logger.info("上传对象开始：");
        try{

            logger.info("原来Object的key="+fileFullName);
            //适应原来的影像资料格式。原来带有 / 开头 ，需要把此/去掉
            //反斜杠开头也去掉
            if(fileFullName.startsWith("/") || fileFullName.startsWith("\\")){
                logger.info("原传入的ObjectKey中开头带有/或\\,已自动去掉，但需要业务人员检查。");
                fileFullName = fileFullName.substring(1);
                logger.info("调整后Object的key="+fileFullName);
            }
            ossClient.putObject(buketName,fileFullName,inputStream);
        }catch (Exception e){
            logger.error("上传对象出错。"+e.getMessage());
            e.printStackTrace();
        }
        logger.info("上传对象结束。");
    }

    /**
     * 获取对象
     * @param ossClient
     * @param buketName :桶名
     * @param fileFullName ：全文件路径及名称(key)
     *                       例子：wls/filesave/document/ddpm_image/image_document/2017/1121/ZZC0220171121001/10000096411503.png
     *@return OSSObject 返回对象
     */
    public static OSSObject getObject(OSSClient ossClient,String buketName, String fileFullName){
        logger.info("获取对象开始：");
        OSSObject ossObj = null;
        try{
            //适应原来的影像资料格式。原来带有 / 开头 ，需要把此/去掉
            //反斜杠开头也去掉
            if(fileFullName.startsWith("/") || fileFullName.startsWith("\\")){
                logger.info("原传入的ObjectKey中开头带有/或\\,已自动去掉，但需要业务人员检查。");
                fileFullName = fileFullName.substring(1);
                logger.info("调整后Object的key="+fileFullName);
            }
            ossObj = ossClient.getObject(buketName, fileFullName);
        }catch (Exception e){
            logger.error("获取对象出错。"+e.getMessage());
            e.printStackTrace();
        }
        logger.info("获取对象结束。");
        return ossObj;
    }

    /**
     * 通过key删除对象
     * @param ossClient
     * @param buketName
     * @param objKey
     */
    public static void deleteObjectByKey(OSSClient ossClient,String buketName,String objKey){

        try{
            ossClient.deleteObject(buketName,objKey);

        }catch (Exception e){
            logger.error("删除object失败。"+e.getMessage());
            e.printStackTrace();
        }


    }

    /**
     *关闭输入流
     * @param ins
     */
    public static void   closeInputStream(InputStream ins){

        logger.info("关闭输入流--开始");
        if(ins != null){
            try {
                ins.close();
            } catch (IOException e) {
                logger.error("关闭输入流出错。"+e.getMessage());
                e.printStackTrace();
            }

        }
        logger.info("关闭输入流--结束");
    }
    /**
     * 关闭输出流
     * @param ops
     */
    public static void   closeOutputStream(OutputStream ops){
        logger.info("关闭输出流--开始");
        if(ops != null){
            try {
                ops.close();
            } catch (IOException e) {
                logger.error("关闭输出流出错。"+e.getMessage());
                e.printStackTrace();
            }

        }
        logger.info("关闭输出流--结束");
    }
}
