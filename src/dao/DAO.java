
package dao;

import java.io.Serializable;
import java.util.Vector;

 /**
 * dao�࣬���������ࡣ
 */
public interface DAO<T,U> {
	/**
	 * �����������ݡ�
	 * @return ������(T)���������ݵ�Vector����
	 * @throws Exception ��������з���������׳��쳣��
	 */
	Vector<T> findAll()throws Exception;
	/**
	 * ��Ӹö���
	 * @param obj ��Ӷ���(T)��
	 * @return ��ӳɹ���
	 * @throws Exception ��������з���������׳��쳣��
	 */
	boolean add(T obj)throws Exception;
	/**
	 * ɾ���ö���
	 * @param obj ɾ���Ķ���(T)��
	 * @return ɾ���ɹ���
	 * @throws Exception ��������д�����׳��쳣��
	 */
	boolean delete(T obj)throws Exception;
	/**
	 * ���¸ö���
	 * @param obj ���µĶ���(T)�� 
	 * @return ���³ɹ���
	 * @throws Exception ��������з�������׳��쳣��
	 */
	boolean update(T obj)throws Exception;
	/**
	 * ��id���Ҹö���
	 * @param id ���ҵ�id��
	 * @return ���ز��ҵĶ���(T)��
	 * @throws Exception ��������з���������׳��쳣��
	 */
	T findById(U id)throws Exception;
}
