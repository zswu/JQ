
package server.frm;

import java.awt.Toolkit;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import server.frm.panes.ConfigPane;
import server.frm.panes.LogPane;
import server.frm.panes.OnlinePane;
import server.frm.panes.ServicePane;
import server.frm.panes.UserPane;
 /**
 * JavaQQ������ࡣ<br>
 * ��ɵĹ��ܣ�1.ϵͳ����<br>
 * 2.ϵͳ����<br>
 * 3.�û�����<br>
 * 4.�����û�<br>
 * 5.��־����<br>
 * 6.����<br>
 */
public class Server extends JFrame implements ChangeListener{

	/** ѡ� */
	private JTabbedPane panes = null;
	/** ϵͳ������� */
	private ServicePane servicePane = new ServicePane(); 
	/** ϵͳ������� */
	private ConfigPane configPane = null;
	/** �û�������� */
	private UserPane userPane = new UserPane();
	/** �����û���� */
	private OnlinePane onlinePane = new OnlinePane();
	/** ��־��ʾ��� */
	public static LogPane logPane = new LogPane();
	/** ���������ļ� */
	public static Properties prop = null;
	
	public static boolean isFileWay = true;
	public static boolean isSaveLog = true;
	
	public Server() {
		servicePane.initProp();
		
		configPane = new ConfigPane(prop);
		setTitle("Java QQ �����");
		setSize(600,520);
		setResizable(false);
		Toolkit tk=Toolkit.getDefaultToolkit();
		setLocation((tk.getScreenSize().width-getSize().width)/2,(tk.getScreenSize().height-getSize().height)/2);
		
		panes = new JTabbedPane();
		panes.add("ϵͳ����",servicePane);
		panes.add("ϵͳ����",configPane);
		panes.add("�û�����",userPane);
		panes.add("�����û�",onlinePane);
		panes.add("��־",logPane);
		
		add(panes);
		panes.addChangeListener(this);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	/**
	 * ѡ���û����������û�ѡ�ʱ�������������û��͵�ǰ�����û���
	 */
	public void stateChanged(ChangeEvent e) {		
		if(panes.getSelectedComponent()==onlinePane)
			onlinePane.flushOnlineUser();
		if(panes.getSelectedComponent()==userPane)
			userPane.flushUser();
	}
}
