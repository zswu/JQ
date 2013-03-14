
package tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

 /**
 * �˴���������ϸ˵��
 */
public class FileDeal {

	/**
	 * �����ļ��ĺ�׺���ж��ļ��Ƿ�����ĳ�����͵�
	 * @param file �ļ�
	 * @param suffixName ��׺��
	 * @return �Ըú�׺����������true������false
	 */
	public static boolean isKindOFType(File file,String suffixName){
		String name = file.getName();
		if(name.endsWith(suffixName))
			return true;
		else
			return false;
	}
	
	/**
	 * ����BufferedImage����
	 * @param fileName ͼ���ļ���·����
	 * @return ���ؼ��غ��ͼ��·���������δ֪����ʱ�����ؿա�
	 */
	public static BufferedImage getICON(String fileName){
		try {
			return ImageIO.read(FileDeal.class.getResource(fileName));
		} catch (IOException e) {
			System.out.println("����ͼ��["+fileName+"]�ļ�ʱ��������:"+e.getMessage());
			return null;
		}
	}
}
