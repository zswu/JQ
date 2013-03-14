
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
 * ������������ࡣ
 */
public class JQServer {

	public final static int manager = 10000;
	/**
	 * �������壬����������档
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT,new BufferedInputStream(JQServer.class.getResourceAsStream("/tools/simsun.ttc")));
			font = font.deriveFont(Font.PLAIN, 12);
			SetFont.setFont(font);
		} catch (FontFormatException e) {
			System.out.println("����:"+e.getMessage());
		} catch (IOException e) {
			System.out.println("����:"+e.getMessage());
		} 
		new Server();
		new Thread(){
			public void run() {
				try {
					new UserDAOByFile().addSysUser(manager);
					new JQCreater().saveIDJQ(1, manager);
				} catch (IOException e) {
					System.out.println("����:"+e.getMessage());
				}
			}
		}.start();
	}

}
