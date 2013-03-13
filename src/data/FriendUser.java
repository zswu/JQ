/**
  * @(#)data.FriendUser.java  2008-9-5  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: 好友类。用户登陆后，发送好友的信息到客户端。
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-9-5 	小猪     		新建
  **/
package data;

import java.io.Serializable;

 /**
 * 好友类。用户登陆后，发送好友的信息到客户端。
 * 2008-9-5
 * @author		达内科技[Tarena Training Group]
 * @version	1.0
 * @since		JDK1.6(建议) 
 */
public class FriendUser implements Serializable {

	private Integer jqnum;
	private String nickName;
	private String signature;
	private Integer photo;
	private Integer state;
	public Integer getJqnum() {
		return jqnum;
	}
	public void setJqnum(Integer jqnum) {
		this.jqnum = jqnum;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Integer getPhoto() {
		return photo;
	}
	public void setPhoto(Integer photo) {
		this.photo = photo;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
	public String toString() {
		return nickName;
	}
	
	public boolean equals(Object obj) {
		//System.out.println("come bijiao");
		if(obj instanceof FriendUser){
			FriendUser user = (FriendUser)obj;
			return user.getJqnum().equals(jqnum);
		}else
			return false;
	}
}
