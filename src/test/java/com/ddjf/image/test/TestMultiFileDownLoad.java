package com.ddjf.image.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author moyongfeng
 * @email: mo.yf@belle.com.cn
 * @date 2018/8/13 18:34
 * @copyright yougou.com
 * @description:
 */
public class TestMultiFileDownLoad {

    private static int position = 0;
    public static void doDownLoad(String url){
//        File targetFile=new File(TestMultiFileDownLoad.class.getResource("").getFile());
//        targetFile=targetFile.getParentFile();
//        targetFile=targetFile.getParentFile();
//        targetFile=targetFile.getParentFile();
//        targetFile=targetFile.getParentFile();
//        File targetFile=new File("d:/temp"+File.separator+ System.currentTimeMillis()+".jpg");
        File targetFile=new File("d:/temp");
        if(!targetFile.exists()){
            try {
                targetFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //File targetFile=new File("D://video.wmv");
        writeFile(url,targetFile);

    }
    private static InputStream getInputStream(String url, long startPosition){
        InputStream inputStream=null;
        HttpURLConnection conn=null;
        try {
            URL filePath=new URL(url);
            conn= (HttpURLConnection) filePath.openConnection();
            conn.setConnectTimeout(3*1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            long contentLength=conn.getContentLengthLong();

            if(startPosition<contentLength){
                // 设置断点续传的开始位置
                conn.disconnect();
                conn=(HttpURLConnection) filePath.openConnection();
                conn.setConnectTimeout(3*1000);
                //防止屏蔽程序抓取而返回403错误
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                conn.setRequestProperty("RANGE","bytes="+startPosition);
                contentLength=conn.getContentLengthLong();
                System.out.println(contentLength);
                inputStream = conn.getInputStream();
            }else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }/*finally {
            conn.disconnect();
        }*/
        return inputStream;
    }
    private static void writeFile(String url,File targetFile){
        // 数据读写
        byte[] buffer = new byte[1024*1024];
        int bytesWritten = 0;
        int byteCount = 0;
        InputStream inputStream =null;
        FileOutputStream fos=null;
        long fileLength=0;
        try {
            fos=new FileOutputStream(targetFile,true);
            fileLength=targetFile.length();
            inputStream = getInputStream(url,fileLength);
            while ((byteCount = inputStream.read(buffer)) != -1) {
                fos.write(buffer, bytesWritten, byteCount);
            }
        }catch (Exception e){
            //e.printStackTrace();
            try {
                inputStream.close();
                fos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            writeFile(url,targetFile);
        }

    }
    public static void main(String[] args) {
        String filepath = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534167036841&di=fa85021ba1557cf225ebe05b839f6746&imgtype=0&src=http%3A%2F%2Fimg17.3lian.com%2Fd%2Ffile%2F201702%2F18%2Fb8de44216a55b279e30453ffe92ee22d.jpg";
        doDownLoad(filepath);
    }

}
