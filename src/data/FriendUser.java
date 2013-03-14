
package data;

import java.io.Serializable;

 /**
 * ∫√”—¿‡°£ 
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
