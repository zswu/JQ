/**
  * @(#)data.Log.java  2008-9-2  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: 日志类。
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-9-2 	小猪     		新建
  **/
package data;

import java.io.Serializable;
import java.util.Date;

import tools.DateDeal;

 /**
 * 日志类。
 */
public class Log implements Serializable {

	private Integer id = 0;
	private Integer type;
	private Integer userid;
	private String nickname;
	private String what;
	private String ip;
	private Date time;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getWhat() {
		return what;
	}
	public void setWhat(String what) {
		this.what = what;
	}
	
	public String toString() {
		return "time:"+DateDeal.getCurrentTime()+",user:"+nickname+"["+userid+"],IP:"+ip+",operation:"+what+"\n";
	}
}
