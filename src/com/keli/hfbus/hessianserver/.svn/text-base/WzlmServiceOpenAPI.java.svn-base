package com.keli.hfbus.hessianserver;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * 公交线路改线通知接口
 * @author LiuGuangKe
 *
 */
public interface WzlmServiceOpenAPI {
	
	/**
	 * 获取公交线路改线通知内容查询
	 * @param hostname 主机名称
	 * @param key 服务密匙
	 * @param url 访问的服务的URL
	 * @param selectPage 选择页 <必填参数> 当设为-1时则获取的是没有进行分页的数据，若需分页则设置为所选择的页数
	 * @param pageSize 每页大小 <必填参数> 当设置为-1是则使用服务默认的pageSize，若需自定义每页记录条数则需自行注意每次设置的统一性
	 * 注：1、如需获取分页数据则必须注意selectPage和pageSize的控制，0<=selectPage<=pageSize是能获取正确的分页数据。
	 * 
	 * @return Map 1、如果获取的是不分页的数据则Map中将包含一个键为wzxxList的包含满足条件的机动车基本的List;
	 * 			   2、如果获取的是分页数据则Map中包含一个数据如下：键				值
	 * 														wzxxList		指定页满足条件的List
	 * 														itemCount		总记录条数
	 * 														pageCount		总页数
	 * 														selectPage		选择页
	 * 														pageSize		页大小
	 * 			   3、返回的Map中有status的键，对应的是int型的状态码  100  正常状态，101  用户名或密码错误，102  没有接口权限或接口不存在，103  用户对该接口的当日访问量用完，104  验证程序异常 稍后重试
	 */
	public Map findGjgxtzWzxxList(String hostname, String key, String url,int selectPage,int pageSize);

	
	/**
	 * 根据文章id获取文章信息
	 * @param hostname 主机名称
	 * @param key 服务密匙
	 * @param url 访问的服务的URL
	 * @param id <必填参数>
	 * @return Map   
	 * Map中包含一个数据如下：键				值
	 * 					wzxxList		满足条件的List
	 *  返回的Map中有status的键，对应的是int型的状态码  100  正常状态，101  用户名或密码错误，102  没有接口权限或接口不存在，103  用户对该接口的当日访问量用完，104  验证程序异常 稍后重试
	 
	 */
	public Map findWzxxById(String hostname, String key, String url,String id);
}
