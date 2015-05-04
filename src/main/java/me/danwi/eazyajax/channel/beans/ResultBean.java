package me.danwi.eazyajax.channel.beans;

/**
 * 方法调用结果Bean
 * @author Demon
 *
 */
public class ResultBean {
    //这两者有且只有一个为null,有返回值就表示没异常,出异常就得不到返回值,很容易理解
    //方法调用的返回值
	private Object returnValue = null;
    //方法调用中捕获的异常
	private Exception exception = null;

    //一堆getter和setter
	public Object getReturnValue() {
		return returnValue;
	}
	public void setReturnValue(Object returnObject) {
		this.returnValue = returnObject;
	}
	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
}
