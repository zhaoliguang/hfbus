package com.keli.hfbus.hessianserver;

import java.util.Map;

public interface JgywService {
	/**
	 * �������ۺϲ�ѯ
	 * @param hostname ��������
	 * @param key ������Կ
	 * @param hpzl ��������
	 * @param hphm ���ƺ���
	 * @param clsbdh ����ʶ����ź���λ
	 * @param clqk ������� "3"Ϊδ����,"2"Ϊ�Ѵ���
	 * @param jkqk �ɿ����  "3"Ϊδ�ɿ�,"2"Ϊ�ѽɿ�
	 * @param selectPage ��ҳ
	 * @return Map
	 */
	public Map jdczhcxFun(String hostname,String key,String hpzl,String hphm,String clsbdh,String clqk,String jkqk,int selectPage);
	/**
	 * ��ʻ֤�ۺϲ�ѯ
	 * @param hostname ��������
	 * @param key ������Կ
	 * @param sfzmhm ���֤������
	 * @param dabh ������ź�12λ
	 * @param jkqk �ɿ����  "3"Ϊδ�ɿ�,"2"Ϊ�ѽɿ�
	 * @param selectPage ��ҳ
	 * @return Map
	 */
	public Map jszzhcxFun(String hostname,String key,String sfzmhm,String dabh,String jkqk,int selectPage);

}
