
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
