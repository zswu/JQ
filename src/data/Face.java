
package data;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

 /**
 * �����ࡣ
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
