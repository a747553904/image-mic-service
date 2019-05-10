package com.ddjf.image.qrcode;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.imageio.ImageIO;


/**
 * 获取二维码图片
 * @author Frez
 *
 */
public class BufferedImageHandler {

	/**
	 * 获取二维码区域截图
	 * @param srcPath
	 * @return
	 */
	public static BufferedImage getSubBufferedImage(String srcPath){
		return getSubBufferedImage(new File(srcPath));
	}
	
	
	/**
	 * 获取二维码区域截图
	 * @param file
	 * @return
	 */
	public static BufferedImage getSubBufferedImage(File file){
		try {
			BufferedImage bufImg = ImageIO.read(file);
			return getSubBufferedImage(bufImg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取二维码区域截图
	 * @param input
	 * @return
	 */
	public static BufferedImage getSubBufferedImage(InputStream input){
		try {
			BufferedImage bufImg = ImageIO.read(input);
			return getSubBufferedImage(bufImg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取二维码区域截图
	 * @param bytes
	 * @return
	 */
	public static BufferedImage getSubBufferedImage(byte[] bytes){
		try {
			InputStream input = new ByteArrayInputStream(bytes); 
			BufferedImage bufImg = ImageIO.read(input);
			return getSubBufferedImage(bufImg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取二维码区域截图
	 * @param bufImg
	 * @return
	 */
	public static BufferedImage getSubBufferedImage(BufferedImage bufImg){
		try {
			int width = bufImg.getWidth();
			//int heght = bufImg.getHeight();
			//System.out.println("width="+width);
			//System.out.println("Height="+heght);
			return bufImg.getSubimage(width-200, 0, 200, 200);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 生成二维码区域截图
	 * @param bufImg
	 * @param formatName
	 * @param tarPath
	 */
	public static void writeBufferedImage(BufferedImage bufImg, String formatName, String tarPath){
		try {
			ImageIO.write(bufImg, formatName, new File(tarPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		BufferedImage bufImg = BufferedImageHandler.getSubBufferedImage("D:/QRcode/RL20161221000002.jpg");
		BufferedImageHandler.writeBufferedImage(bufImg, "jpg", "D:/QRcode/jietu/"+new Date().getTime()+".jpg");
		System.out.println("处理成功===============");
	}
}