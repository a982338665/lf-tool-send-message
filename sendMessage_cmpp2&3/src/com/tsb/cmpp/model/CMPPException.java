package com.tsb.cmpp.model;

import java.io.IOException;


/**
 * cmpp建立和关闭连接时候封装IOException
 * @author David Yin
 * @version 1.0 (2013-11-21 15:20:45)
 */
public class CMPPException extends IOException {
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	
	private static final long serialVersionUID = 1L;

	public CMPPException() {
        super();
    }

    public CMPPException(String msg) {
        super(msg);
    }
}