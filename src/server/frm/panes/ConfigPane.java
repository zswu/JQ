/**
  * @(#)server.frm.panes.ConfigPane.java  2008-8-28  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: ϵͳ�����ࡣ
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-8-28 	С��     		�½�
  **/
package server.frm.panes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import server.frm.Server;
import tools.GetParameter;

 /**
 * ϵͳ�����ࡣ<br>
 * ��ɵĹ���:1.�˿����á�
 * 2.Ҫ���ε�JQ��
 * 3.���ɵ�JQ�ŵ�λ������Сλ�����λ
 * 4.�Ƿ��Զ�������־���Զ�ɾ����־������
 * 5.���ݷ�ʽѡ��1.�ļ���ʽ2.���ݿⷽʽ(���Ӳ������û��������롢���뼯)
 * 2008-8-28
 * @author		���ڿƼ�[Tarena Training Group]
 * @version	1.0
 * @since		JDK1.6(����) 
 */
public class ConfigPane extends JPanel implements ActionListener{

	/** ϵͳ�����Ķ˿� */
	private JLabel lblPort = new MyJLalel("��    ��",90);
	private JTextField txtPort = new MyJTextField();
	/** JQҪ���������JQ�� */
	private JLabel lblNoJQ = new MyJLalel("���κ���",90);
	private JTextArea areaNoJQ = new JTextArea();
	/** ����JQ����Сλ�������λ�� */
	private JLabel lblMinDigit = new MyJLalel("��Сλ��",90);
	private JTextField txtMinDigit = new MyJTextField();
	private JLabel lblMaxDigit = new MyJLalel("��Сλ��",90);
	private JTextField txtMaxDigit = new MyJTextField();
	/** �Ƿ��Զ�������־ */
	private JLabel lblIsAutoBak = new MyJLalel("������־",90);
	private JCheckBox cbIsAutoBak = new JCheckBox("�Զ�������־",true);
	/** �Զ�ɾ��n��ǰ����־ */
	private JLabel lblDelDay = new MyJLalel("�Զ�ɾ��",90);
	private JTextField txtDelDay = new MyJTextField();
	/** �������ݵķ�ʽ */
	private JLabel lblDataWay  = new MyJLalel("���ݷ�ʽ",90);
	private JRadioButton rbFile = new JRadioButton("�ļ���ʽ");
	private JRadioButton rbData = new JRadioButton("���ݿⷽʽ");
	
	private JLabel lblDriver = new MyJLalel("��������",90);
	private JTextField txtDriver = new MyJTextField();
	/** ���Ӳ��� */
	private JLabel lblLinkParameter = new MyJLalel("���Ӳ���",90);
	private JTextField txtLinkParameter = new MyJTextField();
	/** �û��� */
	private JLabel lblUserName = new MyJLalel("�� �� ��",90);
	private JTextField txtUserName = new MyJTextField();
	/** ���� */
	private JLabel lblPassword = new MyJLalel("��    ��",90);
	private JTextField txtPassword = new MyJTextField();
	/** ���� */
	private JLabel lblDBName = new MyJLalel("���ݿ���",90);
	private JTextField txtDBName = new MyJTextField();
	/** ���뼯 */
	private JLabel lblCharset = new MyJLalel("�� �� ��",90);
	private JTextField txtCharset = new MyJTextField();
	
	/** �������� */
	private JButton btnSave = new JButton("��������");
	private JButton btnDefault = new JButton("�ָ�ȱʡֵ");
	
	public ConfigPane(Properties prop) {
		cbIsAutoBak.addActionListener(this);
		rbFile.addActionListener(this);
		rbData.addActionListener(this);
		btnDefault.addActionListener(this);
		btnSave.addActionListener(this);
		
		init();
		try {
			getProp(prop);
		} catch (Exception e) {
			System.out.println("����:"+e.getMessage());
		}
	}
	
	/**
	 * ��ʼ����塣��Ϊʱ���ϵ������д�ıȽϴ��������¡�
	 */
	private void init(){
		
		areaNoJQ.setLineWrap(true);
		JScrollPane sp = new JScrollPane(areaNoJQ);
		sp.setPreferredSize(new Dimension(170,70));
		
		cbIsAutoBak.setPreferredSize(new Dimension(170,20));
		
		ButtonGroup group = new ButtonGroup();
		group.add(rbData);
		group.add(rbFile);
		rbFile.setSelected(true);
		setDataWay(rbData.isSelected());
		rbData.setPreferredSize(new Dimension(85,20));
		rbFile.setPreferredSize(new Dimension(75,20));
		
		
		JPanel paneSet = new JPanel();
		paneSet.setPreferredSize(new Dimension(520, 220));
		paneSet.setLayout(new FlowLayout(FlowLayout.CENTER,10,3));
		paneSet.setBorder(new TitledBorder(new LineBorder(Color.DARK_GRAY)," ���������� "));
		
		paneSet.add(lblPort);
		paneSet.add(txtPort);
		paneSet.add(new MyJLalel("JQ�����������������Ķ˿�"));
		paneSet.add(lblMinDigit);
		paneSet.add(txtMinDigit);
		paneSet.add(new MyJLalel("����JQ����Сλ��"));
		paneSet.add(lblMaxDigit);
		paneSet.add(txtMaxDigit);
		paneSet.add(new MyJLalel("����JQ�����λ��"));
		paneSet.add(lblNoJQ);
		paneSet.add(sp);
		paneSet.add(new MyJLalel("Ҫ���ε�JQ�ţ��÷ֺ�[\";\"]�ָ�"));
		
		paneSet.add(lblIsAutoBak);
		paneSet.add(cbIsAutoBak);
		paneSet.add(new MyJLalel("�Ƿ񱸷���־"));
		paneSet.add(lblDelDay);
		paneSet.add(txtDelDay);
		paneSet.add(new MyJLalel("�Զ�ɾ��������ǰ����־"));
		
		JPanel paneDataWay = new JPanel();
		paneDataWay.setPreferredSize(new Dimension(520, 190));
		paneDataWay.setLayout(new FlowLayout(FlowLayout.CENTER,10,3));
		paneDataWay.setBorder(new TitledBorder(new LineBorder(Color.DARK_GRAY)," ���ݴ洢��ʽ "));
		
		paneDataWay.add(lblDataWay);
		paneDataWay.add(rbFile);
		paneDataWay.add(rbData);
		paneDataWay.add(new MyJLalel("���ݱ�����õķ�ʽ[���ݿ�δʵ��]"));
		paneDataWay.add(lblDriver);
		paneDataWay.add(txtDriver);
		paneDataWay.add(new MyJLalel("���ݿ����������"));
		paneDataWay.add(lblLinkParameter);
		paneDataWay.add(txtLinkParameter);
		paneDataWay.add(new MyJLalel("���ݿ������������Ӳ���"));
		paneDataWay.add(lblUserName);
		paneDataWay.add(txtUserName);
		paneDataWay.add(new MyJLalel("���ݿ�de�û���  "));
		paneDataWay.add(lblPassword);
		paneDataWay.add(txtPassword);
		paneDataWay.add(new MyJLalel("���ݿ�de����"));
		paneDataWay.add(lblDBName);
		paneDataWay.add(txtDBName);
		paneDataWay.add(new MyJLalel("���ݿ�de����"));
		paneDataWay.add(lblCharset);
		paneDataWay.add(txtCharset);
		paneDataWay.add(new MyJLalel("�������ݿ���뼯"));
		
		JPanel paneBtn = new JPanel();
		paneBtn.setPreferredSize(new Dimension(520,30));
		paneBtn.setLayout(new FlowLayout(FlowLayout.RIGHT,50,5));
		paneBtn.add(btnDefault);
		//paneBtn.add(new FillWidth(50,20));
		paneBtn.add(btnSave);
		
		setLayout(new FlowLayout(FlowLayout.CENTER,10,5));
		add(paneSet);
		//add(paneLog);
		add(paneDataWay);
		add(paneBtn);
	}
	
	/**
	 * ���水ť���ظ�ȱʡֵ��ť�ȵ��¼���
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==cbIsAutoBak){
			txtDelDay.setEditable(cbIsAutoBak.isSelected());
		}
		if(e.getSource()==rbFile)
			setDataWay(false);
		if(e.getSource()==rbData)
			setDataWay(true);
		if(e.getSource()==btnDefault){
			try {
				GetParameter.setDefault();
				getProp(Server.prop);
			} catch (Exception e1) {
				System.out.println("����:"+e1.getMessage());
			}
			//
		}
		if(e.getSource()==btnSave){
			try {
				setProp();
			} catch (IOException e1) {
				System.out.println("����:"+e1.getMessage());
			}
		}
	}
	
	private void setDataWay(boolean b){
		txtDriver.setEditable(b);
		txtLinkParameter.setEditable(b);
		txtUserName.setEditable(b);
		txtPassword.setEditable(b);
		txtDBName.setEditable(b);
		txtCharset.setEditable(b);
	}
	
	private class MyJLalel extends JLabel{
		
		public MyJLalel(String text,int width) {
			super(text);
			setHorizontalAlignment(SwingConstants.RIGHT);
			setSize(width,20);
			setPreferredSize(new Dimension(width,20));
		}
		public MyJLalel(String text) {
			super(text);
			setHorizontalAlignment(SwingConstants.LEFT);
			setSize(200,20);
			setPreferredSize(new Dimension(220,20));
		}
	}
	
	private class MyJTextField extends JTextField{
		public MyJTextField() {
			this(170);
		}
		public MyJTextField(int width) {
			super();
			setSize(width,20);
			setPreferredSize(new Dimension(width,20));
		}
	}
	
	/**
	 * ��ȡϵͳ�������ļ�
	 * @param prop �����ļ�ӳ���Properties����
	 * @throws Exception
	 */
	public void getProp(Properties prop) throws Exception{
			
			int len = GetParameter.keys.length;
			String[] values = new String[len]; 
			for(int i=0;i<len;i++)
				values[i] = prop.getProperty(GetParameter.keys[i]);
			txtPort.setText(values[0]);
			txtMinDigit.setText(values[1]);
			txtMaxDigit.setText(values[2]);
			areaNoJQ.setText(values[3]);
			int isAutoBak = Integer.parseInt(values[4]);
			if(isAutoBak==1)
				cbIsAutoBak.setSelected(true);
			else
				cbIsAutoBak.setSelected(false);
			txtDelDay.setText(values[5]);
			txtDelDay.setEditable(cbIsAutoBak.isSelected());
			
			int dataWay = Integer.parseInt(values[6]);
			if(dataWay==0)
				rbFile.setSelected(true);
			
			txtDriver.setText(values[7]);
			txtLinkParameter.setText(values[8]);
			txtUserName.setText(values[9]);
			txtPassword.setText(values[10]);
			txtDBName.setText(values[11]);
			txtCharset.setText(values[12]);
			setDataWay(rbData.isSelected());
	}
	
	/**
	 * ���������ļ���
	 * @throws IOException
	 */
	public void setProp() throws IOException{
		String values[] = {
				txtPort.getText(),txtMinDigit.getText(),txtMaxDigit.getText(),areaNoJQ.getText(),
				cbIsAutoBak.isSelected()?"1":"0",txtDelDay.getText(),rbFile.isSelected()?"0":"1",
				txtDriver.getText(),txtLinkParameter.getText(),txtUserName.getText(),txtPassword.getText(),txtDBName.getText(),txtCharset.getText()};
		GetParameter.saveProp(GetParameter.keys, values);
	}
}
