
package data;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

 /**
 * JavaQQ�û��ࡣ
 */
public class User implements Serializable {

	private Integer id;
	private Integer jqnum;
	private String realname;
	private String nickname;
	private Integer sex = 0;
	private Integer age = 0;
	private String password;
	private String signature = "......";
	private String email;
	private Integer photo = 1;
	private Integer state = 4;
	private Vector<Integer> listFriend = new Vector<Integer>();
	private Date registerTime;
	
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getJqnum() {
		return jqnum;
	}
	public void setJqnum(Integer jqnum) {
		this.jqnum = jqnum;
	}
	public Vector getListFriend() {
		return listFriend;
	}
	public void setListFriend(Vector listFriend) {
		this.listFriend = listFriend;
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
	public Integer getPhoto() {
		return photo;
	}
	public void setPhoto(Integer photo) {
		this.photo = photo;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public Date getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
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
}
