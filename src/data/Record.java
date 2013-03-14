/**
  * @(#)data.Record.java  2008-9-2  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: 聊天记录类。
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-9-2 	小猪     		新建
  **/
package data;

import java.io.Serializable;
import java.util.Date;

 /**
 * 聊天记录类。
 */
public class Record implements Serializable {

	private Integer id = 1;
	private Integer fromid = 10000;
	private Integer toid = 10000;
	private Date sendTime = new Date();
	private Date readTime = new Date();
	private boolean isRead = false;
	private String content = "";
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getFromid() {
		return fromid;
	}
	public void setFromid(Integer fromid) {
		this.fromid = fromid;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	public Date getReadTime() {
		return readTime;
	}
	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public Integer getToid() {
		return toid;
	}
	public void setToid(Integer toid) {
		this.toid = toid;
	}
	
	public String toString() {
		return id+"="+isRead+"="+readTime+sendTime+toid+fromid+content;
	}
}
