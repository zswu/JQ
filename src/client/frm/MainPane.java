/**
  * @(#)client.frm.MainPane.java  2008-8-30  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: 主程序类、JQ的主框架类。
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-8-30 	小猪     		新建
  **/
package client.frm;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import data.FriendUser;
import data.JQMessage;
import data.LoginUser;
import data.Portrait;
import data.Record;
import data.UserState;

 /**
 * 主程序类、JQ的主框架类。<br> 
 */
public class MainPane extends JFrame implements ActionListener{

	private JLabel lblPhoto = new JLabel();
	private JComboBox boxState = new JComboBox();
	private JLabel lblNickName = new JLabel();
	private JTextArea txtSignature = new JTextArea();
	
	private JList listFriend ;//= new JList();
	private DefaultListModel listModel = null;
	
	private JButton btnMenu = new JButton("Menu");
	private JButton btnFind = new JButton("Search");
	private JButton btnSys = new JButton("System Message");
	
	private JLabel lblLoginBar = new JLabel();
	private JButton btnCancleLogin = new JButton("Log off");
	private JPanel fillWidth = new FillWidth(118,120,Color.WHITE);
	
	private Socket client = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	
	private HashMap<Integer, ChatPane> chat = null;
	private FriendUser selfUser = null;
	
	private String serverIp;
	private Integer serverPort;
	private Integer jqnum;
	private String password;
	private Integer state;
	
	private JPopupMenu popupMenu = null;
	private JMenuItem itemChat = null;
	private JMenuItem itemDelete = null;
	private JMenuItem itemFriendInfo = null;
	private JMenuItem itemLog = null;
	
	private FindWindow findWindow = null;
	private JPopupMenu menu = new JPopupMenu();
	private JMenuItem itemQuit = new JMenuItem("Exit");
	
	private Broadcastwindow broadcastwindow = null;
	private Thread thread = null;
	
	/**
	 * 登陆后显示的主面板的构造函数。
	 * @param serverIp 服务器ip。
	 * @param serverPort 服务器端口。
	 * @param jqnum 登陆的jq号码。
	 * @param password 登陆的jq密码。
	 * @param state 登陆的状态。
	 */
	public MainPane(String serverIp,Integer serverPort,Integer jqnum,String password,Integer state) {
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.jqnum = jqnum;
		this.password = password;
		this.state = state;
		//System.out.println(jqnum+":"+password+"State:"+state);
		setTitle("Instant Messaging");
		setSize(200,550);
		setResizable(false);
		Toolkit tk=Toolkit.getDefaultToolkit();
		setLocation((tk.getScreenSize().width-getSize().width)-10,(tk.getScreenSize().height-getSize().height)/2-30);
		
		//init();
		getContentPane().setBackground(Color.WHITE);
		initLoginPane();
			
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		thread = new LoginThread();
		thread.start();
		
	}
	
	/**
	 * 初始化用户按下登陆按钮后，主面板显示登陆进度条窗体。
	 */
	private void initLoginPane(){
		btnCancleLogin.setPreferredSize(new Dimension(60,20));
		btnCancleLogin.setMargin(new Insets(0,0,0,0));
		btnCancleLogin.setFocusPainted(false);
		
		lblLoginBar.setIcon(new ImageIcon(MainPane.class.getResource("/client/images/loginbar.gif")));
		setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
		add(fillWidth);
		add(lblLoginBar);
		//add(new FillWidth(1,2,Color.WHITE));
		add(btnCancleLogin);
		
		btnCancleLogin.addActionListener(this);
		//initMain();
	}
	
	/**
	 * 初始化用户登陆成功后，显示主窗体。
	 * @param v 好友列表。
	 */
	private void initMain(Vector<FriendUser> v){
		newMSG(25);
		selfUser = v.get(0);
		v.remove(0);
		setTitle("JQ2008 "+selfUser.getJqnum());
		popupMenu = new JPopupMenu();
		itemChat = new JMenuItem("Send Message");
		itemDelete = new JMenuItem("Delete");
		itemFriendInfo = new JMenuItem("Check Information");
		itemLog = new JMenuItem("History");
		popupMenu.add(itemChat);
		popupMenu.add(itemFriendInfo);
		popupMenu.addSeparator();
		popupMenu.add(itemLog);
		popupMenu.addSeparator();
		popupMenu.add(itemDelete);
		
		menu.add(new JMenuItem("Set up"));
		menu.addSeparator();
		menu.add(new JMenuItem("Help"));
		menu.addSeparator();
		menu.add(itemQuit);
		itemQuit.addActionListener(this);
		btnMenu.addMouseListener(new ListMouseAdapter());
		
		itemChat.addActionListener(this);
		itemDelete.addActionListener(this);
		itemFriendInfo.addActionListener(this);
		itemLog.addActionListener(this);
		
		btnMenu.setMargin(new Insets(0,5,0,5));
		btnFind.setMargin(new Insets(0,5,0,5));
		btnFind.addActionListener(this);
		btnSys.setMargin(new Insets(0,5,0,5));
		btnSys.addMouseListener(new ListMouseAdapter());
		
		lblPhoto.setSize(50,50);
		lblPhoto.setLocation(5, 5);
		lblPhoto.setOpaque(true);
		lblPhoto.setBackground(new Color(116,220,253,150));
		lblPhoto.setHorizontalAlignment(SwingConstants.CENTER);
		lblPhoto.setIcon(new Portrait(selfUser.getPhoto(),selfUser.getState()));
		lblPhoto.setBorder(new LineBorder(new Color(60,168,206),1,true));
		
		boxState.setSize(50,20);
		boxState.setLocation(60, 5);
		boxState.addItem(UserState.ONLINESTATE);
		boxState.addItem(UserState.HIDDENSTATE);
		boxState.addItem(UserState.DEPARTURESTATE);
		boxState.addItem(UserState.BUSYSTATE);
		boxState.setSelectedIndex(selfUser.getState());
		lblNickName.setSize(80,20);
		lblNickName.setLocation(115,5);
		lblNickName.setText(selfUser.getNickName()+"["+UserState.getStateName(selfUser.getState())+"]");
		txtSignature.setText(selfUser.getSignature());
		txtSignature.setEditable(false);
		txtSignature.setLineWrap(true);
		txtSignature.setBackground(getBackground());
		JScrollPane spSign = new JScrollPane(txtSignature);
		spSign.setSize(125,25);
		spSign.setLocation(60, 30);
		spSign.setBorder(new EmptyBorder(new Insets(0,0,0,0)));
		
		JPanel paneTop = new JPanel();
		paneTop.setLayout(null);
		paneTop.setPreferredSize(new Dimension(200,60));
		paneTop.add(lblPhoto);
		paneTop.add(boxState);
		paneTop.add(lblNickName);
		paneTop.add(spSign);
		
		listModel = new DefaultListModel();
		for(FriendUser user:v){
			listModel.addElement(user);
		}
		//System.out.println(v.size());
		listFriend = new JList();
		listFriend.setCellRenderer(new CompanyLogoListCellRenderer());
		listFriend.setModel(listModel);
		listFriend.setFixedCellHeight(50);
		listFriend.addMouseListener(new ListMouseAdapter());
		listFriend.addMouseMotionListener(new ListMouseAdapter());
		JScrollPane sp = new JScrollPane(listFriend);
		
		chat = new HashMap<Integer, ChatPane>();
		for(int i=0;i<v.size();i++)
			chat.put(v.get(i).getJqnum(), null);
		
		JPanel paneBottom = new JPanel();
		paneBottom.setLayout(new FlowLayout(FlowLayout.LEADING,5,5));
		paneBottom.add(btnMenu);
		paneBottom.add(btnSys);
		paneBottom.add(btnFind);
		
		setVisible(false);
		btnCancleLogin.removeActionListener(this);
		remove(fillWidth);
		remove(lblLoginBar);
		remove(btnCancleLogin);
		validate();
		//repaint();
		
		setLayout(new BorderLayout());
		
		add(paneTop,BorderLayout.NORTH);
		add(sp,BorderLayout.CENTER);
		add(new FillWidth(5,5),BorderLayout.EAST);
		add(new FillWidth(5,5),BorderLayout.WEST);
		add(paneBottom,BorderLayout.SOUTH);
		
		setVisible(true);
		broadcastwindow = new Broadcastwindow();
	}
	
	/**
	 * 取消按钮、聊天按钮、删除好友、好友按钮、查找按钮事件。
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnCancleLogin){
			closeClient();
			System.exit(0);
			return;
		}
		if(e.getSource()==itemChat){
			chatWithFriend();
			return;
		}
		if(e.getSource()==itemDelete){
			JOptionPane.showMessageDialog(null, "功能制作中...");
			return;
		}
		if(e.getSource()==itemFriendInfo){
			JOptionPane.showMessageDialog(null, "功能制作中...");
			return;
		}
		if(e.getSource()==itemLog){
			JOptionPane.showMessageDialog(null, "功能制作中...");
			return;
		}
		if(e.getSource()==btnFind){
			if(findWindow==null)
				findWindow = new FindWindow(this,false);
			else{
				findWindow.setVisible(true);
			}
			return;
		}
		if(e.getSource()==itemQuit){
			quit();
			closeClient();
			System.exit(0);
		}
	}
	
	/**
	 * 根据好友号码获得listFriendUser中的好友。
	 * @param jqnum 好友的jq号码。 
	 * @return 好友的信息FriendUser类。不存在是返回空。
	 */
	private FriendUser getFriendUserFromList(int jqnum){
		for(int i=0;i<listFriend.getModel().getSize();i++){
			Object obj = listFriend.getModel().getElementAt(i);
			if(obj instanceof FriendUser){
				FriendUser friendUser = (FriendUser)obj;
				if(friendUser.getJqnum()==jqnum)
					return friendUser;
			}
		}
		return null;
	}
	
	/**
	 * 退出事件。
	 */
	private void quit(){
		JQMessage message = new JQMessage();
		message.setType(24);
		message.setObj(selfUser);
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		closeClient();
		thread.interrupt();
		System.exit(0);
	}
	
	/*public static void main(String[] args) {
		try {
			//System.out.println(JQClient.class.getResource("../tools/simsun.ttc").getPath());
			Font font = Font.createFont(Font.TRUETYPE_FONT, new File(MainPane.class.getResource("/tools/simsun.ttc").getPath()));
			font = font.deriveFont(Font.PLAIN, 12);
			SetFont.setFont(font);
		} catch (FontFormatException e) {
			System.out.println("错误:"+e.getMessage());
		} catch (IOException e) {
			System.out.println("错误:"+e.getMessage());
		}
		new MainPane("",0,0,"",1);
	}*/

	/**
	 * 窗体关闭时触发事件。
	 */
	private class MyWindowAdapter extends WindowAdapter{
		public void windowClosing(WindowEvent e) {
			//super.windowClosing(e);
			quit();
		}
	}
	
	/**
	 * 自己定制的好友类表的ListCellRenderer
	 */
	private class CompanyLogoListCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus){
        	Component retValue = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        	//System.out.println(value.toString());
        	if(value instanceof FriendUser){
        		FriendUser user = (FriendUser)value;
        		setIcon(new Portrait(user.getPhoto(),user.getState()));
        		setToolTipText("<html>"+user.getNickName()+"["+user.getJqnum()+"]"+"<br><font color='red'>"+user.getSignature()+"</font></html>");
        	}
        	return retValue;
         } 
     }
	
	/**
	 * 鼠标事件，完成鼠标进入好友List中，选中好友，双击与好友进行聊天等。

	 */
	private class ListMouseAdapter extends MouseAdapter{
		public void mouseMoved(MouseEvent e) {
			//System.out.println(e.getSource());
			if(e.getSource()==listFriend){
				listFriend.clearSelection();
				int index = listFriend.locationToIndex(e.getPoint());
				//System.out.println("index:"+index);
				listFriend.setSelectedIndex(index);
			}
		}
		public void mouseClicked(MouseEvent e) {
			if(e.getSource()==listFriend ){
				if(e.getClickCount()==2){
					chatWithFriend();
				}
				if(e.getButton()==MouseEvent.BUTTON3){
					popupMenu.show(listFriend, e.getX(), e.getY());
				}
				return;
			}
			if(e.getSource()==btnMenu){
				menu.show(btnMenu, e.getX()-25, e.getY()-75);
			}
			if(e.getSource()==btnSys){
				broadcastwindow.showNow();
			}
		}
	}
	
	/**
	 * 打开好友聊天窗口。
	 */
	private void chatWithFriend(){
		Object obj = listFriend.getSelectedValue();
		if(obj instanceof FriendUser){
			FriendUser friendUser = (FriendUser)obj;
			int jqnum = friendUser.getJqnum();
			ChatPane chatPane = chat.get(jqnum);
			if(chatPane==null){
				chatPane = new ChatPane(oos,friendUser,selfUser,true);
				chat.put(jqnum, chatPane);
			}
			else if(chatPane.isDisplayable()){
				chatPane.setFocusable(true);
				chatPane.setState(NORMAL);
			}
			else{
				chatPane.setVisible(true);
			}
		}
	}
	
	/**
	 * 登陆线程。处理服务端的信息以及发送消息到服务端。

	 */
	private class LoginThread extends Thread{
		
		public LoginThread(){
			try {
				client = new Socket(serverIp,serverPort);
				MainPane.this.addWindowListener(new MyWindowAdapter());
				oos = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
				LoginUser loginUser = new LoginUser();
				loginUser.setJqnum(jqnum);
				loginUser.setPassword(password);
				loginUser.setState(state);
				JQMessage message = new JQMessage();
				message.setType(20);
				message.setObj(loginUser);
				new WriteThread(message).start();
				ois = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
				//oos = new ObjectOutputStream(client.getOutputStream());
				//ois = new ObjectInputStream(client.getInputStream());
			} catch (UnknownHostException e) {
				closeClient();
				JOptionPane.showMessageDialog(null, "请确保输入的服务器IP和端口正确!"+e.getMessage());
				dispose();
				new LoginPane();
			} catch (IOException e) {
				closeClient();
				JOptionPane.showMessageDialog(null, "请确保输入的服务器IP和端口正确!"+e.getMessage());
				dispose();
				new LoginPane();
			}
		}
		
		public void run() {
			try {
				JQMessage message = null;
				while(ois!=null){
					Object obj = ois.readObject();
					if(obj instanceof JQMessage){
						message = (JQMessage)obj;
						int type =  message.getType();
						switch(type){
						case 21://登陆成功
							if(message.getObj() instanceof Vector)	{							
								Vector<FriendUser> v = (Vector)message.getObj();
								initMain(v);
							}
							break;
						case 22://登录失败
							closeClient();
							JOptionPane.showMessageDialog(null, message.getObj().toString());
							dispose();
							new LoginPane();
							break;
						case 23://账号在别处登录
							causeLetClientQuit(message);
							break;
						case 25://好友上线
							dealFriendUserLogin(message);
							break;
						case 31://接收到消息
							dealRecord(message.getObj(),type);
							break;
						case 41://接受当前在线用户数
							dealOnlineNum(message.getObj());
							break;
						case 43://接受在线的用户显示到table上
							dealShowOnlineUser(message.getObj());
							break;
						case 45:
							JOptionPane.showMessageDialog(null, message.getObj().toString());
							break;
						case 46:
							JOptionPane.showMessageDialog(null, message.getObj().toString());
							break;
						case 90://服务端退出
							causeLetClientQuit(message);
							break;
						case 91:
							dealBroadcast(message);
							break;
						}
					}
				}
			} catch (IOException e) {
				System.out.println("发生异常:"+e.getMessage());
				closeClient();
				JOptionPane.showMessageDialog(null, "和服务端连接发生错误:"+e.getMessage()+",请重新登录!");
				System.exit(0);
			} catch (ClassNotFoundException e) {
				System.out.println("发生异常:"+e.getMessage());
				closeClient();
				JOptionPane.showMessageDialog(null, "和服务端连接发生错误:"+e.getMessage()+",请重新登录!");
				System.exit(0);
			}
		}
	}
	
	/**
	 * 处理广播。
	 * @param message JQMessage消息对象。
	 */
	private void dealBroadcast(JQMessage message){
		newMSG(message.getType());
		if(broadcastwindow!=null){
			broadcastwindow.txt.setText(message.getObj().toString());
			broadcastwindow.showNow();
		}		
	}
	
	/**
	 * 有消息来时出发声音。
	 * @param type 消息的类型。
	 */
	private void newMSG(int type){
		//播放*.au文件时建议使用下面的方法
//		AudioClip audioClip = null;
//		if(n==31)
//		audioClip = Applet.newAudioClip(MainPane.class.getResource("../sound/msg.au"));
//		if(n==91)
//			audioClip = Applet.newAudioClip(MainPane.class.getResource("../sound/system.au"));
//		audioClip.play();
		//播放*.wav文件时建议使用下面的方法
		try {
			AudioStream audioStream = null;
			switch (type) {
			case 25:
				audioStream = new AudioStream(getClass().getResourceAsStream("/client/sound/global.wav"));
				break;
			case 31:
				audioStream = new AudioStream(getClass().getResourceAsStream("/client/sound/msg.wav"));
				break;
			case 91:
				audioStream = new AudioStream(getClass().getResourceAsStream("/client/sound/system.wav"));
				break;
			default:
				audioStream = new AudioStream(getClass().getResourceAsStream("/client/sound/msg.wav"));
				break;
			}
			AudioPlayer.player.start(audioStream);
		} catch (IOException e) {
			System.out.println("在播放声音文件时[type="+type+"]，发生异常:"+e.getMessage());
		}
	}
	
	/**
	 * 处理好友上线时消息。
	 * @param message
	 */
	private void dealFriendUserLogin(JQMessage message){
		Object obj  = message.getObj();
		if(obj instanceof FriendUser){
			newMSG(message.getType());
			FriendUser friendUser = (FriendUser)obj;
			if(listModel.contains(friendUser)){
				for(int i=0;i<listModel.getSize();i++){
					Object object = listModel.get(i);
					if(object instanceof FriendUser){
						FriendUser user = (FriendUser)object;
						if(user.equals(friendUser)){
							user.setState(friendUser.getState());
							user.setPhoto(friendUser.getPhoto());
							user.setNickName(friendUser.getNickName());
							user.setSignature(friendUser.getSignature());
							listFriend.repaint();
							break;
						}
					}
				}
				//更改聊天窗口的图标
				ChatPane chatPane = chat.get(friendUser.getJqnum());
				if(chatPane!=null)
					chatPane.updateFriendPhoto(new Portrait(friendUser.getPhoto(),friendUser.getState(),false));
			}else{
				listModel.addElement(friendUser);
				chat.put(friendUser.getJqnum(), null);
				listFriend.repaint();
			}	
		}
	}
	
	/**
	 * 根据服务端发送的消息，将在线用户显示到查找面板上。
	 * @param obj 在线用户。
	 */
	private void dealShowOnlineUser(Object obj){
		if(obj instanceof FriendUser)
			findWindow.addOnlineUser((FriendUser)obj);
	}
	
	/**
	 * 显示在线用户数到查找面板上。
	 * @param obj 在线用户数。
	 */
	private void dealOnlineNum(Object obj){
		if(obj instanceof Integer){
			int size = (Integer)obj;
			findWindow.lblOnlineNum.setText("当前在线人数 :   "+size);
		}
	}
	
	/**
	 * 处理服务端发送的从好友发过来的消息。
	 * @param obj Record对象。
	 * @param type 消息类型。
	 */
	private void dealRecord(Object obj,int type){
		if(obj instanceof Record){
			Record record = (Record)obj;
			FriendUser friendUser = getFriendUserFromList(record.getFromid());
			if(friendUser!=null){
				newMSG(type);
				ChatPane chatPane = chat.get(record.getFromid());
				if(chatPane==null){
					chatPane = new ChatPane(oos,friendUser,selfUser,true);
					chat.put(friendUser.getJqnum(), chatPane);
				}
				else if(chatPane.isDisplayable()){
					chatPane.setFocusable(true);
					chatPane.setState(NORMAL);
				}
				else{
					chatPane.setVisible(true);
				}
				chatPane.showRecord(friendUser.getNickName(),record,Color.BLUE);
			}
		}
	}
	
	/**
	 * 因为某些原因服务端使得客户端退出。
	 * @param message 退出的JQMessage对象。
	 */
	private void causeLetClientQuit(JQMessage message){
		closeClient();
		JOptionPane.showMessageDialog(null, message.getObj().toString());
		System.exit(0);
	}
	
	/**
	 * 书写消息线程。
	 */
	private class WriteThread extends Thread{
		private JQMessage message = null;
		public WriteThread(JQMessage message) {
			this.message = message;
		}
		public void run() {
			try {
				if(oos!=null){
					oos.writeObject(message);
					oos.flush();
				}
			} catch (IOException e) {
				System.out.println("发生异常:"+e.getMessage());
				closeClient();
				JOptionPane.showMessageDialog(null, "和服务端连接发生错误:"+e.getMessage()+",请重新登录!");
				System.exit(0);
			}
		}
	}
	
	/**
	 * 关闭到服务端的连接。
	 */
	private void closeClient(){
		try {
			if(oos!=null)oos.close();oos = null;
			if(ois!=null)ois.close();ois = null;
			if(client!=null)client.close();client=null;
		} catch (IOException e) {
			System.out.println("错误:"+e.getMessage());
		}
	}
	
	/** 
	 * 查找好友窗口。
	 */
	private class FindWindow extends JDialog implements ActionListener{
		private JTabbedPane paneFindWay = new JTabbedPane();
		private JPanel paneBtn = new JPanel();
		private JButton btnFind = new JButton("Search");
		private JButton btnClose = new JButton("Close");
		private JButton btnPrevious = new JButton("Back");
		private JButton btnAddFriend = new JButton("Add Friend");
		
		private JPanel paneBaseFind = new JPanel();
		private CardLayout card = new CardLayout();
		
		private JPanel paneBaseFirst = new JPanel();
		private JPanel paneBaseSecond = new JPanel();
		
		private JLabel lblInfo = new JLabel("Advanced Search");
		private JRadioButton btnWhoIsOnline = new JRadioButton("Find out who is on line");
		private JRadioButton btnExactFind = new JRadioButton("Advanced Search");
		private JLabel lblExactFind = new JLabel();
		public JLabel lblOnlineNum = new JLabel("Total Number :   Unknown");
		
		private JLabel lblInfo2 = new JLabel("Show the users");
		public JTable tableUser = null;
		private DefaultTableModel model = new MyDefaultTableModel();
		
		private Color bgColor = new Color(252,254,252);
		
		public FindWindow(Frame owner, boolean modal) {
			super(owner, modal);
			
			setTitle("Search/Add friends");
			setSize(400,325);
			setResizable(false);
			Toolkit tk=Toolkit.getDefaultToolkit();
			setLocation((tk.getScreenSize().width-getSize().width)/2,(tk.getScreenSize().height-getSize().height)/2);
			try {
				setIconImage(ImageIO.read(getClass().getResource("/client/images/searchbutton.gif")));
			} catch (IOException e) {
				System.out.println("Error:"+e.getMessage());
			}
			init();
			
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setVisible(true);
			
			dealFindUser(40);
			model.setRowCount(0);
		}
		
		private void init(){
			paneBtn.setLayout(new FlowLayout(FlowLayout.RIGHT,5,5));
			paneBtn.add(btnFind);
			paneBtn.add(btnClose);
			
			lblInfo.setPreferredSize(new Dimension(350,34));
			lblInfo.setHorizontalAlignment(SwingConstants.LEFT);
			//lblInfo.setIcon(new ImageIcon(getClass().getResource("../images/searchinfo.gif")));
			lblInfo.setIcon(new ImageIcon(getClass().getResource("/client/images/searchinfo.gif")));
			ButtonGroup group = new ButtonGroup();
			group.add(btnWhoIsOnline);
			group.add(btnExactFind);
			btnWhoIsOnline.setOpaque(true);
			btnWhoIsOnline.setSelected(true);
			btnWhoIsOnline.setBackground(bgColor);
			btnWhoIsOnline.setPreferredSize(new Dimension(218,20));
			btnExactFind.setOpaque(true);
			btnExactFind.setBackground(bgColor);
			btnExactFind.setPreferredSize(new Dimension(218,20));
			lblExactFind.setIcon(new ImageIcon(MainPane.class.getResource("/client/images/searchexact.gif")));
			lblOnlineNum.setPreferredSize(new Dimension(338,25));
			
			paneBaseFirst.setLayout(new FlowLayout(FlowLayout.CENTER,0,3));
			paneBaseFirst.setOpaque(true);
			paneBaseFirst.setBackground(bgColor);
			paneBaseFirst.add(lblInfo);
			paneBaseFirst.add(btnWhoIsOnline);
			paneBaseFirst.add(btnExactFind);
			paneBaseFirst.add(lblExactFind);
			paneBaseFirst.add(lblOnlineNum);
			//paneBaseFirst.add();
			
			lblInfo2.setIcon(new ImageIcon(getClass().getResource("/client/images/searchinfo.gif")));
			lblInfo2.setPreferredSize(new Dimension(360,34));
			lblInfo2.setOpaque(true);
			lblInfo2.setBackground(bgColor);
			String[] data = {"Account","Nickname","State"};
			
			model.addColumn(data[0]);
			model.addColumn(data[1]);
			model.addColumn(data[2]);
			tableUser = new JTable(model);
			TableRowSorter sorter = new TableRowSorter(model); 
			tableUser.setRowSorter(sorter); 
			tableUser.setRowHeight(22);
			tableUser.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
			tableUser.setPreferredScrollableViewportSize(new Dimension(360,160));
			TableColumn tcJQ = tableUser.getColumn(data[0]);
			tcJQ.setPreferredWidth(80);
			TableColumn tcNick = tableUser.getColumn(data[1]);
			tcNick.setPreferredWidth(90);
			TableColumn tcSign = tableUser.getColumn(data[2]);
			tcSign.setPreferredWidth(180);
			paneBaseSecond.setOpaque(true);
			paneBaseSecond.setBackground(new Color(252,254,252));
			paneBaseSecond.add(lblInfo2);
			paneBaseSecond.add(new JScrollPane(tableUser));
			
			paneBaseFind.setLayout(card);
			paneBaseFind.add("first", paneBaseFirst);
			paneBaseFind.add("second", paneBaseSecond);
			
			paneFindWay.add("Basic",paneBaseFind);
			
			//add(new FillWidth(5,5),BorderLayout.NORTH);
			add(paneFindWay,BorderLayout.CENTER);
			add(new FillWidth(5,5),BorderLayout.EAST);
			add(new FillWidth(5,5),BorderLayout.WEST);
			add(paneBtn,BorderLayout.SOUTH);
			
			btnFind.addActionListener(this);
			btnClose.addActionListener(this);
			
			DefaultTableCellRenderer LblRenderer = new DefaultTableCellRenderer() { 
		 	    public void setValue(Object value) { 
		 	        if (value instanceof JLabel) {
		 	        	JLabel lbl = (JLabel)value;
		 	        	setIcon(lbl.getIcon());
		 	        	setText(lbl.getText());
		 	        } else { 
		 	        	super.setValue(value); 
		 	        } 
		 	    } 
			};
			LblRenderer.setHorizontalAlignment(SwingConstants.LEFT);
			tcJQ.setCellRenderer(LblRenderer);
			DefaultTableCellRenderer dtc1 = (DefaultTableCellRenderer)tableUser.getCellRenderer(0, 1);
			dtc1.setHorizontalAlignment(SwingConstants.CENTER);
			DefaultTableCellRenderer dtc2 = (DefaultTableCellRenderer)tableUser.getCellRenderer(0, 1);
			dtc2.setHorizontalAlignment(SwingConstants.CENTER);
			
		}
		
		private void dealFindUser(int type){
			JQMessage message = new JQMessage();
			message.setType(type);
			message.setObj(null);
			new WriteThread(message).start();
		}
		
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==btnFind){
				btnFind.removeActionListener(this);
				paneBtn.remove(btnFind);
				btnPrevious.addActionListener(this);
				btnAddFriend.addActionListener(this);
				paneBtn.add(btnPrevious,0);
				paneBtn.add(btnAddFriend,1);
				paneBtn.validate();
				paneBtn.repaint();
				card.next(paneBaseFind);
				if(btnWhoIsOnline.isSelected()){
					dealFindUser(42);
					model.setRowCount(0);
				}
			}
			if(e.getSource()==btnClose){
				dispose();
				return;
			}
			if(e.getSource()==btnPrevious){
				btnPrevious.removeActionListener(this);
				btnAddFriend.removeActionListener(this);
				paneBtn.remove(btnPrevious);
				paneBtn.remove(btnAddFriend);
				btnFind.addActionListener(this);
				paneBtn.add(btnFind,0);
				paneBtn.validate();
				paneBtn.repaint();
				card.previous(paneBaseFind);
				dealFindUser(40);
			}
			if(e.getSource()==btnAddFriend){
				int row = tableUser.getSelectedRow();
				if(row==-1){
					JOptionPane.showMessageDialog(null, "Please select one user!");
					return;
				}
				Object value = tableUser.getValueAt(row, 0);
				if(value instanceof JLabel){
					Integer jqnum = Integer.parseInt(((JLabel)value).getText());
					JQMessage message = new JQMessage();
					message.setType(44);
					message.setObj(jqnum);
					new WriteThread(message).start();
				}
				System.out.println(value);
			}
		}
		
		private void addOnlineUser(FriendUser user){
			if(isVisible()){
				JLabel lblJQNum = new JLabel(user.getJqnum()+"");
				lblJQNum.setIcon(new Portrait(user.getPhoto(),user.getState(),false));
				Object[] data = {lblJQNum,user.getNickName(),user.getSignature()};
				model.addRow(data);
			}
		}
	}
	
	private class MyDefaultTableModel extends DefaultTableModel{
		public boolean isCellEditable(int row, int column) {
			super.isCellEditable(row, column);
			return false;
		}
		/*public Class getColumnClass(int c) {
			if(c==0)
				return JLabel.class;
			else
				return super.getColumnClass(c); 
		}*/
	} 
	
	/**
	 * 系统消息窗口。
	 */
	private class Broadcastwindow extends JDialog implements WindowFocusListener{
		private JTextArea txt = new JTextArea("No message!");
		public Broadcastwindow() {
			super();
			setTitle("System Message");
			setSize(200,160);
			setResizable(false);
			setLocation(MainPane.this.getLocationOnScreen().x,MainPane.this.getLocationOnScreen().y+357);
			
			JScrollPane sp = new JScrollPane(txt);
			txt.setEditable(false);
			txt.setLineWrap(true);
			sp.setSize(190, 125);
			sp.setLocation(5, 2);
			setLayout(null);
			add(sp);
			
			addWindowFocusListener(this);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			//setVisible(true);
		}
		public void windowGainedFocus(WindowEvent e) {}
		public void windowLostFocus(WindowEvent e) {
			dispose();
		}
		public void showNow(){
			setLocation(MainPane.this.getLocationOnScreen().x,MainPane.this.getLocationOnScreen().y+357);
			setVisible(true);
		}
	}
}
