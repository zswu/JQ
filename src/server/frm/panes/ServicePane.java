/**
  * @(#)server.frm.panes.ServicePane.java  2008-8-28  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: 系统服务面板类。
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-8-28 	小猪     		新建
  **/
package server.frm.panes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import dao.LogDAOByFile;
import dao.RecordDAOByFile;
import dao.UserDAOByFile;
import data.FriendUser;
import data.JQMessage;
import data.Log;
import data.LoginUser;
import data.Record;
import data.RegUser;
import data.User;
import data.UserState;

import server.JQServer;
import server.frm.Server;
import tools.DateDeal;
import tools.GetParameter;
import tools.JQCreater;

 /**
 * 系统服务面板类。<br>
 * 完成功能：系统的启动与停止<br>
 * 用户的连接日志显示。
 */
public class ServicePane extends JPanel implements ActionListener,Runnable{

	/** 启动JQ服务按钮 */
	private JButton btnStart = new JButton("启动Java QQ服务");
	/** 停止JQ服务按钮 */
	private JButton btnStop = new JButton("停止Java QQ服务");
	/**  */
	private ServecieProcessBar bar = new ServecieProcessBar(300,30);
	/** 显示连接日志 */
	private JTextArea areaLog = new JTextArea();
	
	private ServerSocket server = null;
	
	public static Hashtable<Integer,ClientLink> table = null;
	
	private Thread thread;
	private static boolean isServiceRun = false;
	
	private String path = "log.txt";
	private PrintWriter raf = null;
	
	
	public ServicePane() {
		try {
			raf = new PrintWriter(new BufferedOutputStream(new FileOutputStream(new File(path),true)));
		} catch (FileNotFoundException e) {
			areaLog.append("发生异常错误，请确保"+path+"文件可写!原因如下:"+e.getMessage());
			btnStart.setEnabled(false);
		}
		thread = new Thread(this);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		
		areaLog.setEditable(false);
		areaLog.setLineWrap(true);
		bar.setPreferredSize(new Dimension(580,27));
		btnStart.addActionListener(this);
		btnStop.addActionListener(this);
		btnStop.setEnabled(false);
		
		JPanel pane = new JPanel();
		pane.setPreferredSize(new Dimension(600,70));
		pane.setLayout(new FlowLayout(FlowLayout.CENTER));
		pane.add(btnStart);
		pane.add(btnStop);
		pane.add(bar);
		
		setLayout(new BorderLayout());
		add(pane,BorderLayout.NORTH);
		add(new JScrollPane(areaLog),BorderLayout.CENTER);
		add(new FillWidth(4,4),BorderLayout.WEST);
		add(new FillWidth(4,4),BorderLayout.EAST);
		
	}
	
	/**
	 * 初始化配置文件。
	 */
	public void initProp(){
 		try {
			Server.prop = GetParameter.getProp();
		} catch (Exception e) {
			writeSysLog(DateDeal.getCurrentTime()+",加载配置文件时发生错误！原因如下:"+e.getMessage());
		}
		int dataWay = Integer.parseInt(Server.prop.getProperty(GetParameter.keys[6]));
		if(dataWay==0)
			Server.isFileWay = true;
		else{
			Server.isFileWay = false;
			writeSysLog("注意:系统暂不支持数据库方式!请使用文件方式保存数据!");
		}
		int saveLog = Integer.parseInt(Server.prop.getProperty(GetParameter.keys[4]));
		if(saveLog==1)
			Server.isSaveLog = true;
		else
			Server.isSaveLog = false;
	}
	
	/**
	 * 启动按钮、停止按钮的事件。
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnStart){
			initProp();
			if(!Server.isFileWay){
				JOptionPane.showMessageDialog(null, "暂不支持数据库存储方式!请选择文件方式!");
				return;
			}
			try {
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
				bar.startRoll();
				startServer();
			} catch (IOException e1) {
				writeSysLog(DateDeal.getCurrentTime()+",JQ服务器启动服务时发生错误，原因如下:"+e1.getMessage());
			}
		}
		if(e.getSource()==btnStop){
			btnStart.setEnabled(true);
			btnStop.setEnabled(false);
			bar.stopRoll();
			try {
				stopServer();
			} catch (IOException e1) {
				writeSysLog(DateDeal.getCurrentTime()+",JQ服务器停止服务时发生错误，原因如下:"+e1.getMessage());
			}
		}
	}
	
	/**
	 * 书写系统日志。
	 * @param log 日志。
	 */
	public void writeSysLog(String log){
		areaLog.append(log+"\n");
		//滚动条自动下滚
		areaLog.setCaretPosition(areaLog.getDocument().getLength());
		raf.write(log+"\n");
		raf.flush();
	}
	
	/**
	 * 书写日志。
	 * @param log 日志。
	 */
	private void writeLog(Log log){
		try {
			LogDAOByFile LogDAO = new LogDAOByFile();
			LogDAO.add(log);
			Server.logPane.getAreaLog().append(log.toString()+"");
			Server.logPane.getAreaLog().setCaretPosition(Server.logPane.getAreaLog().getDocument().getLength());
		} catch (IOException e) {
			writeSysLog(DateDeal.getCurrentTime()+",写入操作日志["+log.toString()+"]时发生错误:"+e.getMessage());
		}
	}
	
	/**
	 * 启动服务器。
	 * @throws IOException IO异常。
	 */
	public void startServer() throws IOException{
		isServiceRun = true;
		int port = Integer.parseInt(Server.prop.getProperty(GetParameter.keys[0]));
		table = new Hashtable<Integer,ClientLink>();
		server = new ServerSocket(port);
		new Thread(this).start();
		writeSysLog(DateDeal.getCurrentTime()+",JQ服务器服务启动成功!等待JQ用户上线...");
	}
	
	/**
	 * 停止服务器。
	 * @throws IOException IO异常。
	 */
	public void stopServer() throws IOException{
		isServiceRun = false;
		Enumeration<ClientLink> en = table.elements();
		while(en.hasMoreElements()){
			ClientLink client = en.nextElement();
			client.updateUserState(client.jqnum, UserState.OFFLIENSTATE.getState());
			client.letClientQuit();
		}
		table.clear();table=null;
		if(server!=null)
			server.close();
		server = null;
		writeSysLog(DateDeal.getCurrentTime()+",JQ服务器服务停止成功!");
	}
	
	@Override
	public void run() {
		while(isServiceRun){
			try {
				Socket client = server.accept();
				//System.out.println("come accept");
				new Thread(new ClientLink(client)).start();
			} catch (IOException e) {
				writeSysLog(DateDeal.getCurrentTime()+",JQ服务器接受客户端时发生异常:"+e.getMessage());
			}
		}
	}
	
	/**
	 * 客户端连接类。处理和客户端的各种消息。
	 */
	private class ClientLink implements Runnable,Serializable{
		
		public Socket client = null;
		public ObjectInputStream ois = null;
		public ObjectOutputStream oos = null;
		public int jqnum = -1;
		
		public ClientLink(Socket client) {
			
			this.client = client;
			writeSysLog(DateDeal.getCurrentTime()+",客户端"+getClientIP()+"]连接到服务端");
			try {
				ois = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
				oos = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
				//ois = new ObjectInputStream(client.getInputStream());
				//oos = new ObjectOutputStream(client.getOutputStream());
				//System.out.println("come client");
			} catch (IOException e) {
				writeSysLog(DateDeal.getCurrentTime()+",获取到客户端"+getClientIP()+"的连接发生错误:"+e.getMessage());
			}
		}
		
		/*以1开头的：注册相关消息
		10:客户端发送注册信息到服务端
		11:服务端回复注册成功到客户端
		12:服务端回复注册失败到客户端
		以2开头的：登陆相关消息
		20:客户端发送登陆信息到服务端（判断此用户是否已登录）
		21:登陆成功服务端发送好友信息到客户端
		22:登录失败服务端发送错误信息到客户端
		23:服务端发送账号在别处登陆
		24:客户端发送退出到服务端
		25:服务端发送好友上线功能
		以3开头的：发送记录相关消息
		30:客户端发送消息到服务端
		31:服务端根据消息发送到客户端
		以4开头的：搜索在线用户加好友相关消息
		40:客户端发送获取在线用户数
		41:服务端发送在线用户数
		42:客户端发送获取当前在线用户
		43:服务端发送在线列表用户
		44:客户端发送添加好友的qq号
		45:服务端发送添加好友成功
		46:服务端发送添加好友失败
		以9开头的：系统相关消息
		90:服务端发送下线功能到客户端
		91:服务端发送广播消息到客户端
		*/
		@Override
		public void run() {
			if(Server.isFileWay){
				try {
					while(isServiceRun && ois!=null && oos!=null){
						Object obj = ois.readObject();
						//System.out.println("come");
						if(obj instanceof JQMessage){
							JQMessage message = (JQMessage)obj;
							int type = message.getType();
							//System.out.println(type);
							if(type==10){
								dealRegiter(message);
								break;
							}else{
								switch(type){
								case 20:
									DealLogin(message);
									break;
								case 24:
									dealQuit(message);
									break;
								case 30:
									dealMessage(message);
									break;
								case 40:
									dealFindOnlineUserNum();
									break;
								case 42:
									dealFindOnlineUser();
									break;
								case 44:
									dealAddUser(message);
									break;
								}
							}
						}else
							writeSysLog("客户端"+getClientIP()+"发送错误的数据信息到服务端");
					}
				} catch (IOException e) {
					//逻辑的慎密~~这里容易忽略
					removeClientForException(this);
					closeClient();
				} catch (ClassNotFoundException e) {
					removeClientForException(this);
					closeClient();
				}
			}else{
				writeSysLog("注意:系统暂不支持数据库方式!请使用文件方式保存数据!");
				letClientQuit();
			}
		}
		
		/**
		 * 获取客户端IP
		 * @return 返回字符串的IP。
		 */
		private String getClientIP(){
			return client==null?"[客户端已关闭,不能获取信息]":"["+client.getInetAddress().toString()+":"+client.getPort()+"]";
		}
		
		/**
		 * 使得客户端下线。
		 */
		private void letClientQuit(){
			JQMessage message = new JQMessage();
			message.setType(90);
			message.setObj("和服务端断开连接");
			writeToClient(message);
			closeClient();
		}
		
		/**
		 * 关闭和客户端的连接
		 */
		private void closeClient(){
			String ip = getClientIP();
			writeSysLog(DateDeal.getCurrentTime()+",客户端"+getClientIP()+"下线.");
			try {
				if(oos!=null)oos.close();oos = null;
				if(ois!=null)ois.close();ois = null;
				if(client!=null)client.close();client=null;
			} catch (IOException e) {
				writeSysLog(DateDeal.getCurrentTime()+",关闭到客户端"+ip+"的连接时时发生错误:"+e.getMessage());
			}
		}
		
		/**
		 * 处理客户端退出消息。
		 * @param message JQMessage对象。
		 */
		private void dealQuit(JQMessage message){
			Object obj = message.getObj();
			if(obj instanceof FriendUser){
				FriendUser user = (FriendUser)obj;
				writeLog(getLog(user, "用户退出"));
			}
			updateUserState(jqnum, UserState.OFFLIENSTATE.getState());
			UserDAOByFile userDAO = new UserDAOByFile();
			try {
				telFriendState(userDAO.findById(jqnum));
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			} catch (ClassNotFoundException e) {}
			table.remove(jqnum);
			closeClient();
		}
		
		/**
		 * 处理注册新用户。
		 * @param message JQMessage对象。
		 */
		private void dealAddUser(JQMessage message){
			if(message.getObj() instanceof Integer){
				int jqnum = (Integer)message.getObj();
				JQMessage message2 = new JQMessage();
				boolean b = eachAddFriend(jqnum);
				if(b){
					message2.setType(45);
					message2.setObj("恭喜好友添加成功!");
				}else{
					message2.setType(46);
					message2.setObj("抱歉好友添加失败!");
				}
				writeToClient(message2);
				UserDAOByFile userDAO = new UserDAOByFile();
				User user = userDAO.findById(jqnum);
				User self = userDAO.findById(this.jqnum);
				if(user!=null)
					telfriendOnline(this.jqnum, user);
				if(self!=null)
					telfriendOnline(jqnum, self);
			}
		}
		
		/**
		 * 告诉好友我的状态。
		 * @param jqnum jq号码。
		 * @param user User对象。
		 */
		private void telfriendOnline(int jqnum,User user){
			if(isHasLoged(jqnum)){
				JQMessage message = new JQMessage();
				FriendUser friendUser = new FriendUser();
				friendUser.setJqnum(user.getJqnum());
				friendUser.setNickName(user.getNickname());
				friendUser.setPhoto(user.getPhoto());
				friendUser.setSignature(user.getSignature());
				friendUser.setState(user.getState());
				message.setType(25);
				message.setObj(friendUser);
				table.get(jqnum).writeToClient(message);
			}
		}
		
		/**
		 * 相互添加好友。
		 * @param jqnum jq号码。
		 * @return 然会添加成功否。
		 */
		private boolean eachAddFriend(int jqnum){
			UserDAOByFile userDAO = new UserDAOByFile();
			User user = userDAO.findById(jqnum);
			User self = userDAO.findById(this.jqnum);
			if(user!=null && self!=null){
				Vector<Integer> v = user.getListFriend();
				//好友不存在好友列表的情况下再添加
				if(!v.contains(this.jqnum)){
					v.add(this.jqnum);
					user.setListFriend(v);
					writeLog(getLog(user, " 用户["+user.getNickname()+"("+user.getJqnum()+")]添加好友["+self.getNickname()+"("+self.getJqnum()+")]"));
					
					try {
						userDAO.update(user);
					} catch (FileNotFoundException e) {
						writeLog(getLog(user, " 用户["+user.getNickname()+"("+user.getJqnum()+")]添加好友["+self.getNickname()+"("+self.getJqnum()+")]时发生错误:"+e.getMessage()));
						return false;
					} catch (IOException e) {
						writeLog(getLog(user, " 用户["+user.getNickname()+"("+user.getJqnum()+")]添加好友["+self.getNickname()+"("+self.getJqnum()+")]时发生错误:"+e.getMessage()));
						return false;
					}
				}
				Vector<Integer> v2 = self.getListFriend();
				if(!v2.contains(jqnum)){
					v2.add(jqnum);
					self.setListFriend(v2);
					writeLog(getLog(user, " 用户["+self.getNickname()+"("+self.getJqnum()+")]添加好友["+user.getNickname()+"("+user.getJqnum()+")]"));
					try {
						userDAO.update(self);
					} catch (FileNotFoundException e) {
						v.remove(this.jqnum);
						user.setListFriend(v);
						try {
							userDAO.update(user);
						} catch (FileNotFoundException e1) {} catch (IOException e1) {}
						writeLog(getLog(self, " 用户["+self.getNickname()+"("+self.getJqnum()+")]添加好友["+user.getNickname()+"("+user.getJqnum()+")]时发生错误:"+e.getMessage()));
						return false;
					} catch (IOException e) {
						v.remove(this.jqnum);
						user.setListFriend(v);
						try {
							userDAO.update(user);
						} catch (FileNotFoundException e1) {} catch (IOException e1) {}
						writeLog(getLog(self, " 用户["+self.getNickname()+"("+self.getJqnum()+")]添加好友["+user.getNickname()+"("+user.getJqnum()+")]时发生错误:"+e.getMessage()));
						return false;
					}
				}
				return true;
			}else
				return false;
		}
		
		/**
		 * 传输当前的在线用户FriendUser。
		 */
		private void dealFindOnlineUser(){
			UserDAOByFile userDAO = new UserDAOByFile();
			Enumeration<Integer> en = table.keys();
			while(en.hasMoreElements()){
				int jqnum = en.nextElement();
				if(jqnum!=this.jqnum){
					User user = userDAO.findById(jqnum);
					if(user!=null && !user.getListFriend().contains(this.jqnum)){
						FriendUser friendUser = new FriendUser();
						friendUser.setJqnum(jqnum);
						friendUser.setPhoto(user.getPhoto());
						friendUser.setNickName(user.getNickname());
						friendUser.setSignature(user.getSignature());
						friendUser.setState(user.getState());
						JQMessage message = new JQMessage();
						message.setType(43);
						message.setObj(friendUser);
						writeToClient(message);
					}
				}
			}
		}
		
		/**
		 * 传输当前在线用户数。
		 */
		private void dealFindOnlineUserNum(){
			JQMessage message = new JQMessage();
			message.setType(41);
			message.setObj(table.size());
			writeToClient(message);
		}
		
		/**
		 * 处理客户端发送过来的消息。
		 * @param message 发送过来的消息。
		 * @throws FileNotFoundException 发送给好友失败时抛出此异常。
		 * @throws IOException 发送给好友失败是抛出此异常。
		 */
		private void dealMessage(JQMessage message) throws FileNotFoundException, IOException{
			if(message.getObj() instanceof Record){
				Record record = (Record)message.getObj();
				record.setSendTime(new Date());
				int jqnum = record.getToid();
				//发送消息的好友是否检测在线
				//System.out.println("新消息"+record);
				if(isHasLoged(jqnum)){
					ClientLink client = table.get(jqnum);
					sendRecordToClient(client, record);
				}else{
					record.setRead(false);
					record.setReadTime(new Date());
					record.setSendTime(new Date());
					RecordDAOByFile recordDAO = new RecordDAOByFile();
					recordDAO.addRecordForAdmin(record);
				}
			}else
				writeSysLog("客户端"+getClientIP()+"发送错误的数据信息["+message.getObj()+"]到服务端");
		}
		
		/**
		 * 发送消息到客户端。
		 * @param client 发送的目标Socket
		 * @param record 发送的记录
		 * @throws FileNotFoundException 发送失败抛出此异常
		 * @throws IOException 发送失败抛出此异常
		 */
		private void sendRecordToClient(ClientLink client,Record record) throws FileNotFoundException, IOException{
			JQMessage message = new JQMessage();
			message.setType(31);
			message.setObj(record);
			if(client.writeToClient(message)){
				record.setRead(true);
				record.setReadTime(new Date());
			}else{
				record.setRead(false);
				RecordDAOByFile recordDAO = new RecordDAOByFile();
				recordDAO.addRecordForAdmin(record);
			}
		}
		
		/**
		 * 处理注册用户。
		 * @param message JQMessage对象。
		 * @throws FileNotFoundException
		 * @throws IOException
		 */
		private void dealRegiter(JQMessage message) throws FileNotFoundException, IOException{
			if(message.getObj() instanceof User){
				User user = (User)message.getObj();
				JQCreater creater = new JQCreater();
				int id = creater.createID();
				int num = creater.createJQ();
				creater.saveIDJQ(id, num);
				user.setId(id);
				user.setJqnum(num);
				user.setRegisterTime(new Date());
				UserDAOByFile userDAO = new UserDAOByFile();
				boolean b =userDAO.add(user);
				eachAddFriend(JQServer.manager);
				JQMessage regResult = new JQMessage();
				if(b){
					regResult.setType(11);
					RegUser regUser = new RegUser();
					regUser.setJqnum(user.getJqnum());
					regUser.setNickname(user.getNickname());
					regUser.setRealname(user.getRealname());
					regUser.setPassword(user.getPassword());
					regResult.setObj(regUser);
					writeLog(getLog(user,"新用户注册成功!"));
				}else{
					regResult.setType(12);
					regResult.setObj(null);
					writeLog(getLog(user,"用户注册失败!"));
				}
				writeToClient(regResult);
			}else{
				writeSysLog("客户端"+getClientIP()+"发送错误的数据信息到服务端");
			}
			//closeClient();
		}
		
		/**
		 * 处理用户登陆。
		 * @param message JQMessage对象。
		 */
		private void DealLogin(JQMessage message){
			//用户登陆
			if(message.getObj() instanceof LoginUser){
				LoginUser loginUser = (LoginUser)message.getObj();
				String inputPassword = loginUser.getPassword();
				int jqnum = loginUser.getJqnum();
				UserDAOByFile userDAO = new UserDAOByFile();
				try {
					User user = userDAO.findById(jqnum);
					JQMessage loginResult = new JQMessage();
					//用户存在
					if(user!=null){
						if(user.getPassword().equals(inputPassword)){
							this.jqnum = jqnum;
							//正确登陆
							//检测用户是否已经登陆，若已登陆，将之前的登陆用户下线。
							if(isHasLoged(jqnum))
								letClientLogout(jqnum, client.getInetAddress().toString());
							//更改用户状态
							//System.out.println("状态:"+loginUser.getState());
							user.setState(loginUser.getState());
							userDAO.update(user);
							//发送用户的好友的状态.
							loginResult.setType(21);
							Vector<FriendUser> friends = new Vector<FriendUser>();
							FriendUser SelfUser = new FriendUser();
							SelfUser.setJqnum(user.getJqnum());
							SelfUser.setNickName(user.getNickname());
							SelfUser.setPhoto(user.getPhoto());
							SelfUser.setSignature(user.getSignature());
							SelfUser.setState(user.getState());
							friends.add(SelfUser);
							Vector<Integer> listFriend = user.getListFriend();
							//System.out.println(listFriend.size());
							for(int i=0;i<listFriend.size();i++){
								Integer friend_jqnum = listFriend.get(i);
								User friend_user = userDAO.findById(friend_jqnum);
								if(friend_user!=null){
									FriendUser friendUser = new FriendUser();
									friendUser.setJqnum(friend_user.getJqnum());
									friendUser.setNickName(friend_user.getNickname());
									friendUser.setPhoto(friend_user.getPhoto());
									friendUser.setSignature(friend_user.getSignature());
									friendUser.setState(friend_user.getState());
									friends.add(friendUser);
								}
							}
							loginResult.setObj(friends);
							//向table中添加该用户
							if(writeToClient(loginResult))
								table.put(user.getJqnum(), this);
							else
								return;
							//通知好友，我上线了
							writeLog(getLog(user, "用户登录"));
							telFriendState(user);
							//检测是否存在好友的留言，发送过去给用户。
							RecordDAOByFile recordDAO = new RecordDAOByFile();
							Vector<Record> v = recordDAO.findLeaveRecord(jqnum);
							try {
								if(v!=null){
									for(Record record:v)
										sendRecordToClient(this, record);
									recordDAO.deleteRecordForAdmin(jqnum);
									//System.out.println(recordDAO.deleteRecordForAdmin(jqnum));
								}
							} catch (RuntimeException e) {
								writeLog(getLog(user, "发送留言给用户时发生错误:"+e.getMessage()));
							}
						}else{
						//密码错误
							loginResult.setType(22);
							loginResult.setObj("错误的登陆密码["+loginUser.getJqnum()+"]");
							writeToClient(loginResult);
							writeLog(getLoginLog(loginUser, "错误的用户["+loginUser.getJqnum()+"]登录密码"));
							closeClient();
						}
					}else{
						//不存在的用户.
						loginResult.setType(22);
						loginResult.setObj("不存在的用户["+loginUser.getJqnum()+"]");
						writeToClient(loginResult);
						writeLog(getLoginLog(loginUser, "不存在的用户["+loginUser.getJqnum()+"]登录"));
						closeClient();
					}
				} catch (ClassNotFoundException e) {
					writeSysLog("错误:"+e.getMessage());
				} catch (FileNotFoundException e) {
					writeSysLog("错误:"+e.getMessage());
				} catch (IOException e) {
					writeSysLog("错误:"+e.getMessage());
				}		
			}else{
				writeSysLog("客户端"+getClientIP()+"发送错误的数据信息到服务端");
				closeClient();
			}
			
			
		}
		/**
		 * 告诉当前用户的好友自己的状态。
		 * @param user 当前用户.
		 * @throws FileNotFoundException 程序运行异常将抛出此异常。
		 * @throws IOException  程序运行异常将抛出此异常。
		 * @throws ClassNotFoundException 程序运行异常将抛出此异常。
		 */
		private void telFriendState(User user) throws FileNotFoundException, IOException, ClassNotFoundException{
			FriendUser meState = new FriendUser();
			meState.setJqnum(user.getJqnum());
			meState.setState(user.getState());
			meState.setNickName(user.getNickname());
			meState.setPhoto(user.getPhoto());
			meState.setSignature(user.getSignature());
			JQMessage friendLogin = new JQMessage();
			friendLogin.setType(25);
			friendLogin.setObj(meState);
			Vector<Integer> listFriend = user.getListFriend();
			//UserDAOByFile userDAO = new UserDAOByFile();
			for(int i=0;i<listFriend.size();i++){
				Integer friend_jqnum = listFriend.get(i);
				/*User friend_user = userDAO.findById(friend_jqnum);
				
				if((state==UserState.ONLINESTATE.getState() || state==UserState.BUSYSTATE.getState() || state==UserState.DEPARTURESTATE.getState()) && isHasLoged(friend_jqnum) && friend_user.getState()!=UserState.OFFLIENSTATE.getState()){
					writeToClient(friendLogin);
				}*/
				if(isHasLoged(friend_jqnum)){
					ClientLink client = table.get(friend_jqnum);
					client.writeToClient(friendLogin);
				}
			}
		}
		
		
		/**
		 * 想客户端发送消息。
		 * @param message JQMessage对象。
		 * @return 返回发送成功否。
		 */
		public boolean writeToClient(JQMessage message){
			
		/*	if(oos!=null){
				try {
					oos.writeObject(message);
					oos.flush();
					return true;
				} catch (IOException e) {
					writeSysLog("向客户端["+client.getLocalAddress().toString()+":"+client.getLocalPort()+"]发送数据失败!");
					return false;
				}
			}else
				return false;*/
			new ClientWrite(this,message).start();
			return true;
			
		}
		
		/**
		 * 根据User的情况和日志内容获得日志对象。
		 * @param user User对象。
		 * @param what 日志内容。
		 * @return 返回日志对象。
		 */
		public Log getLog(User user,String what){
			Log log = new Log();
			log.setUserid(user.getJqnum());
			log.setIp(client.getLocalAddress().toString());
			log.setNickname(user.getNickname());
			log.setTime(new Date());
			log.setUserid(user.getJqnum());
			log.setWhat(what);
			return log;
		}
		
		/**
		 * 根据FriendUser的情况和日志内容获得日志对象。
		 * @param user FriendUser对象。
		 * @param what 日志内容。
		 * @return 返回日志对象。
		 */
		public Log getLog(FriendUser user,String what){
			Log log = new Log();
			log.setUserid(user.getJqnum());
			log.setIp(client.getLocalAddress().toString());
			log.setNickname(user.getNickName());
			log.setTime(new Date());
			log.setUserid(user.getJqnum());
			log.setWhat(what);
			return log;
		}
		
		/**
		 * 根据LoginUser和日志内容返回日志对象。
		 * @param user LoginUser对象。
		 * @param what 日志内容。
		 * @return 返回日志对象。
		 */
		private Log getLoginLog(LoginUser user,String what){
			Log log = new Log();
			log.setNickname("未知用户");
			log.setUserid(user.getJqnum());
			log.setIp(client.getLocalAddress().toString());
			log.setTime(new Date());
			log.setWhat(what);
			return log;
			
		}
		
		/**
		 * 更新用户的状态。
		 * @param jqnum jq号码。
		 * @param state 用户的当前状态。
		 */
		private void updateUserState(int jqnum,int state){
			UserDAOByFile userDAO = new UserDAOByFile();
			User user = userDAO.findById(jqnum);
			if(user!=null){
				try {
				user.setState(state);
				userDAO.update(user);
				} catch (FileNotFoundException e) {
					writeLog(getLog(user, "更改用户状态时发生错误:"+e.getMessage()));
				} catch (IOException e) {
					writeLog(getLog(user, "更改用户状态时发生错误:"+e.getMessage()));
				}
			}
		}
	}
	
	/**
	 * 客户端的输出线程类。
	 * 2008-9-27
	 * @author		达内科技[Tarena Training Group]
	 * @version	1.0
	 * @since		JDK1.6(建议) 
	 */
	private class ClientWrite extends Thread{
		
		private ClientLink clientLink;
		private JQMessage message;
		
		public ClientWrite(ClientLink clientLink,JQMessage message) {
				this.clientLink = clientLink;
				this.message = message;
		}
		
		public void run() {
			if(clientLink.oos!=null){
				try {
					clientLink.oos.writeObject(message);
					clientLink.oos.flush();
				} catch (IOException e) {
					writeSysLog("向客户端"+clientLink.getClientIP()+"发送数据失败!");
				}
			}
		}
	}
	
	/**
	 * 检测jqnum用户是否已经登陆。
	 * @param jqnum
	 * @return 返回用户是否登陆否。
	 */
	private boolean isHasLoged(int jqnum){
		return table.containsKey(jqnum);
	}
	/**
	 * 断开和服务端的连接。
	 * @param jqnum 
	 * @param ip
	 */
	private void letClientLogout(int jqnum,String ip){
		if(isHasLoged(jqnum)){
			ClientLink clientLink = table.get(jqnum);
			JQMessage message = new JQMessage();
			message.setType(23);
			message.setObj("您的账号在别处[IP:"+ip+"]登录,程序将退出!");
			clientLink.writeToClient(message);
			table.remove(jqnum);
			clientLink.closeClient();
		}
	}
	
	/**
	 * 客户端异常发生和服务端断开连接时，清空table中断开的客户端~~，这是绝对的需要处理的。
	 * @param client
	 */
	private void removeClientForException(ClientLink client){
		if(client!=null && table!=null && table.contains(client)){
			Enumeration<Integer> en = table.keys();
			while(en.hasMoreElements()){
				Integer jqnum = en.nextElement();
				if(table.get(jqnum).equals(client) || table.get(jqnum)==client){
					table.remove(jqnum);
					//更改用户状态
					client.updateUserState(jqnum, UserState.OFFLIENSTATE.getState());
					break;
				}
			}
		}
	}
	
	/**
	 * 广播系统消息。
	 * @param msg 消息内容
	 * @return 返回广播成功否。
	 */
	public static boolean broadcast(String msg){
		if(isServiceRun){
			JQMessage message = new JQMessage();
			message.setType(91);
			message.setObj("  系统消息:"+msg+"\n[该消息发布于"+DateDeal.getCurrentTime()+"]");
			Enumeration<ClientLink> en = table.elements();
			while(en.hasMoreElements()){
				en.nextElement().writeToClient(message);
			}
			return true;
		}else{
			JOptionPane.showMessageDialog(null, "系统服务未启动，无法发送广播!");
			return false;
		}
	}
}

	