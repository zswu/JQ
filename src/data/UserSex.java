
package data;

 /**
 * 性别类。 
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
	 * 根据性别类型，得到性别。
	 * @param type 类型。
	 * @return 性别。
	 */
	public static String getSex(int type){
		return type==Boy.type?Boy.name:Girl.name;
	}
}
