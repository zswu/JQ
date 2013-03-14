
package dao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Vector;

import tools.FileDeal;

import data.User;
import data.UserState;

 /**
 * �û������࣬���ļ��ķ�ʽ����User.
 */
public class UserDAOByFile implements DAO<User,Integer> {

	private String path = "users";
	private String suffixName = ".dat";
	
	/**
	 * ������û���
	 * @param user User�û�����
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public boolean add(User user) throws FileNotFoundException, IOException {
		File category = new File(path);
		if(!category.exists())
			category.mkdir();
		File file = new File(path+File.separator+user.getJqnum()+suffixName);
		//�����쳣ͨ�����׳����棬�õ������������˴�Ϊ��ʡ�¾��������Ŵ�����
		//���ǲ�ʡ�°�~~
		if(file.exists())
			//file.delete();
			return false;//��ֹ����Ĳ��������Ҳ�Ƿ�ֹ���û���ע�᷽��~~
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file,false)));
		oos.writeObject(user);
		oos.flush();
		oos.close();
		oos = null;
		return true;
	}
	
	/**
	 * ���ϵͳ����Ա
	 * @param jqnum ����Ա��jq����
	 */
	public void addSysUser(Integer jqnum){
		User user = new User();
		user.setId(1);
		user.setJqnum(jqnum);
		user.setNickname("admin");
		user.setRealname("admin");
		user.setPassword("admin");
		user.setEmail("wmaoyuting@gmail.com");
		user.setAge(21);
		user.setRegisterTime(new Date());
		try {
			add(user);
		} catch (FileNotFoundException e) {
			System.out.println("Error:"+e.getMessage());
		} catch (IOException e) {
			System.out.println("Error:"+e.getMessage());
		}
	}

	/**
	 * ɾ���û�
	 * @param user User�û�����
	 */
	public boolean delete(User user){
		File file = new File(path+File.separator+user.getJqnum()+suffixName);
		if(file.exists())
			return file.delete();
		else
			return false;	
	}

	/**
	 * ���������û���
	 * @return ���ر���User�����Vector
	 */
	public Vector<User> findAll(){
		Vector<User> v = new Vector<User>();
		File filePath = new File(path+File.separator);
		File[] fileUsers = filePath.listFiles();
		for(File file:fileUsers){
			try {
				if(FileDeal.isKindOFType(file, suffixName)){
					ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
					Object obj = ois.readObject();
					if(obj instanceof User){
						User user = (User)obj;
						v.add(user);
					}
					ois.close();
					ois = null;
				}
			} catch (FileNotFoundException e) {
				System.out.println("Error:"+e.getMessage());
			} catch (IOException e) {
				System.out.println("Error:"+e.getMessage());
			} catch (ClassNotFoundException e) {
				System.out.println("Error:"+e.getMessage());
			}
		}
		return v;
	}

	/**
	 * ��id�����û���
	 * @param id �û���id��
	 * @return ����User�û�����
	 */
	public User findById(Integer id){
		File file = new File(path+File.separator+id+suffixName);
		if(file.exists()){
			try {
				ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
				Object obj = ois.readObject();
				ois.close();
				ois = null;
				if(obj instanceof User)
					return (User)obj;
				else
					return null;
			} catch (FileNotFoundException e) {
				System.out.println("��ȡ�û�["+id+"]��Ϣʱ����:"+e.getMessage());
				return null;
			} catch (IOException e) {
				System.out.println("��ȡ�û�["+id+"]��Ϣʱ����:"+e.getMessage());
				return null;
			} catch (ClassNotFoundException e) {
				System.out.println("��ȡ�û�["+id+"]��Ϣʱ����:"+e.getMessage());
				return null;
			}
		}else
			return null;
	}
	
	/**
	 * ���ǳƲ����û���
	 * @param NickName �û��ǳơ�
	 * @return ���ر���Use��Vector����
	 */
	public Vector<User> findUserByName(String NickName){
		Vector<User> allUser = findAll();
		Vector<User> users = new Vector<User>();
		for(User user:allUser){
			if(user.getNickname().indexOf(NickName)!=-1)
				users.add(user);
		}
		return users;
	}
	
	/**
	 * ���ǳƲ��ҵ�ǰ���ߵ��û���
	 * @param NickName �ǳơ�
	 * @return ���ر���User��Vector����
	 */
	public Vector<User> findOnlineUserByName(String NickName){
		Vector<User> allUser = findAll();
		Vector<User> users = new Vector<User>();
		for(User user:allUser){
			if((user.getState()==UserState.ONLINESTATE.getState() || 
					user.getState()==UserState.BUSYSTATE.getState() ||
					user.getState()==UserState.DEPARTURESTATE.getState() ||
					user.getState()==UserState.HIDDENSTATE.getState()) && 
					user.getNickname().indexOf(NickName)!=-1)
				users.add(user);
		}
		return users;
	}

	/**
	 * �����û���
	 * @param user User�û�����
	 * @return ���³ɹ���
	 */
	public boolean update(User user) throws FileNotFoundException, IOException{
		if(delete(user))
			return add(user);
		else
			return false;
	}
	
	/**
	 * ���ҵ�ǰ���������û���
	 * @return ���ر�������User��Vector����
	 * @throws FileNotFoundException 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Vector<User> findOnlineAll() throws FileNotFoundException, IOException, ClassNotFoundException{
		Vector<User> v = new Vector<User>();
		File filePath = new File(path+File.separator);
		File[] fileUsers = filePath.listFiles();
		for(File file:fileUsers){
			if(FileDeal.isKindOFType(file, suffixName)){
				ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
				Object obj = ois.readObject();
				if(obj instanceof User){
					User user = (User)obj;
					if(user.getState()==UserState.ONLINESTATE.getState() || 
							user.getState()==UserState.BUSYSTATE.getState() ||
							user.getState()==UserState.DEPARTURESTATE.getState() ||
							user.getState()==UserState.HIDDENSTATE.getState())
					v.add(user);
				}
			}
		}
		return v;
	}
	
	/**
	 * ���ص�ǰ���ߣ���״̬Ϊ���ߡ��뿪����æ���û���
	 * @return ���ر�������User��Vector����
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Vector<User> findOnline() throws FileNotFoundException, IOException, ClassNotFoundException{
		Vector<User> v = new Vector<User>();
		File filePath = new File(path+File.separator);
		File[] fileUsers = filePath.listFiles();
		for(File file:fileUsers){
			if(FileDeal.isKindOFType(file, suffixName)){
				ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
				Object obj = ois.readObject();
				if(obj instanceof User){
					User user = (User)obj;
					if(user.getState()==UserState.ONLINESTATE.getState() || 
							user.getState()==UserState.BUSYSTATE.getState() ||
							user.getState()==UserState.DEPARTURESTATE.getState())
					v.add(user);
				}
				ois.close();
				ois = null;
			}
		}
		return v;
	}
}
