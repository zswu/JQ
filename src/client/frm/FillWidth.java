/**
  * @(#)server.frm.panes.FillWidth.java  2008-8-28  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: ����Ϊ�����BorderLayout�Ŀ��
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-8-28 	С��     		�½�
  **/
package client.frm;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

 /**
 * ����Ϊ�����BorderLayout�Ŀ��
 * 2008-8-28
 * @author		���ڿƼ�[Tarena Training Group]
 * @version	1.0
 * @since		JDK1.6(����) 
 */
public class FillWidth extends JPanel {

	public FillWidth(int width,int height) {
		setPreferredSize(new Dimension(width,height));
	}
	public FillWidth(int width,int height,Color color) {
		this(width,height);
		setOpaque(true);
		setBackground(color);
	}
}
