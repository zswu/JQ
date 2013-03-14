/**
  * @(#)client.frm.ChatPane.java  2008-9-1  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: 聊天窗体类。
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-9-1 	小猪     		新建
  **/
package client.frm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JWindow;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import data.Face;
import data.FriendUser;
import data.JQMessage;
import data.Portrait;
import data.Record;

import tools.DateDeal;


public class ChatPane extends JFrame implements ActionListener{

	private JLabel lblTop = new JLabel();
	private JLabel lblFriendInfo = new JLabel();
	private JLabel lblPhoto = new JLabel();
	private JTextPane txtMessage = new JTextPane();
	private JTextPane txtWrite = new JTextPane();
	
	private JPanel paneFriendInfo = new JPanel();
	private JPanel paneTools = new JPanel();
	private JPanel paneBtn = new JPanel();
	
	private JButton btnSend = new JButton("Send(S)");
	private JButton btnClose = new JButton("Close(C)");
	private JButton btnSet = new JButton("↓");
	
	private JPopupMenu popupMenu = new JPopupMenu();
	private JCheckBoxMenuItem itemEnter = new JCheckBoxMenuItem("Press Enter to Send");
	private JCheckBoxMenuItem itemCEnter = new JCheckBoxMenuItem("Press Ctrl+Enter to Send");
	
	private Color bgColor = new Color(169,213,244);
	
	//private Socket client = null;
	private ObjectOutputStream oos = null;
	private FriendUser friendUser = null;
	private FriendUser selfUser = null;
	
	private JButton btnFaces = new JButton();
	private FaceWindow faceWindow = null;

	/**
	 * 窗体的构造函数。
	 * @param oos 输出对象流。
	 * @param friendUser FriendUser好友对象。
	 * @param selfUser FriendUser自己对象。
	 * @param show 是否显示。
	 */
	public ChatPane(ObjectOutputStream oos,FriendUser friendUser,FriendUser selfUser,boolean show) {
		this.oos = oos;
		this.friendUser = friendUser;
		this.selfUser = selfUser;
		setTitle("Chatting with "+friendUser.getNickName());
		setSize(494,500);
		try {
			setIconImage(ImageIO.read(ChatPane.class.getResource("/client/images/chat/icon.gif")));
		} catch (IOException e) {
			System.out.println("Error"+e.getMessage());
		}
		//setResizable(false);
		Toolkit tk=Toolkit.getDefaultToolkit();
		setLocation((tk.getScreenSize().width-getSize().width)/2,(tk.getScreenSize().height-getSize().height)/2);
		
		init();
		btnClose.addActionListener(this);
		btnSend.addActionListener(this);
		btnSet.addActionListener(this);
		itemEnter.addActionListener(this);
		itemCEnter.addActionListener(this);
		btnFaces.addActionListener(this);
		
		txtMessage.setContentType("text/html");
		txtWrite.addKeyListener(new SendKeyListenter());
//		txtWrite.setContentType("text/html");
//		txtWrite.setText("<html><body style=\"font-size: 12px;font-family:'宋体';color:'red';font-style: normal;\" >测试数据</body></html>");
/*		StyleContext sc = StyleContext.getDefaultStyleContext();
		NamedStyle style = sc.new NamedStyle();
		//Style style = new StyleContext.NamedStyle();
		style.addAttribute("font-family", "宋体");
		style.addAttribute("font-size", "12px");
		style.addAttribute("font-style", "normal");
		style.addAttribute("color", "yellow");
		txtWrite.setLogicalStyle(style);
*/		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(show);
	}
	
	/**
	 * 初始化面板。
	 */
	private void init(){
		lblTop.setPreferredSize(new Dimension(494,61));
		lblTop.setVerticalAlignment(SwingConstants.TOP);
		lblTop.setIcon(new ImageIcon(ChatPane.class.getResource("/client/images/chat/top.gif")));
		lblTop.setOpaque(true);
		lblTop.setBackground(bgColor);
		lblFriendInfo.setVerticalAlignment(SwingConstants.TOP);
		lblFriendInfo.setPreferredSize(new Dimension(151,407));
		lblFriendInfo.setIcon(new ImageIcon(ChatPane.class.getResource("/client/images/chat/friend_info.gif")));
		lblFriendInfo.setOpaque(true);
		lblFriendInfo.setBackground(bgColor);
		
		btnClose.setFocusPainted(false);
		btnClose.setPreferredSize(new Dimension(60,20));
		btnClose.setMargin(new Insets(0,5,0,5));
		btnSend.setFocusPainted(false);
		btnSend.setPreferredSize(new Dimension(60,20));
		btnSend.setMargin(new Insets(0,5,0,5));
		btnSet.setFocusPainted(false);
		btnSet.setPreferredSize(new Dimension(9,20));
		btnSet.setMargin(new Insets(0,-4,0,-5));
		
		btnFaces.setIcon(new Face(200));
		btnFaces.setMargin(new Insets(0,0,0,0));
		btnFaces.setBorder(new EmptyBorder(0,0,0,0));
		
		
		lblPhoto.setPreferredSize(new Dimension(20,20));
		lblPhoto.setIcon(new Portrait(friendUser.getPhoto(),friendUser.getState(),false));
		JLabel lblInfo = new JLabel(friendUser.getNickName()+"("+friendUser.getJqnum()+")  "+friendUser.getSignature());
		lblInfo.setPreferredSize(new Dimension(290,20));
		lblInfo.setForeground(Color.BLUE);
		//lblInfo.setBorder(new LineBorder(Color.BLACK));
		paneFriendInfo.setOpaque(true);
		paneFriendInfo.setBackground(new Color(205,237,255));
		paneFriendInfo.setPreferredSize(new Dimension(337,25));
		paneFriendInfo.setLayout(new FlowLayout(FlowLayout.LEFT,5,2));
		paneFriendInfo.add(lblPhoto);
		paneFriendInfo.add(lblInfo);
		
		paneTools.setOpaque(true);
		paneTools.setBackground(new Color(205,237,255));
		paneTools.setPreferredSize(new Dimension(337,25));
		paneTools.setLayout(new FlowLayout(FlowLayout.LEFT,2,5));
		paneTools.add(btnFaces);
		
		paneBtn.setPreferredSize(new Dimension(337,30));
		paneBtn.setOpaque(true);
		paneBtn.setBackground(bgColor);
		paneBtn.setLayout(new FlowLayout(FlowLayout.RIGHT,0,5));
		paneBtn.add(btnClose);
		paneBtn.add(new FillWidth(5,20,bgColor));
		paneBtn.add(btnSend);
		paneBtn.add(new FillWidth(5,20,bgColor));
		paneBtn.add(btnSet);
		
		JScrollPane spMessage = new JScrollPane(txtMessage);
		spMessage.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spMessage.setBorder(new EmptyBorder(0,0,0,0));
		
		JScrollPane spWrite = new JScrollPane(txtWrite);
		spWrite.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spWrite.setBorder(new EmptyBorder(0,0,0,0));
		
		JPanel paneMessage = new JPanel();
		paneMessage.setLayout(new BorderLayout());
		paneMessage.add(paneFriendInfo,BorderLayout.NORTH);
		paneMessage.add(spMessage,BorderLayout.CENTER);
		
		
		
		JPanel paneWrite = new JPanel();
		paneWrite.setPreferredSize(new Dimension(337,96));
		paneWrite.setLayout(new BorderLayout());
		paneWrite.add(paneTools,BorderLayout.NORTH);
		paneWrite.add(spWrite,BorderLayout.CENTER);
		paneWrite.add(paneBtn,BorderLayout.SOUTH);
		
		JPanel paneCenter = new JPanel();
		paneCenter.setLayout(new BorderLayout());
		paneCenter.setBorder(new LineBorder(new Color(118,171,211)));
		paneCenter.setOpaque(true);
		paneCenter.setBackground(bgColor);
		paneCenter.add(paneMessage,BorderLayout.CENTER);
		paneCenter.add(paneWrite,BorderLayout.SOUTH);
		
		JPanel paneAll = new JPanel();
		paneAll.setLayout(new BorderLayout());
		paneAll.setOpaque(true);
		paneAll.setBackground(bgColor);
		paneAll.add(paneCenter,BorderLayout.CENTER);
		paneAll.add(paneBtn,BorderLayout.SOUTH);
		
		setLayout(new BorderLayout());
		add(lblTop,BorderLayout.NORTH);
		add(new FillWidth(6,407,bgColor),BorderLayout.WEST);
		add(lblFriendInfo,BorderLayout.EAST);
		add(paneAll,BorderLayout.CENTER);
		add(new FillWidth(494,5,bgColor),BorderLayout.SOUTH);
		
		ButtonGroup btnGroup = new ButtonGroup();
		btnGroup.add(itemCEnter);
		btnGroup.add(itemEnter);
		popupMenu.add(itemEnter);
		popupMenu.add(itemCEnter);
		itemCEnter.setSelected(true);
	}
	
	/**
	 * 关闭、发送、设置、按Enter、按Ctrl+Enter、表情按钮等事件。
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnClose){
			this.dispose();
			return;
		}
		if(e.getSource()==btnSend){
			String content = txtWrite.getText();
			//System.out.println(content);
			//txtMessage.setEditorKit(txtWrite.getEditorKit());
			//txtMessage.setDocument(txtWrite.getDocument());
			//txtMessage.setText(content);
			
			JQMessage message = new JQMessage();
			Record record = new Record();
			record.setFromid(selfUser.getJqnum());
			record.setToid(friendUser.getJqnum());
			record.setId(1);
			record.setContent(txtWrite.getText());
			showRecord(selfUser.getNickName(),record,Color.GREEN);
			message.setType(30);
			message.setObj(record);
			new WriteThread(message).start();
			txtWrite.setText("");
			return;
		}
		if(e.getSource()==btnSet){
			//Point point = getLocationOnScreen();
			//popupMenu.setLocation(point.x+getWidth()-150, point.y+getHeight()-20);
			//popupMenu.setVisible(true);
			popupMenu.show(this, getWidth()-150, getHeight()-20);
			return;
		}
		if(e.getSource()==itemEnter){
			removeSendKey();
			return;
		}
		if(e.getSource()==itemCEnter){
			removeSendKey();
			return;
		}
		if(e.getSource()==btnFaces){
			if(faceWindow==null)
				faceWindow = new FaceWindow(this);
			else{
				//faceWindow.pack();
				Point point = ChatPane.this.getLocationOnScreen();
				faceWindow.setLocation(point.x+20, point.y+120);
				faceWindow.setVisible(true);
			}
		}
	}
	
	/**
	 * 更新好友的图标。
	 * @param icon
	 */
	public void updateFriendPhoto(Icon icon){
		lblPhoto.setIcon(icon);
	}
	
	/**
	 * 显示用户昵称和聊天记录信息到聊天面板上。
	 * @param nickName 昵称
	 * @param record 聊天记录
	 * @param color 显示文字的颜色。
	 */
	public void showRecord(String nickName,Record record,Color color){
		//JOptionPane.showMessageDialog(null, record.getContent());
		insertString(nickName+" "+DateDeal.getDate2(record.getSendTime()), color);
		insertString(record.getContent(), null);
	}
	
	/**
	 * 将信息插入到聊天面板上。
	 * @param msg 消息的内容。
	 * @param color 显示文字的颜色。
	 */
	private void insertString(String msg,Color color){
		StyledDocument document = txtMessage.getStyledDocument();
		SimpleAttributeSet set = new SimpleAttributeSet();
		if(color!=null)
			StyleConstants.setForeground(set, color);
		try {
			document.insertString(document.getLength(), msg+"\n", set);
		} catch (BadLocationException e) {
			System.out.println("Error");
		}
	}
	
	/**
	 * 添加发送事件。
	 */
	private void removeSendKey(){
		txtWrite.removeKeyListener(txtWrite.getKeyListeners()[0]);
		txtWrite.addKeyListener(new SendKeyListenter());
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
		new ChatPane(null,null,null,true);
	}*/

	/**
	 * 
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
				JOptionPane.showMessageDialog(null, "和服务端连接发生错误:"+e.getMessage()+",请重新登录!");
				System.exit(0);
			}
		}
	}
	
	
	private class SendKeyListenter extends KeyAdapter{
		public void keyPressed(KeyEvent e){ 
			//System.out.println(e.getModifiersEx()+"=="+e.paramString());
			if(e.getModifiers()==(KeyEvent.CTRL_DOWN_MASK+KeyEvent.VK_ENTER))
				System.out.println("ok");
			if((itemEnter.isSelected() && e.getKeyCode()==KeyEvent.VK_ENTER) || (itemCEnter.isSelected() && e.getModifiersEx()==(KeyEvent.CTRL_DOWN_MASK + KeyEvent.VK_ENTER)))
				btnSend.doClick();
			return;
		}
	}
	
	/**
	 * 表情显示JWindow类。
	 */
	private class FaceWindow extends JWindow implements ActionListener,Runnable,WindowFocusListener,MouseListener{
		private static final int faceNum  = 134;
		JPanel paneFace = new JPanel();
		JLabel[] btnFace = new JLabel[faceNum];
		
		/**
		 * 构造函数。
		 * @param owner 显示的窗体。
		 */
		public FaceWindow(Frame owner) {
			super(owner);
			setSize(395,245);
			Point point = ChatPane.this.getLocationOnScreen();
			setLocation(point.x+20, point.y+120);
			
			paneFace.setLayout(new GridLayout(9,15));
			for(int i=0;i<faceNum;i++){
				btnFace[i] = new JLabel();
				paneFace.add(btnFace[i]);
			}
			new Thread(this).start();
			
			JTabbedPane tabPane = new JTabbedPane();
			tabPane.add("默认", paneFace);
			add(tabPane);
			
			addWindowFocusListener(this);
			setVisible(true);
		}
		/**
		 * 将表情显示到JLabel上。
		 */
		public void run() {
			for(int i=0;i<faceNum;i++){
				//btnFace[i] = new JLabel();
				btnFace[i].setIcon(new Face(i));
				btnFace[i].addMouseListener(this);
				btnFace[i].setBorder(new EmptyBorder(1,1,1,1));
				repaint();
			}
			//repaint();
		}
		
		/**
		 * ActionListener事件。
		 */
		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();
			if(obj instanceof JButton){
				JButton btnFace = (JButton)obj;
				txtWrite.insertIcon(btnFace.getIcon());
				this.dispose();
			}	
		}
		/**
		 * 该窗体失去焦点时事件。
		 */
		public void windowLostFocus(WindowEvent e) {
			dispose();
		}
		public void windowGainedFocus(WindowEvent e) {	}
		/**
		 * 鼠标点击事件。
		 */
		public void mouseClicked(MouseEvent e) {
			Object obj = e.getSource();
			if(obj instanceof JLabel){
				JLabel lbl = (JLabel)e.getSource();
				txtWrite.insertIcon(lbl.getIcon());
				this.dispose();
			}
		}
		/**
		 * 鼠标进入事件。
		 */
		public void mouseEntered(MouseEvent e) {
			Object obj = e.getSource();
			if(obj instanceof JLabel){
				JLabel lbl = (JLabel)e.getSource();
				lbl.setBorder(new LineBorder(Color.DARK_GRAY));
			}			
		}
		/**
		 * 鼠标移出时事件。
		 */
		public void mouseExited(MouseEvent e) {
			Object obj = e.getSource();
			if(obj instanceof JLabel){
				JLabel lbl = (JLabel)e.getSource();
				lbl.setBorder(new EmptyBorder(1,1,1,1));
			}			
		}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
}
