/**
  * @(#)data.Face.java  2008-9-17  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: 表情类。
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-9-17 	小猪     		新建
  **/
package data;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

 /**
 * 表情类。
 * 2008-9-17
 * @author		达内科技[Tarena Training Group]
 * @version	1.0
 * @since		JDK1.6(建议) 
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
