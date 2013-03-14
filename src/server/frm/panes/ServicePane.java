/**
  * @(#)server.frm.panes.ServicePane.java  2008-8-28  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: ϵͳ��������ࡣ
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-8-28 	С��     		�½�
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
 * ϵͳ��������ࡣ<br>
 * ��ɹ��ܣ�ϵͳ��������ֹͣ<br>
 * �û���������־��ʾ��
 */
public class ServicePane extends JPanel implements ActionListener,Runnable{

	/** ����JQ����ť */
	private JButton btnStart = new JButton("����Java QQ����");
	/** ֹͣJQ����ť */
	private JButton btnStop = new JButton("ֹͣJava QQ����");
	/**  */
	private ServecieProcessBar bar = new ServecieProcessBar(300,30);
	/** ��ʾ������־ */
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
			areaLog.append("�����쳣������ȷ��"+path+"�ļ���д!ԭ������:"+e.getMessage());
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
	 * ��ʼ�������ļ���
	 */
	public void initProp(){
 		try {
			Server.prop = GetParameter.getProp();
		} catch (Exception e) {
			writeSysLog(DateDeal.getCurrentTime()+",���������ļ�ʱ��������ԭ������:"+e.getMessage());
		}
		int dataWay = Integer.parseInt(Server.prop.getProperty(GetParameter.keys[6]));
		if(dataWay==0)
			Server.isFileWay = true;
		else{
			Server.isFileWay = false;
			writeSysLog("ע��:ϵͳ�ݲ�֧�����ݿⷽʽ!��ʹ���ļ���ʽ��������!");
		}
		int saveLog = Integer.parseInt(Server.prop.getProperty(GetParameter.keys[4]));
		if(saveLog==1)
			Server.isSaveLog = true;
		else
			Server.isSaveLog = false;
	}
	
	/**
	 * ������ť��ֹͣ��ť���¼���
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnStart){
			initProp();
			if(!Server.isFileWay){
				JOptionPane.showMessageDialog(null, "�ݲ�֧�����ݿ�洢��ʽ!��ѡ���ļ���ʽ!");
				return;
			}
			try {
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
				bar.startRoll();
				startServer();
			} catch (IOException e1) {
				writeSysLog(DateDeal.getCurrentTime()+",JQ��������������ʱ��������ԭ������:"+e1.getMessage());
			}
		}
		if(e.getSource()==btnStop){
			btnStart.setEnabled(true);
			btnStop.setEnabled(false);
			bar.stopRoll();
			try {
				stopServer();
			} catch (IOException e1) {
				writeSysLog(DateDeal.getCurrentTime()+",JQ������ֹͣ����ʱ��������ԭ������:"+e1.getMessage());
			}
		}
	}
	
	/**
	 * ��дϵͳ��־��
	 * @param log ��־��
	 */
	public void writeSysLog(String log){
		areaLog.append(log+"\n");
		//�������Զ��¹�
		areaLog.setCaretPosition(areaLog.getDocument().getLength());
		raf.write(log+"\n");
		raf.flush();
	}
	
	/**
	 * ��д��־��
	 * @param log ��־��
	 */
	private void writeLog(Log log){
		try {
			LogDAOByFile LogDAO = new LogDAOByFile();
			LogDAO.add(log);
			Server.logPane.getAreaLog().append(log.toString()+"");
			Server.logPane.getAreaLog().setCaretPosition(Server.logPane.getAreaLog().getDocument().getLength());
		} catch (IOException e) {
			writeSysLog(DateDeal.getCurrentTime()+",д�������־["+log.toString()+"]ʱ��������:"+e.getMessage());
		}
	}
	
	/**
	 * ������������
	 * @throws IOException IO�쳣��
	 */
	public void startServer() throws IOException{
		isServiceRun = true;
		int port = Integer.parseInt(Server.prop.getProperty(GetParameter.keys[0]));
		table = new Hashtable<Integer,ClientLink>();
		server = new ServerSocket(port);
		new Thread(this).start();
		writeSysLog(DateDeal.getCurrentTime()+",JQ���������������ɹ�!�ȴ�JQ�û�����...");
	}
	
	/**
	 * ֹͣ��������
	 * @throws IOException IO�쳣��
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
		writeSysLog(DateDeal.getCurrentTime()+",JQ����������ֹͣ�ɹ�!");
	}
	
	@Override
	public void run() {
		while(isServiceRun){
			try {
				Socket client = server.accept();
				//System.out.println("come accept");
				new Thread(new ClientLink(client)).start();
			} catch (IOException e) {
				writeSysLog(DateDeal.getCurrentTime()+",JQ���������ܿͻ���ʱ�����쳣:"+e.getMessage());
			}
		}
	}
	
	/**
	 * �ͻ��������ࡣ����Ϳͻ��˵ĸ�����Ϣ��
	 */
	private class ClientLink implements Runnable,Serializable{
		
		public Socket client = null;
		public ObjectInputStream ois = null;
		public ObjectOutputStream oos = null;
		public int jqnum = -1;
		
		public ClientLink(Socket client) {
			
			this.client = client;
			writeSysLog(DateDeal.getCurrentTime()+",�ͻ���"+getClientIP()+"]���ӵ������");
			try {
				ois = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
				oos = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
				//ois = new ObjectInputStream(client.getInputStream());
				//oos = new ObjectOutputStream(client.getOutputStream());
				//System.out.println("come client");
			} catch (IOException e) {
				writeSysLog(DateDeal.getCurrentTime()+",��ȡ���ͻ���"+getClientIP()+"�����ӷ�������:"+e.getMessage());
			}
		}
		
		/*��1��ͷ�ģ�ע�������Ϣ
		10:�ͻ��˷���ע����Ϣ�������
		11:����˻ظ�ע��ɹ����ͻ���
		12:����˻ظ�ע��ʧ�ܵ��ͻ���
		��2��ͷ�ģ���½�����Ϣ
		20:�ͻ��˷��͵�½��Ϣ������ˣ��жϴ��û��Ƿ��ѵ�¼��
		21:��½�ɹ�����˷��ͺ�����Ϣ���ͻ���
		22:��¼ʧ�ܷ���˷��ʹ�����Ϣ���ͻ���
		23:����˷����˺��ڱ𴦵�½
		24:�ͻ��˷����˳��������
		25:����˷��ͺ������߹���
		��3��ͷ�ģ����ͼ�¼�����Ϣ
		30:�ͻ��˷�����Ϣ�������
		31:����˸�����Ϣ���͵��ͻ���
		��4��ͷ�ģ����������û��Ӻ��������Ϣ
		40:�ͻ��˷��ͻ�ȡ�����û���
		41:����˷��������û���
		42:�ͻ��˷��ͻ�ȡ��ǰ�����û�
		43:����˷��������б��û�
		44:�ͻ��˷�����Ӻ��ѵ�qq��
		45:����˷�����Ӻ��ѳɹ�
		46:����˷�����Ӻ���ʧ��
		��9��ͷ�ģ�ϵͳ�����Ϣ
		90:����˷������߹��ܵ��ͻ���
		91:����˷��͹㲥��Ϣ���ͻ���
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
							writeSysLog("�ͻ���"+getClientIP()+"���ʹ����������Ϣ�������");
					}
				} catch (IOException e) {
					//�߼�������~~�������׺���
					removeClientForException(this);
					closeClient();
				} catch (ClassNotFoundException e) {
					removeClientForException(this);
					closeClient();
				}
			}else{
				writeSysLog("ע��:ϵͳ�ݲ�֧�����ݿⷽʽ!��ʹ���ļ���ʽ��������!");
				letClientQuit();
			}
		}
		
		/**
		 * ��ȡ�ͻ���IP
		 * @return �����ַ�����IP��
		 */
		private String getClientIP(){
			return client==null?"[�ͻ����ѹر�,���ܻ�ȡ��Ϣ]":"["+client.getInetAddress().toString()+":"+client.getPort()+"]";
		}
		
		/**
		 * ʹ�ÿͻ������ߡ�
		 */
		private void letClientQuit(){
			JQMessage message = new JQMessage();
			message.setType(90);
			message.setObj("�ͷ���˶Ͽ�����");
			writeToClient(message);
			closeClient();
		}
		
		/**
		 * �رպͿͻ��˵�����
		 */
		private void closeClient(){
			String ip = getClientIP();
			writeSysLog(DateDeal.getCurrentTime()+",�ͻ���"+getClientIP()+"����.");
			try {
				if(oos!=null)oos.close();oos = null;
				if(ois!=null)ois.close();ois = null;
				if(client!=null)client.close();client=null;
			} catch (IOException e) {
				writeSysLog(DateDeal.getCurrentTime()+",�رյ��ͻ���"+ip+"������ʱʱ��������:"+e.getMessage());
			}
		}
		
		/**
		 * ����ͻ����˳���Ϣ��
		 * @param message JQMessage����
		 */
		private void dealQuit(JQMessage message){
			Object obj = message.getObj();
			if(obj instanceof FriendUser){
				FriendUser user = (FriendUser)obj;
				writeLog(getLog(user, "�û��˳�"));
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
		 * ����ע�����û���
		 * @param message JQMessage����
		 */
		private void dealAddUser(JQMessage message){
			if(message.getObj() instanceof Integer){
				int jqnum = (Integer)message.getObj();
				JQMessage message2 = new JQMessage();
				boolean b = eachAddFriend(jqnum);
				if(b){
					message2.setType(45);
					message2.setObj("��ϲ������ӳɹ�!");
				}else{
					message2.setType(46);
					message2.setObj("��Ǹ�������ʧ��!");
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
		 * ���ߺ����ҵ�״̬��
		 * @param jqnum jq���롣
		 * @param user User����
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
		 * �໥��Ӻ��ѡ�
		 * @param jqnum jq���롣
		 * @return Ȼ����ӳɹ���
		 */
		private boolean eachAddFriend(int jqnum){
			UserDAOByFile userDAO = new UserDAOByFile();
			User user = userDAO.findById(jqnum);
			User self = userDAO.findById(this.jqnum);
			if(user!=null && self!=null){
				Vector<Integer> v = user.getListFriend();
				//���Ѳ����ں����б������������
				if(!v.contains(this.jqnum)){
					v.add(this.jqnum);
					user.setListFriend(v);
					writeLog(getLog(user, " �û�["+user.getNickname()+"("+user.getJqnum()+")]��Ӻ���["+self.getNickname()+"("+self.getJqnum()+")]"));
					
					try {
						userDAO.update(user);
					} catch (FileNotFoundException e) {
						writeLog(getLog(user, " �û�["+user.getNickname()+"("+user.getJqnum()+")]��Ӻ���["+self.getNickname()+"("+self.getJqnum()+")]ʱ��������:"+e.getMessage()));
						return false;
					} catch (IOException e) {
						writeLog(getLog(user, " �û�["+user.getNickname()+"("+user.getJqnum()+")]��Ӻ���["+self.getNickname()+"("+self.getJqnum()+")]ʱ��������:"+e.getMessage()));
						return false;
					}
				}
				Vector<Integer> v2 = self.getListFriend();
				if(!v2.contains(jqnum)){
					v2.add(jqnum);
					self.setListFriend(v2);
					writeLog(getLog(user, " �û�["+self.getNickname()+"("+self.getJqnum()+")]��Ӻ���["+user.getNickname()+"("+user.getJqnum()+")]"));
					try {
						userDAO.update(self);
					} catch (FileNotFoundException e) {
						v.remove(this.jqnum);
						user.setListFriend(v);
						try {
							userDAO.update(user);
						} catch (FileNotFoundException e1) {} catch (IOException e1) {}
						writeLog(getLog(self, " �û�["+self.getNickname()+"("+self.getJqnum()+")]��Ӻ���["+user.getNickname()+"("+user.getJqnum()+")]ʱ��������:"+e.getMessage()));
						return false;
					} catch (IOException e) {
						v.remove(this.jqnum);
						user.setListFriend(v);
						try {
							userDAO.update(user);
						} catch (FileNotFoundException e1) {} catch (IOException e1) {}
						writeLog(getLog(self, " �û�["+self.getNickname()+"("+self.getJqnum()+")]��Ӻ���["+user.getNickname()+"("+user.getJqnum()+")]ʱ��������:"+e.getMessage()));
						return false;
					}
				}
				return true;
			}else
				return false;
		}
		
		/**
		 * ���䵱ǰ�������û�FriendUser��
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
		 * ���䵱ǰ�����û�����
		 */
		private void dealFindOnlineUserNum(){
			JQMessage message = new JQMessage();
			message.setType(41);
			message.setObj(table.size());
			writeToClient(message);
		}
		
		/**
		 * ����ͻ��˷��͹�������Ϣ��
		 * @param message ���͹�������Ϣ��
		 * @throws FileNotFoundException ���͸�����ʧ��ʱ�׳����쳣��
		 * @throws IOException ���͸�����ʧ�����׳����쳣��
		 */
		private void dealMessage(JQMessage message) throws FileNotFoundException, IOException{
			if(message.getObj() instanceof Record){
				Record record = (Record)message.getObj();
				record.setSendTime(new Date());
				int jqnum = record.getToid();
				//������Ϣ�ĺ����Ƿ�������
				//System.out.println("����Ϣ"+record);
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
				writeSysLog("�ͻ���"+getClientIP()+"���ʹ����������Ϣ["+message.getObj()+"]�������");
		}
		
		/**
		 * ������Ϣ���ͻ��ˡ�
		 * @param client ���͵�Ŀ��Socket
		 * @param record ���͵ļ�¼
		 * @throws FileNotFoundException ����ʧ���׳����쳣
		 * @throws IOException ����ʧ���׳����쳣
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
		 * ����ע���û���
		 * @param message JQMessage����
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
					writeLog(getLog(user,"���û�ע��ɹ�!"));
				}else{
					regResult.setType(12);
					regResult.setObj(null);
					writeLog(getLog(user,"�û�ע��ʧ��!"));
				}
				writeToClient(regResult);
			}else{
				writeSysLog("�ͻ���"+getClientIP()+"���ʹ����������Ϣ�������");
			}
			//closeClient();
		}
		
		/**
		 * �����û���½��
		 * @param message JQMessage����
		 */
		private void DealLogin(JQMessage message){
			//�û���½
			if(message.getObj() instanceof LoginUser){
				LoginUser loginUser = (LoginUser)message.getObj();
				String inputPassword = loginUser.getPassword();
				int jqnum = loginUser.getJqnum();
				UserDAOByFile userDAO = new UserDAOByFile();
				try {
					User user = userDAO.findById(jqnum);
					JQMessage loginResult = new JQMessage();
					//�û�����
					if(user!=null){
						if(user.getPassword().equals(inputPassword)){
							this.jqnum = jqnum;
							//��ȷ��½
							//����û��Ƿ��Ѿ���½�����ѵ�½����֮ǰ�ĵ�½�û����ߡ�
							if(isHasLoged(jqnum))
								letClientLogout(jqnum, client.getInetAddress().toString());
							//�����û�״̬
							//System.out.println("״̬:"+loginUser.getState());
							user.setState(loginUser.getState());
							userDAO.update(user);
							//�����û��ĺ��ѵ�״̬.
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
							//��table����Ӹ��û�
							if(writeToClient(loginResult))
								table.put(user.getJqnum(), this);
							else
								return;
							//֪ͨ���ѣ���������
							writeLog(getLog(user, "�û���¼"));
							telFriendState(user);
							//����Ƿ���ں��ѵ����ԣ����͹�ȥ���û���
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
								writeLog(getLog(user, "�������Ը��û�ʱ��������:"+e.getMessage()));
							}
						}else{
						//�������
							loginResult.setType(22);
							loginResult.setObj("����ĵ�½����["+loginUser.getJqnum()+"]");
							writeToClient(loginResult);
							writeLog(getLoginLog(loginUser, "������û�["+loginUser.getJqnum()+"]��¼����"));
							closeClient();
						}
					}else{
						//�����ڵ��û�.
						loginResult.setType(22);
						loginResult.setObj("�����ڵ��û�["+loginUser.getJqnum()+"]");
						writeToClient(loginResult);
						writeLog(getLoginLog(loginUser, "�����ڵ��û�["+loginUser.getJqnum()+"]��¼"));
						closeClient();
					}
				} catch (ClassNotFoundException e) {
					writeSysLog("����:"+e.getMessage());
				} catch (FileNotFoundException e) {
					writeSysLog("����:"+e.getMessage());
				} catch (IOException e) {
					writeSysLog("����:"+e.getMessage());
				}		
			}else{
				writeSysLog("�ͻ���"+getClientIP()+"���ʹ����������Ϣ�������");
				closeClient();
			}
			
			
		}
		/**
		 * ���ߵ�ǰ�û��ĺ����Լ���״̬��
		 * @param user ��ǰ�û�.
		 * @throws FileNotFoundException ���������쳣���׳����쳣��
		 * @throws IOException  ���������쳣���׳����쳣��
		 * @throws ClassNotFoundException ���������쳣���׳����쳣��
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
		 * ��ͻ��˷�����Ϣ��
		 * @param message JQMessage����
		 * @return ���ط��ͳɹ���
		 */
		public boolean writeToClient(JQMessage message){
			
		/*	if(oos!=null){
				try {
					oos.writeObject(message);
					oos.flush();
					return true;
				} catch (IOException e) {
					writeSysLog("��ͻ���["+client.getLocalAddress().toString()+":"+client.getLocalPort()+"]��������ʧ��!");
					return false;
				}
			}else
				return false;*/
			new ClientWrite(this,message).start();
			return true;
			
		}
		
		/**
		 * ����User���������־���ݻ����־����
		 * @param user User����
		 * @param what ��־���ݡ�
		 * @return ������־����
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
		 * ����FriendUser���������־���ݻ����־����
		 * @param user FriendUser����
		 * @param what ��־���ݡ�
		 * @return ������־����
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
		 * ����LoginUser����־���ݷ�����־����
		 * @param user LoginUser����
		 * @param what ��־���ݡ�
		 * @return ������־����
		 */
		private Log getLoginLog(LoginUser user,String what){
			Log log = new Log();
			log.setNickname("δ֪�û�");
			log.setUserid(user.getJqnum());
			log.setIp(client.getLocalAddress().toString());
			log.setTime(new Date());
			log.setWhat(what);
			return log;
			
		}
		
		/**
		 * �����û���״̬��
		 * @param jqnum jq���롣
		 * @param state �û��ĵ�ǰ״̬��
		 */
		private void updateUserState(int jqnum,int state){
			UserDAOByFile userDAO = new UserDAOByFile();
			User user = userDAO.findById(jqnum);
			if(user!=null){
				try {
				user.setState(state);
				userDAO.update(user);
				} catch (FileNotFoundException e) {
					writeLog(getLog(user, "�����û�״̬ʱ��������:"+e.getMessage()));
				} catch (IOException e) {
					writeLog(getLog(user, "�����û�״̬ʱ��������:"+e.getMessage()));
				}
			}
		}
	}
	
	/**
	 * �ͻ��˵�����߳��ࡣ
	 * 2008-9-27
	 * @author		���ڿƼ�[Tarena Training Group]
	 * @version	1.0
	 * @since		JDK1.6(����) 
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
					writeSysLog("��ͻ���"+clientLink.getClientIP()+"��������ʧ��!");
				}
			}
		}
	}
	
	/**
	 * ���jqnum�û��Ƿ��Ѿ���½��
	 * @param jqnum
	 * @return �����û��Ƿ��½��
	 */
	private boolean isHasLoged(int jqnum){
		return table.containsKey(jqnum);
	}
	/**
	 * �Ͽ��ͷ���˵����ӡ�
	 * @param jqnum 
	 * @param ip
	 */
	private void letClientLogout(int jqnum,String ip){
		if(isHasLoged(jqnum)){
			ClientLink clientLink = table.get(jqnum);
			JQMessage message = new JQMessage();
			message.setType(23);
			message.setObj("�����˺��ڱ�[IP:"+ip+"]��¼,�����˳�!");
			clientLink.writeToClient(message);
			table.remove(jqnum);
			clientLink.closeClient();
		}
	}
	
	/**
	 * �ͻ����쳣�����ͷ���˶Ͽ�����ʱ�����table�жϿ��Ŀͻ���~~�����Ǿ��Ե���Ҫ����ġ�
	 * @param client
	 */
	private void removeClientForException(ClientLink client){
		if(client!=null && table!=null && table.contains(client)){
			Enumeration<Integer> en = table.keys();
			while(en.hasMoreElements()){
				Integer jqnum = en.nextElement();
				if(table.get(jqnum).equals(client) || table.get(jqnum)==client){
					table.remove(jqnum);
					//�����û�״̬
					client.updateUserState(jqnum, UserState.OFFLIENSTATE.getState());
					break;
				}
			}
		}
	}
	
	/**
	 * �㲥ϵͳ��Ϣ��
	 * @param msg ��Ϣ����
	 * @return ���ع㲥�ɹ���
	 */
	public static boolean broadcast(String msg){
		if(isServiceRun){
			JQMessage message = new JQMessage();
			message.setType(91);
			message.setObj("  ϵͳ��Ϣ:"+msg+"\n[����Ϣ������"+DateDeal.getCurrentTime()+"]");
			Enumeration<ClientLink> en = table.elements();
			while(en.hasMoreElements()){
				en.nextElement().writeToClient(message);
			}
			return true;
		}else{
			JOptionPane.showMessageDialog(null, "ϵͳ����δ�������޷����͹㲥!");
			return false;
		}
	}
}

	