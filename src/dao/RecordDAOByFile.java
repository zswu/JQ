/**
  * @(#)dao.RecordDAOByFile.java  2008-9-2  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: ��¼�����ࡣ
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-9-2 	С��     		�½�
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
 * ��¼�����࣬���ļ��ķ�ʽ����Record��
 */
public class RecordDAOByFile implements DAO<Record, Integer> {

	private String path = "logs";
	private String suffixName = ".dat";
	
	private String pathAdmin = "records";
	
	/**
	 * ���һ���¼�¼��
	 * @param record ��¼��
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @return ��ӳɹ���
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
	 * ����Ա���һ���¼�¼��
	 * @param record ��¼��
	 * @return ������ӳɹ���
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
	 * ɾ��һ����¼��
	 * @param record Ҫɾ���ļ�¼��
	 * @return ����ɾ���ɹ���
	 */
	public boolean delete(Record record) throws Exception {
		File file = new File(record.getToid()+File.separator+path+File.separator+record.getFromid()+suffixName);
		if(file.exists())
			return file.delete();
		else
			return false;
	}

	
	/**
	 * �������м�¼��
	 * @throws Exception 
	 * @return ������ζ����ؿա�δ������
	 */
	public Vector<Record> findAll() throws Exception {
		return null;
	}

	/**
	 * ���Һ��ѷ��͵����Լ�¼��
	 * @param jqnum ���û���jqmum
	 * @return ���ظ��û������Լ�¼
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
	 * ɾ������Ա�ļ�¼��
	 * @param jqnum
	 * @return ɾ���ɹ���
	 */
	public boolean deleteRecordForAdmin(int jqnum){
		File file = new File(pathAdmin+File.separator+jqnum+suffixName);
		if(file.exists())
			return file.delete();
		else
			return false;
	}
	
	/**
	 * ��id���Ҽ�¼
	 * @param id ��¼��id
	 * @throws Exception 
	 * @return ������ζ����ؿա�δ������
	 */
	public Record findById(Integer id) throws Exception {
		
		return null;
	}

	/**
	 * ���¼�¼��
	 * @param record �����µļ�¼��
	 * @throws Exception
	 * @return ������ζ�����false��δ������
	 */
	public boolean update(Record record) throws Exception {
		
		return false;
	}

}
