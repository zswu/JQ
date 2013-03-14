
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
