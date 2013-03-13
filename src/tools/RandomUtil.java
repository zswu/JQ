/**
  * @(#)tools.RandomUtil.java  2008.07.01  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: ��ѧ��-�����������
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2007.07.17 	С��     		�½�
  **/

package tools;

import java.util.Random;

/**
 * ��ѧ��-�����������
 * 2008-07-16
 * @author		���ڿƼ�[Tarena Training Group]
 * @version	1.0
 * @since		JDK1.6(����) 
 */
public class RandomUtil {

	/**
	 * ����a-b�������
	 * @param a ����a
	 * @param b ����b
	 * @return a-b�������
	 */
	public static int randomInt(int a,int b){
		int t,n=0;
		if(a>b)
		{
			t=a;a=b;b=t;
		}
		t=(int)(Math.ceil(Math.log10(b)));//��b����ȡ��
		while(true)
		{
			n=(int)(Math.random()*Math.pow(10,t));//�����*10��t�η�
			if(n>=a && n<=b)
				break;
		}
		//System.out.println("���ɵ����������:"+n);
		return n;
	}
	
	/**
	 * ����0-a���������
	 * @param a ����a��
	 * @return ����0-a���������
	 */
	public static int randomInt(int a){
		return new Random().nextInt(a);
	}
}
