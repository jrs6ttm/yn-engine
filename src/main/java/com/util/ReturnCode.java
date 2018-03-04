package com.util;

import java.io.Serializable;

public class ReturnCode implements Serializable{
	
	private static final long serialVersionUID = 957672821467415262L;
	
	public static final Integer ERROR=3; //服务错误
	public static final Integer LOGIN=2; //登录提示
	public static final Integer SUCCESS=1; //请求成功
	public static final Integer FAIL=0;  //请求失败
	
	private Integer code;
	private String msg;
	private Object data;
	
	public ReturnCode(){}
	
	public ReturnCode(Integer code,String msg){
		super();
		this.code=code;
		this.msg=msg;
	}
	
	public ReturnCode(Integer code, String msg, Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
