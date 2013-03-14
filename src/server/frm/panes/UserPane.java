///**
//  * @(#)server.frm.panes.UserPane.java  2008-8-29  
//  * Copy Right Information	: Tarena
//  * Project					: JavaQQ
//  * JDK version used		: jdk1.6.4
//  * Comments				: �û���������ࡣ
//  * Version					: 1.0
//  * Sr	Date		Modified By		Why & What is modified
//  * 1.	2008-8-29 	С��     		�½�
//  **/
//package server.frm.panes;
//
//import java.awt.BorderLayout;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.util.Vector;
//
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JLabel;
//import javax.swing.JMenuItem;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JPopupMenu;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//import javax.swing.JTextField;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableColumn;
//import javax.swing.table.TableRowSorter;
//
//import server.frm.Server;
//import tools.DateDeal;
//import dao.UserDAOByFile;
//import data.User;
//import data.UserSex;
//import data.UserState;
//
// /**
// * �û���������ࡣ
// * 2008-8-29
// * @author		���ڿƼ�[Tarena Training Group]
// * @version	1.0
// * @since		JDK1.6(����) 
// */
//public class UserPane extends JPanel implements ActionListener,Runnable{
//	/** ��ѯJQ�û� */
//	private JLabel lblQuery = new JLabel("��ѯJQ�û�");
//	/** ����Ĳ�ѯ�Ĺؼ��� */
//	private JTextField txtQuery = new JTextField("");
//	/** ��ѯ������ */
//	private JComboBox boxQuery = new JComboBox();
//	/** ��ѯ */
//	private JButton btnQuery = new JButton("��ѯ");
//	/** ������û� */
//	private JButton btnAddNew = new JButton("������û�");
//	/** ˢ�����»�������û� */
//	private JButton btnFlash = new JButton("ˢ��");
//	
//	/** ��ʾ�û��ı�� */
//	private JTable table = null;
//	/** ����ģ�� */
//	private DefaultTableModel model = null;
//	
//	/** ״̬�� */
//	private JLabel lblInfo = new JLabel("����JQ�û���0");
//	
//	private JPopupMenu popupMenu = new JPopupMenu();
//	private JMenuItem itemChange = new JMenuItem("�޸�");
//	private JMenuItem itemDelete = new JMenuItem("ɾ��");
//	
//	public UserPane() {
//		init();
//		btnAddNew.addActionListener(this);
//		btnFlash.addActionListener(this);
//		btnQuery.addActionListener(this);
//	}
//	/**
//	 * ��ʼ����塣��Ϊʱ���ϵ������д�ıȽϴ��������¡�
//	 */
//	private void init(){
//		itemChange.addActionListener(this);
//		itemDelete.addActionListener(this);
//		popupMenu.add(itemChange);
//		popupMenu.addSeparator();
//		popupMenu.add(itemDelete);
//		
//		txtQuery.setPreferredSize(new Dimension(100,25));
//		boxQuery.addItem(new QueryWay("��JQ�Ų�ѯ",0));
//		boxQuery.addItem(new QueryWay("���ǳƲ�ѯ",1));
//		
//		model = new MyDefaultTableModel();
//		model.addColumn("ID");
//		model.addColumn("JQ��");
//		model.addColumn("�ǳ�");
//		model.addColumn("����");
//		model.addColumn("�Ա�");
//		model.addColumn("����");
//		model.addColumn("E-mail");
//		model.addColumn("״̬");
//		model.addColumn("ע��ʱ��");
//		
//		table = new JTable(model);
//		TableRowSorter sorter = new TableRowSorter(model); 
//		table.setRowSorter(sorter);
//		table.addMouseListener(new TableMouseAdapter());
//		table.addMouseMotionListener(new TableMouseAdapter());
//		TableColumn tcID = table.getColumn(model.getColumnName(0));
//		tcID.setPreferredWidth(25);
//		tcID.setMaxWidth(25);
//		tcID.setMinWidth(25);
//		TableColumn tcJQ = table.getColumn(model.getColumnName(1));
//		tcJQ.setPreferredWidth(70);
//		tcJQ.setMaxWidth(80);
//		tcJQ.setMinWidth(60);
//		TableColumn tcNick = table.getColumn(model.getColumnName(2));
//		tcNick.setPreferredWidth(70);
//		tcNick.setMaxWidth(80);
//		tcNick.setMinWidth(60);
//		TableColumn tcName = table.getColumn(model.getColumnName(3));
//		tcName.setPreferredWidth(60);
//		tcName.setMaxWidth(65);
//		tcName.setMinWidth(55);
//		TableColumn tcSex = table.getColumn(model.getColumnName(4));
//		tcSex.setPreferredWidth(30);
//		tcSex.setMaxWidth(30);
//		tcSex.setMinWidth(30);
//		TableColumn tcAge = table.getColumn(model.getColumnName(5));
//		tcAge.setPreferredWidth(30);
//		tcAge.setMaxWidth(30);
//		tcAge.setMinWidth(30);
//		TableColumn tcState = table.getColumn(model.getColumnName(7));
//		tcState.setPreferredWidth(30);
//		tcState.setMaxWidth(30);
//		tcState.setMinWidth(30);
//		
//		JPanel paneNorth = new JPanel();
//		paneNorth.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
//		paneNorth.add(lblQuery);
//		paneNorth.add(txtQuery);
//		paneNorth.add(boxQuery);
//		paneNorth.add(btnQuery);
//		paneNorth.add(btnAddNew);
//		paneNorth.add(btnFlash);
//		
//		JPanel paneSouth = new JPanel();
//		paneSouth.add(lblInfo);
//		
//		setLayout(new BorderLayout());
//		add(paneNorth,BorderLayout.NORTH);
//		add(new JScrollPane(table),BorderLayout.CENTER);
//		add(new FillWidth(5,5),BorderLayout.WEST);
//		add(new FillWidth(5,5),BorderLayout.EAST);
//		add(paneSouth,BorderLayout.SOUTH);
//	}
//	/**
//	 * ������û���ˢ�¡���ѯ���޸ġ�ɾ���Ȱ�ť�¼���
//	 */
//	public void actionPerformed(ActionEvent e) {
//		if(e.getSource()==btnAddNew){
//			JOptionPane.showMessageDialog(null, "�˹�����δ����!ллʹ��!���ÿͻ���ע�����û�.");
//		}
//		if(e.getSource()==btnFlash){
//			flushUser();
//		}
//		if(e.getSource()==btnQuery){
//			QueryUser(txtQuery.getText(),((QueryWay)boxQuery.getSelectedItem()).getWay());
//		}
//		if(e.getSource()==itemChange)
//			JOptionPane.showMessageDialog(null, "����������...");
//		if(e.getSource()==itemDelete){
//			int[] rows = table.getSelectedRows();
//			UserDAOByFile userDAO = new UserDAOByFile();
//			for(int i=0;i<rows.length;i++){
//				int jqnum = (Integer)table.getValueAt(rows[i], 1);
//				if(ServicePane.table==null || !ServicePane.table.containsKey(jqnum)){
//					//System.out.println("come:"+jqnum);
//					User user = userDAO.findById(jqnum);
//					if(user!=null){
//						int n = JOptionPane.showConfirmDialog(null, "ȷ��ɾ���û�["+user.getNickname()+"("+user.getJqnum()+")]","ȷ��ɾ��?",JOptionPane.OK_CANCEL_OPTION);
//						if(n==JOptionPane.OK_OPTION)
//							userDAO.delete(user);
//					}
//				}
//			}
//			flushUser();
//		}
//	}
//	
//	/**
//	 * ��ѯ�ķ�ʽ��
//	 * 2008-8-29
//	 * @author		���ڿƼ�[Tarena Training Group]
//	 * @version	1.0
//	 * @since		JDK1.6(����) 
//	 */
//	private class QueryWay{
//		private String name;
//		private int way;
//		public QueryWay(String name,int way) {
//			this.name = name;
//			this.way = way;
//		}
//		public String toString() {
//			return this.name;
//		}
//		public int getWay() {
//			return way;
//		}
//	}
//	
//	@Override
//	public void run() {
//		btnFlash.setEnabled(false);
//		//RowSorter sorter = table.getRowSorter();
//		if(Server.isFileWay){
//			table.setAutoCreateRowSorter(false);
//			model.setRowCount(0);
//			UserDAOByFile userDAO = new UserDAOByFile();
//			Vector<User> users = userDAO.findAll();
//			int totalNum = 0;
//			int onlineNum = 0;
//			int hiddenNum = 0;
//			int departureNum = 0;
//			int busyNum = 0;
//			int offlineNum = 0;
//			for(User user:users){
//				if(user!=null){
//					totalNum++;
//					if(user.getState()==UserState.ONLINESTATE.getState())
//						onlineNum++;
//					if(user.getState()==UserState.DEPARTURESTATE.getState())
//						departureNum++;
//					if(user.getState()==UserState.BUSYSTATE.getState())
//						busyNum++;
//					if(user.getState()==UserState.HIDDENSTATE.getState())
//						hiddenNum++;
//					if(user.getState()==UserState.OFFLIENSTATE.getState())
//						offlineNum++;
//				}
//				addUserToTable(user);
//			}
//			table.setAutoCreateRowSorter(true);
//			lblInfo.setText("����JQ�û�:"+totalNum+"λ.����," +
//					"�����û�:"+onlineNum+"λ." +
//					"�����û�:"+hiddenNum+"λ." +
//					"�뿪�û�:"+departureNum+"λ." +
//					"��æ�û�:"+busyNum+"λ." +
//					"�����û�:"+offlineNum+"λ.");
//		}else
//			lblInfo.setText("ע��:ϵͳ�ݲ�֧�����ݿⷽʽ!��ʹ���ļ���ʽ��������!");
//		btnFlash.setEnabled(true);
//	}
//	
//	/**
//	 * ˢ�µ�ǰ�����û���
//	 */
//	public void flushUser(){
//		new Thread(this).start();
//	}
//	
//	/**
//	 * ����ѯ��ʽ����ѯ���Ͳ����û���
//	 * @param query ��ѯ����
//	 * @param type ��ѯ��ʽ��0:��JQ�����ѯ��1�����ǳƲ�ѯ
//	 */
//	private void QueryUser(String query,int type){
//		if(query.equals(""))
//			return;
//		//��jq�Ų�ѯ
//		UserDAOByFile userDAO = new UserDAOByFile();
//		model.setRowCount(0);
//		if(type==0){
//			try {
//				int jqnum = Integer.parseInt(query);
//					User user = userDAO.findById(jqnum);
//					if(user!=null)
//						addUserToTable(user);
//			} catch (NumberFormatException e) {
//				JOptionPane.showMessageDialog(null, "��ȷ���������ȷ��JQ����!");
//				return;
//			}
//			return;
//		}
//		//���ǳƺŲ�ѯ
//		if(type==1){
//				Vector<User> users= userDAO.findUserByName(query);
//				for(User user:users){
//					if(user!=null)
//						addUserToTable(user);
//				}
//			return;
//		}
//		
//	}
//	
//	/**
//	 * ��table������û�
//	 * @param user User����
//	 */
//	private void addUserToTable(User user){
//		if(user!=null){
//			Vector<Object> v = new Vector<Object>();
//			v.add(user.getId());
//			v.add(user.getJqnum());
//			v.add(user.getNickname());
//			v.add(user.getRealname());
//			v.add(UserSex.getSex(user.getSex()));
//			v.add(user.getAge());
//			v.add(user.getEmail());
//			v.add(UserState.getStateName(user.getState()));
//			v.add(DateDeal.getDate(user.getRegisterTime()));
//			model.addRow(v);
//		}
//	}
//	
//	/**
//	 * ���Ƶ�DefaultTableModel��
//	 * 2008-9-27
//	 * @author		���ڿƼ�[Tarena Training Group]
//	 * @version	1.0
//	 * @since		JDK1.6(����) 
//	 */
//	private class MyDefaultTableModel extends DefaultTableModel{
//		
//		public boolean isCellEditable(int row, int column) {
//			super.isCellEditable(row, column);
//			return false;
//		}
//	}
//	
//	/**
//	 * ����ƶ��������¼���
//	 * 2008-9-27
//	 * @author		���ڿƼ�[Tarena Training Group]
//	 * @version	1.0
//	 * @since		JDK1.6(����) 
//	 */
//	private class TableMouseAdapter extends MouseAdapter{
//		public void mouseEntered(MouseEvent e) {
//
//		}
//		public void mouseClicked(MouseEvent e) {
//			if(e.getButton()==MouseEvent.BUTTON3){
//				popupMenu.show(table,e.getX(),e.getY());
//			}
//		}
//		
//		public void mouseMoved(MouseEvent e) {
//			if(e.getSource()==table){
//				int row = table.rowAtPoint(e.getPoint());
//				table.changeSelection(row, 0, false, true);
//			}
//		}
//	}
//}
/**
  * @(#)server.frm.panes.UserPane.java  2008-8-29  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: �û���������ࡣ
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-8-29 	С��     		�½�
  **/
package server.frm.panes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
 * �û���������ࡣ
 */
public class UserPane extends JPanel implements ActionListener,Runnable{
	/** ��ѯJQ�û� */
	private JLabel lblQuery = new JLabel("��ѯJQ�û�");
	/** ����Ĳ�ѯ�Ĺؼ��� */
	private JTextField txtQuery = new JTextField("");
	/** ��ѯ������ */
	private JComboBox boxQuery = new JComboBox();
	/** ��ѯ */
	private JButton btnQuery = new JButton("��ѯ");
	/** ������û� */
	private JButton btnAddNew = new JButton("������û�");
	/** ˢ�����»�������û� */
	private JButton btnFlash = new JButton("ˢ��");
	
	/** ��ʾ�û��ı�� */
	private JTable table = null;
	/** ����ģ�� */
	private DefaultTableModel model = null;
	
	/** ״̬�� */
	private JLabel lblInfo = new JLabel("����JQ�û���0");
	
	private JPopupMenu popupMenu = new JPopupMenu();
	private JMenuItem itemChange = new JMenuItem("�޸�");
	private JMenuItem itemDelete = new JMenuItem("ɾ��");
	
	public UserPane() {
		init();
		btnAddNew.addActionListener(this);
		btnFlash.addActionListener(this);
		btnQuery.addActionListener(this);
	}
	/**
	 * ��ʼ����塣��Ϊʱ���ϵ������д�ıȽϴ��������¡�
	 */
	private void init(){
		itemChange.addActionListener(this);
		itemDelete.addActionListener(this);
		popupMenu.add(itemChange);
		popupMenu.addSeparator();
		popupMenu.add(itemDelete);
		
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
		table.addMouseListener(new TableMouseAdapter());
		table.addMouseMotionListener(new TableMouseAdapter());
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
		paneNorth.add(btnAddNew);
		paneNorth.add(btnFlash);
		
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
	 * ������û���ˢ�¡���ѯ���޸ġ�ɾ���Ȱ�ť�¼���
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnAddNew){
			JOptionPane.showMessageDialog(null, "�˹�����δ����!ллʹ��!���ÿͻ���ע�����û�.");
		}
		if(e.getSource()==btnFlash){
			flushUser();
		}
		if(e.getSource()==btnQuery){
			QueryUser(txtQuery.getText(),((QueryWay)boxQuery.getSelectedItem()).getWay());
		}
		if(e.getSource()==itemChange)
			JOptionPane.showMessageDialog(null, "����������...");
		if(e.getSource()==itemDelete){
			int[] rows = table.getSelectedRows();
			UserDAOByFile userDAO = new UserDAOByFile();
			for(int i=0;i<rows.length;i++){
				int jqnum = (Integer)table.getValueAt(rows[i], 1);
				if(ServicePane.table==null || !ServicePane.table.containsKey(jqnum)){
					//System.out.println("come:"+jqnum);
					User user = userDAO.findById(jqnum);
					if(user!=null){
						int n = JOptionPane.showConfirmDialog(null, "ȷ��ɾ���û�["+user.getNickname()+"("+user.getJqnum()+")]","ȷ��ɾ��?",JOptionPane.OK_CANCEL_OPTION);
						if(n==JOptionPane.OK_OPTION)
							userDAO.delete(user);
					}
				}
			}
			flushUser();
		}
	}
	
	/**
	 * ��ѯ�ķ�ʽ��
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
	
	@Override
	public void run() {
		btnFlash.setEnabled(false);
		//RowSorter sorter = table.getRowSorter();
		if(Server.isFileWay){
			table.setAutoCreateRowSorter(false);
			model.setRowCount(0);
			UserDAOByFile userDAO = new UserDAOByFile();
			Vector<User> users = userDAO.findAll();
			int totalNum = 0;
			int onlineNum = 0;
			int hiddenNum = 0;
			int departureNum = 0;
			int busyNum = 0;
			int offlineNum = 0;
			for(User user:users){
				if(user!=null){
					totalNum++;
					if(user.getState()==UserState.ONLINESTATE.getState())
						onlineNum++;
					if(user.getState()==UserState.DEPARTURESTATE.getState())
						departureNum++;
					if(user.getState()==UserState.BUSYSTATE.getState())
						busyNum++;
					if(user.getState()==UserState.HIDDENSTATE.getState())
						hiddenNum++;
					if(user.getState()==UserState.OFFLIENSTATE.getState())
						offlineNum++;
				}
				addUserToTable(user);
			}
			table.setAutoCreateRowSorter(true);
			lblInfo.setText("����JQ�û�:"+totalNum+"λ.����," +
					"�����û�:"+onlineNum+"λ." +
					"�����û�:"+hiddenNum+"λ." +
					"�뿪�û�:"+departureNum+"λ." +
					"��æ�û�:"+busyNum+"λ." +
					"�����û�:"+offlineNum+"λ.");
		}else
			lblInfo.setText("ע��:ϵͳ�ݲ�֧�����ݿⷽʽ!��ʹ���ļ���ʽ��������!");
		btnFlash.setEnabled(true);
	}
	
	/**
	 * ˢ�µ�ǰ�����û���
	 */
	public void flushUser(){
		new Thread(this).start();
	}
	
	/**
	 * ����ѯ��ʽ����ѯ���Ͳ����û���
	 * @param query ��ѯ����
	 * @param type ��ѯ��ʽ��0:��JQ�����ѯ��1�����ǳƲ�ѯ
	 */
	private void QueryUser(String query,int type){
		if(query.equals(""))
			return;
		//��jq�Ų�ѯ
		UserDAOByFile userDAO = new UserDAOByFile();
		model.setRowCount(0);
		if(type==0){
			try {
				int jqnum = Integer.parseInt(query);
					User user = userDAO.findById(jqnum);
					if(user!=null)
						addUserToTable(user);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "��ȷ���������ȷ��JQ����!");
				return;
			}
			return;
		}
		//���ǳƺŲ�ѯ
		if(type==1){
				Vector<User> users= userDAO.findUserByName(query);
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
	 * ���Ƶ�DefaultTableModel��
	 */
	private class MyDefaultTableModel extends DefaultTableModel{
		
		public boolean isCellEditable(int row, int column) {
			super.isCellEditable(row, column);
			return false;
		}
	}
	
	/**
	 * ����ƶ��������¼���
	 */
	private class TableMouseAdapter extends MouseAdapter{
		public void mouseEntered(MouseEvent e) {

		}
		public void mouseClicked(MouseEvent e) {
			if(e.getButton()==MouseEvent.BUTTON3){
				popupMenu.show(table,e.getX(),e.getY());
			}
		}
		
		public void mouseMoved(MouseEvent e) {
			if(e.getSource()==table){
				int row = table.rowAtPoint(e.getPoint());
				table.changeSelection(row, 0, false, true);
			}
		}
	}
}
