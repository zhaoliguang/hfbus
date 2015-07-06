package com.keli.hfbus.hessianserver;
/**
 * 请求回调接口
 * @author shaolong
 *
 */
public interface IRequest {
	
	/**
	 * 如果hessian调用的void方法,则doCall需要返回此对象
	 */
	public String HessianVoid = "RequestImpl_HessianVoid_NotNeedReturn";
	
	/**
	 * 请求Loading,显示加载提示
	 */
	public void doLoading();
	
	/**
	 * hessian调用,必须实现
	 */
	public Object doCall();
	
	/**
	 * 请求完成,回发处理
	 * @param o hessian直接返回的对象,需要自己转换
	 */
	public void doComplete(Object o);
	
	/**
	 * 请求超时
	 */
	public void doTimeout();
	
	/**
	 * 请求出错
	 * @param e 报错
	 */
	public void doError(Exception e);
}
