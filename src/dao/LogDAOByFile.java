
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
		String xlog = "time:"+DateDeal.getCurrentTime()+",user:"+log.getNickname()+"["+log.getUserid()+"],IP:"+log.getIp()+",����:"+log.getWhat()+"\n";
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
