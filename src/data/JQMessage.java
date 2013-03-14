
package data;

import java.io.Serializable;

 /**
 * �ͻ��˺ͷ���˵���Ϣ�ࡣ 
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
