
package server.frm.panes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import server.frm.Server;
import tools.DateDeal;

import dao.UserDAOByFile;
import data.User;
import data.UserSex;
import data.UserState;

 /**
 * �����û���塣
 */
public class OnlinePane extends JPanel implements ActionListener,Runnable{
	/** ��ѯJQ�û� */
	private JLabel lblQuery = new JLabel("��ѯJQ�û�");
	/** ����Ĳ�ѯ�Ĺؼ��� */
	private JTextField txtQuery = new JTextField("");
	/** ��ѯ������ */
	private JComboBox boxQuery = new JComboBox();
	/** ��ѯ */
	private JButton btnQuery = new JButton("��ѯ");
	/** ������û� */
	private JButton btnBroadCast = new JButton("�㲥ϵͳ��Ϣ");
	/** ˢ�����»�������û� */
	private JButton btnFlash = new JButton("ˢ��");
	
	/** ��ʾ�û��ı�� */
	private JTable table = null; //= new JTable();
	/** ����ģ�� */
	private DefaultTableModel model = null;
	
	/** ״̬�� */
	private JLabel lblInfo = new JLabel("����JQ�û�:0λ");
	
	private BroadcastWindow broadcastWindow = null;
	
	public OnlinePane() {
		btnBroadCast.addActionListener(this);
		btnFlash.addActionListener(this);
		btnQuery.addActionListener(this);
		init();
	}
	
	/**
	 * ��ʼ������������ʼ����塣��Ϊʱ���ϵ������д�ıȽϴ��������¡�
	 */
	private void init(){
		txtQuery.setPreferredSize(new Dimension(100,25));
		boxQuery.addItem(new QueryWay("��JQ�Ų�ѯ",0));
		boxQuery.addItem(new QueryWay("���ǳƲ�ѯ",1));
		
		model = new MyDefaultTableModel();
		model.addColumn("ID");
		model.addColumn("JQ��");
		model.addColumn("�ǳ�");
		model.addColumn("����");
		model.addColumn("�Ա�");
		model.addColumn("����");
		model.addColumn("E-mail");
		model.addColumn("״̬");
		model.addColumn("ע��ʱ��");
		
		table = new JTable(model);
		TableRowSorter sorter = new TableRowSorter(model); 
		table.setRowSorter(sorter);
		TableColumn tcID = table.getColumn(model.getColumnName(0));
		tcID.setPreferredWidth(25);
		tcID.setMaxWidth(25);
		tcID.setMinWidth(25);
		TableColumn tcJQ = table.getColumn(model.getColumnName(1));
		tcJQ.setPreferredWidth(70);
		tcJQ.setMaxWidth(80);
		tcJQ.setMinWidth(60);
		TableColumn tcNick = table.getColumn(model.getColumnName(2));
		tcNick.setPreferredWidth(70);
		tcNick.setMaxWidth(80);
		tcNick.setMinWidth(60);
		TableColumn tcName = table.getColumn(model.getColumnName(3));
		tcName.setPreferredWidth(60);
		tcName.setMaxWidth(65);
		tcName.setMinWidth(55);
		TableColumn tcSex = table.getColumn(model.getColumnName(4));
		tcSex.setPreferredWidth(30);
		tcSex.setMaxWidth(30);
		tcSex.setMinWidth(30);
		TableColumn tcAge = table.getColumn(model.getColumnName(5));
		tcAge.setPreferredWidth(30);
		tcAge.setMaxWidth(30);
		tcAge.setMinWidth(30);
		TableColumn tcState = table.getColumn(model.getColumnName(7));
		tcState.setPreferredWidth(30);
		tcState.setMaxWidth(30);
		tcState.setMinWidth(30);
		
		JPanel paneNorth = new JPanel();
		paneNorth.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		paneNorth.add(lblQuery);
		paneNorth.add(txtQuery);
		paneNorth.add(boxQuery);
		paneNorth.add(btnQuery);
		paneNorth.add(btnFlash);
		paneNorth.add(btnBroadCast);
		
		JPanel paneSouth = new JPanel();
		paneSouth.add(lblInfo);
		
		setLayout(new BorderLayout());
		add(paneNorth,BorderLayout.NORTH);
		add(new JScrollPane(table),BorderLayout.CENTER);
		add(new FillWidth(5,5),BorderLayout.WEST);
		add(new FillWidth(5,5),BorderLayout.EAST);
		add(paneSouth,BorderLayout.SOUTH);
	}
	/**
	 * �㲥��Ϣ��ť��ˢ�°�ť����ѯ��ť���¼���
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnBroadCast){
			if(broadcastWindow==null)
				broadcastWindow = new BroadcastWindow();
			else
				broadcastWindow.setVisible(true);
		}
		if(e.getSource()==btnFlash){
			flushOnlineUser();
		}
		if(e.getSource()==btnQuery){
			QueryOnlineUser(txtQuery.getText(),((QueryWay)boxQuery.getSelectedItem()).getWay());
		}
	}
	
	/**
	 * ��ѯ��ʽ��
	 */
	private class QueryWay{
		private String name;
		private int way;
		public QueryWay(String name,int way) {
			this.name = name;
			this.way = way;
		}
		public String toString() {
			return this.name;
		}
		public int getWay() {
			return way;
		}
	}
	
	public void run() {
		btnFlash.setEnabled(false);
		if(Server.isFileWay){
			table.setAutoCreateRowSorter(false);
			model.setRowCount(0);
			int totalNum = 0;
			int onlineNum = 0;
			int hiddenNum = 0;
			int departureNum = 0;
			int busyNum = 0;
			if(ServicePane.table!=null && !ServicePane.table.isEmpty()){
				totalNum = ServicePane.table.size();
				Enumeration<Integer> onlineUser = ServicePane.table.keys();
				UserDAOByFile userDAO = new UserDAOByFile();
				while(onlineUser.hasMoreElements()){
					Integer jqnum = onlineUser.nextElement();
					User user = userDAO.findById(jqnum);
					if(user!=null){
						if(user.getState()==UserState.ONLINESTATE.getState())
							onlineNum++;
						if(user.getState()==UserState.DEPARTURESTATE.getState())
							departureNum++;
						if(user.getState()==UserState.BUSYSTATE.getState())
							busyNum++;
						if(user.getState()==UserState.HIDDENSTATE.getState())
							hiddenNum++;
					}
					addUserToTable(user);
				}
			}
			table.setAutoCreateRowSorter(true);
			lblInfo.setText("����JQ�û�:"+totalNum+"λ.����," +
					"�����û�:"+onlineNum+"λ." +
					"�����û�:"+hiddenNum+"λ." +
					"�뿪�û�:"+departureNum+"λ." +
					"��æ�û�:"+busyNum+"λ.");
		}else
			lblInfo.setText("ע��:ϵͳ�ݲ�֧�����ݿⷽʽ!��ʹ���ļ���ʽ��������!");
		btnFlash.setEnabled(true);
	}
	
	/**
	 * ˢ�µ�ǰ�����û���
	 */
	public void flushOnlineUser(){
		new Thread(this).start();
	}
	
	/**
	 * ���ݲ�ѯ���Ͳ�ѯ�����û�
	 * @param query ��ѯ��������
	 * @param type ��ѯ�����͡�
	 */
	private void QueryOnlineUser(String query,int type){
		//��jq�Ų�ѯ
		if(query.equals(""))
			return;
		UserDAOByFile userDAO = new UserDAOByFile();
		model.setRowCount(0);
		if(type==0){
			try {
				int jqnum = Integer.parseInt(query);
				if(ServicePane.table.containsKey(jqnum)){
					User user = userDAO.findById(jqnum);
					if(user!=null)
						addUserToTable(user);
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "��ȷ���������ȷ��JQ����!");
				return;
			}
			return;
		}
		//���ǳƺŲ�ѯ
		if(type==1){
				Vector<User> users= userDAO.findOnlineUserByName(query);
				for(User user:users){
					if(user!=null)
						addUserToTable(user);
				}
			return;
		}
		
	}
	
	/**
	 * ��table������û�
	 * @param user User����
	 * @see User
	 */
	private void addUserToTable(User user){
		if(user!=null){
			Vector<Object> v = new Vector<Object>();
			v.add(user.getId());
			v.add(user.getJqnum());
			v.add(user.getNickname());
			v.add(user.getRealname());
			v.add(UserSex.getSex(user.getSex()));
			v.add(user.getAge());
			v.add(user.getEmail());
			v.add(UserState.getStateName(user.getState()));
			v.add(DateDeal.getDate(user.getRegisterTime()));
			
			model.addRow(v);
		}
	}
	
	/**
	 * �Լ����Ƶ�DefaultTableModel
	 */
	private class MyDefaultTableModel extends DefaultTableModel{
		
		public boolean isCellEditable(int row, int column) {
			super.isCellEditable(row, column);
			return false;
		}
	}
	
	/**
	 * �㲥�����ࡣ
	 */
	private class BroadcastWindow extends JDialog implements WindowFocusListener,ActionListener{
		private JTextArea txt = new JTextArea();
		private JButton btnSend = new JButton("����");
		private JButton btnClose = new JButton("�ر�");
		private JButton btnClear = new JButton("���");
		public BroadcastWindow() {
			//super(null,true);
			setTitle("�㲥ϵͳ��Ϣ");
			setSize(300,200);
			setResizable(false);
			Toolkit tk=Toolkit.getDefaultToolkit();
			setLocation((tk.getScreenSize().width-getSize().width)/2,(tk.getScreenSize().height-getSize().height)/2);
			
			btnSend.addActionListener(this);
			btnClear.addActionListener(this);
			btnClose.addActionListener(this);
			
			JPanel pane = new JPanel();
			pane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			pane.setPreferredSize(new Dimension(300,35));
			pane.add(btnClose);
			pane.add(btnClear);
			pane.add(btnSend);
			
			txt.setLineWrap(true);
			add(new JScrollPane(txt),BorderLayout.CENTER);
			add(pane,BorderLayout.SOUTH);
			add(new FillWidth(2,2),BorderLayout.NORTH);
			add(new FillWidth(4,4),BorderLayout.EAST);
			add(new FillWidth(4,4),BorderLayout.WEST);
			
			addWindowFocusListener(this);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setVisible(true);
		}
		public void windowGainedFocus(WindowEvent e) {}
		public void windowLostFocus(WindowEvent e) {
			dispose();
		}
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==btnClear)
				txt.setText("");
			if(e.getSource()==btnClose)
				dispose();
			if(e.getSource()==btnSend){
				String msg = txt.getText();
				if(!msg.equals("")){
					if(ServicePane.broadcast(msg)){
						txt.setText("");
						JOptionPane.showMessageDialog(null, "���ͳɹ�!");
					}				
				}
					
			}
		}
	}
}
