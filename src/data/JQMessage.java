/**
  * @(#)data.JQMessage.java  2008-9-4  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: �ͻ��˺ͷ���˵���Ϣ�ࡣ
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-9-4 	С��     		�½�
  **/
package data;

import java.io.Serializable;

 /**
 * �ͻ��˺ͷ���˵���Ϣ�ࡣ
 * 2008-9-4
 * @author		���ڿƼ�[Tarena Training Group]
 * @version	1.0
 * @since		JDK1.6(����) 
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
