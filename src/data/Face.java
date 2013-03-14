
package data;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

 /**
 * 表情类。
 */
public class Face extends ImageIcon{

	private int num;
	private String path;
	
	/**
	 * 缺省构造第一个表情。
	 */
	public Face() {
		this(1);
	}
	/**
	 * 按num构造表情。
	 * @param num 表情的文件名。
	 */
	public Face(int num) {
		super(Face.class.getResource("/client/images/face/"+num+".gif"));
		this.num = num;
		path = "/client/images/face/"+num+".gif";
		//setImage(ImageIO.read(Face.class.getResource(path)));
		//setImage(new ImageIcon(Face.class.getResource(path)));
	}
	
	
}
