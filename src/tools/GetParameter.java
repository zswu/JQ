/**
  * @(#)tools.GetParameter.java  2008-9-2  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: ��ȡ������
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-9-2 	С��     		�½�
  **/
package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

 /**
 * ��ȡ������
 */
public class GetParameter {

	/**
	 * �����ļ������ơ�
	 */
	public static String[] keys = {"Port",
									"MinDigit",
									"MaxDigit",
									"ShieldJQ",
									"IsBak",
									"DeleteDays",
									"DataWay",
									"Driver",
									"LinkParameter",
									"UserName",
									"Password",
									"DBName",
									"Charset"};
	/**
	 * �����ļ���ȱʡֵ��
	 */
	public static String[] values = {"3608",
										"5",
										"9",
										"10000;88888",
										"1",
										"5",
										"0",
										"com.mysql.jdbc.Driver",
										"jdbc:mysql://127.0.0.1:3306/",
										"root",
										"123",
										"javaqq",
										"gb2312"};
	/**
	 * ���������ļ������ơ�
	 */
	private static String path = "server.properties";
	
	/**
	 * ��������ļ���
	 * @return ���������ļ���Properties����
	 * @throws Exception ���������ļ�����ʱ�׳����쳣��
	 */
	public static Properties getProp() throws Exception{
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File(path)));
		} catch (FileNotFoundException e) {
			setDefault();
			prop.load(new FileInputStream(new File(path)));
			//throw new Exception("���������ļ�ʱ��������:"+e.getMessage());
		} catch (IOException e) {
			setDefault();
			prop.load(new FileInputStream(new File(path)));
			//throw new Exception("���������ļ�ʱ��������:"+e.getMessage());
		}
		return prop;
	}
	
	/**
	 * ���������ļ���
	 * @param keys ���Լ�
	 * @param values ����ֵ
	 * @return ���ر���ɹ���
	 * @throws IOException 
	 */
	public static boolean saveProp(String[] keys,String[] values) throws IOException{
		if(keys.length==values.length){
			Properties prop = new Properties();
			FileOutputStream fos = new FileOutputStream(new File(path));
			for(int i=0;i<keys.length;i++)
				prop.setProperty(keys[i], values[i]);
			prop.store(fos, "slandi@hk");
			fos.flush();
			fos.close();
		}else
			return false;
		return true;
	}
	
	/**
	 * �ظ�ȱʡֵ
	 * @return ���ػظ�ȱʡֵ�ɹ���
	 * @throws IOException
	 */
	public static boolean setDefault() throws IOException{
		return saveProp(keys, values);
	}
	
	/**
	 * ȡ��ȱʡֵ�������ļ���
	 * @return ����Properties����
	 * @throws IOException
	 */
	public static Properties getDefault() throws IOException{
		Properties prop = new Properties();
		for(int i=0;i<keys.length;i++)
			prop.setProperty(keys[i], values[i]);
		return prop;
	}
}
