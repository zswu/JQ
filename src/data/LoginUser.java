/**
  * @(#)data.LoginUser.java  2008-9-4  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: ��½�û��ࡣ
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-9-4 	С��     		�½�
  **/
package data;

import java.io.Serializable;

 /**
 * ��½�û��ࡣ
 */
public class LoginUser implements Serializable {

	private Integer jqnum;
	private String password;
	private Integer state;
	public Integer getJqnum() {
		return jqnum;
	}
	public void setJqnum(Integer jqnum) {
		this.jqnum = jqnum;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
}
