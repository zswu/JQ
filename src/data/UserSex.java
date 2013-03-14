/**
  * @(#)data.UserSex.java  2008-8-30  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: �Ա��ࡣ
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-8-30 	С��     		�½�
  **/
package data;

 /**
 * �Ա��ࡣ 
 */
public class UserSex {

	private int type;
	private String name;
	
	public UserSex(int type,String name) {
		this.type = type;
		this.name = name;
	}
	
	public static UserSex Boy = new UserSex(0,"male");
	public static UserSex Girl = new UserSex(1,"female");
	
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}
	
	/**
	 * �����Ա����ͣ��õ��Ա�
	 * @param type ���͡�
	 * @return �Ա�
	 */
	public static String getSex(int type){
		return type==Boy.type?Boy.name:Girl.name;
	}
}
