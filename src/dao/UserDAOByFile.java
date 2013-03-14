
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
 * 用户操作类，以文件的方式处理User.
 */
public class UserDAOByFile implements DAO<User,Integer> {

	private String path = "users";
	private String suffixName = ".dat";
	
	/**
	 * 添加新用户。
	 * @param user User用户对象。
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public boolean add(User user) throws FileNotFoundException, IOException {
		File category = new File(path);
		if(!category.exists())
			category.mkdir();
		File file = new File(path+File.separator+user.getJqnum()+suffixName);
		//本来异常通常是抛出外面，让调用者来处理，此处为了省事就自作主张处理了
		//还是不省事把~~
		if(file.exists())
			//file.delete();
			return false;//防止意外的产生，这个也是防止该用户被注册方法~~
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file,false)));
		oos.writeObject(user);
		oos.flush();
		oos.close();
		oos = null;
		return true;
	}
	
	/**
	 * 添加系统管理员
	 * @param jqnum 管理员的jq号码
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
	 * 删除用户
	 * @param user User用户对象。
	 */
	public boolean delete(User user){
		File file = new File(path+File.separator+user.getJqnum()+suffixName);
		if(file.exists())
			return file.delete();
		else
			return false;	
	}

	/**
	 * 查找所有用户。
	 * @return 返回保存User对象的Vector
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
	 * 按id查找用户。
	 * @param id 用户的id。
	 * @return 返回User用户对象。
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
				System.out.println("读取用户["+id+"]信息时错误:"+e.getMessage());
				return null;
			} catch (IOException e) {
				System.out.println("读取用户["+id+"]信息时错误:"+e.getMessage());
				return null;
			} catch (ClassNotFoundException e) {
				System.out.println("读取用户["+id+"]信息时错误:"+e.getMessage());
				return null;
			}
		}else
			return null;
	}
	
	/**
	 * 按昵称查找用户。
	 * @param NickName 用户昵称。
	 * @return 返回保存Use的Vector对象。
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
	 * 按昵称查找当前在线的用户。
	 * @param NickName 昵称。
	 * @return 返回保存User的Vector对象。
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
	 * 更新用户。
	 * @param user User用户对象。
	 * @return 更新成功否。
	 */
	public boolean update(User user) throws FileNotFoundException, IOException{
		if(delete(user))
			return add(user);
		else
			return false;
	}
	
	/**
	 * 查找当前所有在线用户。
	 * @return 返回保存在线User的Vector对象。
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
	 * 返回当前在线，且状态为在线、离开、繁忙的用户。
	 * @return 返回保存在线User的Vector对象。
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
