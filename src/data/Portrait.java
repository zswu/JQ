/**
  * @(#)data.Portrait.java  2008-8-30  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: 头像类。
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-8-30 	小猪     		新建
  **/
package data;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


 /**
 * 头像类。
 * 2008-8-30
 * @author		达内科技[Tarena Training Group]
 * @version	1.0
 * @since		JDK1.6(建议) 
 */
public class Portrait extends ImageIcon{

	private int num;
	private String path;
	
	public Portrait() {
		this(1);
	}
	public Portrait(int num) {
		super();
		try {
			this.num = num;
			path = "/client/images/portrait/"+num+".jpg";
			setImage(ImageIO.read(Portrait.class.getResource(path)));
		} catch (IOException e) {
			System.out.println("加载头像时发生错误"+e.getMessage());
		}
	}
	
	public Portrait(int num,Integer state) {
		super();
		try {
			this.num = num;
			if(state==UserState.OFFLIENSTATE.getState())
				path = "/client/images/portrait/"+num+"_h.jpg";
			else
				path = "/client/images/portrait/"+num+".jpg";
			setImage(ImageIO.read(Portrait.class.getResource(path)));
		} catch (IOException e) {
			System.out.println("加载头像时发生错误"+e.getMessage());
		}
	}
	
	public Portrait(int num,Integer state,boolean isBig) {
		super();
		try {
			this.num = num;
			if(state==UserState.OFFLIENSTATE.getState()){
				if(isBig)
					path = "/client/images/portrait/"+num+"_h.jpg";
				else
					path = "/client/images/portrait/"+num+"_m_h.jpg";
			}
			else{
				if(isBig)
					path = "/client/images/portrait/"+num+".jpg";
				else
					path = "/client/images/portrait/"+num+"_m.jpg";
			}
			//System.out.println(path);
			setImage(ImageIO.read(Portrait.class.getResource(path)));
		} catch (IOException e) {
			System.out.println("加载头像时发生错误"+e.getMessage());
		}
	}
	
	public Portrait(String path){
		super(path);
	}
	
	public String getPath() {
		return path;
	}
	
	public String toString() {
		return path;
	}
	public int getNum() {
		return num;
	}
}
