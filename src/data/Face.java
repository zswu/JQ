/**
  * @(#)data.Face.java  2008-9-17  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: �����ࡣ
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-9-17 	С��     		�½�
  **/
package data;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

 /**
 * �����ࡣ
 * 2008-9-17
 * @author		���ڿƼ�[Tarena Training Group]
 * @version	1.0
 * @since		JDK1.6(����) 
 */
public class Face extends ImageIcon{

	private int num;
	private String path;
	
	/**
	 * ȱʡ�����һ�����顣
	 */
	public Face() {
		this(1);
	}
	/**
	 * ��num������顣
	 * @param num ������ļ�����
	 */
	public Face(int num) {
		super(Face.class.getResource("/client/images/face/"+num+".gif"));
		this.num = num;
		path = "/client/images/face/"+num+".gif";
		//setImage(ImageIO.read(Face.class.getResource(path)));
		//setImage(new ImageIcon(Face.class.getResource(path)));
	}
	
	
}
