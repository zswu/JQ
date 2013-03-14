
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
	 * JQ�ͻ����������ࡣ<br>
	 * �������嵽UIManager��������½���ڡ� 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, new BufferedInputStream(JQClient.class.getResourceAsStream("/tools/simsun.ttc")));
			font = font.deriveFont(Font.PLAIN, 12);
			SetFont.setFont(font);
		} catch (FontFormatException e) {
			System.out.println("����:"+e.getMessage());
		} catch (IOException e) {
			System.out.println("����:"+e.getMessage());
		}

		new LoginPane();
	}

}
