package com.keli.hfbus.hessianserver;

import java.util.Map;

public interface JgywService {
	/**
	 * 机动车综合查询
	 * @param hostname 主机名称
	 * @param key 服务密钥
	 * @param hpzl 号牌种类
	 * @param hphm 号牌号码
	 * @param clsbdh 车辆识别代号后六位
	 * @param clqk 处理情况 "3"为未处理,"2"为已处理
	 * @param jkqk 缴款情况  "3"为未缴款,"2"为已缴款
	 * @param selectPage 翻页
	 * @return Map
	 */
	public Map jdczhcxFun(String hostname,String key,String hpzl,String hphm,String clsbdh,String clqk,String jkqk,int selectPage);
	/**
	 * 驾驶证综合查询
	 * @param hostname 主机名称
	 * @param key 服务密钥
	 * @param sfzmhm 身份证件号码
	 * @param dabh 档案编号后12位
	 * @param jkqk 缴款情况  "3"为未缴款,"2"为已缴款
	 * @param selectPage 翻页
	 * @return Map
	 */
	public Map jszzhcxFun(String hostname,String key,String sfzmhm,String dabh,String jkqk,int selectPage);

}
