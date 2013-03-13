/**
  * @(#)data.UserState.java  2008-8-29  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: 用户状态类。
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-8-29 	小猪     		新建
  **/
package data;

import java.io.Serializable;

 /**
 * 用户状态类。
 * 2008-8-29
 * @author		达内科技[Tarena Training Group]
 * @version	1.0
 * @since		JDK1.6(建议) 
 */
public class UserState implements Serializable{

	private int state;
	private String SName;
	private Integer jqnum;
	
	public UserState() {
		
	}

	public UserState(String SName,int state) {
		this.state = state;
		this.SName = SName;
	}
	
	public static UserState ONLINESTATE = new UserState("在线",0);
	public static UserState HIDDENSTATE = new UserState("隐身",1);
	public static UserState DEPARTURESTATE = new UserState("离开",2);
	public static UserState BUSYSTATE = new UserState("繁忙",3);
	public static UserState OFFLIENSTATE = new UserState("离线",4);
	
	public String toString() {
		return SName;
	}

	public int getState() {
		return state;
	}
	public Integer getJqnum() {
		return jqnum;
	}

	public void setJqnum(Integer jqnum) {
		this.jqnum = jqnum;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getSName() {
		return SName;
	}

	public void setSName(String name) {
		SName = name;
	}
	
	/**
	 * 根据用户状态返回状态信息。
	 * @param state 状态。
	 * @return 状态信息。
	 */
	public static String getStateName(int state){
		String sName = state==DEPARTURESTATE.getState()?DEPARTURESTATE.getSName():
			(state==BUSYSTATE.getState()?BUSYSTATE.getSName():
			(state==HIDDENSTATE.getState()?HIDDENSTATE.getSName():
			(state==ONLINESTATE.getState()?ONLINESTATE.getSName():
			(OFFLIENSTATE.getSName()))));
		return sName;
	}
}
