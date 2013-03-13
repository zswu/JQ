/**
  * @(#)server.frm.panes.ServecieProcessBar.java  2008-8-28  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: ���ع������ࡣ
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2008-8-28 	С��     		�½�
  **/
package server.frm.panes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

 /**
 * ���ع������ࡣ
 * 2008-8-28
 * @author		���ڿƼ�[Tarena Training Group]
 * @version	1.0
 * @since		JDK1.6(����) 
 */
public class ServecieProcessBar extends JLabel implements ActionListener{

	private Timer timer = null;
	
	private boolean isPaintLeft = true;
	
	/** ������εĿ�� */
	private int width = 0;
	/** ������εĸ߶� */
	private int height = 0;
	/** ������ε�ˮƽλ�� */
	private int nowX = 0;
	/** ������еĴ�ֱλ�� */
	private int nowY = 0;
	/** �Ƿ��������ұ��ƶ� */
	private boolean isLeft = true;
	/** �Ƿ�ʼ���� */
	private boolean isBeginRoll = false;
	
	public ServecieProcessBar(int width,int height) {
		setBackground(Color.WHITE);
		timer = new Timer(16,this);
		
		this.width = width*3/4;
		this.height = height;
		setSize(width,height);
		setPreferredSize(new Dimension(width,height));
		
		//System.out.println(getWidth()+":"+getHeight());
	}
	
	public void actionPerformed(ActionEvent e) {
		if(isLeft){
			if(nowX<=getWidth())
				nowX+=3;
			else
				isLeft = false;
		}else{
			if(nowX>=-1*width)
				nowX-=3;
			else
				isLeft = true;
		}
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		//super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		Color c = g2.getColor();
		if(isBeginRoll){
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, getWidth(), getHeight());
			
			if(isLeft)
				g2.setPaint(new GradientPaint(nowX,0,Color.WHITE,nowX+width,0,new Color(110,132,224,200)));
			else
				g2.setPaint(new GradientPaint(nowX,0,new Color(110,132,224,200),nowX+width,0,Color.WHITE));
			g2.fillRect(nowX, nowY, width, height);
			g2.setColor(Color.BLACK);
			g2.drawString("JavaQQ����������...", (getWidth()-50)/2, (getHeight()+4)/2);
			g2.setColor(Color.WHITE);
			if(isLeft)
				g2.fillRect(0, 0, 3, getHeight());
			else
				g2.fillRect(getWidth()-3, 0, 3, getHeight());
			
		}else{
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, getWidth(), getHeight());
		}
		g2.setColor(c);
	}
	
	/**
	 * ��ʼ����������
	 */
	public void startRoll(){
		nowX = -1*this.width;
		nowY = 0;
		isBeginRoll = true;
		timer.start();
	}
	
	/**
	 * ֹͣ������������
	 */
	public void stopRoll(){
		isBeginRoll = false;
		timer.stop();
		repaint();
	}
}
