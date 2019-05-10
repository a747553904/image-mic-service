package com.ddjf.image.qrcode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

/**
 * 二维码图片解码处理
 * @author Frez
 *
 */
public class QRCodeDecoderHandler {
	
	public final static String CHARSET = "UTF-8";

	/**
	 * 解码图片二维码
	 * @param filePath
	 * @return
	 */
	public static String decoderQRCode(String filePath) {
		File file = new File(filePath);
		return decoderQRCode(file);
	}
	
	/**
	 * 解码图片二维码
	 * @param file
	 * @return
	 */
	public static String decoderQRCode(File file) {
		try {
			BufferedImage image = ImageIO.read(file);
			return decoderQRCode(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解码图片二维码
	 * @param input
	 * @return
	 */
	public static String decoderQRCode(InputStream input) {
		try {
			BufferedImage image = ImageIO.read(input);
			return decoderQRCode(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解码图片二维码
	 * @param image
	 * @return
	 */
	public static String decoderQRCode(BufferedImage image) {
		try {
			MultiFormatReader formatReader = new MultiFormatReader();
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			Binarizer binarizer = new HybridBinarizer(source);
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
			Map<DecodeHintType, String> hints = new EnumMap<DecodeHintType, String>(DecodeHintType.class);
			hints.put(DecodeHintType.CHARACTER_SET, QRCodeDecoderHandler.CHARSET);
			Result result = formatReader.decode(binaryBitmap, hints);
			/*System.out.println("result = " + result.toString());
			System.out.println("resultFormat = " + result.getBarcodeFormat());
			System.out.println("resultText = " + result.getText());*/
			return result.toString();
		} catch(NotFoundException e){
			//System.out.println("NotFoundException============");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		QRCodeDecoderHandler.decoderQRCode("D:/QRcode/1482377289956.jpg");
	}
}
