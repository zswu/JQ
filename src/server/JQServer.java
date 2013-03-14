/**
  * @(#)server.JQServer.java  2008-8-28  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: 服务端主程序类。
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-8-28 	小猪     		新建
  **/
package server;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import dao.UserDAOByFile;

import server.frm.Server;
import tools.JQCreater;
import tools.SetFont;

 /**
 * 服务端主程序类。
 */
public class JQServer {

	public final static int manager = 10000;
	/**
	 * 加载字体，弹出程序界面。
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT,new BufferedInputStream(JQServer.class.getResourceAsStream("/tools/simsun.ttc")));
			font = font.deriveFont(Font.PLAIN, 12);
			SetFont.setFont(font);
		} catch (FontFormatException e) {
			System.out.println("错误:"+e.getMessage());
		} catch (IOException e) {
			System.out.println("错误:"+e.getMessage());
		} 
		new Server();
		new Thread(){
			public void run() {
				try {
					new UserDAOByFile().addSysUser(manager);
					new JQCreater().saveIDJQ(1, manager);
				} catch (IOException e) {
					System.out.println("错误:"+e.getMessage());
				}
			}
		}.start();
	}

}
