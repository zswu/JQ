/**
  * @(#)dao.RecordDAOByFile.java  2008-9-2  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: 记录操作类。
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-9-2 	小猪     		新建
  **/
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

import data.Record;

 /**
 * 记录操作类，以文件的方式处理Record。
 */
public class RecordDAOByFile implements DAO<Record, Integer> {

	private String path = "logs";
	private String suffixName = ".dat";
	
	private String pathAdmin = "records";
	
	/**
	 * 添加一条新记录。
	 * @param record 记录。
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @return 添加成功否。
	 */
	public boolean add(Record record) throws FileNotFoundException, IOException{
		File category = new File(record.getToid()+File.separator+path);
		if(!category.exists())
			category.mkdirs();
		File file = new File(record.getToid()+File.separator+path+File.separator+record.getFromid()+suffixName);
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		oos.writeObject(record);
		oos.flush();
		oos.close();
		oos = null;
		return true;
	}

	/** 
	 * 管理员添加一条新记录。
	 * @param record 记录。
	 * @return 返回添加成功否。
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public boolean addRecordForAdmin(Record record) throws FileNotFoundException, IOException{
		File category = new File(pathAdmin);
		if(!category.exists())
			category.mkdir();
		File file = new File(pathAdmin+File.separator+record.getToid()+suffixName);
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		oos.writeObject(record);
		oos.flush();
		oos.close();
		oos = null;
		return true;
	}
	
	/**
	 * 删除一条记录。
	 * @param record 要删除的记录。
	 * @return 返回删除成功否。
	 */
	public boolean delete(Record record) throws Exception {
		File file = new File(record.getToid()+File.separator+path+File.separator+record.getFromid()+suffixName);
		if(file.exists())
			return file.delete();
		else
			return false;
	}

	
	/**
	 * 查找所有记录。
	 * @throws Exception 
	 * @return 无论如何都返回空。未作处理。
	 */
	public Vector<Record> findAll() throws Exception {
		return null;
	}

	/**
	 * 查找好友发送的留言记录。
	 * @param jqnum 该用户的jqmum
	 * @return 返回该用户的留言记录
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Vector<Record> findLeaveRecord(int jqnum) throws FileNotFoundException, IOException{
		File file = new File(pathAdmin+File.separator+jqnum+suffixName);
		if(!file.exists())
			return null;
		else{
			
				Vector<Record> v = new Vector<Record>();
				ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
				while(true){
					Object obj = null;
					try {
						obj = ois.readObject();
					} catch (IOException e) {
						break;
					} catch (ClassNotFoundException e) {
						break;
					}
					if(obj==null)
						break;
					if(obj instanceof Record){
						Record record = (Record)obj;
						record.setRead(true);
						record.setReadTime(new Date());
						v.add(record);
					}
				}
				ois.close();
				ois = null;
				return v;
		}	
	}
	
	/**
	 * 删除管理员的记录。
	 * @param jqnum
	 * @return 删除成功否。
	 */
	public boolean deleteRecordForAdmin(int jqnum){
		File file = new File(pathAdmin+File.separator+jqnum+suffixName);
		if(file.exists())
			return file.delete();
		else
			return false;
	}
	
	/**
	 * 按id查找记录
	 * @param id 记录的id
	 * @throws Exception 
	 * @return 无论如何都返回空。未作处理。
	 */
	public Record findById(Integer id) throws Exception {
		
		return null;
	}

	/**
	 * 更新记录。
	 * @param record 欲跟新的记录。
	 * @throws Exception
	 * @return 无论如何都返回false，未作处理。
	 */
	public boolean update(Record record) throws Exception {
		
		return false;
	}

}
