
package server.frm.panes;

import java.awt.Dimension;

import javax.swing.JPanel;

 /**
 * 仅仅为了填充BorderLayout的宽度
 */
public class FillWidth extends JPanel {

	public FillWidth(int width,int height) {
		setPreferredSize(new Dimension(width,height));
	}
}
