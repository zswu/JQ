package tools;

import java.util.Random;

/**
 * ��ѧ��-�����������
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
