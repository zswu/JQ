/**
  * @(#)server.frm.panes.LogPane.java  2008-8-29  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: 日志显示类。
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-8-29 	小猪     		新建
  **/
package server.frm.panes;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

 /**
 * 日志显示类。
 */
public class LogPane extends JPanel implements ActionListener{

	private JButton btnLook = new JButton("查看所有日志");
	private JTextArea areaLog = new JTextArea();
	
	public LogPane() {
		init();
	}
	
	public JTextArea getAreaLog() {
		return areaLog;
	}

	/**
	 * 初始化面板。因为时间关系，可能写的比较戳，望见谅。
	 */
	private void init(){
		btnLook.addActionListener(this);
		JPanel paneNorth = new JPanel();
		paneNorth.setLayout(new FlowLayout(FlowLayout.RIGHT));
		paneNorth.add(btnLook);
		
		areaLog.setEditable(false);
		areaLog.setLineWrap(true);
		setLayout(new BorderLayout());
		add(paneNorth,BorderLayout.NORTH);
		add(new FillWidth(5,5),BorderLayout.EAST);
		add(new FillWidth(5,5),BorderLayout.WEST);
		add(new JScrollPane(areaLog),BorderLayout.CENTER);
	}
	
	/**
	 * 查看所有日志的事件。当前事件似乎未在Windows平台实现，其他平台未测试。Desktop类似乎未发生作用。
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnLook){
			if(Desktop.isDesktopSupported()){
				try {
					File files = new File("logs");
					if(files.exists()){
						System.out.println(files.getAbsolutePath());
						Desktop desktop = Desktop.getDesktop();
						for(File file:files.listFiles())
							desktop.open(file);
					}
				} catch (IOException e1) {
					System.out.println("打开文件时，发生错误:"+e1.getMessage());
				} 
			}else
				JOptionPane.showMessageDialog(null, "不支持文件打开!");
		}
	}
}
