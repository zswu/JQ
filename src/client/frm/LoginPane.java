/**
  * @(#)client.frm.LoginPane.java  2008-8-29  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: ��¼����ࡣ
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-8-29 	С��     		�½�
  **/
package client.frm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import data.UserState;

 /**
 * ��¼����ࡣ<br>
 * 2008-8-29
 * @author		���ڿƼ�[Tarena Training Group]
 * @version	1.0
 * @since		JDK1.6(����) 
 */
public class LoginPane extends JFrame implements ActionListener{

	private JLabel lblImg = new JLabel();
	private JLabel lblJQNum = new JLabel("JQ�˺�");
	private JComboBox boxJQNum = new JComboBox();
	private JLabel lblPassword = new JLabel("JQ����");
	private JPasswordField pfPassword = new JPasswordField();
	
	private JLabel lblState = new JLabel("״̬:");
	private JComboBox boxState = new JComboBox();
	private JCheckBox boxAutoLogin = new JCheckBox("�Զ���¼");
	
	private JButton btnRegister = new JButton("�����˺�");
	private JButton btnSet = new JButton("���á�");
	private JButton btnLogin = new JButton("��¼");
	
	private JLabel lblServerIP = new JLabel("������IP:");
	private JTextField txtServerIP = new JTextField("127.0.0.1");
	private JLabel lblServerPort = new JLabel("�˿�:");
	private JTextField txtServerPort = new JTextField("3608");
	private boolean isSet = false;
	/**
	 * ��½���塣
	 * 
	 */
	public LoginPane() {
		setTitle("JQ�û���¼");
		setSize(324,235);
		setResizable(false);
		Toolkit tk=Toolkit.getDefaultToolkit();
		setLocation((tk.getScreenSize().width-getSize().width)/2,(tk.getScreenSize().height-getSize().height)/2);
		setBackground(new Color(224,244,251));
		
		init();
		btnLogin.addActionListener(this);
		btnRegister.addActionListener(this);
		btnSet.addActionListener(this);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	/**
	 * ��ʼ����½�������塣
	 */
	private void init(){
		lblImg.setIcon(new ImageIcon(LoginPane.class.getResource("/client/images/top.gif")));
		lblImg.setPreferredSize(new Dimension(325,47));
		
		boxState.addItem(UserState.ONLINESTATE);
		boxState.addItem(UserState.HIDDENSTATE);
		boxState.addItem(UserState.DEPARTURESTATE);
		boxState.addItem(UserState.BUSYSTATE);
		boxState.setBackground(new Color(240,250,255));
		boxState.setPreferredSize(new Dimension(60,20));
		boxAutoLogin.setBackground(new Color(240,250,255));
		
		boxJQNum.setEditable(true);
		boxJQNum.setPreferredSize(new Dimension(140,20));
		pfPassword.setPreferredSize(new Dimension(140,20));
		
		txtServerIP.setPreferredSize(new Dimension(80,20));
		txtServerPort.setPreferredSize(new Dimension(50,20));
		
		JPanel pane = new JPanel();
		pane.setBackground(new Color(240,250,255));
		pane.setBorder(new LineBorder(new Color(144,185,215)));
		pane.setLayout(new FlowLayout(FlowLayout.CENTER,15,12));
		pane.setPreferredSize(new Dimension(300,110));
		
		
		pane.add(new FillWidth(20,20,new Color(240,250,255)));
		pane.add(lblJQNum);
		pane.add(boxJQNum);
		pane.add(new FillWidth(20,20,new Color(240,250,255)));
		pane.add(new FillWidth(20,20,new Color(240,250,255)));
		pane.add(lblPassword);
		pane.add(pfPassword);
		pane.add(new FillWidth(20,20,new Color(240,250,255)));
		pane.add(lblState);
		pane.add(boxState);
		pane.add(boxAutoLogin);
		
		TitledBorder tb = new TitledBorder(new LineBorder(Color.GRAY),"��������");
		JPanel paneSet = new JPanel();
		paneSet.setPreferredSize(new Dimension(300,60));
		paneSet.setBorder(tb);
		paneSet.add(lblServerIP);
		paneSet.add(txtServerIP);
		paneSet.add(new FillWidth(30,20));
		paneSet.add(lblServerPort);
		paneSet.add(txtServerPort);
		
		setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
		add(lblImg);
		add(new FillWidth(100,8));
		add(pane);
		add(new FillWidth(300,8));
		add(btnRegister);
		add(btnSet);
		add(new FillWidth(65,20));
		add(btnLogin);
		add(new FillWidth(300,8));
		add(paneSet);
		
	}
	
	/**
	 * ���ð�ť����½��ť��ע�ᰴť���¼���
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnSet){
			if(isSet){
				isSet = false;
				setSize(getWidth(),getHeight()-65);
				btnSet.setText("���á�");
			}else{
				isSet = true;
				setSize(getWidth(),getHeight()+65);
				btnSet.setText("���á�");
			}
		}
		if(e.getSource()==btnLogin){
			dispose();
			new MainPane(txtServerIP.getText(),Integer.parseInt(txtServerPort.getText()),Integer.parseInt(boxJQNum.getSelectedItem().toString()),new String(pfPassword.getPassword()),((UserState)boxState.getSelectedItem()).getState());
		}
		if(e.getSource()==btnRegister){
			dispose();
			new RegisterPane();
		}
		
	}
}
