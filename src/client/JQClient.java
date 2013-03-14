
package client;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

import tools.SetFont;
import client.frm.LoginPane;


public class JQClient {

	/**
	 * JQ客户端主程序类。<br>
	 * 加载字体到UIManager，启动登陆窗口。 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, new BufferedInputStream(JQClient.class.getResourceAsStream("/tools/simsun.ttc")));
			font = font.deriveFont(Font.PLAIN, 12);
			SetFont.setFont(font);
		} catch (FontFormatException e) {
			System.out.println("错误:"+e.getMessage());
		} catch (IOException e) {
			System.out.println("错误:"+e.getMessage());
		}

		new LoginPane();
	}

}
