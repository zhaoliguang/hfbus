package com.keli.hfbus.hessianserver;

import java.util.Date;
import java.util.Map;

public interface UserManagerService {

	/**
	 * 用户登录
	 * @param hostname 主机名称
	 * @param key 服务密匙
	 * @param url 访问的服务的URL
	 * @param userName 用户账号
	 * @param passWord 密码
	 * @return Map     状态码 operaStatus String
	 * 				    		0	登录成功
								1  	登录出错
								2  	帐号或密码错误
								3  	帐号未激活
 			返回的Map中有status的键，对应的是int型的状态码  100  正常状态，101  用户名或密码错误，102  没有接口权限或接口不存在，103  用户对该接口的当日访问量用完，104  验证程序异常 稍后重试
	 */
	public Map loginUser(String hostname, String key, String url,String userName,String passWord);
	
	/**
	 * 用户注册
	 * @param hostname 主机名称
	 * @param key 服务密匙
	 * @param url 访问的服务的URL
	 * @param userName 用户账号
	 * @param phoneName 手机号
	 * @param passWord 密码
	 * @return Map       状态码 operaStatus String
	 * 								0  注册成功
									1  注册出错
									2  用户名重复
									3  手机号重复
		返回的Map中有status的键，对应的是int型的状态码  100  正常状态，101  用户名或密码错误，102  没有接口权限或接口不存在，103  用户对该接口的当日访问量用完，104  验证程序异常 稍后重试
	 */
	public Map registerUser(String hostname, String key, String url,String userName,String phoneName,String passWord);

	/**
	 * 修改密码
	 * @param hostname 主机名称
	 * @param key 服务密匙
	 * @param url 访问的服务的URL
	 * @param userName 用户账号
	 * @param oldPassWord 原始密码
	 * @param newPassWord 新密码
	 * @return Map       状态码 operaStatus String
	 * 							0  修改成功
								1  修改出错
								2  原密码错误

		返回的Map中有status的键，对应的是int型的状态码  100  正常状态，101  用户名或密码错误，102  没有接口权限或接口不存在，103  用户对该接口的当日访问量用完，104  验证程序异常 稍后重试
	 */
	public Map updatePassWord(String hostname, String key, String url,String userName,String oldPassWord,String newPassWord);

	/**
	 * 保存用户公交线路关注
	  * @param hostname 主机名称
	 * @param key 服务密匙
	 * @param url 访问的服务的URL
	 * @param userName 用户账号
	 * @param xlmc 线路名称
	 * @param fx 方向 默认0 上行；1下行
	 * @param cz 车站（上车）
	 * @param tqtx 提前N路提醒 默认0为到站提醒
	 * @param xq 星期【1-7】数组
	 * @param sj 时间 数组
	 * @param qy 是否启用 默认0 不启用；1启用
	 * @return Map       状态码 operaStatus String
	 * 							0  保存成功
								1  程序出错
								2 保存出错，公交线路关注重复
		返回的Map中有status的键，对应的是int型的状态码  100  正常状态，101  用户名或密码错误，102  没有接口权限或接口不存在，103  用户对该接口的当日访问量用完，104  验证程序异常 稍后重试
	 */
	public Map saveGjcgz(String hostname, String key, String url,String userName,String xlmc,String fx,String cz,String tqtx,String[] xq,String[] sj,String lineid,String czmc,String qy);

	/**
	 * 删除公交车绑定
	 * @param hostname
	 * @param key
	 * @param url
	 * @param id 必填参数 该条公交绑定信息的编号
	 * @return Map 状态码 operaStatus String
	 * 							0  删除失败
								1  删除成功
		返回的Map中有status的键，对应的是int型的状态码  100  正常状态，101  用户名或密码错误，102  没有接口权限或接口不存在，103  用户对该接口的当日访问量用完，104  验证程序异常 稍后重试
	 */ 
	public Map delGjcgz(String hostname, String key, String url,String id);
	
	/**
	 * 更新公交车绑定
	 * @param hostname
	 * @param key
	 * @param url
	 * @param id  必填参数   该条公交绑定信息的编号
	 * @param userName   必填参数    用户名
	 * @param xlmc    必填参数   线路名称
	 *  @param fx    必填参数   方向
	 * @param cz  必填参数    站点ID
	 * @param tqtx   必填参数   提前提醒
	 *  @param xq   必填参数   星期
	 * @param sj   必填参数    时间
	 * @param lineid   必填参数   线路的ID
	 * @param czmc   必填参数   站点名称
	 * @param qy   必填参数   是否启用
	  * @return Map 状态码 operaStatus String
	 * 							0  更新失败
								1  更新成功
		返回的Map中有status的键，对应的是int型的状态码  100  正常状态，101  用户名或密码错误，102  没有接口权限或接口不存在，103  用户对该接口的当日访问量用完，104  验证程序异常 稍后重试
	 */
	public Map updateGjcgz(String hostname, String key, String url,String id,String userName,String xlmc,String fx,String cz,String tqtx,String[] xq,String[] sj,String lineid,String czmc,String qy);
	/**
	 * 获取用户公交线路关注列表
	 * @param hostname
	 * @param key
	 * @param url
	 * @param userName
	 * @param xlmc
	 * @param fx
	 * @param cz
	 * @param tqtx
	 * @param qy
	 * @return Map  
	 * 				gjcList   List类型
	 *	返回的Map中有status的键，对应的是int型的状态码  100  正常状态，101  用户名或密码错误，102  没有接口权限或接口不存在，103  用户对该接口的当日访问量用完，104  验证程序异常 稍后重试
	 
	 */
	public Map getGjcgz(String hostname, String key, String url,String userName,String xlmc,String fx,String cz,String tqtx,String qy);

	
	/**
	 * 获取用户绑定车辆信息
	 * @param hostname
	 * @param key
	 * @param url
	 * @param userName
	 * @param bdlb  <可选参数 >  01 驾驶人 ；02机动车
	 * @return	Map 
	 * 				bdfwList   List 类型
	 *	返回的Map中有status的键，对应的是int型的状态码  100  正常状态，101  用户名或密码错误，102  没有接口权限或接口不存在，103  用户对该接口的当日访问量用完，104  验证程序异常 稍后重试
	 */
	public Map getYhclBdxx(String hostname, String key, String url,String userName,String bdlb);
	
	/**
	 * 车辆绑定
	 * @param hostname
	 * @param key
	 * @param url
	 * @param userName
	 * @param hphm  号牌号码
	 * @param clsbdm 车辆识别代码后6位
	 * @param hpzl 	号牌种类
	 * @param bdzds  车辆绑定最大数 <= 该数字
	 * @return Map       状态码 operaStatus String
									0  绑定成功
									1  绑定出错
									2  该账号已绑定相同的号牌
									3  该账号已达到绑定最大数（暂定5个）
									4 该号牌信息不正确

	 *	返回的Map中有status的键，对应的是int型的状态码  100  正常状态，101  用户名或密码错误，102  没有接口权限或接口不存在，103  用户对该接口的当日访问量用完，104  验证程序异常 稍后重试
	 */
	public Map setYhclbdxx(String hostname, String key, String url,String userName,String hphm,String clsbdm,String hpzl,String bdzds);
	/**
	 * 驾驶人绑定
	 * @param hostname
	 * @param key
	 * @param url
	 * @param userName
	 * @param jszh  驾驶证号
	 * @param dabh 	档案编号
	 * @param bdzds  车辆绑定最大数 <= 该数字
	  * @param jsrxm 驾驶人姓名
	  * @return Map       状态码 operaStatus String
									0  绑定成功
									1  绑定出错
									2  该账号已绑定相同的号牌
									3  该账号已达到绑定最大数（暂定5个）

	 *	返回的Map中有status的键，对应的是int型的状态码  100  正常状态，101  用户名或密码错误，102  没有接口权限或接口不存在，103  用户对该接口的当日访问量用完，104  验证程序异常 稍后重试
	 */
	public Map setYhjsrbdxx(String hostname, String key, String url,String userName,String jszh,String dabh,String bdzds,String jsrxm);

	/**
	 * 删除用户绑定信息
	 * @param hostname
	 * @param key
	 * @param url
	 * @param id 绑定表中编号
	 * @return Map  状态码 operaStatus String
	 * 							0  删除成功
	 *	返回的Map中有status的键，对应的是int型的状态码  100  正常状态，101  用户名或密码错误，102  没有接口权限或接口不存在，103  用户对该接口的当日访问量用完，104  验证程序异常 稍后重试
	 */
	public Map delYhbdxx(String hostname, String key, String url,String id);
	
	/**
	 * 班次查询 
	 * @param hostname
	 * @param key
	 * @param url
	 * @param sfzName 始发站
	 * @param zdzName 终点站
	 * @param time    时间点
	 * @return Map 
	 * 					KybcList    List类型
	 *	返回的Map中有status的键，对应的是int型的状态码  100  正常状态，101  用户名或密码错误，102  没有接口权限或接口不存在，103  用户对该接口的当日访问量用完，104  验证程序异常 稍后重试
	 */
	public Map getKybcList(String hostname, String key, String url,String sfzName,String zdzName,String time);
	/**
	 * 自动更新:
	 * @param hostname
	 * @param key
	 * @param url
	 * @param rjlx  软件类型（1、合肥公交；2、交管服务；3、客运服务；4、路况导航）
	 * @param rjbbh 软件版本号
	 * @return Map 
	 * 			
	 * 			bbh    String 版本号
	 * 			addr   String 地址
	 * 			otherInfo   String  其他信息
	 *	返回的Map中有status的键，对应的是int型的状态码  100  正常状态，101  用户名或密码错误，102  没有接口权限或接口不存在，103  用户对该接口的当日访问量用完，104  验证程序异常 稍后重试
	  * 
	 */
	public Map getAutoUpdateInfo(String hostname, String key, String url,String rjlx,String rjbbh);

}



