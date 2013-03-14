
package client.frm;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import tools.DateDeal;

import data.JQMessage;
import data.Portrait;
import data.RegUser;
import data.User;
import data.UserSex;
import data.UserState;

 /**
 * 新用户注册类。
 */
public class RegisterPane extends JFrame implements ActionListener{
	private JLabel lblNickName = new JLabel("Nickname:");
	private JLabel lblEmail = new JLabel("E-mail:");
	private JLabel lblPassword = new JLabel("Password:");
	private JLabel lblRePass = new JLabel("Confirm Password:");
	private JLabel lblSex = 	new JLabel("Sex:");
	private JLabel lblAge = new JLabel("Age:");
	private JLabel lblRealName = new JLabel("Name:");
	private JLabel lblSignature = new JLabel("Signature:");
	
	private JTextField txtNickName = new JTextField();
	private JTextField txtEmail= new JTextField();
	private JPasswordField pfPassword = new JPasswordField();
	private JPasswordField pfRePass = new JPasswordField();
	private JComboBox  boxSex = new JComboBox();
	private JTextField txtAge = new JTextField();
	private JTextField txtRealName = new JTextField();
	private JTextArea areaSignature = new JTextArea();
	
	private JLabel lblPhoto = new JLabel();
	private JButton btnChange = new JButton("Photo");
	
	private JButton btnSet = new JButton("Setup↓");
	private JButton btnOk = new JButton("Register");
	private JButton btnCancle = new JButton("Cancel");
	
	private ChooseProtrait chooseProtrait = null;
	
	private JLabel lblServerIP = new JLabel("Server IP:");
	private JTextField txtServerIP = new JTextField("127.0.0.1");
	private JLabel lblServerPort = new JLabel("Port:");
	private JTextField txtServerPort = new JTextField("3608");
	private boolean isSet = false;
	
	private Socket client = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	
	public RegisterPane() {
		setTitle("Register");
		setSize(330,343);
		setResizable(false);
		Toolkit tk=Toolkit.getDefaultToolkit();
		setLocation((tk.getScreenSize().width-getSize().width)/2,(tk.getScreenSize().height-getSize().height)/2);
		
		init();
		
		btnCancle.addActionListener(this);
		btnChange.addActionListener(this);
		btnOk.addActionListener(this);
		btnSet.addActionListener(this);
		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	/**
	 * 初始化面板。因为时间关系，可能写的比较戳，望见谅。
	 */
	private void init(){
		setLayout(null);
		lblNickName.setPreferredSize(new Dimension(60,20));
		lblEmail.setPreferredSize(new Dimension(60,20));
		lblPassword.setPreferredSize(new Dimension(60,20));
		lblRePass.setPreferredSize(new Dimension(60,20));
		lblSex.setPreferredSize(new Dimension(60,20));
		lblAge.setPreferredSize(new Dimension(30,20));
		lblRealName.setPreferredSize(new Dimension(30,20));
		lblSignature.setPreferredSize(new Dimension(60,20));
		
		txtNickName.setPreferredSize(new Dimension(120,20));
		txtEmail.setPreferredSize(new Dimension(120,20));
		pfPassword.setPreferredSize(new Dimension(120,20));
		pfRePass.setPreferredSize(new Dimension(120,20));
		boxSex.setPreferredSize(new Dimension(40,20));
		txtAge.setPreferredSize(new Dimension(40,20));
		txtRealName.setPreferredSize(new Dimension(60,20));
		JScrollPane sp = new JScrollPane(areaSignature);
		sp.setPreferredSize(new Dimension(220,60));
		
		lblPhoto.setOpaque(true);
		lblPhoto.setBackground(Color.WHITE);
		lblPhoto.setHorizontalAlignment(SwingConstants.CENTER);
		lblPhoto.setPreferredSize(new Dimension(50,50));
		lblPhoto.setBorder(new LineBorder(Color.DARK_GRAY));
		lblPhoto.setIcon(new Portrait());
		btnChange.setPreferredSize(new Dimension(60,20));
		btnChange.setMargin(new Insets(0,0,0,0));
		
		boxSex.addItem(UserSex.Boy);
		boxSex.addItem(UserSex.Girl);
		
		JPanel paneRequire = new JPanel();
		paneRequire.setBorder(new TitledBorder(new LineBorder(Color.GRAY),"Must fill in"));
		paneRequire.setSize(210,135);
		paneRequire.setLocation(10, 10);
		paneRequire.setLayout(new FlowLayout(FlowLayout.LEFT,5,6));
		paneRequire.add(lblNickName);
		paneRequire.add(txtNickName);
		paneRequire.add(lblEmail);
		paneRequire.add(txtEmail);
		paneRequire.add(lblPassword);
		paneRequire.add(pfPassword);
		paneRequire.add(lblRePass);
		paneRequire.add(pfRePass);
		
		JPanel paneUnRequire = new JPanel();
		paneUnRequire.setBorder(new TitledBorder(new LineBorder(Color.GRAY),"Option"));
		paneUnRequire.setSize(305,125);
		paneUnRequire.setLocation(10, 150);
		paneUnRequire.setLayout(new FlowLayout(FlowLayout.LEFT,5,6));
		paneUnRequire.add(lblSex);
		paneUnRequire.add(boxSex);
		paneUnRequire.add(lblAge);
		paneUnRequire.add(txtAge);
		paneUnRequire.add(lblRealName);
		paneUnRequire.add(txtRealName);
		paneUnRequire.add(lblSignature);
		paneUnRequire.add(sp);
		
		JPanel panePhoto = new JPanel();
		panePhoto.setBorder(new TitledBorder(new LineBorder(Color.GRAY),"Photo"));
		panePhoto.setSize(85,135);
		panePhoto.setLocation(230, 10);
		panePhoto.setLayout(new FlowLayout(FlowLayout.CENTER,5,8));
		panePhoto.add(new FillWidth(50,4));
		panePhoto.add(lblPhoto);
		panePhoto.add(btnChange);
		
		JPanel paneBottom = new JPanel();
		paneBottom.setSize(305,30);
		paneBottom.setLocation(10, 275);
		paneBottom.setLayout(new FlowLayout(FlowLayout.LEFT,2,5));
		paneBottom.add(btnSet);
		paneBottom.add(new FillWidth(100,20));
		paneBottom.add(btnOk);
		paneBottom.add(new FillWidth(8,20));
		paneBottom.add(btnCancle);
		
		TitledBorder tb = new TitledBorder(new LineBorder(Color.GRAY),"Set up");
		JPanel paneSet = new JPanel();
		paneSet.setSize(305,60);
		paneSet.setLocation(10,313);
		paneSet.setBorder(tb);
		paneSet.add(lblServerIP);
		paneSet.add(txtServerIP);
		paneSet.add(new FillWidth(30,20));
		paneSet.add(lblServerPort);
		paneSet.add(txtServerPort);
		
		add(paneRequire);
		add(paneUnRequire);
		add(panePhoto);
		add(paneBottom);
		add(paneSet);
	}
	
	/**
	 * 取消按钮、更改头像按钮、注册按钮、设置按钮事件。
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnCancle){
			dispose();
			new LoginPane();
		}
		if(e.getSource()==btnChange){
			if(chooseProtrait==null)
				new ChooseProtrait();
			else
				chooseProtrait.setVisible(true);
			
		}
		if(e.getSource()==btnOk){
				try {
					String pass = new String(pfPassword.getPassword());
					String repass = new String(pfRePass.getPassword());
					if(pass.equals(repass)){
						btnOk.setEnabled(false);
						new RegNewUser().start();
					}
					else
						JOptionPane.showMessageDialog(null, "Please confirm the password!");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Error:"+e1.getMessage());
				}
			
		}
		if(e.getSource()==btnSet){
			if(isSet){
				isSet = false;
				setSize(getWidth(),getHeight()-65);
				btnSet.setText("Set up↓");
			}else{
				isSet = true;
				setSize(getWidth(),getHeight()+65);
				btnSet.setText("Set up↑");
			}
		}
	}
	
	/**
	 * 注册新用户线程
	 */
	private class RegNewUser extends Thread{
		
		public RegNewUser() throws IOException {
			client = new Socket(txtServerIP.getText(),Integer.parseInt(txtServerPort.getText()));
			oos = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
			User user = new User();
			user.setRealname(txtRealName.getText());
			user.setNickname(txtNickName.getText());
			user.setEmail(txtEmail.getText());
			user.setPassword(new String(pfPassword.getPassword()));
			user.setSex(((UserSex)boxSex.getSelectedItem()).getType());
			user.setAge(Integer.parseInt(txtAge.getText()));
			user.setSignature(areaSignature.getText());
			user.setPhoto(((Portrait)lblPhoto.getIcon()).getNum());
			JQMessage regMessage = new JQMessage();
			regMessage.setType(10);
			regMessage.setObj(user);
			oos.writeObject(regMessage);
			oos.flush();
			ois = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
			
			//ois = new ObjectInputStream(client.getInputStream());
			//oos = new ObjectOutputStream(client.getOutputStream());
		}
		public void run() {
			
			try {
				while(ois!=null){
					Object obj = ois.readObject();
					if(obj instanceof JQMessage){
						JQMessage message = (JQMessage)obj;
						int type = message.getType();
						switch (type) {
						case 11:
							RegUser xuser = (RegUser)message.getObj();
							new RegSuccess(xuser,RegisterPane.this,true);
							//btnOk.setEnabled(true);
							closeClient();
							break;
						case 12:
							JOptionPane.showMessageDialog(null, "注册失败!请重新注册!");
							btnOk.setEnabled(true);
							closeClient();
							break;
						case 90:
							JOptionPane.showMessageDialog(null, message.getObj().toString());
							closeClient();
							System.exit(0);
							break;
						}
						break;
					}
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Error:"+e.getMessage());
				closeClient();
			} catch (ClassNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Error:"+e.getMessage());
				closeClient();
			}	
		}
		
		public void closeClient(){
			try {
				if(oos!=null)oos.close();oos = null;
				if(ois!=null)ois.close();ois = null;
				if(client!=null)client.close();client=null;
			} catch (IOException e) {
				System.out.println("Error:"+e.getMessage());
			}
		}
	}
	
	/**
	 * 注册成功窗口 
	 */
	private class RegSuccess extends JDialog implements ActionListener{
		
		private JTextArea txtInfo = new JTextArea();
		private JButton btnLogin = new JButton("Login");
		private JButton btnReturn = new JButton("Back to Login interface");
		private RegUser user;
		
		public RegSuccess(RegUser user,Frame owner, boolean modal) {
			super(owner,modal);
			this.user = user;
			setSize(250,190);
			setResizable(false);
			Toolkit tk=Toolkit.getDefaultToolkit();
			setLocation((tk.getScreenSize().width-getSize().width)/2,(tk.getScreenSize().height-getSize().height)/2);
			setTitle("Message");
			
			txtInfo.setText("Ok!"+user.getRealname()+",Successed！\n"+
					"Nickname:"+user.getNickname()+"\n"+
					"Account Number:"+user.getJqnum()+"\n"+
					"Password:"+user.getPassword()+"\n"+
					"Carefull with your password!");
			txtInfo.setEditable(false);
			txtInfo.setOpaque(true);
			txtInfo.setBackground(this.getBackground());
			txtInfo.setPreferredSize(new Dimension(200,100));
			txtInfo.setBorder(new TitledBorder(new LineBorder(Color.DARK_GRAY)," Message "));
			setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
			
			btnLogin.addActionListener(this);
			btnReturn.addActionListener(this);
			add(txtInfo);
			add(btnLogin);
			add(btnReturn);
			
			setVisible(true);
		}
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==btnLogin){
				dispose();
				RegisterPane.this.dispose();
				new MainPane(txtServerIP.getText(),Integer.parseInt(txtServerPort.getText()),user.getJqnum(),user.getPassword(),UserState.ONLINESTATE.getState());
			}
			if(e.getSource()==btnReturn){
				dispose();
				RegisterPane.this.dispose();
				new LoginPane();
			}
				
		}
	}
	
	/**
	 * 选择头像窗口
	 */
	private class ChooseProtrait extends JDialog implements ActionListener{
		private JButton[] btnPortrait = new JButton[158];
		private Portrait[] portraits = new Portrait[158];
		
		private JLabel lblBoys = new JLabel("male(30)");
		private JLabel lblGirls = new JLabel("female(29)");
		private JLabel lblAnimals = new JLabel("animal(36)");
		private JLabel lblOthers = new JLabel("other(63)");
		
		private JLabel lblViewInfo = new JLabel("preview:");
		private JLabel lblPhotoView = new JLabel();
		
		private JButton btnP_Ok = new JButton("confirm");
		private JButton btnP_Cancle = new JButton("cancel");
		
		public ChooseProtrait() {
			setTitle("choose photo");
			setSize(500,440);
			setResizable(false);
			Toolkit tk=Toolkit.getDefaultToolkit();
			setLocation((tk.getScreenSize().width-getSize().width)/2,(tk.getScreenSize().height-getSize().height)/2);
			
			btnP_Ok.setSize(80,20);
			btnP_Ok.setLocation(300, 375);
			btnP_Ok.addActionListener(this);
			btnP_Cancle.setSize(80,20);
			btnP_Cancle.setLocation(400, 375);
			btnP_Cancle.addActionListener(this);
			
			//初始化按钮，并将头像显示的按钮上
			for(int i=0;i<btnPortrait.length;i++){
				btnPortrait[i] = new JButton();
				btnPortrait[i].setMargin(new Insets(0,0,0,0));
				btnPortrait[i].setPreferredSize(new Dimension(50,50));
				btnPortrait[i].addActionListener(this);
				btnPortrait[i].setOpaque(true);
				btnPortrait[i].setBackground(Color.WHITE);
				
			}
			//初始化预览头像
			lblPhotoView.setOpaque(true);
			lblPhotoView.setBackground(Color.WHITE);
			lblPhotoView.setHorizontalAlignment(SwingConstants.CENTER);
			lblPhotoView.setPreferredSize(new Dimension(50,50));
			lblPhotoView.setBorder(new LineBorder(Color.DARK_GRAY));
			lblPhotoView.setIcon(lblPhoto.getIcon());
			lblPhotoView.setSize(50,50);
			lblPhotoView.setLocation(420, 40);
			lblViewInfo.setSize(60,20);
			lblViewInfo.setLocation(425, 10);
			
			initJLabel(lblBoys);
			initJLabel(lblGirls);
			initJLabel(lblAnimals);
			initJLabel(lblOthers);
			
			JPanel paneBoys = getPane(0, 30);
			JPanel paneGirls = getPane(30, 60);
			JPanel paneAnimals = getPane(60, 96);
			JPanel paneOthers = getPane(96, 158);
			
			JPanel panePortrait = new JPanel();
			panePortrait.setPreferredSize(new Dimension(380,1500));
			panePortrait.setOpaque(true);
			panePortrait.setBackground(Color.WHITE);
			panePortrait.add(lblBoys);
			panePortrait.add(paneBoys);
			panePortrait.add(lblGirls);
			panePortrait.add(paneGirls);
			panePortrait.add(lblAnimals);
			panePortrait.add(paneAnimals);
			panePortrait.add(lblOthers);
			panePortrait.add(paneOthers);
			
			JScrollPane sp = new JScrollPane(panePortrait);
			sp.setSize(400,350);
			sp.setLocation(10,5);
			setLayout(null);
			
			JPanel paneAll = new JPanel();
			paneAll.setSize(480,365);
			paneAll.setLocation(5, 0);
			paneAll.setOpaque(true);
			paneAll.setBackground(Color.WHITE);
			paneAll.setBorder(new LineBorder(Color.BLACK));
			paneAll.setLayout(null);
			paneAll.add(sp);
			paneAll.add(lblViewInfo);
			paneAll.add(lblPhotoView);
			
			add(paneAll);
			add(btnP_Ok);
			add(btnP_Cancle);
			
			//启动多线程加载头像到按钮上，加快对话框的显示时间
			new Thread(){
				public void run() {
					for(int i=0;i<btnPortrait.length;i++){
						portraits[i] = new Portrait(i+1);
						btnPortrait[i].setIcon(portraits[i]);
					}
				}
			}.start();
			
			
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setVisible(true);
		}
		
		/**
		 * 初始化一些面板，仅仅为简便方法。
		 * @param pane
		 */
		private void initJLabel(JLabel pane){
			pane.setOpaque(true);
			pane.setBackground(new Color(226,247,254));
			pane.setPreferredSize(new Dimension(380,25));
			pane.setBorder(new LineBorder(Color.BLACK));
		}
		
		/**
		 * 根据开始和结束获得面板。
		 * @param begin 开始的头像位置。
		 * @param end 结束的头像位置。
		 * @return 添加好头像的面板。
		 */
		private JPanel getPane(int begin,int end){
			JPanel pane = new JPanel();
			pane.setOpaque(true);
			pane.setBackground(Color.WHITE);
			pane.setLayout(new GridLayout(0,7,5,5));
			for(int i = begin;i<end;i++)
				pane.add(btnPortrait[i]);
			
			return pane;
		}
		
		public void actionPerformed(ActionEvent e) {
			//点击确定时更改选择的图像
			if(e.getSource()==btnP_Ok){
				lblPhoto.setIcon(lblPhotoView.getIcon());
				dispose();
				return;
			}
			//点击确定时关闭选择图像的窗口
			if(e.getSource()==btnP_Cancle){
				dispose();
				return;
			}
			//如果是图像的按钮时，显示图想到浏览头像面板上
			int i = -1;
			for(i=0;i<btnPortrait.length;i++){
				if(e.getSource()==btnPortrait[i])
					break;
			}
			if(i<btnPortrait.length){
				lblPhotoView.setIcon(portraits[i]);
			}
				
		}
	}
}
