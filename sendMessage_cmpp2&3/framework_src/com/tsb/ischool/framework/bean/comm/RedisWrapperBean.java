package com.tsb.ischool.framework.bean.comm;

import java.io.Serializable;

public class RedisWrapperBean implements Serializable{

	private Object bean;

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

	@Override
	public String toString() {
		return "RedisWrapperBean [bean=" + bean + "]";
	}

	public RedisWrapperBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
