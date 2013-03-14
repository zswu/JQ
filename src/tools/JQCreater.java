
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
 * JQ号生成器类。
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
	 * 返回保存JQ号码的文件。
	 * @return 返回保存JQ号码的文件。
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
	 * 产生JQ号码
	 * @return 返回产生jqnum
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Integer createJQ() throws FileNotFoundException, IOException{
		String s = "";
		int num = 0;
		while(true){
			int n = RandomUtil.randomInt(minDigit, maxDigit);
			//System.out.println("位数:"+n);
			for(int i=0;i<n;i++)
				s += part[RandomUtil.randomInt(part.length)];
			num = Integer.parseInt(s);
			s = num+"";
			//System.out.println("生成的号码"+s);
			if(s.length()>=minDigit && isAllowed(num) && isAllowReged(num))
				break;
		}
		return num;
	}
	
	/**
	 * JQ号码是否为屏蔽的JQ号码。
	 * @param num JQ号码
	 * @return 返回是否为屏蔽的JQ号码。
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
	 * 返回允许注册否
	 * @param num JQ号码
	 * @return 返回允许注册否
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
	 * 产生User用户ID
	 * @return 返回id
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
	 * 保存用户的id和JQ号码
	 * @param id 用户id
	 * @param num JQ号码
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
