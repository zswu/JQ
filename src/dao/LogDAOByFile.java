/**
  * @(#)dao.LogDAOByFile.java  2008-9-2  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: �˴��������˵��
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-9-2 	С��     		�½�
  **/
package dao;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Vector;

import server.frm.Server;
import tools.DateDeal;
import tools.FileDeal;
import tools.GetParameter;

import data.Log;

 /**
 * ��־�����࣬���ļ��ķ�ʽ����Log��
 * 2008-9-2
 * @author		���ڿƼ�[Tarena Training Group]
 * @version	1.0
 * @since		JDK1.6(����) 
 * @author		Administrator
 */
public class LogDAOByFile implements DAO<Log,Integer> {

	private String path = "logs";
	private String suffixName = ".txt";
	
	/** 
	 * ���һ������־��
	 * @param log ��־��
	 * @throws IOException
	 * @return ��ӳɹ���
	 */
	public boolean add(Log log) throws IOException{
		File category = new File(path);
		if(!category.exists())
			category.mkdir();
		int delDay = Integer.parseInt(Server.prop.getProperty(GetParameter.keys[5]));
		deleteBeforeSomeDays(delDay);
		File file = new File(path+File.separator+DateDeal.getCurrentDate()+suffixName);
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file,true)));
		String xlog = "ʱ��:"+DateDeal.getCurrentTime()+",�û�:"+log.getNickname()+"["+log.getUserid()+"],IP:"+log.getIp()+",����:"+log.getWhat()+"\n";
		pw.write(xlog);
		pw.flush();
		pw.close();
		return true;
	}
	
	/**
	 * ɾ��������ǰ����־��
	 * @param n ������
	 */
	public void deleteBeforeSomeDays(int n){
		File filePath = new File(path+File.separator);
		File[] xfile = filePath.listFiles();
		for(File file:xfile){
			if(FileDeal.isKindOFType(file, suffixName))
				if(file.lastModified()-System.currentTimeMillis()>n*24*60*60*1000)
					file.delete();
		}
	}
	
	/**
	 * ɾ����־��
	 * @param log ��־��
	 * @throws Exception
	 * @return ������ζ�����false��δ������
	 */
	public boolean delete(Log log) throws Exception {
		return false;
	}
	
	/**
	 * ������־��
	 * @throws Exception
	 * @return ������ζ����ؿգ�δ������
	 */
	public Vector<Log> findAll() throws Exception {
		return null;
	}
	
	
	/**
	 * ��id������־��
	 * @param id ��־id
	 * @throws Exception
	 * @return ������ζ����ؿգ�δ������
	 */
	public Log findById(Integer id) throws Exception {
		return null;
	}
	
	/**
	 * ������־��
	 * @param log ��־��
	 * @throws Exception
	 * @return ������ζ�����false��δ������
	 */
	public boolean update(Log log) throws Exception {
		return false;
	}
}
