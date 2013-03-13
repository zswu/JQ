/**
  * @(#)tools.FileDeal.java  2008-9-2  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: 此处输入简单类说明
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-9-2 	小猪     		新建
  **/
package tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

 /**
 * 此处加入类详细说明
 * 2008-9-2
 * @author		达内科技[Tarena Training Group]
 * @version	1.0
 * @since		JDK1.6(建议) 
 * @author		Administrator
 */
public class FileDeal {

	/**
	 * 根据文件的后缀名判断文件是否属于某种类型的
	 * @param file 文件
	 * @param suffixName 后缀名
	 * @return 以该后缀名结束返回true，否则false
	 */
	public static boolean isKindOFType(File file,String suffixName){
		String name = file.getName();
		if(name.endsWith(suffixName))
			return true;
		else
			return false;
	}
	
	/**
	 * 加载BufferedImage对象。
	 * @param fileName 图像文件的路径。
	 * @return 返回加载后的图像。路径错误或发生未知错误时均返回空。
	 */
	public static BufferedImage getICON(String fileName){
		try {
			return ImageIO.read(FileDeal.class.getResource(fileName));
		} catch (IOException e) {
			System.out.println("加载图标["+fileName+"]文件时发生错误:"+e.getMessage());
			return null;
		}
	}
}
