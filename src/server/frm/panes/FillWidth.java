
package server.frm.panes;

import java.awt.Dimension;

import javax.swing.JPanel;

 /**
 * ����Ϊ�����BorderLayout�Ŀ��
 */
public class FillWidth extends JPanel {

	public FillWidth(int width,int height) {
		setPreferredSize(new Dimension(width,height));
	}
}
