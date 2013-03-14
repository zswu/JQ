
package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.StringTokenizer;

import server.frm.Server;

 /**
 * JQ���������ࡣ
 */
public class JQCreater {

	private String[] part = {"0","1","2","3","4","5","6","7","8","9","0"};
	private int minDigit;
	private int maxDigit;
	private String notAllowed;
	
	private String path = "jid.dat";
	
	public JQCreater(){
		minDigit = Integer.parseInt(Server.prop.getProperty(GetParameter.keys[1]));
		maxDigit = Integer.parseInt(Server.prop.getProperty(GetParameter.keys[2]));
		notAllowed = Server.prop.getProperty(GetParameter.keys[3]);
		
	}
	
	/**
	 * ���ر���JQ������ļ���
	 * @return ���ر���JQ������ļ���
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private RandomAccessFile getFile() throws FileNotFoundException,IOException{
		RandomAccessFile raf = null;
		File file = new File(path);
		boolean isWrite = false;
		if(!file.exists()){
			file.createNewFile();
			isWrite = true;
		}
		raf = new RandomAccessFile(file,"rw");
		if(isWrite)
			raf.writeUTF("0\n");
		return raf;
	}
	
	/**
	 * ����JQ����
	 * @return ���ز���jqnum
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Integer createJQ() throws FileNotFoundException, IOException{
		String s = "";
		int num = 0;
		while(true){
			int n = RandomUtil.randomInt(minDigit, maxDigit);
			//System.out.println("λ��:"+n);
			for(int i=0;i<n;i++)
				s += part[RandomUtil.randomInt(part.length)];
			num = Integer.parseInt(s);
			s = num+"";
			//System.out.println("���ɵĺ���"+s);
			if(s.length()>=minDigit && isAllowed(num) && isAllowReged(num))
				break;
		}
		return num;
	}
	
	/**
	 * JQ�����Ƿ�Ϊ���ε�JQ���롣
	 * @param num JQ����
	 * @return �����Ƿ�Ϊ���ε�JQ���롣
	 */
	private boolean isAllowed(Integer num){
		StringTokenizer tokenizer = new StringTokenizer(notAllowed,";");
		while(tokenizer.hasMoreTokens()){
			Integer shieldjq = Integer.parseInt(tokenizer.nextToken());
			if(num==shieldjq)
				return false;
		}
		return true;
	}
	
	/**
	 * ��������ע���
	 * @param num JQ����
	 * @return ��������ע���
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private boolean isAllowReged(Integer num) throws FileNotFoundException, IOException{
		RandomAccessFile raf = getFile();
		String s;
		while((s=raf.readLine())!=null){
			if(s.indexOf(num+"")!=-1)
				return false;
		}
		return true;
	}
	
	/**
	 * ����User�û�ID
	 * @return ����id
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Integer createID() throws FileNotFoundException, IOException{
		int id = -1;
		RandomAccessFile raf = getFile();
		String s = raf.readLine();
		
		if(s!=null){
			s = s.trim();
			id = Integer.parseInt(s)+1;
		}
			
		raf.close();
		return id;
	}
	
	/**
	 * �����û���id��JQ����
	 * @param id �û�id
	 * @param num JQ����
	 * @throws IOException
	 */
	public void saveIDJQ(int id,int num) throws IOException{
		RandomAccessFile raf = getFile();
		raf.writeUTF(id+":"+num+"\n");
		raf.close();
		raf = getFile();
		raf.seek(0);
		raf.writeUTF(id+"\n");
		raf.close();
	}
	
}
