/**
  * @(#)data.JQMessage.java  2008-9-4  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: 客户端和服务端的消息类。
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-9-4 	小猪     		新建
  **/
package data;

import java.io.Serializable;

 /**
 * 客户端和服务端的消息类。
 * 2008-9-4
 * @author		达内科技[Tarena Training Group]
 * @version	1.0
 * @since		JDK1.6(建议) 
 */
public class JQMessage implements Serializable {

	private Integer type;
	private Object obj;
	
	public JQMessage() {
		
	}
	public JQMessage(Integer type,Object obj) {
		this.type = type;
		this.obj = obj;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}
