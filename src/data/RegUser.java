/**
  * @(#)data.RegUser.java  2008-9-4  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: 此处输入简单类说明
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-9-4 	小猪     		新建
  **/
package data;

import java.io.Serializable;


public class RegUser implements Serializable {
	private Integer jqnum;
	private String realname;
	private String nickname;
	private String password;
	public Integer getJqnum() {
		return jqnum;
	}
	public void setJqnum(Integer jqnum) {
		this.jqnum = jqnum;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	
	
}
