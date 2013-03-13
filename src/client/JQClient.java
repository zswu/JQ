/**
  * @(#)client.JQClient.java  2008-8-29  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: JQ�ͻ����������ࡣ
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-8-29 	С��     		�½�
  **/
package client;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

import tools.SetFont;
import client.frm.LoginPane;

 /**
 * JQ�ͻ����������ࡣ
 * 2008-8-29
 * @author		���ڿƼ�[Tarena Training Group]
 * @version	1.0
 * @since		JDK1.6(����) 
 */
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
