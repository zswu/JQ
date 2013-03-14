
package data;

import java.io.Serializable;

 /**
 * µÇÂ½ÓÃ»§Àà¡£
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
