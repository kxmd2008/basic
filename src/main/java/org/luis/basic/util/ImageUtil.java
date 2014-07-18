package org.luis.basic.util;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageUtil {
	public static final int IMAGE_SIZE = 120;
	
	public static void createPreviewImage(String srcFile, String destFile) {
		createPreviewImage(srcFile, destFile, IMAGE_SIZE);
	}

	/**
	 * 生成缩略图
	 * @param srcFile
	 * @param destFile
	 */
	public static void createPreviewImage(String srcFile, String destFile, int imgSize) {
		try {
			// 新建源图片和生成的小图片的文件对象
			File fi = new File(srcFile); // src
			File fo = new File(destFile); // dest
			BufferedImage bis = ImageIO.read(fi);
			// 原图像的长度,宽度
			int srcWidth = bis.getWidth();
			int srcHeight = bis.getHeight();
			//图像的长宽比例 
//			double scale = (double) w / h;
			int nw = imgSize; 
			int nh = (nw * srcHeight) / srcWidth;
			if (nh > imgSize) {
				nh = imgSize;
				nw = (nh * srcWidth) / srcHeight;
			}
			double sx = (double) nw / srcWidth;
			double sy = (double) nh / srcHeight;
			// 生成图像变换对象
			AffineTransform transform = new AffineTransform();
			// 设置图像转换的比例  
			transform.setToScale(sx, sy);
			AffineTransformOp ato = new AffineTransformOp(transform, null);
			//生成缩小图像的缓冲对象
			BufferedImage bid = new BufferedImage(nw, nh,
					BufferedImage.TYPE_3BYTE_BGR);
			//生成小图像 
			ato.filter(bis, bid);
			//输出小图像  
			ImageIO.write(bid, "jpeg", fo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(
					" Failed in create preview image. Error:  "
							+ e.getMessage());
		}
	}
}
